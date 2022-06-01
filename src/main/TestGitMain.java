package main;

import edu.ucla.se.GitCreator;
import edu.ucla.se.GitHandler;
import edu.ucla.se.P_LANG;

import java.nio.file.Paths;
import java.util.List;

public class TestGitMain {
    public static void main(String[] args) {
        GitCreator gitCreator = new GitCreator();
        gitCreator.createNewRepo("xv6");
        String commitId = gitCreator.commitFilesToRepo("xv6", "D:\\Study\\CS 537\\p2b\\xv6-sp20");
        if (commitId == null) {
            System.out.println("Commit Failed!");
            System.exit(1);
        }
        GitHandler gitHandler = new GitHandler(
                Paths.get("D:\\Study\\CS 230\\final project\\CLDiff\\repos\\xv6\\.git").toString(),
                commitId, P_LANG.C);
        List<String> allFiles = gitHandler.getFiles();
        for (String file : allFiles) System.out.println(file);
    }
}
