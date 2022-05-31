package edu.ucla.se;

import org.apache.commons.exec.util.StringUtils;

import java.util.*;


public class RegexGenerator {
    /* Input object */
    HashMap<Integer, HashMap<String, List<Integer>>> grouping;      // group_num -> (filename: [line_changed])
    // a b c d
    // - - -      1
    // - - -      1
    //
    // - -   -    2
    //
    //{ 1:
    //      {
    //          a.java: [[3,7,8],[0,1,2]], // being deleted
    //          b.java: [10,11,12],
    //          c.java: [4,5,6],
    //      }
    // }
    // {2:
    //       {
    //          a: [1,2,3]
    //          b: [5,6,7]
    //          d: [8,9,10]
    //       }
    // }

    Map<String, Map<Integer, String>>  dict;                // filename -> (line_num: code)
    GitHandler gitHandler;

    public RegexGenerator(HashMap<Integer, HashMap<String, List<Integer>>> _grouping, Map<String,
            Map<Integer, String>> _dict){
        this.grouping = _grouping;
        this.dict = _dict;
    }

    public RegexGenerator(HashMap<Integer, HashMap<String, List<Integer>>> grouping, GitHandler gitHandler) {
        this.grouping = grouping;
        this.gitHandler = gitHandler;
    }

    /**
     * TODO: read from file
     * @return: HashMap (group -> [[code snippet1],[code snippet2]])
     * */
    public HashMap<Integer,ArrayList<String>> getCodeSnippet(){
        HashMap<Integer,ArrayList<String>> ret = new HashMap<>();

        for (Integer g : grouping.keySet()) {
            HashMap<String, List<Integer>> files = grouping.get(g);
            this.dict = gitHandler.getOldFileContentByLine(files);
            ArrayList<String> val = new ArrayList<>();

            for (Map.Entry f: files.entrySet()){
                String fileName = (String)f.getKey();
                if (!dict.containsKey(fileName)){
                    continue;
                }
                Map<Integer, String> fileCodeDict = dict.get(fileName);
                String code = "";
                for (int i = 0; i < files.get(fileName).size(); ++i){
                    Integer idx = files.get(fileName).get(i);
                    code += "#;";       // new line
                    if (!fileCodeDict.containsKey(idx)){
                        continue;
                    }
                    if (fileCodeDict.get(idx).length() > 0){
                        code += fileCodeDict.get(idx);
                    }
                }
                val.add(code);
            }
            ret.put(g, val);
        }
        return ret;
    }

    /**
     * @input: a single line code of function call
     * @return: add funcstart label, argument count
     * **/
    public ArrayList<String> processFunction(String func){
        ArrayList<String> ret = new ArrayList<>();
        Integer args = 1;
        for (int i = 0; i < func.length(); i++){
            if (func.charAt(i) == '(' && i+1 < func.length() && func.charAt(i+1) == ')'){
                args = 0;
                break;
            }
            if (func.charAt(i) == ',') args++;
        }
        String [] raw = func.split("\\,|\\(|\\)");
        ret.add("funcstart");
        ret.add(args.toString());

        ret.add(raw[0]);
        return ret;
    }

    public String generator(ArrayList<String> tokens){
        String unit = ".*"; //  or [^0-9a-ZA-Z\_]*;
        String regex = "";
        char [] math = {'+','-','*','/','%','&','^','|','='};
        for (int i = 0; i < tokens.size(); ++i){
            String s = tokens.get(i);
            if (s.length() == 0){
                continue;
            }
            if(s.charAt(0) == '.'){
                regex += "\\\\";
                regex += s;
            }else if (s.equals("funcstart")){
                Integer argsNum = Integer.parseInt(tokens.get(i+1));
                if (tokens.get(i+2).equals("elseif")){
                    regex = regex + "else\\\\sif.*";
                }else{
                    regex = regex + tokens.get(i+2) + unit;
                }
                regex += "(";
                for (int j = 0; j < argsNum; ++j){
                    regex += unit;
                    if (j != argsNum-1) regex += ",";
                }
                regex += ")";
                i += 2;
            }else if (s.equals("#")){
                regex += unit;
            }else if (new String(math).indexOf(s.charAt(0)) != -1){
                regex += unit;
                regex += "\\\\";
                regex += s.charAt(0);
                regex += unit;
            }
            else{
                regex += s;
            }
        }
        regex += unit;
        return regex;
    }

    public String findRegex(ArrayList<String> codes){
        // input: ["event.item = item;sendEvent(true, event);", "ev.item = item;sendEvent(true, ev);"]
        // parse: ["event", ".item", "=item", "funcstart", "2", "sendEvent"]
        // output ".*\\.item=.*sendEvent(.*,.*)"
        // output "(\\S*)\.item=(\\S*)sendEvent(true,\\1)"

        System.out.println(codes);

        String delimiter = "((?=\\.|=|\\+|-|\\*|/|%|\\^|<|>|<=|>=)|(;|\\{|\\})|(?<=\\+|-|\\*|/|%|\\^|<|>|<=|>=))";
        ArrayList<ArrayList<String>> tokens = new ArrayList<>();
        int shortest = 0;
        // Step1: split
        for (int i = 0; i < codes.size(); ++i){ // ((?=\.|=)|(;|,)|(?<=\(|\)))
            String striped = codes.get(i);
            ArrayList<String> raw = new ArrayList<>(Arrays.asList(striped.split(delimiter,0)));
            ArrayList<String> token = new ArrayList<>();
            for (int j = 0; j < raw.size(); ++j){
                if (raw.get(j).contains("(") && raw.get(j).contains(")")){
                    ArrayList<String> processed = processFunction(raw.get(j));
                    for (String p: processed){
                        token.add(p.replaceAll("[\\s\\n]*",""));
                    }
                }else if (raw.get(j).contains("(")){        // extract possible function name
                    token.add(raw.get(j).substring(0,raw.get(j).indexOf("(")));
                }else if (raw.get(j).contains(")")){
                    continue;
                }else{
                    token.add(raw.get(j).replaceAll("[\\s\\n]*",""));
                }
            }
            tokens.add(token);
            if (token.size() < shortest){
                shortest = i;
            }
        }
        System.out.println(tokens);

        //Step2: union set
        ArrayList<String> based = tokens.get(shortest);
        for (ArrayList<String> other: tokens){
            based.retainAll(other);
        }
        System.out.println(based);

        String regex = generator(based);

        regex = regex.replaceAll("[\\s\\n]*","");

        return regex;
    }

    public ArrayList<String> generateRegex(){
        HashMap<Integer,ArrayList<String>> codeSnippet = getCodeSnippet();
        ArrayList<String> ret = new ArrayList<>();

        for (Integer g: codeSnippet.keySet()){
            ArrayList<String> codes = codeSnippet.get(g);
            String pattern = findRegex(codes);
            ret.add(pattern);
        }

        return ret;
    }

}
