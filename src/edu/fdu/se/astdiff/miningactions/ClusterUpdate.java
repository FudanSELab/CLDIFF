package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.AbstractCluster;
import edu.fdu.se.astdiff.miningactions.Body.MatchSimpleNameOrLiteral;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Created by huangkaifeng on 2018/2/2.
 */
public class ClusterUpdate extends AbstractCluster {

    public ClusterUpdate(Class mClazz, MiningActionData mminingActionData) {
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

    public void doClusterUpdate() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree tmp = (Tree) a.getNode();
            if(processSmallAction(a,tmp.getAstNode().getNodeType())==1){
                System.out.println(SimpleActionPrinter.getMyOneActionString(a));
            }
        }
    }
}
