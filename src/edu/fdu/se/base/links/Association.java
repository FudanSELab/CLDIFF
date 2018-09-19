package edu.fdu.se.base.links;

import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import org.json.JSONObject;

/**
 * Created by huangkaifeng on 4/7/18.
 *
 */
public class Association {


    private String fileA;

    private String fileB;

    private ChangeEntity changeEntity1;

    private ChangeEntity changeEntity2;

    private String type;

    private String keyWord;

    public Association(ChangeEntity changeEntity1,ChangeEntity changeEntity2,String type,String keyWord,String fileName){
        this.changeEntity1 = changeEntity1;
        this.changeEntity2 = changeEntity2;
        this.type = type;
        this.keyWord = keyWord;
        this.fileA = fileName;
    }

    /**
     * file A B 按照字母顺序 固定
     * @param fileA
     * @param fileB
     * @param changeEntity1
     * @param changeEntity2
     */
    public Association(String fileA,String fileB,ChangeEntity changeEntity1,ChangeEntity changeEntity2,String type,String keyWord){
        if(fileA.compareTo(fileB)<0){
            this.fileA = fileA;
            this.fileB = fileB;
            this.changeEntity1 = changeEntity1;
            this.changeEntity2 = changeEntity2;
        }else{
            this.fileA = fileB;
            this.fileB = fileA;
            this.changeEntity1 = changeEntity2;
            this.changeEntity2 = changeEntity1;
        }
        this.type = type;
        this.keyWord = keyWord;
    }



    public String toString(){
        return changeEntity1.getChangeEntityId()+". "+changeEntity1.getClass().getSimpleName()
                +" -> "+changeEntity2.getChangeEntityId()+". "+changeEntity2.getClass().getSimpleName()+" : " +type +" "+keyWord;
    }

    public JSONObject linkJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from",changeEntity1.getChangeEntityId());
        jsonObject.put("to",changeEntity2.getChangeEntityId());
        if(this.keyWord==null){
            jsonObject.put("desc",this.type);
        }else{
            jsonObject.put("desc",this.type+" "+this.keyWord);
        }
        return jsonObject;
    }
}
