package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
//        String repo = "/path/to/git/repo/.git";
//        String commitId = "commit id";
//        String outputDir = "/path/to/output/dir";
        String repo = "D:/Workspace/CLDiff-2018-7-12/spring-framework/.git";
        String commitId = "3c1adf7f6af0dff9bda74f40dabe8cf428a62003";
        String outputDir = "C:/Users/huangkaifeng/Desktop/output";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}
