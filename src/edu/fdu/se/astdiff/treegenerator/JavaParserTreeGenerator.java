package edu.fdu.se.astdiff.treegenerator;

import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class JavaParserTreeGenerator {
    public TreeContext srcTC;
    public TreeContext dstTC;
    public ITree src;
    public ITree dst;
    public MappingStore mapping;

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

    public TreeContext generate(Reader r) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map pOptions = JavaCore.getOptions();
        pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(pOptions);
        parser.setSource(readerToCharArray(r));
        JavaParserVisitor visitor = new JavaParserVisitor();
        ASTNode temp = parser.createAST(null);
        temp.accept(visitor);
        visitor.getTreeContext().setCu((CompilationUnit)temp);
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
        return  fileData.toString().toCharArray();
    }

    public TreeContext generateFromReader(Reader r) throws IOException {
        TreeContext ctx = generate(r);
        ctx.validate();
        return ctx;
    }
    public TreeContext generateFromFile(File file) throws IOException {
        return generateFromReader(new FileReader(file));
    }

    public String getPrettyOldTreeString() {
        return ConsolePrint.getPrettyTreeString(srcTC, src);
    }
    public String getPrettyNewTreeString(){
        return ConsolePrint.getPrettyTreeString(dstTC, dst);
    }

}

