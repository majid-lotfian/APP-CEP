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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class PlPCEPWindow {


    public static void main(String[] args) throws Exception {


        //initialization


        ArrayList<String> OTModels = new ArrayList<>();
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
        //OTModels.add("Generalization(e1)");
        //OTModels.add("Generalization(e2)");
        //OTModels.add("Generalization(e3)");

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


        List<DataEvent> events = readDataSet(filePath,"data.csv");

        //testcase
        //List<DataEvent> events = readDataSet(filePath,"data.txt");

        //sample collection
        List<DataEvent> sample = events.subList(0,12000);

/*
        List<String> customerIDs = new ArrayList<>();
        for (DataEvent de:events){
            customerIDs.add(de.getCustomerID());
        }

 */




        //DataStream<DataEvent> eventStream = env.fromCollection(events);

        /*
        eventStream.writeAsText(filePath+"output.txt", FileSystem.WriteMode.OVERWRITE)
                .setParallelism(1);  // ensure that events are written in order;

         */

        // selecting a random pattern from the set for the public pattern
        //Random rand = new Random();
        // Setting the upper bound to generate the
        // random numbers in specific range
        //int upperbound = 197;

        //testcase
        //int upperbound = patternSet.size();


        // Generating random values from 0 - 197
        // using nextInt()

        int randomPatternSetSize=18;
        int randomPublicSize = 15;
        int randomPrivateSize = 3;


        //using sample set
        LinkedList<String> eventsInString = new LinkedList<>();
        for (DataEvent de:sample){
            eventsInString.add(de.toString());
        }

        //testcase
        //int randomPatternSetSize=4;
        //int randomPublicSize = 2;
        //int randomPrivateSize = 2;


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



        List<Integer> patternIndices = readIndices(filePath+"patternIndexes.txt");
        System.out.println("indices: "+patternIndices.toString());


        List<StockPattern> randomPatternSet=new ArrayList<>();
        for (int index:patternIndices) {
            randomPatternSet.add(patternSet.get(index-1));

        }



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



        //detecting private patterns
        //System.out.println("size: "+randomPrivatePatterns.size());

        //reading pattern matches
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




        //starting windowing process

        HashMap<Integer, List<String>> windowedDataSet = new HashMap<>();

        int windowSize=4000;

        //generating windows of size "windowSize"
        for (int i=0;i<eventsInString.size()/windowSize;i++){
            windowedDataSet.put(i, new LinkedList<>());
            for (int j=0;j<windowSize;j++){
                windowedDataSet.get(i).add(eventsInString.get(i*windowSize+j));
            }
        }
        //for each private pattern
        for (int privatePatternCounter = 0; privatePatternCounter
                <randomPrivateSize; privatePatternCounter++){
            StockPattern privatePattern=patternSet.get(patternIndices.get(privatePatternCounter)-1);
            System.out.println("private pattern: "+privatePattern.getEventStockIDs());
            //writing sum of all pattern matches for each pattern in a file
            writeStringToFile(String.valueOf(privatePatternMatches.get(patternIndices.get(privatePatternCounter)).size()),filePath+
                    "/WindowBased/privatePattern"+privatePatternCounter+"/sumOfMatchesWithoutWindowing.txt");


            //for each window
            for (int windowCounter = 0; windowCounter
                    <windowedDataSet.keySet().size(); windowCounter++){
                System.out.println("for window "+windowCounter);
                //generate the modified list of events for each window for each OT
                for (String ot:OTModels){
                    int actualPrivateMatches =0;

                    privatePattern.setModifiedEventsForOTs(new HashMap<>());
                    privatePattern.setModifiedEventsInStringForOTs(new HashMap<>());
                    privatePattern.getModifiedEventsForOTs().put(ot, new LinkedList<>());
                    privatePattern.getModifiedEventsInStringForOTs().put(ot, new LinkedList<>());
                    //System.out.println("for OT: "+ot);
                    //put the original window events in the list
                    for (int eventCounter = 0; eventCounter <windowSize; eventCounter++){
                        privatePattern.getModifiedEventsInStringForOTs().get(ot).add(windowedDataSet.get(
                                windowCounter).get(eventCounter));
                        //System.out.println("event "+ eventCounter +" added to modified list!");
                    }
                    //System.out.println("size of the list at the start for pattern "+patternIndices.get(
                            //privatePatternCounter)+" : "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());
                    for (PatternMatch patternMatch:privatePatternMatches.get(patternIndices.get(
                            privatePatternCounter))){
                        if (windowedDataSet.get(windowCounter).contains(patternMatch.getEventList().get(2).toString())
                                && privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(2).toString())>1){

                            actualPrivateMatches++;
                            if (ot.equals("Drop(e1)")){

                                //boolean eventListModified =
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(0).toString());
                                //if (eventListModified){
                                   // System.out.println("event removed!");
                               // }

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Drop(e2)")){

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(1).toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Drop(e3)")){

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(2).toString());


                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Reorder(e1,e2)")){

                                int indexE0 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(0).toString());
                                int indexE1 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(1).toString());
                                System.out.println("index e1 in reorder 1,2"+indexE1);

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(1).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE1,
                                        patternMatch.getEventList().get(0).toString());

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(0).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE0,
                                        patternMatch.getEventList().get(1).toString());


                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Reorder(e2,e3)")){

                                int indexE1 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(1).toString());
                                int indexE2 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(2).toString());

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(2).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE2,
                                        patternMatch.getEventList().get(1).toString());

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(1).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE1,
                                        patternMatch.getEventList().get(2).toString());


                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Reorder(e1,e3)")){

                                int indexE0 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(0).toString());
                                int indexE2 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(2).toString());

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(2).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE2,
                                        patternMatch.getEventList().get(0).toString());

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(0).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE0,
                                        patternMatch.getEventList().get(2).toString());


                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Tamper(e1)")){
                                DataEvent de = patternMatch.getEventList().get(0);

                                int indexE0 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(0).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(0).toString());
                                de.replaceStockCode("111111");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE0,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Tamper(e2)")){
                                DataEvent de = patternMatch.getEventList().get(1);

                                int indexE1 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(1).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(1).toString());
                                de.replaceStockCode("22222");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE1,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Tamper(e3)")){
                                DataEvent de = patternMatch.getEventList().get(2);

                                int indexE2 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(2).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(2).toString());
                                de.replaceStockCode("3333333");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE2,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Injection(e,e1,e2)")){
                                DataEvent de = patternMatch.getEventList().get(0);


                                int indexE0= privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(0).toString());

                                de.setStockCode("iiiiiiii");

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add((indexE0+1),
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Injection(e,e2,e3)")){
                                DataEvent de = patternMatch.getEventList().get(1);


                                int indexE1= privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(1).toString());

                                de.setStockCode("iiiiiiii");

                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add((indexE1+1),
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Generalization(e1)")){
                                DataEvent de = patternMatch.getEventList().get(0);

                                int indexE0 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(0).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(0).toString());
                                de.replaceStockCode("general");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE0,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Generalization(e2)")){
                                DataEvent de = patternMatch.getEventList().get(1);

                                int indexE1 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(1).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(1).toString());
                                de.replaceStockCode("general");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE1,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }
                            if (ot.equals("Generalization(e3)")){
                                DataEvent de = patternMatch.getEventList().get(2);

                                int indexE2 = privatePattern.getModifiedEventsInStringForOTs().get(ot).indexOf(patternMatch.getEventList().get(2).toString());
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).remove(
                                        patternMatch.getEventList().get(2).toString());
                                de.replaceStockCode("general");
                                //System.out.println("tamper: "+de);
                                privatePattern.getModifiedEventsInStringForOTs().get(ot).add(indexE2,
                                        de.toString());

                                //System.out.println("event list size in "+ot+" in private pattern "+patternIndices.get(privatePatternCounter)+": "+privatePattern.getModifiedEventsInStringForOTs().get(ot).size());

                            }

                        }
                    }
                    //System.out.println("actual matches are: "+actualMatches);
                    for (int k=0;k<privatePattern.getModifiedEventsInStringForOTs().get(ot).size();k++){
                        privatePattern.getModifiedEventsForOTs().get(ot).add(new DataEvent(privatePattern.getModifiedEventsInStringForOTs().get(ot).get(k),""));
                    }

                    //generating the results of detecting all patterns for each OT model in this window using modified event lists
                    for (int patternCounter=0; patternCounter<randomPatternSetSize;patternCounter++){
                        //System.out.println("pattern id: "+patternIndices.get(patternCounter));
                        //StockPattern pattern=patternSet.get(patternIndices.get(patternCounter)-1);
                        //System.out.println("Rechecking the results for pattern: "+pattern.getEventStockIDs().toString());
                        PatternStream<DataEvent> patternStream = CEP
                                .pattern(env.fromCollection(privatePattern.getModifiedEventsForOTs().get(ot)), randomPatterns.get(patternCounter)).inProcessingTime();
                        //for (DataEvent de:privatePattern.getModifiedEventsForOTs().get(ot)){
                          //  System.out.println(de.toString());
                        //}

                        //detecting the private pattern  over the input stream
                        DataStream<List<DataEvent>> results = patternStream.process(new PatternProcessFunction<DataEvent, List<DataEvent>>() {
                            @Override
                            public void processMatch(Map<String, List<DataEvent>> map, Context context, Collector<List<DataEvent>> collector) throws Exception {

                                List<DataEvent> list = new ArrayList<>();
                                list.add(map.get("start").get(0));
                                list.add(map.get("middle").get(0));
                                list.add(map.get("end").get(0));

                                collector.collect(list);

                            }
                        });


                        results.writeAsText(filePath+"/WindowBased/privatePattern"+privatePatternCounter+"/window"+windowCounter+"/OT"+ot+"/output "+patternCounter+".txt", FileSystem.WriteMode.OVERWRITE)
                                .setParallelism(1);  // ensure that events are written in order;
                        writeStringToFile(String.valueOf(actualPrivateMatches),filePath+
                                "/WindowBased/privatePattern"+privatePatternCounter+"/window"+windowCounter+"/OT"+ot+"/actualMatch.txt");
                    }
                }

            }


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
    private static void writeStringToFile(String string, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(string);
            bw.newLine();

        } catch (IOException e) {
            // Print the stack trace
            // if an IO exception occurs
            e.printStackTrace();
        }
    }

    private static List<DataEvent> readEventFile(String filePath, String filename) {
        List<DataEvent> eventList = new ArrayList<>();

        List<String> lines = new ArrayList<>();


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

            lines.add(fileLine);
        }
        fileScanner.close();

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
