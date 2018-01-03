package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ConsolePrint;

/**
 * Most of our basic work on this java class Input: 4 kinds of edit action,
 * insert,move,update,delete Output: sort of summary of actions
 * 
 * @author huangkaifeng
 *
 */
public class FindPattern {

	public FindPattern(MiningActionBean bean) {
		this.mMiningActionBean = bean;
		this.mHighLevelOperationBeanList = new ArrayList<HighLevelOperationBean>();
	}

	private MiningActionBean mMiningActionBean;
	private List<HighLevelOperationBean> mHighLevelOperationBeanList;
	
	public TreeContext getDstTree(){
		return this.mMiningActionBean.mDstTree;
	}
	
	public TreeContext getSrcTree(){
		return this.mMiningActionBean.mSrcTree;
	}
	public String getDstTreeContextTypeLabel(ITree node){
		return this.mMiningActionBean.mDstTree.getTypeLabel(node);
	}
	
	public String getSrcTreeContextTypeLabel(ITree node){
		return this.mMiningActionBean.mSrcTree.getTypeLabel(node);
	}
	
	
	
	public void setActionTraversedMap(List<Action> actionList){
		this.mMiningActionBean.setActionTraversedMap(actionList);
	}
	
	public ITree getMappedSrcOfDstNode(ITree dst){
		return this.mMiningActionBean.mMapping.getSrc(dst);
	}
	public ITree getMappedDstOfSrcNode(ITree src){
		return this.mMiningActionBean.mMapping.getDst(src);
	}

	/**
	 * main level-I 入口 思路：围绕if/else/else if 和method call来抽 AST 节点：
	 * VariableDeclarationStatement，ExpressionStatement，IfStatement
	 */
	public void find() {
		// this.findMove();
		this.findInsert();
		this.findUpdate();
		this.findDelete();
	}
	
	public void find2(){
		for(HighLevelOperationBean item:this.mHighLevelOperationBeanList){
			if(item.getOperationEntity().equals("IF")){
				Tree node = (Tree) item.getCurNode();
				Tree parent = (Tree) node.getParent();
				String parentType = null;
				if(item.getCurAction() instanceof Insert){
					parentType = this.mMiningActionBean.mDstTree.getTypeLabel(parent);
					if(parentType.equals(StatementConstants.CATCHCLAUSE)){
						System.out.println("在catch里增加了if语句");
					}
				}else{
					parentType = this.mMiningActionBean.mSrcTree.getTypeLabel(parent);
					if(parentType.equals(StatementConstants.CATCHCLAUSE)){
						System.out.println("在catch里删除了if语句");
					}
				}
				
			}
		}
	}

