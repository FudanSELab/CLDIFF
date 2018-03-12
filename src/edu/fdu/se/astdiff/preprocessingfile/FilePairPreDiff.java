package edu.fdu.se.astdiff.preprocessingfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.javaparser.JDTParserFactory;
import edu.fdu.se.javaparser.JDTParserUtil;
import org.eclipse.jdt.core.dom.*;

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
        TypeDeclaration mTypePrev = (TypeDeclaration) cu.types().get(0);
        traverseTypeDeclarationInitPrevData(mTypePrev, mTypePrev.getName().toString() + ".");
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
            List<VariableDeclarationFragment> mmList = fd.fragments();
            for (VariableDeclarationFragment vd:mmList) {
                if (preprocessedTempData.prevNodeBodyNameMap.containsKey(prefix + vd.getName().toString())) {
                    List<BodyDeclarationPair> mOverload = preprocessedTempData.prevNodeBodyNameMap.get(prefix + vd.getName().toString());
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
    private int checkCurrBodies(TypeDeclaration cod, String prefixClassName) {
        String key = prefixClassName + cod.getName().toString() + ".";
        if (preprocessedTempData.prevNodeBodyDeclarationMap.containsKey(key)) {
            BodyDeclarationPair prevNode = preprocessedTempData.prevNodeBodyDeclarationMap.get(key);
            if (prevNode.getBodyDeclaration().hashCode() == cod.hashCode()
                    && prevNode.getLocationClassString().hashCode() == prefixClassName.hashCode()) {
                preprocessedTempData.addToRemoveList(cod);
                preprocessedTempData.setBodyPrevNodeMap(prevNode, PreprocessedTempData.BODY_SAME_REMOVE);
                traverseTypeDeclarationSetVisited((TypeDeclaration) prevNode.getBodyDeclaration(), prefixClassName);
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
        Initializer idd;
        if (bd instanceof Initializer) {
            idd = (Initializer) bd;
            bdMapPrevKey = prefixClassName;
            if (idd.modifiers().contains("static")) {
                bdMapPrevKey += "static";
            } else {
                bdMapPrevKey += "{";
            }
            bdMapPrevMethodNameKey = prefixClassName + bdMapPrevKey;
        } else {
            if (bd instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bd;
                bdMapPrevKey = prefixClassName + JDTParserUtil.getDeclarationAsString(md);
                bdMapPrevMethodNameKey = prefixClassName + md.getName().toString();
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



    private void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
        List<ASTNode> commentList = cu.getCommentList();
        PackageDeclaration packageDeclaration = cu.getPackage();
        if(packageDeclaration!=null)
            packageDeclaration.delete();
        List<ImportDeclaration> imprortss = cu.imports();
        for(int i = commentList.size()-1;i>=0 ;i--){
            commentList.get(i).delete();

        }
        for(int i = imprortss.size()-1;i>=0 ;i--){
            imprortss.get(i).delete();
        }
        assert cu.types() != null;
        assert cu.types().size() == 1;
    }


    public FilePairPreDiff compareTwoFile(String prev, String curr, String outputDirName,boolean debugFlag) {
        CompilationUnit cuPrev = JDTParserFactory.getCompilationUnit(prev);
        CompilationUnit cuCurr = JDTParserFactory.getCompilationUnit(curr);
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
        TypeDeclaration codMain = (TypeDeclaration) cuCurr.types().get(0);
        traverseTypeDeclarationCmpCurr(codMain, codMain.getName().toString() + ".");
//        preprocessedTempData.removeRemovalList();//1 考虑后面的识别method name变化，这里把remove的注释掉
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.prevNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
            BodyDeclaration bd = bdp.getBodyDeclaration();
            if (bd instanceof TypeDeclaration) {
                switch (value) {
//                    case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
//                    case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
//                        break;
                    case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                        this.preprocessedData.addBodiesDeleted(bdp);
                        this.preprocessedTempData.addToRemoveList(bd);
                        traverseTypeDeclarationSetVisited((TypeDeclaration) bd, bdp.getLocationClassString());
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
            if (!(bd instanceof TypeDeclaration)) {
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
                String methodName = md.getName().toString();
                List<BodyDeclarationPair> bdpDeleteList = new ArrayList<>();
                for (BodyDeclarationPair bdpDelete : this.preprocessedData.getmBodiesDeleted()) {
                    if(bdpDelete.getBodyDeclaration() instanceof MethodDeclaration){
                        MethodDeclaration md2 = (MethodDeclaration) bdpDelete.getBodyDeclaration();
                        String methodName2 = md2.getName().toString();
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
    public void traverseTypeDeclarationCmpCurr(TypeDeclaration cod, String prefixClassName) {
        this.preprocessedData.addTypeDeclaration(prefixClassName, cod);
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration node = nodeList.get(i);
            if (node instanceof TypeDeclaration) {
                TypeDeclaration cod2 = (TypeDeclaration) node;
                int status = checkCurrBodies(cod2, prefixClassName);
                if (status == 3) {
                    this.preprocessedData.addBodiesAdded(cod2, prefixClassName);
                    this.preprocessedTempData.addToRemoveList(cod2);
                } else if (status != 1) {
                    traverseTypeDeclarationCmpCurr(cod2, prefixClassName + cod2.getName().toString() + ".");
                }
                continue;
            }
            if (node instanceof Initializer
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

//    /**
//     * prev + curr
//     *
//     * @param cod class name
//     */
//    public void traverseTypeDeclarationRemoveComment(TypeDeclaration cod) {
//        cod.removeJavaDocComment();
//        cod.removeComment();
//        NodeList nodeList = cod.getMembers();
//        for (int i = nodeList.size() - 1; i >= 0; i--) {
//            Node node = nodeList.get(i);
//            node.removeComment();
//            removeCommentss(node);
//            if (node instanceof TypeDeclaration) {
//                traverseTypeDeclarationRemoveComment((TypeDeclaration) node);
//            }
//            if (node instanceof AnnotationDeclaration) {
//                preprocessedTempData.addToRemoveList((BodyDeclaration) node);
//            }
//            if (node instanceof ConstructorDeclaration) {
//                ConstructorDeclaration cd = (ConstructorDeclaration) node;
//                cd.removeJavaDocComment();
//            }
//            if (node instanceof MethodDeclaration) {
//                MethodDeclaration md = (MethodDeclaration) node;
//                md.removeJavaDocComment();
//            }
//            if (node instanceof FieldDeclaration) {
//                FieldDeclaration fd = (FieldDeclaration) node;
//                fd.removeJavaDocComment();
//            }
//            if (node instanceof Initializer) {
//                Initializer idd = (Initializer) node;
//                idd.removeJavaDocComment();
//            }
//        }
//    }

    /**
     * 设置该cod下的孩子节点为访问，因为father已经被remove了，所以不需要remove
     *
     * @param cod             该节点
     * @param prefixClassName 该节点为止的preix ClassName
     */
    public void traverseTypeDeclarationSetVisited(TypeDeclaration cod, String prefixClassName) {
        List<BodyDeclaration> tmpList = cod.bodyDeclarations();
        String childrenClassPrefix = prefixClassName + cod.getName().toString() + ".";
        for (int m = tmpList.size() - 1; m >= 0; m--) {
            BodyDeclaration n = tmpList.get(m);
            BodyDeclarationPair bdp = new BodyDeclarationPair((BodyDeclaration) n, childrenClassPrefix);
            if (this.preprocessedTempData.prevNodeVisitingMap.containsKey(bdp)) {
                this.preprocessedTempData.setBodyPrevNodeMap(bdp, PreprocessedTempData.BODY_FATHERNODE_REMOVE);
            }
            if (n instanceof TypeDeclaration) {
                TypeDeclaration next = (TypeDeclaration) n;
                traverseTypeDeclarationSetVisited(next, childrenClassPrefix);
            }
        }
    }

    /**
     * prev
     *
     * @param cod             classname
     * @param prefixClassName prefix name
     */
    public void traverseTypeDeclarationInitPrevData(TypeDeclaration cod, String prefixClassName) {
        this.preprocessedData.addTypeDeclaration(prefixClassName, cod);
        List<BodyDeclaration> nodeList = cod.bodyDeclarations();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            BodyDeclaration node = nodeList.get(i);
            if (node instanceof BodyDeclaration) {
                BodyDeclaration bd = (BodyDeclaration) node;
//                if (bd instanceof AnnotationDeclaration) {
//                    //todo index 5 AccountManager
//                    continue;
//                }
                preprocessedTempData.initBodyPrevNodeMap(bd, prefixClassName);
                BodyDeclarationPair bdp = new BodyDeclarationPair(bd, prefixClassName);

                if (node instanceof TypeDeclaration) {
                    TypeDeclaration cod2 = (TypeDeclaration) node;
                    String subCodName = prefixClassName + cod2.getName().toString() + ".";
                    traverseTypeDeclarationInitPrevData(cod2, subCodName);
                    preprocessedTempData.addToMapBodyDeclaration(bdp, subCodName);
                    continue;
                }
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration md = (MethodDeclaration) node;
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + JDTParserUtil.getDeclarationAsString(md));
                    preprocessedTempData.addToMapBodyName(bdp, prefixClassName + md.getName().toString());
                    continue;
                }
                if (node instanceof FieldDeclaration) {
                    FieldDeclaration fd = (FieldDeclaration) node;
                    preprocessedTempData.addToMapBodyDeclaration(bdp, prefixClassName + fd.toString());
                    List<VariableDeclarationFragment> mmList = fd.fragments();
                    for (VariableDeclarationFragment vd : mmList) {
                        preprocessedTempData.addToMapBodyName(bdp, prefixClassName + vd.getName().toString());
                    }
                    continue;
                }
                if (node instanceof Initializer) {
                    //内部类不会有static
                    Initializer idd = (Initializer) node;
                    String iddStr;
                    if (idd.modifiers().contains("static")) {
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
