package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import edu.fdu.se.handlefile.Method;

/**
 * Created by huangkaifeng on 2018/1/22.
 */
public class MethodChangeEntity extends ChangeEntity {

    public MethodChangeEntity(ClusteredActionBean bean){
        super(bean);

    }

    public MethodChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        MethodDeclaration md =(MethodDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = md.getRange().get();
        this.changeEntity = "Method";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        String isStatic = "";
        if(md.isStatic()){
            isStatic = "static ";
        }
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) +" " + isStatic + this.changeEntity+" "+md.getDeclarationAsString();
    }

    @Override
    public String toString(){
        return this.outputDesc;
    }

}
