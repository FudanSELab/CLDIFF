package edu.fdu.se.astdiff.preprocessingfile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import org.eclipse.jdt.core.dom.*;

import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedTempData;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.javaparser.JDTParserFactory;

/**
 * 两个文件 预处理
 * 删除一摸一样的方法
 * 删除一摸一样的field
 * 删除一摸一样的内部类
 * 删除add method
 * 删除remove method
 * 删除内部类中的add / remove method
 * 保留 remove field 和add field 因为需要识别是否是refactor
 *
 * prefx 为 method field等所属的class，如果是内部类A, 那么prfix写到X.X.X.A.为止
 */
public class FilePairPreDiff {


    public FilePairPreDiff() {
        preprocessedData = new PreprocessedData();
        preprocessedTempData = new PreprocessedTempData();
        queue = new LinkedList<>();
    }

    private PreprocessedData preprocessedData;
    private PreprocessedTempData preprocessedTempData;

    class SrcDstPair{
        TypeDeclaration tpSrc;
        TypeDeclaration tpDst;
    }
    private Queue<SrcDstPair> queue;

    public void initFile(String prevPath,String currPath){
        preprocessedData.srcCu = JDTParserFactory.getCompilationUnit(prevPath);
        preprocessedData.dstCu = JDTParserFactory.getCompilationUnit(currPath);
        preprocessedData.loadTwoCompilationUnits(preprocessedData.srcCu, preprocessedData.dstCu, prevPath, currPath);
    }
    public void initFile(byte[] prevContent,byte[] currContent){
        try {
            preprocessedData.srcCu = JDTParserFactory.getCompilationUnit(prevContent);
            preprocessedData.dstCu = JDTParserFactory.getCompilationUnit(currContent);
            preprocessedData.loadTwoCompilationUnits(preprocessedData.srcCu, preprocessedData.dstCu, prevContent, currContent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public int compareTwoFile(String outputDirName) {
        CompilationUnit cuSrc = preprocessedData.srcCu;
        CompilationUnit cuDst = preprocessedData.dstCu;
//        fileOutputLog.writeFileBeforeProcess(preprocessedData);
//        if ("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_PREPROCESSING))) {
//            fileOutputLog = new FileOutputLog(outputDirName);
//
//        }
        preprocessedTempData.removeAllSrcComments(cuSrc, preprocessedData.srcLines);
        preprocessedTempData.removeAllDstComments(cuDst, preprocessedData.dstLines);
        if(cuSrc.types().size() != cuDst.types().size()){
            return -1;
        }
        for(int i = 0;i<cuSrc.types().size();i++){
            BodyDeclaration bodyDeclarationSrc = (BodyDeclaration) cuSrc.types().get(i);
            BodyDeclaration bodyDeclarationDst = (BodyDeclaration) cuDst.types().get(i);
            if ((bodyDeclarationSrc instanceof TypeDeclaration) && (bodyDeclarationDst instanceof TypeDeclaration)) {
                SrcDstPair srcDstPair = new SrcDstPair();
                srcDstPair.tpSrc = (TypeDeclaration) bodyDeclarationSrc;
                srcDstPair.tpDst = (TypeDeclaration) bodyDeclarationDst;
                this.queue.offer(srcDstPair);
            }else{
                return -1;
            }
        }
        while(queue.size()!=0){
            SrcDstPair tmp = queue.poll();
            compare(cuSrc,cuDst,tmp.tpSrc,tmp.tpDst);
        }
        return 0;
    }
    public void addSuperClass(TypeDeclaration type,List<String> list){
        List<Type> aa  = type.superInterfaceTypes();
        if(aa!=null) {
            for (Type aaa : aa) {
                list.add(aaa.toString());
            }
        }
        if(type.getSuperclassType()!=null) {
            list.add(type.getSuperclassType().toString());
        }
    }

    private void compare(CompilationUnit cuSrc,CompilationUnit cuDst,TypeDeclaration tdSrc,TypeDeclaration tdDst){
        TypeNodesTraversal astTraversal = new TypeNodesTraversal();
        addSuperClass(tdSrc,preprocessedData.interfacesAndFathers);
        addSuperClass(tdDst,preprocessedData.interfacesAndFathers);

        astTraversal.traverseSrcTypeDeclarationInit(preprocessedData, preprocessedTempData, tdSrc, tdSrc.getName().toString() + ".");
        astTraversal.traverseDstTypeDeclarationCompareSrc(preprocessedData, preprocessedTempData, tdDst, tdDst.getName().toString() + ".");
        // 考虑后面的识别 method name变化，这里把remove的注释掉
        iterateVisitingMap();
        undeleteSignatureChange();
        preprocessedTempData.removeSrcRemovalList(cuSrc, preprocessedData.srcLines);
        preprocessedTempData.removeDstRemovalList(cuDst, preprocessedData.dstLines);
        iterateVisitingMap2LoadContainerMap();
//        astTraversal.traverseSrcTypeDeclaration2Keys(preprocessedData,preprocessedTempData,tdSrc,tdSrc.getName().toString() + ".");

//        if (fileOutputLog != null) {
//            fileOutputLog.writeFileAfterProcess(preprocessedData);
//        }

    }


    private void iterateVisitingMap() {
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.srcNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
            BodyDeclaration bd = bdp.getBodyDeclaration();
            if (bd instanceof TypeDeclaration) {
                switch (value) {
//                    case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
//                    case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
//                        break;
                    case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                        this.preprocessedData.addBodiesDeleted(bdp);
                        this.preprocessedTempData.addToSrcRemoveList(bd);
                        TypeNodesTraversal.traverseTypeDeclarationSetVisited(preprocessedTempData, (TypeDeclaration) bd, bdp.getLocationClassString());
                        break;
                    case PreprocessedTempData.BODY_SAME_REMOVE:
                        this.preprocessedTempData.addToSrcRemoveList(bd);
                        TypeNodesTraversal.traverseTypeDeclarationSetVisited(preprocessedTempData, (TypeDeclaration) bd, bdp.getLocationClassString());
                        break;
                }
            }
        }
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.srcNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
            BodyDeclaration bd = bdp.getBodyDeclaration();

            if (!(bd instanceof TypeDeclaration)) {
                switch (value) {
                    case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
                    case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
                        break;
                    case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                        this.preprocessedData.addBodiesDeleted(bdp);
                        preprocessedTempData.addToSrcRemoveList(bd);
                        break;
                    case PreprocessedTempData.BODY_SAME_REMOVE:
                        preprocessedTempData.addToSrcRemoveList(bd);
                        break;
                }
            }
//            if(bd instanceof MethodDeclaration){
//                MethodDeclaration md = (MethodDeclaration) bd;
////                if(md.getName().toString().equals("create")){
////                    System.out.println("aa");
//                    break;
//                }
//            }
        }
    }

    private void iterateVisitingMap2LoadContainerMap() {
        for (Entry<BodyDeclarationPair, Integer> item : preprocessedTempData.srcNodeVisitingMap.entrySet()) {
            BodyDeclarationPair bdp = item.getKey();
            int value = item.getValue();
//            System.out.println(bdp.getBodyDeclaration().toString());
//            System.out.println(bdp.getLocationClassString());
//            System.out.println(value);
            switch (value) {
                case PreprocessedTempData.BODY_DIFFERENT_RETAIN:
                    this.preprocessedData.entityContainer.addKey(bdp);
                    break;
                case PreprocessedTempData.BODY_FATHERNODE_REMOVE:
                case PreprocessedTempData.BODY_INITIALIZED_VALUE:
                case PreprocessedTempData.BODY_SAME_REMOVE:
                    break;
            }
        }
        this.preprocessedData.entityContainer.sortKeys();

    }

    public PreprocessedData getPreprocessedData() {
        return preprocessedData;
    }

    public void undeleteSignatureChange() {
        List<BodyDeclarationPair> addTmp = new ArrayList<>();
        for (BodyDeclarationPair bdpAdd : preprocessedData.getmBodiesAdded()) {
            if (bdpAdd.getBodyDeclaration() instanceof MethodDeclaration) {
                MethodDeclaration md = (MethodDeclaration) bdpAdd.getBodyDeclaration();
                String methodName = md.getName().toString();
                List<BodyDeclarationPair> bdpDeleteList = new ArrayList<>();
                for (BodyDeclarationPair bdpDelete : preprocessedData.getmBodiesDeleted()) {
                    if (bdpDelete.getBodyDeclaration() instanceof MethodDeclaration) {
                        MethodDeclaration md2 = (MethodDeclaration) bdpDelete.getBodyDeclaration();
                        String methodName2 = md2.getName().toString();
                        if (potentialMethodNameChange(methodName, methodName2)) {
                            bdpDeleteList.add(bdpDelete);
                        }
                    }
                }
                if (bdpDeleteList.size() > 0) {
                    //remove的时候可能会有hashcode相同但是一个是在内部类的情况，但是这种情况很少见，所以暂时先不考虑
                    preprocessedTempData.dstRemovalNodes.remove(bdpAdd.getBodyDeclaration());
                    addTmp.add(bdpAdd);
                    for (BodyDeclarationPair bdpTmp : bdpDeleteList) {
                        this.preprocessedTempData.srcRemovalNodes.remove(bdpTmp.getBodyDeclaration());
                        this.preprocessedData.getmBodiesDeleted().remove(bdpTmp);
                        this.preprocessedData.entityContainer.addKey(bdpTmp);
                    }
                }
            }

        }
        for (BodyDeclarationPair tmp : addTmp) {
            this.preprocessedData.getmBodiesAdded().remove(tmp);
        }
    }

    public boolean potentialMethodNameChange(String name1, String name2) {
        if (name1.length() == 0) return false;
        String tmp;
        if (name1.length() > name2.length()) {
            tmp = name1;
            name1 = name2;
            name2 = tmp;
        }
        int i;
        for (i = 0; i < name1.length(); i++) {
            char ch1 = name1.charAt(i);
            char ch2 = name2.charAt(i);
            if (ch1 != ch2) {
                break;
            }
        }
        double ii = (i * 1.0) / name1.length();
        if (ii > 0.7) {
//            System.out.println("Potential:"+name1+" "+name2);
            return true;
        }
        return false;
    }


}
