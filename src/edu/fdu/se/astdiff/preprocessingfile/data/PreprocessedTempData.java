package edu.fdu.se.astdiff.preprocessingfile.data;


import javassist.compiler.ast.MethodDecl;
import org.eclipse.jdt.core.dom.*;

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
        srcNodeBodyNameMap = new HashMap<>();
        srcNodeVisitingMap = new HashMap<>();
        srcNodeHashCodeMap = new HashMap<>();
        srcRemovalNodes = new ArrayList<>();
        dstRemovalNodes = new ArrayList<>();
    }

    public Map<String, List<BodyDeclarationPair>> srcNodeBodyNameMap;
    /**
     * 0 初始化之后的值  1 遍历到了之后 需要保留的different  2 遍历到了之后 需要删除的same   3 prev中有，curr没有，change：deleted
     */
    public Map<BodyDeclarationPair,Integer> srcNodeVisitingMap;

    public Map<BodyDeclarationPair,Integer> srcNodeHashCodeMap;

    /**
     * list of comments to be removed
     */
    public List<ASTNode> srcRemovalNodes;
    public List<ASTNode> dstRemovalNodes;


    /**
     * method name
     *
     */
    public void addToMapBodyName(BodyDeclarationPair bd,String name) {
        if (this.srcNodeBodyNameMap.containsKey(name)) {
            List<BodyDeclarationPair> mList = this.srcNodeBodyNameMap.get(name);
            mList.add(bd);
        } else {
            List<BodyDeclarationPair> mList = new ArrayList<>();
            mList.add(bd);
            this.srcNodeBodyNameMap.put(name, mList);
        }
    }


    public void addToSrcRemoveList(ASTNode bd) {
        this.srcRemovalNodes.add(bd);
    }

    private void setLinesFlag(List<Integer> lineFlags,int start,int end){
        for(int i =start ;i<=end;i++){
            if(lineFlags.get(i-1)>0){
                lineFlags.set(i-1, -lineFlags.get(i-1));
            }
        }
    }

    public void removeSrcRemovalList(CompilationUnit cu, List<Integer> lineList) {
        for (ASTNode item : this.srcRemovalNodes) {
//            if(item instanceof MethodDeclaration){
//                MethodDeclaration md = (MethodDeclaration) item;
//                if(md.getName().toString().startsWith("create")){
//                    System.out.println(md.getName().toString());
//
//                }
//            }
            setLinesFlag(lineList,cu.getLineNumber(item.getStartPosition()),
                    cu.getLineNumber(item.getStartPosition()+item.getLength()-1));
            item.delete();
        }
        this.srcRemovalNodes.clear();
    }

    public void addToDstRemoveList(ASTNode bd) {
        dstRemovalNodes.add(bd);
    }

    public void removeDstRemovalList(CompilationUnit cu, List<Integer> lineList) {
        for (ASTNode item : this.dstRemovalNodes) {
            if(item instanceof BodyDeclaration){
                BodyDeclaration bd = (BodyDeclaration) item;
                if(bd.getJavadoc()!=null){
                    setLinesFlag(lineList,cu.getLineNumber(bd.getJavadoc().getStartPosition()),
                            cu.getLineNumber(bd.getJavadoc().getStartPosition()+bd.getJavadoc().getLength()-1));
                }
            }
            setLinesFlag(lineList,cu.getLineNumber(item.getStartPosition()),
                    cu.getLineNumber(item.getStartPosition()+item.getLength()-1));
            item.delete();
        }
        dstRemovalNodes.clear();
    }

    public void initBodySrcNodeMap(BodyDeclarationPair bodyDeclarationPair){
        this.srcNodeVisitingMap.put(bodyDeclarationPair,BODY_INITIALIZED_VALUE);
    }

    /**
     *
     * @param v
     */
    public void setBodySrcNodeMap(BodyDeclarationPair bdp, int v){
        this.srcNodeVisitingMap.put(bdp,v);
    }

    public int getNodeMapValue(BodyDeclarationPair bodyDeclarationPair){
        return this.srcNodeVisitingMap.get(bodyDeclarationPair);
    }


    public void removeAllSrcComments(CompilationUnit cu,List<Integer> lineList) {
        PackageDeclaration packageDeclaration = cu.getPackage();
        if (packageDeclaration != null)
            addToSrcRemoveList(packageDeclaration);
        List<ASTNode> commentList = cu.getCommentList();
        for (int i = commentList.size() - 1; i >= 0; i--) {
            if(commentList.get(i) instanceof Javadoc){
                addToSrcRemoveList(commentList.get(i));
            }
        }
        List<ImportDeclaration> imprortss = cu.imports();
        for (int i = imprortss.size() - 1; i >= 0; i--) {
            addToSrcRemoveList(imprortss.get(i));
        }
        removeSrcRemovalList(cu,lineList);
    }

    public void removeAllDstComments(CompilationUnit cu,List<Integer> lineList) {
        List<ASTNode> commentList = cu.getCommentList();
        PackageDeclaration packageDeclaration = cu.getPackage();
        if (packageDeclaration != null)
            addToDstRemoveList(packageDeclaration);
        List<ImportDeclaration> imprortss = cu.imports();
        for (int i = commentList.size() - 1; i >= 0; i--) {
            if(commentList.get(i) instanceof Javadoc) {
                addToDstRemoveList(commentList.get(i));
            }
        }
        for (int i = imprortss.size() - 1; i >= 0; i--) {
            addToDstRemoveList(imprortss.get(i));
        }
        removeDstRemovalList(cu,lineList);
    }

}
