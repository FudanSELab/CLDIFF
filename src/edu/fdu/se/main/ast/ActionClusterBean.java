package edu.fdu.se.main.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.gumtreediff.actions.model.Action;

public class ActionClusterBean {
	
    private DirectedGraph<Action, DefaultEdge> graph;

    private List<Set<Action>> clusters;
    
    public List<Action> startNodes = new ArrayList<>();
    
    public ActionClusterBean(ActionGeneratorBean agb){
    	
    }

}
