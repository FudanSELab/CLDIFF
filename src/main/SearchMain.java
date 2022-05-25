package main;

import edu.ucla.se.GenerateRegex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.ucla.se.GitHandler;
import edu.ucla.se.P_LANG;

import java.nio.file.Paths;

public class SearchMain {
    public static void main(String[] args) {
//        System.out.println("Start running search...");
//
//        HashMap<String, List<Integer>> fileToLine = new HashMap<>();
//        fileToLine.put("a.java", Arrays.asList(7,8));
//        fileToLine.put("b.java", Arrays.asList(7,8));
//
//        HashMap<Integer, HashMap<String, List<Integer>>> group = new HashMap<>();
//        group.put(1, fileToLine);
//
//        HashMap<Integer, String> a = new HashMap<>();
//        a.put(7, "event.item = item;");
//        a.put(8, "sendEvent(true, event);");
//
//        HashMap<Integer, String> b = new HashMap<>();
//        b.put(7, "event.item = item;");
//        b.put(8, "sendEvent(true, ev);");
//
//        HashMap<String, HashMap<Integer, String>> dict = new HashMap<>();
//        dict.put("a.java", a);
//        dict.put("b.java", b);
//
//        GenerateRegex generator = new GenerateRegex(group, dict);
//        HashMap<Integer,ArrayList<ArrayList<String>>> code = generator.getCodeSnippet();
//        System.out.println(code);

        if (args.length != 2) {
            System.err.println("Usage [Repo Path] [Commit Id].\n");
            System.exit(1);
        }

        String repoPath = Paths.get(args[0]).toString();
        String commitId = args[1];

        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);

        GitHandler handler = new GitHandler(repoPath, commitId, P_LANG.JAVASCRIPT);

        List<String> oldVersionFiles =  handler.getOldFiles();

        for (String file : oldVersionFiles) System.out.println(file);

    }
}
