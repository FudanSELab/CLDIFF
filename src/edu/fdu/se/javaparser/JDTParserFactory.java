package edu.fdu.se.javaparser;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by huangkaifeng on 2018/3/12.
 *
 */
public class JDTParserFactory {

    public static String getUnit(String s){
        String[] temp = s.split("/");
        String t = temp[temp.length-1];
        return t.substring(0,t.length()-5);
    }

    public static CompilationUnit getCompilationUnit(InputStream is) throws Exception{

        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);

//        astParser.setEnvironment(null, null, null, true);
//        astParser.setUnitName("ClusterAction2");//需要与代码文件的名称一致
//        astParser.setResolveBindings(true);
//        astParser.setBindingsRecovery(true);

        astParser.setCompilerOptions(options);
        astParser.setSource(new String(input).toCharArray());
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        return result;
    }


    public static CompilationUnit getCompilationUnit(byte[] fileContent) throws Exception{
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
//        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);
        astParser.setSource(new String(fileContent).toCharArray());
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



    public static String getLinesOfFile(String filePath,List<String> fileList){
        try {
            FileInputStream fis = new FileInputStream(filePath);
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine())!= null){
                fileList.add(line);
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String getLinesOfFile(byte[] fileContent,List<String> fileList){
        try {
            InputStream fis = new ByteArrayInputStream(fileContent);
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine())!= null){
                fileList.add(line);
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Integer> getLinesList(int line){
        List<Integer> mList = new ArrayList<>();
        for(int i=0;i<line;i++){
            mList.add(i);
        }
        return mList;
    }



    public static void main(String args[]) {
        try {
//        CompilationUnit cu = getCompilationUnit("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accounts/AccountManager.java");
            CompilationUnit cu = getCompilationUnit("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java");
            CompilationUnit cuPrev = getCompilationUnit("C:\\Users\\huangkaifeng\\Desktop\\Test.java");
            CompilationUnit cuCurr = getCompilationUnit("C:\\Users\\huangkaifeng\\Desktop\\a\\Test.java");
//            removeAllCommentsOfCompilationUnit(cuPrev);
//            removeAllCommentsOfCompilationUnit(cuCurr);
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

}
