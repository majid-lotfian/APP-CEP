package org.example;

import java.io.Serializable;
import java.util.List;

public class ParallelEvents implements Serializable {
    List<String> eventIDs;
    int frequency;

    public ParallelEvents(List<String> eventIDs, int frequency) {
        this.eventIDs = eventIDs;
        this.frequency = frequency;
    }
    public ParallelEvents() {

    }

    public String toString(){
        return this.getEventIDs().toString()+":"+this.getFrequency();
    }

    public List<String> getEventIDs() {
        return eventIDs;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setEventIDs(List<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public boolean containsStockCode(String s){
        for (String stock:this.getEventIDs()){
            //System.out.println(stock+" :: "+s);
            if (stock.equals(s)){
                return true;
            }
        }
        return false;
    }
}
