package main;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import edu.ucla.se.*;
import edu.ucla.se.utils.ParserHelper;

import java.nio.file.Paths;

public class SearchMain {
    public static HashMap<Integer, List<List<Integer>>> parseSingleFile(String commitId, String parsedName, String repoName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("start parse");
        String linkFileName = "./output/" + repoName + "/" + commitId + "/link.json";
        String groupingFileName = "./src/edu/ucla/se/utils/grouping.txt";
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

         */

        // TODO Get disjoint id --> higher-level group id
        HashMap<Integer, Integer> linkToHigerLevel = new HashMap<>();
        // TODO Add some fake data
        linkToHigerLevel.put(0, 6);
        linkToHigerLevel.put(1, 5);

        //
        HashMap<Integer, List<List<Integer>>> resultOfGroup = ph.getGroupedLines(linkToHigerLevel);
        System.out.println(resultOfGroup);
        return resultOfGroup;
    }

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("Start running search...");

        String oldPath = "./DataSet/testPatch/old";
        String newPath = "./DataSet/testPatch/new";

        // Get all changed old file names
        String repoName = "CLIDIFFTEST";
        String commitId = "9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713";

        List<String> listOfChangedFileNames = ParserHelper.parseMetaJson(repoName, commitId);
        System.out.println("All file names : " + listOfChangedFileNames);
        HashMap<String, HashMap<Integer, List<List<Integer>>>> systematicChangeGroup = new HashMap<>();
        for (String file : listOfChangedFileNames) {
            systematicChangeGroup.put(file, SearchMain.parseSingleFile(commitId, file, repoName));
        }
        System.out.println("All grouped changes : " + systematicChangeGroup);
        SearchEngine searchEngine = new SearchEngine(oldPath, newPath, "abc", P_LANG.JAVA);
        HashMap<Integer, HashMap<String, List<List<Integer>>>> groups = new HashMap<>();
        HashMap<String, List<List<Integer>>> curGroup = new HashMap<>();
        List<Integer> change1 = new ArrayList<>(Arrays.asList(8, 9));
        List<Integer> change2 = new ArrayList<>(Arrays.asList(29, 30));
        List<Integer> change3 = new ArrayList<>(Arrays.asList(38, 39));
        List<List<Integer>> curFile = new ArrayList<List<Integer>>(){
            {
                add(change1);
                add(change2);
                add(change3);
            }
        };
        curGroup.put("A.java", curFile);
        groups.put(0, curGroup);

        searchEngine.run(groups);
    }
}
