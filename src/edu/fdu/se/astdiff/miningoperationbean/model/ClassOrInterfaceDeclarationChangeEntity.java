package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.IfStatement;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class ClassOrInterfaceDeclarationChangeEntity extends ChangeEntity{
    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ClassOrInterfaceDeclarationChangeEntity(ClusteredActionBean bean){
        super(bean);
        Range a;
    }

    /**
     * 预处理 识别的
     */
    public ClassOrInterfaceDeclarationChangeEntity(BodyDeclarationPair bodyDeclarationPair, int changeType){
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration)bodyDeclarationPair.getBodyDeclaration();
        this.lineRange = cod.getRange().get();
        this.changeEntity = "ClassOrInterfaceDeclaration";
        this.changeType = changeType;
        this.location = bodyDeclarationPair.getLocationClassString();

    }
}
