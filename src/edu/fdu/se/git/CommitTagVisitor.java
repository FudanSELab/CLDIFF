package edu.fdu.se.git;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jgit.lib.Ref;

import edu.fdu.se.fileutil.FileWriter;

public class CommitTagVisitor extends AbstractVisitor {

	public CommitTagVisitor(String outputFile) {
		super(outputFile);
	}

	List<Entry<String, Ref>> mList;
	Map<String, Integer> mMap;
	List<Entry<String, Integer>> simpleMList;

	public void visit(JGitCommand cmd, Set<Entry<String, Ref>> mSet) {
		mList = new ArrayList<Entry<String, Ref>>();
		simpleMList = new ArrayList<Entry<String, Integer>>();
		mMap = new HashMap<String, Integer>();
		for (Entry<String, Ref> item : mSet) {
			mList.add(item);
		}
		mList.sort(new Comparator<Map.Entry<String, Ref>>() {

			@Override
			public int compare(Entry<String, Ref> arg0, Entry<String, Ref> arg1) {
				int time1 = cmd.readCommitTime(arg0.getValue().getObjectId());
				int time2 = cmd.readCommitTime(arg1.getValue().getObjectId());
				return time1 - time2;
			}
		});
		for (Entry<String, Ref> item : mList) {
			String key = item.getKey();
			Ref mRef = item.getValue();
			FileWriter.writeInSegments(this.getFile(), key + "\n", FileWriter.FILE_APPEND_AND_NOT_CLOSE);

			int time = cmd.readCommitTime(mRef.getObjectId());
			FileWriter.writeInSegments(this.getFile(), String.valueOf(time) + "\n",
					FileWriter.FILE_APPEND_AND_NOT_CLOSE);
			mMap.put(key, time);

			// Long l = new Long(time);
			// String dateStr = stampToDate(l*1000);
			// System.out.println(dateStr);
		}
		this.close();
		for (Entry<String, Integer> item : mMap.entrySet()) {
			simpleMList.add(item);
		}
		simpleMList.sort(new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				return arg0.getValue() - arg1.getValue();
			}

		});
	}

	public static String stampToDate(Long s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(s);
		res = simpleDateFormat.format(date);
		return res;
	}

	public void compareCommitTime(int time) {
		int i = 0;
		for (Entry<String, Integer> item : simpleMList) {
			if (item.getValue() > time) {
				break;
			}
			i++;
		}
		System.out.println(simpleMList.get(i));
	}

}
