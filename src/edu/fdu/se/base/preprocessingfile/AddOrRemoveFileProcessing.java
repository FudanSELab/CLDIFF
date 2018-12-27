package edu.fdu.se.base.preprocessingfile;

import com.github.gumtreediff.actions.model.Insert;
import edu.fdu.se.base.links.MyRange;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.member.ClassChangeEntity;
import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.javaparser.JDTParserFactory;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/19.
 *
 */
public class AddOrRemoveFileProcessing {

    public ChangeEntityData ced;

    public CompilationUnit cu;

    public List<String> linesList;

    public String FILE_TYPE;


    public AddOrRemoveFileProcessing(byte[] content, String fileType){
        try {
            FILE_TYPE = fileType;
            cu = JDTParserFactory.getCompilationUnit(content);
            this.linesList = new ArrayList<>();
            BodyDeclaration bodyDeclaration = (BodyDeclaration) cu.types().get(0);
            if(bodyDeclaration instanceof TypeDeclaration){
                init((TypeDeclaration)bodyDeclaration);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void init(TypeDeclaration typeDeclaration){
        int treeType = 0;
        if(FILE_TYPE.equals(ChangeEntityDesc.StageIIIFile.DST)){
            treeType = ChangeEntityDesc.StageITreeType.DST_TREE_NODE;
        }else{
            treeType = ChangeEntityDesc.StageITreeType.SRC_TREE_NODE;
        }
        ClassChangeEntity classChangeEntity = new ClassChangeEntity(new BodyDeclarationPair(typeDeclaration,typeDeclaration.getName().toString()+"."),
                Insert.class.getSimpleName(),
                new MyRange(cu.getLineNumber(typeDeclaration.getStartPosition()),cu.getLineNumber(typeDeclaration.getStartPosition()+typeDeclaration.getLength()),
                        treeType ));
        List<ChangeEntity> mList = new ArrayList<>();
        mList.add(classChangeEntity);
        ced = new ChangeEntityData(new MiningActionData(mList));
        FilePairPreDiff diff = new FilePairPreDiff();
        ced.mad.preprocessedData = diff.getPreprocessedData();
        diff.addSuperClass(typeDeclaration,ced.mad.preprocessedData.getInterfacesAndFathers());
    }
}
