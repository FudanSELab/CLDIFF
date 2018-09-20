package edu.fdu.se.base.links;

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
public class TotalFileLinks {

    public Map<String,List<Link>> fileAndfileTpfileAssos;

    public TotalFileLinks(){
        fileAndfileTpfileAssos = new HashMap<>();
    }

    public void addEntry(String fileName,List<Link> assos){
        if(fileAndfileTpfileAssos.containsKey(fileName)){
            List<Link> links = fileAndfileTpfileAssos.get(fileName);
            links.addAll(assos);
        }else{
            List<Link> links = new ArrayList<>();
            links.addAll(assos);
            fileAndfileTpfileAssos.put(fileName, links);
        }
    }

    public void addEntry(String fileName,Link asso){
        if(fileAndfileTpfileAssos.containsKey(fileName)){
            List<Link> links = fileAndfileTpfileAssos.get(fileName);
            links.add(asso);
        }else{
            List<Link> links = new ArrayList<>();
            links.add(asso);
            fileAndfileTpfileAssos.put(fileName, links);
        }
    }


    public void addFile2FileAssos(String fileA,String fileB,List<Link> a){
        String key;
        if(fileA.compareTo(fileB)<0){
            key = fileA +"----"+fileB;
        }else{
            key = fileB +"----"+fileA;
        }
        List<Link> mList;
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
        for(Map.Entry<String,List<Link>> entry:this.fileAndfileTpfileAssos.entrySet()){
            String key = entry.getKey();
            List<Link> assos = entry.getValue();
//            if(assos.size()<=0){
//                continue;
//            }
//            if(key.contains("----")) {
//                sb.append("Links among files: "+key);
//            }else {
//                sb.append("Links within file: "+key);
//            }
//            sb.append("  \n");

            for(Link as : assos){
                sb.append(as.toString());
                sb.append("\n");
            }
//            sb.append("\n");
        }
        return sb.toString();
    }




    public String toAssoJSonString(){
        JSONObject jsonObject = new JSONObject();
        JSONArray ja2 = new JSONArray();
        for(Map.Entry<String,List<Link>> entry:this.fileAndfileTpfileAssos.entrySet()){
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
            List<Link> assos = entry.getValue();
            JSONArray linkArr = new JSONArray();
            for(Link as : assos){
                linkArr.put(as.linkJsonString());
            }
            jo2.put("links",linkArr);
            ja2.put(jo2);
        }
        jsonObject.put("links",ja2);
        return jsonObject.toString(4);
    }

}
