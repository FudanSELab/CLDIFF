package edu.ucla.se;

public class MissingChangeInfo {
    private int startLine;
    private int endLine;
    private String content;

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getContent() {
        return content;
    }

    MissingChangeInfo (int s, int e, String c) {
        startLine = s;
        endLine = e;
        content = c;
    }
}
