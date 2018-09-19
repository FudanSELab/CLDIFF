package edu.fdu.se.base.links;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Created by huangkaifeng on 2018/4/16.
 *
 */
public class LinkUtil {
    public static int isRangeWithin(ChangeEntity ce1, ChangeEntity ce2) {
        MyRange myRange1 = ce1.getLineRange();
        MyRange myRange2 = ce2.getLineRange();
        int res= myRange1.isRangeWithin(myRange2);
        if ( res!= 0) {
            return res;
        } else {
            return res;
        }
    }

    public static String findResidingMethodName(Tree t){

        while(true){
            if(t.getAstNode().getClass().toString().endsWith("CompilationUnit")){
                break;
            }
            if(t.getAstNode().getClass().toString().endsWith("Declaration")){
                MethodDeclaration md = (MethodDeclaration) t.getAstNode();
                return md.getName().toString();
            }
            t = (Tree) t.getParent();
        }
        return null;

    }
}
