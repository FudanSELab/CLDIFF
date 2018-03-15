package edu.fdu.se.main.astdiff;

import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.ClusterActions;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperation;
import edu.fdu.se.astdiff.preprocessingfile.FilePairPreDiff;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessedData;
import edu.fdu.se.astdiff.treegenerator.JavaParserTreeGenerator;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;

/**
 * Created by huangkaifeng on 2018/2/27.
 *
 */
public class BaseDiffMiner {

    protected void doo(String filePrev, String fileCurr, String outputDirName) {
        FilePairPreDiff psc = new FilePairPreDiff().compareTwoFile(filePrev, fileCurr, outputDirName);
        PreprocessedData preData = psc.getPreprocessedData();
        JavaParserTreeGenerator jtg = new JavaParserTreeGenerator(preData.getPreviousCu().toString(),preData.getCurrentCu().toString());
        MyActionGenerator gen = new MyActionGenerator(jtg.src, jtg.dst, jtg.mapping);
        GeneratingActionsData data = gen.generate();
        if("true".equals(ProjectProperties.getInstance().getValue(PropertyKeys.DEBUG_SRC_DST_TREE))){
            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/srcTree.txt", jtg.getPrettyOldTreeString());
            FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR) + "/dstTree.txt", jtg.getPrettyNewTreeString());
            SimpleActionPrinter.printMyActions(data.getAllActions());
        }
        MiningActionData mMiningActionData = new MiningActionData(data, jtg.srcTC, jtg.dstTC, jtg.mapping);
        ClusterActions.doCluster(mMiningActionData);
        MiningOperation mo = new MiningOperation(preData,mMiningActionData);
        mo.printListDiffMiner();
        mo.printListPreprocess();
    }
}
