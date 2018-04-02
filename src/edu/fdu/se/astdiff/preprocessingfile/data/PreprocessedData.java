package edu.fdu.se.astdiff.preprocessingfile.data;


import edu.fdu.se.astdiff.humanreadableoutput.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.javaparser.JDTParserFactory;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/16.
 *
 */
public class PreprocessedData {
    public String fullStringSrc;
    public String fullStringDst;

    public List<String> dstLineList;
    public List<String> srcLineList;
    public List<Integer> dstLines;
    public List<Integer> srcLines;

    public CompilationUnit dstCu;
    public CompilationUnit srcCu;

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

    private List<ChangeEntity> preprocessChangeEntity;

    public List<ChangeEntity> getPreprocessChangeEntity() {
        return preprocessChangeEntity;
    }

    public void setPreprocessChangeEntity(List<ChangeEntity> preprocessChangeEntity) {
        this.preprocessChangeEntity = preprocessChangeEntity;
    }

    /**
     * prev 和curr 中都没被删除 保修下来在CompilationUnit的Body
     */
    private List<BodyDeclarationPair> mBodiesRetained;

    private Map<String,List<BodyDeclaration>> classOrInterfaceOrEnum;

    public PreprocessedData(){
        mBodiesAdded = new ArrayList<>();
        mBodiesDeleted = new ArrayList<>();
        mBodiesRetained = new ArrayList<>();
        classOrInterfaceOrEnum = new HashMap<>();
        entityContainer = new LayeredChangeEntityContainer();


    }
    public LayeredChangeEntityContainer entityContainer;


    public void addTypeDeclaration(String prefix, BodyDeclaration a, String name){
        String key = prefix + "." + name;
        if(this.classOrInterfaceOrEnum.containsKey(key)){
            classOrInterfaceOrEnum.get(key).add(a);
        }else{
            List<BodyDeclaration> mList = new ArrayList<>();
            mList.add(a);
            this.classOrInterfaceOrEnum.put(key,mList);
        }
    }

    public void loadTwoCompilationUnits(CompilationUnit src,CompilationUnit dst,String srcPath,String dstPath){
        this.srcCu = src;
        this.srcLineList = new ArrayList<>();
        this.fullStringSrc = JDTParserFactory.getLinesOfFile(srcPath,this.srcLineList);
        this.srcLines = JDTParserFactory.getLinesList(srcLineList.size());

        this.dstCu = dst;
        this.dstLineList = new ArrayList<>();
        this.fullStringDst = JDTParserFactory.getLinesOfFile(dstPath,this.dstLineList);
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

    public int hashCodeOfBodyDeclaration(BodyDeclaration bodyDeclaration, String fileFullText){

        int a = bodyDeclaration.getStartPosition();
        int b = bodyDeclaration.getLength();
        if(bodyDeclaration.getJavadoc()!=null){
            int c = bodyDeclaration.getJavadoc().getLength();
            return fileFullText.substring(a+c+1,a+b).trim().hashCode();
        }else{
            return fileFullText.substring(a,a+b).trim().hashCode();
        }

    }

}
