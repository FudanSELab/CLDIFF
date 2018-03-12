package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.HashSet;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/16.
 *
 */
public class FieldChangeEntity extends MemberPlusChangeEntity {
    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean){
        super(bean);
        this.lineRange = bean.range;
        this.changeEntity = "Field - AnonymousClass";
        this.changeType = bean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.SPLITTER + this.changeEntity +ChangeEntity.SPLITTER;
    }


    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType,MyRange myRange){
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.lineRange = myRange;
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
        List<VariableDeclarationFragment> list = fd.fragments();
        this.linkBean = new LinkBean();
        this.linkBean.variables = new HashSet<>();
        String res = "";
        for(VariableDeclarationFragment vd:list){
            res += vd+",";
            this.linkBean.variables.add(vd.getName().toString());
        }
        this.outputStringList.add(fieldDeclarationPair.getLocationClassString() + res);

    }





}
