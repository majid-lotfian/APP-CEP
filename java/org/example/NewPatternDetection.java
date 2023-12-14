package org.example;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


import org.apache.flink.cep.CEP;

import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.util.Collector;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class NewPatternDetection {

    public static void main(String[] args) throws Exception {


        // Set up the Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        List<String> producerList= new ArrayList<String>(){
            {
                add("p1");
                add("p2");
                add("p3");
                add("p4");
                add("p5");
                add("p6");
            }
        };

        String filePath = "/home/majidlotfian/flink/flink-quickstart/PLprivacy_Poster/input_folder/PlP-CEP/output.txt";


        DataStream<String> stream = env.fromCollection(producerList);



        Pattern<String, ?> pattern1 = Pattern.<String>begin("start")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String s) throws Exception {
                        //System.out.println("type of this event is :");
                        return s.equals("p1");
                    }
                }).next("end")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String s) throws Exception {
                        return s.equals("p2");
                    }
                })
                .within(Time.seconds(10));




        PatternStream<String> patternStream = CEP.pattern(stream, pattern1).inProcessingTime();

        DataStream<String> results = patternStream.process(new PatternProcessFunction<String, String>() {
            @Override
            public void processMatch(Map<String, List<String>> map, Context context, Collector<String> collector) throws Exception {

                System.out.println("within process");
                String start=map.get("start").get(0);
                String end = map.get("end").get(0);
                collector.collect(start+end);
            }
        });

        results.writeAsText(filePath, FileSystem.WriteMode.OVERWRITE)
                .setParallelism(1);  // ensure that events are written in order;





        env.execute("new");
    }
}
