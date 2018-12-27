package edu.fdu.se.base.miningchangeentity.member;

import edu.fdu.se.base.links.MyRange;
import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.base.MemberPlusChangeEntity;
import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;
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
    public MyRange dstRange;

    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange1,MyRange myRange2){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange1);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());
        this.dstRange = myRange2;

    }

    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange1){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange1);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());

    }

    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.toString2());
        return sb.toString();
    }
}
