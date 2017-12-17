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

public class Test {
	public static void main(String args[]){
		try{
		Repository repo = new FileRepository(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		RevWalk walk = new RevWalk(repo);
		RevCommit commit = walk.parseCommit(repo.resolve("c1211c47ae4707ed07a1fda7fd8cf9a66235fe0b" + "^0"));
		for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet())
			if (e.getKey().startsWith(Constants.R_HEADS))
				if (walk.isMergedInto(commit,
						walk.parseCommit(e.getValue().getObjectId())))
					System.out.println("Ref " + e.getValue().getName()
							+ " contains commit " + commit);
		}catch(Exception e){
			
		}
	}

}
