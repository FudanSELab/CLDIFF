package edu.fdu.se.astdiff.miningchangeentity.member;

import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.EnumDeclaration;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/21.
 *
 */
public class EnumChangeEntity extends MemberPlusChangeEntity{

    public EnumChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    final public static String enumStr = "Enum";

    public List<String> variableList;
    public List<String> methodList;

    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());

//        this.linkBean.methodDeclarations = MiningOperationBeanUtil.getNames(ed.bodyDeclarations());
//        this.linkBean.methodDeclarations.add(ed.getName().toString());

    }

    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
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
