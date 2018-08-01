package edu.fdu.se.base.preprocessingfile;

import com.github.gumtreediff.actions.model.Insert;
import edu.fdu.se.base.associating.MyRange;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.member.ClassChangeEntity;
import edu.fdu.se.base.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.javaparser.JDTParserFactory;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/19.
 *
 */
public class NewFileProcessing {

    public ChangeEntityData ced;


    public CompilationUnit cu;

    public List<String> dstLineList;

    public NewFileProcessing(byte[] content){
        try {

            cu = JDTParserFactory.getCompilationUnit(content);
            this.dstLineList = new ArrayList<>();
            BodyDeclaration bodyDeclaration = (BodyDeclaration) cu.types().get(0);
            if(bodyDeclaration instanceof TypeDeclaration){
                init((TypeDeclaration)bodyDeclaration);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void init(TypeDeclaration typeDeclaration){
        ClassChangeEntity classChangeEntity = new ClassChangeEntity(new BodyDeclarationPair(typeDeclaration,typeDeclaration.getName().toString()+"."),
                Insert.class.getSimpleName(),
                new MyRange(cu.getLineNumber(typeDeclaration.getStartPosition()),cu.getLineNumber(typeDeclaration.getStartPosition()+typeDeclaration.getLength()),
                        ChangeEntityDesc.StageITreeType.DST_TREE_NODE));
        List<ChangeEntity> mList = new ArrayList<>();
        mList.add(classChangeEntity);
        ced = new ChangeEntityData(new MiningActionData(mList));
    }
}
