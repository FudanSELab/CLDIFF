package edu.fdu.se.astdiff.miningoperationbean;

import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.member.*;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessedData;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class MiningOperation {

    private PreprocessedData preprocessedData;

    private List<ChangeEntity> mChangeEntityAll;

    private MiningActionData mad;
    public MiningOperation(PreprocessedData pd, MiningActionData mad){
        this.preprocessedData = pd;
        this.mChangeEntityAll = new ArrayList<>();
        initPreprocessChangeEntityList();
        initDiffMinerChangeEntityList();
        this.mad = mad;
    }


    public void initDiffMinerChangeEntityList(){

    }




    public void initPreprocessChangeEntityList(){
        this.preprocessedData.findMethodNameChange();
        for(BodyDeclarationPair item:this.preprocessedData.getmBodiesAdded()){
            addOneBody(item,OperationTypeConstants.INSERT);
        }
        for(BodyDeclarationPair item:this.preprocessedData.getmBodiesDeleted()){
            addOneBody(item,OperationTypeConstants.DELETE);
        }
    }

    public void addOneBody(BodyDeclarationPair item,int type){
        ChangeEntity ce = null;
        if(item.getBodyDeclaration() instanceof FieldDeclaration){
            ce = new FieldChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof MethodDeclaration){
            ce = new MethodChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof Initializer){
            ce = new InitializerChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof TypeDeclaration){
            ce = new ClassOrInterfaceDeclarationChangeEntity(item,type);
        }
        if(ce!=null){
            this.mChangeEntityAll.add(ce);
        }
    }


    public void printListDiffMiner() {
        List<ChangeEntity> mChangeEntityList =  this.mad.getChangeEntityList();
        mChangeEntityList.forEach(a -> System.out.println(a.toString()));
    }


    public void printListPreprocess() {
        List<ChangeEntity> mChangeEntityList =  this.mChangeEntityAll;
        mChangeEntityList.forEach(a -> System.out.println(a.toString()));
    }


}
