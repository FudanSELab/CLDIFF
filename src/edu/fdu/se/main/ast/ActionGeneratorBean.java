package edu.fdu.se.main.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.gumtreediff.actions.model.Action;

public class ActionGeneratorBean {
	
	
	protected List<Action> dstTreeActions;
	protected List<Integer> dstLayerLastNodeIndex;
	protected List<Integer> dstTreeActionIndex;
	
	
	protected List<Action> srcTreeActions;
	protected List<Integer> srcLayerLastNodeIndex;
	protected List<Integer> srcTreeActionIndex;
	

	public ActionGeneratorBean() {
		super();
		this.dstTreeActions = new ArrayList<Action>();
		this.dstLayerLastNodeIndex = new ArrayList<Integer>();
		this.dstTreeActionIndex = new ArrayList<Integer>();
		
		this.srcTreeActions = new ArrayList<Action>();
		this.srcLayerLastNodeIndex =new ArrayList<Integer>();
		this.srcTreeActionIndex = new ArrayList<Integer>();
		this.startNodes = new ArrayList<>();
		this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
	}


	public List<Action> getDstTreeActions() {
		return dstTreeActions;
	}


	public List<Integer> getDstLayerLastNodeIndex() {
		return dstLayerLastNodeIndex;
	}


	public List<Integer> getDstTreeActionIndex() {
		return dstTreeActionIndex;
	}


	public List<Action> getSrcTreeActions() {
		return srcTreeActions;
	}


	public List<Integer> getSrcLayerLastNodeIndex() {
		return srcLayerLastNodeIndex;
	}


	public List<Integer> getSrcTreeActionIndex() {
		return srcTreeActionIndex;
	}
	
    protected DirectedGraph<Action, DefaultEdge> graph;

    private List<Set<Action>> clusters;
    
    public List<Action> startNodes ;
    
    public void initClusterData(){
    	this.startNodes.addAll(this.dstTreeActions);
    	this.startNodes.addAll(this.srcTreeActions);
    	for(Integer a:this.srcTreeActionIndex){
    		Action tmp = this.srcTreeActions.get(a);
    		this.startNodes.remove(tmp);
    	}
    	for(Action a:this.dstTreeActions){
    		graph.addVertex(a);
    	}
    	for (Action a: this.srcTreeActions)
            graph.addVertex(a);
    	
    }
	

}
