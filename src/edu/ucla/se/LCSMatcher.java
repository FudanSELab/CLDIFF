package edu.ucla.se;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LCSMatcher {
    public static Map<String, List<MissingChangeInfo>> matchLCS(GitHandler gitHandler,
                                                                HashMap<Integer, HashMap<String, List<List<Integer>>>> groups)
                                                                throws IOException {
        Map<String, List<MissingChangeInfo>> rs = new HashMap<>();

        for (int curGroup : groups.keySet()) {
            Map<String, List<MissingChangeInfo>> curGroupRs = matchSingleGroup(gitHandler, groups.get(curGroup));
            for (List<MissingChangeInfo> curFileInfo : curGroupRs.values()) {
                for (MissingChangeInfo info : curFileInfo) info.group = curGroup;
            }
            rs.putAll(curGroupRs);
        }

        return rs;
    }

    private static Map<String, List <MissingChangeInfo>> matchSingleGroup(GitHandler gitHandler,
                                                                          Map<String, List<List<Integer>>> curGroup) throws IOException {
        Map<String, Map<Integer, String>> dict = gitHandler.getOldFileContentByLine(curGroup);
        ArrayList<String> oldContentsCode = new ArrayList<>();

        for (Map.Entry f: curGroup.entrySet()){        // file
            String fileName = (String)f.getKey();
            if (!dict.containsKey(fileName)){
                continue;
            }
            Map<Integer, String> fileCodeDict = dict.get(fileName);
            for (int i = 0; i < curGroup.get(fileName).size(); ++i){   // code snippet
                String code = "";
                for (int j = 0; j < curGroup.get(fileName).get(i).size(); j++) {    // lines of code
                    Integer idx = curGroup.get(fileName).get(i).get(j);
                    code += ";";       // new line
                    if (!fileCodeDict.containsKey(idx)){
                        continue;
                    }
                    if (fileCodeDict.get(idx).length() > 0){
                        code += fileCodeDict.get(idx);
                    }
                }
                oldContentsCode.add(code);
            }
        }

        for (String s : oldContentsCode) {
            System.out.println("code snippet");
            System.out.println(s);
        }
        ScoreComputer sc = new LCSScoreComputer();
        PEAM peam = new PEAM(sc);

        double sim_score_thres = 0.79;
        double min_sup_ratio = 1;
        double match_score = 0.8;

        System.out.println("Start Finding Patterns...");
        peam.FindFrequentPattern(oldContentsCode, sim_score_thres, min_sup_ratio);
        peam.PrintPatterns();
        System.out.println("Done");

        int pattern_cnt = peam.GetPatternCnt();

        return gitHandler.matchLCS(peam,4,
                                   (int) Math.max((pattern_cnt*0.7),1),
                                   (int) Math.max((pattern_cnt*0.7),1), match_score);
    }
}
