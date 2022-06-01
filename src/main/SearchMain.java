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
        searchEngine.run();
    }
}
