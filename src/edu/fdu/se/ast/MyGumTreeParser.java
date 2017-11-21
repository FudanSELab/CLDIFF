package edu.fdu.se.ast;

import java.util.List;

import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.GumTreeDiffParser;
import edu.fdu.se.fileutil.FileUtil;

public class MyGumTreeParser extends GumTreeDiffParser{
	public MyGumTreeParser(String oldFileName, String newFileName) {
		super(oldFileName, newFileName);
	}
	public void printClusteredActions(List<List<Action>> actionSet){
		StringBuilder sb = new StringBuilder();
		for(List<Action> oneEntry:actionSet){
			boolean isFirst=true;
			for(Action a:oneEntry){
				if(isFirst==true){
					isFirst=false;
				}
			}
		}
	}
	

}
