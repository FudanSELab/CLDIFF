package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.MatchIfElse;
import edu.fdu.se.astdiff.miningactions.statement.MatchSwitch;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchBlock {

    public static void matchBlock(MiningActionData fp, Action a) {
        Tree fatherNode = (Tree)a.getNode().getParent();
        int type = fatherNode.getAstNode().getNodeType();
        switch (type) {
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitchCaseByFather(fp,a);
                break;
            case ASTNode.IF_STATEMENT:
                //Pattern 1.2 Match else
                MatchIfElse.matchElse(fp, a);
                break;
            case ASTNode.TRY_STATEMENT:
                ////Finally块
                if(fatherNode.getChildPosition(a.getNode()) == fatherNode.getChildren().size()-1){
                    MatchTry.matchFinally(fp, a);
                }
                break;
            default:
                break;
        }
    }

}
