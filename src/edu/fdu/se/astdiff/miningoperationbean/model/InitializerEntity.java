package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/1/30.
 */
public class InitializerEntity extends ChangeEntity {

    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public InitializerEntity(ClusteredActionBean bean){
        super(bean);
    }

    /**
     * 预处理 识别的
     */
    public InitializerEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        InitializerDeclaration cod = (InitializerDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = cod.getRange().get();
        this.changeEntity = "Initializer";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();

    }

    @Override
    public String toString(){
        return this.outputDesc;
    }
}
