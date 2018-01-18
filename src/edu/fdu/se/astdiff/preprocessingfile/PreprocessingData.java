package edu.fdu.se.astdiff.preprocessingfile;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import edu.fdu.se.javaparser.JavaParserFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/16.
 */
public class PreprocessingData {
    /**
     * prev file中 retain的body
     */
    private Map<BodyDeclaration,ClassOrInterfaceDeclaration> mMap;
    private List<BodyDeclaration> mBodiesRetained;

    /**
     * curr 删除的added的body
     */
    private List<BodyDeclaration> mBodiesAdded;
    /**
     * prev 删除的removed body
     */
    private List<BodyDeclaration> mBodiesDeleted;

    private ClassOrInterfaceDeclaration mainClass;

    private CompilationUnit currentCu;
    private CompilationUnit previousCu;


    public PreprocessingData(){
        mMap = new HashMap<>();
        mBodiesRetained = new ArrayList<>();
        mBodiesAdded = new ArrayList<>();
        mBodiesDeleted = new ArrayList<>();
    }
    private void addToMap(ClassOrInterfaceDeclaration cod,BodyDeclaration bd){
        mMap.put(bd,cod);
        mBodiesRetained.add(bd);
    }


    public void processTrimedFilePair(String prevStr){
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prevStr);
        TypeDeclaration mTypeCurr = cuPrev.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;

        NodeList nodeList = mTypeCurr.getMembers();
        this.mainClass = cod;
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            Node node = nodeList.get(i);
            if (node instanceof AnnotationDeclaration) {
                continue;
            }
            if (node instanceof ConstructorDeclaration||node instanceof MethodDeclaration||node instanceof FieldDeclaration) {
                addToMap(cod,(BodyDeclaration)node);
                continue;
            }


            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration) node;
                NodeList innerList = innerClass.getMembers();
                for (int j = innerList.size() - 1; j >= 0; j--) {
                    Node item2 = innerList.get(j);
                    if (item2 instanceof AnnotationDeclaration) {
                        continue;
                    }
                    if (item2 instanceof ConstructorDeclaration||item2 instanceof MethodDeclaration||item2 instanceof FieldDeclaration) {
                        addToMap(innerClass,(BodyDeclaration)node);
                        continue;
                    }
                }
            }

        }
    }


    public BodyDeclaration getBelongedBodyDeclaration(int start){
        for(BodyDeclaration bd: mBodiesRetained){
            Position p = (Position)bd.getBegin().get();
            Position p2 = (Position)bd.getEnd().get();
            if(start>p.line && start<p2.line){
                return bd;
            }
        }
        return null;
    }


    public CompilationUnit getPreviousCu() {
        return previousCu;
    }

    public CompilationUnit getCurrentCu() {
        return currentCu;
    }

    public List<BodyDeclaration> getmBodiesDeleted() {
        return mBodiesDeleted;
    }

    public List<BodyDeclaration> getmBodiesAdded() {
        return mBodiesAdded;
    }

    public void setCurrentCu(CompilationUnit currentCu) {
        this.currentCu = currentCu;
    }

    public void setPreviousCu(CompilationUnit previousCu) {
        this.previousCu = previousCu;
    }
}
