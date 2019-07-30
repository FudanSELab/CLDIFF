package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
        String repo = "/home/xie/Workspace/Eclipse/Maven/che-plugin-git-ext-git/.git";
        String commitId = "fb69c3f6bbe785ed256e3b4f105bf85a5a817b71";
        String outputDir = "~/Desktop";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}
