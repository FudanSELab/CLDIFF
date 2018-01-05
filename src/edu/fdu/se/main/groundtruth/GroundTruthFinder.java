package edu.fdu.se.main.groundtruth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import edu.fdu.se.bean.AndroidCacheCommit;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidCacheCommitDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitRepositoryManager;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.RepositoryHelper;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;
import edu.fdu.se.javaparser.JavaParserFactory;

/**
 * 输入tag标签，tag标签之前的candidate commit
 * 
 * @author huangkaifeng
 *
 */
public class GroundTruthFinder {

	public GroundTruthFinder() {
		tagCmd = JGitRepositoryManager.getBaseCommand();
		this.commitAndTagMap = new HashMap<RevCommit, String>();
		tagCmd = (JGitTagCommand) RepositoryHelper.getInstance1().myCmd;
	}

	JGitTagCommand tagCmd;
	Map<RevCommit, String> commitAndTagMap;

	public static void main(String args[]) {
		GroundTruthFinder run = new GroundTruthFinder();
		// run.addReleaseCommitByTagName("android-4.0.3_r1");
		// run.addReleaseCommitByTagName("android-4.1.1_r1");
		// run.addReleaseCommitByTagName("android-4.2_r1");
		// run.addReleaseCommitByTagName("android-4.3_r1");
		// run.addReleaseCommitByTagName("android-4.4_r1");
		// run.addReleaseCommitByTagName("android-5.0.0_r1");
		// run.addReleaseCommitByTagName("android-5.1.0_r1");
		// run.addReleaseCommitByTagName("android-6.0.0_r1");
		// run.addReleaseCommitByTagName("android-7.0.0_r1");
		run.addReleaseCommitByTagName("android-7.1.0_r1");
		// run.addReleaseCommitByTagName("android-8.0.0_r1");
		run.run();
	}

	private void addReleaseCommitByTagName(String tagName) {
		AndroidTag mTag = AndroidTagDAO.selectTagByShortNameAndProjName(tagName,
				RepoConstants.platform_frameworks_base_);
		RevCommit revCommit = tagCmd.revCommitOfTag(mTag.getTagShaId());
		System.out.println(tagName + "tag commit id: " + revCommit.getName());
		this.commitAndTagMap.put(revCommit, tagName);
	}

	private List<RevCommit> chooseCandidateTag(int commitTime) {
		List<RevCommit> candidateCommit = new ArrayList<RevCommit>();
		for (RevCommit commit : this.commitAndTagMap.keySet()) {
			if (commit.getCommitTime() < commitTime) {
				candidateCommit.add(commit);
			}
		}
		return candidateCommit;
	}

	public void run() {
		List<AndroidCacheCommit> mList = AndroidCacheCommitDAO.selectByKey("android-7.1.0_r1--bug ");
		String line;
		int cnt = 0;
		int size = 0;
//		for (AndroidCacheCommit entry : mList) {
		AndroidCacheCommit entry = mList.get(1);
			line = entry.getCommitId();
			line = line.trim();
			System.out.println(line);
			size++;
			line = line.substring(0, 40);
			int commitTime = tagCmd.readCommitTime(line);
			List<RevCommit> candidate = this.chooseCandidateTag(commitTime);
			if (candidate.size() == 0) {
				System.out.println("early skip");
//				continue;
			}
			CommitCodeInfo mCCI = tagCmd.getCommitFileEditSummary(line, JGitCommand.JAVA_FILE);
			Map<RevCommit, List<FileChangeEditList>> mMap = mCCI.getFileDiffEntryMap();
			for (Entry<RevCommit, List<FileChangeEditList>> item : mMap.entrySet()) {
				RevCommit parent = item.getKey();
				List<FileChangeEditList> list = item.getValue();
				System.out.println("\tParent: " + item.getKey().getName());
				List<String> tagNameListAll = null;
				for (FileChangeEditList item2 : list) {
					// 每一个文件 都去比较一次
					String oldPath = item2.getOldFilePath();
					String fileName = getClassNameFromPath(oldPath);
					// commit change的文件
					System.out.println("\t" + oldPath);
					Map<String, List<BodyDeclaration>> tagVersionMethods = this
							.mappingTagStrToMethodDeclarationList(oldPath, fileName, candidate);
					InputStream fileInputStream = tagCmd.extractAndReturnInputStream(oldPath, parent.getName());
					Set<BodyDeclaration> buggyMethods = JavaParserFactory
							.parseInputStreamGetOverlapMethodDeclarationList(fileInputStream, fileName, item2);
					System.out.println("\tlinking:");
					List<String> tagNameList = this.compareTwoSet(tagVersionMethods, buggyMethods);
					if (tagNameList != null && tagNameList.size() != 0) {
						if (tagNameListAll == null) {
							tagNameListAll = tagNameList;
						} else {
							tagNameListAll.retainAll(tagNameList);
						}
					}
				}
				if (tagNameListAll != null && tagNameListAll.size() != 0) {
					System.out.print("Ok: tag name:");
					cnt++;
					for (String tmp : tagNameListAll) {
						System.out.println("\t" + tmp);
					}
					System.out.println("");
				} else {
					System.out.println("No Tag commit matched\n\n");
				}

//			}
		}
		System.out.println("\n\n最后统计mapped的buggy commit 数量：" + cnt + "/" + size);
	}

