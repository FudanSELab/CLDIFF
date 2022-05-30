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
    private P_LANG lang;
    private GitHandler gitHandler;

    public SearchEngine(String repoPath, String commitId, P_LANG lang) {
        this.repoPath = repoPath.substring(0, repoPath.length() - 5);
        this.commitId = commitId;
        this.lang = lang;
        this.gitHandler = new GitHandler(repoPath, commitId, lang);
    }

    public Map<String, List<MissingChangeInfo>> run(List<String> regex) {
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
//                    System.out.println(curFilePath);
//                    int idx = 0;
//                    for (Line line : curAllLines) System.out.printf("Line %d, Start: %d, End: %d, %s\n", idx++, line.start, line.end,
//                            contents.substring(line.start, line.end+1));
//                    System.out.println();
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
