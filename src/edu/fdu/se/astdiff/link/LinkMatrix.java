package edu.fdu.se.astdiff.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by huangkaifeng on 2018/3/19.
 *
 */
public class LinkMatrix {

    private int[][] matrix;
    public LinkMatrix(int size){
        matrix = new int[size][size];
    }

    public void buildLink(int a,int b,int linkType){
        matrix[a][b] = linkType;
        matrix[b][a] = linkType;
    }
    public static int DATA_LINK = 1;
    public static int CONTROL_LINK = 2;
    public static int METHODINVOCATION_LINK = 3;

    public List<List<Integer>> doCluster(){
        Map<Integer,Integer> tmpMap = new HashMap<>();
        List<List<Integer>> result = new ArrayList<>();
        for(int i=0;i<this.matrix.length;i++){
            tmpMap.put(i,1);
        }
        for(int i=0;i<this.matrix.length;i++){
            if(tmpMap.get(i)==1){
                List<Integer> newline = new ArrayList<>();
                int[] line = matrix[i];
                newline.add(i);
                for(int j=0;j<line.length;j++){
                    if(line[j]!=0){
                        newline.add(j);
                        tmpMap.put(j,0);
                    }
                }
                result.add(newline);
            }
        }
        return result;
    }
}
