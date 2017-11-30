package edu.fdu.se.astdiff.generatingactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;

public class ActionGeneratorBean {

	private List<Action> dstTreeActions;
	private List<Integer> dstLayerLastNodeIndex;
	private List<Integer> dstTreeActionIndex;

	private List<Action> srcTreeActions;
	private List<Integer> srcLayerLastNodeIndex;
	private List<Integer> srcTreeActionIndex;

	private List<Action> insertActions;
	private List<Action> updateActions;
	private List<Action> moveActions;
	private List<Action> deleteActions;

	private Map<Action, Integer> insertActionMap;
	private Map<Action, Integer> updateActionMap;
	private Map<Action, Integer> deleteActionMap;
	private Map<Action, Integer> moveActionMap;

	public ActionGeneratorBean() {
		super();
		this.dstTreeActions = new ArrayList<Action>();
		this.dstLayerLastNodeIndex = new ArrayList<Integer>();
		this.dstTreeActionIndex = new ArrayList<Integer>();

		this.srcTreeActions = new ArrayList<Action>();
		this.srcLayerLastNodeIndex = new ArrayList<Integer>();
		this.srcTreeActionIndex = new ArrayList<Integer>();
		this.insertActions = new ArrayList<Action>();
		this.moveActions = new ArrayList<Action>();
		this.updateActions = new ArrayList<Action>();
		this.deleteActions = this.srcTreeActions;

		this.startNodes = new ArrayList<>();
		this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
		
		this.insertActionMap = new HashMap<Action,Integer>();
		this.updateActionMap = new HashMap<Action,Integer>();
		this.deleteActionMap = new HashMap<Action,Integer>();
		this.moveActionMap = new HashMap<Action,Integer>();
	}

	public void addAction(Action action, int actionIndex) {
		if (action instanceof Insert) {
			this.dstTreeActions.add(action);
			this.dstTreeActionIndex.add(actionIndex);
			this.insertActions.add(action);
		} else if (action instanceof Update) {
			this.dstTreeActions.add(action);
			this.dstTreeActionIndex.add(actionIndex);
			this.updateActions.add(action);
		} else if (action instanceof Move) {
			this.dstTreeActions.add(action);
			this.dstTreeActionIndex.add(actionIndex);
			this.moveActions.add(action);
		} else if (action instanceof Delete) {
			if (actionIndex == -1) {
				this.srcTreeActions.add(action);
			} else {
				this.srcTreeActionIndex.add(this.srcTreeActions.size() - 1);
				this.srcTreeActions.add(action);
			}
		}
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

	public List<Action> startNodes;

	public void initClusterData() {
		this.startNodes.addAll(this.dstTreeActions);
		this.startNodes.addAll(this.srcTreeActions);
		for (Integer a : this.srcTreeActionIndex) {
			Action tmp = this.srcTreeActions.get(a);
			this.startNodes.remove(tmp);
		}
		for (Action a : this.dstTreeActions) {
			graph.addVertex(a);
		}
		for (Action a : this.srcTreeActions)
			graph.addVertex(a);

	}

	public List<Action> getInsertActions() {
		return insertActions;
	}

	public void setInsertActions(List<Action> insertActions) {
		this.insertActions = insertActions;
	}

	public List<Action> getUpdateActions() {
		return updateActions;
	}

	public void setUpdateActions(List<Action> updateActions) {
		this.updateActions = updateActions;
	}

	public List<Action> getMoveActions() {
		return moveActions;
	}

	public void setMoveActions(List<Action> moveActions) {
		this.moveActions = moveActions;
	}

	public List<Action> getDeleteActions() {
		return deleteActions;
	}

	public void setDeleteActions(List<Action> deleteActions) {
		this.deleteActions = deleteActions;
	}

	public Map<Action, Integer> getInsertActionMap() {
		return insertActionMap;
	}

	public Map<Action, Integer> getUpdateActionMap() {
		return updateActionMap;
	}

	public Map<Action, Integer> getDeleteActionMap() {
		return deleteActionMap;
	}

	public Map<Action, Integer> getMoveActionMap() {
		return moveActionMap;
	}

	public void generateActionMap() {
		
		for (Action tmp : this.insertActions) {
			this.insertActionMap.put(tmp, 0);
		}
		for (Action tmp : this.moveActions) {
			this.moveActionMap.put(tmp, 0);
		}
		for (Action tmp : this.updateActions) {
			this.updateActionMap.put(tmp, 0);
		}
		for (Action tmp : this.deleteActions) {
			this.deleteActionMap.put(tmp, 0);
		}
	}

}
