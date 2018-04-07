package edu.fdu.se.astdiff.link;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.humanreadableoutput.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.link.linkbean.*;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.member.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.member.FieldChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.member.MethodChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/3/19.
 *
 */
public class AssociationGenerator {

    private List<Association> mAssociations;

    public AssociationGenerator(){
        mAssociations = new ArrayList<>();
    }

    /**
     * main entrance
     * @param container
     */
    public void generate(LayeredChangeEntityContainer container) {
        List<ChangeEntity> entities = container.getmChangeEntityAll();
        entities.forEach(this::initLinkBean);
        Map<BodyDeclarationPair, List<ChangeEntity>> mMap = container.getLayerMap();
        List<ChangeEntity> methodChangeEntity = new ArrayList<>();
        List<ChangeEntity> fieldChangeEntity = new ArrayList<>();
        List<ChangeEntity> innerClassChangeEntity = new ArrayList<>();
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : mMap.entrySet()) {
            BodyDeclarationPair key = entry.getKey();
            if (key.getBodyDeclaration() instanceof MethodDeclaration) {
                List<ChangeEntity> mList = entry.getValue();
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = i + 1; j < mList.size(); j++) {
                        ChangeEntity ce1 = mList.get(i);
                        ChangeEntity ce2 = mList.get(j);
                        LinkStatement2Statement.checkStmtAssociation(ce1, ce2);// stmt 与stmt之间
                    }
                }
            }
            if(key.getBodyDeclaration() instanceof TypeDeclaration){
                List<ChangeEntity> mList = entry.getValue();
                for(ChangeEntity tmp:mList){
                    if(tmp instanceof MethodChangeEntity){
                        methodChangeEntity.add(tmp);
                    }
                    if(tmp instanceof ClassOrInterfaceDeclarationChangeEntity){
                        innerClassChangeEntity.add(tmp);
                    }
                    if(tmp instanceof FieldChangeEntity){
                        fieldChangeEntity.add(tmp);
                    }
                }
            }
        }
        for (int i = 0; i < methodChangeEntity.size(); i++) {
            for (int j = i + 1; j < methodChangeEntity.size(); j++) {
                ChangeEntity ce1 = methodChangeEntity.get(i);
                ChangeEntity ce2 = methodChangeEntity.get(j);
                LinkMember2Member.checkMethodAssociation(ce1, ce2);// method与method之间
            }
        }
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : mMap.entrySet()) {
            BodyDeclarationPair key = entry.getKey();
            if (key.getBodyDeclaration() instanceof MethodDeclaration) {
                List<ChangeEntity> mList = entry.getValue();
                for (int i = 0; i < mList.size(); i++) {
                    LinkStatement2Member.checkStmtMethodAssociation();
                    LinkStatement2Member.checkStmtFieldAssociation();
                }
            }
        }

    }

    public void initLinkBean(ChangeEntity ce){
        if(ce instanceof ClassOrInterfaceDeclarationChangeEntity){
            ce.linkBean = new ClassData((ClassOrInterfaceDeclarationChangeEntity)ce);
        }else if(ce instanceof FieldChangeEntity){
            ce.linkBean = new FieldData((FieldChangeEntity)ce);
        }else if(ce instanceof MethodChangeEntity){
            ce.linkBean = new MethodData((MethodChangeEntity)ce);
        }else if(ce instanceof StatementPlusChangeEntity){
            ce.linkBean = new StmtData((StatementPlusChangeEntity)ce);
        }else{
            System.err.println("[ERR]not included");
        }

    }

// sub的问题
    // Link的问题
    // dataset的问题
    // User study的问题
}
