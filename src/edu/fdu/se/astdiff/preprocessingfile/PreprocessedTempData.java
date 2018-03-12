package edu.fdu.se.astdiff.preprocessingfile;


import org.eclipse.jdt.core.dom.BodyDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/18.
 *
 */
public class PreprocessedTempData {

    /**
     * 已经设置为same remove有可能被 overload遍历到，设置为retain，需要加check
     */
    final static public int BODY_SAME_REMOVE = 10;
    /**
     * 已经为retain ，如果遍历到发现是same remove 则可以re- set 为remove
     */
    final static public int BODY_DIFFERENT_RETAIN = 11;
    final static public int BODY_INITIALIZED_VALUE = 12;
    /**
     * 已经设置为remove的，表示curr中cod已经被删除，所以不会再revisit到
     */
    final static public int BODY_FATHERNODE_REMOVE = 13;


    public PreprocessedTempData(){
        prevNodeBodyDeclarationMap = new HashMap<>();
        prevNodeBodyNameMap = new HashMap<>();
        prevNodeVisitingMap = new HashMap<>();
        this.removalList = new ArrayList<>();
    }

    protected Map<String, BodyDeclarationPair> prevNodeBodyDeclarationMap;
    protected Map<String, List<BodyDeclarationPair>> prevNodeBodyNameMap;
    /**
     * 0 初始化之后的值  1 遍历到了之后 需要保留的different  2 遍历到了之后 需要删除的same   3 prev中有，curr没有，change：deleted
     */
    public Map<BodyDeclarationPair,Integer> prevNodeVisitingMap;

    /**
     * list of comments to be removed
     */
    public List<BodyDeclaration> removalList;

    /**
     * method name
     *
     */
    public void addToMapBodyDeclaration(BodyDeclarationPair bd,String fullDeclaration) {
        this.prevNodeBodyDeclarationMap.put(fullDeclaration, bd);

    }

    /**
     * method name
     *
     */
    public void addToMapBodyName(BodyDeclarationPair bd,String name) {
        if (this.prevNodeBodyNameMap.containsKey(name)) {
            List<BodyDeclarationPair> mList = this.prevNodeBodyNameMap.get(name);
            mList.add(bd);
        } else {
            List<BodyDeclarationPair> mList = new ArrayList<>();
            mList.add(bd);
            this.prevNodeBodyNameMap.put(name, mList);
        }
    }

    /**
     * revomal list prev+curr
     * @param bd
     */
    public void addToRemoveList(BodyDeclaration bd) {
        this.removalList.add(bd);
    }

    public void removeRemovalList() {
        for (BodyDeclaration item : this.removalList) {
            item.delete();
        }
        this.removalList.clear();
    }

    public void initBodyPrevNodeMap(BodyDeclaration bd,String classPrefix){
        this.prevNodeVisitingMap.put(new BodyDeclarationPair(bd,classPrefix),BODY_INITIALIZED_VALUE);
    }

    /**
     *
     * @param v
     */
    public void setBodyPrevNodeMap(BodyDeclarationPair bdp,int v){
        this.prevNodeVisitingMap.put(bdp,v);
    }

    public int getNodeMapValue(BodyDeclarationPair bodyDeclarationPair){
        return this.prevNodeVisitingMap.get(bodyDeclarationPair);
    }



}
