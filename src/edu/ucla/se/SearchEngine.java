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

    private class Line {
        int start;
        int end;

        Line(int s, int e) {
            start = s;
            end = e;
        }
    }

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
                    List<Line> curAllLines = new ArrayList<>();
                    String contents = getFileContent(curAllLines, curFilePath.toString());
//                    System.out.println(curFilePath);
//                    int idx = 0;
//                    for (Line line : curAllLines) System.out.printf("Line %d, Start: %d, End: %d, %s\n", idx++, line.start, line.end,
//                            contents.substring(line.start, line.end+1));
//                    System.out.println();
                    Matcher curMatcher = curPattern.matcher(contents);
                    while (curMatcher.find()) {
                        int startIdx = curMatcher.start();
                        int endIdx = curMatcher.end() - 1;
                        int startLine = getLineNumber(startIdx, curAllLines);
                        int endLine = getLineNumber(endIdx, curAllLines);
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


    private String getFileContent(List<Line> lines, String filePath) {
        try {
            FileInputStream in = new FileInputStream(filePath);
            StringBuilder sb = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            int rt;
            int curStart = 0, curEnd = -1;
            while((rt = br.read()) != -1){
                char cur = (char) rt;
                sb.append(cur);
                curEnd++;
                if (rt == 10) {
                    lines.add(new Line(curStart, curEnd));
                    curStart = curEnd + 1;
                }
            }
            if (curEnd >= curStart) lines.add(new Line(curStart, curEnd));
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private int getLineNumber(int idx, List<Line> lines) {
        int low = 0, high = lines.size();

        while (low < high) {
            int mid = low + (high - low) / 2;
            Line midLine = lines.get(mid);
            if (idx >= midLine.start && idx <= midLine.end) return mid + 1;
            else if (idx < midLine.start) high = mid - 1;
            else low = mid + 1;
        }

        return low + 1;
    }
}
