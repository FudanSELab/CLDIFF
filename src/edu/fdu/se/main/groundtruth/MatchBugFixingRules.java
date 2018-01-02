package edu.fdu.se.main.groundtruth;


public class MatchBugFixingRules {
	
	public static boolean bugOrNot(String msg,String msgFull){
		boolean result = false;
		if(msg.contains("bug")|| msg.contains("fix")||msg.contains("patch")||msg.contains("fixing")){
			result = true;
		}
		if(msgFull.contains("bug")|| msgFull.contains("fix")||msgFull.contains("patch")||msgFull.contains("fixing")){
			result = true;
		}
		return result;
	}
	/**
	 * add anr ,fc
	 * @param msg
	 * @param msgFull
	 * @return
	 */
	public static boolean bugOrNot2(String msg,String msgFull){
		boolean result = false;
		msg = msg.toLowerCase();
		msgFull = msgFull.toLowerCase();
		if(msg.contains("bug")|| msg.contains("fix")||msg.contains("patch")||msg.contains("fixing")||msg.contains("anr")||msg.contains("fc")){		
			result = true;
		}
		if(msgFull.contains("bug")|| msgFull.contains("fix")||msgFull.contains("patch")||msgFull.contains("fixing")||msg.contains("anr")||msg.contains("fc")){
			result = true;
		}
		return result;
	}
	
	
	/**
	 * add anr ,fc,oom,exception
	 * @param msg
	 * @param msgFull
	 * @return
	 */
	public static boolean bugOrNot3(String msg,String msgFull){
		boolean result = false;
		String msgL = msg.toLowerCase();
		String msgFullL = msgFull.toLowerCase();
		String[] bugKeyWordsL ={"bug","fix","patch","fixing","anr","fc","exception","oom"};
		for(String i : bugKeyWordsL){
			if(msgL.contains(i)){
				return true;
			}
			if(msgFullL.contains(i)){
				return true;
			}
		}
		return result;
	}

}
