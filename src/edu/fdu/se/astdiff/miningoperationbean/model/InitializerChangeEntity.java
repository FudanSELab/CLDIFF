package edu.fdu.se.astdiff.miningoperationbean.model;


import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/22.
 */
public class InitializerChangeEntity extends ChangeEntity{

    public InitializerChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public InitializerChangeEntity(InitializerDeclaration fd, int changeType){
        this.lineRange = fd.getRange().get();
        this.changeEntity = "InitializerDeclaration";
        this.changeType = changeType;
    }
}
