package edu.fdu.se.server;

/**
 * 用于生成
 */

public class CommitFile {

    /**
     * file_name : ExecutorConfigurationSupport.java
     * prev_file_path : prev/9d63f805b3b3ad07f102f6df779b852b2d1f306c/ExecutorConfigurationSupport.java
     * curr_file_path : curr/9d63f805b3b3ad07f102f6df779b852b2d1f306c/ExecutorConfigurationSupport.java
     * parent_commit : 9d63f805b3b3ad07f102f6df779b852b2d1f306c
     */
    private int id;
    private String file_name;
    private String prev_file_path;
    private String curr_file_path;
    private String parent_commit;
    private String diffPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiffPath() {
        return diffPath;
    }

    public void setDiffPath(String diffPath) {
        this.diffPath = diffPath;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPrev_file_path() {
        return prev_file_path;
    }

    public void setPrev_file_path(String prev_file_path) {
        this.prev_file_path = prev_file_path;
    }

    public String getCurr_file_path() {
        return curr_file_path;
    }

    public void setCurr_file_path(String curr_file_path) {
        this.curr_file_path = curr_file_path;
    }

    public String getParent_commit() {
        return parent_commit;
    }

    public void setParent_commit(String parent_commit) {
        this.parent_commit = parent_commit;
    }

    public CommitFile() {
    }

}
