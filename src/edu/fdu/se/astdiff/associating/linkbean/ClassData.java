package edu.fdu.se.astdiff.associating.linkbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.util.MyList;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.ClassChangeEntity;
import org.apache.ibatis.javassist.compiler.ast.FieldDecl;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class ClassData extends LinkBean {


    public ClassData(ClassChangeEntity ce) {
        interfacesAndSuperClazz = new MyList<>();
        methods = new MyList<>();
        fields = new MyList<>();
        fieldType = new MyList<>();
        if(ce.stageIIBean==null
            || ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
            TypeDeclaration td = (TypeDeclaration)ce.bodyDeclarationPair.getBodyDeclaration();
            this.clazzName = td.getName().toString();
            List<Type> aa  = td.superInterfaceTypes();
            for(Type aaa:aa) {
                interfacesAndSuperClazz.add(aaa.toString());
            }
            if(td.getSuperclassType()!=null) {
                interfacesAndSuperClazz.add(td.getSuperclassType().toString());
            }
            MethodDeclaration[] mehtodss = td.getMethods();
            for(MethodDeclaration mds :mehtodss){
                methods.add(mds.getName().toString());
            }
            FieldDeclaration[] fielddd = td.getFields();
            for(FieldDeclaration fdd:fielddd){
                List<VariableDeclarationFragment> list = fdd.fragments();
                for(VariableDeclarationFragment vd:list){
                    fields.add(vd.getName().toString());
                }
                fieldType.add(fdd.getType().toString());
            }

        }else{
            if(ce.clusteredActionBean.curAction instanceof Move){

            }else{
                parseNonMove(ce);
            }
        }
    }
    private List<String> interfacesAndSuperClazz;
    public String clazzName;
    public List<String> methods;
    public List<String> fields;
    public List<String> fieldType;


    public void parseNonMove(ClassChangeEntity ce){
        Tree tree = (Tree)ce.clusteredActionBean.curAction.getNode();
        List<String> tempinterfacesAndSuperClazz = new MyList<>();
        String tempClassName = null;
        if(tree.getAstNode().getNodeType() == ASTNode.TYPE_DECLARATION) {
            TypeDeclaration td = (TypeDeclaration) tree.getAstNode();
            tempClassName = td.getName().toString();
            List<Type> aa  = td.superInterfaceTypes();
            for(Type aaa:aa) {
                tempinterfacesAndSuperClazz.add(aaa.toString());
            }
            if(td.getSuperclassType()!=null) {
                tempinterfacesAndSuperClazz.add(td.getSuperclassType().toString());
            }

        }
        for(Action a:ce.clusteredActionBean.actions){
            Tree t = (Tree) a.getNode();
            if (t.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME
                    || t.getAstNode().getClass().getSimpleName().endsWith("Literal")) {
                if(tempinterfacesAndSuperClazz.contains(t.getLabel())){
                    interfacesAndSuperClazz.add(t.getLabel());
                }
                if(tempClassName!=null &&tempClassName.equals(t.getLabel())){
                    clazzName = t.getLabel();
                }
            }
        }
    }



}
