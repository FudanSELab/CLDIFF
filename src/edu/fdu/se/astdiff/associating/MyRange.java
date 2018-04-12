package edu.fdu.se.astdiff.associating;

/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class MyRange {

    /**
     * type insert/delete
     */
    public int type;

    public int startLineNo;
    public int endLineNo;

    public MyRange(int start,int end,int type){
        this.startLineNo = start;
        this.endLineNo = end;
        this.type = type;
    }

    @Override
    public String toString(){
        return "("+this.startLineNo+","+this.endLineNo+")";
    }

    public String toJsonString(){
        return this.startLineNo+","+this.endLineNo;
    }

}
