package edu.fdu.se.handlefile;

public class Method {

	private int begin;
	private int end;
	private String signature;
	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String content;

	public Method(int begin, int end, String signature, String content) {
		this.begin = begin;
		this.end = end;
		this.signature = signature.trim();
		this.content = content;
	}
}
