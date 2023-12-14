package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ValueComparator implements Comparator<List<String>> {
    HashMap<List<String>, Integer> map = new HashMap<List<String>, Integer>();

    public ValueComparator(HashMap<List<String>, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(List<String> ls1, List<String> ls2) {
        if(map.get(ls1) >= map.get(ls2)){
            return -1;
        }else{
            return 1;
        }
    }
}
