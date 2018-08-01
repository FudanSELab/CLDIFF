package edu.fdu.se.cldiff;

import edu.fdu.se.base.Global.Global;
import edu.fdu.se.base.generatingactions.GeneratingActionsData;
import edu.fdu.se.base.generatingactions.JavaParserTreeGenerator;
import edu.fdu.se.base.generatingactions.MyActionGenerator;
import edu.fdu.se.base.generatingactions.SimpleActionPrinter;
import edu.fdu.se.base.miningactions.ActionAggregationGenerator;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.ChangeEntityPreprocess;
import edu.fdu.se.base.preprocessingfile.FilePairPreDiff;
import edu.fdu.se.base.preprocessingfile.NewFileProcessing;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.base.webapi.GenerateChangeEntityJson;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {


    public ChangeEntityData changeEntityData;
    public FileOutputLog mFileOutputLog;

    public void doo(String filePrev, String fileCurr, String output) {
        int index = filePrev.lastIndexOf('/');
        if(index ==-1) index = filePrev.lastIndexOf("\\");
        String fileName = filePrev.substring(index+1,filePrev.length());
        Global.fileName = fileName;
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFilePath(filePrev,fileCurr);
        int result = preDiff.compareTwoFile();
        if(result ==-1){
            return;
        }
        runDiff(preDiff,fileName);
    }


    public void doo(String fileName,byte[] filePrevContent, byte[] fileCurrContent, String output) {
        long start = System.nanoTime();
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFileContent(filePrevContent,fileCurrContent);
        int result = preDiff.compareTwoFile();
        long end = System.nanoTime();
        System.out.println("----pre-processing " +(end-start));
        if(result ==-1){
            return;
        }
        runDiff(preDiff,fileName);
    }

    public void dooNewFile(String fileName,byte[] fileCurrContent,String output){
        NewFileProcessing newFileProcessing = new NewFileProcessing(fileCurrContent);
        changeEntityData = newFileProcessing.ced;
        changeEntityData.fileName = fileName;

    }


    private void runDiff(FilePairPreDiff preDiff,String fileName){
        long start = System.nanoTime();
        PreprocessedData preData = preDiff.getPreprocessedData();
        JavaParserTreeGenerator treeGenerator = new JavaParserTreeGenerator(preData.getSrcCu(),preData.getDstCu());
        treeGenerator.setFileName(fileName);
        //gumtree
        MyActionGenerator actionGenerator = new MyActionGenerator(treeGenerator);
        GeneratingActionsData actionsData = actionGenerator.generate();
        //print
        long end = System.nanoTime();
        System.out.println("----mapping " +(end-start));

        printActions(actionsData,treeGenerator);
        long start2 = System.nanoTime();
        MiningActionData mad = new MiningActionData(preData,actionsData,treeGenerator);
        ActionAggregationGenerator aag = new ActionAggregationGenerator();
        aag.doCluster(mad);
//修正
        ChangeEntityData ced = new ChangeEntityData(mad);
        ChangeEntityPreprocess cep = new ChangeEntityPreprocess(ced);
        cep.preprocessChangeEntity();//1.init 2.merge 3.set 4.sub
        changeEntityData = ced;
        changeEntityData.fileName = fileName;
        long end2 = System.nanoTime();
        System.out.println("----grouping " +(end2-start2));
// json
        GenerateChangeEntityJson.setStageIIIBean(ced);
        String json = GenerateChangeEntityJson.generateEntityJson(ced.mad);
        this.mFileOutputLog.writeEntityJson(json);
        System.out.println(json);


    }


    private void printActions(GeneratingActionsData actionsData, JavaParserTreeGenerator treeGenerator){
        if("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_SRC_DST_TREE))){
            mFileOutputLog.writeTreeFile(treeGenerator.getPrettyOldTreeString(),treeGenerator.getPrettyNewTreeString());
            SimpleActionPrinter.printMyActions(actionsData.getAllActions());
        }
    }


}
