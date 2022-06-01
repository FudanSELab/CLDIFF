package edu.ucla.se.utils;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ParserHelper {
    private UnionFind uf;
    public List<MethodComponent> listOfComponent;
    public ParserHelper() {
        uf = new UnionFind();
        listOfComponent = new ArrayList<>();
    }
    public void parseLinkJson(String fileName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        JSONArray ob = (JSONArray)((JSONObject) new JSONParser().parse(new FileReader(fileName))).get("links");
        JSONArray subob = (JSONArray) ((JSONObject) ob.get(0)).get("links");

        //        JsonObject jsonObject = new JsonParser().parse(fileName).getAsJsonObject();
//        Object obj = parser.parse(new FileReader());
        for (int i = 0; i < subob.size(); i++) {
            JSONObject temp = (JSONObject) subob.get(i);
            int from = ((Long) temp.get("from")).intValue();
            int to = ((Long) temp.get("to")).intValue();
            String changeType =(String) temp.get("desc");
            if (changeType.contains("Systematic")) {
                uf.add(from);
                uf.add(to);
                uf.merge(from, to);
                System.out.println(changeType);
            }

        }

        System.out.println();
    }

    public List<List<Integer>> parseDiffFile(String filename, String sourceFileName) {
        boolean flag = true;
        MethodComponent currComponent = new MethodComponent(uf);
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> list = Arrays.asList(data.split(" "));
                if (list.size() == 1) {
                    // finish reading a component
                    flag = false;
                    currComponent.calculateRoots();
                    listOfComponent.add(currComponent);
                } else if (list.get(0).equals(sourceFileName)) {
                    // start a new component
                    flag = true;
                    currComponent = new MethodComponent(uf);
                    currComponent.setFunctionName(list.get(2));
                    continue;
                }

                if (flag ) {
                    int idx = Integer.parseInt(list.get(0).
                                substring(0, list.get(0).length() - 1));
                    if (uf.father.containsKey(idx)) {
                        currComponent.addChangeIndex(idx);
                        currComponent.lineNumbers.add(lineNumberHelper(list.get(list.size() - 1)));
                    }

                }
            }
            currComponent.calculateRoots();
            listOfComponent.add(currComponent);

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        List<List<Integer>> result = new ArrayList<>();
        for (MethodComponent c : listOfComponent) {
            result.add(new ArrayList<>(c.getRoots()));
        }
        return result;
    }

    private Integer lineNumberHelper(String s) {
        int left = 0;
        for (int i = 0; i < s.length(); i ++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                left = i;
                break;
            }
        }
        int right = left;
        while (right < s.length() && s.charAt(right) >= '0' && s.charAt(left) <= '9') {
            right ++;
        }

        return Integer.parseInt(s.substring(left, right));
    }

    public HashMap<Integer, List<List<Integer>>> getGroupedLines(HashMap<Integer, Integer> linkToHigerLevel) {
        HashMap<Integer, List<List<Integer>>> result = new HashMap<>();

        // calculate each method component
        for (MethodComponent c : listOfComponent) {
            // store result of each component in a hashmap
            HashMap<Integer, List<Integer>> groupingInfoByComponent = new HashMap<>();
            for (int idx = 0; idx < c.roots.size(); idx ++ ) { // iterate every disjoint set id
                int currentSetId = c.roots.get(idx);
                if (linkToHigerLevel.containsKey(currentSetId)) {
                    // set key as the higher level group number, value as the changed line code
                    List<Integer> currList;
                    if (groupingInfoByComponent.containsKey(linkToHigerLevel.get(currentSetId))) {
                        currList = groupingInfoByComponent.get(linkToHigerLevel.get(currentSetId));
                    } else {
                        currList = new ArrayList<>();
                    }
                    currList.add(c.lineNumbers.get(idx));
                    groupingInfoByComponent.put(linkToHigerLevel.get(currentSetId), currList);
                }
            }
            // add the result of a component to final result
            addMethodToFinalResult(result,groupingInfoByComponent );
        }

        return result;
    }


    // add the result of each component to the final result
    private void addMethodToFinalResult(HashMap<Integer, List<List<Integer>>> result, HashMap<Integer, List<Integer>> componentResult) {
        for (Integer higerLevelKey : componentResult.keySet()) {
            List<List<Integer>>  finalList = new ArrayList<>();
            if (result.containsKey(higerLevelKey)) {
                finalList = result.get(higerLevelKey);
            } else {
                finalList = new ArrayList<>();
            }
            List<Integer> relatedLineNumberByHigerGroup = componentResult.get(higerLevelKey);
            finalList.add(new ArrayList<>(relatedLineNumberByHigerGroup));
            result.put(higerLevelKey, finalList);
        }
    }

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
//        String linkFileName = "/Users/jiayuesun/2022/SP-2022/CLDIFF/output/JavaCLIDiff/785b5c927f4396e6b2562e617492904a7664c853/link.json";
        String linkFileName = "/Users/junyu/Desktop/2022spring/cs230/Project/CLDIFF/output/CLIDIFFTEST/9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713/link.json";
//        String groupingFileName = "/Users/jiayuesun/2022/SP-2022/CLDIFF/src/edu/ucla/se/utils/grouping.txt";
        String groupingFileName = "/Users/junyu/Desktop/2022spring/cs230/Project/CLDIFF/src/edu/ucla/se/utils/grouping.txt";
        String sourceCodeFile = "A";
        ParserHelper ph = new ParserHelper();
        // Parse json file and generate unionfind/disjoint set
        ph.parseLinkJson(linkFileName);
        // parse function and generate function groups
        List<List<Integer>> result = ph.parseDiffFile(groupingFileName, sourceCodeFile+".");


        // TODO Get disjoint id --> higher-level group id
        HashMap<Integer, Integer> linkToHigerLevel = new HashMap<>();
        // TODO Add some fake data
        linkToHigerLevel.put(0, 6);
        linkToHigerLevel.put(1, 5);

        //
        HashMap<Integer, List<List<Integer>>> resultOfGroup = ph.getGroupedLines(linkToHigerLevel);
        System.out.println(resultOfGroup);
    }
}

