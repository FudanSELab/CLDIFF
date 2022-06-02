package edu.ucla.se;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;
import java.util.List;
import java.util.HashMap;

public class PEAM {
	ScoreComputer score_computer;
	ArrayList <Pattern> pattern_list;
	
	public PEAM(ScoreComputer sc) {
		this.score_computer = sc;
	}
	
	public void PrintPatterns() {
		System.out.println(pattern_list.size());
		for (Pattern p : this.pattern_list) {
			System.out.println(p.GetString());
		}
	}
	
	public int GetPatternCnt() {
		return pattern_list.size();
	}
	
	public ArrayList<Pattern> FindFrequentPattern(
			Map<String, Map<Integer, String>> oldContents,
			double sim_score_thres, double min_sup_ratio){
		 
        ArrayList <String> oldCodes = new ArrayList<String>();
		
        for (Map.Entry<String, Map<Integer, String>> fileEntry : oldContents.entrySet()) {
            String code = "";
        	for (Map.Entry<Integer, String> lineEntry : fileEntry.getValue().entrySet()) {
                code += lineEntry.getValue();
            }
        	oldCodes.add(code);
        }
        
        return FindFrequentPattern(oldCodes, 
        		sim_score_thres, 
        		min_sup_ratio);
	}
	
	
	public ArrayList<Pattern> FindFrequentPattern(ArrayList<String> raw_changes, 
			double sim_score_thres, double min_sup_ratio){
		this.pattern_list = new ArrayList<>();
		int min_sup = (int)(min_sup_ratio * raw_changes.size());
		
		ArrayList<ArrayList <String> > similar_stmts = new ArrayList<>();
		ArrayList<ArrayList <Integer>> similar_stmts_pos = new ArrayList<>();
		
		for(int id=0; id < raw_changes.size(); id++) {
			ArrayList<ArrayList <String> > old_similar_stmts = new ArrayList<>(similar_stmts);
			Statements stmts = new Statements(raw_changes.get(id));
			for(int s_id = 0; s_id < stmts.GetStatementCnt(); s_id++) {
				boolean add_flag = false;
				String statement = stmts.GetStatementAt(s_id);
				for(int i = 0; i < old_similar_stmts.size(); i++) {
					ArrayList<String> stmt_class = old_similar_stmts.get(i);
					for(int j = 0; j < stmt_class.size(); j++) {
						//System.out.println(statement);
						//System.out.println(stmt_class.get(j));
						if(this.score_computer.IsSimilar(statement, stmt_class.get(j), sim_score_thres)) {
							//System.out.println("true");
							ArrayList <String> tmp_s = new ArrayList<>(similar_stmts.get(i));
							//System.out.println(tmp_s.size());
							tmp_s.add(statement);
							//System.out.println(tmp_s.size());
							similar_stmts.set(i, tmp_s);

							ArrayList <Integer> tmp_p = new ArrayList<>(similar_stmts_pos.get(i));
							tmp_p.add(s_id);
							similar_stmts_pos.set(i, tmp_p);
							
							add_flag = true;
							break;
						}
						//else {
						//	System.out.println("false");
						//}
					}
				}
				if (add_flag == false) {
					ArrayList<String> tmp = new ArrayList<>();
					tmp.add(statement);
					similar_stmts.add(tmp);
					ArrayList<Integer> tmp_pos = new ArrayList<>();
					tmp_pos.add(s_id);
					similar_stmts_pos.add(tmp_pos);
				}
			}
		}
		
		for (int i = 0; i < similar_stmts.size(); i++) {
			ArrayList<String> stmt_class = similar_stmts.get(i);
			//System.out.println(i);
			//for(String s: stmt_class) {
			//	System.out.println(s);
			//}
			if (stmt_class.size() >= min_sup) {
				//System.out.println("pass");
				ArrayList<Integer> stmt_class_pos = similar_stmts_pos.get(i);
				String pattern = GetPattern(stmt_class);
				double pattern_pos = GetPatternPosition(stmt_class_pos);
				this.pattern_list.add(new Pattern(pattern, pattern_pos));
				//System.out.println(pattern);
				//System.out.println(pattern_pos);
			}
		}
		
		return this.pattern_list;
	}
	
	public Map<String, List<MissingChangeInfo> > RecursiveFindMatch(Path search_path, 
			int max_interval, 
			int min_stmt_cnt, 
			int min_hit_patterns, 
			double match_score) throws IOException {
		File file = new File(search_path.toString());
		if (file.isFile()) {
			return FindMatch(search_path, 
					max_interval, 
					min_stmt_cnt,
					min_hit_patterns, 
					match_score);
		}
		else {
			Map<String, List<MissingChangeInfo> > return_results = new HashMap<>();
			try (Stream<Path> paths = Files.walk(search_path)){
				for(Path p : paths.filter(Files::isRegularFile).toArray(Path[]::new)) {
					// there will not be duplicate key, so putAll is safe
					String fname = p.toString();
					if (fname.endsWith(".java")) {
						return_results.putAll(FindMatch(p,
							max_interval, 
							min_stmt_cnt,
							min_hit_patterns, 
							match_score));
					}
				}
			}
			return return_results;
		}
	}
	
