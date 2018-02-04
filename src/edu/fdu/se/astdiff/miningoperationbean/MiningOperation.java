package edu.fdu.se.astdiff.miningoperationbean;

import com.github.javaparser.ast.body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.model.*;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class MiningOperation {

    private PreprocessingData preprocessingData;

    private List<ChangeEntity> mChangeEntityAll;


    public MiningOperation(PreprocessingData pd){
        this.preprocessingData = pd;
        this.mChangeEntityAll = new ArrayList<>();
        initPreprocessChangeEntityList();
        initDiffMinerChangeEntityList();
    }

    public MiningOperation(){
    }

    public void initDiffMinerChangeEntityList(){

    }




    public void initPreprocessChangeEntityList(){
        for(BodyDeclarationPair item:this.preprocessingData.getmBodiesAdded()){
            addOneBody(item,OperationTypeConstants.INSERT);
        }
        for(BodyDeclarationPair item:this.preprocessingData.getmBodiesDeleted()){
            addOneBody(item,OperationTypeConstants.DELETE);
        }
    }

    public void addOneBody(BodyDeclarationPair item,int type){
        ChangeEntity ce = null;
        if(item.getBodyDeclaration() instanceof FieldDeclaration){
            ce = new FieldChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof MethodDeclaration){
            ce = new MethodChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof InitializerDeclaration){
            ce = new InitializerChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof ConstructorDeclaration){
            ce = new ConstructorChangeEntity(item,type);
        }else if(item.getBodyDeclaration() instanceof ClassOrInterfaceDeclaration){
            ce = new ClassOrInterfaceDeclarationChangeEntity(item,type);
        }
        if(ce!=null){
            this.mChangeEntityAll.add(ce);
        }
    }


    public void printHighLevelOperationBeanList(MiningActionData fp) {
        List<ChangeEntity> mChangeEntityList =  fp.getChangeEntityList();
        mChangeEntityList.forEach(a -> System.out.print(a.toString()));
    }


    public void printHighLevelOperationBeanList() {
        List<ChangeEntity> mChangeEntityList =  this.mChangeEntityAll;
        mChangeEntityList.forEach(a -> System.out.println(a.toString()));
    }


}
