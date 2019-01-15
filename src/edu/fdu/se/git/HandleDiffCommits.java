package edu.fdu.se.git;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

/**
 * Created by linshengwang on 2019/1/7.
 */
public interface HandleDiffCommits {

    void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String currCommitId,RevCommit currCommit, String nextCommitId,RevCommit nextCommit);
}
