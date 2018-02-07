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
    final public static String CLASS_STR = "Class";
    final public static String INTERFACE_STR = "Interface";

    /**
     * 预处理 识别的
     */
    public ClassOrInterfaceDeclarationChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        super();
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration)bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = cod.getRange().get();
        if(cod.isInterface()){
            this.changeEntity = INTERFACE_STR;
        }else{
            this.changeEntity = CLASS_STR;
        }
//        String isStatic = "";
//        if(cod.isStatic()){
//            isStatic = "static ";
//        }
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(OperationTypeConstants.ENTITY_MEMBER));
        this.outputStringList.add("PRE_DIFF");
        this.outputStringList.add(OperationTypeConstants.getKeyNameByValue(changeType));
//        this.outputStringList.add(isStatic);
        this.outputStringList.add(this.changeEntity);
        this.outputStringList.add(cod.getNameAsString());
    }




}
