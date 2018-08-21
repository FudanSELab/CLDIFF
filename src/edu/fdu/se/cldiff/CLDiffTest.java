package edu.fdu.se.cldiff;

import java.io.File;

import edu.fdu.se.base.generatingactions.GeneratingActionsData;
import edu.fdu.se.base.generatingactions.GumTreeDiffParser;
import edu.fdu.se.base.generatingactions.MyActionGenerator;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;

/**
 * Created by huangkaifeng on 2018/2/27.
 */
public class CLDiffTest extends BaseCLDiff {


    public int runGumTree(String prevContent, String currContent) {
//        String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
//        String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
        GumTreeDiffParser gt = new GumTreeDiffParser(prevContent, currContent);
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", his.getPrettyOldTreeString());
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", his.getPrettyNewTreeString());
        MyActionGenerator gen = new MyActionGenerator(gt.src, gt.dst, gt.mapping);
        GeneratingActionsData data = gen.generate();
//        System.out.println("size: "+data.getAllActions().size());
//        SimpleActionPrinter.printMyActions(data.getAllActions());
        return data.getAllActions().size();
    }

    /**
     * 使用修改简化之后的流程，测试单个文件的功能
     */
    private void runSingleFilePair() {
        String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
        String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
        String outputDir = rootOutPath;
        this.mFileOutputLog = new FileOutputLog(rootOutPath, "tes");
        this.mFileOutputLog.setCommitId("null");
        doo(file1, file2, outputDir);
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
                doo(prevFile, currf1.getAbsolutePath(), outputDir);
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
//        i.runSingleFilePair();
    }

}

