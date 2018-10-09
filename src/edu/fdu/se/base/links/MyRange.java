package edu.fdu.se.base.links;

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


    public int isRangeWithin(MyRange myRange){
        if(this.startLineNo<=myRange.startLineNo
                && this.endLineNo >= myRange.endLineNo){
            return -1;
        }else if(myRange.startLineNo <= this.startLineNo
                && this.endLineNo <=myRange.endLineNo){
            return 1;
        }else{
            return 0;
        }
    }
}
