package edu.fdu.se.astdiff.miningchangeentity.base;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/28.
 *
 */
public class StageIIIBean {

    private int changeEntityId;

    private String key;

    private String file;

    private String range;

    private String type1;
    private String type2;

    private String displayDesc;

    private List<SubRange> subRange;

    public StageIIIBean() {

    }

    class SubRange {
        String file2;
        String subRangeCode;
        String subType;
    }

    private void addRangeResultList(Integer[] range, CompilationUnit cu, String file, String type) {
        if (subRange == null) {
            subRange = new ArrayList<>();
        }
        int startLine = cu.getLineNumber(range[0]);
        int startColumn = cu.getColumnNumber(range[0]);
        int endLine = cu.getLineNumber(range[1]);
        int endColumn = cu.getColumnNumber(range[1]);

        List<Integer> endColumnList = new ArrayList<>();
        int i = 1;
        while (startLine + i <= endLine) {
            int lineNum = startLine + i;
            int pos = cu.getPosition(lineNum, 0);
            pos--;
            endColumnList.add(pos);
            i++;
        }
        for (int j = startLine, m = 0; j < endLine; j++, m++) {
            int pos = endColumnList.get(m);
            if (j == startLine) {
                SubRange subRangeItem = new SubRange();
                subRangeItem.file2 = file;
                subRangeItem.subType = type;
                subRangeItem.subRangeCode = startLine + "," + startColumn + "," + pos;
                subRange.add(subRangeItem);
            } else {
                SubRange subRangeItem = new SubRange();
                subRangeItem.file2 = file;
                subRangeItem.subType = type;
                subRangeItem.subRangeCode = startLine + "," + 0 + "," + pos;
                subRange.add(subRangeItem);
            }
        }
        int startColumn2 = endLine > startLine ? 0 : startColumn;
        SubRange subRangeItem = new SubRange();
        subRangeItem.file2 = file;
        subRangeItem.subType = type;
        subRangeItem.subRangeCode = endLine + "," + startColumn2 + "," + endColumn;
        subRange.add(subRangeItem);
    }

    private void addRangesResultList(List<Integer[]> ranges, CompilationUnit cu, String file, String type) {
        for (Integer[] range : ranges) {
            addRangeResultList(range,cu,file,type);
        }
    }

    public void addInsertList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = ChangeEntityDesc.StageIIIFile.DST;
        String type = "insert";
        addRangesResultList(ranges, cu, file, type);
    }

    public void addUpdateList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = ChangeEntityDesc.StageIIIFile.SRC;
        String type = "update";
        addRangesResultList(ranges, cu, file, type);
    }

    public void addDeleteList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = ChangeEntityDesc.StageIIIFile.SRC;
        String type = "delete";
        addRangesResultList(ranges, cu, file, type);
    }

    public void addMoveListSrc(Integer[] range,CompilationUnit cu){
        String file = ChangeEntityDesc.StageIIIFile.SRC;
        String type = "move";
        addRangeResultList(range, cu, file, type);
    }

    public void addMoveListDst(Integer[] range,CompilationUnit cu){
        String file = ChangeEntityDesc.StageIIIFile.DST;
        String type = "move";
        addRangeResultList(range, cu, file, type);
    }


    public void setDisplayDesc(String displayDesc) {
        this.displayDesc = displayDesc;
    }


    public void setChangeEntityId(int changeEntityId) {
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


    public void setType1(String type) {
        this.type1 = type;
    }
    public void setType2(String type) {
        this.type2 = type;
    }

    public JSONObject genJSonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.KEYY, key);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.FILE, file);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.RANGE, range);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.TYPE1, type1);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.TYPE2, type2);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.DESCRIPTION, displayDesc);
        jsonObject.put(ChangeEntityDesc.StageIIIKeys.ID,changeEntityId);
        if (subRange != null) {
            JSONArray jsonArray1 = new JSONArray();
            for (SubRange subRange1 : subRange) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put(ChangeEntityDesc.StageIIIKeys.FILE, subRange1.file2);
                jsonObject1.put(ChangeEntityDesc.StageIIIKeys.SUB_RANGE_CODE, subRange1.subRangeCode);
                jsonObject1.put(ChangeEntityDesc.StageIIIKeys.SUB_TYPE, subRange1.subType);
                jsonArray1.put(jsonObject1);
            }
            jsonObject.put(ChangeEntityDesc.StageIIIKeys.SUB_RANGE, jsonArray1);
        }
        return jsonObject;
    }
}
