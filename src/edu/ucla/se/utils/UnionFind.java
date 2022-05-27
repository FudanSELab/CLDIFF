package edu.ucla.se.utils;

import java.util.HashMap;

class UnionFind {
    public HashMap<Integer, Integer> father;
    private HashMap<Integer, Integer> sizeOfSet;
    private int numOfSet;

    public UnionFind() {
        father = new HashMap<>();
        sizeOfSet = new HashMap<>();
        numOfSet = 0;
    }

    public void add (Integer x) {
        if (father.containsKey(x)) {
            return;
        }

        father.put(x, null);
        sizeOfSet.put(x, 1);
        numOfSet += 1;
    }

    public Integer find(Integer x) {
        Integer root = x;
        while (father.get(root) != null) {
            root = father.get(root);
        }

        while (!x.equals(root)) {
            Integer originalFather = father.get(x);
            father.put(x, root);
            x = originalFather;
        }

        return root;
    }

    public void merge(Integer a, Integer b) {
        Integer rootX = find(a);
        Integer rootY = find(b);

        if (rootX.equals(rootY)) {
            return;
        }

        numOfSet -= 1;
        father.put(rootY, rootX);
        sizeOfSet.put(rootX, sizeOfSet.get(rootY) + sizeOfSet.get(rootX));
    }

    public int getNumOfSet() {
        return numOfSet;
    }

}