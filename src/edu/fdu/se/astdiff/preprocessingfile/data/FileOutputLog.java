package edu.fdu.se.astdiff.preprocessingfile.data;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

import java.io.File;

/**
 * Created by huangkaifeng on 2018/3/18.
 *
 */
public class FileOutputLog {

    public String rootPath;
    public String projName;
    public String commitId;
    public String metaLinkPath;
    public String currSourceFile;
    public String prevSourceFile;
    public String currSourceGen;
    public String prevSourceGen;
    public FileOutputLog(String rootPath,String projName){
        this.rootPath = rootPath;
        this.projName = projName;
    }

    public void setCommitId(String commit){
        this.commitId = commit;
        this.metaLinkPath = rootPath+"\\"+projName+"\\"+ commit;
        this.prevSourceFile = metaLinkPath+"\\prev";
        this.currSourceFile = metaLinkPath+"\\curr";
        this.prevSourceGen = metaLinkPath+"\\prev\\gen";
        this.currSourceGen = metaLinkPath +"\\curr\\gen";
        File srcDirFile = new File(this.prevSourceGen);
        File dstDirFile = new File(this.currSourceGen);
        if(!srcDirFile.exists()){
            srcDirFile.mkdirs();
        }
        if(!dstDirFile.exists()){
            dstDirFile.mkdirs();
        }
    }


    public void writeFileBeforeProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(this.prevSourceGen + "/BeforeTrim"+Global.fileName, preprocessedData.srcLineList);
        FileWriter.writeInAll(this.currSourceGen + "/BeforeTrim"+Global.fileName, preprocessedData.dstLineList);
    }

    public void writeFileAfterProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(this.prevSourceGen + "/AfterTrim"+Global.fileName, preprocessedData.srcLineList,preprocessedData.srcLines);
        FileWriter.writeInAll(this.currSourceGen + "/AfterTrim"+Global.fileName, preprocessedData.dstLineList,preprocessedData.dstLines);
    }


    public void writeTreeFile(String srcTree,String dstTree){
        FileWriter.writeInAll(this.prevSourceGen + "/Tree"+Global.fileName+".txt", srcTree);
        FileWriter.writeInAll(this.currSourceGen + "/Tree"+Global.fileName+".txt", dstTree);
    }


    public void writeEntityJson(String json){
        FileWriter.writeInAll(this.prevSourceGen +  "/Diff"+Global.fileName+".json", json);
        FileWriter.writeInAll(this.currSourceGen +  "/Diff"+Global.fileName+".json", json);
    }

    public void writeSourceFile(byte[] prev,byte[] curr,String fileName){
        FileWriter.writeInAll(this.prevSourceFile + "\\"+fileName, prev);
        FileWriter.writeInAll(this.currSourceFile + "\\"+fileName, curr);
    }

    public void writeMetaFile(String metaJson){
        FileWriter.writeInAll(this.metaLinkPath+"\\meta.json",metaJson);
    }


    public void writeLinkJson(String link){
        FileWriter.writeInAll(this.metaLinkPath+"\\link.json", link);
    }
//    public void writeRQ1CommitFile(byte[] src,byte[] dst,String commitIdFileName,String fileName){
//        File prev = new File(path+"/"+commitIdFileName);
//        File curr = new File(path+"/"+commitIdFileName);
//        if (!prev.exists()) {
//            prev.mkdirs();
//        }
//        if (!curr.exists()) {
//            curr.mkdirs();
//        }
//
//        FileWriter.writeInAll(srcDirFile.getAbsolutePath()+"/"+commitIdFileName+"/"+fileName,src);
//        FileWriter.writeInAll(dstDirFile.getAbsolutePath()+"/"+commitIdFileName+"/"+fileName,dst);
//    }


}
