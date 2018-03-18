package edu.fdu.se.astdiff.preprocessingfile;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

import java.io.File;

/**
 * Created by huangkaifeng on 2018/3/18.
 *
 */
public class FilePreprocessLog {

    File srcDirFile;
    File dstDirFile;
    public FilePreprocessLog(String path){
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


}
