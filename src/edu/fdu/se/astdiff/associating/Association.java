package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;

/**
 * Created by huangkaifeng on 4/7/18.
 *
 */
public class Association {

    private String file;

    private ChangeEntity changeEntity1;

    private ChangeEntity changeEntity2;

    private String type;

    public Association(ChangeEntity changeEntity1,ChangeEntity changeEntity2,String type){
        this.changeEntity1 = changeEntity1;
        this.changeEntity2 = changeEntity2;
        this.type = type;
    }

    public ChangeEntity getChangeEntity1() {
        return changeEntity1;
    }

    public void setChangeEntity1(ChangeEntity changeEntity1) {
        this.changeEntity1 = changeEntity1;
    }

    public ChangeEntity getChangeEntity2() {
        return changeEntity2;
    }

    public void setChangeEntity2(ChangeEntity changeEntity2) {
        this.changeEntity2 = changeEntity2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
