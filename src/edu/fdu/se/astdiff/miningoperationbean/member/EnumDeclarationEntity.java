package edu.fdu.se.astdiff.miningoperationbean.member;

import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.EnumDeclaration;

/**
 * Created by huangkaifeng on 2018/3/21.
 *
 */
public class EnumDeclarationEntity extends MemberPlusChangeEntity{

    public EnumDeclarationEntity(ClusteredActionBean bean){
        super(bean);
    }

    final public static String enumStr = "Enum";


    public EnumDeclarationEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = myRange;
        this.changeEntity = enumStr;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.stageIIOutput.add("PRE_DIFF");
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.stageIIOutput.add(this.changeEntity);
        this.stageIIOutput.add(ed.getName().toString());
        this.linkBean = new LinkBean();
//        this.linkBean.methodDeclarations = MiningOperationBeanUtil.getNames(ed.bodyDeclarations());
//        this.linkBean.methodDeclarations.add(ed.getName().toString());

    }
}
