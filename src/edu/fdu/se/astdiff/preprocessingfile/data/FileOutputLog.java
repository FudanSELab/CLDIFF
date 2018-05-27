package edu.fdu.se.astdiff.preprocessingfile.data;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

import java.io.File;
import java.util.List;

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
    public String sourceGen;
    public FileOutputLog(String rootPath,String projName){
        this.rootPath = rootPath;
        this.projName = projName;
    }

    public void setCommitId(String commit,List<String> parentCommits){
        this.commitId = commit;
        this.metaLinkPath = rootPath+"\\"+projName+"\\"+ commit;

        this.prevSourceFile = metaLinkPath+"\\prev";
        this.currSourceFile = metaLinkPath+"\\curr";
        this.sourceGen = metaLinkPath+"\\gen";
        for(String s:parentCommits){
            File temp = new File(this.prevSourceFile+"/"+s);
            temp.mkdirs();
            temp = new File(this.currSourceFile+"/"+s);
            temp.mkdirs();
            temp = new File(this.sourceGen+"/"+s);
            temp.mkdirs();
        }
    }


    public void setCommitId(String commit){
        this.commitId = commit;
        this.metaLinkPath = rootPath+"\\"+projName+"\\"+ commit;

        this.prevSourceFile = metaLinkPath+"\\prev";
        this.currSourceFile = metaLinkPath+"\\curr";
        this.sourceGen = metaLinkPath+"\\gen";

            File temp = new File(this.prevSourceFile);
            temp.mkdirs();
            temp = new File(this.currSourceFile);
            temp.mkdirs();
            temp = new File(this.sourceGen);
            temp.mkdirs();
    }


//    public void writeFileBeforeProcess(PreprocessedData preprocessedData){
//        FileWriter.writeInAll(this.prevSourceGen + "/BeforeTrim"+Global.fileName, preprocessedData.srcLineList);
//        FileWriter.writeInAll(this.currSourceGen + "/BeforeTrim"+Global.fileName, preprocessedData.dstLineList);
//    }
//
//    public void writeFileAfterProcess(PreprocessedData preprocessedData){
//        FileWriter.writeInAll(this.prevSourceGen + "/AfterTrim"+Global.fileName, preprocessedData.srcLineList,preprocessedData.srcLines);
//        FileWriter.writeInAll(this.currSourceGen + "/AfterTrim"+Global.fileName, preprocessedData.dstLineList,preprocessedData.dstLines);
//    }
//

    public void writeTreeFile(String srcTree,String dstTree){
        FileWriter.writeInAll(this.prevSourceFile + "/Tree"+Global.fileName+".txt", srcTree);
        FileWriter.writeInAll(this.currSourceFile + "/Tree"+Global.fileName+".txt", dstTree);
    }


    public void writeEntityJson(String json){
        FileWriter.writeInAll(this.sourceGen +"/"+Global.parentCommit+"/Diff"+Global.fileName+".json", json);
    }

    public void writeSourceFile(byte[] prev,byte[] curr,String fileName){
        if(prev!=null) {
            FileWriter.writeInAll(this.prevSourceFile + "\\" + fileName, prev);
        }
        FileWriter.writeInAll(this.currSourceFile + "\\"+fileName, curr);
    }

    public void writeMetaFile(String metaJson){
        FileWriter.writeInAll(this.metaLinkPath+"\\meta.json",metaJson);
    }


    public void writeLinkJson(String link){
        FileWriter.writeInAll(this.metaLinkPath+"\\link.json", link);
    }



}
