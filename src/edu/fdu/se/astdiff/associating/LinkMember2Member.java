package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class LinkMember2Member {

    public static void checkMethodAssociation(ChangeEntityData changeEntityData,ChangeEntity ce1, ChangeEntity ce2){
        if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)){
            // 相似的signature
        }else if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)){

        }else if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){

        }
    }
}
