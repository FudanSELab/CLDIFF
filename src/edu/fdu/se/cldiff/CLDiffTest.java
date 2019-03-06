package edu.fdu.se.cldiff;

import java.io.File;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.generatingactions.GeneratingActionsData;
import edu.fdu.se.base.generatingactions.GumTreeDiffParser;
import edu.fdu.se.base.generatingactions.MyActionGenerator;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.PathUtil;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class CLDiffTest extends CLDiffCore {

    public int runGumTree(String prevContent, String currContent) {
//        String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
//        String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
        GumTreeDiffParser gt = new GumTreeDiffParser(prevContent, currContent);
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", gt.getPrettyOldTreeString());
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", gt.getPrettyNewTreeString());
        MyActionGenerator gen = new MyActionGenerator(gt.src, gt.dst, gt.mapping);
        GeneratingActionsData data = gen.generate();
//        System.out.println("size: "+data.getAllActions().size());
//        SimpleActionPrinter.printMyActions(data.getAllActions());
        return data.getAllActions().size();
    }

    /**
     * 使用修改简化之后的流程，测试单个文件的功能
     */
    private void runSingleFilePair(String file1,String file2,String outputDir,String projectOwnerName) {
        this.mFileOutputLog = new FileOutputLog(outputDir, "testproject",projectOwnerName);
        this.mFileOutputLog.setCommitId("commitid");
        Global.parentCommit = "null";
        dooDiffFile(file1, file2, outputDir+projectOwnerName);
    }

    /**
     * 使用修改简化之后的流程，测试多个文件的功能
     */
    private void runBatchTest() {
        String batchTestFilePath = "D:\\Workspace\\test_batch_simple_action";
        File currdir = new File(batchTestFilePath + "\\curr");
        File[] files = currdir.listFiles();
        String outputDir = "test";
        try {
            for (int i = 0; i < files.length; i++) {
                File currf1 = files[i];
                String prevFile = batchTestFilePath + "\\prev\\" + currf1.getName();
                System.out.println(i + " " + currf1.getName());
                dooDiffFile(prevFile, currf1.getAbsolutePath(), outputDir);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        CLDiffTest i = new CLDiffTest();
//        i.runGumTree(null,null);
//        i.runBatchTest();
        String filePrev = PathUtil.unifyPathSeparator(args[0]);
        String fileCurr = PathUtil.unifyPathSeparator(args[1]);
        String outputDir = PathUtil.unifyPathSeparator(args[2]);
        String projectOwnerName = PathUtil.unifyPathSeparator(args[3]);
        i.runSingleFilePair(filePrev,fileCurr,outputDir,projectOwnerName);
    }

}
