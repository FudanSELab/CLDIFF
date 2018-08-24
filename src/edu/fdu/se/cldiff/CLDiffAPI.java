package edu.fdu.se.cldiff;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.common.FilePairData;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.associating.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.server.Meta;
import edu.fdu.se.server.CommitFile;

import java.util.*;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class CLDiffAPI {

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public CLDiffCore clDiffCore;
    public String outputDir;
    private String commitId;
    private List<FilePairData> filePairDatas;
//    public Meta meta;

    /**
     * output path +"proj_name" + "commit_id"
     * @param outputDir
     */
    public CLDiffAPI(String outputDir, Meta meta){
        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        clDiffCore = new CLDiffCore();
        commitId = meta.getCommit_hash();
        String projName = meta.getProject_name();
        clDiffCore.mFileOutputLog = new FileOutputLog(outputDir, projName);
        List<String> parents = meta.getParents();
        clDiffCore.mFileOutputLog.setCommitId(commitId,parents);
        this.outputDir = outputDir;
        initDataFromJson(meta);
    }

    public CLDiffAPI(Meta meta){
        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        clDiffCore = new CLDiffCore();
        commitId = meta.getCommit_hash();
        String projName = meta.getProject_name();
        clDiffCore.mFileOutputLog = new FileOutputLog(meta.getOutputDir(), projName);
        List<String> parents = meta.getParents();
        clDiffCore.mFileOutputLog.setCommitId(commitId,parents);
        this.outputDir = meta.getOutputDir();
        initDataFromJson(meta);
    }



    public void initDataFromJson(Meta meta) {
        List<CommitFile> commitFiles = meta.getFiles();
        for(CommitFile file:commitFiles){
            String fileFullName = file.getFile_name();
            int index = fileFullName.lastIndexOf("/");
            String fileName = fileFullName.substring(index+ 1, fileFullName.length());
            String prevFilePath = file.getPrev_file_path();
            if(CLDiffCore.isFilter(prevFilePath)){
                continue;
            }
            String currFilePath = file.getCurr_file_path();
            String parentCommit = file.getParent_commit();
            String basePath = clDiffCore.mFileOutputLog.metaLinkPath;
            FilePairData fp = new FilePairData(null,null,basePath +"/"+prevFilePath,basePath+"/"+currFilePath,fileName);
            fp.setParentCommit(parentCommit);
            filePairDatas.add(fp);
        }
    }


    public void generateDiffMinerOutput(){
        String absolutePath = this.clDiffCore.mFileOutputLog.metaLinkPath;
        Global.changeEntityFileNameMap = new HashMap<>();
        for(FilePairData fp:filePairDatas){
            Global.parentCommit = fp.getParentCommit();
            System.out.println(fp.getFileName());
            Global.fileName = fp.getFileName();
            if(fp.getPrev()==null && fp.getCurr()==null){
                if(fp.getPrevPath()==null){
//                    this.clDiffCore.dooNewFile(fp.currPath,absolutePath);
                }else {
                    this.clDiffCore.doo(fp.getPrevPath(), fp.getCurrPath(), absolutePath);
                }
            }else{
                if(fp.getPrev()==null){
                    this.clDiffCore.dooNewFile(fp.getFileName(),fp.getCurr(),absolutePath);
                }else {
                    this.clDiffCore.doo(fp.getFileName(), fp.getPrev(), fp.getCurr(),absolutePath);
                }
            }

            this.fileChangeEntityData.put(fp.getParentCommit() +"@@@"+ this.clDiffCore.changeEntityData.fileName,this.clDiffCore.changeEntityData);
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
        clDiffCore.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
        System.out.println(totalFileAssociations.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1,Tree tree2){
        TreeDistance treeDistance = new TreeDistance(tree1,tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}
