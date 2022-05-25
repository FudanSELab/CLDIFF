package main;

import edu.ucla.se.GitHandler;
import edu.ucla.se.P_LANG;

import java.nio.file.Paths;
import java.util.List;

public class SearchMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage [Repo Path] [Commit Id].\n");
            System.exit(1);
        }

        String repoPath = Paths.get(args[0]).toString();
        String commitId = args[1];

        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);

        GitHandler handler = new GitHandler(repoPath, commitId, P_LANG.JAVASCRIPT);

        List<String> oldVersionFiles =  handler.getOldFiles();

        for (String file : oldVersionFiles) System.out.println(file);
    }
}
