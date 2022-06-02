package edu.ucla.se;

import edu.fdu.se.cldiff.CLDiffLocal;
import edu.ucla.se.utils.Config;
import edu.ucla.se.utils.ParserHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.gitHandler = new GitHandler(gitCreator.getRepo(repoName), commitId, lang);
    }

    public void run() {
        try {
            //=============================== RUN CLDIFF =========================================
            CLDiffLocal clDiffLocal = new CLDiffLocal();
            clDiffLocal.run(commitId, Paths.get(repoPath, ".git").toString(), Config.CLDIFF_OUTPUT_PATH);

            //========================== STEP 1: GET GROUPING INFO ===============================
            HashMap<Integer, HashMap<String, List<List<Integer>>>> groups = ParserHelper.getChangeGroups(repoName, commitId);

            //============== STEP 2.1: GENERATE REGEX AND MATCH IN CURRENT COMMIT ================
            List<String> regex = generateRegex(groups);
            Map<String, List<MissingChangeInfo>> regexResults = gitHandler.matchRegex(regex);

            //============= STEP 2.2: GENERATE AND MATCH TOKENS IN CURRENT COMMIT ================


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

    private List<String> generateRegex(HashMap<Integer, HashMap<String, List<List<Integer>>>> grouping) {
        RegexGenerator regexGenerator = new RegexGenerator(grouping, this.gitHandler);
        return regexGenerator.generateRegex();
    }



}
