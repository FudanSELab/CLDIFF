package edu.fdu.se.cldiff;

import com.github.gumtreediff.tree.Tree;
import com.google.gson.GsonBuilder;
import edu.fdu.se.base.common.FilePairData;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.links.FileInnerLinksGenerator;
import edu.fdu.se.base.links.FileOuterLinksGenerator;
import edu.fdu.se.base.links.TotalFileLinks;
import edu.fdu.se.base.links.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.server.Meta;
import edu.fdu.se.server.CommitFile;
import org.eclipse.jgit.internal.storage.file.GlobalAttributesNode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by huangkaifeng on 2018/4/12.
 */
public class CLDiffAPI {

    private Map<String, ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public CLDiffCore clDiffCore;
    private List<FilePairData> filePairDatas;

    /**
     * output path +"proj_name" + "commit_id"
     *
     * @param outputDir
     */
    public CLDiffAPI(String outputDir, Meta meta) {
//        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        clDiffCore = new CLDiffCore();
        clDiffCore.mFileOutputLog = new FileOutputLog(outputDir, meta.getProject_name(), meta.getProjectOwnerName());
        clDiffCore.mFileOutputLog.setCommitId(meta.getCommit_hash(), meta.getParents());
        initDataFromJson(meta);
        FileUtil.createFile("meta.json", new GsonBuilder().setPrettyPrinting().create().toJson(meta), new File(clDiffCore.mFileOutputLog.metaLinkPath));
    }


    public void initDataFromJson(Meta meta) {
        List<CommitFile> commitFiles = meta.getFiles();
        List<String> actions = meta.getActions();
        for (int i = 0; i < commitFiles.size(); i++) {
            CommitFile file = commitFiles.get(i);
            if (file.getDiffPath() == null) {
                continue;
            }
            String action = actions.get(i);
            String fileFullName = file.getFile_name();
            int index = fileFullName.lastIndexOf("/");
            String fileName = fileFullName.substring(index + 1, fileFullName.length());
            String prevFilePath = file.getPrev_file_path();
            String currFilePath = file.getCurr_file_path();
            String parentCommit = file.getParent_commit();
            String basePath = clDiffCore.mFileOutputLog.metaLinkPath;
            byte[] prevBytes = null;
            byte[] currBytes = null;
            try {
                if (prevFilePath != null) {
                    prevBytes = Files.readAllBytes(Paths.get(basePath + "/" + prevFilePath));
                }
                if (currFilePath != null) {
                    currBytes = Files.readAllBytes(Paths.get(basePath + "/" + currFilePath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilePairData fp = new FilePairData(prevBytes, currBytes, basePath + "/" + prevFilePath, basePath + "/" + currFilePath, fileName);
            fp.setParentCommit(parentCommit);
            filePairDatas.add(fp);
        }
    }


    public void generateDiffMinerOutput() {
        String absolutePath = this.clDiffCore.mFileOutputLog.metaLinkPath;
        Global.changeEntityFileNameMap = new HashMap<>();
        for (FilePairData fp : filePairDatas) {
            Global.parentCommit = fp.getParentCommit();
            System.out.println(fp.getFileName());
            Global.fileName = fp.getFileName();
            if (fp.getPrev() == null && fp.getCurr() == null) {
                continue;
            }
            if (fp.getPrev() == null) {
                this.clDiffCore.dooAddFile(fp.getFileName(), fp.getCurr(), absolutePath);
            } else if (fp.getCurr() == null) {
                this.clDiffCore.dooRemoveFile(fp.getFileName(), fp.getPrev(), absolutePath);
            } else {
                this.clDiffCore.dooDiffFile(fp.getFileName(), fp.getPrev(), fp.getCurr(), absolutePath);
            }
            this.fileChangeEntityData.put(fp.getParentCommit() + "@@@" + this.clDiffCore.changeEntityData.fileName, this.clDiffCore.changeEntityData);
        }
        List<String> fileNames = new ArrayList<>(this.fileChangeEntityData.keySet());
        TotalFileLinks totalFileLinks = new TotalFileLinks();
        for (int i = 0; i < fileNames.size(); i++) {
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            Global.ced = cedA;
            FileInnerLinksGenerator associationGenerator = new FileInnerLinksGenerator(cedA);
            associationGenerator.generateFile();
            totalFileLinks.addEntry(fileNameA, cedA.mLinks);
        }
        for (int i = 0; i < fileNames.size(); i++) {
            String fileNameA = fileNames.get(i);
            //存在fileName为null的情况，处理fileName为何为null的情况之后再做处理。
                ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
                FileOuterLinksGenerator fileOuterLinksGenerator = new FileOuterLinksGenerator();
                for (int j = i + 1; j < fileNames.size(); j++) {
                    String fileNameB = fileNames.get(j);
                    //存在fileName为null的情况
                        ChangeEntityData cedB = this.fileChangeEntityData.get(fileNameB);
                        fileOuterLinksGenerator.generateOutsideAssociation(cedA, cedB);
                        totalFileLinks.addFile2FileAssos(fileNameA, fileNameB, fileOuterLinksGenerator.mAssos);
                }
        }
        new FileOuterLinksGenerator().checkSimilarity(this.fileChangeEntityData,totalFileLinks);
        clDiffCore.mFileOutputLog.writeLinkJson(totalFileLinks.toAssoJSonString());
        System.out.println(totalFileLinks.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1, Tree tree2) {
        TreeDistance treeDistance = new TreeDistance(tree1, tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}