	public Map<String, List<MissingChangeInfo> > FindMatch(Path file_name, int max_interval, int min_stmt_cnt, 
			int min_hit_patterns, double match_score) throws IOException {
		List<MissingChangeInfo> results = new ArrayList<>();
		StatementsWithLineNumber stmts_with_line = new StatementsWithLineNumber(file_name);
		ArrayList<ArrayList <Integer> > match_results = MatchStatementAndPatterns(stmts_with_line.GetAllStatements(), match_score);
		int stmt_cnt = stmts_with_line.GetStatementCnt();
		
		int cur_start = 0;
		int cur_end = 0;
		int cur_interval = 0;
		boolean cur_activate_status = false;
		HashSet <Integer> cur_matched_patterns = new HashSet<Integer>();
		//System.out.println("Start Matching");
		//System.out.println(stmt_cnt);
		int i = 0;
		while (i < stmt_cnt) {
 			//System.out.println(stmts_with_line.GetStatementAt(i));
			HashSet <Integer> hit_patterns = new HashSet<>(match_results.get(i));
			//for(Integer k : hit_patterns) {
			//	System.out.println(k);
			//}
			if (cur_activate_status == false) {
				if (hit_patterns.size()>0) {
					cur_activate_status = true;
					cur_matched_patterns = hit_patterns;
					cur_start = i;
					cur_end = i;
					cur_interval = 0;
				}
			}
			else {
				boolean renew_flag = false;
				if (hit_patterns.size() > 0) {
					double max_pos = -1;
					for (Integer p : cur_matched_patterns) {
						double pos = pattern_list.get(p).GetPosition(); 
						if (pos > max_pos) {
							max_pos = pos;
						}
					}
					for (Integer p : hit_patterns) {
						if (pattern_list.get(p).GetPosition() > max_pos) {
							cur_matched_patterns.add(p);
							renew_flag = true;
						}
					}
				}
				
				if (renew_flag == true) {
					cur_end = i;
					cur_interval = 0;
				}
				else {
					cur_interval++;
				}
				
				if (cur_interval >= max_interval || i == stmt_cnt-1) {
					cur_activate_status = false;
					if (cur_end - cur_start + 1 >= min_stmt_cnt && cur_matched_patterns.size() >= min_hit_patterns) {
						MissingChangeInfo m = new MissingChangeInfo( 
								stmts_with_line.GetStartLineFor(cur_start),
								stmts_with_line.GetEndLineFor(cur_end),
								"");
						results.add(m);
							
					}
					
					//if (i < stmt_cnt - 1) {
						i = cur_end;
					//}
				}
			}
			i++;
		}
		HashMap <String, List<MissingChangeInfo> > return_results = new HashMap<>();
		return_results.put(file_name.toString(), results);
		return return_results;
	}
	
	private String GetPattern(ArrayList<String> stmt_class) {
		String pattern = "";
		for (int i = 0; i < stmt_class.size(); i++) {
			if (i == 0) {
				pattern = stmt_class.get(i);
			}
			else {
				pattern = GetLCS(pattern, stmt_class.get(i));
			}
		}
		return pattern;
	}
	
	private double GetPatternPosition(ArrayList<Integer> stmt_class_pos) {
		double ave = 0;
		double cnt = stmt_class_pos.size();
		for(int i = 0; i < stmt_class_pos.size(); i++) {
			ave += (stmt_class_pos.get(i)/cnt);
		}
		return ave;
	}
	
	private ArrayList<ArrayList<Integer> > MatchStatementAndPatterns(ArrayList<String> stmts, double match_score){
		ArrayList <ArrayList<Integer> > match_results = new ArrayList<>();
		for (int i = 0; i < stmts.size(); i++) {
			String stmt = stmts.get(i);
			ArrayList <Integer> matches = new ArrayList<>();
			for (int j = 0; j < this.pattern_list.size(); j++) {
				if (this.score_computer.IsSimilar(stmt, this.pattern_list.get(j).GetString(), match_score)) {
					matches.add(j);
				}
			}
			match_results.add(matches);
		}
		return match_results;
	}
	
	private String GetLCS(String str1, String str2) {
		int m = str1.length();
		int n = str2.length();
		int [][] lcs_len = new int[m+1][n+1];
		String [][] lcs = new String[m+1][n+1];
		
		for (int i = 0; i < m+1; i++) {
			for (int j = 0; j < n+1; j++) {
				lcs_len[i][j] = 0;
				lcs[i][j] = "";
			}
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (str1.charAt(i) == str2.charAt(j)) {
					lcs_len[i+1][j+1] = lcs_len[i][j]+1;
					lcs[i+1][j+1] = lcs[i][j]+str1.charAt(i);
				}
				else {
					if (lcs_len[i+1][j] > lcs_len[i][j+1]) {
						lcs_len[i+1][j+1] = lcs_len[i+1][j];
						lcs[i+1][j+1] = lcs[i+1][j];
					}
					else {
						lcs_len[i+1][j+1] = lcs_len[i][j+1];
						lcs[i+1][j+1] = lcs[i][j+1];
					}
				}
			}
		}
		
		return lcs[m][n];
	}
}