	public static String getClassNameFromPath(String path) {
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		return fileName.substring(0, fileName.length() - 5);
	}

	public List<String> isMethodFromCommitMatchRelease(Map<String, List<BodyDeclaration>> releasedTagMethod,
			BodyDeclaration m) {
		List<String> result = new ArrayList<String>();
		boolean flag = m instanceof MethodDeclaration;
		if(flag) 
		for (Entry<String, List<BodyDeclaration>> entry : releasedTagMethod.entrySet()) {
			
			List<BodyDeclaration> mList2 = entry.getValue();
			for (BodyDeclaration tagRelease : mList2) {
				if(flag && (tagRelease instanceof MethodDeclaration)){
					MethodDeclaration md = (MethodDeclaration)tagRelease;
					if (md.getDeclarationAsString().equals(((MethodDeclaration)m).getDeclarationAsString())) {
						System.out.println("\t\tMatch Same method signature:" + ((MethodDeclaration)m).getDeclarationAsString());
						if (md.getBody().get().toString().equals(((MethodDeclaration)m).getBody().get().toString())) {
							System.out.println("\t\tMatch Same method body:" + ((MethodDeclaration)m).getDeclarationAsString());
							result.add(entry.getKey());
						}
						break;
					}
				}
				if(!flag && (tagRelease instanceof ConstructorDeclaration)){
					ConstructorDeclaration cd = (ConstructorDeclaration)tagRelease;
					if (cd.getDeclarationAsString().equals(((MethodDeclaration)m).getDeclarationAsString())) {
						System.out.println("\t\tMatch Same method signature:" + ((MethodDeclaration)m).getDeclarationAsString());
						if (cd.getBody().toString().equals(((MethodDeclaration)m).getBody().get().toString())) {
							System.out.println("\t\tMatch Same method body:" + ((MethodDeclaration)m).getDeclarationAsString());
							result.add(entry.getKey());
						}
						break;
					}
				}
				
			}
		}
		return result;
	}

	private List<String> compareTwoSet(Map<String, List<BodyDeclaration>> releasedTagMethod,
			Set<BodyDeclaration> mList) {
		List<String> matchedTagList = null;
		int cnt = 0;
		int size = mList.size();
		for (BodyDeclaration changeCode : mList) {
			List<String> result = this.isMethodFromCommitMatchRelease(releasedTagMethod, changeCode);
			if (result != null && result.size() != 0) {
				cnt++;
				System.out.print("\t\t:match 一个method返回 匹配的tag");
				for (String item : result) {
					System.out.print(item);
				}
				System.out.println("");
			}
			if (result.size() != 0) {
				if (matchedTagList == null) {
					matchedTagList = result;
				} else {
					matchedTagList.retainAll(result);
				}
			}
		}

		if (matchedTagList != null && matchedTagList.size() != 0) {
			System.out.print("\t：Match 一个文件所有 buggy methods 匹配的tag");
			for (String tmp : matchedTagList) {
				System.out.print(tmp);
			}
			System.out.println("\n");
		}
		System.out.println("\t文件buggy method统计：" + cnt + "/" + size + "\n");
		return matchedTagList;
	}

	private Map<String, List<BodyDeclaration>> mappingTagStrToMethodDeclarationList(String oldPath, String className,
			List<RevCommit> candidate) {
		Map<String, List<BodyDeclaration>> result = new HashMap<String, List<BodyDeclaration>>();
		for (int i = 0; i < candidate.size(); i++) {
			InputStream fileInputStream = tagCmd.extractAndReturnInputStream(oldPath, candidate.get(i).getName());
			List<BodyDeclaration> mList = JavaParserFactory.parseFileGetAllMethodDeclaration(fileInputStream);
			result.put(this.commitAndTagMap.get(candidate.get(i)), mList);
		}
		return result;
	}

}