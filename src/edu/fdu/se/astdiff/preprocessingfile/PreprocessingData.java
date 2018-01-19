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
     * 操作之后的CompilationUnit
     */
    private CompilationUnit currentCu;
    private CompilationUnit previousCu;


    /**
     * curr 删除的added的body
     */
    private List<BodyDeclaration> mBodiesAdded;
    /**
     * prev 删除的removed body
     */
    private List<BodyDeclaration> mBodiesDeleted;

    /**
     * prev 和curr 中都没被删除 保修下来在CompilationUnit的Body
     */
    private List<BodyDeclaration> mBodiesRetained;


    /**
     * Body 所述 的class
     */
    private Map<BodyDeclaration,ClassOrInterfaceDeclaration> mMap;




    private List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationsList;




    public PreprocessingData(){
        mBodiesAdded = new ArrayList<>();
        mBodiesDeleted = new ArrayList<>();
        mMap = new HashMap<>();
        mBodiesRetained = new ArrayList<>();
        classOrInterfaceDeclarationsList = new ArrayList<>();
    }

    private void addToMap(ClassOrInterfaceDeclaration cod,BodyDeclaration bd){
        mMap.put(bd,cod);
        mBodiesRetained.add(bd);
    }


    public void loadPrevRetainedBodies(String prevStr){
        CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prevStr);
        TypeDeclaration mTypeCurr = cuPrev.getType(0);
        ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;
        traverseClassOrInterfaceMapRetainedBody(cod);
    }


    public void traverseClassOrInterfaceMapRetainedBody(ClassOrInterfaceDeclaration cod){
        classOrInterfaceDeclarationsList.add(cod);
        NodeList nodeList = cod.getMembers();
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
                traverseClassOrInterfaceMapRetainedBody(innerClass);
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

    public void addBodiesAdded(BodyDeclaration bodyDeclaration){
        this.mBodiesAdded.add(bodyDeclaration);
    }

    public void addBodiesDeleted(BodyDeclaration bodyDeclaration){
        this.mBodiesDeleted.add(bodyDeclaration);
    }


    public void setCurrentCu(CompilationUnit currentCu) {
        this.currentCu = currentCu;
    }

    public void setPreviousCu(CompilationUnit previousCu) {
        this.previousCu = previousCu;
    }

    public void printAddedRemovedBodies(){
        for(BodyDeclaration item:this.mBodiesAdded){
            System.out.println(item.toString());
        }
        System.out.print("---------------------------\n");
        for(BodyDeclaration item:this.mBodiesDeleted){
            System.out.println(item.toString());
        }
    }
}
