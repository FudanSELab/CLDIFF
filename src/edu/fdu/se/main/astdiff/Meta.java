package edu.fdu.se.main.astdiff;

import java.util.ArrayList;
import java.util.List;

public class Meta {
    /**
     * author : amitshekhariitbhu
     * date_time : 2018-02-12T13:16:11Z
     * committer : anandgaurav10
     * commit_hash : 43e48d15e6ee435ed0b1abc6d76638dc8bf0217d
     * commit_log : \n      Provide database instance through interface\n
     * children : null
     * parents : ["52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413"]
     */

    private String project_name;
    private String author;
    private String date_time;
    private String committer;
    private String commit_hash;
    private String commit_log;
    private Object children;
    private List<String> parents;
    private List<CommitFile> files;
    private String linkPath;

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    /**
     * 添加一个文件
     *
     * @param file
     */
    public void addFile(CommitFile file) {
        if (files == null) {
            files = new ArrayList<>();
        }

        files.add(file);
    }

    public List<CommitFile> getFiles() {
        return files;
    }

    public void setFiles(List<CommitFile> files) {
        this.files = files;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getCommitter() {
        return committer;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }

    public String getCommit_hash() {
        return commit_hash;
    }

    public void setCommit_hash(String commit_hash) {
        this.commit_hash = commit_hash;
    }

    public String getCommit_log() {
        return commit_log;
    }

    public void setCommit_log(String commit_log) {
        this.commit_log = commit_log;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }
}
