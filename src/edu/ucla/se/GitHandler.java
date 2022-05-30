package edu.ucla.se;

import com.github.gumtreediff.tree.Tree;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitHandler {
    public class Line {
        int start;
        int end;

        Line(int s, int e) {
            start = s;
            end = e;
        }
    }

    private Repository repository;
    private RevWalk revWalk;
    private RevCommit curCommit;
    private Git git;
    private P_LANG lang;

    public GitHandler(String repoPath, String commitId, P_LANG lang) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder.setGitDir(new File(repoPath)).readEnvironment().findGitDir().build();
            revWalk = new RevWalk(repository);
            curCommit = revWalk.parseCommit(ObjectId.fromString(commitId));
            git = new Git(repository);
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
    public Map<String, Map<Integer, String>> getOldFileContentByLine(Map<String, List<Integer>> lines) {
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
                    if (!lines.containsKey(treeWalk.getNameString())) continue;
                    ObjectId curEntryId = treeWalk.getObjectId(0);
                    try (ObjectReader objectReader = repository.newObjectReader()) {
                        ObjectLoader objectLoader = objectReader.open(curEntryId);
                        InputStream in = objectLoader.openStream();
                        fileLineContentMap.put(treeWalk.getNameString(),
                                getLineContentMap(lines.get(treeWalk.getNameString()), in));
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
    public Map<String, Map<Integer, String>> getFileContentByLine(Map<String, List<Integer>> lines) {
        Map<String, Map<Integer, String>> fileLineContentMap = new HashMap<>();
        try {
            RevTree tree = curCommit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                if (!lines.containsKey(treeWalk.getNameString())) continue;
                ObjectId curEntryId = treeWalk.getObjectId(0);
                try (ObjectReader objectReader = repository.newObjectReader()) {
                    ObjectLoader objectLoader = objectReader.open(curEntryId);
                    InputStream in = objectLoader.openStream();
                    fileLineContentMap.put(treeWalk.getNameString(),
                            getLineContentMap(lines.get(treeWalk.getNameString()), in));
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
     * @param filePath the path of the file
     * @return the contents of the file as a String
     */
    public String getFileContentWithLines(List<Line> lines, String filePath) {
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
}
