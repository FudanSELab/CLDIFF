package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.generatingactions.JdtMethodCall;
import edu.fdu.se.javaparser.JDTParserFactory;
import edu.fdu.se.javaparser.JavaParserFactory;
import javassist.compiler.ast.MethodDecl;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/10.
 *
 */
public class Test {

    public static void  getJdkMethodCall(MethodInvocation md){
        IMethodBinding mb = md.resolveMethodBinding();
        if(mb!=null&&md.getExpression()!=null){
//            JdtMethodCall jdtBinding = new JdtMethodCall(md.getExpression().resolveTypeBinding().getQualifiedName(),
//                    mb.getName(), mb.getReturnType().getQualifiedName(), mb.getDeclaringClass().getQualifiedName());
            System.out.println(md.getExpression().resolveTypeBinding().getQualifiedName());
            System.out.println(mb.getName());
            System.out.println(mb.getReturnType().getQualifiedName());
            System.out.println(mb.getDeclaringClass().getQualifiedName());
            ITypeBinding[] list = mb.getParameterTypes();
            for(int i = 0; i < list.length; i++){
//                jdtBinding.addParameter(list[i].getQualifiedName());
            }

//            jdtBinding.setJdk(isJdk(md.getExpression().resolveTypeBinding().getQualifiedName()));
//            return jdtBinding;
        }else{
            if(mb==null)
                System.out.println(md.getName()+" is null.");
            if(md.getExpression()==null)
                System.out.println(md.getName()+" is local method.");
//            return null;
        }
    }

    public static void main(String args[]){
        String file = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\test\\prev\\ClusterAction2.java";
        CompilationUnit cu = JDTParserFactory.getCompilationUnit(file);
        TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
        List<Type> aa  = td.superInterfaceTypes();
        for(Type aaa:aa) {
            System.out.println(aaa.toString());
        }
        td.getSuperclassType();
        MethodDeclaration[] mds =  td.getMethods();
        for(MethodDeclaration md:mds){
            if(md.getName().toString().equals("method1")){
                List<Statement> mList = md.getBody().statements();
                for(Statement m:mList){
                    if(m instanceof ExpressionStatement){
                        ExpressionStatement es = (ExpressionStatement)m;
                        Expression e =es.getExpression();
                        Assignment a = (Assignment) e;
                        MethodInvocation mi =(MethodInvocation) a.getRightHandSide();
//                        IMethodBinding mb = mi.resolveMethodBinding();
//                        SimpleName sn = (SimpleName) a.getLeftHandSide();
//                        getJdkMethodCall(mi);
//                        System.out.println(mb);
//                        System.out.println(mi.getName());
                    }
                }
            }
        }
//        System.out.println(cu.toString());

//            String file = "D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java";
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

//        List<String> m  = new ArrayList<>();
//        String full = JDTParserFactory.getLinesOfFile(file,m);
//        StringBuilder sb = new StringBuilder();
//        TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
////        td.getJavadoc().delete();
//        System.out.println(cu.getLineNumber(td.getStartPosition())+","+cu.getLineNumber(td.getStartPosition()+td.getLength()-1));
//        List<BodyDeclaration> list = td.bodyDeclarations();
//        List<ASTNode> commentList = cu.getCommentList();
//        for(BodyDeclaration bd:list){
//            if(bd.getJavadoc()!=null){
//////                System.out.println(cu.getLineNumber(bd.getStartPosition()+bd.getJavadoc().getLength()-1)+","+cu.getLineNumber(bd.getStartPosition()+bd.getLength()-1));
//                int a = bd.getJavadoc().getStartPosition();
//                int b = bd.getJavadoc().getLength();
//                int c = bd.getStartPosition();
//                int d = bd.getLength();
//                bd.getJavadoc().delete();
//                System.out.println(bd.getJavadoc()==null);
//                System.out.println(a+","+b+","+c+","+d);
////                System.out.println(full.substring(a,a+b));
//                System.out.println(full.substring(a+b+1,a+d).trim());
//            }else{
////                start =bd.getStartPosition();
//                System.out.println(bd.toString());
//            }
////            System.out.println( cu.getLineNumber(bd.getStartPosition())+","+cu.getLineNumber(bd.getStartPosition()+bd.getLength()-1));
//            break;
//        }
//        System.out.println(cu.toString());


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