	/**
	 * Sub level-II
	 */
	public void findInsert() {
		int insertActionCount = this.mMiningActionBean.mActionGeneratorBean.getInsertActions().size();
		int insertActionIndex = 0;
		while (true) {
			if (insertActionIndex == insertActionCount) {
				break;
			}
			Action a = this.mMiningActionBean.mActionGeneratorBean.getInsertActions().get(insertActionIndex);
			insertActionIndex++;
			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
				// 标记过的action
				continue;
			}
			Insert ins = (Insert) a;
			ITree insNode = ins.getNode();
			String type = this.mMiningActionBean.mDstTree.getTypeLabel(insNode);
			ITree fafafather = AstRelations.findFafafatherNode(insNode, this.mMiningActionBean.mDstTree);
			String fatherType = this.mMiningActionBean.mDstTree.getTypeLabel(fafafather);
			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mDstTree);
			System.out.print(nextAction);
			if(StatementConstants.FIELDDECLARATION.equals(type)){
				//
			} else if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
				//
			} else if (StatementConstants.METHODDECLARATION.equals(type)) {
				MatchNewOrDeleteMethod.matchNewOrDeleteMethod(this,a);
				continue;
			} else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
				System.out.println(insNode.getParent().getChildPosition(insNode));
				MatchMethodSignatureChange.matchMethodSignatureChange(this,a, fafafather);
			} else {
				// 方法体
				switch (type) {
					case StatementConstants.IFSTATEMENT:
						// Pattern 1. Match If/else if
						MatchIfElse.matchIf(this,a, type);
						break;
					case StatementConstants.BLOCK:
						if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mDstTree)) {
							// Pattern 1.2 Match else
							MatchIfElse.matchElse(this,a);
						}else if(AstRelations.isChildCotainSynchronizedStatement(a,this.mMiningActionBean.mDstTree)) {
							//同步语句块增加
							MatchSynchronized.matchSynchronized(this,a);
						}else {
							System.err.println("Other Condition");
							// TODO剩下的情况
						}
						break;
					case StatementConstants.TRYSTATEMENT:
						MatchTry.matchTry(this,a);
						break;
					case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
						MatchVariableDeclarationExpression.matchVariableDeclaration(this,a);
						break;
					case StatementConstants.EXPRESSIONSTATEMENT:
						if(AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mDstTree))
							// Pattern 1.2 Match else
							MatchIfElse.matchElse(this,a);
						else
							MatchExpressionStatement.matchExpression(this,a);
						break;
					case StatementConstants.SYNCHRONIZEDSTATEMENT:
						//同步语句块增加
						MatchSynchronized.matchSynchronized(this,a);
						break;
					case StatementConstants.SWITCHSTATEMENT:
						//增加switch语句
						MatchSwitch.matchSwitch(this,a);
						break;
					// 方法参数
					case StatementConstants.SIMPLENAME:
					case StatementConstants.STRINGLITERAL:
					case StatementConstants.NULLLITERAL:
					case StatementConstants.CHARACTERLITERAL:
					case StatementConstants.NUMBERLITERAL:
					case StatementConstants.BOOLEANLITERAL:
					case StatementConstants.INFIXEXPRESSION:
					case StatementConstants.METHODINVOCATION:
						MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, this.mMiningActionBean.mDstTree);
						break;
					default:
						System.err.println("Default1:" + type);
						break;
				}
			}

		}
	}

	/**
	 * Sub level-II
	 */
	public void findDelete() {
		int deleteActionCount = this.mMiningActionBean.mActionGeneratorBean.getDeleteActions().size();
		int index = 0;
		int count = 0;
		String resultStr = null;
		while (true) {
			if (index == deleteActionCount) {
				break;
			}
			Action a = this.mMiningActionBean.mActionGeneratorBean.getDeleteActions().get(index);
			index++;
			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
				continue;
			}
			Delete del = (Delete) a;
			ITree tmp = a.getNode();
			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(tmp);
			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
			ITree fafafather = AstRelations.findFafafatherNode(tmp, this.mMiningActionBean.mSrcTree);
			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
			System.out.print(nextAction);
			if (StatementConstants.METHODDECLARATION.equals(type)) {
				// 删除方法体
				HighLevelOperationBean bean  = MatchNewOrDeleteMethod.matchNewOrDeleteMethod(this,a);
				continue;
			}
			if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
				// 删除方法参数
			} else {
				// 方法体内部
				switch (type) {
				case StatementConstants.IFSTATEMENT:
					MatchIfElse.matchIf(this,a, type);
					break;
				case StatementConstants.BLOCK:
					// Pattern 1.2 Match else
					if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mSrcTree)) {
						MatchIfElse.matchElse(this,a);
					}else if(AstRelations.isChildCotainSynchronizedStatement(a,this.mMiningActionBean.mDstTree)) {
						//同步语句块增加
						MatchSynchronized.matchSynchronized(this,a);
					} else {
						System.err.println("Other Condition");
						// TODO剩下的情况
					}
					break;
				case StatementConstants.EXPRESSIONSTATEMENT:
					// Pattern 1.2 Match else
					if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mSrcTree))
						MatchIfElse.matchElse(this,a);
					else
						MatchExpressionStatement.matchExpression(this,a);
					break;
				case StatementConstants.TRYSTATEMENT:
					MatchTry.matchTry(this,a);
					break;
				case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
					MatchVariableDeclarationExpression.matchVariableDeclaration(this,a);
					break;
				case StatementConstants.SYNCHRONIZEDSTATEMENT:
					//同步语句块增加
					MatchSynchronized.matchSynchronized(this,a);
					break;
				case StatementConstants.SWITCHSTATEMENT:
						//增加switch语句
						MatchSwitch.matchSwitch(this,a);
						break;
				// 方法参数
				case StatementConstants.SIMPLENAME:
				case StatementConstants.STRINGLITERAL:
				case StatementConstants.NULLLITERAL:
				case StatementConstants.CHARACTERLITERAL:
				case StatementConstants.NUMBERLITERAL:
				case StatementConstants.BOOLEANLITERAL:
				case StatementConstants.INFIXEXPRESSION:
				case StatementConstants.METHODINVOCATION:
					MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, this.mMiningActionBean.mDstTree);
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * Sub level-II
	 */
	public void findUpdate() {
		int updateActionCount = this.mMiningActionBean.mActionGeneratorBean.getUpdateActions().size();
		int index = 0;
		while (true) {
			if (index == updateActionCount) {
				break;
			}
			Action a = this.mMiningActionBean.mActionGeneratorBean.getUpdateActions().get(index);
			index++;
			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
				// 标记过的 update action
				continue;
			}
			Update up = (Update) a;
			ITree tmp = a.getNode();
			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(tmp);
			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
			ITree fafafather = AstRelations.findFafafatherNode(tmp, this.mMiningActionBean.mSrcTree);
			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
			System.out.print(nextAction);
			if (StatementConstants.METHODDECLARATION.equals(type)) {
				// 新增方法
				System.err.println("Update 应该不会是method declaration");
			} else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
				// 方法签名update
				if (StatementConstants.BLOCK.equals(type)) {
					System.err.println("Not considered");
				} else {
					MatchMethodSignatureChange.matchMethodSignatureChange(this,a, fafafather);
				}
			} else {
				// 方法体
				switch (type) {
				case StatementConstants.SIMPLENAME:
					MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, this.mMiningActionBean.mSrcTree);
					break;
				default:
					break;
				}
			}
		}

	}

	public void findMove() {
		int moveActionCount = this.mMiningActionBean.mActionGeneratorBean.getMoveActions().size();
		int index = 0;
		while (true) {
			if (index == moveActionCount) {
				break;
			}
			Action a = this.mMiningActionBean.mActionGeneratorBean.getMoveActions().get(index);
			index++;
			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
				// 标记过的 update action
				continue;
			}
			Move up = (Move) a;
			ITree moveNode = a.getNode();
			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(moveNode);
			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
			ITree fafafather = AstRelations.findFafafatherNode(moveNode, this.mMiningActionBean.mSrcTree);
			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
			System.out.print(nextAction);
			ITree dstNode = up.getParent();

		}

	}

