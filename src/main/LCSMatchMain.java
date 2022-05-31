package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucla.se.*;
public class LCSMatchMain {

	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 3) {
            System.err.println("Usage [Repo Path] [Commit Id] [Search Path].\n");
            System.exit(1);
        }

        String repoPath = Paths.get(args[0]).toString();
        String commitId = args[1];
        Path searchPath = Paths.get(args[2]);
        
        System.out.printf("Start missing change search for git repo %s and commit %s...\n", repoPath, commitId);
		
		GitHandler gitHandler = new GitHandler(repoPath, commitId, P_LANG.JAVA);

        Map<String, List<Integer>> lines = new HashMap<>();
        Map<String, Map<Integer, String>> oldContents = gitHandler.getOldFileContentByLine(lines);
        
        ScoreComputer sc = new LCSScoreComputer();
		PEAM peam = new PEAM(sc);
			
		double sim_score_thres = 0.79;
		double min_sup_ratio = 1;
		double match_score = 0.5;
		
		System.out.println("Start Finding Patterns...");
		peam.FindFrequentPattern(oldContents, sim_score_thres, min_sup_ratio);
		peam.PrintPatterns();
		System.out.println("Done");
		
		int pattern_cnt = peam.GetPatternCnt();
		Map<String, List <MissingChangeInfo> > matches = peam.RecursiveFindMatch(searchPath, 
					4, 
					(int) (pattern_cnt*0.7), 
					(int) (pattern_cnt*0.7), 
					match_score);
		
		
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
