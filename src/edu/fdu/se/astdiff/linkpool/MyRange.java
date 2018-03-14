package edu.fdu.se.astdiff.linkpool;

/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class MyRange {

    public static int TYPE_PREV = 10000;
    public static int TYPE_CURR = 10001;

    public int type;

    public int startLineNo;
    public int endLineNo;

    public MyRange(int start,int end,int type){
        this.startLineNo = start;
        this.endLineNo = end;
        this.type = type;
    }

}
