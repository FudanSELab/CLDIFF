package edu.fdu.se.astdiff.associating.linkbean;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.FieldChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 */
public class FieldData extends LinkBean {

    public FieldData(FieldChangeEntity ce){
        if(fieldName==null){
            fieldName = new ArrayList<>();
        }
        FieldDeclaration fd = null;
        switch(ce.stageIIBean.getOpt()) {
            case ChangeEntityDesc.StageIIOpt.OPT_CHANGE:
                Tree t = ce.clusteredActionBean.fafather;
                if (t.getAstNode().getNodeType() == ASTNode.FIELD_DECLARATION) {
                    fd = (FieldDeclaration) t.getAstNode();
                }
                break;
            case ChangeEntityDesc.StageIIOpt.OPT_DELETE:
            case ChangeEntityDesc.StageIIOpt.OPT_INSERT:
                if (ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                    fd = (FieldDeclaration) ce.bodyDeclarationPair.getBodyDeclaration();

                }
                break;

        }
        if(fd!=null){
            List<VariableDeclarationFragment> list = fd.fragments();
            for(VariableDeclarationFragment vd:list){
                fieldName.add(vd.getName().toString());
            }
            fieldType = fd.getType().toString();
        }


    }

    public List<String> fieldName;

    public String fieldType;
}
