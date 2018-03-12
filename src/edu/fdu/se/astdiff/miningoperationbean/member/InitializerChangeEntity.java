package edu.fdu.se.astdiff.miningoperationbean.member;


import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.Initializer;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 */
public class InitializerChangeEntity extends MemberPlusChangeEntity {

    public InitializerChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public InitializerChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType,MyRange myRange){
        Initializer iid = (Initializer) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = myRange;
//        String isStatic = "";
//        if(iid.isStatic()){
//            isStatic = "static ";
//        }
        this.changeEntity = "Initializer";
        this.location = bodyDeclarationPair.getLocationClassString();
        this.changeType = changeType;
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.outputStringList.add("PRE_DIFF");
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.outputStringList.add(this.changeEntity);
        this.linkBean = new LinkBean();

    }
    public String staticOrNonStatic;



}
