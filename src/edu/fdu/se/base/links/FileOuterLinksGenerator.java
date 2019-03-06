package edu.fdu.se.base.links;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.links.linkbean.ClassData;
import edu.fdu.se.base.links.linkbean.MethodData;
import edu.fdu.se.base.links.linkbean.StmtData;
import edu.fdu.se.base.links.similarity.TreeDistance;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.base.StatementPlusChangeEntity;
import edu.fdu.se.base.miningchangeentity.member.ClassChangeEntity;
import edu.fdu.se.base.miningchangeentity.member.MethodChangeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by huangkaifeng on 2018/4/16.
 */
public class FileOuterLinksGenerator {

    public ChangeEntityData ce1;

    public ChangeEntityData ce2;
    public List<Link> mAssos;

    public FileOuterLinksGenerator() {
        mAssos = new ArrayList<>();
    }


    public void generateOutsideAssociation(ChangeEntityData ca, ChangeEntityData cb) {
        mAssos.clear();
        ce1 = ca;
        ce2 = cb;
        List<ChangeEntity> methodAList = methodDeclarations(ca);
        List<ChangeEntity> methodBList = methodDeclarations(cb);
//        List<ChangeEntity> stmtAList = stmtChange(ca);
//        List<ChangeEntity> stmtBList = stmtChange(cb);
//        List<ChangeEntity> classAList = classChange(ca);
//        List<ChangeEntity> classBList = classChange(cb);
//        checkMethodInvokes(methodAList, stmtBList, 1);
//        checkMethodInvokes(methodBList, stmtAList, 0);
//        checkClassInvokes(classAList, stmtBList, 1);
//        checkClassInvokes(classBList, stmtAList, 0);
        checkMethodInheritage(methodAList, methodBList);


    }

