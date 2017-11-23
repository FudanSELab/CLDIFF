package edu.fdu.se.main.ast;

import java.io.File;
import java.util.ArrayList;
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
import com.github.gumtreediff.tree.TreeUtils;

import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.GumTreeDiffParser;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MyGumTreeParser extends GumTreeDiffParser{
	public MyGumTreeParser(String oldFileName, String newFileName) {
		super(oldFileName, newFileName);
	}
	public void printClusteredActions(List<List<Action>> actionSet){
		String actionOutputFile = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/clusteredAction.txt";
		File mFile = new File(actionOutputFile);
		FileWriter.writeInSegments(mFile,"%%%%%%%%%%%%%%%%%   ClusterSize:"+actionSet.size()+"  %%%%%% Start:\n\n",FileWriter.FILE_NEW_AND_APPEND);
		for(List<Action> oneEntry:actionSet){
			boolean isFirst=true;
			FileWriter.writeInSegments(mFile,"@@@@ Cluter:   @@@@\n",1);
			for(Action a:oneEntry){
				if(isFirst==true){
					isFirst=false;
				}
				String oneAction = this.printMyOneActionString(a);
				FileWriter.writeInSegments(mFile, oneAction, FileWriter.FILE_APPEND_AND_NOT_CLOSE);
			}
			FileWriter.writeInSegments(mFile, "\n\n\n", FileWriter.FILE_APPEND_AND_NOT_CLOSE);
		}
		FileWriter.writeInSegments(mFile, "--------", FileWriter.FILE_APPEND_AND_CLOSE);
	}
	public String printMyOneActionString(Action a){
		StringBuilder sb = new StringBuilder();
		if(a instanceof Delete){
			sb.append("\tDelete > ");
			Delete delete = (Delete)a;
			ITree deleteNode = delete.getNode();
			sb.append(prettyString(dstTC,deleteNode)+" from "+prettyString(dstTC,deleteNode.getParent())+"\n");
			sb.append(delete.toString()+"\n");
		}
		if(a instanceof Insert){
			sb.append("\tInsert > ");
			Insert insert = (Insert)a;
			ITree insertNode = insert.getNode();
			sb.append(prettyString(dstTC,insertNode)+" to "+prettyString(dstTC,insertNode.getParent())+" at "+ insert.getPosition()+"\n");
			sb.append(insert.toString()+"\n");
			sb.append("Insert.getParent(Old Tree):\n");
			sb.append(prettyString(dstTC, insert.getParent())+"\n");//old
		}
		if(a instanceof Move){
			sb.append("\tMove > ");
			Move move = (Move)a;
			ITree moveNode = move.getNode();
			sb.append(prettyString(dstTC,moveNode)+" to "+prettyString(dstTC,move.getParent())+" at "+ move.getPosition()+"\n");//old
			sb.append(move.toString()+"\n");
			sb.append(move.getParent()==move.getNode().getParent());

			sb.append("\n------------Move.getParent(New Tree)-------------\n");
			sb.append(prettyString(dstTC, move.getParent())+"\n");//new
		}
		if(a instanceof Update){
			sb.append("\tUpdate > ");
			Update update = (Update)a;
			ITree updateNode = update.getNode();
			sb.append("from "+updateNode.getLabel()+" to "+update.getValue()+"\n");//old
		}
		sb.append("---------------Action.getNode--------------\n");
		sb.append(prettyString(dstTC, a.getNode())+"\n");
		//move-old,update-old,insert-new,delete-old
//		sb.append(toTreeString(dstTC, a.getNode()));
		sb.append("---------------Action.getNode.getParent--------\n");
		sb.append(prettyString(dstTC, a.getNode().getParent())+"\n");//move-old,update-old,insert-new,delete-old
//		sb.append(toTreeString(dstTC, a.getNode().getParent()));
		return sb.toString();
	}
	
	
	public void test(){
		List<Integer> num =new ArrayList<Integer>();
		MyTreeUtil.layeredBreadthFirst(this.src, num);
		System.out.println(num);
	}
	

}
