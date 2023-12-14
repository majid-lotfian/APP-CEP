package org.example;

import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class PlPCEPWindowStep2 {


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
        //StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
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

        //collecting sample event set
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

        int privatePatternWeight=5;
        int publicPatternWeight=1;


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

        // the key is "private pattern index : window number" and the value is number of actual pattern matches of this private pattern in this window
        HashMap<String, Integer> actualPrivatePatternMatches = new HashMap<>();
        HashMap<String,Integer> SumOfMatchesForEachPrivatePatternWithoutWindowing = new HashMap<>();


        for (int privateP = 0; privateP<randomPrivateSize;privateP++){
            for (int windowNum=0;windowNum<eventsInString.size()/windowSize;windowNum++){
                String path = filePath+
                        "/WindowBased/privatePattern"+privateP+"/window"+windowNum+"/OTDrop(e1)"+"/actualMatch.txt";
                if (Files.exists(Paths.get(path))){
                    List<String> lines=scanFile(path);
                    actualPrivatePatternMatches.put(String.valueOf(patternIndices.get(privateP))+" : "+String.valueOf(windowNum),Integer.parseInt(lines.get(0)));
                }else {

                    actualPrivatePatternMatches.put(String.valueOf(patternIndices.get(privateP))+" : "+String.valueOf(windowNum),0);

                }



                //System.out.println("actual: "+actualPrivatePatternMatches.get());
            }
            String path2 = filePath+
                    "/WindowBased/privatePattern"+privateP+"/sumOfMatchesWithoutWindowing.txt";
            if (Files.exists(Paths.get(path2))){
                List<String> list=scanFile(path2);
                SumOfMatchesForEachPrivatePatternWithoutWindowing.put(String.valueOf(patternIndices.get(privateP)),Integer.parseInt(list.get(0)));
            }else {
                SumOfMatchesForEachPrivatePatternWithoutWindowing.put(String.valueOf(patternIndices.get(privateP)),0);
            }

        }


        List<WindowBasedInitialResults> initialResults = new ArrayList<>();

        HashMap<String,WindowBasedInitialResults> OTUtility = new HashMap<>();
        //for each private pattern
        for (int privatePatternCounter = 0; privatePatternCounter
                <randomPrivateSize; privatePatternCounter++){
            StockPattern privatePattern=patternSet.get(patternIndices.get(privatePatternCounter)-1);
            System.out.println("private pattern: "+privatePattern.getEventStockIDs());
            //for each window
            for (int windowCounter = 0; windowCounter
                    <windowedDataSet.keySet().size(); windowCounter++){

                //System.out.println("for window "+windowCounter);
                //generate the modified list of events for each window for each OT
                for (String ot:OTModels){
                    WindowBasedInitialResults w = new WindowBasedInitialResults();
                    w.setPrivateMatches(0);
                    w.setPublicMatches(0);
                    w.setUtility(0);
                    w.setActualPrivatePatternMatches(0);
                    w.setOtherPrivateMatches(0);

                    for (int patternCounter=0; patternCounter<randomPatternSetSize;patternCounter++){
                        String path = filePath+"/WindowBased/privatePattern"+privatePatternCounter+"/window"+windowCounter+"/OT"+ot+"/output "+patternCounter+".txt";


                        List<String> lines = scanFile(path);
                        if (patternCounter<randomPrivateSize){
                            if (patternCounter == privatePatternCounter){
                                w.setPrivateMatches(w.getPrivateMatches()+lines.size());
                            }else {
                                w.setOtherPrivateMatches(w.getOtherPrivateMatches()+lines.size());
                            }


                            //initialResults.add(new WindowBasedInitialResults(patternIndices.get(privatePatternCounter),
                                    //ot,patternCounter,"private",windowCounter,lines.size()));
                        }else {
                            w.setPublicMatches(w.getPublicMatches()+lines.size());
                            //publicMatches += lines.size();
                            //initialResults.add(new WindowBasedInitialResults(patternIndices.get(privatePatternCounter),
                                    //ot,patternCounter,"public",windowCounter,lines.size()));
                        }
                    }

                    w.setUtility((publicPatternWeight*w.getPublicMatches())-(privatePatternWeight*w.getPrivateMatches())-w.getOtherPrivateMatches());
                    //calculatedUtility = (publicPatternWeight*publicMatches)-(privatePatternWeight*privateMatches);
                    //w.setPrivateMatches(privateMatches);
                    //w.setPublicMatches(publicMatches);
                    //w.setUtility(calculatedUtility);
                    w.setWindowNumber(windowCounter);
                    w.setPrivatePatternID(patternIndices.get(privatePatternCounter));
                    w.setSelectedOT("");

                    OTUtility.put(ot,w);

                }

                String selectedOT="Drop(e1)";
                int maxUtility = OTUtility.get("Drop(e1)").getUtility();
                for (String ot:OTModels) {
                    //System.out.println("utility for max : "+maxUtility+ " and utility for ot "+ot+" is "+OTUtility.get(ot).getUtility());
                    if (OTUtility.get(ot).getUtility() > maxUtility){
                        selectedOT = ot;
                    }
                }
                WindowBasedInitialResults selected = new WindowBasedInitialResults();

                selected.setPrivatePatternID(OTUtility.get(selectedOT).getPrivatePatternID());
                selected.setWindowNumber(OTUtility.get(selectedOT).getWindowNumber());
                selected.setSelectedOT(selectedOT);
                selected.setPublicMatches(OTUtility.get(selectedOT).getPublicMatches());
                selected.setPrivateMatches(OTUtility.get(selectedOT).getPrivateMatches());
                selected.setOtherPrivateMatches(OTUtility.get(selectedOT).getOtherPrivateMatches());
                selected.setUtility(OTUtility.get(selectedOT).getUtility());
                selected.setActualPrivatePatternMatches(actualPrivatePatternMatches.get(patternIndices.get(privatePatternCounter)+" : "+windowCounter));
                //WindowBasedInitialResults selected = OTUtility.get(selectedOT);
                //selected.setSelectedOT(selectedOT);

                initialResults.add(selected);

            }


        }
        //HashMap<String, Integer> revealedPrivatePatterns=new HashMap<>();
        for (WindowBasedInitialResults w1:initialResults){
            System.out.println("for private pattern "+w1.getPrivatePatternID()+" in window "+w1.getWindowNumber()+
                    " OT "+w1.getSelectedOT()+" is selected and "+w1.getPrivateMatches()+" is revealed and "+w1.getPublicMatches()+" are truly detected");
            //calculating the revealed private pattern by adversary background knowledge
           // boolean flag = false;

            StockPattern privatePattern=patternSet.get(w1.getPrivatePatternID()-1);

            //dependency violation detection
            for (ParallelEvents pe:parallelDependencies){
                //System.out.println("parallel: "+pe.getEventIDs().toString());
                //System.out.println("drop1: "+privatePattern.getEventStockIDs().get(1));
                //System.out.println("contains: "+pe.containsStockCode(privatePattern.getEventStockIDs().get(1)));
                if ((w1.getSelectedOT().equals("Drop(e1)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                        || (w1.getSelectedOT().equals("Drop(e2)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                        || (w1.getSelectedOT().equals("Drop(e3)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))
                        ||(w1.getSelectedOT().equals("Tamper(e1)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                        || (w1.getSelectedOT().equals("Tamper(e2)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                        || (w1.getSelectedOT().equals("Tamper(e3)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))
                        || (w1.getSelectedOT().equals("Generalization(e1)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(0))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(0)))))
                        || (w1.getSelectedOT().equals("Generalization(e2)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(1))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(1)))))
                        || (w1.getSelectedOT().equals("Generalization(e3)") &&
                        (pe.getEventIDs().contains(privatePattern.getEventStockIDs().get(2))
                                || periodicDependencies.contains(new PeriodicEvent(privatePattern.getEventStockIDs().get(2)))))){

                    //System.out.println("size of pattern match for pattern "+patternIndices.get(i)+" is : "+privatePatternMatches.get(patternIndices.get(i)).size());
                    w1.setDependencyViolation(true);
                }
            }

        }


        List<FinalResults> finalResults = new ArrayList<>();
        for (int ppattern =0;ppattern<randomPrivateSize;ppattern++){
            int revealedPrivateMatches = 0;
            int revealedByAdversary = 0;
            int actualPrivateMatches=0;
            int publicMatches=0;
            int otherPrivateMatches=0;
            for (WindowBasedInitialResults w2:initialResults){
                if (w2.getPrivatePatternID() == patternIndices.get(ppattern) ){
                    if (w2.isDependencyViolation()){
                        revealedByAdversary+= w2.getActualPrivatePatternMatches();
                    }
                    actualPrivateMatches+=w2.getActualPrivatePatternMatches();
                    revealedPrivateMatches+= w2.getPrivateMatches();
                    publicMatches+= w2.getPublicMatches();
                    otherPrivateMatches+=w2.getOtherPrivateMatches();

                }

            }
            finalResults.add(new FinalResults(String.valueOf(patternIndices.get(ppattern)),
                    SumOfMatchesForEachPrivatePatternWithoutWindowing.get(String.valueOf(patternIndices.get(ppattern))),
                    actualPrivateMatches,
                    revealedPrivateMatches,
                    revealedByAdversary,
                    publicMatches,
                    otherPrivateMatches));
            //revealedPrivatePatterns.put(String.valueOf(patternIndices.get(ppattern)),count);
        }

        List<String> finalResultWindowing = new ArrayList<>();
        for (int k = 0; k< finalResults.size(); k++){

            finalResultWindowing.add("Pattern ID : "+finalResults.get(k).getPrivatePatternID()
                    +" WithoutWindowing : "+finalResults.get(k).getNumOfMatchesWithoutWindowing()
                    +" Windowing : "+finalResults.get(k).getNumOfMatchesWindowing()
                    +" revealed : "+finalResults.get(k).getNumOfRevealedPrivatePatterns()
                    +" other private pattern revealed : "+finalResults.get(k).getNumOfRevealedOtherPrivatePatterns()
                    +" revealed by adversary : "+finalResults.get(k).getNumOfRevealedByAdversary()
                    +" public : "+finalResults.get(k).getNumOfTrulyDetectedPublicPattern());
        }

        writeListToFile(finalResultWindowing,filePath+
                "/WindowBased/"+"finalResultWindowing.txt");






        //env.execute("PlP-CEP");


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
    private static void writeHashMapToFile(HashMap<String,Integer> hashMap, String path,List<Integer> patternIndices) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int i=0;i<hashMap.keySet().size();i++) {
                // Write each element of the list
                // to a new line in the output.txt file
                bw.write(hashMap.get(String.valueOf(patternIndices.get(i))));
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
