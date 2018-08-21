package edu.fdu.se.RQ;

import edu.fdu.se.base.Global.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.cldiff.BaseCLDiff;
import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.JGitHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 */
public class RQ2 extends RQ {

    private String rq2ProjPath;
    private String argType;

    public static void main(String args[]){
        Global.RQ2 = 1;
        if(!(args[0]!=null && args[1]!=null)){
            return;
        }
        RQ2 rq = new RQ2();
        String projPath = args[1];
        System.out.println("----"+projPath);
        System.out.println("----"+args[0]);
        long startTime = System.nanoTime();
        rq.argType = args[0];
        rq.totalNumber = 0;
        String[] data = projPath.split("\\\\");
        String projName = data[data.length-2];
        if("gt".equals(args[0]) || "df".equals(args[0])){
            rq.outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ2";
            rq.rq2ProjPath = projPath;
            rq.baseDiffMiner = new CLDiffTest();
            rq.jGitHelper = new JGitHelper(rq.rq2ProjPath);
            rq.baseDiffMiner.mFileOutputLog = new FileOutputLog(rq.outputDir,projName);
            rq.jGitHelper.walkRepoFromBackwards(rq);

        }else if("lc".equals(args[0])){
            rq.jGitHelper = new JGitHelper(projPath);
            rq.jGitHelper.walkRepoFromBackwardsCountLineNumber(rq);
        }
        long endTime = System.nanoTime();
        System.out.println("----total number:" + (endTime-startTime));
        System.out.println("----total number:"+ rq.totalNumber);

    }


    private long totalNumber;

    public void handleCommits(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
        System.out.println("----commit id:"+currCommitId);
        long time = 0l;
//        this.baseCLDiff.mFileOutputLog.setCommitId(currCommitId);
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (BaseCLDiff.isFilter(file)) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    int num=0;
                    if(this.argType.equals("gt")){
                        long start = System.nanoTime();
                        num = baseDiffMiner.runGumTree(new String(prevFile), new String(currFile));
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
    class FilePairData{
        public FilePairData(byte[] prevv,byte[] currr,String prevPathh,String currPathh,String fileNamee) {
            prev = prevv;
            curr = currr;
            prevPath = prevPathh;
            currPath = currPathh;
            fileName = fileNamee;
        }
        private byte[] prev;
        private byte[] curr;
        private String prevPath;
        private String currPath;
        private String fileName;
    }

    private List<FilePairData> filePairDatas = new ArrayList<>();
    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();


    public void generateDiffMinerOutput(){
        for(FilePairData fp:filePairDatas){
//            System.out.println(fp.fileName);
            Global.fileName = fp.fileName;
            if(fp.prev==null){
                this.baseDiffMiner.dooNewFile(fp.fileName,fp.curr,null);
            }else {
                this.baseDiffMiner.doo(fp.fileName, fp.prev, fp.curr, null);
            }
            this.fileChangeEntityData.put(this.baseDiffMiner.changeEntityData.fileName,this.baseDiffMiner.changeEntityData);
            int num  = changeEntitySize(this.baseDiffMiner.changeEntityData.mad.getChangeEntityList());
            totalNumber+=num;
            System.out.println("----"+fp.fileName+" "+num);
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

    public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId){

    }
}
