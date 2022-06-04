package edu.ucla.se.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.BiFunction;
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
    private final HashMap<Integer, HashMap<String, ArrayList<Integer>>> group2Loc = new HashMap<>();

    private Set<Integer> editSetIds;
    private int maxMethodEditLen;

    public GroupLinkedDiffs(List<List<Integer>> methodlinkedEdits){
        this.methodLinkedEdits = methodlinkedEdits;
        this.populateGroup2Locations(methodlinkedEdits);
    }

    private void populateGroup2Locations(List<List<Integer>> methodLinkedEdits){
        this.editSetIds = new HashSet<>();
        this.maxMethodEditLen = 0;
        for (int i= methodLinkedEdits.size() - 1; i >= 0 ; i--){
            if (methodLinkedEdits.get(i).isEmpty()){
                methodLinkedEdits.remove(i);
            }
        }
        for (int i=0; i< methodLinkedEdits.size(); i++){
            int methodEditLen = methodLinkedEdits.get(i).size();
            if (maxMethodEditLen < methodEditLen){
                maxMethodEditLen = methodEditLen;
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

    private int getLinkSetAt(int method_i,  int index_j){
        if (method_i < this.methodLinkedEdits.size() && index_j < this.methodLinkedEdits.get(method_i).size()){
            return this.methodLinkedEdits.get(method_i).get(index_j);
        }
        else{
            return -1;
        }
    }

    private List<Integer> getLinkSetAt(int method_i, int index_j_from,  int index_j_to){

        int j_size = this.methodLinkedEdits.get(method_i).size();
        if (method_i < this.methodLinkedEdits.size() && index_j_from < j_size && index_j_to <= j_size){
            return this.methodLinkedEdits.get(method_i).subList(index_j_from, index_j_to);
        }
        else{
            return null;
        }
    }

    public HashMap<List<Integer>, Set<List<Integer>>> getLinkedStmtGroups(){
        /**
        System.out.println("group2Loc");
        System.out.println(this.group2Loc);
        System.out.println("editSetIds");
        System.out.println(this.editSetIds);
        System.out.println();
         */
        HashMap<List<Integer>, Set<List<Integer>>> resultEditGroups = new HashMap<> ();

        Set<Integer> visitedGroups = new HashSet<>();

        for (Integer set_i : this.editSetIds){
            //System.out.println("set_i=" + set_i.toString());
            if (visitedGroups.contains(set_i)){
                continue;
            }
            visitedGroups.add(set_i);

            List<Integer> method_locs = group2Loc.get(set_i).get("method");
            List<Integer> init_locs = group2Loc.get(set_i).get("index");
            List<Integer> cur_locs = new ArrayList<>();
            cur_locs.addAll(init_locs);
            /**
            System.out.println("method_locs" + method_locs.toString());
            System.out.println("init_locs" + init_locs.toString());
            System.out.println("cur_locs" + cur_locs.toString());
             */

            Set<Integer> chosenLocInds = new HashSet<>(IntStream.range(0, init_locs.size()).boxed().
                    collect(Collectors.toList()));
            HashMap<List<Integer>, List<List<Integer>>> group_index_intervals = new HashMap<> ();
            for (int j = 1; j < this.maxMethodEditLen+2; j++) {
                /**
                System.out.println("set_i=" + set_i);
                System.out.println("j=" + j);
                 */
                if (chosenLocInds.isEmpty()){
                    break;
                }

                for (int k = 0 ; k < cur_locs.size(); k++){
                    cur_locs.set(k, cur_locs.get(k) + 1);
                }

                /**
                System.out.println("method_locs");
                System.out.println(method_locs);
                System.out.println("init");
                System.out.println(init_locs);
                System.out.println("cur_locs");
                System.out.println(cur_locs);
                 */

                ArrayList<Integer> group_vals = new ArrayList<>(IntStream.range(0, cur_locs.size())
                        .mapToObj(k -> this.getLinkSetAt(method_locs.get(k), cur_locs.get(k)))
                        .collect(Collectors.toList()));

                //System.out.println("group_vals");
                //System.out.println(group_vals);

                HashMap<List<Integer>, List<Integer>> group_locations = new HashMap<> ();
                HashMap<List<Integer>, List<Integer>> null_group_locations = new HashMap<> ();
                HashMap<List<Integer>, List<List<Integer>>> last_group_index_intervals = group_index_intervals;
                group_index_intervals = new HashMap<> ();
                HashMap<List<Integer>, List<List<Integer>>> null_group_index_intervals = new HashMap<> ();

                for (Integer loc_i : chosenLocInds){
                    Integer method_loc = method_locs.get(loc_i);
                    Integer init_loc = init_locs.get(loc_i);
                    Integer cur_loc = cur_locs.get(loc_i);

                    List<Integer> editList = this.getLinkSetAt(method_loc, init_loc, cur_loc);

                    if (editList != null) {
                        /**
                        System.out.println("add group location");
                        System.out.println(editList);
                        System.out.println(loc_i);
                         */
                        if ( !group_index_intervals.containsKey(editList) ){
                            group_index_intervals.put(editList, new ArrayList<>());
                        }
                        group_index_intervals.get(editList).add(new ArrayList<>(
                                Arrays.asList(method_loc, init_loc, cur_loc)));

                        if ( !group_locations.containsKey(editList)){
                            group_locations.put(editList, new ArrayList<>());
                        }
                        group_locations.get(editList).add(loc_i);
                    }
                    else{
                        cur_loc --;
                        editList = this.getLinkSetAt(method_loc, init_loc, cur_loc);
                        /**System.out.println("null group location");
                        System.out.println(editList);
                        System.out.println(loc_i);*/
                        if ( !null_group_index_intervals.containsKey(editList) ){
                            null_group_index_intervals.put(editList, new ArrayList<>());
                        }
                        null_group_index_intervals.get(editList).add(new ArrayList<>(
                                Arrays.asList(method_loc, init_loc, cur_loc)));

                        if ( !null_group_locations.containsKey(editList)){
                            null_group_locations.put(editList, new ArrayList<>());
                        }
                        null_group_locations.get(editList).add(loc_i);
                    }

                }
                /**
                System.out.println("group index intervals");
                System.out.println(group_index_intervals);
                 */

                chosenLocInds = new HashSet<>();
                for (List<Integer> editList : group_locations.keySet()){
                    /**
                    System.out.println("group_locations[editList] size");
                    System.out.println(group_locations.get(editList).size());
                     */
                    if (group_locations.get(editList).size() > 1){
                        chosenLocInds.addAll(group_locations.get(editList));
                        /**
                        System.out.println("next init");
                        System.out.println(chosenLocInds);
                         */

                    }
                    else {
                        /**
                        System.out.println("add group (freq 1) ");
                        System.out.println(editList);
                         */

                        List<List<Integer>> curLocs = group_index_intervals.get(editList);
                        for (int k=0; k < curLocs.size(); k++){
                            List<Integer> curGroupEditList = curLocs.get(k);
                            curGroupEditList.set(2, curGroupEditList.get(2) - 1);
                        }

                        if (editList.size() > 1) {
                            editList = editList.subList(0, editList.size() - 1);
                        }

                        //System.out.println(curLocs);

                        if (!resultEditGroups.containsKey(editList)){
                            resultEditGroups.put(editList, new HashSet<>());
                            for (Integer editGroup : editList){
                                visitedGroups.add(editGroup);
                            }
                        }
                        resultEditGroups.get(editList).addAll(curLocs);
                    }
                }
                for (List<Integer> editList : null_group_locations.keySet()){
                    /**
                    System.out.println("null_group_locations[editList] size");
                    System.out.println(null_group_locations.get(editList).size());

                    System.out.println("add group (null) ");
                    System.out.println(editList);
                     */

                    List<List<Integer>> curLocs = null_group_index_intervals.get(editList);
                    //System.out.println(curLocs);

                    if (!resultEditGroups.containsKey(editList)){
                        resultEditGroups.put(editList, new HashSet<>());
                        for (Integer editGroup : editList){
                            visitedGroups.add(editGroup);
                        }
                    }
                    resultEditGroups.get(editList).addAll(curLocs);
                }
                //System.out.println();

            }
        }
        //System.out.println(resultEditGroups);

        return resultEditGroups;

    }

}
