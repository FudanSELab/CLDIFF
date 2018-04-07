package edu.fdu.se.astdiff.miningchangeentity;

import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.associating.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.*;
import edu.fdu.se.astdiff.miningchangeentity.statement.ForChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.SynchronizedChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.WhileChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ChangeEntityData {

    public PreprocessedData preprocessedData;

    public List<ChangeEntity> mChangeEntityAll;


    public LayeredChangeEntityContainer entityContainer;


    private MiningActionData mad;


    public ChangeEntityData(PreprocessedData pd, MiningActionData mad) {
        this.preprocessedData = pd;
        this.mChangeEntityAll = mad.getChangeEntityList();
        this.entityContainer = pd.entityContainer;
        this.mad = mad;
    }


    public void mergeMoveAndWrapper() {
        this.entityContainer.mergeMoveAndWrapper();
    }

    public void initContainerEntityData() {
        for (ChangeEntity ce : this.mChangeEntityAll) {
            if (!ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                this.entityContainer.addGumTreePlus(ce, this.mad);
            }
        }
        this.entityContainer.sortEntityList();
        if (preprocessedData.getPreprocessChangeEntity() != null) {
            for (ChangeEntity ce : preprocessedData.getPreprocessChangeEntity()) {
                this.entityContainer.addPreDiffChangeEntity(ce);
            }
        }
        this.preprocessedData.getmBodiesAdded().forEach(a -> addOneBody(a, OperationTypeConstants.INSERT));
        this.preprocessedData.getmBodiesDeleted().forEach(a -> addOneBody(a, OperationTypeConstants.DELETE));
    }

    public void printContainerEntityDataBefore() {
        this.entityContainer.printContainerEntityBeforeSorting(this.preprocessedData.srcCu);
    }


    public void printContainerEntityDataAfter(){
        this.entityContainer.printContainerEntityAfterSorting(this.preprocessedData.srcCu);
    }



    private void addOneBody(BodyDeclarationPair item, int type) {
        ChangeEntity ce = null;
        int s;
        int e;
        MyRange myRange = null;
        if (OperationTypeConstants.INSERT == type) {
            s = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition() + item.getBodyDeclaration().getLength() - 1);
            myRange = new MyRange(s, e, type);
        } else if (OperationTypeConstants.DELETE == type) {
            s = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition() + item.getBodyDeclaration().getLength() - 1);
            myRange = new MyRange(s, e, type);
        }
        if (item.getBodyDeclaration() instanceof FieldDeclaration) {
            ce = new FieldChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof MethodDeclaration) {
            ce = new MethodChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof Initializer) {
            ce = new InitializerChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof TypeDeclaration) {
            ce = new ClassOrInterfaceDeclarationChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof EnumDeclaration) {
            ce = new EnumChangeEntity(item, type, myRange);
        }
        if (ce != null) {
            this.entityContainer.addPreDiffChangeEntity(ce);
        }

    }


    public void printStage1ChangeEntity() {
        this.mChangeEntityAll.forEach(a -> {
//            if (!a.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                System.out.println(a.toString());
//            }
        });
    }

    public void preprocessChangeEntity(){
        this.initContainerEntityData();
        this.printContainerEntityDataBefore();
        this.mergeMoveAndWrapper();
        this.setChangeEntitySub();

    }

    public void setChangeEntitySub(){
        this.mChangeEntityAll.forEach(a -> {
            if (!a.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                if(a instanceof ForChangeEntity || a instanceof WhileChangeEntity
                        || a instanceof SynchronizedChangeEntity ){
//                    a.clusteredActionBean.changePacket.getChangeSet2()
                }
            }
        });
    }


}
