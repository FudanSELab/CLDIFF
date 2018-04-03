package edu.fdu.se.astdiff.miningoperationbean.member;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.link.LinkBean;
import edu.fdu.se.astdiff.link.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/16.
 *
 */
public class FieldChangeEntity extends MemberPlusChangeEntity {



    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType,MyRange myRange){
        super(fieldDeclarationPair.getLocationClassString(),changeType,myRange);
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(fieldDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_FIELD);

        List<VariableDeclarationFragment> list = fd.fragments();
        String res = "";
        for(VariableDeclarationFragment vd:list){
            res += vd+",";
            this.linkBean.variables.add(vd.getName().toString());
        }
        this.stageIIBean.setThumbnail(fieldDeclarationPair.getLocationClassString() + res);

    }

    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean){
        super(bean);
        if(bean.curAction instanceof Move){
            this.linkBean = new LinkBean(bean.curAction);
        }else {
            this.linkBean = new LinkBean(bean.actions);
        }
    }





}
