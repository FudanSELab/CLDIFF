package edu.fdu.se.astdiff.miningchangeentity;

import edu.fdu.se.astdiff.associating.LayeredChangeEntityContainer;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class ChangeEntityPrinter {

    public static void printContainerEntity(LayeredChangeEntityContainer container,CompilationUnit cu) {
        System.out.println("\nMember Key Size:" + container.getLayerMap().size());
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : container.getLayerMap().entrySet()) {
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            List<ChangeEntity> mList = container.getLayerMap().get(bodyDeclarationPair);
            if (mList == null || mList.size() == 0) {
                continue;
            }
            int startL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getStartPosition());
            int endL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getLength() + bodyDeclarationPair.getBodyDeclaration().getStartPosition() - 1);
            System.out.println(bodyDeclarationPair.toString() + " (" + startL + "," + endL + ")"+ " listSize:"+mList.size());
            for (ChangeEntity ce : mList) {
                System.out.println(ce.toString());
            }
            System.out.println("");
        }
    }

    public static void printContainerEntityNatural(LayeredChangeEntityContainer container,CompilationUnit cu) {
        System.out.println("\nMember Key Size:" + container.getLayerMap().size());
        for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : container.getLayerMap().entrySet()) {
            BodyDeclarationPair bodyDeclarationPair = entry.getKey();
            List<ChangeEntity> mList = container.getLayerMap().get(bodyDeclarationPair);
            if (mList == null || mList.size() == 0) {
                continue;
            }
            int startL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getStartPosition());
            int endL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getLength() + bodyDeclarationPair.getBodyDeclaration().getStartPosition() - 1);
            System.out.println(bodyDeclarationPair.toString() + " (" + startL + "," + endL + ")" + " listSize:"+mList.size());
            for (ChangeEntity ce : mList) {
                System.out.println(ce.toString2());
            }
            System.out.println("");
        }
    }

}
