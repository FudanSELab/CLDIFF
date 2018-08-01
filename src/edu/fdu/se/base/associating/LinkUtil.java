package edu.fdu.se.base.associating;

import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;

/**
 * Created by huangkaifeng on 2018/4/16.
 */
public class LinkUtil {
    public static int isRangeWithin(ChangeEntity ce1, ChangeEntity ce2) {
        MyRange myRange1 = ce1.getLineRange();
        MyRange myRange2 = ce2.getLineRange();
        int res= myRange1.isRangeWithin(myRange2);
        if ( res!= 0) {
            return res;
        } else {
            return res;
        }
    }
}
