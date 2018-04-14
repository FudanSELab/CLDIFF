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
//    public FileOutputLog(String path){
//        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
//        srcDirFile = new File(rootOutPath + "/prev/" + path);
//        dstDirFile = new File(rootOutPath + "/curr/" + path);
//        if (!srcDirFile.exists()) {
//            srcDirFile.mkdirs();
//        }
//        if (!dstDirFile.exists()) {
//            dstDirFile.mkdirs();
//        }
//    }

    public FileOutputLog(String path){
        srcDirFile = new File(path + "/prev");
        File tmp1 = new File(path+"/prev/gen");
        dstDirFile = new File(path + "/curr");
        File tmp2 = new File(path+"/curr/gen");
//        if (!srcDirFile.exists()) {
//            srcDirFile.mkdirs();
//        }
//        if (!dstDirFile.exists()) {
//            dstDirFile.mkdirs();
//        }
        if(!tmp1.exists()){
            tmp1.mkdirs();
        }
        if(!tmp2.exists()){
            tmp2.mkdirs();
        }
    }


    public void writeFileBeforeProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/gen/file_before_trim.java", preprocessedData.srcLineList);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/gen/file_before_trim.java", preprocessedData.dstLineList);
    }

    public void writeFileAfterProcess(PreprocessedData preprocessedData){
        FileWriter.writeInAll(srcDirFile.getAbsolutePath() + "/gen/file_after_trim.java", preprocessedData.srcLineList,preprocessedData.srcLines);
        FileWriter.writeInAll(dstDirFile.getAbsolutePath() + "/gen/file_after_trim.java", preprocessedData.dstLineList,preprocessedData.dstLines);
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
