package edu.fdu.se.main.astdiff.RQ;

/**
 * Created by huangkaifeng on 2018/4/4.
 *
 */
public class RQ1Data {

    public static void main(String args[]){
//    	long startTime = System.currentTimeMillis();    //获取开始时间  
//    	System.out.println("开始时间：" + startTime + "ms");  
    	
//        allCommits();        
        
//        long endTime = System.currentTimeMillis();    //获取结束时间
//        System.out.println("结束时间：" + endTime + "ms"); 
//
//    	System.out.println("程序运行时间：" + (endTime - startTime) + "ms");  
        oneCommit();
    }

    // 1.抽取所有的commit filter掉不需要的

    public static void allCommits(){
        String repo = "E:/projects/RxJava/.git";
        String outputDir = "";
        System.out.println("\n\n----------------------------"+repo);  
        JGitHelper cmd = new JGitHelper(repo);
        cmd.walkRepoFromBackwards(outputDir);
    }


    // 2.抽取特定的commit
    public static void oneCommit(){
        String repo = "E:/projects/RxJava/.git";
        String outputDir = "";
        String commitID = "8a6bf14fc9a61f7c1c0016ca217be02ca86211d2";
        JGitHelper cmd = new JGitHelper(repo);
        cmd.analyzeOneCommit(commitID,outputDir);
    }

}
