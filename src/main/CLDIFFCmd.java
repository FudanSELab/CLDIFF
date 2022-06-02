package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
//        String repo = "/Users/jiayuesun/2022/SP-2022/JavaCLIDiff/.git";
//        String commitId = "785b5c927f4396e6b2562e617492904a7664c853";
        // https://github.com/JunYuWang0808/CLIDIFFTEST
        String repo = "/Users/junyu/Desktop/2022spring/cs230/Project/CLIDIFFTEST/.git";
        String commitId = "9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713";
        String outputDir = "./output";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}


