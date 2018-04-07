package edu.fdu.se.astdiff.associating.linkbean;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.miningactions.util.MyList;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.MethodChangeEntity;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 */
public class MethodData extends LinkBean {

    public MethodData(MethodChangeEntity ce) {

        if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
//            ce.linkBean = new LinkBean();
        }else{
            if(ce.clusteredActionBean.curAction==null){
//                ce.linkBean = new LinkBean(ce.clusteredActionBean.fafather);
            } else if(ce.clusteredActionBean.curAction instanceof Move){
//                ce.linkBean = new LinkBean(ce.clusteredActionBean.curAction);
            }else {
//                ce.linkBean = new LinkBean(ce.clusteredActionBean.actions);
            }
        }
        this.parameterName = new MyList<>();
        this.parameterType = new MyList<>();
    }

    public String methodName;

    public List<String> parameterType;

    public List<String> parameterName;


}
