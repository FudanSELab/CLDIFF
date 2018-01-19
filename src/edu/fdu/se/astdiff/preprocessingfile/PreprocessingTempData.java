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


    final static public int BODY_SAME_REMOVE = 10;
    final static public int BODY_DIFFERENT_RETAIN = 11;
    final static public int BODY_INITIALIZED_VALUE = 12;
    final static public int BODY_FATHERNODE_REMOVE = 13;


    public PreprocessingTempData(){
        bodyMapPrev = new HashMap<>();
        bodyMapPrevMethodOrFieldName = new HashMap<>();
        prevNodeVisitingMap1 = new HashMap<>();
        prevNodeVisitingMap2 = new HashMap<>();
        this.removalList = new ArrayList<>();
    }

    protected Map<String, BodyDeclaration> bodyMapPrev;
    protected Map<String, List<BodyDeclaration>> bodyMapPrevMethodOrFieldName;
    /**
     * 0 初始化之后的值  1 遍历到了之后 需要保留的different  2 遍历到了之后 需要删除的same   3 prev中有，curr没有，change：deleted
     */
    protected Map<Integer,Integer> prevNodeVisitingMap1;
    protected Map<Integer,BodyDeclaration> prevNodeVisitingMap2;

    /**
     * list of comments to be removed
     */
    private List<BodyDeclaration> removalList;

    /**
     * method name
     *
     */
    public void addToMapBodyDeclaration(BodyDeclaration bd,String fullDeclaration) {
        this.bodyMapPrev.put(fullDeclaration, bd);

    }

    /**
     * method name
     *
     */
    public void addToMapBodyName(BodyDeclaration bd,String name) {
        if (this.bodyMapPrevMethodOrFieldName.containsKey(name)) {
            List<BodyDeclaration> mList = this.bodyMapPrevMethodOrFieldName.get(name);
            mList.add(bd);
        } else {
            List<BodyDeclaration> mList = new ArrayList<>();
            mList.add(bd);
            this.bodyMapPrevMethodOrFieldName.put(name, mList);
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
            if(item.toString().startsWith("private int mConnectionId")){
                System.out.println("aaa");
            }
            boolean s = item.remove();
            if(!s){
                System.err.println("error: removing return false");
                System.err.println(item.toString());
            }
        }
        this.removalList.clear();
    }

    public void initBodyPrevNodeMap(BodyDeclaration bd,String classPrefix){
        Integer hashCode = bd.hashCode() + classPrefix.hashCode();
        this.prevNodeVisitingMap1.put(hashCode.hashCode(),BODY_INITIALIZED_VALUE);
        this.prevNodeVisitingMap2.put(hashCode.hashCode(),bd);
    }

    public void setBodyPrevNodeMap(BodyDeclaration bd,String classPrefix,int v){
        Integer hashCode = bd.hashCode() + classPrefix.hashCode();
        this.prevNodeVisitingMap1.put(hashCode.hashCode(),v);
    }

    public int getNodeMapValue(int hashKey){
        return this.prevNodeVisitingMap1.get(hashKey);
    }



}
