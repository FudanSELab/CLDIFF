package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.link.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperationBeanUtil;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ClassOrInterfaceDeclarationChangeEntity extends MemberPlusChangeEntity {



    /**
     * 预处理 识别的
     */
    public ClassOrInterfaceDeclarationChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        TypeDeclaration cod = (TypeDeclaration)bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        if(cod.isInterface()){
            this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INTERFACE);
        }else{
            this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INNER_CLASS);
        }
        this.stageIIBean.setThumbnail(cod.getName().toString());
        this.linkBean.methodDeclarations = MiningOperationBeanUtil.getNames(cod.bodyDeclarations());
        this.linkBean.methodDeclarations.add(cod.getName().toString());
    }

    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ClassOrInterfaceDeclarationChangeEntity(ClusteredActionBean bean){
        super(bean);
    }






}
