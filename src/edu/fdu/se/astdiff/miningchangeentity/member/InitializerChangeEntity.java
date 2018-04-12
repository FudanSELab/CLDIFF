package edu.fdu.se.astdiff.miningchangeentity.member;


import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.Initializer;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 */
public class InitializerChangeEntity extends MemberPlusChangeEntity {

    public InitializerChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public InitializerChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType,MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        Initializer iid = (Initializer) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INITIALIZER);
        this.stageIIBean.setThumbnail("{}");

    }


    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.getOpt());
        sb.append(" ");
        sb.append(this.stageIIBean.getChangeEntity());
        if(this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            sb.append(" ");
            sb.append(this.stageIIBean.getSubEntity());
            sb.append("with/by...");
        }
        return sb.toString();
    }



}
