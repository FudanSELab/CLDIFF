package edu.fdu.se.gitrepo;

import java.util.Map;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitTagCommand;

public class Test {
	public static void main(String args[]){
		System.out.println(new Test().uniquePaths(3, 7));
	}
	int [][] dp;
	public int uniquePaths(int m,int n){
		dp = new int[m+1][n+1];
		for(int i=0;i<=n;i++){
			dp[1][i] = 1;
			dp[0][i] = 1;
		}
		for(int j=0;j<=m;j++){
			dp[j][1] = 1;
			dp[j][0] =1;
		
		}
		for(int i=2;i<=n;i++)
			for(int j=2;j<=m;j++){
				dp[j][i] = dp[j][i-1]+dp[j-1][i];
			}
		return dp[3][7];
		
	}

}