//	/**
//	 * Sub level-II
//	 * 
//	 */
//	public void findStatementChange() {
//		for(Entry<String,List<ITree>> entry : this.mMiningActionBean.fatherTypeToFathersMap.entrySet()){
//			String type = entry.getKey();
//			List<ITree> value = entry.getValue();
//			switch(type){
//			case ActionConstants.METHODINVOCATION:
//			case ActionConstants.IfPredicate:
//			case ActionConstants.ForPredicate:
//			case ActionConstants.ExpressionStatement:
//			case ActionConstants.VariableDelcarationStatement:
//				break;
//				//TODO
//			default:
//				break;
//			}
//		}
//		for (Entry<ITree, List<Action>> item : this.mMiningActionBean.fatherToActionMap.entrySet()) {
//			Tree srcParentNode = (Tree) item.getKey();
//			Tree dstParentNode = (Tree) this.mMiningActionBean.mMapping.getDst(srcParentNode);
//			List<Action> mList = item.getValue();
//			Set<String> mSet = this.mMiningActionBean.fatherToActionChangeTypeMap.get(item.getKey());
//			int lenSrc = srcParentNode.getChildren().size();
//			int lenDst = dstParentNode.getChildren().size();
//			if (lenSrc > lenDst) {
//				if (mSet.contains(ActionConstants.UPDATE)) {
//					System.out.println("1.Altering method parameters -Delete&update parameter");
//				} else {
//					System.out.println("1.Altering method parameters - Delete parameter");
//				}
//			} else if (lenSrc < lenDst) {
//				if (mSet.contains(ActionConstants.UPDATE)) {
//					System.out.println("1.Altering method parameters - Add&update parameter");
//				} else {
//					System.out.println(
//							"1.Atering method parameters - Add parameter / 3.Calling another overloaded method with one more parameter");
//				}
//			} else {
//				System.out.println("1.Altering method parameters - Update parameter");
//			}
//		}
//	}
//	if (AstRelations.
// ifFatherNodeTypeSameAs(a, treeContext, StatementConstants.METHODINVOCATION)) {
//		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);


}
