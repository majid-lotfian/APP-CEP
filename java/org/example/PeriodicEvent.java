package org.example;

import java.io.Serializable;

public class PeriodicEvent implements Serializable {
    String eventStockCode;
    int frequency;

    public PeriodicEvent(String eventStockCode, int frequency) {
        this.eventStockCode = eventStockCode;
        this.frequency = frequency;
    }
    public PeriodicEvent() {

    }
    public PeriodicEvent(String eventStockCode){
        this.eventStockCode= eventStockCode;

    }

    public String toString(){
        return this.getEventStockCode()+":"+this.getFrequency();
    }
    public String getEventStockCode() {
        return eventStockCode;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setEventStockCode(String eventStockCode) {
        this.eventStockCode = eventStockCode;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeriodicEvent){
            PeriodicEvent other = (PeriodicEvent) obj;
            //System.out.println("in equal : "+this.eventStockCode+" "+other.eventStockCode);
            if (this.eventStockCode.equals(other.getEventStockCode())){
                return true;
            }
        }

        return false;
    }

}
