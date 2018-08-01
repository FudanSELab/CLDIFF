package edu.fdu.se.base.associating;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by huangkaifeng on 2018/4/16.
 *
 */
public class TotalFileAssociations {

    public Map<String,List<Association>> fileAndfileTpfileAssos;

    public TotalFileAssociations(){
        fileAndfileTpfileAssos = new HashMap<>();
    }

    public void addEntry(String fileName,List<Association> assos){
        if(fileAndfileTpfileAssos.containsKey(fileName)){
            List<Association> associations = fileAndfileTpfileAssos.get(fileName);
            associations.addAll(assos);
        }else{
            List<Association> associations = new ArrayList<>();
            associations.addAll(assos);
            fileAndfileTpfileAssos.put(fileName,associations);
        }
    }

    public void addEntry(String fileName,Association asso){
        if(fileAndfileTpfileAssos.containsKey(fileName)){
            List<Association> associations = fileAndfileTpfileAssos.get(fileName);
            associations.add(asso);
        }else{
            List<Association> associations = new ArrayList<>();
            associations.add(asso);
            fileAndfileTpfileAssos.put(fileName,associations);
        }
    }


    public void addFile2FileAssos(String fileA,String fileB,List<Association> a){
        String key;
        if(fileA.compareTo(fileB)<0){
            key = fileA +"----"+fileB;
        }else{
            key = fileB +"----"+fileA;
        }
        List<Association> mList;
        if(fileAndfileTpfileAssos.containsKey(key)) {
            mList = fileAndfileTpfileAssos.get(key);
            mList.addAll(a);
        }else{
            mList = new ArrayList<>();
            mList.addAll(a);
            fileAndfileTpfileAssos.put(key,mList);
        }
    }

    public String toConsoleString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,List<Association>> entry:this.fileAndfileTpfileAssos.entrySet()){
            String key = entry.getKey();
            if(key.contains("----")) {
                sb.append("CommitFile to CommitFile: "+key);
            }else {
                sb.append("CommitFile: "+key);
            }
            sb.append("  \n");
            List<Association> assos = entry.getValue();
            for(Association as : assos){
                sb.append(as.toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }




    public String toAssoJSonString(){
        JSONObject jsonObject = new JSONObject();
        JSONArray ja2 = new JSONArray();
        for(Map.Entry<String,List<Association>> entry:this.fileAndfileTpfileAssos.entrySet()){
            JSONObject jo2 = new JSONObject();
            String key = entry.getKey();
            if(key.contains("----")) {
                jo2.put("link-type","two-file-link");
                String[] data = key.split("----");
                String [] data21 = data[0].split("@@@");
                String [] data22 = data[1].split("@@@");
                jo2.put("file-name",data21[1]);
                jo2.put("file-name2",data22[1]);
                jo2.put("parent-commit",data21[0]);
                jo2.put("parent-commit2",data22[0]);
            }else {
                jo2.put("link-type","one-file-link");
                String[] data2 = key.split("@@@");
                jo2.put("file-name",data2[1]);
                jo2.put("parent-commit",data2[0]);

            }
            List<Association> assos = entry.getValue();
            JSONArray linkArr = new JSONArray();
            for(Association as : assos){
                linkArr.put(as.linkJsonString());
            }
            jo2.put("links",linkArr);
            ja2.put(jo2);
        }
        jsonObject.put("links",ja2);
        return jsonObject.toString(4);
    }

}
