package edu.fdu.se.astdiff.miningactions.util;

import java.util.ArrayList;

/**
 * Created by huangkaifeng on 2018/3/29.
 */
public class MyList<E> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        if(this.contains(e)){
            return true;
        }
        super.add(e);
        return true;
    }


}
