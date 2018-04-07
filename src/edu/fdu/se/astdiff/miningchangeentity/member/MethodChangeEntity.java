package edu.fdu.se.astdiff.miningchangeentity.member;

import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 */
public class MethodChangeEntity extends MemberPlusChangeEntity {

    public MethodChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public MethodChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        MethodDeclaration md =(MethodDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_METHOD);
        this.stageIIBean.setThumbnail(md.getName().toString());
        this.bodyDeclarationPair = bodyDeclarationPair;
    }

    @Override
    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.stageIIBean.getOpt());
        sb.append(" ");
        sb.append(this.stageIIBean.getChangeEntity());
        if(this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_CHANGE)){
            sb.append(" ");
            sb.append(this.stageIIBean.getSubEntity());
            sb.append("with/by...");
        }
        return sb.toString();
    }



}
