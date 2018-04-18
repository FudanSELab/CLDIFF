package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/4/16.
 *
 *
 */
public class FileOutsideGenerator {

    public String fileA;
    public String fileB;

    public ChangeEntityData ce1;

    public ChangeEntityData ce2;
    public List<Association> mAssos;
    public FileOutsideGenerator(){
        mAssos = new ArrayList<>();
    }


    public void generateOutsideAssociation(ChangeEntityData ca,ChangeEntityData cb){
        mAssos.clear();
        ce1 = ca;
        ce2 = cb;


    }
}
