package edu.fdu.se.astdiff.miningchangeentity;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.associating.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityUtil;
import edu.fdu.se.astdiff.miningchangeentity.statement.ForChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.IfChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.SynchronizedChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.WhileChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 *
 */
public class ChangeEntityPreprocess {

    public ChangeEntityPreprocess(ChangeEntityData ced){
        this.ced = ced;
    }

    public ChangeEntityData ced;


    public void preprocessChangeEntity(){
        this.initContainerEntityData();
        this.printContainerEntityDataBefore();
        this.mergeMoveAndWrapper();
        this.setChangeEntitySub();
        this.printContainerEntityDataAfter();
        this.printNaturalEntityDesc();
    }

    public void mergeMoveAndWrapper() {
        LayeredChangeEntityContainer container = this.ced.entityContainer;
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : container.getLayerMap().entrySet()) {
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            if (bodyDeclarationPair.getBodyDeclaration() instanceof MethodDeclaration) {
                //每个method里面
                List<ChangeEntity> mList = entry.getValue();
                List<ChangeEntity> moveList = new ArrayList<>();
                List<ChangeEntity> stmtWrapperList = new ArrayList<>();
                List<ChangeEntity> deletedMove = new ArrayList<>();
                for (ChangeEntity ce : mList) {
                    int resultCode = ChangeEntityUtil.checkEntityCode(ce);
                    if (resultCode == 1) {
                        moveList.add(ce);
                    } else if (resultCode == 2) {
                        stmtWrapperList.add(ce);
                    }
                }
                for (ChangeEntity ce : stmtWrapperList) {
                    for (ChangeEntity mv : moveList) {
                        if (ChangeEntityUtil.isMoveInWrapper(ced.mad,ce, mv)) {
//                            ChangeEntityUtil.mergeMoveAndWrapper(ce, mv);
                            deletedMove.add(mv);
                            ce.clusteredActionBean.changePacket.getChangeSet2().add(Move.class.getSimpleName());
                        }
                    }
                }
                for (ChangeEntity e : deletedMove) {
                    mList.remove(e);
                }

            }
        }
    }

    public void initContainerEntityData() {
        for (ChangeEntity ce : ced.mad.getChangeEntityList()) {
            if (!ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                ced.entityContainer.addGumTreePlus(ce, ced.mad);
            }
        }
        ced.entityContainer.sortEntityList();
        if (ced.preprocessedData.getPreprocessChangeEntity() != null) {
            for (ChangeEntity ce : ced.preprocessedData.getPreprocessChangeEntity()) {
                ced.entityContainer.addPreDiffChangeEntity(ce);
            }
        }
        ced.preprocessedData.getmBodiesAdded().forEach(a -> ced.addOneBody(a, Insert.class.getSimpleName()));
        ced.preprocessedData.getmBodiesDeleted().forEach(a -> ced.addOneBody(a,Delete.class.getSimpleName()));
    }

    public void printContainerEntityDataBefore() {
        ChangeEntityPrinter.printContainerEntity(ced.entityContainer, ced.preprocessedData.srcCu);
    }

    public void printContainerEntityDataAfter(){
        ChangeEntityPrinter.printContainerEntity(ced.entityContainer, ced.preprocessedData.srcCu);
    }
    public void printNaturalEntityDesc(){
        ChangeEntityPrinter.printContainerEntityNatural(ced.entityContainer, ced.preprocessedData.srcCu);
    }

    public void setChangeEntitySub(){
        ced.mad.getChangeEntityList().forEach(a -> {
            if (!a.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                if(a instanceof ForChangeEntity || a instanceof WhileChangeEntity
                        || a instanceof SynchronizedChangeEntity || a instanceof IfChangeEntity){
//                    a.clusteredActionBean.changePacket.getChangeSet2()
                    if(a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_INSERT)||a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_DELETE)){
                        if(a.clusteredActionBean.changePacket.getChangeSet2().contains(Move.class.getSimpleName())){
                            if(a.clusteredActionBean.changePacket.getChangeSet2().contains(Insert.class.getSimpleName())||
                                    a.clusteredActionBean.changePacket.getChangeSet2().contains(Delete.class.getSimpleName())){
                                a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIIISub.SUB_CONDITION_AND_PARTIAL_BODY);
                            }else{
                                a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIIISub.SUB_CONDITION);
                            }
                        }else{
                            a.stageIIBean.setSubEntity(ChangeEntityDesc.StageIIISub.SUB_CONDITION_AND_BODY);
                        }
                    }
                }
            }
        });
    }





}
