package edu.ucla.se;

public enum P_LANG {
    JAVA, JAVASCRIPT, PYTHON, C, CPP;

    public String getExtension() {
        switch (this) {
            case JAVA:
                return "java";
            case C:
                return "c";
            case CPP:
                return "cpp";
            case PYTHON:
                return "python";
            case JAVASCRIPT:
                return "js";
            default:
                return "txt";
        }
    }
}
