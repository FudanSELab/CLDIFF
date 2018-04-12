package edu.fdu.se.astdiff.webapi;

import edu.fdu.se.astdiff.associating.Association;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/11.
 *
 */
public class GenerateChangeEntityJson {

    public static void setChangeEntityStageIIIBean(List<ChangeEntity> changeEntityList){
        for(int i=0;i<changeEntityList.size();i++) {
            ChangeEntity changeEntity = changeEntityList.get(i);
            changeEntity.stageIIIBean.setChangeEntityId(changeEntity.changeEntityId);
            if (changeEntity.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                changeEntity.stageIIIBean.setKey("preprocess");
            } else {
                changeEntity.stageIIIBean.setKey("gumtree");
            }
            if(changeEntity.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)){
                changeEntity.stageIIIBean.setFile("dst");
                changeEntity.stageIIIBean.setRange(changeEntity.stageIIBean.getLineRange());

            }else if(changeEntity.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
                changeEntity.stageIIIBean.setFile("src-dst");
                //todo range
            }else if(changeEntity.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)) {
                changeEntity.stageIIIBean.setFile("src");
                changeEntity.stageIIIBean.setRange(changeEntity.stageIIBean.getLineRange());
            } else {
                changeEntity.stageIIIBean.setFile("src");
                changeEntity.stageIIIBean.setRange(changeEntity.stageIIBean.getLineRange());
            }
            changeEntity.stageIIIBean.setDisplayDesc(changeEntity.stageIIBean.toString2());

        }
    }



    public static String generateEntityJson(List<ChangeEntity> changeEntityList){
        setChangeEntityStageIIIBean(changeEntityList);
        JSONArray jsonArray = new JSONArray();
        for(int i =0;i<changeEntityList.size();i++){
            ChangeEntity changeEntity = changeEntityList.get(i);
            JSONObject jsonObject = changeEntity.stageIIIBean.genJSonObject();
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString(4);
    }







    public static String generateAssociationJson(List<Association> associationList){
        JSONArray jsonArray = new JSONArray();
        for(int i =0;i<associationList.size();i++){
            Association association = associationList.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",i);
            jsonObject.put("a",association.getChangeEntity1().getChangeEntityId());
            jsonObject.put("b",association.getChangeEntity2().getChangeEntityId());
            jsonObject.put("type",association.getType());
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}
