package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.associating.AssociationGenerator;
import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.JavaParserTreeGenerator;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.ActionAggregationGenerator;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityPreprocess;
import edu.fdu.se.astdiff.preprocessingfile.FilePairPreDiff;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {

    public void doo(String filePrev, String fileCurr, String output) {
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFile(filePrev,fileCurr);
        int result = preDiff.compareTwoFile(output);
        if(result ==-1){
            return;
        }
        int index = filePrev.lastIndexOf('/');
        String fileName = filePrev.substring(index+1,filePrev.length()-5);
        runDiff(preDiff,fileName);
    }
    private void runDiff(FilePairPreDiff preDiff,String fileName){
        PreprocessedData preData = preDiff.getPreprocessedData();
        JavaParserTreeGenerator treeGenerator = new JavaParserTreeGenerator(preData.getSrcCu(),preData.getDstCu());
        treeGenerator.setFileName(fileName);
        //gumtree
        MyActionGenerator actionGenerator = new MyActionGenerator(treeGenerator);
        GeneratingActionsData actionsData = actionGenerator.generate();
        //print
        printActions(actionsData,treeGenerator,preDiff.getFileOutputLog());
        
        MiningActionData mad = new MiningActionData(actionsData,treeGenerator);
        ActionAggregationGenerator aag = new ActionAggregationGenerator();
        aag.doCluster(mad);
//修正
        ChangeEntityData ced = new ChangeEntityData(preData,mad);
        ChangeEntityPreprocess cep = new ChangeEntityPreprocess(ced);
        cep.preprocessChangeEntity();//1.init 2.merge 3.set 4.sub
//association
        AssociationGenerator associationGenerator = new AssociationGenerator(ced);
//        associationGenerator.generate();
    }

    public void doo(String fileName,byte[] filePrevContent, byte[] fileCurrContent, String output) {
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFile(filePrevContent,fileCurrContent);
        int result = preDiff.compareTwoFile(output);
        if(result ==-1){
            return;
        }
        runDiff(preDiff,fileName);
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
