package edu.fdu.se.astdiff.miningchangeentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.associating.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StageIIBean;
import edu.fdu.se.astdiff.miningchangeentity.statement.ForChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.IfChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.SynchronizedChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.WhileChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 *
 */
public class ChangeEntityPreprocess {

    public ChangeEntityPreprocess(ChangeEntityData ced){
        this.ced = ced;
    }

    public ChangeEntityData ced;


    public void preprocessChangeEntity() {
        this.initContainerEntityData();
        this.printContainerEntityDataBefore();
        this.mergeMoveAndWrapper();
        this.setChangeEntitySub();
        this.setChangeEntityOpt2Opt2Exp();
//        this.printContainerEntityDataAfter();
        this.printNaturalEntityDesc();
    }



    public void setChangeEntityOpt2Opt2Exp(){
        LayeredChangeEntityContainer container = this.ced.entityContainer;
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : container.getLayerMap().entrySet()) {
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            List<ChangeEntity> mList = entry.getValue();
            for(ChangeEntity ce:mList){
                if(!ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
                    if(ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
                        if(ce.stageIIBean.getGranularity().equals(ChangeEntityDesc.StageIIGranularity.GRANULARITY_CLASS)){
                        	System.out.println(333333333);
                            // class signature 设置
//                        	List<Action> actions = ce.clusteredActionBean.actions;
//                        	for(Action a:actions){
//                                Tree tree = (Tree) a.getNode();
//                                int nodeType = tree.getAstNode().getNodeType();
//                                System.out.println(tree.getLabel());
//                                System.out.println(tree.getType());
//                                }
                        }else if(ce.stageIIBean.getGranularity().equals(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER)){
                        	System.out.println(444444444);
                            // method signature
                        }else if(ce.stageIIBean.getGranularity().equals(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT)){
                        	System.out.println(555555555);

                            // stmt
                            ChangeEntity a;
                            List<Action> actions = ce.clusteredActionBean.actions;
                            generatingExpressions(actions,ce.stageIIBean);
                        }
                    }
                }
            }
        }
    }

    public void generatingExpressions(List<Action> actions,StageIIBean bean){
        for(Action a:actions){
            Tree tree = (Tree) a.getNode();
            int nodeType = tree.getAstNode().getNodeType();
//            System.out.println(tree.getAstNode());
            boolean flag = false;
            switch(nodeType){
                case ASTNode.NORMAL_ANNOTATION:
                case ASTNode.MARKER_ANNOTATION:
                case ASTNode.SINGLE_MEMBER_ANNOTATION:
                case ASTNode.ARRAY_ACCESS:
                case ASTNode.ARRAY_CREATION:
                case ASTNode.ARRAY_INITIALIZER:
                case ASTNode.ASSIGNMENT:
                case ASTNode.BOOLEAN_LITERAL:
                case ASTNode.CAST_EXPRESSION:
                case ASTNode.CHARACTER_LITERAL:
                case ASTNode.CLASS_INSTANCE_CREATION:
                case ASTNode.CONDITIONAL_EXPRESSION:
                case ASTNode.CREATION_REFERENCE:
                case ASTNode.EXPRESSION_METHOD_REFERENCE:
                case ASTNode.FIELD_ACCESS:
                case ASTNode.INFIX_EXPRESSION:
                case ASTNode.INSTANCEOF_EXPRESSION:
                case ASTNode.LAMBDA_EXPRESSION:
                case ASTNode.METHOD_INVOCATION:
//                case ASTNode.SUPER_METHOD_REFERENCE:
                case ASTNode.QUALIFIED_NAME:
                case ASTNode.SIMPLE_NAME:
                case ASTNode.NULL_LITERAL:
                case ASTNode.NUMBER_LITERAL:
                case ASTNode.PARENTHESIZED_EXPRESSION:
                case ASTNode.POSTFIX_EXPRESSION:
                case ASTNode.PREFIX_EXPRESSION:
                case ASTNode.STRING_LITERAL:
                case ASTNode.SUPER_FIELD_ACCESS:
                case ASTNode.SUPER_METHOD_INVOCATION:
                case ASTNode.SUPER_METHOD_REFERENCE:
                case ASTNode.THIS_EXPRESSION:
                case ASTNode.TYPE_LITERAL:
                case ASTNode.TYPE_METHOD_REFERENCE:
                case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
                flag=true;
                break;
                default:break;
            }
            if(flag){
                String name =a.getClass().getSimpleName();
                String exp = tree.getAstNode().getClass().getSimpleName();
//            	System.out.println("-------------------------"+tree.getAstNode().getNodeType());
//                System.out.println("-------------------------"+name+"  "+exp);
                bean.addOpt2AndOpt2Expression(name,exp);
            }
        }
    }

    public void mergeMoveAndWrapper() {
        LayeredChangeEntityContainer container = this.ced.entityContainer;
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : container.getLayerMap().entrySet()) {
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            if (bodyDeclarationPair.getBodyDeclaration() instanceof MethodDeclaration) {
                //每个method里面
                List<ChangeEntity> mList = entry.getValue();
                List<ChangeEntity> moveList = new ArrayList<>();
                List<ChangeEntity> stmtWrapperList = new ArrayList<>();
                List<ChangeEntity> deletedMove = new ArrayList<>();
                for (ChangeEntity ce : mList) {
                    int resultCode = ChangeEntityUtil.checkEntityCode(ce);
                    if (resultCode == 1) {
                        moveList.add(ce);
                    } else if (resultCode == 2) {
                        stmtWrapperList.add(ce);
                    }
                }
                for (ChangeEntity ce : stmtWrapperList) {
                    for (ChangeEntity mv : moveList) {
                        if (ChangeEntityUtil.isMoveInWrapper(ced.mad,ce, mv)) {
                            deletedMove.add(mv);
                            ce.clusteredActionBean.changePacket.getChangeSet2().add(Move.class.getSimpleName());
                        }
                    }
                }
                for (ChangeEntity e : deletedMove) {
                    mList.remove(e);
                    ced.mad.getChangeEntityList().remove(e);
                }

            }
        }
    }

    public void initContainerEntityData() {
        ced.mad.preprocessedData.getmBodiesAdded().forEach(a -> {
            ChangeEntity ce = ced.addOneBody(a, Insert.class.getSimpleName());
            ced.entityContainer.addPreDiffChangeEntity(ce);
            if(ce!=null){
                ced.mad.getChangeEntityList().add(ce);
            }
        });
        ced.mad.preprocessedData.getmBodiesDeleted().forEach(a -> {
            ChangeEntity ce = ced.addOneBody(a,Delete.class.getSimpleName());
            ced.entityContainer.addPreDiffChangeEntity(ce);
            if(ce!=null){
                ced.mad.getChangeEntityList().add(ce);
            }
        });
        if (ced.mad.preprocessedData.getPreprocessChangeEntity() != null) {
            ced.mad.preprocessedData.getPreprocessChangeEntity().forEach(a->{
                ced.entityContainer.addPreDiffChangeEntity(a);
                ced.mad.getChangeEntityList().add(a);
            });
        }
        ced.mad.getChangeEntityList().forEach(a->{
            if (!a.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                ced.entityContainer.addGumTreePlus(a, ced.mad);
            }
        });
        ced.entityContainer.sortEntityList();

    }

    public void printContainerEntityDataBefore() {
        ChangeEntityPrinter.printContainerEntity(ced.entityContainer, ced.mad.preprocessedData.srcCu);
    }

    public void printContainerEntityDataAfter(){
        ChangeEntityPrinter.printContainerEntity(ced.entityContainer, ced.mad.preprocessedData.srcCu);
    }
    public void printNaturalEntityDesc(){
        ChangeEntityPrinter.printContainerEntityNatural(ced.entityContainer, ced.mad.preprocessedData.srcCu);
    }

    public void setChangeEntitySub(){
        ced.mad.getChangeEntityList().forEach(a -> {
            if (!a.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                if(a instanceof ForChangeEntity || a instanceof WhileChangeEntity
                        || a instanceof SynchronizedChangeEntity || a instanceof IfChangeEntity){
                    if(a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)||a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)){
                        if(a.clusteredActionBean.changePacket.getChangeSet2().contains(Move.class.getSimpleName())){
                            if(a.clusteredActionBean.changePacket.getChangeSet2().contains(Insert.class.getSimpleName())||
                                    a.clusteredActionBean.changePacket.getChangeSet2().contains(Delete.class.getSimpleName())){
                                a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION_AND_PARTIAL_BODY);
                            }else{
                                a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION);
                            }
                        }else{
                            a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION_AND_BODY);
                        }
                    }
                }
            }
        });
    }





}
