package edu.ucla.se;

public class MissingChangeInfo {
    public int startLine;
    public int endLine;
    public String content;
    public boolean foundByRegex;
    public boolean foundByToken;

    MissingChangeInfo (int s, int e, String c) {
        startLine = s;
        endLine = e;
        content = c;
    }
}
