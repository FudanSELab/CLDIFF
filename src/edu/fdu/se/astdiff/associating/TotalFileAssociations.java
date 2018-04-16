package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by huangkaifeng on 2018/4/16.
 */
public class TotalFileAssociations {

    public Map<String,ChangeEntityData> totalSingleFileData;
    public Map<String,List<Association>> file2fileAssos;


    public TotalFileAssociations(){
        totalSingleFileData = new HashMap<>();
        file2fileAssos = new HashMap<>();
    }

    public void addEntry(String fileName, ChangeEntityData changeEntityData){
        totalSingleFileData.put(fileName,changeEntityData);
    }



    public void addFile2FileAssos(String fileA,String fileB,Association a){
        String key1 = fileA+"----"+fileB;
        String key2 = fileB+"----"+fileA;
        List<Association> mList;
        if(file2fileAssos.containsKey(key1)) {
            mList = file2fileAssos.get(key1);
            mList.add(a);
        }else if(file2fileAssos.containsKey(key2)){
            mList = file2fileAssos.get(key2);
            mList.add(a);
        }else{
            mList = new ArrayList<>();
            mList.add(a);
            file2fileAssos.put(key1,mList);
        }
    }
}
