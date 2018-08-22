package edu.fdu.se.base.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.miningactions.Body.*;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningactions.statement.*;
import edu.fdu.se.base.miningactions.util.AstRelations;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/2.
 * Statement/Declaration/控制流的子结构
 */
public class ClusterUpDown extends AbstractCluster{

    public ClusterUpDown(Class mClazz, MiningActionData miningActionData) {
        super(mClazz, miningActionData);
    }

    public void doClusterUpDown() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            if(processBigAction(a,insNode.getAstNode().getNodeType())==1) {

            }
        }
    }

    public void passGumtreePalsePositiveMoves(){
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Move mv = (Move)a;
            Tree movedNode = (Tree)fp.getMappedDstOfSrcNode(mv.getNode());
            List<Integer> posA = new ArrayList<>();
            List<Integer> posB = new ArrayList<>();
            List<Tree> treeListA = nonBlockParents((Tree)mv.getNode(),posA);
            List<Tree> treeListB = nonBlockParents(movedNode,posB);
            if(treeListA.size()!=treeListB.size()){
                continue;
            }
            Tree ta = null;
            Tree tb = null;
            int flag = 0;
            for(int i=0;i<treeListA.size();i++){
                ta = treeListA.get(i);
                tb = treeListB.get(i);
                if(ta.getAstNode().getNodeType() != tb.getAstNode().getNodeType()){
                    flag = 1;
                    break;
                }
                if(posA.get(i).intValue() !=posB.get(i).intValue()){
                    flag = 1;
                    break;
                }
            }
            if(flag==1){
                continue;
            }
            if(ta==null|| tb==null ){
                continue;
            }
            if(ta.getTreeSrcOrDst() == ChangeEntityDesc.StageITreeType.SRC_TREE_NODE){
                Tree tmp = (Tree) fp.getMappedDstOfSrcNode(ta);
                if(tmp!=null) {
                    if(tmp.equals(tb)){
                        fp.setActionTraversedMap(a);
                    }
                }
                if(Global.fileName.endsWith("XStreamMarshaller.java")){
                    System.out.println("pass move "+Global.fileName);
                    fp.setActionTraversedMap(a);
                }

            }
        }

    }

    private List<Tree> nonBlockParents(Tree t,List<Integer> posList){
        List<Tree> list = new ArrayList<>();
        Tree parent = t;
        while(true){
            t = parent;
            parent = (Tree) parent.getParent();
            int pos = parent.getChildPosition(t);
            posList.add(pos);
            list.add(parent);
            if(parent.getAstNode().getNodeType()!=ASTNode.BLOCK){
                break;
            }
        }
        return list;
    }



    public int processBigAction(Action a,int type) {
        int res = 0;
        switch (type) {
            // 外面
            case ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassDeclaration(fp, a);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclaration(fp, a);
                break;
            case ASTNode.INITIALIZER:
                MatchInitializerBlock.matchInitializerBlock(fp, a);
                break;
            case ASTNode.METHOD_DECLARATION:
                MatchMethod.matchMethdDeclaration(fp, a);
                break;
            case ASTNode.ENUM_DECLARATION:
            case ASTNode.ENUM_CONSTANT_DECLARATION:
                MatchEnum.matchEnum(fp,a);
                break;

            // 里面
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssert(fp,a);
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIf(fp, a);
                break;
            case ASTNode.BLOCK:
                MatchBlock.matchBlock(fp, a);
                break;
            case ASTNode.BREAK_STATEMENT:
                MatchControlStatements.matchBreakStatements(fp,a);
                break;
            case ASTNode.CONTINUE_STATEMENT:
                MatchControlStatements.matchContinueStatements(fp,a);
            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnStatement(fp, a);
                break;
            case ASTNode.FOR_STATEMENT:
                //增加for语句
                MatchForStatement.matchForStatement(fp, a);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                //增加for语句
                MatchForStatement.matchEnhancedForStatement(fp, a);
                break;
            case ASTNode.WHILE_STATEMENT:
                //增加while语句
                MatchWhileStatement.matchWhileStatement(fp, a);
                break;
            case ASTNode.DO_STATEMENT:
                //增加do while语句
                MatchWhileStatement.matchDoStatement(fp, a);
                break;
            case ASTNode.TRY_STATEMENT:
                MatchTry.matchTry(fp, a);
                break;
            case ASTNode.THROW_STATEMENT:
                MatchTry.matchThrowStatement(fp, a);
                break;
            case ASTNode.CATCH_CLAUSE:
                MatchTry.matchCatchClause(fp,a);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclaration(fp, a);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                if (AstRelations.isFatherXXXStatement(a, ASTNode.IF_STATEMENT) && a.getNode().getParent().getChildPosition(a.getNode()) == 2) {
                    MatchIfElse.matchElse(fp, a);
                } else {
                    MatchExpressionStatement.matchExpression(fp, a);
                }
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                MatchSynchronized.matchSynchronized(fp, a);
                break;
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitch(fp, a);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCase(fp, a);
                break;
            case ASTNode.EMPTY_STATEMENT:
                break;
            case ASTNode.TYPE_DECLARATION_STATEMENT:
                break;
            case ASTNode.CONSTRUCTOR_INVOCATION:
                MatchConstructorInvocation.matchConstructorInvocation(fp,a);
                break;
            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                MatchConstructorInvocation.matchSuperConstructorInvocation(fp,a);
                break;
            case ASTNode.LABELED_STATEMENT:
                MatchLabeledStatement.matchLabeledStatement(fp,a);
                break;
            default:
                res =1;
                break;
        }
        return  res;
    }
}
