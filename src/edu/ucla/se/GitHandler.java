package edu.ucla.se;

import com.github.gumtreediff.tree.Tree;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHandler {
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
     * get all files in the older version
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
                    String filePath = treeWalk.getPathString();
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
}
