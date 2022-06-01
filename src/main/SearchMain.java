package main;

import java.util.*;

import edu.ucla.se.*;

import java.nio.file.Paths;

public class SearchMain {
    public static void main(String[] args) {
        System.out.println("Start running search...");

        String oldPath = "../DataSet/testPatch/old";
        String newPath = "../DataSet/testPatch/new";

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
