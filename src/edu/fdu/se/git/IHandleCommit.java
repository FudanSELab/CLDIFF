package edu.fdu.se.git;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/22.
 */
public interface IHandleCommit {

    void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String commitId);

//    void handleCommits();
}
