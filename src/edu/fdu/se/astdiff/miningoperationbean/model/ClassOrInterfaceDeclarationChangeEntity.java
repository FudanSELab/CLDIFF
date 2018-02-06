package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ClassOrInterfaceDeclarationChangeEntity extends ChangeEntity{
    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ClassOrInterfaceDeclarationChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    /**
     * 预处理 识别的
     */
    public ClassOrInterfaceDeclarationChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration)bodyDeclarationPair.getBodyDeclaration();

        this.lineRange = cod.getRange().get();
        String classOrInterface;
        if(cod.isInterface()){
            classOrInterface = "Interface";
        }else{
            classOrInterface = "Class";
        }
        String isStatic = "";
        if(cod.isStatic()){
            isStatic = "static ";
        }
        this.changeEntity = classOrInterface;
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) +ChangeEntity.SPLITTER + isStatic +classOrInterface + ChangeEntity.SPLITTER + cod.getNameAsString();
    }

    @Override
    public String toString(){
        return this.outputDesc;
    }


}
