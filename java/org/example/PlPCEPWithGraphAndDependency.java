package org.example;

import javafx.beans.binding.When;

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


public class PlPCEPWithGraphAndDependency {



    public static boolean zeroOutDegree = true;
    public static ArrayList<String> OTModels = new ArrayList<>();
    public static void main(String[] args) throws Exception {



        OTModels.add("Drop(e1)");
        OTModels.add("Drop(e2)");
        OTModels.add("Drop(e3)");
        OTModels.add("Reorder(e1,e2)");
        OTModels.add("Reorder(e1,e3)");
        OTModels.add("Reorder(e2,e3)");
        OTModels.add("Tamper(e1)");
        OTModels.add("Tamper(e2)");
        OTModels.add("Tamper(e3)");
        OTModels.add("Injection(e,e1,e2)");
        OTModels.add("Injection(e,e2,e3)");
        OTModels.add("Generalization(e1)");
        OTModels.add("Generalization(e2)");
        OTModels.add("Generalization(e3)");



        //initializeEnvironment();
        int randomPatternSetSize=18;
        int randomPublicSize = 15;
        int randomPrivateSize = 3;

        //testcase
        //int randomPatternSetSize=4;
        //int randomPublicSize = 2;
        //int randomPrivateSize = 2;

        // Set up the Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);



        //read the input file and put it in a list
        String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/";

        //testcase
        //String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/testCase/";


        List<StockPattern> patternSet = ExtractPatterns(filePath,"MostFrequentPatterns.txt");
        List<ParallelEvents> parallelDependencySet = ExtractParallelDependencies(filePath,"MostFrequentParallelDependencies.txt");
        List<PeriodicEvent> periodicDependencySet = ExtractPeriodicEvents(filePath,"AllPeriodic.txt");

        //read previously selected patterns
        List<Integer> patternIndices = readIndices(filePath+"patternIndexes.txt");
        System.out.println("indices: "+patternIndices.toString());


        List<StockPattern> randomPatternSet=new ArrayList<>();
        for (int index:patternIndices) {
            randomPatternSet.add(patternSet.get(index-1));

        }

        //read dependencies
        List<Integer> parallelDependencyIndexes =  readIndices(filePath+"parallelDependenciesIndexes.txt");
        List<ParallelEvents> parallelDependencies = new ArrayList<>();
        for (int index: parallelDependencyIndexes){
            System.out.println("parallel: "+ parallelDependencySet.get(index-1).toString());
            parallelDependencies.add(parallelDependencySet.get(index-1));
        }

        List<Integer> periodicDependencyIndexes =  readIndices(filePath+"periodicEventsIndexes.txt");
        List<PeriodicEvent> periodicDependencies = new ArrayList<>();
        for (int index: periodicDependencyIndexes){
            System.out.println("periodic: "+periodicDependencySet.get(index-1).toString());
            periodicDependencies.add(periodicDependencySet.get(index-1));
        }

        List<DataEvent> events = readDataSet(filePath,"data.csv");
        List<DataEvent> sample = events.subList(0,12000);

        //testcase
        //List<DataEvent> events = readDataSet(filePath,"data.txt");


        //Modifying the original event stream to obfuscate private patterns
        //List<List<PatternMatch>> allPrivateMatches = new ArrayList<>();
        Map<Integer,List<PatternMatch>> privatePatternMatches = new HashMap<>();
        //PatternMatch match = new PatternMatch();

        //extracting the matches of each private pattern
        for (int i=0;i<randomPrivateSize;i++){

            List<String> lines = scanFile(filePath+"output "+i+".txt");
            //System.out.println("scanning file  : "+"output "+i+".txt");
            //System.out.println("number of pattern matches in "+i+" is : "+lines.size());

            privatePatternMatches.put(patternIndices.get(i), new LinkedList<>() );

            for (int j=0; j<lines.size();j++) {
                privatePatternMatches.get(patternIndices.get(i)).add(new PatternMatch(lines.get(j)));

            }
        }

        LinkedList<String> modifiedEventsInString = new LinkedList<>();

        //LinkedList<String> eventsInString = new LinkedList<>();
        for (DataEvent de:sample){
            modifiedEventsInString.add(de.toString());
        }


        //detecting revealed private patterns by adversaries background knowledge
        int sumOfRevealedPrivatePatterns = 0;


        List<DependencyGraph> subGraphs=new ArrayList<>();
        for (String ot:OTModels) {
            subGraphs.add(new DependencyGraph(ot,randomPatternSet,randomPrivateSize));
        }

        List<String> periodicEventsInString = new ArrayList<>();
        for (PeriodicEvent pe:periodicDependencies){
            periodicEventsInString.add(pe.getEventStockCode());
        }

        //HashMap<String, LinkedList<String>> modifiedStreams= new HashMap<>();
        //HashMap<String, List<DataEvent>> modifiedEventStreams = new HashMap<>();
        //System.out.println("subgraph size: "+subGraphs.size());

        //selecting ot for each private pattern
        for (int i =0; i< randomPrivateSize;i++){
            //modifiedStreams.put(String.valueOf(patternIndices.get(i)), new LinkedList<>());
            //modifiedEventStreams.put(String.valueOf(patternIndices.get(i)), new LinkedList<>());

            zeroOutDegree=true;
            StockPattern privatePattern=patternSet.get(patternIndices.get(i)-1);
            privatePattern.setPatternID(patternIndices.get(i));

            List<DependencyGraph> graphList = new ArrayList<>();
            for (DependencyGraph g:subGraphs) {
                graphList.add(g);
            }
            privatePattern.setUpdatedSubGraphList(graphList);

            //System.out.println("size sub graph at the start: "+privatePattern.getUpdatedSubGraphList().size());
            System.out.println("pattern: "+privatePattern.getEventStockIDs().toString());
            //for (int j=0;j<privatePatternMatches.get(patternIndices.get(i)).size();j++){
                //System.out.println("match "+(j+1)+" for pattern "+patternIndices.get(i)+" is : "+privatePatternMatches.get(patternIndices.get(i)).get(j).toString());
           // }


            //String selectedOT ="";
            DependencyGraph dg= new DependencyGraph();
            boolean dependenciesConsidered = true;

            //while loop for selecting ot until not violating dependencies
            while (dependenciesConsidered && zeroOutDegree){
                //System.out.println("ot in start: "+ots);
                privatePattern.setSelectedOT(dg.FindZeroOutDegree(OTModels,privatePattern));
                //selectedOT = dg.FindZeroOutDegree(OTModels,privatePattern);
                boolean flag = false;


                for (ParallelEvents pe:parallelDependencies){
                    //System.out.println("parallel: "+pe.getEventIDs().toString());
                    //System.out.println("drop1: "+privatePattern.getEventStockIDs().get(1));
                    //System.out.println("contains: "+pe.containsStockCode(privatePattern.getEventStockIDs().get(1)));
                    if ((privatePattern.getSelectedOT().equals("Drop(e1)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                            || (privatePattern.getSelectedOT().equals("Drop(e2)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                            || (privatePattern.getSelectedOT().equals("Drop(e3)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))
                            ||(privatePattern.getSelectedOT().equals("Tamper(e1)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                            || (privatePattern.getSelectedOT().equals("Tamper(e2)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                            || (privatePattern.getSelectedOT().equals("Tamper(e3)") &&
                            (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))){

                        //System.out.println("size of pattern match for pattern "+patternIndices.get(i)+" is : "+privatePatternMatches.get(patternIndices.get(i)).size());


                        flag = true;
                    }
                }
                if (!flag){
                    dependenciesConsidered = false;
                }else {
                    System.out.println("Graph with Dependency: OT model "+privatePattern.getSelectedOT()+" violates dependencies!");
                    DependencyGraph g1=new DependencyGraph();
                    for (DependencyGraph g:privatePattern.getUpdatedSubGraphList()){
                        if (g.getOtModel().equals(privatePattern.getSelectedOT())){
                            g1=g;
                        }
                    }
                    privatePattern.getUpdatedSubGraphList().remove(g1);
                    //System.out.println("update subgraph size: "+privatePattern.getUpdatedSubGraphList().size() );
                    //System.out.println("ots are : "+ots);
                }
            }

            //calculating the revealed private pattern by adversary background knowledge
            boolean flag = false;

            //calculating sum of private pattern matches for violations
            for (ParallelEvents pe:parallelDependencies){
                //System.out.println("parallel: "+pe.getEventIDs().toString());
                //System.out.println("drop1: "+privatePattern.getEventStockIDs().get(1));
                //System.out.println("contains: "+pe.containsStockCode(privatePattern.getEventStockIDs().get(1)));
                if ((privatePattern.getSelectedOT().equals("Drop(e1)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                    || (privatePattern.getSelectedOT().equals("Drop(e2)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                    || (privatePattern.getSelectedOT().equals("Drop(e3)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))
                    ||(privatePattern.getSelectedOT().equals("Tamper(e1)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                    || (privatePattern.getSelectedOT().equals("Tamper(e2)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                    || (privatePattern.getSelectedOT().equals("Tamper(e3)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2)) || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))){

                    //System.out.println("size of pattern match for pattern "+patternIndices.get(i)+" is : "+privatePatternMatches.get(patternIndices.get(i)).size());
                    flag = true;
                }
            }
            if (flag){
                for (PatternMatch p:privatePatternMatches.get(patternIndices.get(i))) {

                    sumOfRevealedPrivatePatterns++;
                }
            }

            System.out.println("OT for pattern "+patternIndices.get(i)+" is selected: "+privatePattern.getSelectedOT());
            modifiedEventsInString = dg.EnforcingOTModel(modifiedEventsInString, privatePattern, privatePatternMatches.get(patternIndices.get(i)));
            System.out.println("modified: "+modifiedEventsInString.size());
            //System.out.println("size of modified stream for pattern "+patternIndices.get(i)+" : "+modifiedEventsInString.size());


            //for (String m:modifiedEventsInString){
                //modifiedStreams.get(String.valueOf(patternIndices.get(i))).add(m);
            //}
            //System.out.println("size of modified stream for pattern "+patternIndices.get(i)+" in hashmap : "+modifiedStreams.get(String.valueOf(patternSet.get(patternIndices.get(i))).size());

        }

        System.out.println("Revealed patterns: "+sumOfRevealedPrivatePatterns);

        //List<DataEvent> modifiedEvents = new ArrayList<>();
        //System.out.println("size :"+modifiedEventsInString.size());

        //converting modifies streams from string to data event (modifiedStreams --> modifiedEventStreams)
        /*for (int i=0;i<modifiedStreams.keySet().size();i++){
            String patternID= String.valueOf(patternIndices.get(i));
            for(int j=0;j<modifiedStreams.get(patternID).size();j++){
                String se= modifiedStreams.get(patternID).get(j);
                modifiedEventStreams.get(patternID).add(new DataEvent(se,""));
            }
            //System.out.println("event: "+se);
            //modifiedEventStreams.get(String.valueOf(patternSet.get(patternIndices.get(i))), new LinkedList<>()).add(new DataEvent(se,""));
        }

         */

        List<DataEvent> modifiedEvents = new ArrayList<>();
        for (String se:modifiedEventsInString){
            //System.out.println("event: "+se);
            modifiedEvents.add(new DataEvent(se,""));
        }




        //LinkedList<DataEvent> lll = new LinkedList<>();

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


        //recheck the pattern matches for all pattern for modified streams by each private pattern selected OT
        //for (int prv=0;prv<randomPrivateSize;prv++){
            //String privatePatternID=String.valueOf(patternIndices.get(prv));
            //System.out.println("id : "+privatePatternID+" and size of stream : "+modifiedEventStreams.get(privatePatternID).size());

        for (int num=0;num<randomPatternSetSize;num++) {
                System.out.println("pattern "+(patternIndices.get(num))+" : "+patternSet.get(patternIndices.get(num)-1).getEventStockIDs().toString());
                // Create pattern streams using the defined patterns
                DataStream<DataEvent> stream = env.fromCollection(modifiedEvents);
                PatternStream<DataEvent> privatePatternStream = CEP
                        .pattern(stream, randomPatterns.get(num))
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
                                filePath + "PlPCEP/output "+num+"After PlP-CEP.txt",
                                FileSystem.WriteMode.OVERWRITE)
                        .setParallelism(1);  // ensure that events are written in order;

            }



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
            parallelEventsstockList.add(stockStringParts[1].substring(1, stockStringParts[1].length()-1));

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
