package edu.ucla.se;

public class MissingChangeInfo {
    public int startLine;
    public int endLine;
    public String content;
    int group;

    MissingChangeInfo (int s, int e, String c) {
        startLine = s;
        endLine = e;
        content = c;
    }

    MissingChangeInfo (int s, int e, String c, int g) {
        startLine = s;
        endLine = e;
        content = c;
        group = g;
    }
}
