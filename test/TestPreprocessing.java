import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import edu.fdu.se.javaparser.JavaParserFactory;

/**
 * Created by huangkaifeng on 2018/1/20.
 */
public class TestPreprocessing {
    public static void main(String args[]){

    }

    public void test(String a) {
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(a);
        cuPrev.removeComment();
        cuPrev.removePackageDeclaration();
        TypeDeclaration mTypeCurr = cuPrev.getType(0);
        System.out.print(cuPrev.toString());
    }
}
