package edu.fdu.se.astdiff.preprocessingfile;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.javaparser.JavaParserFactory;

/**
 * 两个文件 预处理
 * 删除一摸一样的方法
 * 删除一摸一样的field
 * 删除一摸一样的内部类
 * 删除add method
 * 删除remove method
 * 删除内部类中的add / remove method
 * 保留 remove field 和add field 因为需要识别是否是refactor
 */
public class PreprocessingSDKClass {

    public static void main(String args[]) {
        new PreprocessingSDKClass().compareTwoFile("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java",
                "D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/accessibilityservice/AccessibilityService.java", "test_file");
//		new PreprocessingSDKClass().test("D:/test.java");
    }

    public PreprocessingSDKClass() {
        preprocessingData = new PreprocessingData();
        preprocessingTempData = new PreprocessingTempData();
    }

    private PreprocessingData preprocessingData;
    private PreprocessingTempData preprocessingTempData;


    /**
     * prev的method，field 都到map里做标记
     *
     * @param cu compilationUnit
     */
    private void initPreprocessingDataFromPrev(CompilationUnit cu) {
        TypeDeclaration mTypePrev = cu.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypePrev;
        traverseClassOrInterfaceDeclarationInitPrevData(cod, cod.getNameAsString()+".");
    }

    /**
     * visited
     *
     */
    private int checkCurrBodies(FieldDeclaration fd, String prefix) {
        if (preprocessingTempData.bodyMapPrev.containsKey(prefix+fd.toString())) {
            BodyDeclaration prevBd = preprocessingTempData.bodyMapPrev.get(prefix+fd.toString());
            if (prevBd.hashCode() == fd.hashCode()) {
                preprocessingTempData.addToRemoveList(fd);
                preprocessingTempData.setBodyPrevNodeMap(prevBd,prefix,PreprocessingTempData.BODY_SAME_REMOVE);
                return 1;
            } else {
                // impossible
                System.err.println("--------------------------------");
                return 2;
            }
        } else {
            for(VariableDeclarator vd:fd.getVariables()){
                if (preprocessingTempData.bodyMapPrevMethodOrFieldName.containsKey(prefix+vd.getName())) {
                    List<BodyDeclaration> mOverload = preprocessingTempData.bodyMapPrevMethodOrFieldName.get(prefix+vd.getName());
                    for (BodyDeclaration mItem : mOverload) {
                        // variable相同， 设置为不删除
                        String hashCode = String.valueOf(mItem.hashCode()) + String.valueOf(prefix.hashCode());
                        if(PreprocessingTempData.BODY_SAME_REMOVE != preprocessingTempData.getNodeMapValue(hashCode.hashCode())){
                            preprocessingTempData.setBodyPrevNodeMap(mItem,prefix,PreprocessingTempData.BODY_DIFFERENT_RETAIN);
                        }
                    }
                } else {
                    //new field
                    this.preprocessingData.addBodiesAdded(fd);
                    preprocessingTempData.addToRemoveList(fd);
                }
            }
        }
        return 33;
    }

