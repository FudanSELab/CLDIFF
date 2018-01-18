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
        traverseClassOrInterfaceDeclarationInitPrevData(cod, "");
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
                // prev node 默认删除
                preprocessingTempData.noRemovePrevNode.put(prevBd,0);
                return 1;
            } else {
                // impossible
                System.err.println("--------------------------------");
                return 2;
            }
        } else {
            NodeList<VariableDeclarator> vlist = fd.getVariables();
            for (int t = 0; t < vlist.size(); t++) {
                Node nnn = vlist.get(t);
                VariableDeclarator vd = (VariableDeclarator) nnn;
                if (preprocessingTempData.bodyMapPrevMethodOrFieldName.containsKey(prefix+vd.getName())) {
                    List<BodyDeclaration> mOverload = preprocessingTempData.bodyMapPrevMethodOrFieldName.get(prefix+vd.getName());
                    for (BodyDeclaration mItem : mOverload) {
                        // variable相同， 设置为不删除
                        preprocessingTempData.noRemovePrevNode.put(mItem, 1);
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

    private int checkCurrBodies(ClassOrInterfaceDeclaration cod) {
        String name = cod.getNameAsString();
        if (preprocessingTempData.bodyMapPrev.containsKey(name)) {
            BodyDeclaration prevNode = preprocessingTempData.bodyMapPrev.get(name);
            if (prevNode.hashCode() == cod.hashCode()) {
                // prev node 默认为0 设置删除，则其他在map的节点设置为 1 不删除（即不做操作）
                preprocessingTempData.addToRemoveList(cod);
                preprocessingTempData.noRemovePrevNode.put(prevNode,0);
                traverseClassOrInterfaceDeclarationSetVisited((ClassOrInterfaceDeclaration) prevNode);
                return 1;
            } else {
                preprocessingTempData.noRemovePrevNode.put(prevNode,1); //设置不删除
                return 2;
            }
        }
        return 2;
    }

    /**
     * curr的节点去prev的map里check
     */
    private int checkCurrBodies(BodyDeclaration bd, String bdMapPrevKey, String bdMapPrevMethodNameKey) {
        // signature 完全一摸一样的
        if (preprocessingTempData.bodyMapPrev.containsKey(bdMapPrevKey)) {
            BodyDeclaration prevNode = preprocessingTempData.bodyMapPrev.get(bdMapPrevKey);
            if (prevNode.hashCode() == bd.hashCode()) {
                // same [method / field / constructor /initializer]
                preprocessingTempData.noRemovePrevNode.put(prevNode,1);
                preprocessingTempData.addToRemoveList(bd);
                return 1;
            } else {
                preprocessingTempData.noRemovePrevNode.put(prevNode,0);
                // different [method/constructor,initializer]
                return 2;
            }
        } else {
            // signature method
            if (preprocessingTempData.bodyMapPrevMethodOrFieldName.containsKey(bdMapPrevMethodNameKey)) {
                List<BodyDeclaration> mOverload = preprocessingTempData.bodyMapPrevMethodOrFieldName.get(bdMapPrevMethodNameKey);
                // 可能为修改签名之后的方法，也可能为新增的方法
                for (BodyDeclaration mItem : mOverload) {
                    preprocessingTempData.noRemovePrevNode.put(mItem, 1);
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
        TypeDeclaration mTypeCurr = cuCurr.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;
        traverseClassOrInterfaceDeclarationCmpCurr(cod, "");

        for (Entry<BodyDeclaration, Integer> item : preprocessingTempData.noRemovePrevNode.entrySet()) {
            if (item.getValue() == 1) {
                continue;
            }
            BodyDeclaration bd = item.getKey();
            this.preprocessingData.addBodiesDeleted(bd);
            preprocessingTempData.addToRemoveList(bd);
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
     *
     * @param cod class name
     */
    public void traverseClassOrInterfaceDeclarationCmpCurr(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                int status = checkCurrBodies(cod2);
                if (status != 1) {
                    traverseClassOrInterfaceDeclarationCmpCurr((ClassOrInterfaceDeclaration) node, prefixClassName + cod2.getNameAsString() + ".");
                }
                continue;
            }
            if (node instanceof AnnotationDeclaration) {
                continue;
            }
            if (node instanceof InitializerDeclaration) {
                InitializerDeclaration idd = (InitializerDeclaration) node;
                String str;
                if (idd.isStatic()) {
                    str = "static";
                } else {
                    str = "{";
                }
                checkCurrBodies((BodyDeclaration) node,str, str);
                continue;
            }
            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) node;
                String key = prefixClassName + cd.getDeclarationAsString();
                String key2 = prefixClassName + cd.getNameAsString();
                checkCurrBodies(cd,key, key2);
                continue;
            }
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                String key = prefixClassName + md.getDeclarationAsString();
                String key2 = prefixClassName + md.getNameAsString();
                checkCurrBodies(md,key, key2);
                continue;
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkCurrBodies(fd,prefixClassName);
//                if("private static final SparseArray<CapabilityInfo> sAvailableCapabilityInfos = new SparseArray<CapabilityInfo>();".equals(fd.toString())){
            }
        }
    }

    /**
     * prev + curr
     *
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

    public void traverseClassOrInterfaceDeclarationSetVisited(ClassOrInterfaceDeclaration cod) {
        NodeList tmpList = cod.getMembers();
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            Node n = tmpList.get(m);
            this.preprocessingTempData.noRemovePrevNode.put((BodyDeclaration) n, 1);
            if (n instanceof ClassOrInterfaceDeclaration) {
                traverseClassOrInterfaceDeclarationSetVisited((ClassOrInterfaceDeclaration) n);
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
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                traverseClassOrInterfaceDeclarationInitPrevData(cod2, prefixClassName + cod2.getNameAsString() + ".");
                preprocessingTempData.bodyMapPrev.put(cod2.getNameAsString(), cod2);
                continue;
            }
            if (node instanceof AnnotationDeclaration) {
                preprocessingTempData.addToRemoveList((BodyDeclaration) node);
            }
            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) node;
                preprocessingTempData.noRemovePrevNode.put(cd, 0);
                preprocessingTempData.bodyMapPrev.put(prefixClassName + cd.getDeclarationAsString(), cd);
                preprocessingTempData.addToBodyMapPrevMethodNameOrFieldName(prefixClassName + cd.getNameAsString(), cd);
            }
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                preprocessingTempData.noRemovePrevNode.put(md, 0);
                preprocessingTempData.bodyMapPrev.put(prefixClassName + md.getDeclarationAsString(), md);
                preprocessingTempData.addToBodyMapPrevMethodNameOrFieldName(prefixClassName + md.getNameAsString(), md);
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                preprocessingTempData.noRemovePrevNode.put(fd, 0);
                preprocessingTempData.bodyMapPrev.put(prefixClassName + fd.toString(), fd);
                NodeList<VariableDeclarator> fdc = fd.getVariables();
                for (int iii = 0; iii < fdc.size(); iii++) {
                    Node nod = fdc.get(iii);
                    VariableDeclarator vd = (VariableDeclarator) nod;
                    preprocessingTempData.addToBodyMapPrevMethodNameOrFieldName(prefixClassName + vd.getName(), fd);
                }
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
                preprocessingTempData.bodyMapPrev.put(iddStr, idd);
            }
        }
    }


    public void test(String a) {
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(a);
        cuPrev.removeComment();
        cuPrev.removePackageDeclaration();
        TypeDeclaration mTypeCurr = cuPrev.getType(0);
        traverseClassOrInterfaceDeclarationRemoveComment((ClassOrInterfaceDeclaration) mTypeCurr);
        System.out.print(cuPrev.toString());
    }

}
