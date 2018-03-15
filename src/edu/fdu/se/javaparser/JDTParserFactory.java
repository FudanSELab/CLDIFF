package edu.fdu.se.javaparser;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.List;
import java.util.Map;
/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class JDTParserFactory {

    public static CompilationUnit getCompilationUnit(InputStream is) throws Exception{
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);
        astParser.setSource(new String(input).toCharArray());
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        return result;
    }

    public static CompilationUnit getCompilationUnit(String filePath){
        try {
            return getCompilationUnit(new FileInputStream(filePath));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String args[]) {
        try {
//        CompilationUnit cu = getCompilationUnit("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accounts/AccountManager.java");
//            CompilationUnit cu = getCompilationUnit("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java");
            CompilationUnit cuPrev = getCompilationUnit("C:\\Users\\huangkaifeng\\Desktop\\Test.java");
            CompilationUnit cuCurr = getCompilationUnit("C:\\Users\\huangkaifeng\\Desktop\\a\\Test.java");
            removeAllCommentsOfCompilationUnit(cuPrev);
            removeAllCommentsOfCompilationUnit(cuCurr);
            List<ASTNode> list =  cuPrev.types();
            List<ASTNode> list2 = cuCurr.types();
            TypeDeclaration typeDeclaration = (TypeDeclaration) list.get(0);
            TypeDeclaration typeDeclaration2 = (TypeDeclaration) list2.get(0);
            List<ASTNode> bodies = typeDeclaration.bodyDeclarations();
            List<ASTNode> bodies2 = typeDeclaration2.bodyDeclarations();
            for(ASTNode a:bodies){
                System.out.println(a.getClass().getSimpleName()+" "+a.hashCode());
                System.out.println(a.toString());
            }
            System.out.print("--------------------------\n");
            for(ASTNode a:bodies2){
                System.out.println(a.getClass().getSimpleName()+" "+a.hashCode());
                System.out.println(a.toString());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
        List<ASTNode> commentList = cu.getCommentList();
        PackageDeclaration packageDeclaration = cu.getPackage();
        if(packageDeclaration!=null) packageDeclaration.delete();
        List<ASTNode> imprortss = cu.imports();
        for(int i = commentList.size()-1;i>=0 ;i--){
//            System.out.println(commentList.get(i).getClass().getSimpleName());

            commentList.get(i).delete();
        }
        for(int i = imprortss.size()-1;i>=0 ;i--){
//        	System.out.println(imprortss.get(i).getClass().getSimpleName());
            imprortss.get(i).delete();
        }
        assert cu.types() != null;
        assert cu.types().size() == 1;
//        System.out.println(cu.toString());
    }

}
