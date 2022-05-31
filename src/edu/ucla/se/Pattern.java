package edu.ucla.se;

public class Pattern {
	String str;
	double pos;
	public Pattern() {}
	public Pattern(String s, double p) {
		str = s;
		pos = p;
	}
	public String GetString() {
		return str;
	}
	public double GetPosition() {
		return pos;
	}
}
