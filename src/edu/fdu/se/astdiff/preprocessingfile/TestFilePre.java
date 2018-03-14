package edu.fdu.se.astdiff.preprocessingfile;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/10.
 *
 */
public class TestFilePre {

    public static void main(String args[]){
        try {
            ASTParser astParser = ASTParser.newParser(AST.JLS8); // 非常慢
//            String file = "D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java";
            String file = "C:\\Users\\huangkaifeng\\Desktop\\a\\Test.java";
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
            astParser.setSource(new String(input).toCharArray());
            CompilationUnit result = (CompilationUnit) (astParser
                    .createAST(null));
            removeAllCommentsOfCompilationUnit(result);
            List<ASTNode> list =  result.types();
            TypeDeclaration typeDeclaration = (TypeDeclaration) list.get(0);
            List<ASTNode> bodies = typeDeclaration.bodyDeclarations();
            ASTNode aaa = null;
            for(ASTNode a:bodies){
                System.out.println(a.getClass().getSimpleName()+" " + result.getLineNumber(a.getStartPosition()) +","+result.getLineNumber(a.getStartPosition()+a.getLength()-1));
//                System.out.println(a.toString());
                if(a.getClass().getSimpleName().equals("TypeDeclaration")){
                	aaa = a;
                }
            }
            aaa.delete();
            System.out.println("-------------------");
            bodies = typeDeclaration.bodyDeclarations();
            for(ASTNode a:bodies){
            	System.out.println(a.getClass().getSimpleName()+" " + result.getLineNumber(a.getStartPosition()) +","+result.getLineNumber(a.getStartPosition()+a.getLength()-1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // 测试 hashCode 情况
        // 测试注释删除之后hashCode情况
        // 测试 methodSignature的情况
        //
    }
    
    private static void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
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
}
