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
	public static HighLevelOperationBean matchMethodSignatureChange(FindPattern fp,Action a,String nodeType,ITree fafafather,String fafafatherType) {
		String operationEntity = "METHODSIGNATURE";
		if(!StatementConstants.METHODDECLARATION.equals(fafafatherType)) {
			System.err.println(operationEntity+" CHANGE: "+"fafafatherType is not MethodDeclaration" );
			return null;
		}
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
		boolean dst_status = MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, dstfafafather, signatureChidlren);
		boolean src_status = MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, srcfafafather, signatureChidlren);

		int status = MyTreeUtil.isSrcorDstAdded(src_status,dst_status);

//		this.mMiningActionBean.mapMethodSignatureAction(srcfafafather, signatureChidlren);
		fp.setActionTraversedMap(signatureChidlren);

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,signatureChidlren,status,operationEntity,fafafather,fafafatherType);
		return mHighLevelOperationBean;
	}

}
