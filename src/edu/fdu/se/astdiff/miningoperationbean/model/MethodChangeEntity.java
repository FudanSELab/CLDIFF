package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.handlefile.Method;

/**
 * Created by huangkaifeng on 2018/1/22.
 */
public class MethodChangeEntity extends ChangeEntity {

    public MethodChangeEntity(ClusteredActionBean bean){
        super(bean);

    }

    public MethodChangeEntity(MethodDeclaration fd, int changeType){
        this.lineRange = fd.getRange().get();
        this.changeEntity = "MethodDeclaration";
        this.changeType = changeType;
    }

}
