package edu.fdu.se.RQ;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.Global.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.associating.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.cldiff.BaseCLDiff;
import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.JGitHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class RQ3 extends RQ{

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
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
    private String repoPath;
    private String projName;
    private String commitId;

    private List<FilePairData> filePairDatas = new ArrayList<>();


    public static void main(String args[]){
        RQ3 rq = new RQ3();
        String projName = "zxing";
        rq.repoPath = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\"+projName+"\\.git";
        rq.commitId = "379e18daf44c5cb9d3a5387a35a997fa1f08b6ab";
        rq.outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ3\\";
        rq.jGitHelper = new JGitHelper(rq.repoPath);
        rq.baseDiffMiner = new CLDiffTest();
        rq.baseDiffMiner.mFileOutputLog = new FileOutputLog(rq.outputDir,projName);
        rq.jGitHelper.analyzeOneCommit(rq,rq.commitId);
        rq.generateDiffMinerOutput();

    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String currCommitId){
        this.baseDiffMiner.mFileOutputLog.setCommitId(currCommitId);
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            JSONArray ja = new JSONArray();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    ja.put(file);
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    FilePairData fp = new FilePairData(prevFile,currFile,file,file,fileName);
                    filePairDatas.add(fp);
                    this.baseDiffMiner.mFileOutputLog.writeSourceFile(prevFile,currFile,fileName);
                }
            }
            if(changedFileEntry.containsKey("addedFiles")){
                List<String> addedFile = changedFileEntry.get("addedFiles");
                for (String file : addedFile) {
                    if(BaseCLDiff.isFilter(file)){
                        continue;
                    }
                    ja.put(file);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    FilePairData fp = new FilePairData(null,currFile,file,file,fileName);
                    filePairDatas.add(fp);
                    this.baseDiffMiner.mFileOutputLog.writeSourceFile(null,currFile,fileName);
                }
            }
            JSONObject jo = new JSONObject();
            jo.put("files",ja);
            this.baseDiffMiner.mFileOutputLog.writeMetaFile(jo.toString());
        }
    }

    public void generateDiffMinerOutput(){
        String absolutePath = this.outputDir+"\\" + this.commitId;
        for(FilePairData fp:filePairDatas){
            System.out.println(fp.fileName);
            Global.fileName = fp.fileName;
            if(fp.prev==null){
                this.baseDiffMiner.dooNewFile(fp.fileName,fp.curr,absolutePath);
            }else {
                this.baseDiffMiner.doo(fp.fileName, fp.prev, fp.curr, absolutePath);
            }
            this.fileChangeEntityData.put(this.baseDiffMiner.changeEntityData.fileName,this.baseDiffMiner.changeEntityData);
        }
//        if(true) return;
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
        new FileOutsideGenerator().checkDuplicateSimilarity(this.fileChangeEntityData);
        baseDiffMiner.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
//        System.out.println(totalFileAssociations.toAssoJSonString());
        System.out.println(totalFileAssociations.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1,Tree tree2){
        TreeDistance treeDistance = new TreeDistance(tree1,tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }





    public void handleCommits(Map<String, Map<String, List<String>>> mMap, String currCommitId){
        //pass
    }
}
