package edu.fdu.se.cldiff;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.associating.FileInsideAssociationGenerator;
import edu.fdu.se.base.associating.FileOutsideGenerator;
import edu.fdu.se.base.associating.TotalFileAssociations;
import edu.fdu.se.base.associating.similarity.TreeDistance;
import edu.fdu.se.base.common.FilePairData;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.Meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/21.
 *
 */
public class CLDiffContainer {

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public CLDiffCore CLDiffCore;
    public String outputDir;
    private String commitId;
    private List<FilePairData> filePairDatas;

    /**
     * output path +"proj_name" + "commit_id"
     * @param path
     */
    public CLDiffContainer(String path, Meta meta){
        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        CLDiffCore = new CLDiffCore();
        commitId = meta.getCommit_hash();
        String projName = meta.getProject_name();
        CLDiffCore.mFileOutputLog = new FileOutputLog(path, projName);
        List<String> parents = meta.getParents();
        CLDiffCore.mFileOutputLog.setCommitId(commitId,parents);
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
            if(CLDiffCore.isFilter(prevFilePath)){
                continue;
            }
            String currFilePath = file.getCurr_file_path();
            String parentCommit = file.getParent_commit();
            String basePath = CLDiffCore.mFileOutputLog.metaLinkPath;
            FilePairData fp = new FilePairData(null,null,basePath +"/"+prevFilePath,basePath+"/"+currFilePath,fileName);
            fp.setParentCommit(parentCommit);
            filePairDatas.add(fp);
        }
    }


    public void generateDiffMinerOutput(){
        String absolutePath = this.CLDiffCore.mFileOutputLog.metaLinkPath;
        Global.changeEntityFileNameMap = new HashMap<>();
        for(FilePairData fp:filePairDatas){
            Global.parentCommit = fp.getParentCommit();
            System.out.println(fp.getFileName());
            Global.fileName = fp.getFileName();
            if(fp.getPrev()==null && fp.getCurr()==null){
                if(fp.getPrevPath()==null){
//                    this.clDiffCore.dooAddFile(fp.currPath,absolutePath);
                }else {
                    this.CLDiffCore.doo(fp.getPrevPath(), fp.getCurrPath(), absolutePath);
                }
            }else{
                if(fp.getPrev()==null){
                    this.CLDiffCore.dooAddFile(fp.getFileName(),fp.getCurr(),absolutePath);
                }else {
                    this.CLDiffCore.doo(fp.getFileName(), fp.getPrev(), fp.getCurr(),absolutePath);
                }
            }

            this.fileChangeEntityData.put(fp.getParentCommit() +"@@@"+ this.CLDiffCore.changeEntityData.fileName,this.CLDiffCore.changeEntityData);
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
        CLDiffCore.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
        System.out.println(totalFileAssociations.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1, Tree tree2){
        TreeDistance treeDistance = new TreeDistance(tree1,tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}
