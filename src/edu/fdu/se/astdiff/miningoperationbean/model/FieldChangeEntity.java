package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.Variable;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class FieldChangeEntity extends ChangeEntity {
    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean){
        super(bean);
    }


    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType){
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.lineRange = fd.getRange().get();
        this.location = fieldDeclarationPair.getLocationClassString();
        this.changeEntity = "Field";
        this.changeType = changeType;
        String isStatic = "";
        if(fd.isStatic()){
            isStatic = "static ";
        }
        NodeList list = fd.getVariables();

        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) +" " + isStatic + this.changeEntity+" "+list.toString();
    }



    @Override
    public String toString(){
        return this.outputDesc;
    }

}
