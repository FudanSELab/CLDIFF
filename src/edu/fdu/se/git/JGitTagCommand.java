package edu.fdu.se.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.bean.AndroidPlatformFrameworkProject;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidBranchDAO;
import edu.fdu.se.dao.AndroidTagDAO;

public class JGitTagCommand extends JGitCommand {

	public JGitTagCommand(String repopath) {
		super(repopath);
	}

	public RevCommit revCommitOfTag(String tagSHAId) {
		try {
			ObjectId commitId = ObjectId.fromString(tagSHAId);
			RevObject object;
			object = revWalk.parseAny(commitId);
			if (object instanceof RevTag) {
				// annotated
				RevTag tagObj = (RevTag) object;
				RevObject revObj = tagObj.getObject();
				if (revObj instanceof RevCommit) {
					RevCommit result = revWalk.parseCommit(revObj.getId());
					return (RevCommit) result;
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



	/**
	 * return revcommit
	 * 
	 * @param branchId
	 * @return
	 */
	public RevCommit revCommitOfCommitId(String commit) {
		try {
			ObjectId commitId = ObjectId.fromString(commit);
			RevCommit object;
			object = revWalk.parseCommit(commitId);
			return object;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * list项目所有的tag，并存数据库
	 * 
	 * @param project
	 * @return
	 */
	public int listTagsAndSaveToDb(AndroidPlatformFrameworkProject project) {
		List<Ref> call = null;
		try {
			call = git.tagList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		for (Ref ref : call) {
			String tagName = ref.getName();
			String[] data = tagName.split("/");
			String tagShortName = data[data.length - 1];
			String tagSha = ref.getObjectId().getName();
			AndroidTag at = new AndroidTag(0, tagName, tagShortName, tagSha, project.getProjectSubPath());
			AndroidTagDAO.insert(at);
			// System.out.println(ref.getName() + "\n" +
			// ref.getObjectId().getName() + "\n");
		}
		return call.size();
		// LogCommand log = git.log();
		// Ref peeledRef = repository.peel(ref);
		// if(peeledRef.getPeeledObjectId() != null) {
		// log.add(peeledRef.getPeeledObjectId());
		// log.add(ref.getObjectId());
		// Iterable<RevCommit> logs = log.call();
	}

	/**
	 * true 如果是重叠的 false则不在时间线上
	 * 
	 * @param start
	 * @param end
	 * @param revCommitList
	 * @return
	 */
	public boolean walkRepoBackwardsStartWithCommitId(RevCommit start, RevCommit end, List<RevCommit> revCommitList) {
		assert start.getCommitTime() > end.getCommitTime();// 从后往前 start值大于end
		boolean res = false;
		try {
			Queue<RevCommit> commitQueue = new LinkedList<RevCommit>();
			Map<String, Boolean> isTraversed = new HashMap<String, Boolean>();
			commitQueue.offer(start);
			while (commitQueue.size() != 0) {
				RevCommit queueCommitItem = commitQueue.poll();
				RevCommit[] parentCommits = queueCommitItem.getParents();
				if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
					continue;
				}
				isTraversed.put(queueCommitItem.getName(), true);
				revCommitList.add(queueCommitItem);
				if (queueCommitItem.getName().equals(end.getName())) {
					res = true;
					continue;
				}
				// early than end
				if (queueCommitItem.getCommitTime() < end.getCommitTime()) {
					continue;
				}
				for (RevCommit item2 : parentCommits) {
					RevCommit commit2 = revWalk.parseCommit(item2.getId());
					commitQueue.offer(commit2);

				}
			}
			return res;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Ref getBranchByName(String branchName) {
		try {
			List<Ref> branchList = this.git.branchList().call();
			for (Ref ref : branchList) {
				String bName = ref.getName();
				if (bName.equals(branchName)) {

					return ref;
				}
			}
			return null;
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Ref> getBranchesByShortNames(String[] branchNames) {
		try {
			String prefix = "refs/heads/";
			List<Ref> branchList = this.git.branchList().call();
			List<Ref> result = new ArrayList<Ref>();
			for (Ref ref : branchList) {
				String bName = ref.getName();
				for (String item : branchNames) {
					if (bName.equals(prefix + item)) {
						result.add(ref);
						break;
					}
				}

			}
			return result;
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Ref getBranchByShortName(String branchName) {
		try {
			String prefix = "refs/heads/";
			List<Ref> branchList = this.git.branchList().call();
			for (Ref ref : branchList) {
				String bName = ref.getName();
				if (bName.equals(prefix + branchName)) {
					return ref;
				}
			}
			return null;
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * list项目所有的branch，并存数据库
	 * 
	 * @param project
	 * @return
	 */
	public int listBranchesAndSaveToDb(AndroidPlatformFrameworkProject project) {
		List<Ref> call = null;
		try {
			call = git.branchList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		for (Ref ref : call) {
			String branchName = ref.getName();
			String[] data = branchName.split("/");
			String branchShortName = data[data.length - 1];
			RevObject object = null;
			String commitSha = null;
			try {
				object = revWalk.parseAny(ref.getObjectId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (object instanceof RevCommit) {
				RevCommit commit = (RevCommit) object;
				commitSha = commit.getName();
			} else {
				System.err.println("invalid");
			}
			AndroidBranch ab = new AndroidBranch(0, branchName, branchShortName, commitSha,
					project.getProjectSubPath());
			AndroidBranchDAO.insert(ab);
		}
		return call.size();
	}

	public void walkRepoBackwardsStartFromTag(String tagSHA) {
		RevCommit rc = this.revCommitOfTag(tagSHA);
		walkRepoBackwardsStartFromCommit(rc);
	}
	
	public void walkRepoBackwardsStartFromCommit(String sha){
		RevCommit rc = this.revCommitOfCommitId(sha);
		walkRepoBackwardsStartFromCommit(rc);

	}
	
	public void walkRepoBackwardsStartFromCommit(RevCommit rc) {
		try {
			Queue<RevCommit> commitQueue = new LinkedList<RevCommit>();
			Map<String, Boolean> isTraversed = new HashMap<String, Boolean>();
			commitQueue.offer(rc);
			while (commitQueue.size() != 0) {
				RevCommit queueCommitItem = commitQueue.poll();
				RevCommit[] parentCommits = queueCommitItem.getParents();
				if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
					continue;
				}
//				System.out.println(queueCommitItem.getName());
				isTraversed.put(queueCommitItem.getName(), true);
				for (RevCommit item2 : parentCommits) {
					RevCommit commit2 = revWalk.parseCommit(item2.getId());
					commitQueue.offer(commit2);
				}
			}
			System.out.println("CommitSum:" + isTraversed.size());
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
