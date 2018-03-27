package edu.fdu.se.astdiff.miningoperationbean.member;


import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
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
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        Initializer iid = (Initializer) bodyDeclarationPair.getBodyDeclaration();
        this.changeEntity = "Initializer";
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.stageIIOutput.add("PRE_DIFF");
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.stageIIOutput.add(this.changeEntity);
        this.linkBean = new LinkBean();

    }
    public String staticOrNonStatic;



}
