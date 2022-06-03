package main;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import edu.ucla.se.*;
import edu.ucla.se.utils.ParserHelper;

import java.nio.file.Paths;

public class SearchMain {

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("Start running search...");

        String oldPath = "./DataSet/Patch1/NEW_JDT9801";
        String newPath = "./DataSet/Patch1/OLD_JDT9800";

        // Get all changed old file names
        // TOOD
//        String repoName = "CLIDIFFTEST";
//        String commitId = "9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713";
//
//        List<String> listOfChangedFileNames = ParserHelper.parseMetaJson(repoName, commitId);
//        System.out.println("All file names : " + listOfChangedFileNames);
//        HashMap<String, HashMap<Integer, List<List<Integer>>>> systematicChangeGroup = new HashMap<>();
//        for (String file : listOfChangedFileNames) {
//            systematicChangeGroup.put(file, ParserHelper.parseSingleFile(commitId, file, repoName));
//        }
//        System.out.println("All grouped changes : " + systematicChangeGroup);
//        HashMap<Integer, HashMap<String, List<List<Integer>>>> groupChanges = ParserHelper.mapConverter(systematicChangeGroup);
//        System.out.println("All grouped changes after converting : " + groupChanges);
//        HashMap<Integer, HashMap<String, List<List<Integer>>>> groups = new HashMap<>();
//        HashMap<String, List<List<Integer>>> curGroup = new HashMap<>();
//        List<Integer> change1 = new ArrayList<>(Arrays.asList(8, 9));
//        List<Integer> change2 = new ArrayList<>(Arrays.asList(29, 30));
//        List<Integer> change3 = new ArrayList<>(Arrays.asList(38, 39));
//        List<List<Integer>> curFile = new ArrayList<List<Integer>>(){
//            {
//                add(change1);
//                add(change2);
//                add(change3);
//            }
//        };
//        curGroup.put("A.java", curFile);
//        groups.put(0, curGroup);

        SearchEngine searchEngine = new SearchEngine(oldPath, newPath, "testpatch1", P_LANG.JAVA);
        searchEngine.run();
    }
}
