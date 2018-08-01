package edu.fdu.se.base.preprocessingfile;

import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedTempData;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class TypeNodesTraversal {

    private DstBodyCheck dstBodyCheck;

    public TypeNodesTraversal(){
        dstBodyCheck = new DstBodyCheck();
    }
    /**
     * curr
     * @param cod             class 节点
     * @param prefixClassName class 节点为止的prefix ， root节点的class prefix 为classname
     */
    public void traverseDstTypeDeclarationCompareSrc(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        compareResult.addTypeDeclaration(prefixClassName, cod, cod.getName().toString());
        int status = dstBodyCheck.checkTypeDeclarationInDst(compareResult, compareCache, cod, prefixClassName);
        if(status == 1|| status==3){
            return;
        }
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration node = nodeList.get(i);
            if (node instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) node;
                traverseDstTypeDeclarationCompareSrc(compareResult, compareCache, cod2, prefixClassName + cod2.getName().toString() + ".");
            } else if (node instanceof Initializer || node instanceof MethodDeclaration) {
                dstBodyCheck.checkMethodDeclarationOrInitializerInDst(compareResult, compareCache, node, prefixClassName);
            } else if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                dstBodyCheck.checkFieldDeclarationInDst(compareResult, compareCache, fd, prefixClassName);
            } else if (node instanceof AnnotationTypeDeclaration) {
                compareCache.addToDstRemoveList(node);
            } else if (node instanceof EnumDeclaration) {
                EnumDeclaration ed = (EnumDeclaration) node;
                dstBodyCheck.checkEnumDeclarationInDst(compareResult,compareCache,ed,prefixClassName);
            } else {
                System.err.println("[ERR]Error:" + node.getClass().getSimpleName());
            }
        }

    }

    /**
     * 设置该cod下的孩子节点为访问，因为father已经被remove了，所以不需要remove
     *
     * @param cod             该节点
     * @param prefixClassName 该节点为止的preix ClassName
     */
    public static void traverseTypeDeclarationSetVisited(PreprocessedTempData compareCache, TypeDeclaration cod, String prefixClassName) {
        List<BodyDeclaration> tmpList = cod.bodyDeclarations();
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            BodyDeclaration n = tmpList.get(m);
            if (n instanceof TypeDeclaration) {
                TypeDeclaration next = (TypeDeclaration) n;
                BodyDeclarationPair bdp = new BodyDeclarationPair(n, prefixClassName+next.getName().toString()+".");
                if (compareCache.srcNodeVisitingMap.containsKey(bdp)) {
                    compareCache.setBodySrcNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
                }
                traverseTypeDeclarationSetVisited(compareCache, next, prefixClassName + next.getName().toString()+".");
            }else {
                BodyDeclarationPair bdp = new BodyDeclarationPair(n, prefixClassName);
                if (compareCache.srcNodeVisitingMap.containsKey(bdp)) {
                    compareCache.setBodySrcNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
                }
            }
        }
    }


    public void traverseSrcTypeDeclarationInit(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration typeDeclaration, String prefixClassName) {
        List<BodyDeclaration> nodeList = typeDeclaration.bodyDeclarations();
        compareResult.addTypeDeclaration(prefixClassName, typeDeclaration, typeDeclaration.getName().toString());
        BodyDeclarationPair typeBodyDeclarationPair = new BodyDeclarationPair(typeDeclaration, prefixClassName);
        compareCache.addToMapBodyName(typeBodyDeclarationPair, prefixClassName);
        compareCache.initBodySrcNodeMap(typeBodyDeclarationPair);
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
                    compareResult.prevFieldNames.add(vd.getName().toString());
                    compareResult.prevCurrFieldNames.add(vd.getName().toString());
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


    public void traverseSrcTypeDeclaration2Keys(PreprocessedData compareResult, PreprocessedTempData compareCache, TypeDeclaration typeDeclaration, String prefixClassName) {
        List<BodyDeclaration> nodeList = typeDeclaration.bodyDeclarations();
        BodyDeclarationPair typeBodyDeclarationPair = new BodyDeclarationPair(typeDeclaration, prefixClassName);
        compareResult.entityContainer.addKey(typeBodyDeclarationPair);
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration bodyDeclaration = nodeList.get(i);
            if (bodyDeclaration instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) bodyDeclaration;
                String subCodName = prefixClassName + cod2.getName().toString() + ".";
                traverseSrcTypeDeclarationInit(compareResult, compareCache, cod2, subCodName);
                continue;
            }
            BodyDeclarationPair bdp = new BodyDeclarationPair(bodyDeclaration, prefixClassName);
            compareResult.entityContainer.addKey(bdp);
        }
    }




}
