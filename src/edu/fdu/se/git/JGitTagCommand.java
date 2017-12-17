package edu.fdu.se.git;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.bean.AndroidPlatformFrameworkProject;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;

public class JGitTagCommand extends JGitCommand {

	public JGitTagCommand(String repopath) {
		super(repopath);
	}

	public void walkAllTags(CommitTagVisitor visitor) {
		Map<String, Ref> tags = repository.getTags();
		visitor.visit(this, tags.entrySet());
	}

	public String commitIdOfTag(String tagStr) {
		try {
			ObjectId commitId = ObjectId.fromString(tagStr);
			RevObject object;
			object = revWalk.parseAny(commitId);
			if (object instanceof RevTag) {
				// annotated
				RevTag tagObj = (RevTag) object;
				RevObject revObj = tagObj.getObject();
				if (revObj instanceof RevCommit) {
					return revObj.getName();
				} else {
					System.err.println("ERR COmmit");
				}
			} else {
				// invalid
				System.err.println("invalid");
			}
			return null;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int listTags(AndroidPlatformFrameworkProject project) {
		// RevWalk revWalk = new RevWalk(repository);
		// filterTags(revWalk);
		
		List<Ref> call = null;
		try {
			call = git.tagList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		for (Ref ref : call) {
			String tagName = ref.getName();
			String[] data = tagName.split("/");
			String tagShortName = data[data.length-1];
			String tagSha = ref.getObjectId().getName();
			AndroidTag at = new AndroidTag(0,tagName,tagShortName,tagSha,project.getProjectSubPath());
			AndroidTagDAO.insert(at);
//			System.out.println(ref.getName() + "\n" + ref.getObjectId().getName() + "\n");
		}
		return call.size();
		// i++;
		// LogCommand log = git.log();
		// Ref peeledRef = repository.peel(ref);
		// if(peeledRef.getPeeledObjectId() != null) {
		//
		// log.add(peeledRef.getPeeledObjectId());
		// } else {
		// log.add(ref.getObjectId());
		// }
		// Iterable<RevCommit> logs = log.call();
		// for (RevCommit rev : logs) {
		// System.out.println("Commit: " + rev /* + ", name: " +
		// rev.getName() + ", id: " + rev.getId().getName() */);
		// }
	}

}
