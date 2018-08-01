package edu.fdu.se.base.associating.similarity;

import at.unisalzburg.dbresearch.apted.costmodel.PerEditOperationStringNodeDataCostModel;
import at.unisalzburg.dbresearch.apted.distance.APTED;
import at.unisalzburg.dbresearch.apted.node.Node;
import at.unisalzburg.dbresearch.apted.node.StringNodeData;
import com.github.gumtreediff.tree.Tree;

public class TreeDistance {

    private Tree srcTree;
    private Tree dstTree;
    Node<StringNodeData> t1;
    Node<StringNodeData> t2;


    public TreeDistance(){}

    public TreeDistance(Tree srcTree, Tree dstTree) {
        this.srcTree = srcTree;
        this.dstTree = dstTree;
    }

    public void setSrcTree(Tree srcTree) {
        this.srcTree = srcTree;
    }

    public void setDstTree(Tree dstTree) {
        this.dstTree = dstTree;
    }

    //计算相似度值
    public float calculateTreeDistance(){
        TreeInputParser parser = new TreeInputParser();
        t1 = parser.fromTree(srcTree);
        t2 = parser.fromTree(dstTree);
        APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel(0.4f, 0.4f, 0.6f));
        return apted.computeEditDistance(t1, t2);
    }

    public void getDistance() {
        //FileReader fr = new FileReader("buf.txt");
        String path = "D:\\Workspace\\CodeDiff\\SDK_Miner_Out\\testtree.json";
        String path0 = "D:\\Workspace\\CodeDiff\\SDK_Miner_Out\\test.json";
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader(path));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        InputStream in = new FileInputStream("D:\\demo.txt");//读取文件上的数据。
//        //将字节流向字符流的转换。
//        InputStreamReader isr = new InputStreamReader(in);//读取
//        //创建字符流缓冲区
//        BufferedReader bufr = new BufferedReader(isr);//缓冲
//        String json = getDatafromFile(path);
//        Gson gson = new Gson();
//        BaseData metaData = new Gson().fromJson(json, BaseData.class);
//        BaseData metaData = new BaseData()

//        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_2);

        // Parse the input.
//        BracketStringInputParser parser = new BracketStringInputParser();
//        Node<StringNodeData> t1 = parser.fromString(metaData.getT1());
//        Node<StringNodeData> t2 = parser.fromString(metaData.getT2());
//        // Initialise algorithms.
//        APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel(0.4f, 0.4f, 0.6f));
////        AllPossibleMappingsTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apmted = new AllPossibleMappingsTED<>(new PerEditOperationStringNodeDataCostModel(0.4f, 0.4f, 0.6f));
//        // Calculate distances using both algorithms.
//        float result = apted.computeEditDistance(t1, t2);
//        //float correctResult = apmted.computeEditDistance(t1, t2);
//        System.out.println("result:"+result);
        //System.out.println("correctResult:"+correctResult);
    }

}
