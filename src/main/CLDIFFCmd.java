package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
        String repo = "E:\\school\\repo\\FudanSELab\\IssueTracker-Master-developer\\.git";
        String commitId = "0097a5548381b52f6bec68ec739dc1294f718d3f";  //有错误的commit
        String currCommitId = "9d8f4719947c300c3b6666a6313b22f073e12b33";  //mayuyukirinn committed on 3 Dec 2018
        String nextCommitId = "3ebdf8e5d9879b8d5ad5f3a1693a9165932b4a17";  //linshengwang committed on 5 Dec 2018
        String outputDir = "E:\\school\\DiffResult";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
//        CLDiffLocal.run(commitId,repo,outputDir);
        CLDiffLocal.run(currCommitId,nextCommitId,repo,outputDir);
    }
}
