package edu.fdu.se.astdiff.preprocessingfile;

import com.github.javaparser.ast.body.BodyDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/18.
 */
public class PreprocessingTempData {

    public PreprocessingTempData(){
        bodyMapPrev = new HashMap<>();
        bodyMapPrevMethodOrFieldName = new HashMap<>();
        noRemovePrevNode = new HashMap<>();
    }

    protected Map<String, BodyDeclaration> bodyMapPrev;
    protected Map<String, List<BodyDeclaration>> bodyMapPrevMethodOrFieldName;
    /**
     * setting to 1 as not removed
     */
    protected Map<BodyDeclaration, Integer> noRemovePrevNode;

    /**
     * list of comments to be removed
     */
    protected List<BodyDeclaration> removalList;

    /**
     * method name
     *
     * @param key
     * @param bd
     */
    public void addToBodyMapPrevMethodNameOrFieldName(String key, BodyDeclaration bd) {
        if (this.bodyMapPrevMethodOrFieldName.containsKey(key)) {
            List<BodyDeclaration> mList = this.bodyMapPrevMethodOrFieldName.get(key);
            mList.add(bd);
        } else {
            List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
            mList.add(bd);
            this.bodyMapPrevMethodOrFieldName.put(key, mList);
        }
    }

    /**
     * revomal list prev+curr
     * @param bd
     */
    public void addToRemoveList(BodyDeclaration bd) {
        if (this.removalList == null) {
            this.removalList = new ArrayList<>();
        }
        this.removalList.add(bd);
    }

    public void removeRemovalList() {
        for (BodyDeclaration item : this.removalList) {
            boolean s = item.remove();
            if(!s){
                System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%%%%% ERROR 0000000000000000000000000000000000000000000000000000000");
            }
        }
    }



}
