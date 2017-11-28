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

package edu.fdu.se.main.ast;

import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.JGraphPanel;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.TreeContext;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyActionClusterFinder {

    private TreeContext src;

    @SuppressWarnings("unused")
	private TreeContext dst;

    @SuppressWarnings("unused")
	private List<Action> actions;

    private DirectedGraph<Action, DefaultEdge> graph;

    private List<Set<Action>> clusters;
    
    public List<Action> startNodes = new ArrayList<>();
    
    public List<Integer> lastChildrenIndex;
    
    public List<Integer> actionIndexList;

    public MyActionClusterFinder(TreeContext src, TreeContext dst, List<Action> actions) {
        this.src = src;
        this.dst = dst;
        this.actions = actions;
        startNodes.addAll(actions);
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (Action a: actions)
            graph.addVertex(a);
        doCluster();
        
//        for (Action a1: actions) {
//            for (Action a2: actions) {
//                if (a1 != a2) {
//                    if (embeddedInserts(a1, a2) || sameValueUpdates(a1, a2) || sameParentMoves(a1, a2) || embeddedDeletes(a1, a2)){
//                    	graph.addEdge(a1, a2);
//                    	startNodes.remove(a1);
//                    }
//                }
//            }
//        }
//
//        ConnectivityInspector<Action, DefaultEdge> alg = new ConnectivityInspector<>(graph);
//        clusters = alg.connectedSets();
        
    }
    
    public MyActionClusterFinder(TreeContext src, TreeContext dst, List<Action> actions,List<Integer> actionIndexList,List<Integer> lastChildrenIndex) {
        this.src = src;
        this.dst = dst;
        this.actions = actions;
        this.lastChildrenIndex = lastChildrenIndex;
        this.actionIndexList= actionIndexList;
        System.out.println("LastChildrenIndex:");
        for(int tmp:this.lastChildrenIndex){
        	System.out.print(tmp+" ");
        }
        System.out.println("");
        System.out.println("ActionIndex:");
        for(int tmp:this.actionIndexList){
        	System.out.print(tmp+" ");
        }
        System.out.println("");
        startNodes.addAll(actions);
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (Action a: actions)
            graph.addVertex(a);
    }
    public MyActionClusterFinder(){
    	this.actionIndexList= new ArrayList<Integer>();
    	this.lastChildrenIndex=new ArrayList<Integer>();
    	this.actionIndexList.add(15);
    	this.actionIndexList.add(16);
    	this.actionIndexList.add(17);
    	
    	this.lastChildrenIndex.add(1);
    	this.lastChildrenIndex.add(2);
    	this.lastChildrenIndex.add(5);
    	this.lastChildrenIndex.add(8);
    	this.lastChildrenIndex.add(10);
    	this.lastChildrenIndex.add(12);
    	this.lastChildrenIndex.add(15);
    	this.lastChildrenIndex.add(16);
    	this.lastChildrenIndex.add(17);
    }
    public static void main(String args[]){
    	MyActionClusterFinder d = new MyActionClusterFinder();
    	for(int item : d.candidateRange(1)){
    		System.out.println(item);
    	}
    	
    	
    }
    /**
     * 
     */
    public void doCluster(){
    	int size = actions.size();
    	for(int index=0;index<size;index++){
    		Action item = actions.get(index);
    		int [] twoRanges=this.candidateRange(index);
    		this.findParentOf(item, twoRanges[0], twoRanges[1]);
    		this.findSiblingsOf(item,twoRanges[2],twoRanges[3]);
    	}
    }
    
    
    /**
     * 
     * @param indx index of action in action list
     * @return start and end index in bfs node list -> start and end node index in action list
     */
    private int[] candidateRange(int indx){
    	int bfsActionIndex = this.actionIndexList.get(indx);
    	int i;
    	int num;
    	boolean flag=true;
    	for(i=0;i<this.lastChildrenIndex.size();i++){
    		num = this.lastChildrenIndex.get(i);
    		if(num >= bfsActionIndex){
    			flag = false;
    			break;
    		}
    	}
    	int[] intermediate = {this.lastChildrenIndex.get(i-2),this.lastChildrenIndex.get(i-1),bfsActionIndex};
    	System.out.println("Intermediate:");
    	for(int a:intermediate)
    		System.out.println(a);
    	int a = intermediate[0];
    	int b = intermediate[1];
    	int c = intermediate[2];
    	boolean flagA=false;
    	boolean flagB=false;
    	boolean flagC=false;
    	boolean flagD=false;
    	int indexA=-1,indexB=-1,indexC=-1,indexD=-1;
    	//[a+1,b]  [b+1,c-1]
    	for(int j=0;j<this.actionIndexList.size();j++){
    		int tmp = this.actionIndexList.get(j);
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
    	return result;
    	//bug
//    	int j;
//    	boolean flagA = true;
//    	boolean flagC = true;
//    	int indexA=-1;
//    	int indexB=-1;
//    	int indexC=-1;
//    	// action list 36  37 38   36 38 38
//    	// [36,36] [37,37]
//    	// action list 1  3 5  7  9  ast 的 4  7
//    	for(j=0;j<this.actionIndexList.size();j++){
//    		int bfsNum = this.actionIndexList.get(j);
//    		if(a<=bfsNum&&flagA){
//    			if(bfsNum==a){
//    				indexA = j+1;
//    			} else {
//    				indexA =j;
//    			}
//    			flagA=false;
//    		}
//    		if(bfsNum<=b){
//    			indexB = j;
//    		}
//    		if(bfsNum>=c&&flagC){
//    			if(bfsNum==c){
//    				indexC = j-1;
//    			}else{
//    				indexC = j;
//    			}
//    			flagC = false;
//    		}
//    	}
//    	// [a,b] [b+1,c]
//    	int[] result={indexA,indexB,indexC};
////    	System.out.println("Action Index:");
////    	for(int tmp:result)
////    		System.out.println(tmp);
//    	return result;
    }
    
    public void findParentOf(Action a,int start,int end){
    	for(int i=start;i<=end;i++){
    		Action candidate = this.actions.get(i);
    		if(this.isParentOf(candidate, a)){
    			graph.addEdge(candidate, a);
    			startNodes.remove(a);
//    			System.out.println("\nMapping: "+candidate.getNode().getId() + " and " + a.getNode().getId()+"\n");
    		}
    	}
    }
    public void findSiblingsOf(Action a,int start,int end){
    	for(int i=start;i<end;i++){
    		Action candidate = this.actions.get(i);
    		if(this.isSameParent(candidate, a)){
    			graph.addEdge(candidate, a);
    			startNodes.remove(a);
    			System.out.println("\nMapping: "+candidate.getNode().getId() + " and " + a.getNode().getId()+"\n");
    		}
    	}
    }

    private boolean isParentOf(Action a1,Action a2){
    	if(a1.getNode()==null||a2.getNode()==null){
    		return false;
    	}
    	if ((a1 instanceof Insert && a2 instanceof Insert)){
    		Insert i1 = (Insert)a1;
    		Insert i2 = (Insert)a2;
    		if (i2.getParent().equals(i1.getNode()))
                return true;
 
    	}
    	if((a1 instanceof Update && a2 instanceof Update)||(a1 instanceof Delete && a2 instanceof Delete)){
    		if(a1.getNode().equals(a2.getNode().getParent()))
    			return true;
    	}
        return false;
    }
    private boolean isSameParent(Action a1,Action a2){
    	if(a1.getNode()==null||a2.getNode()==null){
    		return false;
    	}
    	if ((a1 instanceof Insert && a2 instanceof Insert)){
    		Insert i1 = (Insert)a1;
    		Insert i2 = (Insert)a2;
    		if (i2.getParent().equals(i1.getParent()))
                return true;
 
    	}
    	if((a1 instanceof Update && a2 instanceof Update)||(a1 instanceof Delete && a2 instanceof Delete)){
    		if(a1.getNode().getParent()==null)
    			return false;
    		if(a1.getNode().getParent().equals(a2.getNode().getParent()))
    			return true;
    	}
        return false;
    }
    
    public List<List<Action>> clusteredActions(){
    	doCluster();
    	List<List<Action>> result =new ArrayList<List<Action>>();
		for(Action item : this.getStartNodes()){
			List<Action> oneEntry =new ArrayList<Action>();
			oneEntry.add(item);
			for(DefaultEdge edge : graph.edgesOf(item)){
				Action a1=graph.getEdgeSource(edge);
				Action a2=graph.getEdgeTarget(edge);
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
    
    
    
    public List<Action> getStartNodes() {
        return startNodes;
    }
    public List<Set<Action>> getClusters() {
        return clusters;
    }
    public void show(){
    	JGraphPanel frame = new JGraphPanel(graph, clusters);
    	frame.init();
    }
    
//    /**
//     * 
//     * @param a1
//     * @param a2
//     * @return true if a1 is a2's parent
//     */
//    private boolean embeddedInserts(Action a1, Action a2) {
//        if (!(a1 instanceof Insert && a2 instanceof Insert))
//            return false;
//        Insert i1 = (Insert) a1;
//        Insert i2 = (Insert) a2;
//        if (i2.getParent().equals(i1.getNode()))
//            return true;
//        else
//            return false;
//    }
    
//    private boolean embeddedAdditions(Action a1, Action a2) {
//        if (!(a1 instanceof Addition && a2 instanceof Addition))
//            return false;
//        Addition i1 = (Addition) a1;
//        Addition i2 = (Addition) a2;
//        if (i1.getParent().equals(i2.getNode()))
//            return true;
//        else
//            return false;
//    }
    
//    private boolean embeddedDeletes(Action a1, Action a2) {
//        if (!(a1 instanceof Delete && a2 instanceof Delete))
//            return false;
//        Delete d1 = (Delete) a1;
//        Delete d2 = (Delete) a2;
//        if (d2.getNode().getParent() == null)
//            return false;
//        if (d2.getNode().getParent().equals(d1.getNode()))
//            return true;
//        else
//            return false;
//    }

//    private boolean sameParentMoves(Action a1, Action a2) {
//        if (!(a1 instanceof Move && a2 instanceof Move))
//            return false;
//        Move m1 = (Move) a1;
//        Move m2 = (Move) a2;
//        if (m1.getNode() == null)
//            return false;
//        if (m2.getNode() == null)
//            return false;
//        if (m1.getNode().getParent().equals(m2.getNode().getParent()))
//            return true;
//        else
//            return false;
//    }

    private boolean sameValueUpdates(Action a1, Action a2) {
        if (!(a1 instanceof Update && a2 instanceof Update))
            return false;
        Update u1 = (Update) a1;
        Update u2 = (Update) a2;
        if (u1.getValue().equals(u2.getValue()))
            return true;
        else
            return false;
    }

    public String getClusterLabel(Set<Action> cluster) {
        if (cluster.size() == 0)
            return "Unknown cluster type";
        Action first = cluster.iterator().next();
        if (first instanceof Insert) {
            Insert root = null;
            for (Action a : cluster)
                if (graph.inDegreeOf(a) == 0)
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
                if (graph.inDegreeOf(a) == 0)
                    root = (Delete) a;
            return root.format(src);
        } else
            return "Unknown cluster type";
    }

}
