package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.link.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.EnumDeclaration;

/**
 * Created by huangkaifeng on 2018/3/21.
 *
 */
public class EnumChangeEntity extends MemberPlusChangeEntity{

    public EnumChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    final public static String enumStr = "Enum";


    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());

//        this.linkBean.methodDeclarations = MiningOperationBeanUtil.getNames(ed.bodyDeclarations());
//        this.linkBean.methodDeclarations.add(ed.getName().toString());

    }
}
