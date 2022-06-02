package edu.ucla.se;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

public class Statements {
	ArrayList <String> statements;
	public Statements(String code) {
		SplitCode(code);
	}
	
	public Statements(Path file_name) throws IOException {
		try {
			byte[] codeBytes = Files.readAllBytes(file_name);
			String code = new String(codeBytes);
			SplitCode(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String GetStatementAt(int pos) {
		return this.statements.get(pos);
	}
	
	public ArrayList<String> GetAllStatements(){
		return this.statements;
	}
	
	private void SplitCode(String code) {
		String code_snippet = code.replaceAll("//.*[\\r\\n]", "");
		code_snippet = code_snippet.replaceAll("\\R+", " ");
		code_snippet = code_snippet.replaceAll("/\\*.*?\\*/", "");
		code_snippet = code_snippet.replaceAll("\\R+", " ");
		code_snippet = code_snippet.replace("{", ";");
		code_snippet = code_snippet.replace("}", ";");
		
		String[] split_code = code_snippet.split(";");
		for(int i = 0; i < split_code.length; i++) {
			split_code[i] = split_code[i].trim();
		}
		this.statements = new ArrayList<String>();
		Collections.addAll(this.statements, split_code);
		while (this.statements.remove("")) {}
		
	}
	
	int GetStatementCnt() {
		return this.statements.size();
	}
}
