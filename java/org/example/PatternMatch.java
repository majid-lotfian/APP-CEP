package org.example;

import java.util.ArrayList;
import java.util.List;

public class PatternMatch {
    List<DataEvent> eventList;

    public PatternMatch(List<DataEvent> eventList) {
        this.eventList = eventList;
    }

    public PatternMatch() {

    }
    public PatternMatch(String s){
        String[] eventString = s.split("InvoiceNo");
        List<DataEvent> events = new ArrayList<>();

        events.add(new DataEvent("InvoiceNo"+eventString[1].substring(0,eventString[1].length()-2),""));
        events.add(new DataEvent("InvoiceNo"+eventString[2].substring(0,eventString[2].length()-2),""));
        events.add(new DataEvent("InvoiceNo"+eventString[3].substring(0,eventString[3].length()-1),""));


        this.eventList= events;
    }

    public List<DataEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<DataEvent> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString(){
        String matches = "";
        for (DataEvent de:this.eventList) {
            matches+= de.toString()+", ";
        }
        return matches;
    }
}
