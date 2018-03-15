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
    public void traverseTypeDeclarationCompareCurr(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        compareResult.addTypeDeclaration(prefixClassName, cod);
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration node = nodeList.get(i);
            if (node instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) node;
                int status = checkCurrBodies(compareResult, compareCache, cod2, prefixClassName);
                if (status != 1) {
                    traverseTypeDeclarationCompareCurr(compareResult, compareCache, cod2, prefixClassName + cod2.getName().toString() + ".");
                }
            } else if (node instanceof Initializer || node instanceof MethodDeclaration) {
                checkCurrBodies(compareResult, compareCache, node, prefixClassName);
            } else if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkCurrBodies(compareResult, compareCache, fd, prefixClassName);
            } else if (node instanceof AnnotationTypeDeclaration) {
                compareCache.addToRemoveList(node);
            } else {
                System.err.println("ERROR:" + node.getClass().getSimpleName());
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
        String childrenClassPrefix = prefixClassName + cod.getName().toString() + ".";
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            BodyDeclaration n = tmpList.get(m);
            BodyDeclarationPair bdp = new BodyDeclarationPair(n, childrenClassPrefix);
            if (compareCache.prevNodeVisitingMap.containsKey(bdp)) {
                compareCache.setBodyPrevNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
            }
            if (n instanceof TypeDeclaration) {
                TypeDeclaration next = (TypeDeclaration) n;
                traverseTypeDeclarationSetVisited(compareCache, next, childrenClassPrefix);
            }
        }
    }

    /**
     * prev
     *
     * @param cod             classname
     * @param prefixClassName prefix name
     */
    public void traverseTypeDeclarationInitPrevData(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        compareResult.addTypeDeclaration(prefixClassName, cod);
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration bodyDeclaration = nodeList.get(i);
            BodyDeclarationPair bdp = new BodyDeclarationPair(bodyDeclaration, prefixClassName);
            compareCache.initBodyPrevNodeMap(bdp);
            if (bodyDeclaration instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) bodyDeclaration;
                String subCodName = prefixClassName + cod2.getName().toString() + ".";
                compareCache.addToMapBodyName(bdp, subCodName);
                traverseTypeDeclarationInitPrevData(compareResult, compareCache, cod2, subCodName);
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
                    System.out.println("staticstatic--------------------");
                } else {
                    iddStr = "{";
                }
                compareCache.addToMapBodyName(bdp, prefixClassName + iddStr);
                continue;
            }
            if (bodyDeclaration instanceof AnnotationTypeDeclaration) {
                compareCache.addToRemoveList(bodyDeclaration);
            }
        }

    }

    /**
     * visited
     */
    private int checkCurrBodies(PreprocessedData compareResult, PreprocessedTempData compareCache, FieldDeclaration fd, String prefix) {
        List<VariableDeclarationFragment> vdList = fd.fragments();
        for (VariableDeclarationFragment vd : vdList) {
            String key = fd + vd.getName().toString();
            if (compareCache.prevNodeBodyNameMap.containsKey(key)) {
                List<BodyDeclarationPair> prevBodyPairs = compareCache.prevNodeBodyNameMap.get(key);
                assert prevBodyPairs.size() <= 1;
                BodyDeclarationPair prevBody = prevBodyPairs.get(0);
                if (prevBody.getBodyDeclaration().toString().hashCode() == fd.toString().hashCode()
                        && prevBody.getLocationClassString().hashCode() == prefix.hashCode()) {
                    compareCache.addToRemoveList(fd);
                    compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    return 1;
                } else {
                    // variable相同， 设置为不删除
                    if (PreprocessedTempData.BODY_SAME_REMOVE != compareCache.getNodeMapValue(prevBody)) {
                        compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    }
                    return 2;
                }
            } else {
                //new field
                compareResult.addBodiesAdded(fd, prefix);
                compareCache.addToRemoveList(fd);
            }
        }
        return 33;
    }

    /**
     * @param cod             内部类
     * @param prefixClassName classname到cod的name前一个为止
     * @return 1 2
     */
    private int checkCurrBodies(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        String key = prefixClassName + cod.getName().toString() + ".";
        if (compareCache.prevNodeBodyNameMap.containsKey(key)) {
            List<BodyDeclarationPair> prevNodeList = compareCache.prevNodeBodyNameMap.get(key);
            assert prevNodeList.size() <= 1;
            BodyDeclarationPair prevBody = prevNodeList.get(0);
            if (prevBody.getBodyDeclaration().toString().hashCode() == cod.toString().hashCode()
                    && prefixClassName.hashCode() == prevBody.getLocationClassString().hashCode()) {
                compareCache.addToRemoveList(cod);
                compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_SAME_REMOVE);
                traverseTypeDeclarationSetVisited(compareCache, (TypeDeclaration) prevBody.getBodyDeclaration(), prefixClassName);
                return 1;
            } else {
                compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        }
        // new class
        compareResult.addBodiesAdded(cod, prefixClassName);
        compareCache.addToRemoveList(cod);
        return 3;
    }

    /**
     * curr的节点去prev的map里check
     */
    private int checkCurrBodies(PreprocessedData compareResult, PreprocessedTempData compareCache, BodyDeclaration bd, String prefixClassName) {
        String methodNameKey = null;
        if (bd instanceof Initializer) {
            Initializer idd = (Initializer) bd;
            methodNameKey = prefixClassName;
            if (idd.modifiers().contains("static")) {
                methodNameKey += "static";
            } else {
                methodNameKey += "{";
            }
        } else {
            if (bd instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bd;
                methodNameKey = prefixClassName + md.getName().toString();
            } else {
                System.err.println("---------------------------");
            }
        }

        if (compareCache.prevNodeBodyNameMap.containsKey(methodNameKey)) {
            List<BodyDeclarationPair> prevNodeList = compareCache.prevNodeBodyNameMap.get(methodNameKey);
            boolean findSame = false;
            for (BodyDeclarationPair prevBody : prevNodeList) {
                if (prevBody.hashCode() == (String.valueOf(bd.toString().hashCode()) + String.valueOf(prefixClassName.hashCode())).hashCode()) {
                    compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_SAME_REMOVE);
                    compareCache.addToRemoveList(bd);
                    findSame = true;
                    break;
                }
            }
            if (findSame) {
                return 1;
            } else {
                for (BodyDeclarationPair prevBody : prevNodeList) {
                    if (PreprocessedTempData.BODY_SAME_REMOVE != compareCache.getNodeMapValue(prevBody)) {
                        compareCache.setBodyPrevNodeMap(prevBody, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    }
                }
                return 2;
            }

        } else {
            //new method
            compareResult.addBodiesAdded(bd, prefixClassName);
            compareCache.addToRemoveList(bd);
            return 5;
        }
    }
}
