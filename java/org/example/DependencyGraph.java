package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DependencyGraph implements Serializable {
    String otModel;
    //String patternType;

    public DependencyGraph(){}

    Graph<StockPattern> dependency = new Graph<>();

    public DependencyGraph(String otModel, List<StockPattern> randomPatternSet, int privatePatternSize) {
        this.otModel = otModel;
        //this.patternType = patternType;
        this.dependency = GenerateGraph(randomPatternSet,privatePatternSize);
    }

    public String getOtModel() {
        return otModel;
    }



    public Graph<StockPattern> getDependency() {
        return dependency;
    }

    public void setOtModel(String otModel) {
        this.otModel = otModel;
    }



    public void setDependency(Graph<StockPattern> dependency) {
        this.dependency = dependency;
    }

    public Graph<StockPattern> GenerateGraph(List<StockPattern> randomPatternSet, int privatePatternSize){
        Graph<StockPattern> graph = new Graph<StockPattern>();
        //System.out.println("OT is "+this.otModel);
        switch (this.otModel){
            case "Drop(e1)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added0 "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added1 "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added3 "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Drop(e2)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("equal: "+!randomPatternSet.get(i).equals(sp2));
                        //if (!randomPatternSet.get(i).equals(sp2) && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))){
                            //graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(1)+"-->"+sp2.getEventStockIDs().toString());
                        //}
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                    }
                }
                break;

            case "Drop(e3)":
                //System.out.println("private pattern size is : "+privatePatternSize);
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 0){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Reorder(e1,e2)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==2
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;



            case "Reorder(e1,e3)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 2
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 0){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==2
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Reorder(e2,e3)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==2
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==0){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }

                break;

            case "Tamper(e1)":
                //System.out.println("private pattern size is : "+privatePatternSize);
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Tamper(e2)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                    }
                }
                break;

            case "Tamper(e3)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1)) == 0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2)) == 0){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Injection(e,e1,e2)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        /*
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                         */
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                    }
                }
                break;

            case "Injection(e,e2,e3)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        /*
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(0))==1
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==2){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }

                         */
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(1))==0
                                && sp2.getEventStockIDs().indexOf(randomPatternSet.get(i).getEventStockIDs().get(2))==1){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Generalization(e1)":
                //System.out.println("private pattern size is : "+privatePatternSize);
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().get(1).equals(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.getEventStockIDs().get(2).equals(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.IsGeneral(sp2.getEventStockIDs().get(0),randomPatternSet.get(i).getEventStockIDs().get(0))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(0))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(0)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Generalization(e2)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().get(0).equals(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().get(2).equals(randomPatternSet.get(i).getEventStockIDs().get(2))
                                && sp2.IsGeneral(sp2.getEventStockIDs().get(1),randomPatternSet.get(i).getEventStockIDs().get(1))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(1))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(1)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }
                break;

            case "Generalization(e3)":
                for (int i=0;i<privatePatternSize;i++){
                    for (StockPattern sp2:randomPatternSet){
                        if (!graph.hasVertex(sp2)){
                            graph.addVertex(sp2);
                            //System.out.println("node "+sp2.getEventStockIDs().toString()+" is added to graph");
                        }
                        //System.out.println("sp1:"+randomPatternSet.get(i)+" sp2: "+sp2);
                        //System.out.println("not equal: "+!randomPatternSet.get(i).equals(sp2));
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().get(0).equals(randomPatternSet.get(i).getEventStockIDs().get(0))
                                && sp2.getEventStockIDs().get(1).equals(randomPatternSet.get(i).getEventStockIDs().get(1))
                                && sp2.IsGeneral(sp2.getEventStockIDs().get(2),randomPatternSet.get(i).getEventStockIDs().get(2))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                        if (!randomPatternSet.get(i).equals(sp2)
                                && sp2.getEventStockIDs().contains(randomPatternSet.get(i).getEventStockIDs().get(2))){
                            graph.addEdge(randomPatternSet.get(i),sp2,false);
                            //System.out.println("added "+randomPatternSet.get(i).getEventStockIDs().get(2)+"-->"+sp2.getEventStockIDs().toString());
                        }
                    }
                }


        }

        //System.out.println("graph for OT "+this.otModel+" is generated");

        return graph;
    }

    public String FindZeroOutDegree(List<String> OTs,StockPattern privatePattern) {


        String selectedOT = "";
        for (DependencyGraph g:privatePattern.getUpdatedSubGraphList()) {
            //System.out.println("OT for this graph is "+g.otModel);
            int matchCount = g.getDependency().getOutgoingEdgesCount(privatePattern);
            //System.out.println("graph "+g.otModel+" : "+matchCount);
            if (matchCount==0){
                selectedOT = g.getOtModel();
                //System.out.println("within if: "+selectedOT);
                return selectedOT;
            }
        }
        PlPCEPWithGraphAndDependency.zeroOutDegree=false;
        Random random=new Random();
        selectedOT = OTs.get(random.nextInt(OTs.size()));
        System.out.println("this random OT is selected: "+selectedOT);
        return selectedOT;
    }

    public LinkedList<String> EnforcingOTModel(LinkedList<String> eventsInString, StockPattern sp,
                                               List<PatternMatch> patternMatches) {
        String selectedOT = sp.getSelectedOT();
        LinkedList<String> enforcedList = new LinkedList<>();
        for (String enforcedEvent:eventsInString){
            enforcedList.add(enforcedEvent);
        }
        //System.out.println(sp.getPatternID()+":"+selectedOT);
        if (sp.getSelectedOT().equals("Drop(e1)")){
            for (PatternMatch p:patternMatches) {
                boolean OTApplied = enforcedList.remove(p.getEventList().get(0).toString());
                //System.out.println("Drop e1 OT applied ? "+OTApplied);
            }
            //System.out.println("this time");
        } else if (sp.getSelectedOT().equals("Drop(e2)")) {
            for (PatternMatch p:patternMatches) {
                boolean a = enforcedList.remove(p.getEventList().get(1).toString());
                //System.out.println("Drop e2 OT applied ? "+a);
                //System.out.println("pattern is :"+sp.getPatternID());
            }
        } else if (sp.getSelectedOT().equals("Drop(e3)")) {
            for (PatternMatch p:patternMatches) {
                boolean b = enforcedList.remove(p.getEventList().get(2).toString());
                //System.out.println("Drop e3 OT applied ? "+b);
            }
        } else if (sp.getSelectedOT().equals("Reorder(e1,e2)")) {
            for (PatternMatch p:patternMatches) {
                String e1= p.getEventList().get(0).toString();
                String e2= p.getEventList().get(1).toString();
                int indexE1= enforcedList.indexOf(e1);
                int indexE2= enforcedList.indexOf(e2);


                //due to index change, first add e1 then add e2 in an earlier position

                enforcedList.remove(e2);
                enforcedList.add(indexE2,e1);

                enforcedList.remove(e1);
                enforcedList.add(indexE1,e2);


            }
        } else if (sp.getSelectedOT().equals("Reorder(e1,e3)")) {
            for (PatternMatch p:patternMatches) {
                String e1= p.getEventList().get(0).toString();
                String e3= p.getEventList().get(2).toString();
                //System.out.println("e1: "+ e1+" e3: "+e3);
                int indexE1= enforcedList.indexOf(e1);
                int indexE3= enforcedList.indexOf(e3);


                //due to index change, first add e1 then add e2 in an earlier position

                //boolean removeE3 =
                enforcedList.remove(e3);
                enforcedList.add(indexE3,e1);

                //System.out.println("removed e3 ? "+removeE3 + " added? "+ enforcedList.get(indexE3));

                //boolean removeE1 =
                enforcedList.remove(e1);
                enforcedList.add(indexE1,e3);
                //System.out.println("removed e1 ? "+removeE1 + " added? "+ enforcedList.get(indexE1));


            }
        } else if (sp.getSelectedOT().equals("Reorder(e2,e3)")) {
            for (PatternMatch p:patternMatches) {
                String e2= p.getEventList().get(1).toString();
                String e3= p.getEventList().get(2).toString();
                int indexE2= enforcedList.indexOf(e2);
                int indexE3= enforcedList.indexOf(e3);


                //due to index change, first add e1 then add e2 in an earlier position

                enforcedList.remove(e3);
                enforcedList.add(indexE3,e2);

                enforcedList.remove(e2);
                enforcedList.add(indexE2,e3);


            }
        } else if (sp.getSelectedOT().equals("Tamper(e1)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(0);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("1111111");

                enforcedList.add(index,de.toString());
            }
        } else if (sp.getSelectedOT().equals("Tamper(e2)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(1);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("1111111");

                enforcedList.add(index,de.toString());
            }
        } else if (sp.getSelectedOT().equals("Tamper(e3)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(2);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("1111111");

                enforcedList.add(index,de.toString());
            }
        } else if (sp.getSelectedOT().equals("Injection(e,e1,e2)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de1 = p.getEventList().get(0);
                int index= enforcedList.indexOf(de1.toString());

                de1.setStockCode("1111111");

                enforcedList.add((index+1),de1.toString());
            }
        } else if (sp.getSelectedOT().equals("Injection(e,e2,e3)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de2 = p.getEventList().get(1);
                int index= enforcedList.indexOf(de2.toString());

                de2.setStockCode("1111111");

                enforcedList.add((index+1),de2.toString());
            }
        } else if (sp.getSelectedOT().equals("Generalization(e1)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(0);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("general");

                enforcedList.add(index,de.toString());
            }
        } else if (sp.getSelectedOT().equals("Generalization(e2)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(1);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("general");

                enforcedList.add(index,de.toString());
            }
        } else if (sp.getSelectedOT().equals("Generalization(e3)")) {
            for (PatternMatch p:patternMatches) {
                DataEvent de = p.getEventList().get(2);
                int index= enforcedList.indexOf(de.toString());

                enforcedList.remove(de.toString());

                de.setStockCode("general");

                enforcedList.add(index,de.toString());
            }
        }


        /*switch (selectedOT){
            case "Drop(e1)":
                for (PatternMatch p:patternMatches) {
                    boolean OTApplied = enforcedList.remove(p.getEventList().get(0).toString());
                    System.out.println("Drop e1 OT applied ? "+OTApplied);
                }

            case "Drop(e2)":
                for (PatternMatch p:patternMatches) {
                    boolean a = enforcedList.remove(p.getEventList().get(1).toString());
                    System.out.println("Drop e2 OT applied ? "+a);
                    System.out.println("pattern is :"+sp.getPatternID());


                }
                break;

            case "Drop(e3)":
                for (PatternMatch p:patternMatches) {
                    boolean b = enforcedList.remove(p.getEventList().get(2).toString());
                    System.out.println("Drop e3 OT applied ? "+b);
                }
                break;

            case "Reorder(e1,e2)":
                for (PatternMatch p:patternMatches) {
                    String e1= p.getEventList().get(0).toString();
                    String e2= p.getEventList().get(1).toString();
                    int indexE1= enforcedList.indexOf(e1);
                    int indexE2= enforcedList.indexOf(e2);


                    //due to index change, first add e1 then add e2 in an earlier position

                    enforcedList.remove(e2);
                    enforcedList.add(indexE2,e1);

                    enforcedList.remove(e1);
                    enforcedList.add(indexE1,e2);
                    
                    
                }
                break;



            case "Reorder(e1,e3)":
                for (PatternMatch p:patternMatches) {
                    String e1= p.getEventList().get(0).toString();
                    String e3= p.getEventList().get(2).toString();
                    //System.out.println("e1: "+ e1+" e3: "+e3);
                    int indexE1= enforcedList.indexOf(e1);
                    int indexE3= enforcedList.indexOf(e3);


                    //due to index change, first add e1 then add e2 in an earlier position

                    //boolean removeE3 =
                    enforcedList.remove(e3);
                    enforcedList.add(indexE3,e1);

                    //System.out.println("removed e3 ? "+removeE3 + " added? "+ enforcedList.get(indexE3));

                    //boolean removeE1 =
                    enforcedList.remove(e1);
                    enforcedList.add(indexE1,e3);
                    //System.out.println("removed e1 ? "+removeE1 + " added? "+ enforcedList.get(indexE1));


                }
                break;

            case "Reorder(e2,e3)":
                for (PatternMatch p:patternMatches) {
                    String e2= p.getEventList().get(1).toString();
                    String e3= p.getEventList().get(2).toString();
                    int indexE2= enforcedList.indexOf(e2);
                    int indexE3= enforcedList.indexOf(e3);


                    //due to index change, first add e1 then add e2 in an earlier position

                    enforcedList.remove(e3);
                    enforcedList.add(indexE3,e2);

                    enforcedList.remove(e2);
                    enforcedList.add(indexE2,e3);


                }
                break;

            case "Tamper(e1)":
                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(0);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("1111111");

                    enforcedList.add(index,de.toString());
                }
                break;

            case "Tamper(e2)":
                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(1);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("1111111");

                    enforcedList.add(index,de.toString());
                }
                break;

            case "Tamper(e3)":
                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(2);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("1111111");

                    enforcedList.add(index,de.toString());
                }
                break;

            case "Injection(e,e1,e2)":

                for (PatternMatch p:patternMatches) {
                    DataEvent de1 = p.getEventList().get(0);
                    int index= enforcedList.indexOf(de1.toString());

                    de1.setStockCode("1111111");

                    enforcedList.add((index+1),de1.toString());
                }
                break;

            case "Injection(e,e2,e3)":

                for (PatternMatch p:patternMatches) {
                    DataEvent de2 = p.getEventList().get(1);
                    int index= enforcedList.indexOf(de2.toString());

                    de2.setStockCode("1111111");

                    enforcedList.add((index+1),de2.toString());
                }
                break;

            case "Generalization(e1)":

                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(0);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("general");

                    enforcedList.add(index,de.toString());
                }
                break;

            case "Generalization(e2)":
                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(1);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("general");

                    enforcedList.add(index,de.toString());
                }
                break;

            case "Generalization(e3)":
                for (PatternMatch p:patternMatches) {
                    DataEvent de = p.getEventList().get(2);
                    int index= enforcedList.indexOf(de.toString());

                    enforcedList.remove(de.toString());

                    de.setStockCode("general");

                    enforcedList.add(index,de.toString());
                }


        }*/
        return enforcedList;
    }
}
