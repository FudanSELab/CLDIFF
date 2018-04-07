package edu.fdu.se.astdiff.miningchangeentity.base;

import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/2/8.
 *
 */
public class MemberPlusChangeEntity extends ChangeEntity {

    public BodyDeclarationPair bodyDeclarationPair;

    public MemberPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public MemberPlusChangeEntity(String location,String changeType,MyRange myRange){
        super(location,changeType,myRange);

    }




}