    /**
     *
     * @param cod 内部类
     * @param prefixClassName classname到cod的name前一个为止
     * @return  1 2
     */
    private int checkCurrBodies(ClassOrInterfaceDeclaration cod,String prefixClassName) {
        String key = prefixClassName + cod.getNameAsString()+".";
        if (preprocessingTempData.bodyMapPrev.containsKey(key)) {
            BodyDeclaration prevNode = preprocessingTempData.bodyMapPrev.get(key);
            if (prevNode.hashCode() == cod.hashCode()) {
                preprocessingTempData.addToRemoveList(cod);
                preprocessingTempData.setBodyPrevNodeMap(prevNode,prefixClassName,PreprocessingTempData.BODY_SAME_REMOVE);
                traverseClassOrInterfaceDeclarationSetVisited((ClassOrInterfaceDeclaration) prevNode,prefixClassName);
                return 1;
            } else {
                preprocessingTempData.setBodyPrevNodeMap(prevNode,prefixClassName,PreprocessingTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        }
        return 2;
    }

    /**
     * curr的节点去prev的map里check
     */
    private int checkCurrBodies(BodyDeclaration bd, String prefixClassName) {
        // signature 完全一摸一样的
        String bdMapPrevKey = null;
        String bdMapPrevMethodNameKey = null;
        InitializerDeclaration idd;
        if(bd instanceof InitializerDeclaration){
            idd = (InitializerDeclaration) bd;
            if(idd.isStatic()){
                bdMapPrevKey = "static";
            }else{
                bdMapPrevKey = "{";
            }
            bdMapPrevMethodNameKey = bdMapPrevKey;
        }else{
            if(bd instanceof MethodDeclaration){
                MethodDeclaration md = (MethodDeclaration) bd;
                bdMapPrevKey = prefixClassName+md.getDeclarationAsString();
                bdMapPrevMethodNameKey = prefixClassName+md.getNameAsString();
            }else if(bd instanceof ConstructorDeclaration){
                ConstructorDeclaration cd = (ConstructorDeclaration) bd;
                bdMapPrevKey = prefixClassName+cd.getDeclarationAsString();
                bdMapPrevMethodNameKey = prefixClassName+cd.getNameAsString();
            }
        }

        if (preprocessingTempData.bodyMapPrev.containsKey(bdMapPrevKey)) {
            BodyDeclaration prevNode = preprocessingTempData.bodyMapPrev.get(bdMapPrevKey);
            if (prevNode.hashCode() == bd.hashCode()) {
                preprocessingTempData.setBodyPrevNodeMap(prevNode,prefixClassName,PreprocessingTempData.BODY_SAME_REMOVE);
                preprocessingTempData.addToRemoveList(bd);
                return 1;
            } else {
                preprocessingTempData.setBodyPrevNodeMap(prevNode,prefixClassName,PreprocessingTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        } else {
            // signature method
            if (preprocessingTempData.bodyMapPrevMethodOrFieldName.containsKey(bdMapPrevMethodNameKey)) {
                List<BodyDeclaration> mOverload = preprocessingTempData.bodyMapPrevMethodOrFieldName.get(bdMapPrevMethodNameKey);
                // 可能为修改签名之后的方法，也可能为新增的方法
                for (BodyDeclaration mItem : mOverload) {
                    String hashStr = String.valueOf(mItem.hashCode()) + String.valueOf(prefixClassName.hashCode());
                    if(PreprocessingTempData.BODY_SAME_REMOVE != preprocessingTempData.getNodeMapValue(hashStr.hashCode())) {
                        preprocessingTempData.setBodyPrevNodeMap(mItem, prefixClassName, PreprocessingTempData.BODY_DIFFERENT_RETAIN);
                    }
                }
                return 4;
            } else {
                //new method
                this.preprocessingData.addBodiesAdded(bd);
                preprocessingTempData.addToRemoveList(bd);
                return 5;
            }

        }
    }

    private void removeCommentss(Node n) {
        List<Comment> mList = n.getAllContainedComments();
        List<Comment> mList2 = n.getOrphanComments();
        for (Comment m : mList) {
            m.remove();
        }
        for (Comment m : mList2) {
            m.remove();
        }
    }

    private void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
        cu.removeComment();
        cu.removePackageDeclaration();
        NodeList imports = cu.getImports();
        for (int i = imports.size() - 1; i >= 0; i--) {
            Node n = imports.get(i);
            n.remove();
        }
        assert cu.getTypes() != null;
        assert cu.getTypes().size() == 1;
        TypeDeclaration mTypeCurr = cu.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;
        traverseClassOrInterfaceDeclarationRemoveComment(cod);
    }


    public PreprocessingSDKClass compareTwoFile(String prev, String curr, String outputDirName) {
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prev);
        CompilationUnit cuCurr = JavaParserFactory.getCompilationUnit(curr);
        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
        File dirFilePrev = new File(rootOutPath + "/prev/" + outputDirName);
        File dirFileCurr = new File(rootOutPath + "/curr/" + outputDirName);
        if (!dirFilePrev.exists()) {
            dirFilePrev.mkdirs();
        }
        if (!dirFileCurr.exists()) {
            dirFileCurr.mkdirs();
        }

        FileWriter.writeInAll(dirFilePrev.getAbsolutePath() + "/file_before_trim.java", cuPrev.toString());
        FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_before_trim.java", cuCurr.toString());
        removeAllCommentsOfCompilationUnit(cuPrev);
        removeAllCommentsOfCompilationUnit(cuCurr);
        initPreprocessingDataFromPrev(cuPrev);
        preprocessingTempData.removeRemovalList();
        ClassOrInterfaceDeclaration codMain = (ClassOrInterfaceDeclaration)cuCurr.getType(0);
        traverseClassOrInterfaceDeclarationCmpCurr((ClassOrInterfaceDeclaration) cuCurr.getType(0), codMain.getNameAsString()+".");
        preprocessingTempData.removeRemovalList();
        for (Entry<Integer, BodyDeclaration> item : preprocessingTempData.prevNodeVisitingMap2.entrySet()) {
            Integer key = item.getKey();
            BodyDeclaration bd = item.getValue();
            Integer status = this.preprocessingTempData.prevNodeVisitingMap1.get(key);
            switch(status){
                case PreprocessingTempData.BODY_DIFFERENT_RETAIN:
                case PreprocessingTempData.BODY_FATHERNODE_REMOVE:
                    break;
                case PreprocessingTempData.BODY_INITIALIZED_VALUE:
                    this.preprocessingData.addBodiesDeleted(bd);
                    preprocessingTempData.addToRemoveList(bd);
                    break;
                case PreprocessingTempData.BODY_SAME_REMOVE:
                    preprocessingTempData.addToRemoveList(bd);
                    break;
            }
        }
        preprocessingTempData.removeRemovalList();

        FileWriter.writeInAll(dirFilePrev.getAbsolutePath() + "/file_after_trim.java", cuPrev.toString());
        FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_after_trim.java", cuCurr.toString());
        this.preprocessingData.setCurrentCu(cuCurr);
        this.preprocessingData.setPreviousCu(cuPrev);
        this.preprocessingData.printAddedRemovedBodies();
        return this;

    }

    public PreprocessingData getPreprocessingData() {
        return preprocessingData;
    }

    /**
     * curr
     * @param cod class 节点
     * @param prefixClassName class 节点为止的prefix ， root节点的class prefix 为“”
     */
    public void traverseClassOrInterfaceDeclarationCmpCurr(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                int status = checkCurrBodies(cod2,prefixClassName);
                if (status != 1) {
                    traverseClassOrInterfaceDeclarationCmpCurr(cod2, prefixClassName + cod2.getNameAsString() + ".");
                }
                continue;
            }
            if (node instanceof AnnotationDeclaration) {
                continue;
            }
            if (node instanceof InitializerDeclaration||node instanceof ConstructorDeclaration
                    ||node instanceof MethodDeclaration) {
                BodyDeclaration bd = (BodyDeclaration) node;
                checkCurrBodies(bd,prefixClassName);
                continue;
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkCurrBodies(fd,prefixClassName);

            }
        }
    }

