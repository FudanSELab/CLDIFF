package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.StatementConstants;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/15.
 */
public class IfStatementOperation extends ClusteredActionBean {

    public IfStatementOperation(Action curAction, String curNodeType, List<Action> actions, Range linePosition, int operationType, String operationEntity, ITree fafafatherNode, String fatherNodeType){
        super(curAction,curNodeType,actions,linePosition,operationType,operationEntity,fafafatherNode,fatherNodeType);
    }
    final public String statementName = StatementConstants.IFSTATEMENT;






}
