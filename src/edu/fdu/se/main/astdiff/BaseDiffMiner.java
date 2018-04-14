package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.Global.Global;
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
import edu.fdu.se.astdiff.generatingactions.JavaParserTreeGenerator;
import edu.fdu.se.astdiff.webapi.GenerateChangeEntityJson;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {

    public void doo(String filePrev, String fileCurr, String output) {
        int index = filePrev.lastIndexOf('/');
        if(index ==-1) index = filePrev.lastIndexOf("\\");
        String fileName = filePrev.substring(index+1,filePrev.length());
        Global.fileName = fileName;
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFile(filePrev,fileCurr);
        int result = preDiff.compareTwoFile(output);
        if(result ==-1){
            return;
        }

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

        MiningActionData mad = new MiningActionData(preData,actionsData,treeGenerator);
        ActionAggregationGenerator aag = new ActionAggregationGenerator();
        aag.doCluster(mad);
//修正
        ChangeEntityData ced = new ChangeEntityData(mad);
        ChangeEntityPreprocess cep = new ChangeEntityPreprocess(ced);
        cep.preprocessChangeEntity();//1.init 2.merge 3.set 4.sub
//association
//        AssociationGenerator associationGenerator = new AssociationGenerator(ced);
//        associationGenerator.generate();
        GenerateChangeEntityJson.setStageIIIBean(ced);
        String json = GenerateChangeEntityJson.generateEntityJson(ced.mad);
        preDiff.getFileOutputLog().writeEntityJson(json);
//        System.out.println(json);
//        String assoa = GenerateChangeEntityJson.generateAssociationJson(ced.mAssociations);
//        System.out.println(assoa);

    }

    public void doo(String fileName,byte[] filePrevContent, byte[] fileCurrContent, String output) {
        // 1.pre
        int index = fileName.lastIndexOf('/');
        if(index ==-1) index = fileName.lastIndexOf("\\");
        fileName = fileName.substring(index+1,fileName.length());
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
            fileOutputLog.writeTreeFile(treeGenerator.getPrettyOldTreeString(),treeGenerator.getPrettyNewTreeString());
            SimpleActionPrinter.printMyActions(actionsData.getAllActions());
        }
    }



}
