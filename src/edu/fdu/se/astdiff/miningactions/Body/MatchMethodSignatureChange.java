package edu.fdu.se.astdiff.miningactions.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchMethodSignatureChange {
	
	/**
	 * 方法签名改变的情况
	 * 
	 * @param a
	 * @param fafafather
	 * @return
	 */
	public static ClusteredActionBean matchMethodSignatureChange(MiningActionData fp, Action a, String nodeType, ITree fafafather, String fafafatherType) {
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
		List<Action> signatureChidlren = new ArrayList<>();
		Set<String> src_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, srcfafafather, signatureChidlren);
		Set<String> dst_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, dstfafafather, signatureChidlren);

		int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
		fp.setActionTraversedMap(signatureChidlren);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(
				a,nodeType,signatureChidlren,nodeLinePosition,status,operationEntity,null,null);
		return mBean;
	}

}
