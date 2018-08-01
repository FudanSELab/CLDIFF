package edu.fdu.se.base.associating.linkbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningactions.util.MyList;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.member.MethodChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class MethodData extends LinkBean {

    public MethodData(MethodChangeEntity ce, MiningActionData fp) {
        this.parameterName = new MyList<>();
        this.parameterType = new MyList<>();
        this.methodName = new MyList<>();

        if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
            MethodDeclaration md = (MethodDeclaration) ce.bodyDeclarationPair.getBodyDeclaration();
            setValue(md);
        }else{
            Tree tree = (Tree)ce.clusteredActionBean.curAction.getNode();
            if(ce.clusteredActionBean.curAction instanceof Move){
                if(tree.getAstNode().getNodeType() == ASTNode.METHOD_DECLARATION){
                    MethodDeclaration md = (MethodDeclaration) tree.getAstNode();
                    setValue(md);
                }
            }else {
                parseNonMove(ce,fp);
            }
        }

    }

    public void setValue(MethodDeclaration md){
        methodName.add(md.getName().toString());
        List<SingleVariableDeclaration> params = md.parameters();
        for(SingleVariableDeclaration svd :params){
            svd.getName();
            parameterName.add(svd.getName().toString());
            parameterType.add(svd.getType().toString());
        }
        if(md.getReturnType2()!=null){
            returnType = md.getReturnType2().toString();
        }
    }


    public void parseNonMove(MethodChangeEntity ce,MiningActionData fp){
        Tree tree = (Tree)ce.clusteredActionBean.curAction.getNode();
//        List<String> tempMethodName = new MyList<>();
        List<String> tempParameterType = new MyList<>();
        List<String> tempParameterName = new MyList<>();
        String tempReturn = null;
        if(tree.getAstNode().getNodeType() == ASTNode.METHOD_DECLARATION) {
            if(tree.getTreeSrcOrDst() == ChangeEntityDesc.StageITreeType.SRC_TREE_NODE){
                Tree dstTree = (Tree) fp.getMappedDstOfSrcNode(tree);
                if(dstTree!=null){
                    MethodDeclaration mdDst = (MethodDeclaration) dstTree.getAstNode();
                    methodName.add(mdDst.getName().toString());
                }

            }
            MethodDeclaration md = (MethodDeclaration) tree.getAstNode();
            methodName.add(md.getName().toString());
            List<SingleVariableDeclaration> params = md.parameters();
            for(SingleVariableDeclaration svd :params){
                svd.getName();
                tempParameterName.add(svd.getName().toString());
                tempParameterType.add(svd.getType().toString());
            }
            if(md.getReturnType2()!=null) {
                tempReturn = md.getReturnType2().toString();
            }
        }

        for(Action a:ce.clusteredActionBean.actions){
            Tree t = (Tree) a.getNode();
            if (t.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME
                    || t.getAstNode().getClass().getSimpleName().endsWith("Literal")) {
//                if(tempMethodName.contains(t.getLabel())){
//                    methodName.add(t.getLabel());
//                }
                if(tempParameterName.contains(t.getLabel())){
                    parameterName.add(t.getLabel());
                }
                if(tempParameterType.contains(t.getLabel())){
                    parameterType.add(t.getLabel());
                }
                if(tempReturn!=null &&tempReturn.equals(t.getLabel())){
                    returnType = t.getLabel();
                }
            }
        }
    }

    public List<String> methodName;

    public List<String> parameterType;

    public List<String> parameterName;

    public String returnType;


}
