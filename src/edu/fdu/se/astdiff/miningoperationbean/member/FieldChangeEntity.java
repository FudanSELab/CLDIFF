package edu.fdu.se.astdiff.miningoperationbean.member;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

import java.util.HashSet;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class FieldChangeEntity extends MemberPlusChangeEntity {
    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean){
        super(bean);
        this.lineRange = bean.nodeLinePosition;
        this.changeEntity = "Field - AnonymousClass";
        this.changeType = bean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.SPLITTER + this.changeEntity +ChangeEntity.SPLITTER;
    }


    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType){
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.lineRange = fd.getRange().get();
        this.location = fieldDeclarationPair.getLocationClassString();
        this.changeEntity = "Field";
        this.changeType = changeType;
//        String isStatic = "";
//        if(fd.isStatic()){
//            isStatic = "static ";
//        }
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.outputStringList.add("PRE_DIFF");
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.outputStringList.add(this.changeEntity);
        NodeList<VariableDeclarator> list = fd.getVariables();
        this.outputStringList.add(fieldDeclarationPair.getLocationClassString() + list.toString());

        this.linkBean = new LinkBean();
        this.linkBean.variables = new HashSet<>();
        for(VariableDeclarator vd:fd.getVariables()){
            this.linkBean.variables.add(vd.getNameAsString());
        }
    }





}
