package edu.fdu.se.base.webapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 */
public class MergeIntervals {

    public class IntervalCmp implements Comparator<Integer[]> {
        @Override
        public int compare(Integer[] i1, Integer[] i2) {
            if (i1[0] < i2[0]) return -1;
            if (i1[0] == i2[0] && i1[1] <= i2[1]) return -1;
            return 1;
        }
    }

    public List<Integer[]> merge(List<Integer[]> intervals) {
        List<Integer[]> ret = new ArrayList<>();
        if (intervals.size() == 0) return ret;
        Integer[][] arr = new Integer[intervals.size()][2];
        intervals.toArray(arr);
        Arrays.sort(arr, new IntervalCmp());
        int start = arr[0][0];
        int end = arr[0][1];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] <= end) {
                end = Math.max(end, arr[i][1]);
            } else {
                Integer[] tmpRange = {start,end};
                ret.add(tmpRange);
                start = arr[i][0];
                end = arr[i][1];
            }
        }
        Integer[] tmpRange = {start,end};
        ret.add(tmpRange);
        return ret;
    }
}
