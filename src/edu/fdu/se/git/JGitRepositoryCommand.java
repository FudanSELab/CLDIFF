package edu.fdu.se.git;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.gitrepo.RepoConstants;

public class JGitRepositoryCommand extends JGitCommand{

	public JGitRepositoryCommand(String repopath) {
		super(repopath);
	}
	
	/**
	 * get all the commit info reversely
	 * 
	 * @param visitor
	 */
	public void walkRepoFromBackwards(CommitVisitorLog visitor) {
		try {
			Map<String, Ref> refs = repository.getAllRefs();
			Queue<RevCommit> commitQueue = new LinkedList<RevCommit>();
			Map<String, Boolean> isTraversed = new HashMap<String, Boolean>();
			for (Entry<String, Ref> item : refs.entrySet()) {
				RevCommit commit = revWalk.parseCommit(item.getValue().getObjectId());
				commitQueue.offer(commit);
				while (commitQueue.size() != 0) {
					RevCommit queueCommitItem = commitQueue.poll();
					RevCommit[] parentCommits = queueCommitItem.getParents();
					if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
						continue;
					}
					Map<String, List<String>> changedFiles = this.getCommitFileList(queueCommitItem.getName());
					visitor.visit(queueCommitItem, changedFiles);
					isTraversed.put(queueCommitItem.getName(), true);
					for (RevCommit item2 : parentCommits) {
						RevCommit commit2 = revWalk.parseCommit(item2.getId());
						commitQueue.offer(commit2);
					}
				}
			}
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public void walkRepoFromBackwardsToDB(CommitVisitorDB visitor) {
		try {
//			Map<String, Ref> refs = repository.getAllRefs();
			Queue<RevCommit> commitQueue = new LinkedList<RevCommit>();
			Map<String, Boolean> isTraversed = new HashMap<String, Boolean>();
//			
//			List<AndroidRepoCommit> allData = AndroidRepoCommitDAO.selectAll();
//			for(AndroidRepoCommit item:allData){
//				isTraversed.put(item.getCommitId(),true);
//			}
//			;//343365
			List<Ref> mList = this.git.branchList().setListMode(ListMode.ALL).call();
			for (Ref item : mList) {
				RevCommit commit = revWalk.parseCommit(item.getObjectId());
				commitQueue.offer(commit);
				while (commitQueue.size() != 0) {
					RevCommit queueCommitItem = commitQueue.poll();
					RevCommit[] parentCommits = queueCommitItem.getParents();
					if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
						continue;
					}
					Map<String, List<String>> changedFiles = this.getCommitFileList(queueCommitItem.getName());
					visitor.visit(queueCommitItem, changedFiles);
					System.out.println(queueCommitItem.getName());
					isTraversed.put(queueCommitItem.getName(), true);
					for (RevCommit item2 : parentCommits) {
						RevCommit commit2 = revWalk.parseCommit(item2.getId()); 
						commitQueue.offer(commit2);
					}
				}
			}
			System.out.println("CommitSum:" + isTraversed.size());
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}

	}
	
	/**
	 * 给一个tag name，搜索所有的branch分支，是否有经过此tag的commit，如果有则说明与此tag关联
	 * @param visitor
	 */
	public void walkRepoFromBackwardsGivenOneTagNameAndBranchNameList(String tagName) {
		try {
			Queue<RevCommit> commitQueue = new LinkedList<RevCommit>();
			Map<String, Boolean> isTraversed = new HashMap<String, Boolean>();
			List<Ref> mList = this.git.branchList().setListMode(ListMode.ALL).call();
			for (Ref item : mList) {
				RevCommit commit = revWalk.parseCommit(item.getObjectId());
				commitQueue.offer(commit);
				while (commitQueue.size() != 0) {
					RevCommit queueCommitItem = commitQueue.poll();
					RevCommit[] parentCommits = queueCommitItem.getParents();
					if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
						continue;
					}
					System.out.println(queueCommitItem.getName());
					isTraversed.put(queueCommitItem.getName(), true);
					for (RevCommit item2 : parentCommits) {
						RevCommit commit2 = revWalk.parseCommit(item2.getId()); 
						commitQueue.offer(commit2);
					}
				}
			}
			System.out.println("CommitSum:" + isTraversed.size());
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}

	}


	/**
	 * 分支的commit 信息存在本地 ，变动信息在remote
	 */
	public void walkRepoBackwardDividedByBranch(CommitVisitorDB visitor) {
		try {
			// List<Ref> mList = this.git.branchList().setListMode(
			// ListMode.REMOTE ).call();
			List<Ref> mList = this.git.branchList().setListMode(ListMode.ALL).call();
			System.out.println(mList.size());
			// for(Ref item:mList){
			// System.out.println(item.getName());
			// RevCommit commit = revWalk.parseCommit(item.getObjectId());
			// visitor.visitBranch(commit, item.getName());
			// }

		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String args[]){
		JGitRepositoryCommand cmd = new JGitRepositoryCommand(
				ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)+RepoConstants.platform_frameworks_base_ + ".git");
//		cmd.getCommitParentMappedFileList2("cd97c0e935d13bbd29dce0417093ec694c3ddd76");
//		CommitCodeInfo cci = cmd.getCommitFileEditSummary("c7f502947b5b80baca084101fb7a0aaa74db9974", JGitCommand.JAVA_FILE);
	}
	public Map<String, Map<String, List<String>>> getCommitParentMappedFileList2(String commmitid) {
		Map<String, Map<String, List<String>>> result = new HashMap<String, Map<String, List<String>>>();
		ObjectId commitId = ObjectId.fromString(commmitid);
		RevCommit commit = null;

		try {
			commit = revWalk.parseCommit(commitId);
			RevCommit[] parentsCommits = commit.getParents();
			for (RevCommit parent : parentsCommits) {
				ObjectReader reader = git.getRepository().newObjectReader();
				CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
				ObjectId newTree = commit.getTree().getId();
				newTreeIter.reset(reader, newTree);
				CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
				RevCommit pCommit = revWalk.parseCommit(parent.getId());
				ObjectId oldTree = pCommit.getTree().getId();
				oldTreeIter.reset(reader, oldTree);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
				DiffFormatter diffFormatter = new DiffFormatter(out);
				diffFormatter.setRepository(git.getRepository());
				List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
				diffFormatter.setContext(0);
				for (DiffEntry entry : entries) {
					switch (entry.getChangeType()) {
					case ADD:
						break;
					case MODIFY:
					case DELETE:
//						this.getCommitEditScript(null, entry);	
//			             RawText r = new RawText(out.toByteArray());
//			               r.getLineDelimiter();
			             System.out.println(out.toString());
			             out.reset();
						break;
					
					default:
						break;
					}
				}
				diffFormatter.close();
			}
			return result;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	



}
