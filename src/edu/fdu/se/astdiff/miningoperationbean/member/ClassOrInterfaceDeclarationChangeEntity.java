package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperationBeanUtil;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ClassOrInterfaceDeclarationChangeEntity extends MemberPlusChangeEntity {
    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ClassOrInterfaceDeclarationChangeEntity(ClusteredActionBean bean){
        super(bean);
    }
    final public static String CLASS_STR = "Class";
    final public static String INTERFACE_STR = "Interface";
    final public static String CLASS_SIGNATURE = "class signature";

    /**
     * 预处理 识别的
     */
    public ClassOrInterfaceDeclarationChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        TypeDeclaration cod = (TypeDeclaration)bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = myRange;
        this.location = bodyDeclarationPair.getLocationClassString();
        if(cod.isInterface()){
            this.changeEntity = INTERFACE_STR;
        }else{
            this.changeEntity = CLASS_STR;
        }
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.outputStringList.add("PRE_DIFF");
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.outputStringList.add(this.changeEntity);
        this.outputStringList.add(cod.getName().toString());
        this.linkBean = new LinkBean();
        this.linkBean.methodDeclarations = MiningOperationBeanUtil.getNames(cod.bodyDeclarations());
        this.linkBean.methodDeclarations.add(cod.getName().toString());
    }







}
