package edu.fdu.se.git;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListTagCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import edu.fdu.se.fileutil.FileUtil;

public class JGitCommand {

	private Repository repository;
	private RevWalk revWalk;
	public String repoPath;
	private Git git;
	/**
	 * Constructor
	 * @param repopath
	 */
	public JGitCommand(String repopath) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			repository = builder.setGitDir(new File(repopath)).readEnvironment() // scan
																					// environment
																					// GIT_*
																					// variables
					.findGitDir() // scan up the file system tree
					.build();
			revWalk = new RevWalk(repository);
			git = new Git(repository);
			repoPath = repopath;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private RevCommit revCommit;
	/**
	 * get added/changed/removed file list given a commit id
	 * @param commmitid
	 * @return
	 */
	public Map<String, List<String>> getCommitFileList(String commmitid) {
		ObjectId commitId = ObjectId.fromString(commmitid);
		RevCommit commit = null;
		Map<String, List<String>> fileList = new HashMap<String, List<String>>();
		List<String> addList = new ArrayList<String>();
		List<String> modifyList = new ArrayList<String>();
		List<String> deleteList = new ArrayList<String>();

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
				DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
				diffFormatter.setRepository(git.getRepository());
				List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
				for (DiffEntry entry : entries) {
					switch (entry.getChangeType()) {
					case ADD:
						addList.add(entry.getNewPath());
						break;
					case MODIFY:
						modifyList.add(entry.getNewPath());
						break;
					case DELETE:
						deleteList.add(entry.getNewPath());
						break;
					default:
						break;
					}
				}
				diffFormatter.close();
				fileList.put("addedFiles", addList);
				fileList.put("modifiedFiles", modifyList);
				fileList.put("deletedFiles", deleteList);
			}
			return fileList;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 */
	public Map<String,Map<String, List<String>>> getCommitParentMappedFileList(String commmitid) {
		Map<String,Map<String,List<String>>> result =new HashMap<String,Map<String,List<String>>>();
		ObjectId commitId = ObjectId.fromString(commmitid);
		RevCommit commit = null;
		Map<String, List<String>> fileList = null;
		List<String> addList = null;
		List<String> modifyList = null;
		List<String> deleteList = null;

		try {
			commit = revWalk.parseCommit(commitId);
			RevCommit[] parentsCommits = commit.getParents();
			for (RevCommit parent : parentsCommits) {
				fileList = new HashMap<String, List<String>>();
				addList = new ArrayList<String>();
				modifyList = new ArrayList<String>();
				deleteList = new ArrayList<String>();
				ObjectReader reader = git.getRepository().newObjectReader();
				CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
				ObjectId newTree = commit.getTree().getId();
				newTreeIter.reset(reader, newTree);
				CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
				RevCommit pCommit = revWalk.parseCommit(parent.getId());
				ObjectId oldTree = pCommit.getTree().getId();
				oldTreeIter.reset(reader, oldTree);
				DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
				diffFormatter.setRepository(git.getRepository());
				List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
				for (DiffEntry entry : entries) {
					switch (entry.getChangeType()) {
					case ADD:
						addList.add(entry.getNewPath());
						break;
					case MODIFY:
						modifyList.add(entry.getNewPath());
						break;
					case DELETE:
						deleteList.add(entry.getNewPath());
						break;
					default:
						break;
					}
				}
				diffFormatter.close();
				fileList.put("addedFiles", addList);
				fileList.put("modifiedFiles", modifyList);
				fileList.put("deletedFiles", deleteList);
				result.put(pCommit.getName(), fileList);
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
	/**
	 * get Commits within range 
	 * NOT Used
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<RevCommit> getCommitsInRange(String begin, String end) {
		List<RevCommit> commitList = new ArrayList<RevCommit>();
		try {
			ObjectId beginId = ObjectId.fromString(begin);
			ObjectId endId = ObjectId.fromString(end);
			ObjectId commitId = beginId;
			while (true) {
				RevCommit tmp = revWalk.parseCommit(commitId);
				commitList.add(tmp);
				break;
			}

			return null;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * get all the commit info reversely
	 * @param visitor
	 */
	public void walkRepoFromBackwards(CommitVisitor visitor) {
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
	/**
	 * read commit time
	 * @param commitId
	 * @return seconds since epoch
	 */
	public int readCommitTime(ObjectId commitId){
		try {
			RevCommit revCommit = revWalk.parseCommit(commitId);
			int time = revCommit.getCommitTime();
			return time;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * read commit time
	 * @param commitId
	 * @return seconds since epoch
	 */
	public int readCommitTime(String commitId){
		try {
			ObjectId id = ObjectId.fromString(commitId);
			RevCommit revCommit = revWalk.parseCommit(id);
			int time = revCommit.getCommitTime();
			return time;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void walkAllTags(CommitTagVisitor visitor) {
		Map<String,Ref> tags = repository.getTags();
		visitor.visit(this,tags.entrySet());
	}
	/**
	 * TODO print Diff entry
	 * @param commit
	 * @return
	 */
//	public List<Object> getChangeFiles(RevCommit commit){
////    	List<ChangeFile> changeFiles= new ArrayList<ChangeFile>();
//		
//		AbstractTreeIterator newTree = prepareTreeParser(commit);
//		if(commit.getParentCount()==0) return changeFiles;
//    	AbstractTreeIterator oldTree = prepareTreeParser(commit.getParent(0));
//    	List<DiffEntry> diff= null;
//		try {
//			diff = git.diff().setOldTree(oldTree).setNewTree(newTree).call();
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		}
//        //ÿһ��diffEntry�����ļ��汾֮��ı䶯����
//		for (DiffEntry diffEntry : diff) {
//			//DiffEntry.ChangeType.MODIFY.toString().equals(diffEntry.getChangeType().toString())&&
//			if(diffEntry.getNewPath()!=null&&diffEntry.getNewPath().endsWith(".java")){
////				changeFiles.add(new ChangeFile(diffEntry.getChangeType().toString(), diffEntry.getOldPath(), diffEntry.getNewPath(), 
////	        			commit.getName(), (commit.getParents()[0]).getName(), diffEntry.getNewId().toObjectId(), diffEntry.getOldId().toObjectId()));
//
//				String fName = "aa"+(new Random()).nextInt(1000);
//				while(new File(fName).exists()){
//					fName = "aa"+(new Random()).nextInt(1000);
//				}
//				BufferedOutputStream out =null;
//	            try {
//	            	out = new BufferedOutputStream(new FileOutputStream(fName));
//					DiffFormatter df = new DiffFormatter(out);
//	                df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
//	                df.setRepository(git.getRepository());
//	            	df.format(diffEntry);
//	            	out.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}finally{
//					try {
//						if(out!=null)
//							out.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//	            
//	            insertAllAddAndDelete(commit.getName(),changeFiles.get(changeFiles.size()-1),fName);
//				File file = new File(fName);
//				file.delete();
//			}
//		} 
//       return changeFiles;
//	}
	/**
	 * get parents commit id in string
	 * @param commitId
	 * @return
	 */
	public String[] getCommitParents(String commitId){
		ObjectId id = ObjectId.fromString(commitId);
		try {
			RevCommit commit = revWalk.parseCommit(id);
			RevCommit[] parentCommits = commit.getParents();
			String[] parentCommitsString = new String[parentCommits.length];
			for(int i=0;i<parentCommits.length;i++){
				parentCommitsString[i]=parentCommits[i].getName();
			}
			return parentCommitsString;
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * checkout to one commit
	 * @param commitid
	 */
	public void checkout(String commitid) {
		try {
			CheckoutCommand checkoutCommand = git.checkout();
			checkoutCommand.setName(commitid).call();
		} catch (RefAlreadyExistsException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidRefNameException e) {
			e.printStackTrace();
		} catch (CheckoutConflictException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		JGitCommand cmd = new JGitCommand("D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base\\.git");
		// cmd.walkRepoFromBackwards(new CommitVisitor());//1.
//		cmd.walkAllTags(new CommitVisitor());
	}
	/**
	 * extract file given file path and commit id
	 * @param fileName
	 * @param revisionId
	 * @return
	 */
	public byte[] extract(String fileName, String revisionId) {
		if (revisionId == null || fileName == null) {
			System.err.println("revisionId/fileName is null..");
			return null;
		}
		if (repository == null || git == null || revWalk == null) {
			System.err.println("git repo is null..");
			return null;
		}
		RevWalk walk = new RevWalk(repository);
		try {
			ObjectId objId = repository.resolve(revisionId);
			if (objId == null) {
				System.err.println("The revision:" + revisionId + " does not exist.");
				walk.close();
				return null;
			}
			RevCommit commit = walk.parseCommit(repository.resolve(revisionId));
			if (commit != null) {
				RevTree tree = commit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, tree);
				ObjectId id = treeWalk.getObjectId(0);
				InputStream is = FileUtil.open(id, repository);
				byte[] res = FileUtil.toByteArray(is);
				return res;
			} else {
				System.err.println("Cannot found file(" + fileName + ") in revision(" + revisionId + "): " + revWalk);
			}
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		walk.close();
		return null;
	}

}
