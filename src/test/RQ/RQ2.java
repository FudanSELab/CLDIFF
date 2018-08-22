package RQ;

import edu.fdu.se.base.common.FilePairData;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.IHandleCommit;
import edu.fdu.se.git.JGitHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 */
public class RQ2 implements IHandleCommit{

    private String projPath;
    private String argType;
    private CLDiffTest cldiff = new CLDiffTest();
    public JGitHelper jGitHelper;


    public static void main(String args[]){
        if(!(args[0]!=null && args[1]!=null)){
            return;
        }
        Global.RQ2 = 1;
        RQ2 rq = new RQ2();
        rq.argType = args[0];
        rq.projPath = args[1];
        rq.totalNumber = 0;
        long startTime = System.nanoTime();
        String[] data = rq.projPath.split("\\\\");
        String projName = data[data.length-2];
        if("gt".equals(args[0]) || "df".equals(args[0])){
            String outputDir = "C:\\Users\\huangkaifeng\\Desktop\\output\\";
            rq.cldiff = new CLDiffTest();
            rq.jGitHelper = new JGitHelper(rq.projPath);
            rq.cldiff.mFileOutputLog = new FileOutputLog(outputDir,projName);
            rq.jGitHelper.walkRepoFromBackwards(rq);
        }
        long endTime = System.nanoTime();
        System.out.println("----total number:" + (endTime-startTime));
        System.out.println("----total number:"+ rq.totalNumber);

    }


    private long totalNumber;

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
        System.out.println("----commit id:"+currCommitId);
        long time = 0l;
//        this.CLDiffCore.mFileOutputLog.setCommitId(currCommitId);
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (CLDiffCore.isFilter(file)) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    int num=0;
                    if(this.argType.equals("gt")){
                        long start = System.nanoTime();
                        num = cldiff.runGumTree(new String(prevFile), new String(currFile));
                        totalNumber += num;
                        long end = System.nanoTime();
                        System.out.println("----"+fileName+" "+num);
                        time += (end-start);
                    }else if(this.argType.equals("df")){
                        Global.fileName = fileName;
                        FilePairData fp = new FilePairData(prevFile,currFile,file,file,fileName);
                        filePairDatas.add(fp);
                    }

                }
            }
        }
        if(this.argType.equals("gt")){
            System.out.println("----one commit time:" + time);
        }else if(this.argType.equals("df")){
            long start = System.nanoTime();
            generateDiffMinerOutput();
            long end = System.nanoTime();
            System.out.println("----one commit time:" + (end-start));
        }
    }

    private List<FilePairData> filePairDatas = new ArrayList<>();
    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();


    public void generateDiffMinerOutput(){
        for(FilePairData fp:filePairDatas){
            Global.fileName = fp.getFileName();
            if(fp.getPrev()==null){
                this.cldiff.dooNewFile(fp.getFileName(),fp.getCurr(),null);
            }else {
                this.cldiff.doo(fp.getFileName(), fp.getPrev(), fp.getCurr(), null);
            }
            this.fileChangeEntityData.put(this.cldiff.changeEntityData.fileName,this.cldiff.changeEntityData);
            int num  = changeEntitySize(this.cldiff.changeEntityData.mad.getChangeEntityList());
            totalNumber+=num;
            System.out.println("----"+fp.getFileName()+" "+num);
        }
        long start = System.nanoTime();
        List<String> fileNames = new ArrayList<>(this.fileChangeEntityData.keySet());
        TotalFileAssociations totalFileAssociations = new TotalFileAssociations() ;
        for(int i =0;i<fileNames.size();i++){
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            FileInsideAssociationGenerator associationGenerator = new FileInsideAssociationGenerator(cedA);
            associationGenerator.generateFile();
            totalFileAssociations.addEntry(fileNameA,cedA.mAssociations);
        }

        for(int i =0;i<fileNames.size();i++){
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            FileOutsideGenerator fileOutsideGenerator = new FileOutsideGenerator();
            for(int j =i+1;j<fileNames.size();j++){
                String fileNameB = fileNames.get(j);
                ChangeEntityData cedB = this.fileChangeEntityData.get(fileNameB);
                fileOutsideGenerator.generateOutsideAssociation(cedA,cedB);
                totalFileAssociations.addFile2FileAssos(fileNameA,fileNameB,fileOutsideGenerator.mAssos);
            }
        }
        long end = System.nanoTime();
        System.out.println("----linking " +(end-start));

        for(int i =0;i<fileNames.size();i++){
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            for(ChangeEntity ce:cedA.mad.getChangeEntityList()){
                if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)){
                    if(ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)
                            ||ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)) {
                        System.out.println("----"+ce.stageIIBean.getOpt() + " "+ ce.clusteredActionBean.actions.size());
                    }
                }
            }
        }
        fileChangeEntityData.clear();
        filePairDatas.clear();
    }

    private int changeEntitySize(List<ChangeEntity> mList){
        int cnt = 0;
        for(ChangeEntity ce:mList){
            if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)
                    ||ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)){
                cnt ++;
            }else if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD)){
                if(ce.stageIIBean.getOpt2List() ==null ){
                    cnt++;
                }else{
                    if(ce.stageIIBean.getOpt2List()==null||ce.stageIIBean.getOpt2List().size()==0){
                        cnt++;
                    }else {
                        cnt += ce.stageIIBean.getOpt2List().size();
                    }
                }
            }
        }
        return cnt;
    }

}