    public List<ChangeEntity> methodDeclarations(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> methodDeclarationList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof MethodChangeEntity) {
                methodDeclarationList.add(ce);
            }
        }
        return methodDeclarationList;
    }

    public List<ChangeEntity> stmtChange(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> stmtList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof StatementPlusChangeEntity) {
                stmtList.add(ce);
            }
        }
        return stmtList;
    }

    public void checkSimilarity(Map<String, ChangeEntityData> mMap, TotalFileLinks totalFileLinks) {
        List<ChangeEntity> totalEntityList = new ArrayList<>();
        Map<Integer, String> changeEntityDataDict = new HashMap<>();
        List<Integer> indexList = new ArrayList<>();
        for (Entry<String, ChangeEntityData> entry : mMap.entrySet()) {
            for (ChangeEntity tmp : entry.getValue().mad.getChangeEntityList()) {
                if (indexList.contains(tmp.getChangeEntityId())) {
                    continue;
                }
                totalEntityList.add(tmp);
                changeEntityDataDict.put(tmp.getChangeEntityId(), entry.getKey());
                indexList.add(tmp.getChangeEntityId());
            }
        }
        List<Class> instances = new ArrayList<>();
        totalEntityList.forEach(a -> {
            if (!instances.contains(a.getClass())) {
                instances.add(a.getClass());
            }
        });
        for (Class clazz : instances) {
            List<ChangeEntity> someTypeOfChangeClassA = new ArrayList<>();
            for (ChangeEntity c : totalEntityList) {
                if (c.getClass().equals(clazz)) {
                    someTypeOfChangeClassA.add(c);
                }
            }
            for (int i = 0; i < someTypeOfChangeClassA.size(); i++) {
                for (int j = i + 1; j < someTypeOfChangeClassA.size(); j++) {
                    ChangeEntity a = someTypeOfChangeClassA.get(i);
                    ChangeEntity b = someTypeOfChangeClassA.get(j);
                    if (!a.stageIIBean.getOpt().equals(b.stageIIBean.getOpt())
                            || a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE)) {
                        continue;
                    }
                    if (a.clusteredActionBean != null && b.clusteredActionBean != null) {
                        if (a.clusteredActionBean.fafather.getAstClass().equals(b.clusteredActionBean.fafather.getAstClass())
                                && a.clusteredActionBean.actions.size() == b.clusteredActionBean.actions.size()) {
                            TreeDistance treeDistance = new TreeDistance(a.clusteredActionBean.fafather, b.clusteredActionBean.fafather);
                            float distance = treeDistance.calculateTreeDistance();
                            if (distance <= 1.0) {
                                String fileNameA = changeEntityDataDict.get(a.getChangeEntityId()).split("@@@")[1];
                                String fileNameB = changeEntityDataDict.get(b.getChangeEntityId()).split("@@@")[1];
                                if (fileNameA.equals(fileNameB)) {
                                    Link link = new Link(fileNameA, a, b, ChangeEntityDesc.StageIIIAssociationType.SYSTEMATIC_CHANGE, null);
                                    totalFileLinks.addEntry(changeEntityDataDict.get(a.getChangeEntityId()), link);
                                } else {
                                    Link link = new Link(fileNameA, fileNameB, a, b, ChangeEntityDesc.StageIIIAssociationType.SYSTEMATIC_CHANGE, null);
                                    totalFileLinks.addFile2FileAssos(changeEntityDataDict.get(a.getChangeEntityId()), changeEntityDataDict.get(b.getChangeEntityId()), link);
                                }
                            }
                        }
                    }
                }
            }

        }
    }


    public List<ChangeEntity> classChange(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> classList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof ClassChangeEntity) {
                classList.add(ce);
            }
        }
        return classList;
    }


    public void checkMethodInvokes(List<ChangeEntity> methodList, List<ChangeEntity> stmtList, int flag) {
        for (ChangeEntity cmethod : methodList) {
            MethodChangeEntity mm = (MethodChangeEntity) cmethod;
            if (mm.linkBean == null) continue;
            MethodData mdata = (MethodData) mm.linkBean;
            for (ChangeEntity cstmt : stmtList) {
                StatementPlusChangeEntity stmt = (StatementPlusChangeEntity) cstmt;
                if (stmt.linkBean != null) {
                    StmtData stmtData = (StmtData) stmt.linkBean;
                    for (String s : stmtData.methodInvocation) {
                        if (mdata.methodName.contains(s)) {
                            Action curAction = stmt.clusteredActionBean.curAction;
                            Tree node = (Tree) curAction.getNode();
                            String methodName = LinkUtil.findResidingMethodName(node);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE, s, methodName);
                            if (flag == 1) {
                                Link link = new Link(ce1.fileName, ce2.fileName, cmethod, cstmt, desc, s);
                                mAssos.add(link);
                            } else {
                                Link link = new Link(ce2.fileName, ce1.fileName, cmethod, cstmt, desc, s);
                                mAssos.add(link);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    public void checkMethodInheritage(List<ChangeEntity> methodListA, List<ChangeEntity> methodListB) {
        String classA = ce1.fileName.substring(0, ce1.fileName.length() - 5);
        String classB = ce2.fileName.substring(0, ce2.fileName.length() - 5);
        List<MethodChangeEntity> mc1 = new ArrayList<>();
        List<MethodChangeEntity> mc2 = new ArrayList<>();
        for (ChangeEntity ce : methodListA) {
            if (ce instanceof MethodChangeEntity) {
                MethodChangeEntity mce = (MethodChangeEntity) ce;
//                if(mce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
                mc1.add(mce);
//                }
            }
        }
        for (ChangeEntity ce : methodListB) {
            if (ce instanceof MethodChangeEntity) {
                MethodChangeEntity mce = (MethodChangeEntity) ce;
//                if(mce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
                mc2.add(mce);
//                }
            }
        }
        List<String> superClasses1 = ce1.mad.preprocessedData.getInterfacesAndFathers();
        List<String> superClasses2 = ce2.mad.preprocessedData.getInterfacesAndFathers();
        for (MethodChangeEntity m : mc1) {
            for (MethodChangeEntity n : mc2) {
                MethodData methodData1 = (MethodData) m.linkBean;
                MethodData methodData2 = (MethodData) n.linkBean;
                //Override-Method: methodName overriden in B.java
                //Abstract-Method: methodName implemented in A.java
                //Implement-Method: methodName implemented interface A.java
                if (methodData1.methodName == null || methodData1.methodName.isEmpty() || methodData2.methodName == null || methodData2.methodName.isEmpty()) {
                    System.err.println("Method name null-- FileOuterLinksGenerator");
                    continue;
                }
                if (methodData1.methodName.get(0).equals(methodData2.methodName.get(0))) {
                    if (superClasses1.contains("superclass---" + classB)) { // B是A的父类
                        if (superClasses2.contains("abstract---" + classB)) {
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.ABSTRACT_METHOD, methodData1.methodName.get(0), classB);
                            Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                            mAssos.add(link);
                        } else {
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.OVERRIDE_METHOD, methodData1.methodName.get(0), classB);
                            Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                            mAssos.add(link);
                        }
                    } else if (superClasses1.contains("interface---" + classB)) { // B是A的接口
                        String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.IMPLEMENT_METHOD, methodData1.methodName.get(0), classB);
                        Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                        mAssos.add(link);

                    }

                    if (superClasses2.contains("superclass---" + classA)) { // A是B的父类
                        if (superClasses1.contains("abstract---" + classA)) {
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.ABSTRACT_METHOD, methodData1.methodName.get(0), classA);
                            Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                            mAssos.add(link);
                        } else {
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.OVERRIDE_METHOD, methodData1.methodName.get(0), classA);
                            Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                            mAssos.add(link);
                        }
                    } else if (superClasses2.contains("interface---" + classA)) { // A是B的接口
                        String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.IMPLEMENT_METHOD, methodData1.methodName.get(0), classA);
                        Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                        mAssos.add(link);
                    }
                }
            }
        }

    }

    public void checkClassInvokes(List<ChangeEntity> classList, List<ChangeEntity> stmtList, int flag) {
        for (ChangeEntity cclass : classList) {
            ClassChangeEntity mm = (ClassChangeEntity) cclass;
            if (mm.linkBean == null) continue;
            ClassData mdata = (ClassData) mm.linkBean;
            for (ChangeEntity cstmt : stmtList) {
                StatementPlusChangeEntity stmt = (StatementPlusChangeEntity) cstmt;
                if (stmt.linkBean != null) {
                    StmtData stmtData = (StmtData) stmt.linkBean;
                    if (stmtData.classCreation.contains(mdata.clazzName)) {
                        String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE, mdata.clazzName);
                        if (flag == 1) {
                            Link link = new Link(ce1.fileName, ce2.fileName, cclass, cstmt, desc, mdata.clazzName);
                            mAssos.add(link);
                        } else {
                            Link link = new Link(ce2.fileName, ce1.fileName, cclass, cstmt, desc, mdata.clazzName);
                            mAssos.add(link);
                        }
                    }
                    for (String s : stmtData.methodInvocation) {

                        if (mdata.methods.contains(s)) {
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE, s, s);
                            if (flag == 1) {
                                Link link = new Link(ce1.fileName, ce2.fileName, cclass, cstmt, desc, mdata.clazzName);
                                mAssos.add(link);
                            } else {
                                Link link = new Link(ce2.fileName, ce1.fileName, cclass, cstmt, desc, mdata.clazzName);
                                mAssos.add(link);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
