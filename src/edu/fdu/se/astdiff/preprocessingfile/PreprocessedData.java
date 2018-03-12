package edu.fdu.se.astdiff.preprocessingfile;


import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class PreprocessedData {
    /**
     * 操作之后的CompilationUnit
     */
    private CompilationUnit currentCu;
    private CompilationUnit previousCu;

    /**
     * curr 删除的added的body
     */
    private List<BodyDeclarationPair> mBodiesAdded;
    /**
     * prev 删除的removed body
     */
    private List<BodyDeclarationPair> mBodiesDeleted;

    /**
     * prev 和curr 中都没被删除 保修下来在CompilationUnit的Body
     */
    private List<BodyDeclarationPair> mBodiesRetained;



    private Map<String,List<TypeDeclaration>> classOrInterfaceDeclarationsList;

    public PreprocessedData(){
        mBodiesAdded = new ArrayList<>();
        mBodiesDeleted = new ArrayList<>();
        mBodiesRetained = new ArrayList<>();
        classOrInterfaceDeclarationsList = new HashMap<>();
    }

    public void addTypeDeclaration(String prefix,TypeDeclaration a){
        if(this.classOrInterfaceDeclarationsList.containsKey(prefix)){
            List<TypeDeclaration> mList = this.classOrInterfaceDeclarationsList.get(prefix);
            mList.add(a);
        }else{
            List<TypeDeclaration> mList = new ArrayList<>();
            mList.add(a);
            this.classOrInterfaceDeclarationsList.put(prefix,mList);
        }
    }












    public CompilationUnit getPreviousCu() {
        return previousCu;
    }

    public CompilationUnit getCurrentCu() {
        return currentCu;
    }

    public void addBodiesAdded(BodyDeclaration bodyDeclaration,String classPrefix){
        this.mBodiesAdded.add(new BodyDeclarationPair(bodyDeclaration,classPrefix));
    }


    public void addBodiesDeleted(BodyDeclarationPair bodyDeclarationPair){
        this.mBodiesDeleted.add(bodyDeclarationPair);
    }


    public void setCurrentCu(CompilationUnit currentCu) {
        this.currentCu = currentCu;
    }

    public void setPreviousCu(CompilationUnit previousCu) {
        this.previousCu = previousCu;
    }

    public void printAddedRemovedBodies(){
        for(BodyDeclarationPair item:this.mBodiesAdded){
//            System.out.println(item.getBodyDeclaration().toString()+"  "+item.getLocationClassString());
            System.out.println(item.getBodyDeclaration().toString());
        }
        System.out.print("-----------------------------\n");
        for(BodyDeclarationPair item:this.mBodiesDeleted){
//            System.out.println(item.getBodyDeclaration().toString()+"  "+item.getLocationClassString());
            System.out.println(item.getBodyDeclaration().toString());
        }
    }

    public List<BodyDeclarationPair> getmBodiesAdded() {
        return mBodiesAdded;
    }

    public List<BodyDeclarationPair> getmBodiesDeleted() {
        return mBodiesDeleted;
    }



    /**
     * name 修改导致 识别出来一个增一个删
     */
    public void findMethodNameChange(){
        //todo
        System.out.println("todo");
    }
}
