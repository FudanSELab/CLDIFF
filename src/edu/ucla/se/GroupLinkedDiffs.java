package edu.ucla.se;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


// m1, m2, m3
// 0   3   0
// 1   0   1
// 3   1   3
// 0
// 1

public class GroupLinkedDiffs {
    private List<List<Integer>> methodLinkedEdits;
    private HashMap<Integer, HashMap<String, ArrayList<Integer>>> group2Loc = new HashMap<>();

    private Set<Integer> editSetIds;
    private int minMethodEditLen;

    public GroupLinkedDiffs(List<List<Integer>> methodlinkedEdits){
        System.out.println(methodlinkedEdits);
        this.methodLinkedEdits = methodlinkedEdits;
        this.populateGroup2Locations(methodlinkedEdits);
    }

    private void populateGroup2Locations(List<List<Integer>> methodLinkedEdits){
        this.editSetIds = new HashSet<>();
        this.minMethodEditLen = Integer.MAX_VALUE;
        for (int i=0; i< methodLinkedEdits.size(); i++){
            int methodEditLen = methodLinkedEdits.get(i).size();
            if (minMethodEditLen > methodEditLen){
                minMethodEditLen = methodEditLen;
            }
            for (int j = 0; j< methodEditLen; j++) {
                Integer group_i = methodLinkedEdits.get(i).get(j);

                if (!group2Loc.containsKey(group_i)){
                    group2Loc.put(group_i, new HashMap<> ());
                }
                String[] loc_types = new String[] {"method", "index"};
                for (String type : loc_types) {
                    if (!group2Loc.get(group_i).containsKey(type)) {
                        group2Loc.get(group_i).put(type, new ArrayList<>());
                    }
                }

                group2Loc.get(group_i).get("method").add(i);
                group2Loc.get(group_i).get("index").add(j);

                this.editSetIds.add(group_i);
            }
        }
    }

    private int getSetIds(int i,  int j){
        if (i < this.methodLinkedEdits.size() && j < this.methodLinkedEdits.get(i).size()){
            return this.methodLinkedEdits.get(i).get(j);
        }
        else{
            return -1;
        }
    }

    public List<List<Integer>> getLinkedStmtGroups(List<List<Integer>> methodLinkedEdits){
        this.populateGroup2Locations(methodLinkedEdits);
        System.out.println("group2Loc");
        System.out.println(this.group2Loc);
        System.out.println("editSetIds");
        System.out.println(this.editSetIds);
        System.out.println();
        List<List<Integer>> resultEditGroups = new ArrayList<> ();

        Set<Integer> visitedGroups = new HashSet<>();

        for (Integer set_i : this.editSetIds){
            if (visitedGroups.contains(set_i)){
                continue;
            }
            visitedGroups.add(set_i);

            ArrayList<Integer> method_locs = group2Loc.get(set_i).get("method");
            ArrayList<Integer> init_locs = group2Loc.get(set_i).get("index");

            List<Integer> curGroup = new ArrayList<>();

            for (int j = 0; j < this.minMethodEditLen; j++) {
                final Integer offset = j;
                ArrayList<Integer> cur_locs = new ArrayList<>(init_locs.stream().map(n -> n + offset)
                        .collect(Collectors.toList()));
                System.out.println("set_i=" + set_i.toString());
                System.out.println("j=" + j);
                System.out.println("method_locs");
                System.out.println(method_locs);
                System.out.println("cur_locs");
                System.out.println(cur_locs);

                ArrayList<Integer> group_vals = new ArrayList<>(IntStream.range(0, cur_locs.size())
                        .mapToObj(k -> this.getSetIds(method_locs.get(k), cur_locs.get(k)))
                        .collect(Collectors.toList()));
                System.out.println(group_vals);
                System.out.println();

                Integer same_group_val = group_vals.get(0);
                if (group_vals.stream().distinct().count() == 1 && same_group_val != -1){
                    curGroup.add(same_group_val);
                    if (set_i != same_group_val){
                        visitedGroups.add(same_group_val);
                    }
                }
                else{
                    break;
                }

            }
            resultEditGroups.add(curGroup);
        }

        return resultEditGroups;

    }

    public HashMap<Integer, Integer> getStmtGroupMap(){
        List<List<Integer>> groupedStmtGroups = this.getLinkedStmtGroups(this.methodLinkedEdits);
        System.out.println("statement groups");
        System.out.println(groupedStmtGroups);
        HashMap<Integer, Integer> linkedStmt2Group = new HashMap<> ();
        for (int i = 0; i < groupedStmtGroups.size(); i++){
            List<Integer> curGroup = groupedStmtGroups.get(i);
            for (Integer g: curGroup){
                linkedStmt2Group.put(g, i);
            }
        }
        return linkedStmt2Group;
    }

}
