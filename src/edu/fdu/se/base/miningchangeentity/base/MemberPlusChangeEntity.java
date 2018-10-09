package edu.fdu.se.base.miningchangeentity.base;

import edu.fdu.se.base.links.MyRange;
import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;

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
