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

    public File srcDirFile;
    public File dstDirFile;
    public String path;

    public FileOutputLog(String path){
        srcDirFile = new File(path + "/prev/gen");
        dstDirFile = new File(path + "/curr/gen");

        if(!srcDirFile.exists()){
            srcDirFile.mkdirs();
        }
        if(!dstDirFile.exists()){
            dstDirFile.mkdirs();
        }
    }


    public void writeFileBeforeProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/BeforeTrim"+Global.fileName, preprocessedData.srcLineList);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/BeforeTrim"+Global.fileName, preprocessedData.dstLineList);
    }

    public void writeFileAfterProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/AfterTrim"+Global.fileName, preprocessedData.srcLineList,preprocessedData.srcLines);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/AfterTrim"+Global.fileName, preprocessedData.dstLineList,preprocessedData.dstLines);
    }


    public void writeTreeFile(String srcTree,String dstTree){
        FileWriter.writeInAll(this.srcDirFile.getAbsolutePath() + "/Tree"+Global.fileName+".txt", srcTree);
        FileWriter.writeInAll(this.dstDirFile.getAbsolutePath() + "/Tree"+Global.fileName+".txt", dstTree);
    }


    public void writeEntityJson(String json){
        FileWriter.writeInAll(this.srcDirFile.getAbsolutePath() +  "/Diff"+Global.fileName+".json", json);
        FileWriter.writeInAll(this.dstDirFile.getAbsolutePath() +  "/Diff"+Global.fileName+".json", json);
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
