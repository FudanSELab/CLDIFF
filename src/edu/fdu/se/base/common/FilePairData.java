package edu.fdu.se.base.common;

/**
 * Created by huangkaifeng on 2018/8/21.
 */
public class FilePairData {

    public FilePairData(byte[] prevv, byte[] currr, String prevPathh, String currPathh, String fileNamee) {
        prev = prevv;
        curr = currr;
        prevPath = prevPathh;
        currPath = currPathh;
        fileName = fileNamee;
    }

    private byte[] prev;

    public byte[] getPrev() {
        return prev;
    }

    public byte[] getCurr() {
        return curr;
    }

    public String getPrevPath() {
        return prevPath;
    }

    public String getCurrPath() {
        return currPath;
    }

    public String getFileName() {
        return fileName;
    }

    private byte[] curr;
    private String prevPath;
    private String currPath;
    private String fileName;
    private String parentCommit;

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }


}
