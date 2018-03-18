package edu.fdu.se.astdiff.preprocessingfile;


import edu.fdu.se.javaparser.JDTParserFactory;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/16.
 *
 */
public class PreprocessedData {

    protected List<String> dstLineList;
    protected List<String> srcLineList;

    protected List<Integer> dstLines;

    protected List<Integer> srcLines;

    protected CompilationUnit dstCu;
    protected CompilationUnit srcCu;

    public CompilationUnit getDstCu() {
        return dstCu;
    }

    public CompilationUnit getSrcCu() {
        return srcCu;
    }

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
        String key = prefix + "." +a.getName().toString();
        if(this.classOrInterfaceDeclarationsList.containsKey(key)){
            classOrInterfaceDeclarationsList.get(key).add(a);
        }else{
            List<TypeDeclaration> mList = new ArrayList<>();
            mList.add(a);
            this.classOrInterfaceDeclarationsList.put(key,mList);
        }
    }

    public void loadTwoCompilationUnits(CompilationUnit src,CompilationUnit dst,String srcPath,String dstPath){
        this.srcCu = src;
        this.srcLineList = JDTParserFactory.getLinesOfFile(srcPath);
        this.srcLines = JDTParserFactory.getLinesList(srcLineList.size());

        this.dstCu = dst;
        this.dstLineList = JDTParserFactory.getLinesOfFile(dstPath);
        this.dstLines = JDTParserFactory.getLinesList(dstLineList.size());
    }



    public void addBodiesAdded(BodyDeclaration bodyDeclaration,String classPrefix){
        this.mBodiesAdded.add(new BodyDeclarationPair(bodyDeclaration,classPrefix));
    }


    public void addBodiesDeleted(BodyDeclarationPair bodyDeclarationPair){
        this.mBodiesDeleted.add(bodyDeclarationPair);
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

}
