package edu.fdu.se.cldiff;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.Global.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.associating.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.server.Meta;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.FilePairData;

import java.util.*;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class CLDiffOnlineAPI {

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public BaseCLDiff baseCLDiff;
    public String outputDir;
    private String commitId;
    private List<FilePairData> filePairDatas;

    /**
     * output path +"proj_name" + "commit_id"
     * @param path
     */
    public CLDiffOnlineAPI(String path, Meta meta){
        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        baseCLDiff = new BaseCLDiff();
        commitId = meta.getCommit_hash();
        String projName = meta.getProject_name();
        baseCLDiff.mFileOutputLog = new FileOutputLog(path, projName);
        List<String> parents = meta.getParents();
        baseCLDiff.mFileOutputLog.setCommitId(commitId,parents);
        this.outputDir = path;
        initDataFromJson(meta);
    }



    public void initDataFromJson(Meta meta) {
        List<CommitFile> commitFiles = meta.getFiles();
        for(CommitFile file:commitFiles){
            String fileFullName = file.getFile_name();
            int index = fileFullName.lastIndexOf("/");
            String fileName = fileFullName.substring(index+ 1, fileFullName.length());
            String prevFilePath = file.getPrev_file_path();
            if(BaseCLDiff.isFilter(prevFilePath)){
                continue;
            }
            String currFilePath = file.getCurr_file_path();
            String parentCommit = file.getParent_commit();
            String basePath = baseCLDiff.mFileOutputLog.metaLinkPath;
            FilePairData fp = new FilePairData(null,null,basePath +"/"+prevFilePath,basePath+"/"+currFilePath,fileName);
            fp.parentCommit = parentCommit;
            filePairDatas.add(fp);
        }
    }


    public void generateDiffMinerOutput(){
        String absolutePath = this.baseCLDiff.mFileOutputLog.metaLinkPath;
        Global.changeEntityFileNameMap = new HashMap<>();
        for(FilePairData fp:filePairDatas){
            Global.parentCommit = fp.parentCommit;
            System.out.println(fp.fileName);
            Global.fileName = fp.fileName;
            if(fp.prev==null && fp.curr==null){
                if(fp.prevPath==null){
//                    this.baseCLDiff.dooNewFile(fp.currPath,absolutePath);
                }else {
                    this.baseCLDiff.doo(fp.prevPath, fp.currPath, absolutePath);
                }
            }else{
                if(fp.prev==null){
                    this.baseCLDiff.dooNewFile(fp.fileName,fp.curr,absolutePath);
                }else {
                    this.baseCLDiff.doo(fp.fileName, fp.prev, fp.curr,absolutePath);
                }
            }

            this.fileChangeEntityData.put(fp.parentCommit +"@@@"+ this.baseCLDiff.changeEntityData.fileName,this.baseCLDiff.changeEntityData);
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
        baseCLDiff.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
        System.out.println(totalFileAssociations.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1,Tree tree2){
        TreeDistance treeDistance = new TreeDistance(tree1,tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}
