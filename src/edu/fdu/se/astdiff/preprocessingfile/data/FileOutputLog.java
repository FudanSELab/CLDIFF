package edu.fdu.se.astdiff.preprocessingfile.data;

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
    public FileOutputLog(String path){
        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
        srcDirFile = new File(rootOutPath + "/prev/" + path);
        dstDirFile = new File(rootOutPath + "/curr/" + path);
        if (!srcDirFile.exists()) {
            srcDirFile.mkdirs();
        }
        if (!dstDirFile.exists()) {
            dstDirFile.mkdirs();
        }
    }

    public FileOutputLog(String path,int a){
        srcDirFile = new File(path + "/prev");
        dstDirFile = new File(path + "/curr");
        if (!srcDirFile.exists()) {
            srcDirFile.mkdirs();
        }
        if (!dstDirFile.exists()) {
            dstDirFile.mkdirs();
        }
    }


    public void writeFileBeforeProcess(PreprocessedData preprocessedData){
//        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/file_before_trim.java", preprocessedData.getSrcCu().toString());
//        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/file_before_trim.java", preprocessedData.getDstCu().toString());
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/file_before_trim.java", preprocessedData.srcLineList);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/file_before_trim.java", preprocessedData.dstLineList);
    }

    public void writeFileAfterProcess(PreprocessedData preprocessedData){
//        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/file_after_trim.java", preprocessedData.getSrcCu().toString());
//        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/file_after_trim.java", preprocessedData.getDstCu().toString());
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/file_after_trim.java", preprocessedData.srcLineList,preprocessedData.srcLines);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/file_after_trim.java", preprocessedData.dstLineList,preprocessedData.dstLines);
    }


    public void writeRQ1CommitFile(byte[] src,byte[] dst,String commitIdFileName,String fileName){
        File prev = new File(srcDirFile.getAbsolutePath()+"/"+commitIdFileName);
        File curr = new File(dstDirFile.getAbsolutePath()+"/"+commitIdFileName);
        if (!prev.exists()) {
            prev.mkdirs();
        }
        if (!curr.exists()) {
            curr.mkdirs();
        }

        FileWriter.writeInAll(srcDirFile.getAbsolutePath()+"/"+commitIdFileName+"/"+fileName,src);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath()+"/"+commitIdFileName+"/"+fileName,dst);
    }


}
