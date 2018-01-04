package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchMethodSignatureChange {
	
	/**
	 * 方法签名改变的情况
	 * 
	 * @param a
	 * @param fafafather
	 * @return
	 */
	public static HighLevelOperationBean matchMethodSignatureChange(FindPattern fp,Action a, ITree fafafather) {
		String operationEntity = "METHODSIGNATURE";
		ITree srcfafafather = null;
		ITree dstfafafather = null;
		if (a instanceof Insert) {
			dstfafafather = fafafather;
			srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
			if (srcfafafather == null) {
				System.err.println("err null mapping");
			}
		} else {
			srcfafafather = fafafather;
			dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
			if (dstfafafather == null) {
				System.err.println("err null mapping");
			}
		}
		List<Action> signatureChidlren = new ArrayList<Action>();
		int status = MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, dstfafafather, signatureChidlren);
//		MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, srcfafafather, signatureChidlren);
//		this.mMiningActionBean.mapMethodSignatureAction(srcfafafather, signatureChidlren);
		fp.setActionTraversedMap(signatureChidlren);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,StatementConstants.METHODDECLARATION,signatureChidlren,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

}
