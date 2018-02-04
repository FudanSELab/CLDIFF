package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.MatchSimpleNameOrLiteral;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/2.
 */
public class ClusterSmall extends AbstractCluster{


    public ClusterSmall(Class mClazz, MiningActionData mminingActionData) {
        super(mClazz, mminingActionData);
    }



    public int processSmallAction(Action a,int type) {
        int res = 0;
        switch(type){
            case ASTNode.TAG_ELEMENT:
            case ASTNode.TEXT_ELEMENT:
            case ASTNode.SIMPLE_NAME:
            case ASTNode.SIMPLE_TYPE:
            case ASTNode.STRING_LITERAL:
            case ASTNode.NULL_LITERAL:
            case ASTNode.PREFIX_EXPRESSION:
            case ASTNode.CHARACTER_LITERAL:
            case ASTNode.NUMBER_LITERAL:
            case ASTNode.BOOLEAN_LITERAL:
            case ASTNode.INFIX_EXPRESSION:
            case ASTNode.METHOD_INVOCATION:
            case ASTNode.QUALIFIED_NAME:
            case ASTNode.MODIFIER:
            case ASTNode.MARKER_ANNOTATION:
            case ASTNode.NORMAL_ANNOTATION:
            case ASTNode.SINGLE_MEMBER_ANNOTATION:
            case ASTNode.ASSIGNMENT:
                MatchSimpleNameOrLiteral.matchSimpleNameOrLiteral(fp, a);
                break;
            default:
                res= 1;
                break;
        }
        return res;

    }





    public void doClusterSmall() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            if(processSmallAction(a,insNode.getAstNode().getNodeType())==1) {
                System.err.println(SimpleActionPrinter.getMyOneActionString(a));
            }
        }
    }
}
