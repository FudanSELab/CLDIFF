package edu.fdu.se.astdiff.miningchangeentity.base;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/28.
 *
 */
public class StageIIIBean {

    private String changeEntityId;

    private String key;

    private String file;

    private String range;

    private String type;

    private String displayDesc;

    private List<SubRange> subRange;

    public StageIIIBean() {

    }

    class SubRange{
        String file2;
        String subRangeCode;
        String type2;
    }



    public void setDisplayDesc(String displayDesc) {
        this.displayDesc = displayDesc;
    }


    public void setChangeEntityId(String changeEntityId) {
        this.changeEntityId = changeEntityId;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setFile(String file) {
        this.file = file;
    }


    public void setRange(String range) {
        this.range = range;
    }


    public void setType(String type) {
        this.type = type;
    }

    public JSONObject genJSonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key",key);
        jsonObject.put("file",file);
        jsonObject.put("range",range);
        jsonObject.put("type",type);
        jsonObject.put("description",displayDesc);

        return null;

    }
}
