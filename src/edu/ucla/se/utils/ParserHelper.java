package edu.ucla.se.utils;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParserHelper {
    private UnionFind uf;
    public List<MethodComponent> listOfComponent;
    public ParserHelper() {
        uf = new UnionFind();
        listOfComponent = new ArrayList<>();
    }

    public Integer getEditId(Integer method_id, Integer edit_pos){
        return listOfComponent.get(method_id).changeIndex.get(edit_pos);
    }

    public static  HashMap<Integer, HashMap<String, List<List<Integer>>>>  mapConverter( HashMap<String, HashMap<Integer, List<List<Integer>>>> origin) {
        HashMap<Integer, HashMap<String, List<List<Integer>>>> result = new HashMap<>();
        for (String fileName : origin.keySet()) {
            String[] namePath = fileName.split("/");
            String subFileName = namePath[namePath.length - 1];
            // for each file name
            HashMap<Integer, List<List<Integer>>> temp = origin.get(fileName);
            for (Integer k : temp.keySet()) {
                List<List<Integer>> changes = temp.get(k);
                HashMap<String, List<List<Integer>>> tempMap = new HashMap<>();
                tempMap.put(subFileName, changes);
                result.put(k, tempMap);
            }
        }

        return result;
    }
    public static List<String> parseMetaJson(String repoName, String commitId) throws IOException, ParseException, org.json.simple.parser.ParseException {
        String fileName = Paths.get(Config.CLDIFF_OUTPUT_PATH, repoName, commitId, "meta.json").toString();
        System.out.println("Link json path " + fileName);
        List<String> result = new ArrayList<>();
        JSONArray ob = (JSONArray)((JSONObject) new JSONParser().parse(new FileReader(fileName))).get("files");
        for (int i = 0; i < ob.size(); i++) {
            JSONObject temp = (JSONObject) ob.get(i);
            String s = (String) temp.get("prev_file_path");
            if (s != null && s.contains("java")) {
                result.add(s);
            }
        }

        return result;
    }

    public void parseLinkJson(String fileName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        JSONArray ob = (JSONArray)((JSONObject) new JSONParser().parse(new FileReader(fileName))).get("links");
        for (int j = 0; j < ob.size(); j ++) {
            JSONArray subob = (JSONArray) ((JSONObject) ob.get(j)).get("links");
            for (int i = 0; i < subob.size(); i++) {
                JSONObject temp = (JSONObject) subob.get(i);
                int from = ((Long) temp.get("from")).intValue();
                int to = ((Long) temp.get("to")).intValue();
                String changeType =(String) temp.get("desc");
                if (changeType.contains("Systematic")) {
                    uf.add(from);
                    uf.add(to);
                    uf.merge(from, to);
//                System.out.println(changeType);
                }

            }
        }
        //        JsonObject jsonObject = new JsonParser().parse(fileName).getAsJsonObject();
//        Object obj = parser.parse(new FileReader());


        System.out.println();
    }

    public List<List<Integer>> parseDiffFile(String filename, String sourceFileName) {
        sourceFileName = sourceFileName.substring(0, sourceFileName.length() - 4);
        boolean flag = true;
        MethodComponent currComponent = new MethodComponent(uf);
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            boolean dirty = false; // true when we find an object

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                List<String> list = Arrays.asList(data.split(" "));
                if (list.size() == 1) {
                    // finish reading a component
                    if (dirty) {
                        currComponent.calculateRoots();
                        listOfComponent.add(currComponent);
                    }
                    flag = false;
                    dirty = false;

                    continue;
                } else if (list.get(0).equals(sourceFileName)) {
                    // start a new component
                    flag = true;
                    dirty = true;
                    currComponent = new MethodComponent(uf);
                    currComponent.setFunctionName(list.get(2));
                    continue;
                } else if (list.get(0).charAt(0) < '0' || list.get(0).charAt(0) > '9'){ // not a link, but another file name
                    dirty = false;
                    continue;
                }

                if (!dirty) {
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

            if (dirty) {
                currComponent.calculateRoots();
                listOfComponent.add(currComponent);
            }

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

    private List<Integer> methodLocsToEditNumbers (List<Integer> curLocs){
        Integer method_loc = curLocs.get(0);
        Integer init_loc = curLocs.get(1);
        Integer cur_loc = curLocs.get(2);
        List<Integer> editNumberList = new ArrayList<>(IntStream.range(init_loc, cur_loc)
                .mapToObj(pos -> this.getEditId(method_loc, pos))
                .collect(Collectors.toList()));
        return editNumberList;
    }

    public HashMap<Integer, List<List<Integer>>> getGroupedLines(HashMap<List<Integer>, Set<List<Integer>>> groupLocs) {
        HashMap<Integer, List<List<Integer>>> result = new HashMap<>();

        // calculate each method component
        int group_i = 0;
        for (List<Integer> group : groupLocs.keySet()){
            List<List<Integer>> editNumberLists = new ArrayList<>();
            for (List<Integer> curLocs : groupLocs.get(group)){
                List<Integer> editNumberList = this.methodLocsToEditNumbers(curLocs);
                editNumberLists.add(editNumberList);
            }
            result.put(group_i, editNumberLists);
            group_i++;
        }

        return result;
    }

    public static HashMap<Integer, List<List<Integer>>> parseSingleFile(String commitId, String parsedName, String repoName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("start parse");
        String linkFileName = Paths.get(Config.CLDIFF_OUTPUT_PATH, repoName, commitId, "link.json").toString();
        String groupingFileName = "./src/edu/ucla/se/utils/grouping_testpatch1.txt";
        // For example sourceCodeFile = xxx.java
        String[] sourceCodePath = parsedName.split("/");
        String sourceCodeFile = sourceCodePath[sourceCodePath.length - 1];
        ParserHelper ph = new ParserHelper();
        // Parse json file and generate unionfind/disjoint set
        ph.parseLinkJson(linkFileName);
        // Step1. parse function and generate function groups

        List<List<Integer>> changeLinksDisjointSetId = ph.parseDiffFile(groupingFileName, sourceCodeFile);

        /**
         *
         *
         previous result:

         0: [0， 2， 3， 4, 6， 7】

         1: [1, 5]

         addNumber1: 0, 1
         addNumber2: 0, 0
         addNumber3: 0, 1
         addNumber4: 0, 0

         1. [5, 5]
         2. [0, 2, 3]
         3. [0, 2]
         */


        GroupLinkedDiffs linkedDiffGrouper = new GroupLinkedDiffs(changeLinksDisjointSetId);
        HashMap<List<Integer>, Set<List<Integer>>> linkToHigerLevel = linkedDiffGrouper.getLinkedStmtGroups();

        //
        HashMap<Integer, List<List<Integer>>> resultOfGroup = ph.getGroupedLines(linkToHigerLevel);
        System.out.println(resultOfGroup);
        return resultOfGroup;
    }

    /**
     * The entry point of getting group info
     * @param repoName the name of the repo
     * @param commitId current commit id
     * @return grouping information to be used by step 2
     * @throws IOException
     * @throws ParseException
     * @throws org.json.simple.parser.ParseException
     */
    public static HashMap<Integer, HashMap<String, List<List<Integer>>>> getChangeGroups (String repoName, String commitId)
            throws IOException, ParseException, org.json.simple.parser.ParseException {
        List<String> listOfChangedFileNames = parseMetaJson(repoName, commitId);
        System.out.println("All file names: " + listOfChangedFileNames);
        HashMap<String, HashMap<Integer, List<List<Integer>>>> systematicChangeGroup = new HashMap<>();
        for (String file : listOfChangedFileNames) {
            systematicChangeGroup.put(file, parseSingleFile(commitId, file, repoName));
        }
        HashMap<Integer, HashMap<String, List<List<Integer>>>> groupChanges = mapConverter(systematicChangeGroup);
        System.out.println("All grouped changes: " + groupChanges);
        return groupChanges;
    }

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {

        String linkFileName = "./output/CLIDIFFTEST/9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713/link.json";
        String groupingFileName = "./src/edu/ucla/se/utils/grouping.txt";
//        String groupingFileName = "/Users/junyu/Desktop/2022spring/cs230/Project/CLDIFF/src/edu/ucla/se/utils/grouping.txt";
        String metaFileName = "./CLIDIFFTEST/9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713/meta.json";
        String sourceCodeFile = "A.java";
        ParserHelper ph = new ParserHelper();
        // Parse json file and generate unionfind/disjoint set
        ph.parseLinkJson(linkFileName);
        // parse function and generate function groups
        List<List<Integer>> result = ph.parseDiffFile(groupingFileName, sourceCodeFile);
//        List<String> changedFileNames = ph.parseMetaJson(metaFileName);

//        System.out.println(changedFileNames);
        /**
         *
         *
         previous result:

         0: [0， 2， 3， 4, 6， 7】

         1: [1, 5]

         addNumber1: 0, 1
         addNumber2: 0, 0
         addNumber3: 0, 1
         addNumber4: 0, 0

         */

        GroupLinkedDiffs linkedDiffGrouper = new GroupLinkedDiffs(result);
        HashMap<List<Integer>, Set<List<Integer>>> linkToHigerLevel = linkedDiffGrouper.getLinkedStmtGroups();

        //
        HashMap<Integer, List<List<Integer>>> resultOfGroup = ph.getGroupedLines(linkToHigerLevel);
        System.out.println(resultOfGroup);
    }
}

