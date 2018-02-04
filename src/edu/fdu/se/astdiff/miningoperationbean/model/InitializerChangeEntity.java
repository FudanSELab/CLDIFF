package edu.fdu.se.astdiff.miningoperationbean.model;


import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
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
        this.changeEntity = "InitializerDeclaration";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
    }
    public String staticOrNonStatic;
}
