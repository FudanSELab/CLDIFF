package edu.ucla.se;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitHandler {
    private String repoPath;
    private String commitId;

    public Repository repository;
    public RevWalk revWalk;
    public Git git;

    public GitHandler(String repoPath, String commitId) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder.setGitDir(new File(repoPath)).readEnvironment().findGitDir().build();
            revWalk = new RevWalk(repository);
            git = new Git(repository);
            this.repoPath = repoPath;
            this.commitId = commitId;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
