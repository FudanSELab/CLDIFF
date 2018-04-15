package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.associating.linkbean.*;
import edu.fdu.se.astdiff.miningactions.util.MyList;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.member.ClassChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.member.FieldChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.member.MethodChangeEntity;
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



    private ChangeEntityData changeEntityData;

    public AssociationGenerator(ChangeEntityData mod){
        this.changeEntityData = mod;
        this.changeEntityData.mAssociations = new MyList<>();
    }

    /**
     * main entrance
     */
    public void generate() {
        LayeredChangeEntityContainer container = this.changeEntityData.entityContainer;
        List<ChangeEntity> entities = this.changeEntityData.mad.getChangeEntityList();
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
                        if(ce1 instanceof StatementPlusChangeEntity && ce2 instanceof StatementPlusChangeEntity) {
                            LinkStatement2Statement.checkStmtAssociation(changeEntityData, ce1, ce2);// stmt 与stmt之间
                        }
                    }
                }
            }
            if(key.getBodyDeclaration() instanceof TypeDeclaration){
                List<ChangeEntity> mList = entry.getValue();
                for(ChangeEntity tmp:mList){
                    if(tmp instanceof MethodChangeEntity){
                        methodChangeEntity.add(tmp);
                    }
                    if(tmp instanceof ClassChangeEntity){
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
                LinkMember2Member.checkMethodAssociation(changeEntityData,ce1, ce2);// method与method之间
            }
        }
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : mMap.entrySet()) {
            BodyDeclarationPair key = entry.getKey();
            if (key.getBodyDeclaration() instanceof MethodDeclaration) {
                List<ChangeEntity> mList = entry.getValue();
                for (ChangeEntity ce:mList){
                    if(! (ce instanceof StatementPlusChangeEntity)){
                        continue;
                    }
                    fieldChangeEntity.forEach(a->{
                        LinkStatement2Member.checkStmtFieldAssociation(changeEntityData,ce,(FieldChangeEntity) a);
                    });
                    methodChangeEntity.forEach(a->{
                        LinkStatement2Member.checkStmtMethodAssociation(changeEntityData,ce,(MethodChangeEntity)a);

                    });
                }
            }
        }

    }

    public void initLinkBean(ChangeEntity ce){

        if(ce instanceof ClassChangeEntity){
            ce.linkBean = new ClassData((ClassChangeEntity)ce);
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


    public void printAssociations(){
        this.changeEntityData.mAssociations.forEach(a->{
            System.out.println(a.toString());
        });
    }

}