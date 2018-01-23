package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class ConstructorChangeEntity extends ChangeEntity {

    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ConstructorChangeEntity(ClusteredActionBean bean){
        super(bean);
        Range a;
    }

    /**
     * 预处理 识别的
     * @param fd
     * @param changeType
     */
    public ConstructorChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        ConstructorDeclaration cd = (ConstructorDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = cd.getRange().get();
        this.changeEntity = "ConstructorDeclaration";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();
    }
}
