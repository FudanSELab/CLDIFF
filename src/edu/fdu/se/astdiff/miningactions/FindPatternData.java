package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;

/**
 * Most of our basic work on this java class Input: 4 kinds of edit action,
 * insert,move,update,delete Output: sort of summary of actions
 * 
 * @author huangkaifeng
 *
 */
public class FindPatternData {

	public FindPatternData(MiningActionBean bean) {
		this.mMiningActionBean = bean;
		this.mHighLevelOperationBeanList = new ArrayList<HighLevelOperationBean>();
	}

	protected MiningActionBean mMiningActionBean;

	protected List<HighLevelOperationBean> mHighLevelOperationBeanList;

	public void addHighLevelOperationBeanToList(HighLevelOperationBean mHighLevelOperationBean) {
		this.mHighLevelOperationBeanList.add(mHighLevelOperationBean);
	}
	
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

	public MiningActionBean getmMiningActionBean() {
		return mMiningActionBean;
	}

	public List<HighLevelOperationBean> getmHighLevelOperationBeanList() {
		return mHighLevelOperationBeanList;
	}

	public void setActionTraversedMap(List<Action> actionList){
		this.mMiningActionBean.setActionTraversedMap(actionList);
	}
	
	public void setActionTraversedMap(Action action){
		this.mMiningActionBean.setActionTraversedMap(action);
	}
	
	
	public ITree getMappedSrcOfDstNode(ITree dst){
		return this.mMiningActionBean.mMapping.getSrc(dst);
	}
	public ITree getMappedDstOfSrcNode(ITree src){
		return this.mMiningActionBean.mMapping.getDst(src);
	}




//	/**
//	 * Sub level-II
//	 */
//	public void findInsert() {
//		int insertActionCount = this.mMiningActionBean.mActionGeneratorBean.getInsertActions().size();
//		int insertActionIndex = 0;
//		while (true) {
//			if (insertActionIndex == insertActionCount) {
//				break;
//			}
//			Action a = this.mMiningActionBean.mActionGeneratorBean.getInsertActions().get(insertActionIndex);
//			insertActionIndex++;
//			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
//				// 标记过的action
//				continue;
//			}
//			Insert ins = (Insert) a;
//			ITree insNode = ins.getNode();
//			String type = this.mMiningActionBean.mDstTree.getTypeLabel(insNode);
//			ITree fafafather = AstRelations.findFafafatherNode(insNode, this.mMiningActionBean.mDstTree);
//			if(fafafather == null){
//				System.out.println("Father Null Condition: "+ActionConstants.getInstanceStringName(a) + " " +type);
//				this.setActionTraversedMap(a);
//				continue;
//			}
//			String fatherType = this.mMiningActionBean.mDstTree.getTypeLabel(fafafather);
////			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mDstTree);
////			System.out.print(nextAction);
//            HighLevelOperationBean operationBean;
//			if(StatementConstants.FIELDDECLARATION.equals(type)){
//				//insert FieldDeclaration
//				operationBean = MatchFieldDeclaration.matchFieldDeclaration(this,a,type);
//				mHighLevelOperationBeanList.add(operationBean);
//				continue;
//			} else if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
//				//insert FieldDeclaration body
//				operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(this,a,type,fafafather,fatherType);
//				mHighLevelOperationBeanList.add(operationBean);
//				continue;
//			}
//
//			if (StatementConstants.METHODDECLARATION.equals(type)) {
//                operationBean = MatchNewOrDeleteMethod.matchNewOrDeleteMethod(this,a,type);
//                mHighLevelOperationBeanList.add(operationBean);
//				continue;
//			} else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
//				System.out.println(insNode.getParent().getChildPosition(insNode));
//                operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(this,a, type,fafafather,fatherType);
//                mHighLevelOperationBeanList.add(operationBean);
//			} else {
//				// 方法体
//				switch (type) {
//					case StatementConstants.IFSTATEMENT:
//						// Pattern 1. Match If/else if
//                        operationBean = MatchIfElse.matchIf(this,a, type);
//                        mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.BLOCK:
//						MatchBlock.matchBlock(this,a, type,this.mMiningActionBean.mDstTree);
//						break;
//					case StatementConstants.BREAKSTATEMENT:
//						if(AstRelations.isFatherSwitchStatement(a, this.mMiningActionBean.mDstTree)) {
//							//增加switch语句
//							operationBean = MatchSwitch.matchSwitchCase(this, a, type, fafafather, fatherType);
//							mHighLevelOperationBeanList.add(operationBean);
//						}else {
//							System.out.println("Other Condition"+ActionConstants.getInstanceStringName(a) + " " +type);
//							this.setActionTraversedMap(a);
//							// TODO剩下的情况
//						}
//						break;
//					case StatementConstants.RETURNSTATEMENT:
//						MatchReturnStatement.matchReturnStatement(this,a, type,this.mMiningActionBean.mDstTree);
//						break;
//					case StatementConstants.FORSTATEMENT:
//						//增加for语句
//						operationBean = MatchForStatement.matchForStatement(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.ENHANCEDFORSTATEMENT:
//						//增加for语句
//						operationBean = MatchForStatement.matchEnhancedForStatement(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.WHILESTATEMENT:
//						//增加while语句
//						operationBean = MatchWhileStatement.matchWhileStatement(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.DOSTATEMENT:
//						//增加do while语句
//						operationBean = MatchWhileStatement.matchDoStatement(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.TRYSTATEMENT:
//						operationBean = MatchTry.matchTry(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.THROWSTATEMENT:
//						operationBean = MatchTry.matchThrowStatement(this,a,type,fafafather,fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
//						operationBean = MatchVariableDeclarationExpression.matchVariableDeclaration(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.EXPRESSIONSTATEMENT:
//						if(AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mDstTree)) {
//							// Pattern 1.2 Match else
//							operationBean = MatchIfElse.matchElse(this, a,type,fafafather,fatherType);
//							mHighLevelOperationBeanList.add(operationBean);
//						}
//						else {
//							operationBean = MatchExpressionStatement.matchExpression(this, a,type,fafafather,fatherType);
//							mHighLevelOperationBeanList.add(operationBean);
//						}
//						break;
//					case StatementConstants.CONDITIONALEXPRESSION:
//						operationBean = MatchConditionalExpression.matchConditionalExpression(this, a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.SYNCHRONIZEDSTATEMENT:
//						//同步语句块增加
//                        operationBean = MatchSynchronized.matchSynchronized(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.SWITCHSTATEMENT:
//						//增加switch语句
//						operationBean = MatchSwitch.matchSwitch(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.SWITCHCASE:
//						//增加switch语句
//						operationBean = MatchSwitch.matchSwitchCase(this,a,type,fafafather,fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					case StatementConstants.JAVADOC:
//						//增加javadoc
//						operationBean = MatchJavaDoc.matchJavaDoc(this,a,type);
//						mHighLevelOperationBeanList.add(operationBean);
//						break;
//					//JAVADOC参数
//					case StatementConstants.TAGELEMENT:
//					case StatementConstants.TEXTELEMENT:
//					// 方法参数
//					case StatementConstants.SIMPLENAME:
//					case StatementConstants.STRINGLITERAL:
//					case StatementConstants.NULLLITERAL:
//					case StatementConstants.CHARACTERLITERAL:
//					case StatementConstants.NUMBERLITERAL:
//					case StatementConstants.BOOLEANLITERAL:
//					case StatementConstants.INFIXEXPRESSION:
//					case StatementConstants.METHODINVOCATION:
//						MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, type,this.mMiningActionBean.mDstTree);
//						break;
//					default:
//						String operationEntity = "DEFAULT: "+ ActionConstants.getInstanceStringName(a) + " " +type;
//						operationBean = new HighLevelOperationBean(a,type,null,-1,operationEntity,fafafather,fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//						this.setActionTraversedMap(a);
//						break;
//				}
//			}
//		}
//	}

//	/**
//	 * Sub level-II
//	 */
//	public void findDelete() {
//		int deleteActionCount = this.mMiningActionBean.mActionGeneratorBean.getDeleteActions().size();
//		int index = 0;
//		int count = 0;
//		String resultStr = null;
//		while (true) {
//			if (index == deleteActionCount) {
//				break;
//			}
//			Action a = this.mMiningActionBean.mActionGeneratorBean.getDeleteActions().get(index);
//			index++;
//			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
//				continue;
//			}
//			Delete del = (Delete) a;
//			ITree tmp = a.getNode();
//			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(tmp);
//			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
//			ITree fafafather = AstRelations.findFafafatherNode(tmp, this.mMiningActionBean.mSrcTree);
//			if(fafafather == null){
//				System.out.println("Father Null Condition: "+ActionConstants.getInstanceStringName(a) + " " +type);
//				this.setActionTraversedMap(a);
//				continue;
//			}
//			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
////			System.out.print(nextAction);
//            HighLevelOperationBean operationBean;
//            if(StatementConstants.FIELDDECLARATION.equals(type)){
//                //delete FieldDeclaration
//				operationBean = MatchFieldDeclaration.matchFieldDeclaration(this,a,type);
//				mHighLevelOperationBeanList.add(operationBean);
//				continue;
//            } else if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
//                //delete FieldDeclaration body
//				operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(this,a,type,fafafather,fatherType);
//				mHighLevelOperationBeanList.add(operationBean);
//				continue;
//            }
//
//            if (StatementConstants.METHODDECLARATION.equals(type)) {
//				// 删除方法体
//				//HighLevelOperationBean bean  = MatchNewOrDeleteMethod.matchNewOrDeleteMethod(this,a,type);
//                operationBean = MatchNewOrDeleteMethod.matchNewOrDeleteMethod(this,a,type);
//                mHighLevelOperationBeanList.add(operationBean);
//			}else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
//				// 删除方法参数
//                operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(this,a, type,fafafather,fatherType);
//                mHighLevelOperationBeanList.add(operationBean);
//			} else {
//				// 方法体内部
//				switch (type) {
//				case StatementConstants.IFSTATEMENT:
//                    operationBean = MatchIfElse.matchIf(this,a, type);
//                    mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.BLOCK:
//					MatchBlock.matchBlock(this,a, type,this.mMiningActionBean.mDstTree);
//					break;
//				case StatementConstants.BREAKSTATEMENT:
//					if(AstRelations.isFatherSwitchStatement(a, this.mMiningActionBean.mSrcTree)) {
//						//删除switch语句
//						operationBean = MatchSwitch.matchSwitchCase(this, a, type, fafafather, fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//					}else {
//						System.out.println("Other Condition"+ActionConstants.getInstanceStringName(a) + " " +type);
//						this.setActionTraversedMap(a);
//						// TODO剩下的情况
//					}
//					break;
//				case StatementConstants.RETURNSTATEMENT:
//					MatchReturnStatement.matchReturnStatement(this,a, type,this.mMiningActionBean.mDstTree);
//					break;
//				case StatementConstants.FORSTATEMENT:
//					//算出for语句
//					operationBean = MatchForStatement.matchForStatement(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.ENHANCEDFORSTATEMENT:
//					//删除for语句
//					operationBean = MatchForStatement.matchEnhancedForStatement(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.WHILESTATEMENT:
//					//删除while语句
//					operationBean = MatchWhileStatement.matchWhileStatement(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.DOSTATEMENT:
//					//删除do while语句
//					operationBean = MatchWhileStatement.matchDoStatement(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.TRYSTATEMENT:
//					operationBean = MatchTry.matchTry(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.THROWSTATEMENT:
//					operationBean = MatchTry.matchThrowStatement(this,a,type,fafafather,fatherType);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
//					operationBean = MatchVariableDeclarationExpression.matchVariableDeclaration(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.EXPRESSIONSTATEMENT:
//					// Pattern 1.2 Match else
//					if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mSrcTree)) {
//						operationBean = MatchIfElse.matchElse(this, a, type, fafafather, fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//					}
//					else {
//						operationBean = MatchExpressionStatement.matchExpression(this, a, type, fafafather, fatherType);
//						mHighLevelOperationBeanList.add(operationBean);
//					}
//					break;
//				case StatementConstants.CONDITIONALEXPRESSION:
//					operationBean = MatchConditionalExpression.matchConditionalExpression(this, a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.SYNCHRONIZEDSTATEMENT:
//					//同步语句块删除
//					operationBean = MatchSynchronized.matchSynchronized(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.SWITCHSTATEMENT:
//					//switch语句
//					operationBean = MatchSwitch.matchSwitch(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.SWITCHCASE:
//					//删除switchcase语句
//					operationBean = MatchSwitch.matchSwitchCase(this,a,type,fafafather,fatherType);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				case StatementConstants.JAVADOC:
//					//删除javadoc
//					operationBean = MatchJavaDoc.matchJavaDoc(this,a,type);
//					mHighLevelOperationBeanList.add(operationBean);
//					break;
//				//JAVADOC参数
//				case StatementConstants.TAGELEMENT:
//				case StatementConstants.TEXTELEMENT:
//				// 方法参数
//				case StatementConstants.SIMPLENAME:
//				case StatementConstants.STRINGLITERAL:
//				case StatementConstants.NULLLITERAL:
//				case StatementConstants.CHARACTERLITERAL:
//				case StatementConstants.NUMBERLITERAL:
//				case StatementConstants.BOOLEANLITERAL:
//				case StatementConstants.INFIXEXPRESSION:
//				case StatementConstants.METHODINVOCATION:
//					MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, type,this.mMiningActionBean.mSrcTree);
//					break;
//				default:
//					String operationEntity = "DEFAULT: "+ActionConstants.getInstanceStringName(a) + " " +type;
//					operationBean = new HighLevelOperationBean(a,type,null,-1,operationEntity,fafafather,fatherType);
//					mHighLevelOperationBeanList.add(operationBean);
//					this.setActionTraversedMap(a);
//					break;
//				}
//			}
//		}
//	}

//	/**
//	 * Sub level-II
//	 */
//	public void findUpdate() {
//		int updateActionCount = this.mMiningActionBean.mActionGeneratorBean.getUpdateActions().size();
//		int index = 0;
//		while (true) {
//			if (index == updateActionCount) {
//				break;
//			}
//			Action a = this.mMiningActionBean.mActionGeneratorBean.getUpdateActions().get(index);
//			index++;
//			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
//				// 标记过的 update action
//				continue;
//			}
//			Update up = (Update) a;
//			ITree tmp = a.getNode();
//			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(tmp);
//			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
//			ITree fafafather = AstRelations.findFafafatherNode(tmp, this.mMiningActionBean.mSrcTree);
//			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
////			System.out.print(nextAction);
//			HighLevelOperationBean operationBean;
//			if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
//				//insert FieldDeclaration body
//				operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(this,a,type,fafafather,fatherType);
//				mHighLevelOperationBeanList.add(operationBean);
//				continue;
//			}
//
//			if (StatementConstants.METHODDECLARATION.equals(type)) {
//				// 新增方法
//				System.out.println("Update 应该不会是method declaration");
//			} else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
//				// 方法签名update
//				if (StatementConstants.BLOCK.equals(type)) {
//					System.out.print(nextAction);
//					System.out.println("Not considered\n");
//				} else {
//					operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(this,a, type,fafafather,fatherType);
//					mHighLevelOperationBeanList.add(operationBean);
//				}
//			} else {
//				// 方法体
//				switch (type) {
//					case StatementConstants.SIMPLENAME:
//					case StatementConstants.STRINGLITERAL:
//					case StatementConstants.NULLLITERAL:
//					case StatementConstants.CHARACTERLITERAL:
//					case StatementConstants.NUMBERLITERAL:
//					case StatementConstants.BOOLEANLITERAL:
//					case StatementConstants.INFIXEXPRESSION:
//					case StatementConstants.METHODINVOCATION:
//					MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(this,a, type,this.mMiningActionBean.mSrcTree);
//					break;
//				default:
//					break;
//				}
//			}
//		}
//
//	}

//	public void findMove() {
//		int moveActionCount = this.mMiningActionBean.mActionGeneratorBean.getMoveActions().size();
//		int index = 0;
//		while (true) {
//			if (index == moveActionCount) {
//				break;
//			}
//			Action a = this.mMiningActionBean.mActionGeneratorBean.getMoveActions().get(index);
//			index++;
//			if (this.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
//				// 标记过的 update action
//				continue;
//			}
//			Move up = (Move) a;
//			ITree moveNode = a.getNode();
//			String type = this.mMiningActionBean.mSrcTree.getTypeLabel(moveNode);
//			String nextAction = ConsolePrint.getMyOneActionString(a, 0, this.mMiningActionBean.mSrcTree);
//			ITree fafafather = AstRelations.findFafafatherNode(moveNode, this.mMiningActionBean.mSrcTree);
//			String fatherType = this.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
//			System.out.print(nextAction+"\n");
//			ITree dstNode = up.getParent();
//
//		}
//
//	}


//		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);


}
