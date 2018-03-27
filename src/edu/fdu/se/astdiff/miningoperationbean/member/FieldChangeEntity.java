package edu.fdu.se.astdiff.miningoperationbean.member;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

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
        if(bean.curAction instanceof Move){
            this.linkBean = new LinkBean(bean.curAction);
        }else {
            this.linkBean = new LinkBean(bean.actions);
        }
    }


    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType,MyRange myRange){
        super(fieldDeclarationPair.getLocationClassString(),changeType,myRange);
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.changeEntity = "Field";
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.stageIIOutput.add("PRE_DIFF");
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(changeType));
        this.stageIIOutput.add(this.changeEntity);
        List<VariableDeclarationFragment> list = fd.fragments();
        this.linkBean = new LinkBean();
        String res = "";
        for(VariableDeclarationFragment vd:list){
            res += vd+",";
            this.linkBean.variables.add(vd.getName().toString());
        }
        this.stageIIOutput.add(fieldDeclarationPair.getLocationClassString() + res);

    }





}
