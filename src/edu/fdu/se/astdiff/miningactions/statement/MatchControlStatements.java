package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Created by huangkaifeng on 2018/2/7.
 */
public class MatchControlStatements {

    public static void matchBreakStatements(MiningActionData fp,Action a){
        if (AstRelations.isFatherXXXStatement(a, ASTNode.SWITCH_STATEMENT)) {
            MatchSwitch.matchSwitchCaseByFather(fp, a);
        } else {
            //todo
        }
    }
    public static void matchContinueStatements(MiningActionData fp,Action a){

    }
}
