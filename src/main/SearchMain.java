package main;

import java.util.*;

import edu.ucla.se.*;

import java.nio.file.Paths;

public class SearchMain {
    public static void main(String[] args) {
        System.out.println("Start running search...");

//        HashMap<String, List<Integer>> fileToLine = new HashMap<>();
//        fileToLine.put("a.java", Arrays.asList(7,8));
//        fileToLine.put("b.java", Arrays.asList(7,8));
//
//        HashMap<Integer, HashMap<String, List<Integer>>> group = new HashMap<>();
//        group.put(1, fileToLine);
//
//        HashMap<Integer, String> a = new HashMap<>();
//        a.put(7, "for (int i = 0; i < n; ++i){");
//        a.put(8, "sendEvent(true, event);");
//
//        HashMap<Integer, String> b = new HashMap<>();
//        b.put(7, "for (int j = 0; j < arr.length(); j++){");
//        b.put(8, "sendEvent(true, ev);");

//        HashMap<String, List<Integer>> fileToLine = new HashMap<>();
//        fileToLine.put("a.java", Arrays.asList(7,8));
//        fileToLine.put("b.java", Arrays.asList(7,8));
//
//        HashMap<Integer, HashMap<String, List<Integer>>> group = new HashMap<>();
//        group.put(1, fileToLine);
//
//        HashMap<Integer, String> a = new HashMap<>();
//        a.put(7, "else if (a && b) {");
//        a.put(8, "b++;");
//
//        HashMap<Integer, String> b = new HashMap<>();
//        b.put(7, "else if (a && d) {");
//        b.put(8, "d++;");
//
//        HashMap<String, HashMap<Integer, String>> dict = new HashMap<>();
//        dict.put("a.java", a);
//        dict.put("b.java", b);
//
//        RegexGenerator generator = new RegexGenerator(group, dict);
//        ArrayList<String> regex = generator.generateRegex();
//        System.out.println(regex);


        if (args.length != 2) {
            System.err.println("Usage [Repo Path] [Commit Id].\n");
            System.exit(1);
        }

        String repoPath = Paths.get(args[0]).toString();
        String commitId = args[1];

        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);
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

        GitHandler gitHandler = new GitHandler(repoPath, commitId, P_LANG.JAVASCRIPT);

        Map<String, List<Integer>> lines = new HashMap<>();
        lines.computeIfAbsent("SinglePostDisplay.js", k->new ArrayList<>()).add(5);
        lines.get("SinglePostDisplay.js").add(7);
        lines.get("SinglePostDisplay.js").add(31);
        lines.computeIfAbsent("Navbar.js", k->new ArrayList<>()).add(51);
        lines.get("Navbar.js").add(55);
        lines.get("Navbar.js").add(58);
        lines.get("Navbar.js").add(63);

        Map<String, Map<Integer, String>> oldContents = gitHandler.getOldFileContentByLine(lines);
        for (Map.Entry<String, Map<Integer, String>> fileEntry : oldContents.entrySet()) {
            System.out.println(fileEntry.getKey() + ':');
            for (Map.Entry<Integer, String> lineEntry : fileEntry.getValue().entrySet()) {
                System.out.printf("%d: %s\n", lineEntry.getKey(), lineEntry.getValue());
            }
        }

        Map<String, Map<Integer, String>> contents = gitHandler.getFileContentByLine(lines);
        for (Map.Entry<String, Map<Integer, String>> fileEntry : contents.entrySet()) {
            System.out.println(fileEntry.getKey() + ':');
            for (Map.Entry<Integer, String> lineEntry : fileEntry.getValue().entrySet()) {
                System.out.printf("%d: %s\n", lineEntry.getKey(), lineEntry.getValue());
            }
        }

    }
}
