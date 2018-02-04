package edu.fdu.se.astdiff.miningoperationbean.model;


import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/1/22.
 */
public class InitializerChangeEntity extends ChangeEntity{

    public InitializerChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public InitializerChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        InitializerDeclaration iid = (InitializerDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = iid.getRange().get();
        String isStatic = "";
        if(iid.isStatic()){
            isStatic = "static ";
        }
        this.changeEntity = "Initializer";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) +" " + isStatic + this.changeEntity;
    }
    public String staticOrNonStatic;


    @Override
    public String toString(){
        return this.outputDesc;
    }
}
