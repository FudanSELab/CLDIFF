package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
//        String repo = "E:\\school\\repo\\FudanSELab\\IssueTracker-Master-master\\.git";
//        String currCommitId = "3fa6e8e11e85e353093d481bae82e769b70c99f3";  //mayuyukirinn committed on 3 Dec 2018
//        String nextCommitId = "e172091f374057aa5626cd5f02dbfff54204ab3b";  //linshengwang committed on 5 Dec 2018
//        CLDiffLocal CLDiffLocal = new CLDiffLocal();
//        String outputDir = "E:\\school\\DiffResult";
//        CLDiffLocal.run(currCommitId,nextCommitId,repo,outputDir);
        String repo = "E:\\school\\repo\\FudanSELab\\IssueTracker-Master-master\\.git";
        String commitId = "3fa6e8e11e85e353093d481bae82e769b70c99f3";
        String outputDir = "E:\\school\\DiffResult";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}
