package edu.fdu.se.astdiff.miningoperationbean;

import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.container.LayeredChangeEntityContainer;
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
public class MiningOperation {

    private PreprocessedData preprocessedData;

    private List<ChangeEntity> mChangeEntityPreDiff;

    private List<ChangeEntity> mChangeEntityGumTreePlus;

    private LayeredChangeEntityContainer entityContainer;


    private MiningActionData mad;
    public MiningOperation(PreprocessedData pd, MiningActionData mad){
        this.preprocessedData = pd;
        this.mChangeEntityPreDiff = new ArrayList<>();
        this.mChangeEntityGumTreePlus = mad.getChangeEntityList();
        this.entityContainer = new LayeredChangeEntityContainer();
        this.mad = mad;
        //todo check logically
        initPreprocessChangeEntityList();
        initDiffMinerChangeEntityList();
    }


    public void initDiffMinerChangeEntityList(){
        for(ChangeEntity ce : this.mChangeEntityGumTreePlus) {
            this.entityContainer.addGumTreePlus(ce,this.mad);
        }
    }


    public void initPreprocessChangeEntityList(){
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
            this.mChangeEntityPreDiff.add(ce);
        }
        this.entityContainer.addPreDiffChangeEntity(ce,item,type);
    }



    public void printListDiffMiner() {
        List<ChangeEntity> mChangeEntityList =  this.mChangeEntityGumTreePlus;
        mChangeEntityList.forEach(a -> System.out.println(a.toString()));
    }


    public void printListPreprocess() {
        List<ChangeEntity> mChangeEntityList =  this.mChangeEntityPreDiff;
        mChangeEntityList.forEach(a -> System.out.println(a.toString()));
    }


}
