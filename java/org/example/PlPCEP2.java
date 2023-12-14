package org.example;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class PlPCEP2 {


    public static void main(String[] args) throws Exception {


        //initializeEnvironment();
        int randomPatternSetSize=8;
        int randomPublicSize = 6;
        int randomPrivateSize = 2;

        // Set up the Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);



        //read the input file and put it in a list
        String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/";

        List<StockPattern> patternSet = ExtractPatterns(filePath,"MostFrequentPatterns.txt");
        List<ParallelEvents> parallelDependencySet = ExtractParallelDependencies(filePath,"MostFrequentParallelDependencies.txt");
        List<PeriodicEvent> periodicDependencySet = ExtractPeriodicEvents(filePath,"AllPeriodic.txt");

        //read previously selected patterns
        List<Integer> patternIndices = readIndices(filePath+"patternIndexes.txt");

        List<DataEvent> events = readDataSet(filePath,"data.csv");
        List<DataEvent> sample = events.subList(0,10);

        //Modifying the original event stream to obfuscate private patterns
        //List<List<PatternMatch>> allPrivateMatches = new ArrayList<>();
        List<PatternMatch> privatePatternMatches = new ArrayList<>();
        //PatternMatch match = new PatternMatch();

        //extracting the matches of each private pattern
        for (int i=0;i<randomPrivateSize;i++){
            List<String> lines = scanFile(filePath+"output "+i+".txt");
            //System.out.println("scanning file  : "+"output "+i+".txt");
            //System.out.println("number of pattern matches in "+i+" is : "+lines.size());

            for (String line:lines  ) {

                privatePatternMatches.add(new PatternMatch(line));

            }
        }

        List<String> eventsInString = new ArrayList<>();
        for (DataEvent de:events){
            eventsInString.add(de.toString());
        }

            for (PatternMatch pm:privatePatternMatches ) {
                eventsInString.remove(pm.getEventList().get(0).toString());

            }

            List<DataEvent> modifiedEvents = new ArrayList<>();
        for (String se:eventsInString){
            //System.out.println("event: "+se);
            modifiedEvents.add(new DataEvent(se,""));
        }


/*
        List<String> customerIDs = new ArrayList<>();
        for (DataEvent de:events){
            customerIDs.add(de.getCustomerID());
        }

 */

        List<String> stockCodes = new ArrayList<>();
        for (DataEvent de:events){
            stockCodes.add(de.getStockCode());
        }


        DataStream<DataEvent> eventStream = env.fromCollection(modifiedEvents);

        List<Pattern<DataEvent, ?>> randomPatterns = new ArrayList<>();
        for (int i = 0; i < patternIndices.size(); i++) {
            //System.out.println("pattern: "+patternIndices.get(i));
            randomPatterns.add(GenerateCEPPattern(patternIndices.get(i)-1,patternSet));
        }



        //public pattern set
        List<Pattern<DataEvent, ?>> randomPublicPatterns = new ArrayList<>();
        for (int j=0; j<randomPublicSize; j++){
            randomPublicPatterns.add(randomPatterns.get(j));

        }



        //private pattern set
        List<Pattern<DataEvent, ?>> randomPrivatePatterns = new ArrayList<>();
        for (int j=randomPatternSetSize; j>randomPatternSetSize-randomPrivateSize; j--){
            randomPrivatePatterns.add(randomPatterns.get(j-1));
        }


        for (int num=0;num<randomPrivatePatterns.size();num++) {
            // Create pattern streams using the defined patterns
            PatternStream<DataEvent> privatePatternStream = CEP
                    .pattern(eventStream, randomPatterns.get(num))
                    .inProcessingTime();
            //detecting the private pattern  over the input stream
            DataStream<List<DataEvent>> results = privatePatternStream.process(new PatternProcessFunction<DataEvent, List<DataEvent>>() {
                @Override
                public void processMatch(
                        Map<String, List<DataEvent>> map,
                        Context context,
                        Collector<List<DataEvent>> collector) throws Exception {

                    List<DataEvent> list = new ArrayList<>();
                    list.add(map.get("start").get(0));
                    list.add(map.get("middle").get(0));
                    list.add(map.get("end").get(0));


                    collector.collect(list);


                }
            });


            results
                    .writeAsText(
                            filePath + "output " + num + "After.txt",
                            FileSystem.WriteMode.OVERWRITE)
                    .setParallelism(1);  // ensure that events are written in order;

        }
/*
        
        //public pattern set
        List<Pattern<DataEvent, ?>> randomPrivatePatterns = new ArrayList<>();
        for (int j=randomPatternSetSize; j>randomPatternSetSize-randomPrivateSize; j--){
            randomPrivatePatterns.add(randomPatterns.get(j-1));
        }


        //detecting private patterns
        //System.out.println("size: "+randomPrivatePatterns.size());

        for (int num=0;num<randomPrivatePatterns.size();num++){
            // Create pattern streams using the defined patterns
            PatternStream<DataEvent> privatePatternStream = CEP.pattern(eventStream, randomPatterns.get(num)).inProcessingTime();
            //detecting the private pattern  over the input stream
            DataStream<DataEvent> results = privatePatternStream.process(new PatternProcessFunction<DataEvent, DataEvent>() {
                @Override
                public void processMatch(Map<String, List<DataEvent>> map, Context context, Collector<DataEvent> collector) throws Exception {

                    //List<DataEvent> list = new ArrayList<>();
                    //list.add(map.get("start").get(0));
                    //list.add(map.get("middle").get(0));
                    //list.add(map.get("end").get(0));




                    collector.collect(map.get("start").get(0));


                }
            });


            results.writeAsText(filePath+"output "+num+".txt", FileSystem.WriteMode.OVERWRITE)
                    .setParallelism(1);  // ensure that events are written in order;

/*
            List<DataEvent> drops = readEventFile(filePath,"output "+num+".txt");
            System.out.println("drop size: "+drops.size());

            for (DataEvent d:drops
                 ) {
                System.out.println(d.toString());
            }

            events.removeAll(drops);
            DataStream<DataEvent> newEventStream= env.fromCollection(events);

            PatternStream<DataEvent> publicPatternStreamAfterDrop = CEP.pattern(newEventStream, randomPatterns.get(num)).inProcessingTime();
            //detecting the private pattern  over the input stream
            DataStream<DataEvent> resultsAfterDrop = publicPatternStreamAfterDrop.process(new PatternProcessFunction<DataEvent, DataEvent>() {
                @Override
                public void processMatch(Map<String, List<DataEvent>> map, Context context, Collector<DataEvent> collector) throws Exception {

                    //List<DataEvent> list = new ArrayList<>();
                    //list.add(map.get("start").get(0));
                    //list.add(map.get("middle").get(0));
                    //list.add(map.get("end").get(0));




                    collector.collect(map.get("start").get(0));


                }
            });


            resultsAfterDrop.writeAsText(filePath+"output "+num+"After.txt", FileSystem.WriteMode.OVERWRITE)
                    .setParallelism(1);  // ensure that events are written in order;



        }

 */








        env.execute("PlP-CEP");


    }





    private static List<Integer> readIndices(String path) {
        List<Integer> indices= new ArrayList<>();
        List<String> lines = scanFile(path);
        for (String line :lines) {
            indices.add(Integer.parseInt(line));
        }
        return indices;
    }

    private static void writeListToFile(List<String> patternSetIndexes, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String index : patternSetIndexes) {
                // Write each element of the list
                // to a new line in the output.txt file
                bw.write(index);
                bw.newLine();
            }
        } catch (IOException e) {
            // Print the stack trace
            // if an IO exception occurs
            e.printStackTrace();
        }
    }

    private static List<DataEvent> readEventFile(String filePath, String filename) {
        List<DataEvent> eventList = new ArrayList<>();

        List<String> lines = scanFile(filePath+ filename);

        for (String readLine : lines) {

            ArrayList<AttributeValue> al = new ArrayList<>();
            String[] parts = readLine.split(",");
            for (String part :parts) {
                AttributeValue av = new AttributeValue();
                String[] attval = part.split(";");
                av.setAttributeName(attval[0]);
                av.setAttributeValue(attval[1]);
                al.add(av);
            }

            eventList.add(new DataEvent(al));

        }
        return eventList;

    }

    private static List<String> scanFile(String path) {
        List<String> lines = new ArrayList<>();


        Scanner fileScanner = null;//Initialize fileScanner

        try
        {
            fileScanner = new Scanner(new File(path));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        while(fileScanner.hasNextLine())
        {
            String fileLine = fileScanner.nextLine();

            lines.add(fileLine);
        }
        fileScanner.close();
        return lines;
    }

    private static Pattern<DataEvent,?> GenerateCEPPattern(Integer randomNumber, List<StockPattern> patternSet) {
        StockPattern sp = patternSet.get(randomNumber);
        Pattern<DataEvent, ?> pattern = Pattern.<DataEvent>begin("start")
                .where(new SimpleCondition<DataEvent>() {
                    @Override
                    public boolean filter(DataEvent dataEvent) throws Exception {
                        //System.out.println("within pattern");
                        return dataEvent.getStockCode().equals(sp.getEventStockIDs().get(0));
                    }
                }).next("middle")
                .where(new SimpleCondition<DataEvent>() {
                    @Override
                    public boolean filter(DataEvent dataEvent) throws Exception {
                        return dataEvent.getStockCode().equals(sp.getEventStockIDs().get(1));
                    }

                }).next("end")
                .where(new SimpleCondition<DataEvent>() {
                    @Override
                    public boolean filter(DataEvent dataEvent) throws Exception {
                        return dataEvent.getStockCode().equals(sp.getEventStockIDs().get(2));
                    }

                });
        return pattern;
    }

    private static List<DataEvent> readDataSet(String filePath, String filename) {
        List<DataEvent> eventList = new ArrayList<>();

        List<String> lines = new ArrayList<>();
        int lineCount=0;

        Scanner fileScanner = null;//Initialize fileScanner

        try
        {
            fileScanner = new Scanner(new File(filePath+filename));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        while(fileScanner.hasNextLine())
        {
            String fileLine = fileScanner.nextLine();
            if (lineCount == 200000)
                break;
            lines.add(fileLine);
            lineCount++;
        }
        fileScanner.close();

        for (String readLine : lines) {
            //System.out.println("first line: "+readLine);

            DataEvent de = new DataEvent(readLine);
            //System.out.println("line "+readLine);
            //System.out.println("event : "+de.toString());
            eventList.add(de);

        }
        return eventList;

    }


    private static List<PeriodicEvent> ExtractPeriodicEvents(String filePath, String filename) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath+filename));
        List<PeriodicEvent> periodicEventList = new ArrayList<>();

        for (String line:lines){
            PeriodicEvent pe = new PeriodicEvent();
            String[] lineParts= line.split(":");
            pe.setFrequency(Integer.parseInt(lineParts[1]));

            pe.setEventStockCode(lineParts[0].substring(1,lineParts[0].length()-1));

            periodicEventList.add(pe);
        }

        return periodicEventList;
    }

    private static List<ParallelEvents> ExtractParallelDependencies(String filePath, String filename) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath+filename));
        List<ParallelEvents> parallelEventsList = new ArrayList<>();

        for (String line:lines){
            List<String> parallelEventsstockList = new ArrayList<>();
            ParallelEvents p = new ParallelEvents();
            String[] lineParts= line.split(":");
            p.setFrequency(Integer.parseInt(lineParts[1]));

            String[] stockStringParts=lineParts[0].split(",");
            parallelEventsstockList.add(stockStringParts[0].substring(1, stockStringParts[0].length()));
            parallelEventsstockList.add(stockStringParts[1].substring(0, stockStringParts[1].length()-1));

            p.setEventIDs(parallelEventsstockList);
            parallelEventsList.add(p);
        }

        return parallelEventsList;
    }

    private static List<StockPattern> ExtractPatterns(String filePath, String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath+filename));
        List<StockPattern> stockPatternsList = new ArrayList<>();

        for (String line:lines){
            List<String> stockList = new ArrayList<>();
            StockPattern sp = new StockPattern();
            String[] lineParts= line.split(":");
            sp.setFrequency(Integer.parseInt(lineParts[1]));

            String[] stockStringParts=lineParts[0].split(",");
            stockList.add(stockStringParts[0].substring(1, stockStringParts[0].length()));
            stockList.add(stockStringParts[1].substring(1, stockStringParts[1].length()));
            stockList.add(stockStringParts[2].substring(1, stockStringParts[2].length()-1));

            sp.setEventStockIDs(stockList);
            stockPatternsList.add(sp);
        }

        return stockPatternsList;
    }


}
