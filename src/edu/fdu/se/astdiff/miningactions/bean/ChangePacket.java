package edu.fdu.se.astdiff.miningactions.bean;


import edu.fdu.se.astdiff.miningactions.util.MyList;


/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class ChangePacket {
    public ChangePacket(){

    }
    private MyList<String> changeSet1;
    private MyList<String> changeSet2;

    public MyList<String> getChangeSet1() {
        return changeSet1;
    }

    public MyList<String> getChangeSet2() {
        return changeSet2;
    }



    public void initChangeSet1(){
        this.changeSet1 = new MyList<>();
    }

    public void initChangeSet2(){
        this.changeSet2 = new MyList<>();
    }



}
