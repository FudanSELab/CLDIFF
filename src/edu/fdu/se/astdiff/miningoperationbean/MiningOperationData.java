package edu.fdu.se.astdiff.miningoperationbean;

import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.humanreadableoutput.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningoperationbean.member.*;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessedData;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class MiningOperationData {

    private PreprocessedData preprocessedData;

    public List<ChangeEntity> mChangeEntityAll;


    private LayeredChangeEntityContainer entityContainer;


    private MiningActionData mad;


    public MiningOperationData(PreprocessedData pd, MiningActionData mad){
        this.preprocessedData = pd;
        this.mChangeEntityAll = mad.getChangeEntityList();
        this.entityContainer = new LayeredChangeEntityContainer();
        this.mad = mad;
    }




    public void initContainerEntityData(){
        for(ChangeEntity ce : this.mChangeEntityAll) {
            if(ce.entityGeneratedStage != ChangeEntity.STAGE_PREDIFF) {
                this.entityContainer.addGumTreePlus(ce, this.mad);
            }
        }
        this.preprocessedData.getmBodiesAdded().forEach(a-> addOneBody(a,OperationTypeConstants.INSERT));
        this.preprocessedData.getmBodiesDeleted().forEach(a-> addOneBody(a,OperationTypeConstants.DELETE));
    }

    private void addOneBody(BodyDeclarationPair item,int type){
        ChangeEntity ce = null;
        int s;
        int e;
        MyRange myRange = null;
        if(OperationTypeConstants.INSERT == type){
            s = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition()+item.getBodyDeclaration().getLength()-1);
            myRange = new MyRange(s,e,type);
        }else if(OperationTypeConstants.DELETE == type){
            s = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition()+item.getBodyDeclaration().getLength()-1);
            myRange = new MyRange(s,e,type);
        }
        if(item.getBodyDeclaration() instanceof FieldDeclaration){
            ce = new FieldChangeEntity(item,type,myRange);
        }else if(item.getBodyDeclaration() instanceof MethodDeclaration){
            ce = new MethodChangeEntity(item,type,myRange);
        }else if(item.getBodyDeclaration() instanceof Initializer){
            ce = new InitializerChangeEntity(item,type,myRange);
        }else if(item.getBodyDeclaration() instanceof TypeDeclaration){
            ce = new ClassOrInterfaceDeclarationChangeEntity(item,type,myRange);
        }else if(item.getBodyDeclaration() instanceof EnumDeclaration){
            ce = new EnumDeclarationEntity(item,type,myRange);
        }
        if(ce!=null){
            this.entityContainer.addPreDiffChangeEntity(ce);
        }

    }


    public void printStage1ChangeEntity(){
        this.mChangeEntityAll.forEach(a->{
            if(a.entityGeneratedStage!=ChangeEntity.STAGE_PREDIFF){
                System.out.println(a.toString());
            }
        });
    }


}
