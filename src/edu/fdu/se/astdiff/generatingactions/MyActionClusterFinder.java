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
 * Copyright 2017 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package edu.fdu.se.astdiff.generatingactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.TreeContext;

import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyActionClusterFinder {

    private TreeContext src;

	private TreeContext dst;

	protected ActionGeneratorBean myAgb;
//    if (embeddedInserts(a1, a2) || sameValueUpdates(a1, a2) || sameParentMoves(a1, a2) || embeddedDeletes(a1, a2)){
//    ConnectivityInspector<Action, DefaultEdge> alg = new ConnectivityInspector<>(graph);
//    clusters = alg.connectedSets();
    
    
    public MyActionClusterFinder(TreeContext src, TreeContext dst,ActionGeneratorBean data) {
        this.src = src;
        this.dst = dst;
        this.myAgb = data;
        System.out.println("LastChildrenIndex:");
        for(int tmp:myAgb.dstLayerLastNodeIndex){
        	System.out.print(tmp+" ");
        }
        System.out.println("");
        System.out.println("ActionIndex:");
        for(int tmp:myAgb.dstTreeActionIndex){
        	System.out.print(tmp+" ");
        }
        System.out.println("");
        
    }
    public MyActionClusterFinder(){
//    	LastChildrenIndex:
//    		1 2 5 10 17 28 42 48 57 63 73 75 79 
//    		ActionIndex:
//    		13 20 21 31 32 33  
    	this.myAgb = new ActionGeneratorBean();
    	this.myAgb.dstTreeActionIndex= new ArrayList<Integer>();
    	this.myAgb.dstLayerLastNodeIndex=new ArrayList<Integer>();
    	
    	myAgb.dstTreeActionIndex.add(13);
    	myAgb.dstTreeActionIndex.add(20);
    	myAgb.dstTreeActionIndex.add(21);
    	myAgb.dstTreeActionIndex.add(31);
    	this.myAgb.dstTreeActionIndex.add(32);
    	this.myAgb.dstTreeActionIndex.add(33);
    	
    	myAgb.dstLayerLastNodeIndex.add(1);
    	myAgb.dstLayerLastNodeIndex.add(2);
    	myAgb.dstLayerLastNodeIndex.add(5);
    	myAgb.dstLayerLastNodeIndex.add(10);
    	myAgb.dstLayerLastNodeIndex.add(17);
    	myAgb.dstLayerLastNodeIndex.add(28);
    	myAgb.dstLayerLastNodeIndex.add(42);
    	myAgb.dstLayerLastNodeIndex.add(48);
    	myAgb.dstLayerLastNodeIndex.add(57);
    	myAgb.dstLayerLastNodeIndex.add(63);
    	myAgb.dstLayerLastNodeIndex.add(73);
    	myAgb.dstLayerLastNodeIndex.add(75);
    	myAgb.dstLayerLastNodeIndex.add(79);
    	
    	for(int i=0;i<this.myAgb.dstTreeActionIndex.size();i++){
    		this.candidateRange(i,this.myAgb.dstLayerLastNodeIndex,this.myAgb.dstTreeActionIndex);
    	}
    	
    }
    public static void main(String args[]){
    	MyActionClusterFinder d = new MyActionClusterFinder();
    }
    /**
     * cluster children 
     */
    public void doCluster(){
    	// ins upd mov
    	int size = this.myAgb.dstTreeActions.size();
    	for(int index=0;index<size;index++){
    		Action item = this.myAgb.dstTreeActions.get(index);
    		int [] twoRanges=this.candidateRange(index,this.myAgb.dstLayerLastNodeIndex,this.myAgb.dstTreeActionIndex);
    		this.findParentOf(item, twoRanges[0], twoRanges[1]);
    		this.findSiblingsOf(item,twoRanges[2],twoRanges[3]);
    	}
    }
    
    
    /**
     * 
     * @param indx index of action in action list
     * @return start and end index in bfs node list -> start and end node index in action list
     */
    private int[] candidateRange(int indx, List<Integer> layerNodeIndex,List<Integer> actionIndex){
    	int bfsActionIndex = actionIndex.get(indx);
    	int i;
    	int num;
    	for(i=0;i<layerNodeIndex.size();i++){
    		num = layerNodeIndex.get(i);
    		if(num >= bfsActionIndex){
    			break;
    		}
    	}
    	int[] intermediate = {layerNodeIndex.get(i-2),layerNodeIndex.get(i-1),bfsActionIndex};
//    	System.out.println("Intermediate:");
//    	for(int a:intermediate)
//    		System.out.println(a);
    	int a = intermediate[0];
    	int b = intermediate[1];
    	int c = intermediate[2];
    	boolean flagA=false;
    	boolean flagB=false;
    	boolean flagC=false;
    	boolean flagD=false;
    	int indexA=-1,indexB=-1,indexC=-1,indexD=-1;
    	//[a+1,b]  [b+1,c-1]
    	for(int j=0;j<actionIndex.size();j++){
    		int tmp = actionIndex.get(j);
    		if(tmp >= a+1 && !flagA){
    			flagA = true;
    			indexA = j;
    		}
    		if(tmp > b && !flagB){
    			flagB = true;
    			indexB =j-1;
    		}
    		if(tmp >= b+1 && !flagC){
    			flagC =true;
    			indexC = j;
    		}
    		if(tmp > c-1 && !flagD){
    			flagD = true;
    			indexD =j-1;
    		}
    	}
    	int[] result = {indexA,indexB,indexC,indexD};
//    	System.out.println("Result:");
//    	for(int item:result){
//    		System.out.println(item);
//    	}
    	return result;
    }
    
    public void findParentOf(Action a,int start,int end){
    	for(int i=start;i<=end;i++){
    		Action candidate = this.myAgb.dstTreeActions.get(i);
    		if(ActionNodeRelation.isParentOf(candidate, a)){
    			this.myAgb.graph.addEdge(candidate, a);
    			this.myAgb.startNodes.remove(a);
//    			System.out.println("\nMapping: "+candidate.getNode().getId() + " and " + a.getNode().getId());
    		}
    	}
    }
    public void findSiblingsOf(Action a,int start,int end){
    	for(int i=start;i<=end;i++){
    		Action candidate = this.myAgb.dstTreeActions.get(i);
    		if(ActionNodeRelation.isSameParent(candidate, a)){
    			this.myAgb.graph.addEdge(candidate, a);
    			this.myAgb.startNodes.remove(a);
//    			System.out.println("\nMapping: "+candidate.getNode().getId() + " and " + a.getNode().getId());
    		}
    	}
    }


    public List<List<Action>> clusteredActions(){
    	this.myAgb.initClusterData();
    	doCluster();
    	List<List<Action>> result =new ArrayList<List<Action>>();
    	
		for(Action item : this.myAgb.startNodes){
			List<Action> oneEntry =new ArrayList<Action>();
			oneEntry.add(item);
			for(DefaultEdge edge : this.myAgb.graph.edgesOf(item)){
				Action a1=this.myAgb.graph.getEdgeSource(edge);
				Action a2=this.myAgb.graph.getEdgeTarget(edge);
				if(item.equals(a1)){
					oneEntry.add(a2);
				}else{
//					oneEntry.add(a1);
				}
			}
			result.add(oneEntry);
		}
		return result;
    }


    public String getClusterLabel(Set<Action> cluster) {
        if (cluster.size() == 0)
            return "Unknown cluster type";
        Action first = cluster.iterator().next();
        if (first instanceof Insert) {
            Insert root = null;
            for (Action a : cluster)
                if (this.myAgb.graph.inDegreeOf(a) == 0)
                    root = (Insert) a;
            return root.format(src);
        } else if (first instanceof Move) {
            Move m = (Move) first;
            return "MOVE from " + m.getParent().toPrettyString(src);
        } else if (first instanceof Update) {
            Update u = (Update) first;
            return "UPDATE from " + first.getNode().getLabel() + " to " + u.getValue();
        } else if (first instanceof Delete) {
            Delete root = null;
            for (Action a : cluster)
                if (this.myAgb.graph.inDegreeOf(a) == 0)
                    root = (Delete) a;
            return root.format(src);
        } else
            return "Unknown cluster type";
    }

}
