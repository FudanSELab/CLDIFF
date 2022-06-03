package edu.ucla.se;

import com.github.gumtreediff.tree.Tree;
import edu.ucla.se.utils.Config;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHandler {
    public class Line {
        int start;
        int end;

        Line(int s, int e) {
            start = s;
            end = e;
        }
    }

    String repoPath;
    private Repository repository;
    private RevWalk revWalk;
    private RevCommit curCommit;
    private P_LANG lang;

    public GitHandler(String repoPath, String commitId, P_LANG lang) {
        this.repoPath = repoPath;
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder.setGitDir(new File(repoPath)).readEnvironment().findGitDir().build();
            revWalk = new RevWalk(repository);
            curCommit = revWalk.parseCommit(ObjectId.fromString(commitId));
            this.lang = lang;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GitHandler(Repository repository, String repoName, String commitId, P_LANG lang) {
        this.repoPath = Paths.get(Config.REPO_PATH, repoName).toString();
        this.repository = repository;
        try {
            this.revWalk = new RevWalk(repository);
            curCommit = revWalk.parseCommit(ObjectId.fromString(commitId));
            this.lang = lang;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the path of all files in the older version
     */
    public List<String> getOldFiles() {
        RevCommit[] parents = curCommit.getParents();
        String fileExtension = lang.getExtension();
        List<String> allSrcFiles = new ArrayList<>();

        for (RevCommit parent : parents) {
            try {
                RevCommit parentCommit = revWalk.parseCommit(parent.getId());
                RevTree tree = parentCommit.getTree();
                TreeWalk treeWalk = new TreeWalk(repository);
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                while (treeWalk.next()) {
                    String filePath = treeWalk.getNameString();
                    int dotIdx = filePath.lastIndexOf(".");
                    if (dotIdx > 0 && filePath.substring(dotIdx + 1).equals(fileExtension))
                        allSrcFiles.add(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return allSrcFiles;
    }


    /**
     * Get the content of files in the parent commit specified by file names and line numbers
     * @param lines the map from file name to a list of line numbers
     * @return the map from file name to a map from line number to contents
     */
    public Map<String, Map<Integer, String>> getOldFileContentByLine(Map<String, List<List<Integer>>> lines) {
        // flatten the lines
        Map<String, List<Integer>> allLines = new HashMap<>();
        for (Map.Entry<String, List<List<Integer>>> entry : lines.entrySet()) {
            List<List<Integer>> curGroupedLines = entry.getValue();
            List<Integer> curAllLines = new ArrayList<>();
            for (List<Integer> curGroupLine : curGroupedLines) curAllLines.addAll(curGroupLine);
            allLines.put(entry.getKey(), curAllLines);
        }

        Map<String, Map<Integer, String>> fileLineContentMap = new HashMap<>();
        RevCommit[] parents = curCommit.getParents();

        for (RevCommit parent : parents) {
            try {
                RevCommit parentCommit = revWalk.parseCommit(parent.getId());
                RevTree tree = parentCommit.getTree();
                TreeWalk treeWalk = new TreeWalk(repository);
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                while (treeWalk.next()) {
                    if (!allLines.containsKey(treeWalk.getPathString())) continue;
                    ObjectId curEntryId = treeWalk.getObjectId(0);
                    try (ObjectReader objectReader = repository.newObjectReader()) {
                        ObjectLoader objectLoader = objectReader.open(curEntryId);
                        InputStream in = objectLoader.openStream();
                        fileLineContentMap.put(treeWalk.getPathString(),
                                getLineContentMap(allLines.get(treeWalk.getPathString()), in));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fileLineContentMap;
    }

    /**
     * Get the content of files in current commit specified by file names and line numbers
     * @param lines the map from file name to a list of line numbers
     * @return the map from file name to a map from line number to contents
     */
    private Map<String, Map<Integer, String>> getFileContentByLine(Map<String, List<Integer>> lines) {
        Map<String, Map<Integer, String>> fileLineContentMap = new HashMap<>();
        try {
            RevTree tree = curCommit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                if (!lines.containsKey(treeWalk.getPathString())) continue;
                ObjectId curEntryId = treeWalk.getObjectId(0);
                try (ObjectReader objectReader = repository.newObjectReader()) {
                    ObjectLoader objectLoader = objectReader.open(curEntryId);
                    InputStream in = objectLoader.openStream();
                    fileLineContentMap.put(treeWalk.getPathString(),
                            getLineContentMap(lines.get(treeWalk.getPathString()), in));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileLineContentMap;
    }


    /**
     * Get the paths of all source files in current commit
     * @return the list of paths of all source files
     */
    public List<String> getFiles() {
        RevTree tree = curCommit.getTree();
        String fileExtension = lang.getExtension();
        List<String> allSrcFiles = new ArrayList<>();

        try {
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                String filePath = treeWalk.getPathString();
                int dotIdx = filePath.lastIndexOf(".");
                if (dotIdx > 0 && filePath.substring(dotIdx + 1).equals(fileExtension))
                    allSrcFiles.add(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSrcFiles;
    }

    /**
     * Retrieves the contents of a file in current commit and the range of each line
     * @param lines the list of range (start index, end index) of each line
     * @param in the input stream of the file
     * @return the contents of the file as a String
     */
    public String getFileContentWithLines(List<Line> lines, InputStream in) {
        try {
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
            in.close();
            return sb.toString();
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Get the corresponding line number in the file for an index
     * @param idx the index whose corresponding line number is to be searched
     * @param lines the list of range of each line
     * @return the corresponding line number
     */
    public int getLineNumber(int idx, List<Line> lines) {
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


    /**
     * Map a line number to the content of the line in the file
     * @param in the file input stream
     * @return the map of line number to content
     */
    private Map<Integer, String> getLineContentMap(List<Integer> lines, InputStream in) {
        Map<Integer, String> lcMap = new HashMap<>();
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        int rt;
        int curLine = 1, lineIdx = 0;
        try {
            while ((rt = br.read()) != -1) {
                char cur = (char) rt;
                sb.append(cur);
                if (rt == 10) {
                    if (curLine == lines.get(lineIdx)) {
                        lineIdx++;
                        lcMap.put(curLine, sb.toString());
                        if (lineIdx >= lines.size()) break;
                    }
                    sb = new StringBuilder();
                    curLine++;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lcMap;
    }


    /**
     * Match regex to all source files in current commit
     * @param regex the list of regex to be matched
     * @return the map from file name to all possible missing change it contains
     * @throws IOException
     */
    public Map<String, Map<Integer, List<MissingChangeInfo>>> matchRegex(List<String> regex) throws IOException {
        Map<String, Map<Integer, List<MissingChangeInfo>>> rs = new HashMap<>();

        for (int groupId = 0; groupId < regex.size(); groupId++) {
            String r = regex.get(groupId);

            System.out.printf("Matching regex %s of group %d\n", r, groupId);
            java.util.regex.Pattern curPattern = Pattern.compile(r);
            RevTree tree = curCommit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);

            while (treeWalk.next()) {
                String curFileName = treeWalk.getNameString();
                int dotIdx = curFileName.lastIndexOf(".");
                if (dotIdx < 0 || !curFileName.substring(dotIdx + 1).equals(lang.getExtension())) continue;

                try {
                    ObjectId curEntryId = treeWalk.getObjectId(0);
                    ObjectReader objectReader = repository.newObjectReader();
                    ObjectLoader objectLoader = objectReader.open(curEntryId);
                    InputStream in = objectLoader.openStream();
                    List<Line> curAllLines = new ArrayList<>();
                    String contents = getFileContentWithLines(curAllLines, in);
                    Matcher curMatcher = curPattern.matcher(contents);
                    while (curMatcher.find()) {
                        int startIdx = curMatcher.start();
                        int endIdx = curMatcher.end() - 1;
                        int startLine = getLineNumber(startIdx, curAllLines);
                        int endLine = getLineNumber(endIdx, curAllLines);
                        rs.computeIfAbsent(treeWalk.getPathString(), k -> new HashMap<>())
                                .computeIfAbsent(groupId, k -> new ArrayList<>())
                                .add(new MissingChangeInfo(startLine, endLine));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rs;
    }

    /**
     * Apply LCS match to  all source files in current commit
     * @param peam a PEAM object
     * @param max_interval
     * @param min_stmt_cnt
     * @param min_hit_patterns
     * @param match_score
     * @return the map from file name to all possible missing changes it contains
     * @throws IOException
     */
    public Map<String, List<MissingChangeInfo>> matchLCS(PEAM peam,
                                                         int max_interval,
                                                         int min_stmt_cnt,
                                                         int min_hit_patterns,
                                                         double match_score) throws IOException {
        Map<String, List<MissingChangeInfo>> rs = new HashMap<>();
        RevTree tree = curCommit.getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        while (treeWalk.next()) {
            String curPathName = treeWalk.getPathString();
            int dotIdx = curPathName.lastIndexOf(".");
            if (dotIdx < 0 || !curPathName.substring(dotIdx + 1).equals(lang.getExtension())) continue;
            Path curFilePath = Paths.get(repoPath, curPathName);
            rs.putAll(peam.FindMatch(curFilePath, max_interval, min_stmt_cnt, min_hit_patterns, match_score));
        }

        return rs;
    }


    /**
     * Format and print possible missing change found without line contents
     * @param rs the missing change found by SearchEngine
     */
    public void printSimpleResult(Map<String, Map<Integer, List<MissingChangeInfo>>> rs) {
        for (Map.Entry<String, Map<Integer, List<MissingChangeInfo>>> entry : rs.entrySet()) {
            String curFile = entry.getKey();
            Map<Integer, List<MissingChangeInfo>> groupedChanges = entry.getValue();
            System.out.printf("Possible missing changes in %s:\n", curFile);

            for (Map.Entry<Integer, List<MissingChangeInfo>> curChangeGroup : groupedChanges.entrySet()) {
                int groupid = curChangeGroup.getKey();
                List<MissingChangeInfo> missingChanges = curChangeGroup.getValue();
                System.out.printf("\tgroup %d:\n", groupid);

                for (MissingChangeInfo info : missingChanges) {
                    System.out.printf("\t\t(line %d - line %d:)\n", info.startLine, info.endLine);
                }
            }
        }
    }

    /**
     * Format and print out all possible missing changes found
     * @param rs the missing changes found by SearchEngine
     */
    public void printResult(Map<String, Map<Integer, List<MissingChangeInfo>>> rs) {
        Map<String, List<Integer>> linesToBeReadPerFile = new HashMap<>();
        for (Map.Entry<String, Map<Integer, List<MissingChangeInfo>>> entry : rs.entrySet()) {
            String fileName = entry.getKey();
            Map<Integer, List<MissingChangeInfo>> groups = entry.getValue();
            SortedSet<Integer> lineSet = new TreeSet<>();
            for (List<MissingChangeInfo> groupInfo : groups.values()) {
                for (MissingChangeInfo info : groupInfo)
                    for (int line = info.startLine; line <= info.endLine; line++) lineSet.add(line);
            }
            List<Integer> allLines = new ArrayList<>();
            for (int line : lineSet) allLines.add(line);
            linesToBeReadPerFile.put(fileName, allLines);
        }

        Map<String, Map<Integer, String>> lineContents = getFileContentByLine(linesToBeReadPerFile);

        for (Map.Entry<String, Map<Integer, List<MissingChangeInfo>>> entry : rs.entrySet()) {
            String curFile = entry.getKey();
            Map<Integer, List<MissingChangeInfo>> groupedChanges = entry.getValue();
            System.out.printf("Possible missing changes in %s:\n", curFile);

            for (Map.Entry<Integer, List<MissingChangeInfo>> curChangeGroup : groupedChanges.entrySet()) {
                int groupid = curChangeGroup.getKey();
                List<MissingChangeInfo> missingChanges = curChangeGroup.getValue();
                System.out.printf("\tgroup %d:\n", groupid);

                for (MissingChangeInfo info : missingChanges) {
                    System.out.printf("\t\t(line %d - line %d:)\n", info.startLine, info.endLine);
                    for (int line = info.startLine; line <= info.endLine; line++) {
                        String lineContent = lineContents.get(curFile).get(line);
                        System.out.printf("\t\t-> %s\n", lineContent);
                    }
                    System.out.println();
                }
            }
        }
    }
}
