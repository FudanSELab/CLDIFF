package edu.fdu.se.astdiff.miningoperationbean.base;

/**
 * Created by huangkaifeng on 2018/3/28.
 *
 */
public class StageIIBean {

    private String changeEntity;

    private String entityCreationStage;

    private String opt;

    private String granularity;

    private String thumbnail;

    private String lineRange;

    private String subEntity;

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }

    public void setSubEntity(String subEntity) {
        this.subEntity = subEntity;
    }

    public String getSubEntity() {

        return subEntity;
    }

    public StageIIBean(){


    }

    public String getLineRange() {
        return lineRange;
    }

    public void setLineRange(String lineRange) {

        this.lineRange = lineRange;
    }

    public String getChangeEntity() {
        return changeEntity;
    }

    public String getEntityCreationStage() {
        return entityCreationStage;
    }

    public String getOpt() {
        return opt;
    }

    public String getGranularity() {
        return granularity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setChangeEntity(String changeEntity) {
        this.changeEntity = changeEntity;
    }

    public void setEntityCreationStage(String entityCreationStage) {
        this.entityCreationStage = entityCreationStage;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
