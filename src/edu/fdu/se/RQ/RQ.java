package edu.fdu.se.RQ;

import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.JGitHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/13.
 *
 */
abstract public class RQ {

    public JGitHelper jGitHelper;

    public CLDiffTest baseDiffMiner;
    public String outputDir;

    abstract public void handleCommits(Map<String, Map<String, List<String>>> mMap,String currCommitId);


    abstract public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId);


}
