package edu.ucla.se;

import edu.ucla.se.utils.Config;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GitCreator {
    private Map<String, Repository> repos;

    public GitCreator () {
        File repoDir = new File(Config.REPO_PATH);
        if (!repoDir.exists()) repoDir.mkdir();
        repos = new HashMap<>();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            for (String repoName : repoDir.list()) {
                String curRepoPath = Paths.get(Config.REPO_PATH, repoName, "\\.git").toString();
                Repository curRepo = builder.setGitDir(new File(curRepoPath)).readEnvironment().findGitDir().build();
                repos.put(repoName, curRepo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new empty repository
     * @param repoName the name of the repo
     */
    public void createNewRepo(String repoName) {
        if (repos.containsKey(repoName)) {
            System.out.printf("Repository %s already exists!\n", repoName);
            return;
        }

        File curRepoDir = new File(Paths.get(Config.REPO_PATH, repoName).toString());
        if (!curRepoDir.exists()) curRepoDir.mkdir();

        try (Git git = Git.init().setDirectory(curRepoDir).call()) {
            System.out.printf("Created a new repo %s.\n", curRepoDir.getPath());
            repos.put(repoName, git.getRepository());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit files to a created repo
     * @param repoName the name of the repo to which the files are committed
     * @param filePath the path of the files to be committed
     */
    public String commitFilesToRepo(String repoName, String filePath) {
        if (!repos.containsKey(repoName)) {
            System.out.printf("Repo %s does not exist!\n", repoName);
            return null;
        }
        filePath = Paths.get(filePath).toString();
        String repoPath = Paths.get(Config.REPO_PATH, repoName).toString();
        File srcDir = new File(filePath);
        File dstDir = new File(repoPath);
        Git git = new Git(repos.get(repoName));
        String localPath = Paths.get("./").toString();

        try {
            FileUtils.copyDirectory(srcDir, dstDir);
            git.add().addFilepattern(localPath).call();
            RevCommit revCommit = git.commit().setMessage("Commit files at " + filePath).call();
            String commitId = revCommit.getId().toString().split(" ")[1];
            System.out.printf("Created in repo %s a new commit %s\n", repoName, commitId);
            return commitId;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Delete a repository
     * @param repoName the name of the repo to be deleted
     */
    public void deleteRepo(String repoName) {
        if (!repos.containsKey(repoName)) {
            System.out.printf("Repo %s does not exist!\n", repoName);
            return;
        }
        String curRepoPath = Paths.get(Config.REPO_PATH, repoName).toString();
        File curRepoDir = new File(curRepoPath);
        try {
            FileUtils.deleteDirectory(curRepoDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        repos.remove(repoName);
        return;
    }

    /**
     * Get the path to the specified repository
     * @param repoName the name of the repo
     * @return the path to the specified repo
     */
    public String getRepoPath(String repoName) {
        if (!repos.containsKey(repoName)) {
            System.out.printf("Repo %s does not exist!\n", repoName);
            return null;
        }

        return Paths.get(Config.REPO_PATH, repoName).toString();
    }


    /**
     * Get the Repository object of the specified repo
     * @param repoName the name of the repo
     * @return the Repository object
     */
    public Repository getRepo(String repoName) {
        return repos.get(repoName);
    }
}
