package edu.fdu.se.git;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.dao.AndroidRepoCommitDAO;

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
			// TODO Auto-generated catch block
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
			// } catch (MissingObjectException e) {
			// e.printStackTrace();
			// } catch (IncorrectObjectTypeException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
		}

	}



}
