package edu.fdu.se.git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/22.
 */
public class IHandleCommit {

<<<<<<< HEAD
    public void handleCommit(Map<String, List<DiffEntry>> changedFiles, String commitId, RevCommit commit) {

    }

    public void handleCommit(Map<String, List<DiffEntry>> changedFiles, String currCommitId, RevCommit currCommit, String nextCommitId, RevCommit nextCommit) {
=======
    void handleCommit(Map<String, List<DiffEntry>> changedFiles, String commitId, RevCommit commit){

    }

    void handleCommit(Map<String, List<DiffEntry>> changedFiles, String currCommitId,RevCommit currCommit, String nextCommitId,RevCommit nextCommit){
>>>>>>> 4a95d713a79aac47a7539d3b9808a21dfc33b18c

    }

}
