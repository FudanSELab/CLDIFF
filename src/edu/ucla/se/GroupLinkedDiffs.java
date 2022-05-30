package edu.ucla.se;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GroupLinkedDiffs {
    private ArrayList<ArrayList<Integer>> setIds;
    private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> group2Loc = new HashMap<>();

    private int nStatementGroup;
    private int minMethodEditLen;

    public GroupLinkedDiffs(ArrayList<ArrayList<Integer>> setIds){
        this.setIds = setIds;
        this.populateGroup2Locations(setIds);
    }

    private void populateGroup2Locations(ArrayList<ArrayList<Integer>> setIds){
        this.nStatementGroup = -1;
        this.minMethodEditLen = Integer.MAX_VALUE;
        for (int i=0; i< setIds.size(); i++){
            int methodEditLen = setIds.get(i).size();
            if (minMethodEditLen > methodEditLen){
                minMethodEditLen = methodEditLen;
            }
            for (int j = 0; j< methodEditLen; j++) {
                Integer group_i = setIds.get(i).get(j);

                if (!group2Loc.containsKey(group_i)){
                    group2Loc.put(group_i, new HashMap<Integer, ArrayList<Integer>> ());
                }
                for (int k=0; k< 2; k++) {
                    if (!group2Loc.get(group_i).containsKey((Integer) k)) {
                        group2Loc.get(group_i).put((Integer) k, new ArrayList<Integer>());
                    }
                }

                group2Loc.get(group_i).get(new Integer(0)).add(new Integer(i));
                group2Loc.get(group_i).get(new Integer(1)).add(new Integer(j));

                if (this.nStatementGroup < group_i) {
                    this.nStatementGroup = group_i;
                }
            }
        }
    }

    private int getSetIds(int i,  int j){
        if (i < this.setIds.size() && j < this.setIds.get(i).size()){
            return this.setIds.get(i).get(j);
        }
        else{
            return -1;
        }
    }

    public ArrayList<Set<Integer>> getLinkedStmtGroups(ArrayList<ArrayList<Integer>> setIds){
        this.populateGroup2Locations(setIds);
        ArrayList<Set<Integer>> resultEditGroups = new ArrayList<> ();

        Set<Integer> visitedGroups = new HashSet<>();

        for (int i = 0; i < this.nStatementGroup; i++){
            if (visitedGroups.contains(i)){
                continue;
            }
            visitedGroups.add(new Integer(i));

            ArrayList<Integer> method_locs = group2Loc.get(i).get(0);
            ArrayList<Integer> init_locs = group2Loc.get(i).get(1);

            Set<Integer> curGroup = new HashSet<>();

            for (int j = 0; j < this.minMethodEditLen; j++) {
                ArrayList<Integer> cur_locs = new ArrayList<Integer>(init_locs.stream().map(n -> n + 1)
                        .collect(Collectors.toList()));
                ArrayList<Integer> group_vals = new ArrayList<Integer>(IntStream.range(0, cur_locs.size())
                        .mapToObj(k -> this.getSetIds(method_locs.get(k), cur_locs.get(k)))
                        .collect(Collectors.toList()));

                Integer same_group_val = group_vals.get(0);
                if (group_vals.stream().distinct().count() == 1){
                    curGroup.add(same_group_val);
                    if (i != same_group_val){
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

    public HashMap<Integer, Integer> populateStmtGroupMap(ArrayList<Set<Integer>> groupedStmtGroups){
        HashMap<Integer, Integer> linkedStmt2Group = new HashMap<> ();
        for (int i = 0; i < groupedStmtGroups.size(); i++){
            Set<Integer> curGroup = groupedStmtGroups.get(i);
            for (Integer g: curGroup){
                linkedStmt2Group.put(g, (Integer) i);
            }
        }
        return linkedStmt2Group;
    }

}
