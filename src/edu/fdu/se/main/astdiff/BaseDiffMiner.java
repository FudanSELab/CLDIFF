package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.humanreadableoutput.ChangeEntityData;
import edu.fdu.se.astdiff.miningactions.ClusterActions;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperationData;
import edu.fdu.se.astdiff.preprocessingfile.FilePairPreDiff;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessedData;
import edu.fdu.se.astdiff.treegenerator.JavaParserTreeGenerator;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {

    protected void doo(String filePrev, String fileCurr, String output) {
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.compareTwoFile(filePrev, fileCurr, output);
        // 1.5 data
        PreprocessedData preData = preDiff.getPreprocessedData();
        // 2.gen
        JavaParserTreeGenerator treeGenerator = new JavaParserTreeGenerator(preData.getSrcCu(),preData.getDstCu());
        MyActionGenerator gen = new MyActionGenerator(treeGenerator);
        // 2.5 data
        GeneratingActionsData actionsData = gen.generate();
        printActions(actionsData,treeGenerator);
        // 3. Aggregation
        MiningActionData mMiningActionData = new MiningActionData(actionsData,treeGenerator);
        ClusterActions.doCluster(mMiningActionData);
        MiningOperationData mod = new MiningOperationData(preData,mMiningActionData);
        // 3.5 data
        mod.printStage1ChangeEntity();
//        // 4.Layer
        mod.initContainerEntityData(); //todo 做好分类  + move merge
        ChangeEntityData changeEntityData = new ChangeEntityData(mod,preData.entityContainer);
        changeEntityData.printStage2ChangeEntity(); //todo print
    }

    private void printActions(GeneratingActionsData actionsData,JavaParserTreeGenerator treeGenerator){
        if("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_SRC_DST_TREE))){
            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", treeGenerator.getPrettyOldTreeString());
            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", treeGenerator.getPrettyNewTreeString());
            SimpleActionPrinter.printMyActions(actionsData.getAllActions());
        }

    }
}
