package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import edu.fdu.se.javaparser.JDTParserUtil;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.HashSet;

/**
 * Created by huangkaifeng on 2018/1/22.
 */
public class MethodChangeEntity extends MemberPlusChangeEntity {

    public MethodChangeEntity(ClusteredActionBean bean){
        super(bean);
        this.lineRange = bean.range;
        this.changeEntity = "Method - AnonymousClass";
        this.changeType = bean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.SPLITTER + this.changeEntity +ChangeEntity.SPLITTER;


    }

    public MethodChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        MethodDeclaration md =(MethodDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = myRange;
        this.changeEntity = "Method";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.outputStringList.add("PRE_DIFF");
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.outputStringList.add(this.changeEntity);
        this.outputStringList.add(JDTParserUtil.getDeclarationAsString(md));
        this.linkBean = new LinkBean();
        this.linkBean.methodDeclarations = new HashSet<>();
        this.linkBean.methodDeclarations.add(md.getName().toString());
    }



}
