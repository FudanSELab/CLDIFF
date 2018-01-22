package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import javassist.compiler.ast.FieldDecl;

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
     * @param fd
     * @param changeType
     */
    public FieldChangeEntity(FieldDeclaration fd,int changeType){
        this.lineRange = fd.getRange().get();
        this.changeEntity = "FieldDeclaration";
        this.changeType = changeType;
    }

}
