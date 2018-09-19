package edu.fdu.se.base.preprocessingfile;

import edu.fdu.se.base.links.MyRange;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.member.EnumChangeEntity;
import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedTempData;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/2.
 *
 */
public class DstBodyCheck {

    public BodyDeclarationPair getExactBodyDeclarationPair(List<BodyDeclarationPair> bodyDeclarationPairs,Class clazz){
        for(BodyDeclarationPair bodyDeclarationPair:bodyDeclarationPairs){
            if(bodyDeclarationPair.getBodyDeclaration().getClass().equals(clazz)){
                return bodyDeclarationPair;
            }
        }
        return null;
    }

    /**
     * visited
     */
    public int checkFieldDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, FieldDeclaration fd, String prefix) {

        List<VariableDeclarationFragment> vdList = fd.fragments();
        for (VariableDeclarationFragment vd : vdList) {
            String key = prefix + vd.getName().toString();
            compareResult.currFieldNames.add(vd.getName().toString());
            compareResult.prevCurrFieldNames.add(vd.getName().toString());
            boolean newFieldFlag = true;
            if (compareCache.srcNodeBodyNameMap.containsKey(key)) {
                List<BodyDeclarationPair> srcBodyPairs = compareCache.srcNodeBodyNameMap.get(key);
                BodyDeclarationPair srcBody = getExactBodyDeclarationPair(srcBodyPairs,FieldDeclaration.class);
                if(srcBody != null){
                    newFieldFlag = false;
                    if (srcBody.getBodyDeclaration().toString().hashCode() == fd.toString().hashCode()
                            && srcBody.getLocationClassString().hashCode() == prefix.hashCode()) {
                        compareCache.addToDstRemoveList(fd);
                        compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                        return 1;
                    } else {
                        // variable相同，设置为不删除
                        if (PreprocessedTempData.BODY_SAME_REMOVE != compareCache.getNodeMapValue(srcBody)) {
                            compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                        }
                        return 2;
                    }
                }else {
                    newFieldFlag = true;
                    System.err.println("[ERROR]");
                }
            }
            if(newFieldFlag){
                //new field
                compareResult.addBodiesAdded(fd, prefix);
                compareCache.addToDstRemoveList(fd);
            }
        }
        return 33;
    }

    /**
     * @param cod             内部类
     * @param prefixClassName classname到cod的name前一个为止
     * @return 1 2
     */
    public int checkTypeDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {

        if (compareCache.srcNodeBodyNameMap.containsKey(prefixClassName)) {
            List<BodyDeclarationPair> srcNodeList = compareCache.srcNodeBodyNameMap.get(prefixClassName);
            BodyDeclarationPair srcBody = getExactBodyDeclarationPair(srcNodeList,TypeDeclaration.class);
            if(srcBody != null) {
                if (srcBody.getBodyDeclaration().toString().hashCode() == cod.toString().hashCode()
                        && prefixClassName.hashCode() == srcBody.getLocationClassString().hashCode()) {
//                System.out.println(srcBody.getBodyDeclaration().toString());
//                System.out.println(cod.toString());
                    compareCache.addToDstRemoveList(cod);
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    TypeNodesTraversal.traverseTypeDeclarationSetVisited(compareCache, (TypeDeclaration) srcBody.getBodyDeclaration(), prefixClassName);
                    return 1;
                } else {
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    return 2;
                }
            }else{
                System.err.println("[ERROR]");
            }
        }
        // new class
        compareResult.addBodiesAdded(cod, prefixClassName);
        compareCache.addToDstRemoveList(cod);
        return 3;
    }

    public int checkEnumDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, EnumDeclaration ed, String prefixClassName){
        String key = prefixClassName + ed.getName().toString();
        if(compareCache.srcNodeBodyNameMap.containsKey(key)){
            List<BodyDeclarationPair> srcNodeList = compareCache.srcNodeBodyNameMap.get(key);
            BodyDeclarationPair srcBody = getExactBodyDeclarationPair(srcNodeList,EnumDeclaration.class);
            if(srcBody != null) {
                if (srcBody.getBodyDeclaration().toString().hashCode() == ed.toString().hashCode()
                        && prefixClassName.hashCode() == srcBody.getLocationClassString().hashCode()) {
                    compareCache.addToDstRemoveList(ed);
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    return 1;
                } else {
                    MyRange myRange;
                    int s, e;
                    s = compareResult.getDstCu().getLineNumber(srcBody.getBodyDeclaration().getStartPosition());
                    e = compareResult.getDstCu().getLineNumber(srcBody.getBodyDeclaration().getStartPosition() + srcBody.getBodyDeclaration().getLength() - 1);
                    myRange = new MyRange(s, e, ChangeEntityDesc.StageITreeType.DST_TREE_NODE);
                    EnumChangeEntity code = new EnumChangeEntity(srcBody, ChangeEntityDesc.StageIIOpt.OPT_CHANGE, myRange);
                    EnumDeclaration fd = (EnumDeclaration) srcBody.getBodyDeclaration();
                    PreprocessUtil.generateEnumChangeEntity(code, fd, ed);
                    if (compareResult.getPreprocessChangeEntity() == null) {
                        compareResult.setPreprocessChangeEntity(new ArrayList<>());
                    }
                    compareResult.getPreprocessChangeEntity().add(code);
                    compareCache.addToDstRemoveList(ed);
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
//                compareCache.setBodySrcNodeMap(srcBody,PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    return 2;
                }
            }else{
                System.err.println("[ERROR]");
            }
        }
        compareResult.addBodiesAdded(ed,prefixClassName);
        compareCache.addToDstRemoveList(ed);
        return 3;
    }

    /**
     * curr的节点去prev的map里check
     */
    public int checkMethodDeclarationOrInitializerInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, BodyDeclaration bd, String prefixClassName) {
        String methodNameKey = null;
        if (bd instanceof Initializer) {
            Initializer idd = (Initializer) bd;
            methodNameKey = prefixClassName;
            if (idd.modifiers().contains("static")) {
                methodNameKey += "static";
            } else {
                methodNameKey += "{";
            }
        } else if (bd instanceof MethodDeclaration) {
            MethodDeclaration md = (MethodDeclaration) bd;
            methodNameKey = prefixClassName + md.getName().toString();
            if(md.getName().toString().equals("setPoolSize")){
                System.out.print("a");
            }

        } else if (bd instanceof EnumDeclaration) {
            EnumDeclaration ed = (EnumDeclaration) bd;
            methodNameKey = prefixClassName + ed.getName().toString();
        } else {
            System.err.println("[ERR] ---------------------------");
        }

        if (compareCache.srcNodeBodyNameMap.containsKey(methodNameKey)) {
            List<BodyDeclarationPair> srcNodeList = compareCache.srcNodeBodyNameMap.get(methodNameKey);
            boolean findSame = false;
            for (BodyDeclarationPair srcBody : srcNodeList) {
                if (srcBody.hashCode() == (String.valueOf(bd.toString().hashCode()) + String.valueOf(prefixClassName.hashCode())).hashCode()) {
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    compareCache.addToDstRemoveList(bd);
                    findSame = true;
                    break;
                }
            }
            if (findSame) {
                return 1;
            } else {
                for (BodyDeclarationPair srcBody : srcNodeList) {
                    if (PreprocessedTempData.BODY_SAME_REMOVE != compareCache.getNodeMapValue(srcBody)) {
                        compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    }
                }
                return 2;
            }

        } else {
            //new method
            compareResult.addBodiesAdded(bd, prefixClassName);
            compareCache.addToDstRemoveList(bd);
            return 5;
        }
    }

}
