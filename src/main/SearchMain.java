package main;

import java.util.*;

import edu.ucla.se.*;

import java.nio.file.Paths;

public class SearchMain {
    public static void main(String[] args) {
        System.out.println("Start running search...");

        HashMap<String, List<List<Integer>>> fileToLine = new HashMap<>();
        List<List<Integer>> snippets = new ArrayList<>();
        snippets.add(Arrays.asList(1,2,3));
        snippets.add(Arrays.asList(7,8,9));
        fileToLine.put("a.java", snippets);

        HashMap<Integer, HashMap<String, List<List<Integer>>>> group = new HashMap<>();
        group.put(1, fileToLine);

        Map<Integer, String> a = new HashMap<>();
        a.put(1, "public RegexGenerator(HashMap<Integer, HashMap<String, List<List<Integer>>>> _grouping, Map<String,Map<Integer, String>> _dict){");
        a.put(2, "       this.grouping = _grouping;");
        a.put(3, "       this.dict = _dict;");
        a.put(7, "public RegexGenerator(HashMap<Integer, HashMap<String, List<Integer>>> _grouping, Map<String,Map<Integer, String>> _dict){");
        a.put(8, "       this.grouping = _grouping;");
        a.put(9, "       this.dict = _dict;");


        Map<String, Map<Integer, String>> dict = new HashMap<>();
        dict.put("a.java", a);

        RegexGenerator generator = new RegexGenerator(group, dict);
        ArrayList<String> regex = generator.generateRegex();
        System.out.println(regex);

//        ================================

//        if (args.length != 2) {
//            System.err.println("Usage [Repo Path] [Commit Id].\n");
//            System.exit(1);
//        }
//
//        String repoPath = Paths.get(args[0]).toString();
//        String commitId = args[1];
//
//        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);

//        ================================
//
//        SearchEngine engine = new SearchEngine(repoPath, commitId, P_LANG.JAVASCRIPT);
//
//        List<String> regex = new ArrayList<>();
//        regex.add(".*const.*\r\n.*db.*");
//
//        Map<String, List<MissingChangeInfo>> possibleRs = engine.run(regex);
//
//        for (Map.Entry<String, List<MissingChangeInfo>> entry : possibleRs.entrySet()) {
//            System.out.printf("Possible Missing Changes in %s:\n", entry.getKey());
//            for (MissingChangeInfo info : entry.getValue())
//                System.out.printf("(Line%d, Line%d), %s\n", info.getStartLine(), info.getEndLine(), info.getContent());
//        }

//        GitHandler gitHandler = new GitHandler(repoPath, commitId, P_LANG.JAVASCRIPT);
//
//        Map<String, List<Integer>> lines = new HashMap<>();
//        lines.computeIfAbsent("SinglePostDisplay.js", k->new ArrayList<>()).add(5);
//        lines.get("SinglePostDisplay.js").add(7);
//        lines.get("SinglePostDisplay.js").add(31);
//        lines.computeIfAbsent("Navbar.js", k->new ArrayList<>()).add(51);
//        lines.get("Navbar.js").add(55);
//        lines.get("Navbar.js").add(58);
//        lines.get("Navbar.js").add(63);
//
//        Map<String, Map<Integer, String>> oldContents = gitHandler.getOldFileContentByLine(lines);
//        for (Map.Entry<String, Map<Integer, String>> fileEntry : oldContents.entrySet()) {
//            System.out.println(fileEntry.getKey() + ':');
//            for (Map.Entry<Integer, String> lineEntry : fileEntry.getValue().entrySet()) {
//                System.out.printf("%d: %s\n", lineEntry.getKey(), lineEntry.getValue());
//            }
//        }
//
//        Map<String, Map<Integer, String>> contents = gitHandler.getFileContentByLine(lines);
//        for (Map.Entry<String, Map<Integer, String>> fileEntry : contents.entrySet()) {
//            System.out.println(fileEntry.getKey() + ':');
//            for (Map.Entry<Integer, String> lineEntry : fileEntry.getValue().entrySet()) {
//                System.out.printf("%d: %s\n", lineEntry.getKey(), lineEntry.getValue());
//            }
//        }

    }
}
