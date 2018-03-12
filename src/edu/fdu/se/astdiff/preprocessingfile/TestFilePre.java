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
            String file = "D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java";
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
            astParser.setSource(new String(input).toCharArray());
            CompilationUnit result = (CompilationUnit) (astParser
                    .createAST(null));
            List<ASTNode> list =  result.types();
            TypeDeclaration typeDeclaration = (TypeDeclaration) list.get(0);
            List<ASTNode> bodies = typeDeclaration.bodyDeclarations();
            for(ASTNode a:bodies){
                System.out.println(a.getClass().getSimpleName());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // 测试 hashCode 情况
        // 测试注释删除之后hashCode情况
        // 测试 methodSignature的情况
        //
    }
}
