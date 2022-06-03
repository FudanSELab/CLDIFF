package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
//        String repo = "../CLIDIFFTEST/.git";
//        String commitId = "9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713";
        // https://github.com/JunYuWang0808/CLIDIFFTEST
        String repo = "/Users/junyu/Desktop/2022spring/cs230/Project/TestSet/TestPatch1/.git";
        String commitId = "394f7a04f68e65c23aa450b615eb6ae89c9b7422";
        String outputDir = "./output";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}


