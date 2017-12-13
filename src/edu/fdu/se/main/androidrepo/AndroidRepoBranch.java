package edu.fdu.se.main.androidrepo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;

public class AndroidRepoBranch {
	public static void main(String[] args) throws IOException, GitAPIException {
		Repository repo = new FileRepository(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		Git git = new Git(repo);
		RevWalk walk = new RevWalk(repo);

		List<Ref> branches = git.branchList().call();

		for (Ref branch : branches) {
			String branchName = branch.getName();

			System.out.println("Commits of branch: " + branch.getName());
			System.out.println("-------------------------------------");

			Iterable<RevCommit> commits = git.log().all().call();

			for (RevCommit commit : commits) {
				boolean foundInThisBranch = false;

				RevCommit targetCommit = walk.parseCommit(repo.resolve(commit.getName()));
				for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
					if (e.getKey().startsWith(Constants.R_HEADS)) {
						if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
							String foundInBranch = e.getValue().getName();
							if (branchName.equals(foundInBranch)) {
								foundInThisBranch = true;
								break;
							}
						}
					}
				}

				if (foundInThisBranch) {
					System.out.println(commit.getName());
					System.out.println(commit.getAuthorIdent().getName());
					System.out.println(new Date(commit.getCommitTime()));
					System.out.println(commit.getFullMessage());
				}
			}
		}
	}

}
