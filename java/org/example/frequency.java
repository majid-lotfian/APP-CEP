package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.TreeMap;

public class frequency {

    public static void main(String[] args) throws Exception {
        //read the input file and put it in a list
        //String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/testCase/data.txt";
        String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/data.csv";
        List<String> lines = new ArrayList<>();
        int lineCount=0;

        Scanner fileScanner = null;//Initialize fileScanner

        try
        {
            fileScanner = new Scanner(new File(filePath));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        while(fileScanner.hasNextLine())
        {
            String fileLine = fileScanner.nextLine();
            //if (lineCount == 0){
              //  lineCount++;
                //continue;
           // }

            if (lineCount == 12000)
            //if (lineCount == 200000)
                break;
            lines.add(fileLine);
            lineCount++;
        }
        fileScanner.close();
        // read the contents of the file into a list of strings
        //List<String> linesTestCase500 = Files.readAllLines(Paths.get(filePathTestCase500));



        // create a list to store the contents of the file
        List<DataEvent> eventList = new ArrayList<>();



        // add the lines of the file to the list
        for (String line : lines) {

            eventList.add(new DataEvent(line));

        }

        //sample
        List<DataEvent> sample = eventList.subList(0,12000);



        List<String> stockCodes = new ArrayList<>();
        for (DataEvent de:sample){
            stockCodes.add(de.getStockCode());
        }
/*
        //periodic events

        int i = 1;
        HashMap<List<String>,Integer> map = new HashMap<List<String>,Integer>();
        while(i < stockCodes.size() - 2){
            List<String> pattern = new ArrayList<>();
            pattern.add(stockCodes.get(i));



            System.out.println(pattern);

            Integer population = map.get(pattern);
            if(population == null){
                population = 1;
            }else{
                population++;
            }
            //System.out.println("pup "+);
            map.put(pattern, population);

            i++;
        }

        //System.out.println(map);

        TreeMap<List<String>, Integer> sortedMap = sortMapByValue(map);
        //System.out.println(sortedMap);


        String outputFilePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/AllPeriodic.txt";
        File file = new File(outputFilePath);

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            int k=0;
            // iterate map entries
            for (Map.Entry<List<String>, Integer> entry :
                    sortedMap.entrySet()) {

                if (k<50){
                    // put key and value separated by a colon
                    bf.write(entry.getKey() + ":"
                            + entry.getValue());

                    // new line
                    bf.newLine();
                    k++;
                }else {
                    break;
                }

            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {

                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }


*/



        //generating parallel dependencies

        int i = 1;
        HashMap<List<String>,Integer> map = new HashMap<List<String>,Integer>();
        while(i < stockCodes.size() - 2){
            List<String> pattern = new ArrayList<>();
            pattern.add(stockCodes.get(i));
            pattern.add(stockCodes.get(i+1));


            System.out.println(pattern);

            Integer population = map.get(pattern);
            if(population == null){
                population = 1;
            }else{
                population++;
            }
            map.put(pattern, population);

            i++;
        }

        //System.out.println(map);

        TreeMap<List<String>, Integer> sortedMap = sortMapByValue(map);
        //System.out.println(sortedMap);

        String outputFilePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/MostFrequentParallelDependencies.txt";

        //String outputFilePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/output_folder/MostFrequentParallelDependencies.txt";
        File file = new File(outputFilePath);

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            int k=0;
            // iterate map entries
            for (Map.Entry<List<String>, Integer> entry :
                    sortedMap.entrySet()) {

                if (k<50){
                    // put key and value separated by a colon
                    bf.write(entry.getKey() + ":"
                            + entry.getValue());

                    // new line
                    bf.newLine();
                    k++;
                }else {
                    break;
                }

            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {

                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }






        //generating public and private pattern sets
/*
        //System.out.println(eventListTestCase500);

        int i = 1;
        HashMap<List<String>,Integer> map = new HashMap<List<String>,Integer>();
        while(i < stockCodes.size() - 2){
            List<String> pattern = new ArrayList<>();
            pattern.add(stockCodes.get(i));
            pattern.add(stockCodes.get(i+1));
            pattern.add(stockCodes.get(i+2));

            System.out.println(pattern);

            Integer population = map.get(pattern);
            if(population == null){
                population = 1;
            }else{
                population++;
            }
            map.put(pattern, population);

            i++;
        }

        //System.out.println(map);

        TreeMap<List<String>, Integer> sortedMap = sortMapByValue(map);
        //System.out.println(sortedMap);


        String outputFilePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/MostFrequentPatterns.txt";
        File file = new File(outputFilePath);

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            int k=0;
            // iterate map entries
            for (Map.Entry<List<String>, Integer> entry :
                    sortedMap.entrySet()) {

                if (k<50){
                    // put key and value separated by a colon
                    bf.write(entry.getKey() + ":"
                            + entry.getValue());

                    // new line
                    bf.newLine();
                    k++;
                }else {
                    break;
                }


            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {

                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }

 */



    }

    public static TreeMap<List<String>, Integer> sortMapByValue(HashMap<List<String>, Integer> map){
        Comparator<List<String>> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<List<String>, Integer> result = new TreeMap<List<String>, Integer>(comparator);
        result.putAll(map);
        return result;
    }
}
