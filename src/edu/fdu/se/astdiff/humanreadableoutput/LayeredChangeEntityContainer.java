package edu.fdu.se.astdiff.humanreadableoutput;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by huangkaifeng on 3/24/18.
 *
 */
public class LayeredChangeEntityContainer {

    protected Map<BodyDeclarationPair,List<ChangeEntity>> layerMap;

    protected List<BodyDeclarationPair> keyIndex;

    public LayeredChangeEntityContainer(){
        this.layerMap = new HashMap<>();
        this.keyIndex = new ArrayList<>();
    }

    public void sortKeys(){
        List<Entry<BodyDeclarationPair,List<ChangeEntity>>> mList = new ArrayList<>(layerMap.entrySet());
        mList.sort(new Comparator<Entry<BodyDeclarationPair,List<ChangeEntity>>>(){
            @Override
            public int compare(Entry<BodyDeclarationPair,List<ChangeEntity>> a,Entry<BodyDeclarationPair,List<ChangeEntity>> b){
                return a.getKey().getBodyDeclaration().getStartPosition() - a.getKey().getBodyDeclaration().getStartPosition();
            }
        });
        mList.forEach(a-> keyIndex.add(a.getKey()));
    }

    public void addKey(BodyDeclarationPair bodyDeclarationPair){
        if(layerMap.containsKey(bodyDeclarationPair)){
            return;
        }
        List<ChangeEntity> mList = new ArrayList<>();
        layerMap.put(bodyDeclarationPair,mList);
    }

    public void addPreDiffChangeEntity(ChangeEntity changeEntity){
        BodyDeclarationPair mKey = null;
        for(BodyDeclarationPair key:this.layerMap.keySet()){
            if(key.getBodyDeclaration() instanceof TypeDeclaration &&
                    changeEntity.location == key.getLocationClassString()){
                mKey = key;
                break;
            }
        }
        this.layerMap.get(mKey).add(changeEntity);
    }

    public void addGumTreePlus(ChangeEntity changeEntity, MiningActionData mad) {
        ITree node = changeEntity.clusteredActionBean.fafather;
        Tree tree = null;
        BodyDeclarationPair mKey = null;
        int startPos = -1;
        if (changeEntity.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN) {
            if(changeEntity.clusteredActionBean.curAction instanceof Insert){
                // insert上一个节点mapping的节点
                tree = (Tree)mad.getMappedSrcOfDstNode(node);
            }else{
                tree = (Tree)node;
            }
            startPos = tree.getAstNode().getStartPosition();
        } else if (changeEntity.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_DOWN_UP){
                // father节点的range
            startPos = tree.getAstNode().getStartPosition();
        }
        mKey = getEnclosedBodyDeclaration(startPos);
        this.layerMap.get(mKey).add(changeEntity);

    }

    private BodyDeclarationPair getEnclosedBodyDeclaration(int start){
        for(BodyDeclarationPair key:this.layerMap.keySet()){
            if(start >= key.getBodyDeclaration().getStartPosition() && start<= (key.getBodyDeclaration().getStartPosition()+key.getBodyDeclaration().getLength())){
                return key;
            }
        }
        return null;
    }

    public void mergeMoveAndWrapper(){
        //todo
        // 原先的输出 如何表达成比较formal的输出
    }

}
