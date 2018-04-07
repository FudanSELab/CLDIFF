package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;

/**
 * Created by huangkaifeng on 4/7/18.
 *
 */
public class Association {

    private ChangeEntity changeEntity1;

    private ChangeEntity changeEntity2;

    private String type;

    public Association(ChangeEntity changeEntity1,ChangeEntity changeEntity2,String type){
        this.changeEntity1 = changeEntity1;
        this.changeEntity2 = changeEntity2;
        this.type = type;
    }
}
