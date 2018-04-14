package edu.fdu.se.astdiff.generatingactions;

import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class JavaParserTreeGenerator {
    public TreeContext srcTC;
    public TreeContext dstTC;
    public ITree src;
    public ITree dst;
    public MappingStore mapping;

    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName.substring(0,fileName.length()-5);
    }

    public JavaParserTreeGenerator(File prevFile, File currFile) {
        File oldFile = prevFile;
        File newFile = currFile;
        try {
            srcTC = generateFromFile(oldFile);
            src = srcTC.getRoot();
            dstTC = generateFromFile(newFile);
            dst = dstTC.getRoot();
            Matcher m = Matchers.getInstance().getMatcher(src, dst);
            m.match();
            mapping = m.getMappings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaParserTreeGenerator(CompilationUnit prev, CompilationUnit curr) {
        srcTC = generateFromCompilationUnit(prev,ChangeEntityDesc.StageITreeType.SRC_TREE_NODE);
        src = srcTC.getRoot();
        dstTC = generateFromCompilationUnit(curr,ChangeEntityDesc.StageITreeType.DST_TREE_NODE);
        dst = dstTC.getRoot();
        Matcher m = Matchers.getInstance().getMatcher(src, dst);
        m.match();
        mapping = m.getMappings();
    }

    public JavaParserTreeGenerator(String prevContent, String currContent) {
        try {
            srcTC = generateFromString(prevContent);
            src = srcTC.getRoot();
            dstTC = generateFromString(currContent);
            dst = dstTC.getRoot();
            Matcher m = Matchers.getInstance().getMatcher(src, dst);
            m.match();
            mapping = m.getMappings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeContext generate(Reader r) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map pOptions = JavaCore.getOptions();
        pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(pOptions);
        parser.setEnvironment(null, null, null, true);
        parser.setUnitName(fileName);//需要与代码文件的名称一致
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setSource(readerToCharArray(r));

        JavaParserVisitor visitor = new JavaParserVisitor();
        ASTNode temp = parser.createAST(null);
        visitor.getTreeContext().setCu((CompilationUnit) temp);
        temp.accept(visitor);
        return visitor.getTreeContext();
    }

    private static char[] readerToCharArray(Reader r) throws IOException {
        StringBuilder fileData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(r)) {
            char[] buf = new char[10];
            int numRead = 0;
            while ((numRead = br.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        }
        return fileData.toString().toCharArray();
    }

    public TreeContext generateFromReader(Reader r) throws IOException {
        TreeContext ctx = generate(r);
        ctx.validate();
        return ctx;
    }

    public TreeContext generateFromFile(File file) throws IOException {
        return generateFromReader(new FileReader(file));
    }

    public TreeContext generateFromString(String content) throws IOException {
        return generateFromReader(new StringReader(content));
    }

    public TreeContext generateFromCompilationUnit(CompilationUnit cu,int srcOrDst) {
        JavaParserVisitor visitor = new JavaParserVisitor(srcOrDst);
        visitor.getTreeContext().setCu(cu);
        ASTNode astNode = cu;
        astNode.accept(visitor);
        TreeContext ctx = visitor.getTreeContext();
        ctx.validate();
        return ctx;
    }

    public String getPrettyOldTreeString() {
        return SimpleActionPrinter.getPrettyTreeString(src);
    }

    public String getPrettyNewTreeString() {
        return SimpleActionPrinter.getPrettyTreeString(dst);
    }


    //JdtMethodCall temp = getJdkMethodCall((MethodInvocation)((Tree)node).getAstNode());




    public JdtMethodCall getJdkMethodCall(MethodInvocation md){
        IMethodBinding mb = md.resolveMethodBinding();
        //如果binding有效，且通过对象或类名调用
        if(mb!=null&&md.getExpression()!=null){
            JdtMethodCall jdtBinding = new JdtMethodCall(md.getExpression().resolveTypeBinding().getQualifiedName(),
                    mb.getName(), mb.getReturnType().getQualifiedName(), mb.getDeclaringClass().getQualifiedName());
            ITypeBinding[] list = mb.getParameterTypes();
            for(int i = 0; i < list.length; i++){
                jdtBinding.addParameter(list[i].getQualifiedName());
            }

            jdtBinding.setJdk(isJdk(md.getExpression().resolveTypeBinding().getQualifiedName()));
            return jdtBinding;
        }else{
            if(mb==null)
                System.out.println(md.getName()+" is null.");
            if(md.getExpression()==null)
                System.out.println(md.getName()+" is local method.");
            return null;
        }
    }
    private boolean isJdk(String s){
        try {
            String temp = s;
            if(temp.contains("<")){
                temp = temp.split("<")[0];
            }
            Class.forName(temp);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

