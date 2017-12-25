package edu.fdu.se.main.androidrepo.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

/*
Copyright 2013, 2014 Dominik Stadler
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ThisExpression;
//import org.dstadler.jgit.helper.CookbookHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.RepositoryHelper;

/**
 * Simple snippet which shows how to list all Tags
 *
 * @author dominik.stadler at gmx.at
 */
public class ListTags {

	public static void main(String[] args) throws IOException, GitAPIException {

	}
	public static void filterTags(RevWalk revWalk) throws Exception {
		FileInputStream fis = new FileInputStream(
				new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/Android_Repo_AllTags.txt"));
		InputStreamReader ib = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(ib);
		String name = null;
		List<String> tagId = new ArrayList<String>();
		while ((name = br.readLine()) != null) {
			tagId.add(name);
			tagId.add(br.readLine());
			br.readLine();
		}
		List<String> v1List = new ArrayList<String>();
		for(int i=0;i<tagId.size()-1;i++){
			name = tagId.get(i);
			String commitId = tagId.get(i+1);
			if(name.endsWith("r1")){
				v1List.add(name);
				v1List.add(commitId);
			}
		}
		StringBuffer sb =new StringBuffer();
		for(String tmp:v1List){
			sb.append(tmp);
			sb.append("\n");
		}
		FileWriter.writeInSegments(new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/android_sdk_v1_tag.txt"),sb.toString(),FileWriter.FILE_APPEND_AND_CLOSE);
		

	}

	public static void tagType(RevWalk revWalk) throws Exception {
		FileInputStream fis = new FileInputStream(
				new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/Android_Repo_AllTags.txt"));
		InputStreamReader ib = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(ib);
		String name = null;
		List<String> tagId = new ArrayList<String>();
		while ((name = br.readLine()) != null) {
			tagId.add(br.readLine());
			br.readLine();
		}
		System.out.println(tagId.size());
		for (String tmp : tagId) {
			ObjectId commitId = ObjectId.fromString(tmp);
			RevObject object = revWalk.parseAny(commitId);
			if (object instanceof RevTag) {
				// annotated
				System.out.println("aaabb");
			} else if (object instanceof RevCommit) {
				// lightweight
				System.out.println("lightweight");
			} else {
				// invalid
				System.out.println("invalid");
			}
		}

	}
}