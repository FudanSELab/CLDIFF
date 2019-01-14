package edu.fdu.se.git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/22.
 */
public interface IHandleCommit {


    void handleCommit(Map<String, List<DiffEntry>> changedFiles, String commitId, RevCommit commit);
}
