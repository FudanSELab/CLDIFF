package edu.fdu.se.astdiff.preprocessingfile;

import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.treegenerator.JavaParserVisitor;
import edu.fdu.se.javaparser.JDTParserFactory;
import org.apache.ibatis.javassist.compiler.ast.MethodDecl;
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
//        try {
//            ASTParser astParser = ASTParser.newParser(AST.JLS8); // 非常慢
////            String file = "D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java";
//            String file = "C:\\Users\\huangkaifeng\\Desktop\\Test.java";
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
//            byte[] input = new byte[bufferedInputStream.available()];
//            bufferedInputStream.read(input);
//            bufferedInputStream.close();
//            astParser.setSource(new String(input).toCharArray());
//            CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
//            removeAllCommentsOfCompilationUnit(result);
//            List<ASTNode> list =  result.types();
//            TypeDeclaration typeDeclaration = (TypeDeclaration) list.get(0);
//            List<ASTNode> bodies = typeDeclaration.bodyDeclarations();
//            MethodDeclaration method =(MethodDeclaration) bodies.get(0);
//            List<Statement> stmts =  method.getBody().statements();
//            for(Statement s:stmts){
//                ExpressionStatement es = (ExpressionStatement) s;
//                MethodInvocation e = (MethodInvocation) es.getExpression();
//                String a = e.getName().toString();
//                String b = e.getExpression().toString();
//                List arguments = e.arguments();
//                Expression ee = e.getExpression();
//                FieldAccess fd = (FieldAccess) ee;
//                System.out.println(a+ ","+b);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        CompilationUnit cu = JDTParserFactory.getCompilationUnit("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java");
        JavaParserVisitor visitor = new JavaParserVisitor();
        visitor.getTreeContext().setCu(cu);
        ASTNode astNode = cu;
        astNode.accept(visitor);
        TreeContext ctx = visitor.getTreeContext();
        ctx.validate();

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
