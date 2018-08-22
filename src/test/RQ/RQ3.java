package RQ;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.common.FilePairData;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.associating.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.IHandleCommit;
import edu.fdu.se.git.JGitHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class RQ3 implements IHandleCommit {

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public JGitHelper jGitHelper;

    private String repoPath;
    private String projName;
    private String commitId;
    private CLDiffCore cldiff = new CLDiffCore();
    private List<FilePairData> filePairDatas = new ArrayList<>();


    public static void main(String args[]){
        RQ3 rq = new RQ3();
        String projName = "zxing";
        rq.repoPath = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\"+projName+"\\.git";
        rq.commitId = "379e18daf44c5cb9d3a5387a35a997fa1f08b6ab";
        String outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ3\\";
        rq.jGitHelper = new JGitHelper(rq.repoPath);
        rq.cldiff = new CLDiffTest();
        rq.cldiff.mFileOutputLog = new FileOutputLog(outputDir,projName);
        rq.jGitHelper.analyzeOneCommit(rq,rq.commitId);
        rq.generateDiffMinerOutput();

    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String currCommitId){
        this.cldiff.mFileOutputLog.setCommitId(currCommitId);
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
                    this.cldiff.mFileOutputLog.writeSourceFile(prevFile,currFile,fileName);
                }
            }
            if(changedFileEntry.containsKey("addedFiles")){
                List<String> addedFile = changedFileEntry.get("addedFiles");
                for (String file : addedFile) {
                    if(CLDiffCore.isFilter(file)){
                        continue;
                    }
                    ja.put(file);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    FilePairData fp = new FilePairData(null,currFile,file,file,fileName);
                    filePairDatas.add(fp);
                    this.cldiff.mFileOutputLog.writeSourceFile(null,currFile,fileName);
                }
            }
            JSONObject jo = new JSONObject();
            jo.put("files",ja);
            this.cldiff.mFileOutputLog.writeMetaFile(jo.toString());
        }
    }

    public void generateDiffMinerOutput(){
        String absolutePath = cldiff.mFileOutputLog.rootPath+"\\" + this.commitId;
        for(FilePairData fp:filePairDatas){
            System.out.println(fp.getFileName());
            Global.fileName = fp.getFileName();
            if(fp.getPrev()==null){
                this.cldiff.dooNewFile(fp.getFileName(),fp.getCurr(),absolutePath);
            }else {
                this.cldiff.doo(fp.getFileName(), fp.getPrev(), fp.getCurr(), absolutePath);
            }
            this.fileChangeEntityData.put(this.cldiff.changeEntityData.fileName,this.cldiff.changeEntityData);
        }
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
        cldiff.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
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
