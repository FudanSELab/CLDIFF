package edu.ucla.se;

/*
 * The score is the length of LCS divided by the average length
 */
public class LCSScoreComputer implements ScoreComputer {

	@Override
	public boolean IsSimilar(String str1, String str2, double sim_ratio) {
		double lcs_len = getLCSLength(str1, str2);
		double total_len = str1.length() + str2.length();
		double score = (2*lcs_len) / total_len;
		//System.out.println(lcs_len);
		//System.out.println(total_len);
		//System.out.println(sim_ratio);
		return score > sim_ratio;
	}
	
	private int getLCSLength(String str1, String str2) {
		int m = str1.length();
		int n = str2.length();
		int [][] lcs_len = new int[m+1][n+1];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				lcs_len[i][j] = 0;
			}
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (str1.charAt(i) == str2.charAt(j)) {
					lcs_len[i+1][j+1] = lcs_len[i][j]+1;
				}
				else {
					lcs_len[i+1][j+1] = Math.max(lcs_len[i+1][j], lcs_len[i][j+1]);
				}
			}
		}
		return lcs_len[m][n];
	}

}
