package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.main.astdiff.BaseDiffMiner;
import edu.fdu.se.main.astdiff.DiffMiner;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/13.
 *
 */
abstract public class RQ {

    JGitHelper jGitHelper;
    FileOutputLog fileOutputLog;
    BaseDiffMiner baseDiffMiner = new DiffMiner();
    String outputDir;

    abstract public void handleCommits(Map<String, Map<String, List<String>>> mMap,String currCommitId);


    abstract public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId);

}
