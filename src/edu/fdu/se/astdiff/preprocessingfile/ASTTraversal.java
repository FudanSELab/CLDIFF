package edu.fdu.se.astdiff.preprocessingfile;

import org.eclipse.jdt.core.dom.*;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class ASTTraversal {
    /**
     * curr
     *
     * @param cod             class 节点
     * @param prefixClassName class 节点为止的prefix ， root节点的class prefix 为classname
     */
    public void traverseDstTypeDeclarationCompareSrc(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        compareResult.addTypeDeclaration(prefixClassName, cod, cod.getName().toString());
        int status = checkTypeDeclarationInDst(compareResult, compareCache, cod, prefixClassName);
        if(status == 1){
            return;
        }
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration node = nodeList.get(i);
            if (node instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) node;
                traverseDstTypeDeclarationCompareSrc(compareResult, compareCache, cod2, prefixClassName + cod2.getName().toString() + ".");
            } else if (node instanceof Initializer || node instanceof MethodDeclaration) {
                checkMethodDeclarationOrInitializerInDst(compareResult, compareCache, node, prefixClassName);
            } else if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkFieldDeclarationInDst(compareResult, compareCache, fd, prefixClassName);
            } else if (node instanceof AnnotationTypeDeclaration) {
                compareCache.addToDstRemoveList(node);
            } else if (node instanceof EnumDeclaration) {
                EnumDeclaration ed = (EnumDeclaration) node;
                checkEnumDeclarationInDst(compareResult,compareCache,ed,prefixClassName);
            } else {
                System.err.println("Error:" + node.getClass().getSimpleName());
            }
        }
    }

    /**
     * 设置该cod下的孩子节点为访问，因为father已经被remove了，所以不需要remove
     *
     * @param cod             该节点
     * @param prefixClassName 该节点为止的preix ClassName
     */
    public void traverseTypeDeclarationSetVisited(PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        List<BodyDeclaration> tmpList = cod.bodyDeclarations();
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            BodyDeclaration n = tmpList.get(m);
            BodyDeclarationPair bdp = new BodyDeclarationPair(n, prefixClassName);
            if (compareCache.srcNodeVisitingMap.containsKey(bdp)) {
                compareCache.setBodySrcNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
            }
            if (n instanceof TypeDeclaration) {
                TypeDeclaration next = (TypeDeclaration) n;
                traverseTypeDeclarationSetVisited(compareCache, next, prefixClassName + next.getName().toString()+".");
            }
        }
    }


    public void traverseSrcTypeDeclarationInit(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration typeDeclaration, String prefixClassName) {
        List<BodyDeclaration> nodeList = typeDeclaration.bodyDeclarations();
        compareResult.addTypeDeclaration(prefixClassName, typeDeclaration, typeDeclaration.getName().toString());
        BodyDeclarationPair typeBodyDeclarationPair = new BodyDeclarationPair(typeDeclaration, prefixClassName);
        compareCache.addToMapBodyName(typeBodyDeclarationPair, prefixClassName);
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration bodyDeclaration = nodeList.get(i);
            if (bodyDeclaration instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) bodyDeclaration;
                String subCodName = prefixClassName + cod2.getName().toString() + ".";
                traverseSrcTypeDeclarationInit(compareResult, compareCache, cod2, subCodName);
                continue;
            }
            BodyDeclarationPair bdp = new BodyDeclarationPair(bodyDeclaration, prefixClassName);
            compareCache.initBodySrcNodeMap(bdp);
            if (bodyDeclaration instanceof EnumDeclaration) {
                EnumDeclaration ed = (EnumDeclaration) bodyDeclaration;
                compareCache.addToMapBodyName(bdp, prefixClassName + ed.getName().toString());
                continue;
            }
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bodyDeclaration;
                compareCache.addToMapBodyName(bdp, prefixClassName + md.getName().toString());
                continue;
            }
            if (bodyDeclaration instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) bodyDeclaration;
                List<VariableDeclarationFragment> mmList = fd.fragments();
                for (VariableDeclarationFragment vd : mmList) {
                    compareCache.addToMapBodyName(bdp, prefixClassName + vd.getName().toString());
                }
                continue;
            }
            if (bodyDeclaration instanceof Initializer) {
                //内部类不会有static
                Initializer idd = (Initializer) bodyDeclaration;
                String iddStr;
                if (idd.modifiers().contains("static")) {
                    iddStr = "static";
                } else {
                    iddStr = "{";
                }
                compareCache.addToMapBodyName(bdp, prefixClassName + iddStr);
                continue;
            }
            if (bodyDeclaration instanceof AnnotationTypeDeclaration) {
                compareCache.addToSrcRemoveList(bodyDeclaration);
            }

        }
    }

    /**
     * visited
     */
    private int checkFieldDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, FieldDeclaration fd, String prefix) {
        List<VariableDeclarationFragment> vdList = fd.fragments();
        for (VariableDeclarationFragment vd : vdList) {
            String key = prefix + vd.getName().toString();
            if (compareCache.srcNodeBodyNameMap.containsKey(key)) {
                List<BodyDeclarationPair> srcBodyPairs = compareCache.srcNodeBodyNameMap.get(key);
                assert srcBodyPairs.size() <= 1;
                BodyDeclarationPair srcBody = srcBodyPairs.get(0);
                if (srcBody.getBodyDeclaration().toString().hashCode() == fd.toString().hashCode()
                        && srcBody.getLocationClassString().hashCode() == prefix.hashCode()) {
                    compareCache.addToDstRemoveList(fd);
                    compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    return 1;
                } else {
                    // variable相同， 设置为不删除
                    if (PreprocessedTempData.BODY_SAME_REMOVE != compareCache.getNodeMapValue(srcBody)) {
                        compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    }
                    return 2;
                }
            } else {
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
    private int checkTypeDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        if (compareCache.srcNodeBodyNameMap.containsKey(prefixClassName)) {
            List<BodyDeclarationPair> srcNodeList = compareCache.srcNodeBodyNameMap.get(prefixClassName);
            assert srcNodeList.size() <= 1;
            BodyDeclarationPair srcBody = srcNodeList.get(0);
            if (srcBody.getBodyDeclaration().toString().hashCode() == cod.toString().hashCode()
                    && prefixClassName.hashCode() == srcBody.getLocationClassString().hashCode()) {
                compareCache.addToDstRemoveList(cod);
                compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                traverseTypeDeclarationSetVisited(compareCache, (TypeDeclaration) srcBody.getBodyDeclaration(), prefixClassName);
                return 1;
            } else {
                compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        }
        // new class
        compareResult.addBodiesAdded(cod, prefixClassName);
        compareCache.addToDstRemoveList(cod);
        return 3;
    }

    private int checkEnumDeclarationInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, EnumDeclaration ed, String prefixClassName){
        String key = prefixClassName + ed.getName().toString();
        if(compareCache.srcNodeBodyNameMap.containsKey(key)){
            List<BodyDeclarationPair> srcNodeList = compareCache.srcNodeBodyNameMap.get(key);
            assert srcNodeList.size()<=1;
            BodyDeclarationPair srcBody = srcNodeList.get(0);
            if(srcBody.getBodyDeclaration().toString().hashCode()== ed.toString().hashCode()
                    && prefixClassName.hashCode() == srcBody.getLocationClassString().hashCode()){
                compareCache.addToDstRemoveList(ed);
                compareCache.setBodySrcNodeMap(srcBody, PreprocessedTempData.BODY_SAME_REMOVE);
                return 1;
            }else{
                compareCache.setBodySrcNodeMap(srcBody,PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        }
        compareResult.addBodiesAdded(ed,prefixClassName);
        compareCache.addToDstRemoveList(ed);
        return 3;
    }

    /**
     * curr的节点去prev的map里check
     */
    private int checkMethodDeclarationOrInitializerInDst(PreprocessedData compareResult, PreprocessedTempData compareCache, BodyDeclaration bd, String prefixClassName) {
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
        } else if (bd instanceof EnumDeclaration) {
            EnumDeclaration ed = (EnumDeclaration) bd;
            methodNameKey = prefixClassName + ed.getName().toString();
        } else {
            System.err.println("---------------------------");
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
