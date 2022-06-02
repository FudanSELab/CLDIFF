package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucla.se.*;
public class LCSMatchMain {

	
	public static void main(String[] args) throws IOException {
		/*
		if (args.length != 3) {
            System.err.println("Usage [Repo Path] [Commit Id] [Search Path].\n");
            System.exit(1);
        }

        String repoPath = Paths.get(args[0]).toString();
        String commitId = args[1];
        Path searchPath = Paths.get(args[2]);
        
        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);
        */
		String oldPath = "D:\\2021-2022IMPORTANT\\cs230/test-cases/testPatch/old";
        String newPath = "D:\\2021-2022IMPORTANT\\cs230/test-cases/testPatch/new";
        String repoName = "abc";
        Path searchPath = Paths.get("D:\\2021-2022IMPORTANT\\cs230/test-cases/testPatch/new/src/core/A.java");
        
        HashMap<String, List<List<Integer>>> curGroup = new HashMap<>();
        List<Integer> change1 = new ArrayList<>(Arrays.asList(8, 9));
        List<Integer> change2 = new ArrayList<>(Arrays.asList(29, 30));
        List<Integer> change3 = new ArrayList<>(Arrays.asList(38, 39));
        List<List<Integer>> curFile = new ArrayList<List<Integer>>(){
            {
                add(change1);
                add(change2);
                add(change3);
            }
        };
        curGroup.put("A.java", curFile);
		
        GitCreator gitCreator = new GitCreator();
        gitCreator.deleteRepo(repoName);
        gitCreator.createNewRepo(repoName);
        String repoPath = gitCreator.getRepoPath(repoName);
        gitCreator.commitFilesToRepo(repoName, oldPath);
        String commitId = gitCreator.commitFilesToRepo(repoName, newPath);
        GitHandler gitHandler = new GitHandler(gitCreator.getRepo(repoName), commitId, P_LANG.JAVA);
        
        HashMap<Integer,ArrayList<String>> oldContents = new HashMap<>();

        Map<String, Map<Integer, String>> dict = gitHandler.getOldFileContentByLine(curGroup);
        ArrayList<String> val = new ArrayList<>();

        for (Map.Entry f: curGroup.entrySet()){        // file
            String fileName = (String)f.getKey();
            if (!dict.containsKey(fileName)){
                continue;
            }
            Map<Integer, String> fileCodeDict = dict.get(fileName);
            for (int i = 0; i < curGroup.get(fileName).size(); ++i){   // code snippet
                String code = "";
                for (int j = 0; j < curGroup.get(fileName).get(i).size(); j++) {    // lines of code
                    Integer idx = curGroup.get(fileName).get(i).get(j);
                    code += ";";       // new line
                    if (!fileCodeDict.containsKey(idx)){
                        continue;
                    }
                    if (fileCodeDict.get(idx).length() > 0){
                        code += fileCodeDict.get(idx);
                    }
                }
                val.add(code);
            }
        }
        oldContents.put(0, val);

        ArrayList<String> oldContentsCode = oldContents.get(0);
        for (String s : oldContentsCode) {
        	System.out.println("code snippet");
        	System.out.println(s);
        }
        ScoreComputer sc = new LCSScoreComputer();
		PEAM peam = new PEAM(sc);
			
		double sim_score_thres = 0.79;
		double min_sup_ratio = 1;
		double match_score = 0.8;
		
		System.out.println("Start Finding Patterns...");
		peam.FindFrequentPattern(oldContentsCode, sim_score_thres, min_sup_ratio);
		peam.PrintPatterns();
		System.out.println("Done");
		
		int pattern_cnt = peam.GetPatternCnt();
		Map<String, List <MissingChangeInfo> > matches = peam.RecursiveFindMatch(searchPath, 
					4, 
					(int) Math.max((pattern_cnt*0.7),1), 
					(int) Math.max((pattern_cnt*0.7),1), 
					match_score);
		
		System.out.println("Match Finish");
		for(Map.Entry<String, List<MissingChangeInfo>> entry : matches.entrySet()) {
			if (entry.getValue().size() == 0) continue;
			System.out.println(entry.getKey());
			for(MissingChangeInfo m : entry.getValue()) {
				System.out.printf("%d->%d\n", m.startLine, m.endLine);
			}
		}
		/*
		Path path = Paths.get("D:/2021-2022IMPORTANT/cs230/test-cases/Dataset/Patch2/code.txt");
		String code = Files.readString(path);
		String[] split_code = code.split("END");
			
			
		ArrayList <String> changes = new ArrayList<String>();
		Collections.addAll(changes, split_code);
			
					
		System.out.println("Start Finding Patterns...");
		peam.FindFrequentPattern(changes, sim_score_thres, min_sup_ratio);
		peam.PrintPatterns();
		System.out.println("Done");
			
		path = Paths.get("D:/2021-2022IMPORTANT/cs230/test-cases/DataSet\\Patch2\\OLD_JDT10610\\compiler\\org\\eclipse\\jdt\\internal\\compiler\\parser\\Scanner.java");
		//StatementsWithLineNumber s = new StatementsWithLineNumber(path);
			
		int pattern_cnt = peam.GetPatternCnt();
		ArrayList <Integer> matches = peam.FindMatch(path, 
					4, 
					(int) (pattern_cnt*0.7), 
					(int) (pattern_cnt*0.7), 
					match_score);
		for(int i = 0; i < matches.size(); i+=2) {
			System.out.printf("%d-->%d\n", matches.get(i), matches.get(i+1));
		}
		System.out.printf("Find Matches done, %d matches in total", matches.size()/2);
		*/
	}


}
