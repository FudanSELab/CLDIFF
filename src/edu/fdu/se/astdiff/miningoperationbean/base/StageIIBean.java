package edu.fdu.se.astdiff.miningoperationbean.base;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/28.
 *
 */
public class StageIIBean {

    public StageIIBean(){

        this.opt2List = new ArrayList<>();
    }

    private String entityCreationStage;

    private String granularity;

    private String opt;

    private String changeEntity;

    private String subEntity;

    private List<Opt2> opt2List;

    class Opt2 {
        private String opt2;
        private String opt2Expression;

        @Override
        public int hashCode(){
            return (opt2+opt2Expression).hashCode();
        }
    }



    private String thumbnail;

    private String lineRange;

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

    public void setOpt2AndOpt2Expression(String opt2,String opt2Expression) {
        Opt2 opt2Class = new Opt2();
        opt2Class.opt2 = opt2;
        opt2Class.opt2Expression = opt2Expression;
        for(Opt2 tmp:this.opt2List){
            if(tmp.hashCode() == opt2Class.hashCode()){
                return;
            }
        }
        this.opt2List.add(opt2Class);
    }

    public List<Opt2> getOpt2List() {
        return opt2List;
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

    @Override
    public String toString() {
        return this.entityCreationStage + " " +
                this.granularity + " " +
                this.opt + " " +
                this.changeEntity + " " +
                this.subEntity + " " +
                this.thumbnail + " " +
                this.lineRange + " " +
                this.location;
    }

    public String toString2(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.opt);
        sb.append(" ");
        sb.append(this.changeEntity);
        if(this.opt.equals(ChangeEntityDesc.StageIIIOpt.OPT_CHANGE)){
            sb.append(" with/by");

        }
        return sb.toString();
    }
}
