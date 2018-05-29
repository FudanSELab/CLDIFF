package edu.fdu.se.main.astdiff;

import java.io.File;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.GumTreeDiffParser;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import com.github.gumtreediff.actions.model.Action;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class DiffMinerTest extends BaseDiffMiner {


    /**
     * 使用gt的流程
     * test 单个文件
     */
    public int runGumTree(String prevContent,String currContent) {
//        System.out.println("Step1 Generating Diff Actions:----------------------");
//        String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
//        String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
//        GumTreeDiffParser his = new GumTreeDiffParser(new CommitFile(file1), new CommitFile(file2));
        GumTreeDiffParser his = new GumTreeDiffParser(prevContent, currContent);
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", his.getPrettyOldTreeString());
//        FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", his.getPrettyNewTreeString());
        // package 1
        
        MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
        GeneratingActionsData data = gen.generate();
//        wholeSize += data.getAllActions().size();
//        System.out.println("size: "+data.getAllActions().size());
//        SimpleActionPrinter.printMyActions(data.getAllActions());
        return data.getAllActions().size();
        // package 2
//        System.out.println("Step2 Begin to cluster actions:-------------------");
//        MiningActionData mMiningActionData = new MiningActionData(data,his);
//        ActionAggregationGenerator aag = new ActionAggregationGenerator();
//        aag.doCluster(mMiningActionData);
//        // package 3
//        List<ChangeEntity> mlist = mMiningActionData.getChangeEntityList();
//        mlist.forEach(a -> {
//            System.out.println(a.toString());
//        });

    }

    /**
     * 使用修改简化之后的流程，测试单个文件的功能
     */
    private void runSingleFilePair() {
        String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
        String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
        String outputDir = rootOutPath;
        this.mFileOutputLog = new FileOutputLog(rootOutPath,"tes");
        this.mFileOutputLog.setCommitId("abcd");
        doo(file1, file2, outputDir);
    }

    /**
     * 使用修改简化之后的流程，测试多个文件的功能
     */
    private void runBatchTest() {
        String batchTestFilePath = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\test_batch_simple_action";
        File currdir = new File(batchTestFilePath + "\\curr");
        File[] files = currdir.listFiles();
        String outputDir = "test";
        try{
            for (int i =0;i<files.length;i++){
                File currf1 = files[i];
                String prevFile = batchTestFilePath + "\\prev\\" + currf1.getName();
                if(currf1.getName().startsWith("IfElseAddition.java")) {
                    //IfElseAddition
                    System.out.println(i+" "+currf1.getName());
                    doo(prevFile, currf1.getAbsolutePath(), outputDir);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DiffMinerTest i = new DiffMinerTest();
//        i.runGumTree(null,null);
//        i.runBatchTest();

        i.runSingleFilePair();
    }

}

