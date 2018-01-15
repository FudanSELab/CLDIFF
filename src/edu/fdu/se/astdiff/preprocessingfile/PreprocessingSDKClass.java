package edu.fdu.se.astdiff.preprocessingfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;

import com.github.javaparser.ast.stmt.ReturnStmt;
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
        new PreprocessingSDKClass().compareTwoSDKFile3("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java",
                "D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/accessibilityservice/AccessibilityService.java","test_file");
//		new PreprocessingSDKClass().test("D:/test.java");
    }

    /**
     * prev的method，field 都到map里做标记
     *
     * @param cu
     */
    public void loadPrev(CompilationUnit cu) {
        bdMapPrev = new HashMap<String, BodyDeclaration>();
        bdMapPrevMethodName = new HashMap<String, List<BodyDeclaration>>();
        visitedPrevNode = new HashMap<BodyDeclaration, Integer>();
        TypeDeclaration mTypePrev = cu.getType(0);
        NodeList nodeList = mTypePrev.getMembers();
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            if (node instanceof AnnotationDeclaration) {
                this.addToRemoveList((BodyDeclaration) node);
            }
            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) node;
                this.visitedPrevNode.put(cd, 0);
                bdMapPrev.put(cd.getDeclarationAsString(), cd);
                addToBdMapPrevMethodName(cd.getNameAsString(), cd);
            }
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                this.visitedPrevNode.put(md, 0);
                bdMapPrev.put(md.getDeclarationAsString(), md);
                addToBdMapPrevMethodName(md.getNameAsString(), md);
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration md = (FieldDeclaration) node;
                this.visitedPrevNode.put(md, 0);
                bdMapPrev.put(md.toString(), md);
            }

            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration) node;
                NodeList innerList = innerClass.getMembers();
                for (int j = 0; j < innerList.size(); j++) {
                    Node item2 = innerList.get(j);
                    if (item2 instanceof AnnotationDeclaration) {
                        this.addToRemoveList((BodyDeclaration) item2);
                    }
                    if (item2 instanceof ConstructorDeclaration) {
                        ConstructorDeclaration cd2 = (ConstructorDeclaration) item2;
                        this.visitedPrevNode.put(cd2, 0);
                        bdMapPrev.put(innerClass.getNameAsString() + "." + cd2.getDeclarationAsString(), cd2);
                        String key = innerClass.getNameAsString() + "." + cd2.getNameAsString();
                        addToBdMapPrevMethodName(key, cd2);
                    }
                    if (item2 instanceof MethodDeclaration) {
                        MethodDeclaration md2 = (MethodDeclaration) item2;
                        this.visitedPrevNode.put(md2, 0);
                        bdMapPrev.put(innerClass.getNameAsString() + "." + md2.getDeclarationAsString(), md2);
                        String key = innerClass.getNameAsString() + "." + md2.getNameAsString();
                        addToBdMapPrevMethodName(key, md2);
                    }
                    if (item2 instanceof FieldDeclaration) {
                        FieldDeclaration md2 = (FieldDeclaration) item2;
                        this.visitedPrevNode.put(md2, 0);
                        bdMapPrev.put(innerClass.getNameAsString() + "." + md2.toString(), md2);
                    }
                }
                bdMapPrev.put(innerClass.getNameAsString(), innerClass);
            }
        }
    }

    private Map<String, BodyDeclaration> bdMapPrev;
    private Map<String, List<BodyDeclaration>> bdMapPrevMethodName;
    private Map<BodyDeclaration, Integer> visitedPrevNode;
    private List<BodyDeclaration> newMethod;
    private List<BodyDeclaration> deletedMethod;
    private CompilationUnit curCu;
    private CompilationUnit preCu;
    
    
    


    public CompilationUnit getCurCu() {
		return curCu;
	}

	public CompilationUnit getPreCu() {
		return preCu;
	}

	/**
     * method name
     *
     * @param key
     * @param bd
     */
    private void addToBdMapPrevMethodName(String key, BodyDeclaration bd) {
        if (bdMapPrevMethodName.containsKey(key)) {
            List<BodyDeclaration> mList = bdMapPrevMethodName.get(key);
            mList.add(bd);
        } else {
            List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
            mList.add(bd);
            bdMapPrevMethodName.put(key, mList);
        }
    }

    /**
     * curr的节点去prev的map里check
     *
     * @param bdMapPrevKey
     * @param bdMapPrevMethodNameKey
     * @param bd
     */
    private int checkCurrBodies(String bdMapPrevKey, String bdMapPrevMethodNameKey, BodyDeclaration bd) {
        // signature 完全一摸一样的
        if (this.bdMapPrev.containsKey(bdMapPrevKey)) {
            this.visitedPrevNode.put(bd, 1);
            BodyDeclaration prevNode = this.bdMapPrev.get(bdMapPrevKey);
            this.visitedPrevNode.put(prevNode, 1);
            if (prevNode.hashCode() == bd.hashCode()) {
                this.addToRemoveList(prevNode);
                this.addToRemoveList(bd);
                return 1;
            } else {
                //不一样的pair
//				if(bdMapPrevKey.equals("private void dispatchServiceConnected()")){
//                System.out.println(bdMapPrevKey);
//				}
                return 2;
            }
        } else {
            if (bdMapPrevMethodNameKey == null) {
                // field return
//				newMethod.add(bd);//new field
//				this.addToRemoveList(bd);
                return 3;
            }
            if (this.bdMapPrevMethodName.containsKey(bdMapPrevMethodNameKey)) {
                List<BodyDeclaration> mOverload = this.bdMapPrevMethodName.get(bdMapPrevMethodNameKey);
                // 可能为修改签名之后的方法，也可能为新增的方法
                for (BodyDeclaration mItem : mOverload) {
                    this.visitedPrevNode.put(mItem, 1);
                }
                return 4;
            } else {
                //TODO new method/field
                newMethod.add(bd);
                this.addToRemoveList(bd);
                return 5;
            }
        }
    }

    private void removeCommentss(Node n) {
        List<Comment> mList = n.getAllContainedComments();
        List<Comment> mList2 = n.getOrphanComments();
        for (Comment m : mList) {
//			n.removeOrphanComment(m);
            m.remove();
        }
        for (Comment m : mList2) {
            m.remove();
        }
    }

    private CompilationUnit removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
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
        cod.removeJavaDocComment();
        cod.removeComment();
        NodeList nodeList = mTypeCurr.getMembers();

        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            node.removeComment();
            removeCommentss(node);
            if (node instanceof AnnotationDeclaration) {
                node.remove();
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
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration) node;
                NodeList innerList = innerClass.getMembers();
                for (int j = 0; j < innerList.size(); j++) {
                    Node item2 = innerList.get(j);
                    item2.removeComment();
                    removeCommentss(node);
                    if (item2 instanceof AnnotationDeclaration) {
                        item2.remove();
                    }
                    if (item2 instanceof ConstructorDeclaration) {
                        ConstructorDeclaration cd2 = (ConstructorDeclaration) item2;
                        cd2.removeJavaDocComment();
                    }
                    if (item2 instanceof MethodDeclaration) {
                        MethodDeclaration md2 = (MethodDeclaration) item2;
                        md2.removeJavaDocComment();
                    }
                    if (item2 instanceof FieldDeclaration) {
                        FieldDeclaration fd2 = (FieldDeclaration) item2;
                        fd2.removeJavaDocComment();
                    }
                }
            }

        }
        return cu;
    }

    private List<BodyDeclaration> removalList;

    private void addToRemoveList(BodyDeclaration bd) {
        if (this.removalList == null) {
            this.removalList = new ArrayList<>();
        }
        this.removalList.add(bd);
    }

    private void removeRemovalList() {
        for (BodyDeclaration item : this.removalList) {
            boolean s = item.remove();
        }
    }

    public PreprocessingSDKClass compareTwoSDKFile3(String prev, String curr,String outputDirName) {
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prev);

        CompilationUnit cuCurr = JavaParserFactory.getCompilationUnit(curr);
        String rootOutPath = ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_GUMTREE_OUTPUT_DIR);
        File dirFilePrev = new File(rootOutPath +"/prev/"+outputDirName);
        File dirFileCurr = new File(rootOutPath +"/curr/"+outputDirName);
        if(!dirFilePrev.exists()){
            dirFilePrev.mkdirs();
        }
        if(!dirFileCurr.exists()){
            dirFileCurr.mkdirs();
        }

        FileWriter.writeInAll( dirFilePrev.getAbsolutePath()+ "/file_before_trim.java", cuPrev.toString());
        FileWriter.writeInAll(dirFileCurr.getAbsolutePath()+ "/file_before_trim.java", cuCurr.toString());
        cuPrev = removeAllCommentsOfCompilationUnit(cuPrev);
        cuCurr = removeAllCommentsOfCompilationUnit(cuCurr);

        loadPrev(cuPrev);
        TypeDeclaration mTypeCurr = cuCurr.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;

        NodeList nodeList = mTypeCurr.getMembers();
        newMethod = new ArrayList<>();
        deletedMethod = new ArrayList<>();

        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof AnnotationDeclaration) {
                this.addToRemoveList((AnnotationDeclaration) node);
                continue;
            }

            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = (ConstructorDeclaration) node;
                checkCurrBodies(cd.getDeclarationAsString(), cd.getNameAsString(), (BodyDeclaration) node);
                continue;
            }
            if (node instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) node;
                checkCurrBodies(md.getDeclarationAsString(), md.getNameAsString(), (BodyDeclaration) node);

                continue;
            }
            if (node instanceof FieldDeclaration) {
                FieldDeclaration fd = (FieldDeclaration) node;
                checkCurrBodies(fd.toString(), null, fd);
                continue;
            }

            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration) node;
                NodeList innerList = innerClass.getMembers();
                int status = checkCurrBodies(innerClass.getNameAsString(), null, innerClass);
                if (status == 1) {
                    continue;
                }
                for (int j = innerList.size() - 1; j >= 0; j--) {
                    Node item2 = innerList.get(j);
                    if (item2 instanceof AnnotationDeclaration) {
                        this.addToRemoveList((BodyDeclaration) item2);
                    }
                    if (item2 instanceof ConstructorDeclaration) {
                        ConstructorDeclaration cd2 = (ConstructorDeclaration) item2;
                        String key = innerClass.getNameAsString() + "." + cd2.getDeclarationAsString();
                        String key2 = innerClass.getNameAsString() + "." + cd2.getNameAsString();
                        checkCurrBodies(key, key2, cd2);
                    }
                    if (item2 instanceof MethodDeclaration) {
                        MethodDeclaration md2 = (MethodDeclaration) item2;
                        String key = innerClass.getNameAsString() + "." + md2.getDeclarationAsString();
                        String key2 = innerClass.getNameAsString() + "." + md2.getNameAsString();
                        checkCurrBodies(key, key2, md2);
                    }
                    if (item2 instanceof FieldDeclaration) {
                        FieldDeclaration fd2 = (FieldDeclaration) item2;
                        checkCurrBodies(innerClass.getNameAsString() + "." + fd2.toString(), null, fd2);
                    }
                }
            }

        }
        for (Entry<BodyDeclaration, Integer> item : this.visitedPrevNode.entrySet()) {
            if (item.getValue() == 1) {
                continue;
            }
            BodyDeclaration bd = item.getKey();
            deletedMethod.add(bd);
            this.addToRemoveList(bd);
        }
        this.removeRemovalList();
        FileWriter.writeInAll(dirFilePrev.getAbsolutePath() +"/file_after_trim.java", cuPrev.toString());
        FileWriter.writeInAll(dirFileCurr.getAbsolutePath() + "/file_after_trim.java", cuCurr.toString());
        this.curCu = cuCurr;
        this.preCu = cuPrev;
        return this;

    }

    public void test(String a) {
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(a);
        cuPrev.removeComment();
        cuPrev.removePackageDeclaration();
        TypeDeclaration mTypeCurr = cuPrev.getType(0);
        mTypeCurr.removeComment();
        mTypeCurr.removeJavaDocComment();
        NodeList nodeList = mTypeCurr.getMembers();
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            MethodDeclaration md = (MethodDeclaration) node;
            md.removeComment();
            removeCommentss(node);
            System.out.print("a");
        }

        System.out.print(cuPrev.toString());
    }

}
