package edu.fdu.se.astdiff.preprocessingfile;

import java.io.File;
import java.util.ArrayList;
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
import edu.fdu.se.handlefile.Method;
import edu.fdu.se.javaparser.JavaParserFactory;
import javassist.compiler.ast.MethodDecl;

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
public class FilePairPreDiff {

    public static void main(String args[]) {
        new FilePairPreDiff().compareTwoFile("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java",
                "D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/accessibilityservice/AccessibilityService.java", "test_file",true);
    }

    public FilePairPreDiff() {
        preprocessedData = new PreprocessedData();
        preprocessedTempData = new PreprocessedTempData();
    }

    private PreprocessedData preprocessedData;
    private PreprocessedTempData preprocessedTempData;


    /**
     * prev的method，field 都到map里做标记
     *
     * @param cu compilationUnit
     */
    private void initPreprocessingDataFromPrev(CompilationUnit cu) {
        TypeDeclaration mTypePrev = cu.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypePrev;
        traverseClassOrInterfaceDeclarationInitPrevData(cod, cod.getNameAsString() + ".");
    }

    /**
     * visited
     */
    private int checkCurrBodies(FieldDeclaration fd, String prefix) {
        BodyDeclarationPair currBdp = new BodyDeclarationPair(fd, prefix);
        if (preprocessedTempData.prevNodeBodyDeclarationMap.containsKey(prefix + fd.toString())) {
            BodyDeclarationPair prevBdP = preprocessedTempData.prevNodeBodyDeclarationMap.get(prefix + fd.toString());
            if (prevBdP.hashCode() == currBdp.hashCode()) {
                preprocessedTempData.addToRemoveList(fd);
                preprocessedTempData.setBodyPrevNodeMap(prevBdP, PreprocessedTempData.BODY_SAME_REMOVE);
                return 1;
            } else {
                // impossible
                System.err.println("--------------------------------");
                return 2;
            }
        } else {
            for (VariableDeclarator vd : fd.getVariables()) {
                if (preprocessedTempData.prevNodeBodyNameMap.containsKey(prefix + vd.getName())) {
                    List<BodyDeclarationPair> mOverload = preprocessedTempData.prevNodeBodyNameMap.get(prefix + vd.getName());
                    for (BodyDeclarationPair mItem : mOverload) {
                        // variable相同， 设置为不删除
                        if (PreprocessedTempData.BODY_SAME_REMOVE != preprocessedTempData.getNodeMapValue(mItem)) {
                            preprocessedTempData.setBodyPrevNodeMap(mItem, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                        }
                    }
                } else {
                    //new field
                    this.preprocessedData.addBodiesAdded(fd, prefix);
                    preprocessedTempData.addToRemoveList(fd);
                }
            }
        }
        return 33;
    }

    /**
     * @param cod             内部类
     * @param prefixClassName classname到cod的name前一个为止
     * @return 1 2
     */
    private int checkCurrBodies(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        String key = prefixClassName + cod.getNameAsString() + ".";
        if (preprocessedTempData.prevNodeBodyDeclarationMap.containsKey(key)) {
            BodyDeclarationPair prevNode = preprocessedTempData.prevNodeBodyDeclarationMap.get(key);
            if (prevNode.getBodyDeclaration().hashCode() == cod.hashCode()
                    && prevNode.getLocationClassString().hashCode() == prefixClassName.hashCode()) {
                preprocessedTempData.addToRemoveList(cod);
                preprocessedTempData.setBodyPrevNodeMap(prevNode, PreprocessedTempData.BODY_SAME_REMOVE);
                traverseClassOrInterfaceDeclarationSetVisited((ClassOrInterfaceDeclaration) prevNode.getBodyDeclaration(), prefixClassName);
                return 1;
            } else {
                preprocessedTempData.setBodyPrevNodeMap(prevNode, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        }
        return 3;
    }

    /**
     * curr的节点去prev的map里check
     */
    private int checkCurrBodies(BodyDeclaration bd, String prefixClassName) {
        // signature 完全一摸一样的
        String bdMapPrevKey = null;
        String bdMapPrevMethodNameKey = null;
        InitializerDeclaration idd;
        if (bd instanceof InitializerDeclaration) {
            idd = (InitializerDeclaration) bd;
            bdMapPrevKey = prefixClassName;
            if (idd.isStatic()) {
                bdMapPrevKey += "static";
            } else {
                bdMapPrevKey += "{";
            }
            bdMapPrevMethodNameKey = prefixClassName + bdMapPrevKey;
        } else {
            if (bd instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bd;
                bdMapPrevKey = prefixClassName + md.getDeclarationAsString();
                bdMapPrevMethodNameKey = prefixClassName + md.getNameAsString();
            } else if (bd instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) bd;
                bdMapPrevKey = prefixClassName + cd.getDeclarationAsString();
                bdMapPrevMethodNameKey = prefixClassName + cd.getNameAsString();
            }
        }

        if (preprocessedTempData.prevNodeBodyDeclarationMap.containsKey(bdMapPrevKey)) {
            BodyDeclarationPair prevNode = preprocessedTempData.prevNodeBodyDeclarationMap.get(bdMapPrevKey);
            if (prevNode.getBodyDeclaration().hashCode() == bd.hashCode()
                    && prevNode.getLocationClassString().hashCode() == prefixClassName.hashCode()) {
                preprocessedTempData.setBodyPrevNodeMap(prevNode, PreprocessedTempData.BODY_SAME_REMOVE);
                preprocessedTempData.addToRemoveList(bd);
                return 1;
            } else {
                preprocessedTempData.setBodyPrevNodeMap(prevNode, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                return 2;
            }
        } else {
            // signature method
            if (preprocessedTempData.prevNodeBodyNameMap.containsKey(bdMapPrevMethodNameKey)) {
                List<BodyDeclarationPair> mOverload = preprocessedTempData.prevNodeBodyNameMap.get(bdMapPrevMethodNameKey);
                // 可能为修改签名之后的方法，也可能为新增的方法
                for (BodyDeclarationPair mItem : mOverload) {
                    if (PreprocessedTempData.BODY_SAME_REMOVE != preprocessedTempData.getNodeMapValue(mItem)) {
                        preprocessedTempData.setBodyPrevNodeMap(mItem, PreprocessedTempData.BODY_DIFFERENT_RETAIN);
                    }
                }
                return 4;
            } else {
                //new method
                this.preprocessedData.addBodiesAdded(bd, prefixClassName);
                preprocessedTempData.addToRemoveList(bd);
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


    public FilePairPreDiff compareTwoFile(String prev, String curr, String outputDirName,boolean debugFlag) {
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
        if(debugFlag) {
            FileWriter.writeInAll(dirFilePrev.getAbsolutePath() + "/file_before_trim.java", cuPrev.toString());
            FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_before_trim.java", cuCurr.toString());
        }
        removeAllCommentsOfCompilationUnit(cuPrev);
        removeAllCommentsOfCompilationUnit(cuCurr);
        initPreprocessingDataFromPrev(cuPrev);
//        preprocessedTempData.removeRemovalList();//2
        ClassOrInterfaceDeclaration codMain = (ClassOrInterfaceDeclaration) cuCurr.getType(0);
        traverseClassOrInterfaceDeclarationCmpCurr((ClassOrInterfaceDeclaration) cuCurr.getType(0), codMain.getNameAsString() + ".");
//        preprocessedTempData.removeRemovalList();//1 考虑后面的识别method name变化，这里把remove的注释掉
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.prevNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
            BodyDeclaration bd = bdp.getBodyDeclaration();
            if (bd instanceof ClassOrInterfaceDeclaration) {
                switch (value) {
//                    case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
//                    case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
//                        break;
                    case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                        this.preprocessedData.addBodiesDeleted(bdp);
                        this.preprocessedTempData.addToRemoveList(bd);
                        traverseClassOrInterfaceDeclarationSetVisited((ClassOrInterfaceDeclaration) bd, bdp.getLocationClassString());
                        break;
                    case PreprocessedTempData.BODY_SAME_REMOVE:
                        this.preprocessedTempData.addToRemoveList(bd);
                        break;
                }
            }

        }
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.prevNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
            BodyDeclaration bd = bdp.getBodyDeclaration();
            if (!(bd instanceof ClassOrInterfaceDeclaration)) {
                switch (value) {
                    case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
                    case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
                        break;
                    case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                        this.preprocessedData.addBodiesDeleted(bdp);
                        preprocessedTempData.addToRemoveList(bd);
                        break;
                    case PreprocessedTempData.BODY_SAME_REMOVE:
                        preprocessedTempData.addToRemoveList(bd);
                        break;
                }
            }
        }
        this.undeleteSignatureChange();
        preprocessedTempData.removeRemovalList();
        if(debugFlag) {
            FileWriter.writeInAll(dirFilePrev.getAbsolutePath() + "/file_after_trim.java", cuPrev.toString());
            FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_after_trim.java", cuCurr.toString());
        }
        this.preprocessedData.setCurrentCu(cuCurr);
        this.preprocessedData.setPreviousCu(cuPrev);
        return this;
    }

    public PreprocessedData getPreprocessedData() {
        return preprocessedData;
    }

    public void undeleteSignatureChange() {
        List<BodyDeclarationPair> addTmp = new ArrayList<>();
        for (BodyDeclarationPair bdpAdd : this.preprocessedData.getmBodiesAdded()) {
            if(bdpAdd.getBodyDeclaration() instanceof MethodDeclaration){
                MethodDeclaration md = (MethodDeclaration)bdpAdd.getBodyDeclaration();
                String methodName = md.getNameAsString();
                List<BodyDeclarationPair> bdpDeleteList = new ArrayList<>();
                for (BodyDeclarationPair bdpDelete : this.preprocessedData.getmBodiesDeleted()) {
                    if(bdpDelete.getBodyDeclaration() instanceof MethodDeclaration){
                        MethodDeclaration md2 = (MethodDeclaration) bdpDelete.getBodyDeclaration();
                        String methodName2 = md2.getNameAsString();
                        if(potentialMethodNameChange(methodName,methodName2)){
                            bdpDeleteList.add(bdpDelete);
                        }
                    }
                }
                if(bdpDeleteList.size()>0){
                    //remove的时候可能会有hashcode相同但是一个是在内部类的情况，但是这种情况很少见，所以暂时先不考虑
                    this.preprocessedTempData.removalList.remove(bdpAdd.getBodyDeclaration());
                    addTmp.add(bdpAdd);
                    for(BodyDeclarationPair bdpTmp:bdpDeleteList){
                        this.preprocessedTempData.removalList.remove(bdpTmp.getBodyDeclaration());
                        this.preprocessedData.getmBodiesDeleted().remove(bdpTmp);
                    }
                }
            }

        }
        for(BodyDeclarationPair tmp:addTmp){
            this.preprocessedData.getmBodiesAdded().remove(tmp);
        }
    }

    public boolean potentialMethodNameChange(String name1,String name2){
        if(name1.length()==0)  return false;
        String tmp;
        if(name1.length()>name2.length()){
            tmp = name1;
            name1 = name2;
            name2 = tmp;
        }
        int i;
        for(i =0;i<name1.length();i++){
            char ch1 = name1.charAt(i);
            char ch2 = name2.charAt(i);
            if(ch1!=ch2){
                break;
            }
        }
        double ii = (i * 1.0)/name1.length();
        if(ii > 0.7){
            return true;
        }
        return false;
    }

    /**
     * curr
     *
     * @param cod             class 节点
     * @param prefixClassName class 节点为止的prefix ， root节点的class prefix 为classname
     */
    public void traverseClassOrInterfaceDeclarationCmpCurr(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        this.preprocessedData.addClassOrInterfaceDeclaration(prefixClassName, cod);
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                int status = checkCurrBodies(cod2, prefixClassName);
                if (status == 3) {
                    this.preprocessedData.addBodiesAdded(cod2, prefixClassName);
                    this.preprocessedTempData.addToRemoveList(cod2);
                } else if (status != 1) {
                    traverseClassOrInterfaceDeclarationCmpCurr(cod2, prefixClassName + cod2.getNameAsString() + ".");
                }
                continue;
            }
            if (node instanceof AnnotationDeclaration) {
                continue;
            }
            if (node instanceof InitializerDeclaration || node instanceof ConstructorDeclaration
                    || node instanceof MethodDeclaration) {
                BodyDeclaration bd = (BodyDeclaration) node;
                checkCurrBodies(bd, prefixClassName);
                continue;
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkCurrBodies(fd, prefixClassName);

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
                preprocessedTempData.addToRemoveList((BodyDeclaration) node);
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
     *
     * @param cod             该节点
     * @param prefixClassName 该节点为止的preix ClassName
     */
    public void traverseClassOrInterfaceDeclarationSetVisited(ClassOrInterfaceDeclaration cod, String prefixClassName) {
        NodeList tmpList = cod.getMembers();
        String childrenClassPrefix = prefixClassName + cod.getNameAsString() + ".";
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            Node n = tmpList.get(m);
            BodyDeclarationPair bdp = new BodyDeclarationPair((BodyDeclaration) n, childrenClassPrefix);
            if (this.preprocessedTempData.prevNodeVisitingMap.containsKey(bdp)) {
                this.preprocessedTempData.setBodyPrevNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
            }
            if (n instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration next = (ClassOrInterfaceDeclaration) n;
                traverseClassOrInterfaceDeclarationSetVisited(next, childrenClassPrefix);
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
        this.preprocessedData.addClassOrInterfaceDeclaration(prefixClassName, cod);
        NodeList nodeList = cod.getMembers();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof BodyDeclaration) {
                BodyDeclaration bd = (BodyDeclaration) node;
                if (bd instanceof AnnotationDeclaration) {
                    //todo index 5 AccountManager
                    continue;
                }
                preprocessedTempData.initBodyPrevNodeMap(bd, prefixClassName);
                BodyDeclarationPair bdp = new BodyDeclarationPair(bd, prefixClassName);

                if (node instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration cod2 = (ClassOrInterfaceDeclaration) node;
                    String subCodName = prefixClassName + cod2.getNameAsString() + ".";
                    traverseClassOrInterfaceDeclarationInitPrevData(cod2, subCodName);
                    preprocessedTempData.addToMapBodyDeclaration(bdp, subCodName);
                    continue;
                }
                if (node instanceof ConstructorDeclaration) {
                    ConstructorDeclaration cd = (ConstructorDeclaration) node;
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + cd.getDeclarationAsString());
                    preprocessedTempData.addToMapBodyName(bdp, prefixClassName + cd.getNameAsString());
                    continue;
                }
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration md = (MethodDeclaration) node;
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + md.getDeclarationAsString());
                    preprocessedTempData.addToMapBodyName(bdp, prefixClassName + md.getNameAsString());
                    continue;
                }
                if (node instanceof FieldDeclaration) {
                    FieldDeclaration fd = (FieldDeclaration) node;
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + fd.toString());

                    for (VariableDeclarator vd : fd.getVariables()) {
                        preprocessedTempData.addToMapBodyName(bdp, prefixClassName + vd.getName());
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
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + iddStr);
                    preprocessedTempData.addToMapBodyName(bdp, prefixClassName + iddStr);
                }
            }

        }
    }

}
