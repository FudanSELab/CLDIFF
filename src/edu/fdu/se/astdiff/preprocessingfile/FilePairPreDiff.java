package edu.fdu.se.astdiff.preprocessingfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.javaparser.JDTParserFactory;
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
                "D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/accessibilityservice/AccessibilityService.java", "test_file");

    }

    public FilePairPreDiff() {
        preprocessedData = new PreprocessedData();
        preprocessedTempData = new PreprocessedTempData();
    }

    private PreprocessedData preprocessedData;
    private PreprocessedTempData preprocessedTempData;

    private void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
        List<ASTNode> commentList = cu.getCommentList();
        PackageDeclaration packageDeclaration = cu.getPackage();
        if (packageDeclaration != null)
            packageDeclaration.delete();
        List<ImportDeclaration> imprortss = cu.imports();
        for (int i = commentList.size() - 1; i >= 0; i--) {
            commentList.get(i).delete();

        }
        for (int i = imprortss.size() - 1; i >= 0; i--) {
            imprortss.get(i).delete();
        }
        assert cu.types() != null;
        assert cu.types().size() == 1;
    }


    public FilePairPreDiff compareTwoFile(String prev, String curr, String outputDirName) {
        ASTTraversal astTraversal = new ASTTraversal();
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
        if ("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_PREPROCESSING))) {
            FileWriter.writeInAll(dirFilePrev.getAbsolutePath() + "/file_before_trim.java", cuPrev.toString());
            FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_before_trim.java", cuCurr.toString());
        }
        removeAllCommentsOfCompilationUnit(cuPrev);
        removeAllCommentsOfCompilationUnit(cuCurr);
        BodyDeclaration bodyDeclarationPrev = (BodyDeclaration) cuPrev.types().get(0);
        BodyDeclaration bodyDeclarationCurr = (BodyDeclaration) cuCurr.types().get(0);
        if(!(bodyDeclarationPrev instanceof TypeDeclaration)||!(bodyDeclarationCurr instanceof TypeDeclaration)){
            this.preprocessedData.setCurrentCu(cuCurr);
            this.preprocessedData.setPreviousCu(cuPrev);
            return this;
        }
        TypeDeclaration mTypePrev = (TypeDeclaration) cuPrev.types().get(0);
        astTraversal.traverseTypeDeclarationInitPrevData(this.preprocessedData,this.preprocessedTempData,mTypePrev, mTypePrev.getName().toString() + ".");
//        preprocessedTempData.removeRemovalList();//2
        TypeDeclaration codMain = (TypeDeclaration) cuCurr.types().get(0);
        astTraversal.traverseTypeDeclarationCompareCurr(this.preprocessedData,this.preprocessedTempData,codMain, codMain.getName().toString() + ".");
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
                        astTraversal.traverseTypeDeclarationSetVisited(this.preprocessedTempData,(TypeDeclaration) bd, bdp.getLocationClassString());
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
        if ("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_PREPROCESSING))) {
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
            if (bdpAdd.getBodyDeclaration() instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bdpAdd.getBodyDeclaration();
                String methodName = md.getName().toString();
                List<BodyDeclarationPair> bdpDeleteList = new ArrayList<>();
                for (BodyDeclarationPair bdpDelete : this.preprocessedData.getmBodiesDeleted()) {
                    if (bdpDelete.getBodyDeclaration() instanceof MethodDeclaration) {
                        MethodDeclaration md2 = (MethodDeclaration) bdpDelete.getBodyDeclaration();
                        String methodName2 = md2.getName().toString();
                        if (potentialMethodNameChange(methodName, methodName2)) {
                            bdpDeleteList.add(bdpDelete);
                        }
                    }
                }
                if (bdpDeleteList.size() > 0) {
                    //remove的时候可能会有hashcode相同但是一个是在内部类的情况，但是这种情况很少见，所以暂时先不考虑
                    this.preprocessedTempData.removalList.remove(bdpAdd.getBodyDeclaration());
                    addTmp.add(bdpAdd);
                    for (BodyDeclarationPair bdpTmp : bdpDeleteList) {
                        this.preprocessedTempData.removalList.remove(bdpTmp.getBodyDeclaration());
                        this.preprocessedData.getmBodiesDeleted().remove(bdpTmp);
                    }
                }
            }

        }
        for (BodyDeclarationPair tmp : addTmp) {
            this.preprocessedData.getmBodiesAdded().remove(tmp);
        }
    }

    public boolean potentialMethodNameChange(String name1, String name2) {
        if (name1.length() == 0) return false;
        String tmp;
        if (name1.length() > name2.length()) {
            tmp = name1;
            name1 = name2;
            name2 = tmp;
        }
        int i;
        for (i = 0; i < name1.length(); i++) {
            char ch1 = name1.charAt(i);
            char ch2 = name2.charAt(i);
            if (ch1 != ch2) {
                break;
            }
        }
        double ii = (i * 1.0) / name1.length();
        if (ii > 0.7) {
            return true;
        }
        return false;
    }



}
