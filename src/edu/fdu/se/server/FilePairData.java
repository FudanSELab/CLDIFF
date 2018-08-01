package edu.fdu.se.server;

/**
 * Created by huangkaifeng on 2018/5/25.
 *
 */
public class FilePairData {

    public FilePairData(byte[] prevv, byte[] currr, String prevPathh, String currPathh, String fileNamee) {
        prev = prevv;
        curr = currr;
        prevPath = prevPathh;
        currPath = currPathh;
        fileName = fileNamee;
    }

    public byte[] prev;
    public byte[] curr;
    public String prevPath;
    public String currPath;
    public String fileName;
    public String parentCommit;
}
