package edu.ucla.se;

public class MissingChangeInfo {
    public int startLine;
    public int endLine;
    int group;

    MissingChangeInfo (int s, int e) {
        startLine = s;
        endLine = e;
    }

    MissingChangeInfo (int s, int e, int g) {
        startLine = s;
        endLine = e;
        group = g;
    }
}
