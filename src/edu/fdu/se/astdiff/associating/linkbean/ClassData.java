package edu.fdu.se.astdiff.associating.linkbean;

import edu.fdu.se.astdiff.miningchangeentity.member.ClassChangeEntity;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 */
public class ClassData extends LinkBean {

    public ClassData(ClassChangeEntity ce) {

    }

    private List<String> interfacesAndSuperClazz;

    public String clazzName;
//    public List<String> methodNames;

}
