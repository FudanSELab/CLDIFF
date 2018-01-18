package edu.fdu.se.astdiff.miningoperationbean;

import com.github.javaparser.ast.body.BodyDeclaration;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class OperationManager {
    private List<OperationBean> mList;

    public OperationManager(List<OperationBean> list){
        this.mList = list;
        this.changeMap = new HashMap<>();
    }

    private Map<BodyDeclaration,List<OperationBean>> changeMap;

    public void initChangeBean(PreprocessingData dli){
        for(OperationBean ob:mList){
            // ob.getStart
            BodyDeclaration belongedBean = dli.getBelongedBodyDeclaration(3);
            if(belongedBean==null){
                System.err.println("ERROR ERROR");
                continue;
            }
            if(changeMap.containsKey(belongedBean)){
                changeMap.get(belongedBean).add(ob);
            }else{
                List<OperationBean> tmpList = new ArrayList<>();
                tmpList.add(ob);
                changeMap.put(belongedBean,tmpList);
            }
        }
    }
}
