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

    private String type;

    private String displayDesc;

    private List<SubRange> subRange;

    public StageIIIBean() {

    }

    class SubRange {
        String file2;
        String subRangeCode;
        String type2;
    }

    private void addRangeResultList(List<Integer[]> ranges, CompilationUnit cu, String file, String type) {
        if (subRange == null) {
            subRange = new ArrayList<>();
        }
        for (Integer[] range : ranges) {
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
                    subRangeItem.type2 = type;
                    subRangeItem.subRangeCode = startLine + "," + startColumn + "," + pos;
                    subRange.add(subRangeItem);
                } else {
                    SubRange subRangeItem = new SubRange();
                    subRangeItem.file2 = file;
                    subRangeItem.type2 = type;
                    subRangeItem.subRangeCode = startLine + "," + 0 + "," + pos;
                    subRange.add(subRangeItem);
                }
            }
            int startColumn2 = endLine > startLine ? 0 : startColumn;
            SubRange subRangeItem = new SubRange();
            subRangeItem.file2 = file;
            subRangeItem.type2 = type;
            subRangeItem.subRangeCode = endLine + "," + startColumn2 + "," + endColumn;
            subRange.add(subRangeItem);
        }
    }

    public void addInsertList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = "dst";
        String type = "insert";
        addRangeResultList(ranges, cu, file, type);
    }

    public void addUpdateList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = "src";
        String type = "update";
        addRangeResultList(ranges, cu, file, type);
    }

    public void addDeleteList(List<Integer[]> ranges, CompilationUnit cu) {
        String file = "src";
        String type = "delete";
        addRangeResultList(ranges, cu, file, type);
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


    public void setType(String type) {
        this.type = type;
    }

    public JSONObject genJSonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("file", file);
        jsonObject.put("range", range);
        jsonObject.put("type", type);
        jsonObject.put("description", displayDesc);
        jsonObject.put("id",changeEntityId);
        if (subRange != null) {
            JSONArray jsonArray1 = new JSONArray();
            for (SubRange subRange1 : subRange) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("file", subRange1.file2);
                jsonObject1.put("sub_range_code", subRange1.subRangeCode);
                jsonObject1.put("type", subRange1.type2);
                jsonArray1.put(jsonObject1);
            }
            jsonObject.put("sub_range", jsonArray1);
        }
        return jsonObject;
    }
}
