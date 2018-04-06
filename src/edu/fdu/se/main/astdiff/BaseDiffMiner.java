package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.humanreadableoutput.ChangeEntityData;
import edu.fdu.se.astdiff.miningactions.ClusterActions;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperationData;
import edu.fdu.se.astdiff.preprocessingfile.FilePairPreDiff;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.astdiff.treegenerator.JavaParserTreeGenerator;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {

    public void doo(String filePrev, String fileCurr, String output) {
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFile(filePrev,fileCurr);
        int result = preDiff.compareTwoFile(output);
        if(result ==-1){
            return;
        }
        runDiff(preDiff);
    }
    private void runDiff(FilePairPreDiff preDiff){
        // 1.5 data
        PreprocessedData preData = preDiff.getPreprocessedData();
//        preData.printAddedRemovedBodies();
        // 2.gen
        JavaParserTreeGenerator treeGenerator = new JavaParserTreeGenerator(preData.getSrcCu(),preData.getDstCu());
        MyActionGenerator gen = new MyActionGenerator(treeGenerator);
        // 2.5 data
        GeneratingActionsData actionsData = gen.generate();
        printActions(actionsData,treeGenerator,preDiff.getFileOutputLog());
        // 3. Aggregation
        MiningActionData mMiningActionData = new MiningActionData(actionsData,treeGenerator);
        ClusterActions.doCluster(mMiningActionData);
        MiningOperationData mod = new MiningOperationData(preData,mMiningActionData);
        // 3.5 data
//        mod.printStage1ChangeEntity();
        mod.preprocessChangeEntity(); //1.init 2.merge 3.set 4.sub
        ChangeEntityData changeEntityData = new ChangeEntityData(mod);
        changeEntityData.printStage2ChangeEntity();
    }

    public void doo(byte[] filePrevContent, byte[] fileCurrContent, String output) {
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFile(filePrevContent,fileCurrContent);
        int result = preDiff.compareTwoFile(output);
        if(result ==-1){
            return;
        }
        runDiff(preDiff);
    }




    private void printActions(GeneratingActionsData actionsData, JavaParserTreeGenerator treeGenerator, FileOutputLog fileOutputLog){
        if("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_SRC_DST_TREE))){
//            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", treeGenerator.getPrettyOldTreeString());
            FileWriter.writeInAll(fileOutputLog.srcDirFile.getAbsolutePath() + "/tree.txt", treeGenerator.getPrettyOldTreeString());
//            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", treeGenerator.getPrettyNewTreeString());
            FileWriter.writeInAll(fileOutputLog.dstDirFile.getAbsolutePath() + "/tree.txt", treeGenerator.getPrettyNewTreeString());
            SimpleActionPrinter.printMyActions(actionsData.getAllActions());
        }
    }
    // 验证 Preprocessing *
    // 验证 GumTree 输出  **
    // 验证Aggregatio   ***
    // 验证分类   ***
    // 验证按照行号排序 ***
}
