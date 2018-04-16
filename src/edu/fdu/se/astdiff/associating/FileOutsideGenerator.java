package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/4/16.
 *
 *
 */
public class FileOutsideGenerator {

    public static String fileA;

    public static String fileB;

    public static void generateOutsideAssociation(Map<String,ChangeEntityData> totalMapData){
        Set<String> keys = totalMapData.keySet();
        List<String> fileKeysList = new ArrayList<>(keys);
        for(int i =0;i<fileKeysList.size();i++){
            for(int j=i;j<fileKeysList.size();j++){
                String a = fileKeysList.get(i);
                String b = fileKeysList.get(j);
                fileA = a;
                fileB = b;
                ChangeEntityData ca = totalMapData.get(a);
                ChangeEntityData cb = totalMapData.get(b);
                gen(ca,cb);
            }
        }
    }

    public static void gen(ChangeEntityData ca,ChangeEntityData cb){
        
    }
}
