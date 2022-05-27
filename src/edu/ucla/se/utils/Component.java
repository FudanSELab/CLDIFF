package edu.ucla.se.utils;

import java.util.ArrayList;
import java.util.List;

public class Component {
    private List<Integer> changeItems;
    private List<Integer> changeIndex;
    private List<Integer> roots;
    private String description;
    private String functionName;
    public UnionFind uf;

    public Component(UnionFind uf) {
        changeItems = new ArrayList<>();
        changeIndex = new ArrayList<>();
        description = "";
        roots = new ArrayList<>();
        this.uf = uf;
    }

    public void setFunctionName(String functionName) {
        functionName = functionName;
    }

    public void calculateRoots() {
        for (Integer s : changeIndex) {
            roots.add(uf.find(s));
        }
    }

    public List<Integer> getRoots() {
        return roots;
    }

    public List<Integer> getChangeIndex() {
        return changeIndex;
    }

    public String getDescription() {
        return description;
    }

    public void addChangeIndex(int idx) {
        changeIndex.add(idx);
    }

//    public void addDescription(List<String> words) {
//        description = String.join(" ", words);
//    }
//
//    public void addChangeItems(List<String> words) {
//        String result = String.join(" ", words);
//        changeItems.add(result);
//        changeIndex.add(words.get(0));
//    }



}
