package edu.fdu.se.astdiff.humanreadableoutput;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.member.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.*;

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
            if(key.getBodyDeclaration() instanceof TypeDeclaration){
                if(changeEntity instanceof ClassOrInterfaceDeclarationChangeEntity){
                    String location = changeEntity.stageIIBean.getLocation();
                    location = location.substring(0,location.length()-1);
                    int index = location.lastIndexOf(".");
                    location = location.substring(0,index);
                    if(location.equals(key.getLocationClassString())){
                        mKey = key;
                        break;
                    }
                }else{
                    if(changeEntity.stageIIBean.getLocation().equals(key.getLocationClassString())){
                        mKey = key;
                        break;
                    }
                }
            }
        }
        if(mKey!=null && this.layerMap.containsKey(mKey)){
            this.layerMap.get(mKey).add(changeEntity);
        }else{
            System.err.println(changeEntity.stageIIBean.getLocation()+" "+changeEntity.getClass().getSimpleName());
        }
    }

    public void addGumTreePlus(ChangeEntity changeEntity, MiningActionData mad) {
        ITree node = changeEntity.clusteredActionBean.fafather;
        Tree tree = null;
        BodyDeclarationPair mKey = null;
        int startPos = -1;
        if (changeEntity.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN) {
            if(changeEntity.clusteredActionBean.curAction instanceof Insert){
                // insert上一个节点mapping的节点
                while(tree==null){
                    node = node.getParent();
                    tree = (Tree) mad.getMappedSrcOfDstNode(node);
                }
            }else{
                tree = (Tree)node;
            }
            if(tree ==null){
                System.out.println("a");
            }
            startPos = tree.getAstNode().getStartPosition();

        } else if (changeEntity.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_DOWN_UP){
                // father节点的range
            tree = (Tree)node;
            startPos = tree.getAstNode().getStartPosition();
        }
        mKey = getEnclosedBodyDeclaration(startPos);
        if(mKey!=null && this.layerMap.containsKey(mKey)){
            this.layerMap.get(mKey).add(changeEntity);
        }else{
            System.err.println(changeEntity.toString());
        }

    }

    private BodyDeclarationPair getEnclosedBodyDeclaration(int start){
        for(BodyDeclarationPair key:this.layerMap.keySet()){
            if(key.getBodyDeclaration() instanceof TypeDeclaration){
                continue;
            }
            if(start >= key.getBodyDeclaration().getStartPosition() && start<= (key.getBodyDeclaration().getStartPosition()+key.getBodyDeclaration().getLength())){
                return key;
            }
        }
        return null;
    }

    public void mergeMoveAndWrapper(){
        for(Entry<BodyDeclarationPair,List<ChangeEntity>> entry:this.layerMap.entrySet()){
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            if(bodyDeclarationPair.getBodyDeclaration() instanceof MethodDeclaration){
                List<ChangeEntity> mList = entry.getValue();
            }
        }
    }

    public void sortEntityList(){
        for(Entry<BodyDeclarationPair,List<ChangeEntity>> entry:this.layerMap.entrySet()){
            List<ChangeEntity> mList = entry.getValue();
            mList.sort(new Comparator<ChangeEntity>(){
                @Override
                public int compare(ChangeEntity a,ChangeEntity b){
                    return a.lineRange.startLineNo - b.lineRange.startLineNo;
                }
            });
        }
    }

    public void printContainerEntityBeforeSorting(){
        System.out.println(this.layerMap.size());
        for(Entry<BodyDeclarationPair,List<ChangeEntity>> entry:this.layerMap.entrySet()){
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            List<ChangeEntity> mList = entry.getValue();
            System.out.println(bodyDeclarationPair.toString());

            for(ChangeEntity ce:mList){
                System.out.println(ce.toString());
            }
            System.out.println("");
        }

    }



}
