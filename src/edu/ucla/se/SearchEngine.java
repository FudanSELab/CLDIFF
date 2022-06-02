package edu.ucla.se;

import edu.fdu.se.cldiff.CLDiffLocal;
import edu.ucla.se.utils.Config;
import edu.ucla.se.utils.ParserHelper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class SearchEngine {
    private String repoPath;
    private String repoName;
    private String commitId;

    private GitHandler gitHandler;

    public SearchEngine(String repoPath, String commitId, P_LANG lang) {
        this.repoPath = repoPath.substring(0, repoPath.length() - 5);
        this.gitHandler = new GitHandler(repoPath, commitId, lang);
    }

    public SearchEngine(String oldPath, String newPath, String repoName, P_LANG lang) {
        oldPath = Paths.get(oldPath).toString();
        newPath = Paths.get(newPath).toString();
        GitCreator gitCreator = new GitCreator();
        gitCreator.deleteRepo(repoName);
        gitCreator.createNewRepo(repoName);
        this.repoPath = gitCreator.getRepoPath(repoName);
        gitCreator.commitFilesToRepo(repoName, oldPath);
        String commitId = gitCreator.commitFilesToRepo(repoName, newPath);
        this.repoName = repoName;
        this.commitId = commitId;
        this.gitHandler = new GitHandler(gitCreator.getRepo(repoName), repoName, commitId, lang);
    }

    public void run() {
        try {
            //====================== CLEAR PREVIOUSLY GENERATED FILES ============================
            File outputDir = new File(Paths.get(Config.CLDIFF_OUTPUT_PATH, repoName).toString());
            FileUtils.deleteDirectory(outputDir);

            //=============================== RUN CLDIFF =========================================
            CLDiffLocal clDiffLocal = new CLDiffLocal();
            clDiffLocal.run(commitId, Paths.get(repoPath, ".git").toString(), Config.CLDIFF_OUTPUT_PATH);

            //========================== STEP 1: GET GROUPING INFO ===============================
            HashMap<Integer, HashMap<String, List<List<Integer>>>> groups = ParserHelper.getChangeGroups(repoName, commitId);

            //============== STEP 2.1: GENERATE REGEX AND MATCH IN CURRENT COMMIT ================
            List<String> regex = new RegexGenerator(groups, this.gitHandler).generateRegex();
            Map<String, List<MissingChangeInfo>> regexResults = gitHandler.matchRegex(regex);

            //=================== STEP 2.2: LCS MATCH IN CURRENT COMMIT ==========================
            Map<String, List<MissingChangeInfo>> LCSResults = LCSMatcher.matchLCS(gitHandler, groups);

            //======================== STEP 3: COMBINE MATCHING OUTPUT ===========================
            //TODO

            for (Map.Entry<String, List<MissingChangeInfo>> entry : regexResults.entrySet()) {
                System.out.printf("Possible Missing Changes in %s:\n", entry.getKey());
                for (MissingChangeInfo info : entry.getValue())
                    System.out.printf("(Line%d, Line%d), %s\n", info.startLine, info.endLine, info.content);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
