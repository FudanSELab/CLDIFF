package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.ast.body.*;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningactions.MiningActionData;
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
            addOneBody(item,OperationTypeConstants.INSERT_BODYDECLARATION);
        }
        for(BodyDeclarationPair item:this.preprocessingData.getmBodiesDeleted()){
            addOneBody(item,OperationTypeConstants.DELETE_BODYDECLARATION);
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
        List<ClusteredActionBean> mHighLevelOperationBeanList =  fp.getmHighLevelOperationBeanList();
        if (mHighLevelOperationBeanList.isEmpty()) {
            System.out.println("HighLevelOperationBeanList is null!");
        }else {
            for (ClusteredActionBean operationBean : mHighLevelOperationBeanList) {
                TreeContext treeContext;
                if (operationBean.curAction instanceof Insert) {
                    treeContext = fp.getDstTree();
                } else {
                    treeContext = fp.getSrcTree();
                }
                String nextAction = ConsolePrint.getMyOneActionString(operationBean.curAction, 0, treeContext);
                System.out.print(nextAction);
                System.out.println(operationBean.toString()+"\n");
            }
        }
    }

}
