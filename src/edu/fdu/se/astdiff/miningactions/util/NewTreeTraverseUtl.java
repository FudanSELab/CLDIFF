package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/26.
 */
public class NewTreeTraverseUtl {

    /**
     * statment 节点
     *
     * @param action
     * @param startIndex
     * @param endIndex
     * @param result
     * @return
     */
    public static Set<String> traverseNodeGetAllEditActions(Action action, int startIndex, int endIndex, List<Action> result) {
        ITree node = action.getNode();
        Set<String> actionTypes = new HashSet<>();
        List<ITree> children = node.getChildren();
        for (int i = startIndex; i <= endIndex; i++) {
            ITree temp = children.get(i);
            for (ITree sub : temp.preOrder()) {
                Tree myTree = (Tree) sub;
                if (myTree.getDoAction() == null) {
                    actionTypes.add(ActionConstants.NULLACTION);
                    continue;
                }
                List<Action> nodeActions = myTree.getDoAction();
                for (Action aTmp : nodeActions) {
                    result.add(aTmp);
                    actionTypes.add(aTmp.getClass().getSimpleName());
                }
            }
        }
        return actionTypes;
    }


    /**
     * 遍历从MethodDeclaration 遍历下来的节点 添加有doAction引用的节点
     * 遍历node下的所有的孩子，把所有有action引用的添加到alleditaction ，返回值表示与action a相同修改类型的个数
     *
     * @param a
     * @param node
     * @param allEditAction
     * @return
     */
    public static Set<String> traverseMethodSignatureChildrenWithoutBlock(Action a, ITree node, List<Action> allEditAction) {
        List<ITree> children = node.getChildren();
        int len = children.size();
        return traverseNodeGetAllEditActions(a,0,len-1,allEditAction);
    }



}
