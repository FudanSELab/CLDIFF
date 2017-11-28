/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package edu.fdu.se.main.ast;
/**
 * generate action and cluster
 */
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.AbstractTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import edu.fdu.se.gumtree.MyTreeUtil;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyActionGenerator {

    private ITree origSrc;

    private ITree copySrc;

    private ITree origDst;

    private MappingStore origMappings;

    private MappingStore newMappings;

    private Set<ITree> dstInOrder;

    private Set<ITree> srcInOrder;

    private int lastId;

//    private List<Action> actions;

    private TIntObjectMap<ITree> origSrcTrees;

    private TIntObjectMap<ITree> copySrcTrees;

    public MyActionGenerator(ITree src, ITree dst, MappingStore mappings) {
        this.origSrc = src;
        this.copySrc = this.origSrc.deepCopy();
        this.origDst = dst;

        origSrcTrees = new TIntObjectHashMap<>();
        for (ITree t: origSrc.getTrees())
            origSrcTrees.put(t.getId(), t);
        copySrcTrees = new TIntObjectHashMap<>();
        for (ITree t: copySrc.getTrees())
            copySrcTrees.put(t.getId(), t);

        origMappings = new MappingStore();
        for (Mapping m: mappings)
            this.origMappings.link(copySrcTrees.get(m.getFirst().getId()), m.getSecond());
        this.newMappings = origMappings.copy();
        myAgbData = new ActionGeneratorBean();
    }


    public ActionGeneratorBean myAgbData;
    public ActionGeneratorBean generate() {
    	myAgbData = new ActionGeneratorBean();
        ITree srcFakeRoot = new AbstractTree.FakeTree(copySrc);
        ITree dstFakeRoot = new AbstractTree.FakeTree(origDst);
        copySrc.setParent(srcFakeRoot);
        origDst.setParent(dstFakeRoot);

        dstInOrder = new HashSet<>();
        srcInOrder = new HashSet<>();

        lastId = copySrc.getSize() + 1;
        newMappings.link(srcFakeRoot, dstFakeRoot);

//        List<ITree> bfsDst = TreeUtils.breadthFirst(origDst);
        List<ITree> bfsDst = MyTreeUtil.layeredBreadthFirst(origDst, myAgbData.dstLayerLastNodeIndex);
        
        for (int i=1;i<=bfsDst.size();i++){
        	ITree item = bfsDst.get(i-1);
            ITree w = null;
            ITree parentDst = item.getParent();
            ITree parentSrc = newMappings.getSrc(parentDst);

            if (!newMappings.hasDst(item)) {
            	//item is not in src
                int k = findPos(item);
                // Insertion case : insert new node.
                w = new AbstractTree.FakeTree();
                w.setId(newId());
                // In order to use the real nodes from the second tree, we
                // furnish x instead of w and fake that x has the newly
                // generated ID.
                Action ins = new Insert(item, origSrcTrees.get(parentSrc.getId()), k);
                myAgbData.dstTreeActions.add(ins);
                myAgbData.dstTreeActionIndex.add(i);

                origSrcTrees.put(w.getId(), item);
                newMappings.link(w, item);
                parentSrc.getChildren().add(k, w);
                w.setParent(parentSrc);
            } else {
            	//in 
                w = newMappings.getSrc(item);
                if (!item.equals(origDst)) { // TODO => x != origDst // Case of the root
                    ITree v = w.getParent();
                    if (!w.getLabel().equals(item.getLabel())) {
                    	Update upd = new Update(origSrcTrees.get(w.getId()), item.getLabel());
                    	myAgbData.dstTreeActions.add(upd);
                        myAgbData.dstTreeActionIndex.add(i);
                        w.setLabel(item.getLabel());
                    }
                    if (!parentSrc.equals(v)) {
                        int k = findPos(item);
                        Action mv = new Move(origSrcTrees.get(w.getId()), origSrcTrees.get(parentSrc.getId()), k);
                        myAgbData.dstTreeActions.add(mv);
                        myAgbData.dstTreeActionIndex.add(i);
                        //System.out.println(mv);
                        int oldk = w.positionInParent();
                        parentSrc.getChildren().add(k, w);
                        w.getParent().getChildren().remove(oldk);
                        w.setParent(parentSrc);
                    }
                }
            }

            //FIXME not sure why :D
            srcInOrder.add(w);
            dstInOrder.add(item);
            alignChildren(w, item,i);
        }
        List<ITree> bfsSrc = MyTreeUtil.layeredBreadthFirst(copySrc, myAgbData.srcLayerLastNodeIndex);
//        for (ITree w : copySrc.postOrder()) {
        for (int i=1;i<=bfsSrc.size();i++){
        	ITree w = bfsSrc.get(i);
            if (!newMappings.hasSrc(w)) {
            	myAgbData.srcTreeActions.add(new Delete(origSrcTrees.get(w.getId())));
            	myAgbData.srcTreeActionIndex.add(i);
                //w.getParent().getChildren().remove(w);
            }
//            count++;
        }
        return myAgbData;
        //FIXME should ensure isomorphism.
    }

    private void alignChildren(ITree w, ITree x,int nodeIndex) {
        srcInOrder.removeAll(w.getChildren());
        dstInOrder.removeAll(x.getChildren());

        List<ITree> s1 = new ArrayList<>();
        for (ITree c: w.getChildren())
            if (newMappings.hasSrc(c))
                if (x.getChildren().contains(newMappings.getDst(c)))
                    s1.add(c);

        List<ITree> s2 = new ArrayList<>();
        for (ITree c: x.getChildren())
            if (newMappings.hasDst(c))
                if (w.getChildren().contains(newMappings.getSrc(c)))
                    s2.add(c);

        List<Mapping> lcs = lcs(s1, s2);

        for (Mapping m : lcs) {
            srcInOrder.add(m.getFirst());
            dstInOrder.add(m.getSecond());
        }

        for (ITree a : s1) {
            for (ITree b: s2 ) {
                if (origMappings.has(a, b)) {
                    if (!lcs.contains(new Mapping(a, b))) {
                        int k = findPos(b);
                        Action mv = new Move(origSrcTrees.get(a.getId()), origSrcTrees.get(w.getId()), k);
                        myAgbData.dstTreeActions.add(mv);
                        myAgbData.dstTreeActionIndex.add(nodeIndex);
                        //System.out.println(mv);
                        int oldk = a.positionInParent();
                        w.getChildren().add(k, a);
                        if (k  < oldk ) // FIXME this is an ugly way to patch the index
                            oldk ++;
                        a.getParent().getChildren().remove(oldk);
                        a.setParent(w);
                        srcInOrder.add(a);
                        dstInOrder.add(b);
                    }
                }
            }
        }
    }

    private int findPos(ITree x) {
        ITree y = x.getParent();
        List<ITree> siblings = y.getChildren();

        for (ITree c : siblings) {
            if (dstInOrder.contains(c)) {
                if (c.equals(x)) return 0;
                else break;
            }
        }

        int xpos = x.positionInParent();
        ITree v = null;
        for (int i = 0; i < xpos; i++) {
            ITree c = siblings.get(i);
            if (dstInOrder.contains(c)) v = c;
        }

        //if (v == null) throw new RuntimeException("No rightmost sibling in order");
        if (v == null) return 0;

        ITree u = newMappings.getSrc(v);
        // siblings = u.getParent().getChildren();
        // int upos = siblings.indexOf(u);
        int upos = u.positionInParent();
        // int r = 0;
        // for (int i = 0; i <= upos; i++)
        // if (srcInOrder.contains(siblings.get(i))) r++;
        return upos + 1;
    }

    private int newId() {
        return ++lastId;
    }

    private List<Mapping> lcs(List<ITree> x, List<ITree> y) {
        int m = x.size();
        int n = y.size();
        List<Mapping> lcs = new ArrayList<>();

        int[][] opt = new int[m + 1][n + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (newMappings.getSrc(y.get(j)).equals(x.get(i))) opt[i][j] = opt[i + 1][j + 1] + 1;
                else  opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        while (i < m && j < n) {
            if (newMappings.getSrc(y.get(j)).equals(x.get(i))) {
                lcs.add(new Mapping(x.get(i), y.get(j)));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) i++;
            else j++;
        }

        return lcs;
    }

}
