package org.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class StockPattern implements Serializable {
    String selectedOT;

    public void setSelectedOT(String selectedOT) {
        this.selectedOT = selectedOT;
    }

    public String getSelectedOT() {
        return selectedOT;
    }

    List<String> eventStockIDs;

    List<DependencyGraph> updatedSubGraphList;


    HashMap<String, List<DataEvent>> modifiedEventsForOTs;
    HashMap<String, List<String>> modifiedEventsInStringForOTs;

    public void setModifiedEventsInStringForOTs(HashMap<String, List<String>> modifiedEventsInStringForOTs) {
        this.modifiedEventsInStringForOTs = modifiedEventsInStringForOTs;
    }

    public HashMap<String, List<String>> getModifiedEventsInStringForOTs() {
        return modifiedEventsInStringForOTs;
    }




    public void setUpdatedSubGraphList(List<DependencyGraph> updatedSubGraphList) {
        this.updatedSubGraphList = updatedSubGraphList;
    }

    public List<DependencyGraph> getUpdatedSubGraphList() {
        return updatedSubGraphList;
    }

    int frequency;
    int patternID;

    public StockPattern(List<String> eventStockIDs, int frequency, int patternID,List<DependencyGraph> updatedSubGraphList
            ,HashMap<String, List<DataEvent>> modifiedEventsInStringForOTs) {
        this.eventStockIDs = eventStockIDs;
        this.frequency = frequency;
        this.patternID = patternID;
        this.updatedSubGraphList=updatedSubGraphList;
        this.modifiedEventsForOTs = modifiedEventsInStringForOTs;
    }
    public StockPattern() {

    }

    public void setModifiedEventsForOTs(HashMap<String, List<DataEvent>> modifiedEventsInStringForOTs) {
        this.modifiedEventsForOTs = modifiedEventsInStringForOTs;
    }

    public HashMap<String, List<DataEvent>> getModifiedEventsForOTs() {
        return modifiedEventsForOTs;
    }

    public int getPatternID() {
        return patternID;
    }

    public void setPatternID(int patternID) {
        this.patternID = patternID;
    }

    public String toString(){
        return this.getEventStockIDs().toString()+":"+this.getFrequency();
    }

    public List<String> getEventStockIDs() {
        return eventStockIDs;
    }
    public boolean IsGeneral(String general, String particular){
        return false;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setEventStockIDs(List<String> eventStockIDs) {
        this.eventStockIDs = eventStockIDs;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StockPattern){

            StockPattern other = (StockPattern) obj;
            boolean flag=true;
            for (int i=0; i<this.getEventStockIDs().size();i++){
                if (!this.getEventStockIDs().get(i).equals(other.getEventStockIDs().get(i))){
                    flag = false;
                }
            }

            return flag;

        }


        return false;

    }

}
