package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;

/**
 * Created by huangkaifeng on 2018/4/16.
 */
public class LinkUtil {
    public static boolean isRangeWithin(ChangeEntity ce1, ChangeEntity ce2) {
        MyRange myRange1 = ce1.getLineRange();
        MyRange myRange2 = ce2.getLineRange();
        if (myRange1.isRangeWithin(myRange2) != 0) {
            return true;
        } else {
            return false;
        }
    }
}
