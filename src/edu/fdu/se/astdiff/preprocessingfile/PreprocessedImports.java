package edu.fdu.se.astdiff.preprocessingfile;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/6.
 *
 */
public class PreprocessedImports {


    public static void diffImports(List<String> importA, List<String> importB, List<String> commonImports, List<String> deletedImports, List<String> addedImports){
        Map<String,Integer> importAMap = new HashMap<>();
        Map<String,Integer> importBMap = new HashMap<>();
        commonImports = new ArrayList<>();
        deletedImports = new ArrayList<>();
        addedImports = new ArrayList<>();
        importA.forEach(a-> importAMap.put(a,0));
        importB.forEach(a->importBMap.put(a,0));
        for(Map.Entry<String,Integer> item: importBMap.entrySet()){
            if(importAMap.containsKey(item.getKey())){
                commonImports.add(item.getKey());
                item.setValue(1);
                importAMap.put(item.getKey(),1);
            }else{
                addedImports.add(item.getKey());
            }
        }
        for(Map.Entry<String,Integer> item: importAMap.entrySet()){
            if(item.getValue()==1){
                deletedImports.add(item.getKey());
            }
        }

    }
}
