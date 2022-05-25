package edu.ucla.se;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateRegex {
    /* Input object */
    HashMap<Integer, HashMap<String, List<Integer>>> grouping;      // group_num -> (filename: [line_changed])
    HashMap<String, HashMap<Integer, String>>  dict;                // filename -> (line_num: code)

    public GenerateRegex(HashMap<Integer, HashMap<String, List<Integer>>> _grouping, HashMap<String,
            HashMap<Integer, String>> _dict){
        this.grouping = _grouping;
        this.dict = _dict;
    }

    /*
    * @return: HashMap (group -> [[code snippet1],[code snippet2]])
    * */
    public HashMap<Integer,ArrayList<ArrayList<String>>> getCodeSnippet(){
        HashMap<Integer,ArrayList<ArrayList<String>>> ret = new HashMap<>();

        for (Integer g : grouping.keySet()) {
            HashMap<String, List<Integer>> files = grouping.get(g);
            ArrayList<ArrayList<String>> val = new ArrayList<>();

            for (Map.Entry f: files.entrySet()){
                String fileName = (String)f.getKey();
                if (!dict.containsKey(fileName)){
                    continue;
                }
                HashMap<Integer, String> fileCodeDict = dict.get(fileName);
                ArrayList<String> code = new ArrayList<>();
                for (Integer i: files.get(fileName)){
                    if (!fileCodeDict.containsKey(i)){
                        continue;
                    }
                    code.add(fileCodeDict.get(i));
                }
                val.add(code);
            }

            ret.put(g, val);
        }

        return ret;
    }

    public String findRegex(ArrayList<ArrayList<String>> codes){
        return "aaa";
    }

    public ArrayList<String> generateRegex(){
        HashMap<Integer,ArrayList<ArrayList<String>>> codeSnippet = getCodeSnippet();
        ArrayList<String> ret = new ArrayList<>();

        for (Integer g: codeSnippet.keySet()){
            ArrayList<ArrayList<String>> codes = codeSnippet.get(g);
            String pattern = findRegex(codes);
            ret.add(pattern);
        }

        return ret;
    }

}