    /**
     * prev + curr
     * @param cod class name
     */
    public void traverseClassOrInterfaceDeclarationRemoveComment(ClassOrInterfaceDeclaration cod) {
        cod.removeJavaDocComment();
        cod.removeComment();
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            node.removeComment();
            removeCommentss(node);
            if (node instanceof ClassOrInterfaceDeclaration) {
                traverseClassOrInterfaceDeclarationRemoveComment((ClassOrInterfaceDeclaration) node);
            }
            if (node instanceof AnnotationDeclaration) {
                preprocessingTempData.addToRemoveList((BodyDeclaration) node);
            }
            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) node;
                cd.removeJavaDocComment();
            }
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                md.removeJavaDocComment();
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                fd.removeJavaDocComment();
            }
            if (node instanceof InitializerDeclaration) {
                InitializerDeclaration idd = (InitializerDeclaration) node;
                idd.removeJavaDocComment();
            }
        }
    }

    /**
     * 设置该cod下的孩子节点为访问，因为father已经被remove了，所以不需要remove
     * @param cod 该节点
     * @param prefixClassName  该节点为止的preix ClassName
     */
    public void traverseClassOrInterfaceDeclarationSetVisited(ClassOrInterfaceDeclaration cod,String prefixClassName) {
        NodeList tmpList = cod.getMembers();
        String childrenClassPrefix = prefixClassName + cod.getNameAsString()+".";
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            Node n = tmpList.get(m);
            this.preprocessingTempData.setBodyPrevNodeMap((BodyDeclaration)n,childrenClassPrefix,PreprocessingTempData.BODY_FATHERNODE_REMOVE);
            if (n instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration next = (ClassOrInterfaceDeclaration) n;
                traverseClassOrInterfaceDeclarationSetVisited(next,childrenClassPrefix);
            }
        }
    }

    /**
     * prev
     *
     * @param cod             classname
     * @param prefixClassName prefix name
     */
    public void traverseClassOrInterfaceDeclarationInitPrevData(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if(node instanceof BodyDeclaration){
                BodyDeclaration bd = (BodyDeclaration)node;
                if(bd instanceof AnnotationDeclaration){
                    //todo index 5 AccountManager
                    continue;
                }
                preprocessingTempData.initBodyPrevNodeMap(bd,prefixClassName);

                if (node instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                    String subCodName = prefixClassName + cod2.getNameAsString() +".";
                    traverseClassOrInterfaceDeclarationInitPrevData(cod2, subCodName);
                    preprocessingTempData.addToMapBodyDeclaration(cod2,subCodName);
                    continue;
                }
                if (node instanceof ConstructorDeclaration) {
                    ConstructorDeclaration cd = (ConstructorDeclaration) node;
                    preprocessingTempData.addToMapBodyDeclaration(cd,prefixClassName+cd.getDeclarationAsString());
                    preprocessingTempData.addToMapBodyName(cd,prefixClassName + cd.getNameAsString());
                    continue;
                }
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration md = (MethodDeclaration) node;
                    preprocessingTempData.addToMapBodyDeclaration(md,prefixClassName+md.getDeclarationAsString());
                    preprocessingTempData.addToMapBodyName(md,prefixClassName + md.getNameAsString());
                    continue;
                }
                if (node instanceof FieldDeclaration) {
                    FieldDeclaration fd = (FieldDeclaration) node;
                    preprocessingTempData.addToMapBodyDeclaration(fd,prefixClassName + fd.toString());

                    for(VariableDeclarator vd:fd.getVariables()){
                        preprocessingTempData.addToMapBodyName(fd,prefixClassName + vd.getName());
                    }
                    continue;
                }
                if (node instanceof InitializerDeclaration) {
                    //内部类不会有static
                    InitializerDeclaration idd = (InitializerDeclaration) node;
                    String iddStr;
                    if (idd.isStatic()) {
                        iddStr = "static";
                    } else {
                        iddStr = "{";
                    }
                    preprocessingTempData.addToMapBodyDeclaration(idd,prefixClassName+iddStr);
                    preprocessingTempData.addToMapBodyName(idd,prefixClassName + iddStr);
                }
            }

        }
    }
}
