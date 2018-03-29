package edu.fdu.se.astdiff.miningoperationbean.member;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 */
public class MethodChangeEntity extends MemberPlusChangeEntity {

    public MethodChangeEntity(ClusteredActionBean bean){
        super(bean);
        this.lineRange = bean.range;
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_METHOD);
        this.stageIIBean.setOpt(OperationTypeConstants.getKeyNameByValue(bean.changePacket.getOperationType()));
        if(bean.curAction instanceof Move){
            this.linkBean = new LinkBean(bean.curAction);
        }else {
            this.linkBean = new LinkBean(bean.actions);
        }
    }

    public MethodChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        MethodDeclaration md =(MethodDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_METHOD);
        this.stageIIBean.setThumbnail(md.getName().toString());
        this.linkBean.methodDeclarations.add(md.getName().toString());
    }



}
