package edu.ucla.se;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {

    private String repoPath;
    private String commitId;

    private String oldPath;
    private String newPath;

    private GitHandler gitHandler;

    public SearchEngine(String repoPath, String commitId, P_LANG lang) {
        this.repoPath = repoPath.substring(0, repoPath.length() - 5);
        this.commitId = commitId;
        this.gitHandler = new GitHandler(repoPath, commitId, lang);
    }

    public SearchEngine(String oldPath, String newPath, String repoName, P_LANG lang) {
        this.oldPath = oldPath;
        this.newPath = newPath;
        GitCreator gitCreator = new GitCreator();
        gitCreator.deleteRepo(repoName);
        gitCreator.createNewRepo(repoName);
        gitCreator.commitFilesToRepo(repoName, oldPath);
        String commitId = gitCreator.commitFilesToRepo(repoName, newPath);
        this.gitHandler = new GitHandler(gitCreator.getRepo(repoName), commitId, lang);
    }

    public void run() {
        HashMap<Integer, HashMap<String, List<Integer>>> groups = new HashMap<>();
        List<String> regex = generateRegex(groups);
        Map<String, List<MissingChangeInfo>> results = matchRegex(regex);

        for (Map.Entry<String, List<MissingChangeInfo>> entry : results.entrySet()) {
            System.out.printf("Possible Missing Changes in %s:\n", entry.getKey());
            for (MissingChangeInfo info : entry.getValue())
                System.out.printf("(Line%d, Line%d), %s\n", info.getStartLine(), info.getEndLine(), info.getContent());
            System.out.println();
        }
    }

    private List<String> generateRegex(HashMap<Integer, HashMap<String, List<Integer>>> grouping) {
        RegexGenerator regexGenerator = new RegexGenerator(grouping, this.gitHandler);
        return regexGenerator.generateRegex();
    }

    private Map<String, List<MissingChangeInfo>> matchRegex(List<String> regex) {
        List<String> allSrcFiles = gitHandler.getFiles();
        Map<String, List<MissingChangeInfo>> rs = new HashMap<>();

        for (String r : regex) {
            Pattern curPattern = Pattern.compile(r);

            for (String filePath : allSrcFiles) {
                if (filePath == null) continue;
                try {
                    Path curFilePath = Paths.get(repoPath, filePath);
                    List<GitHandler.Line> curAllLines = new ArrayList<>();
                    String contents = gitHandler.getFileContentWithLines(curAllLines, curFilePath.toString());
                    Matcher curMatcher = curPattern.matcher(contents);
                    while (curMatcher.find()) {
                        int startIdx = curMatcher.start();
                        int endIdx = curMatcher.end() - 1;
                        int startLine = gitHandler.getLineNumber(startIdx, curAllLines);
                        int endLine = gitHandler.getLineNumber(endIdx, curAllLines);
                        rs.computeIfAbsent(filePath, k->new ArrayList<>())
                                .add(new MissingChangeInfo(startLine, endLine, curMatcher.group()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rs;
    }



}
