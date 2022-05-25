package edu.ucla.se;

import java.io.IOException;
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

    public Map<String, List<String>> run(List<String> regex) {
        List<String> allSrcFiles = gitHandler.getFiles();
        Map<String, List<String>> rs = new HashMap<>();

        for (String r : regex) {
            Pattern curPattern = Pattern.compile(r);

            for (String filePath : allSrcFiles) {
                if (filePath == null) continue;
                try {
                    Path curFilePath = Paths.get(repoPath, filePath);
                    byte[] allBytes = Files.readAllBytes(curFilePath);
                    String contents = new String(allBytes);
                    Matcher curMatcher = curPattern.matcher(contents);
                    while (curMatcher.find()) {
                        rs.computeIfAbsent(filePath, k->new ArrayList<>()).add(curMatcher.group());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rs;
    }

}
