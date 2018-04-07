package edu.fdu.se.astdiff.link;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.humanreadableoutput.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Created by huangkaifeng on 2018/3/19.
 *
 */
public class AssociationGenerator {

    private List<Association> mAssociations;

    public AssociationGenerator(){
        mAssociations = new ArrayList<>();
    }

    public void generate(LayeredChangeEntityContainer container) {
        Map<BodyDeclarationPair, List<ChangeEntity>> mMap = container.getLayerMap();
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : mMap.entrySet()) {
            // 方法先整理
            BodyDeclarationPair key = entry.getKey();
            if (key.getBodyDeclaration() instanceof MethodDeclaration) {
                List<ChangeEntity> mList = entry.getValue();
                //初始化
                for (ChangeEntity ce : mList) {
                    initLinkBean(ce);
                }
                //之后建立connection
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = i + 1; j < mList.size(); j++) {
                        ChangeEntity ce1 = mList.get(i);
                        ChangeEntity ce2 = mList.get(j);
                        checkAssociation(ce1, ce2);// 方法内部
                    }
                }


            }
        }
        List<ChangeEntity> methodSignatureChange = new ArrayList<>();
        List<ChangeEntity> insertDeleteMethod = new ArrayList<>();
        for(ChangeEntity changeEntity:container.getmChangeEntityAll()){
            if(changeEntity.stageIIBean.getChangeEntity().equals(ChangeEntityDesc.StageIIENTITY.ENTITY_METHOD)){
                if(changeEntity.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_CHANGE)){
                    methodSignatureChange.add(changeEntity);
                }else{
                    insertDeleteMethod.add(changeEntity);
                }
            }
        }
    }

    public void initLinkBean(ChangeEntity ce){
        if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
            ce.linkBean = new LinkBean();
        }else{
            if(ce.clusteredActionBean.curAction==null){
                ce.linkBean = new LinkBean(ce.clusteredActionBean.fafather);
            } else if(ce.clusteredActionBean.curAction instanceof Move){
                ce.linkBean = new LinkBean(ce.clusteredActionBean.curAction);
            }else {
                ce.linkBean = new LinkBean(ce.clusteredActionBean.actions);
            }
        }


    }

    public void checkAssociation(ChangeEntity ce1,ChangeEntity ce2){
        // check variable && control
    }

}
