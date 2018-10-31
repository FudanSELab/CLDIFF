package edu.fdu.se.base.preprocessingfile.data;

import edu.fdu.se.base.common.Global;
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

    public String middleGenPathPrev;
    public String middleGenPathCurr;

    public FileOutputLog(String rootPath,String projName){
        this.rootPath = rootPath;
        this.projName = projName;
    }

    public void setCommitId(String commit,List<String> parentCommits){
        this.commitId = commit;
        this.metaLinkPath = rootPath+"/"+projName+"/"+ commit;

        this.prevSourceFile = metaLinkPath+"/prev";
        this.currSourceFile = metaLinkPath+"/curr";
        this.sourceGen = metaLinkPath+"/gen";
        for(String s:parentCommits){
            File temp = new File(this.prevSourceFile+"/"+s);
            temp.mkdirs();
            temp = new File(this.currSourceFile+"/"+s);
            temp.mkdirs();
            temp = new File(this.sourceGen+"/"+s);
            temp.mkdirs();
            this.middleGenPathPrev = temp.getAbsolutePath() +"/prev";
            this.middleGenPathCurr = temp.getAbsolutePath() +"/curr";
            temp = new File(this.middleGenPathPrev);
            temp.mkdirs();
            temp = new File(this.middleGenPathCurr);
            temp.mkdirs();
        }
    }


    public void setCommitId(String commit){
        this.commitId = commit;
        this.metaLinkPath = rootPath+"/"+projName+"/"+ commit;

        this.prevSourceFile = metaLinkPath+"/prev";
        this.currSourceFile = metaLinkPath+"/curr";
        this.sourceGen = metaLinkPath+"/gen";

        File temp = new File(this.prevSourceFile);
        temp.mkdirs();
        temp = new File(this.currSourceFile);
        temp.mkdirs();
        temp = new File(this.sourceGen);
        temp.mkdirs();
        this.middleGenPathPrev = temp.getAbsolutePath() +"/prev";
        this.middleGenPathCurr = temp.getAbsolutePath() +"/curr";
        temp = new File(this.middleGenPathPrev);
        temp.mkdirs();
        temp = new File(this.middleGenPathCurr);
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
        FileWriter.writeInAll(this.middleGenPathPrev + "/Tree"+Global.fileName+".txt", srcTree);
        FileWriter.writeInAll(this.middleGenPathCurr + "/Tree"+Global.fileName+".txt", dstTree);
    }


    public void writeEntityJson(String json){
        String path = null;
        if("null".equals(Global.parentCommit)){
            path = this.sourceGen +"/Diff"+Global.fileName+".json";
        } else {
            path = this.sourceGen + "/" + Global.parentCommit + "/Diff" + Global.fileName + ".json";
        }
        FileWriter.writeInAll(path, json);
    }

    public void writeSourceFile(byte[] prev,byte[] curr,String fileName){
        if(prev!=null) {
            FileWriter.writeInAll(this.prevSourceFile + "/" + fileName, prev);
        }
        FileWriter.writeInAll(this.currSourceFile + "/"+fileName, curr);
    }

    public void writeMetaFile(String metaJson){
        String path = this.metaLinkPath+"/meta.json";
//        Global.outputFilePathList.add(path);
        FileWriter.writeInAll(path,metaJson);
    }


    public void writeLinkJson(String link){
        String path = this.metaLinkPath+"/link.json";
//        Global.outputFilePathList.add(path);
        FileWriter.writeInAll(path, link);
    }



}
