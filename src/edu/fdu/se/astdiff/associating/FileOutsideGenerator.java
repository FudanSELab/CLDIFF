package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.associating.linkbean.MethodData;
import edu.fdu.se.astdiff.associating.linkbean.StmtData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.member.MethodChangeEntity;
import edu.fdu.se.handlefile.Method;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/4/16.
 *
 *
 */
public class FileOutsideGenerator {

    public ChangeEntityData ce1;

    public ChangeEntityData ce2;
    public List<Association> mAssos;
    public FileOutsideGenerator(){
        mAssos = new ArrayList<>();
    }


    public void generateOutsideAssociation(ChangeEntityData ca,ChangeEntityData cb){
        mAssos.clear();
        ce1 = ca;
        ce2 = cb;
        List<ChangeEntity> methodAList = methodDeclarations(ca);
        List<ChangeEntity> methodBList = methodDeclarations(cb);
        List<ChangeEntity> stmtAList = stmtChange(ca);
        List<ChangeEntity> stmtBList = stmtChange(cb);
        checkMethodInvokes(methodAList,stmtBList,1);
        checkMethodInvokes(methodBList,stmtAList,0);
    }

    public List<ChangeEntity> methodDeclarations(ChangeEntityData ced){
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> methodDeclarationList = new ArrayList<>();
        for(ChangeEntity ce:mList){
            if(ce instanceof MethodChangeEntity){
                methodDeclarationList.add(ce);
            }
        }
        return methodDeclarationList;
    }

    public List<ChangeEntity> stmtChange(ChangeEntityData ced){
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> stmtList = new ArrayList<>();
        for(ChangeEntity ce:mList){
            if(ce instanceof StatementPlusChangeEntity){
                stmtList.add(ce);
            }
        }
        return stmtList;
    }


    public void checkMethodInvokes(List<ChangeEntity> methodList,List<ChangeEntity> stmtList,int flag){
        for(ChangeEntity cmethod:methodList){
            MethodChangeEntity mm = (MethodChangeEntity) cmethod;
            if(mm.linkBean == null) continue;
            MethodData mdata = (MethodData) mm.linkBean;
            for(ChangeEntity cstmt:stmtList){
                StatementPlusChangeEntity stmt = (StatementPlusChangeEntity) cstmt;
                if(stmt.linkBean!=null){
                    StmtData stmtData = (StmtData) stmt.linkBean;
                    for(String s:stmtData.methodInvocation){
                        if(mdata.methodName.contains(s)){
                            if(flag==1) {
                                Association association = new Association(ce1.fileName, ce2.fileName, cmethod, cstmt, ChangeEntityDesc.StageIIIAssociationType.TYPE_CROSS_FILE_CALL_METHOD);
                                mAssos.add(association);
                            }else{
                                Association association = new Association(ce2.fileName, ce1.fileName, cmethod, cstmt, ChangeEntityDesc.StageIIIAssociationType.TYPE_CROSS_FILE_CALL_METHOD);
                                mAssos.add(association);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }
}
