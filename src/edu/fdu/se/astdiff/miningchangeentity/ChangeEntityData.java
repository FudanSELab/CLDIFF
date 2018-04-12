package edu.fdu.se.astdiff.miningchangeentity;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.associating.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.*;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;
import org.eclipse.jdt.core.dom.*;


/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ChangeEntityData {

    public PreprocessedData preprocessedData;
    public LayeredChangeEntityContainer entityContainer;
    public MiningActionData mad;



    public ChangeEntityData(PreprocessedData pd, MiningActionData mad) {
        this.preprocessedData = pd;
        this.entityContainer = pd.entityContainer;
        this.mad = mad;
    }


    public void addOneBody(BodyDeclarationPair item, String type) {
        ChangeEntity ce = null;
        int s;
        int e;
        MyRange myRange = null;
        if (Insert.class.getSimpleName().equals(type)) {
            s = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getDstCu().getLineNumber(item.getBodyDeclaration().getStartPosition() + item.getBodyDeclaration().getLength() - 1);
            myRange = new MyRange(s, e, ChangeEntityDesc.StageITreeType.DST_TREE_NODE);
        } else if (Delete.class.getSimpleName().equals(type)) {
            s = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition());
            e = this.preprocessedData.getSrcCu().getLineNumber(item.getBodyDeclaration().getStartPosition() + item.getBodyDeclaration().getLength() - 1);
            myRange = new MyRange(s, e, ChangeEntityDesc.StageITreeType.SRC_TREE_NODE);
        }
        if (item.getBodyDeclaration() instanceof FieldDeclaration) {
            ce = new FieldChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof MethodDeclaration) {
            ce = new MethodChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof Initializer) {
            ce = new InitializerChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof TypeDeclaration) {
            ce = new ClassOrInterfaceDeclarationChangeEntity(item, type, myRange);
        } else if (item.getBodyDeclaration() instanceof EnumDeclaration) {
            ce = new EnumChangeEntity(item, type, myRange);
        }
        if (ce != null) {
            this.entityContainer.addPreDiffChangeEntity(ce);
        }
    }





}
