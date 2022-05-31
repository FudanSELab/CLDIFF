package edu.ucla.se;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatementsWithLineNumber extends Statements {
	ArrayList<Integer> start_line = new ArrayList<>();
	ArrayList<Integer> end_line = new ArrayList<>();
	public StatementsWithLineNumber(Path file_name) throws IOException {
		super(file_name);
		try (Stream<String> stream_lines = Files.lines(file_name)){
			ArrayList<String> lines = stream_lines.collect(Collectors
					.toCollection(ArrayList::new));
			int line_cnt = lines.size();
			
			int cur_line = 0;
			for(int i = 0; i < this.statements.size(); i++) {
				int j = 0;
				String snippet = "";
				boolean flag = false;
				String statement = this.statements.get(i);
				while (cur_line + j < line_cnt) {
					snippet = snippet + "\n" + lines.get(cur_line+j);
					//System.out.println("snippet");
					//System.out.println(snippet);
					Statements s = new Statements(snippet);
					int s_size = s.GetStatementCnt();
					if (s_size==0) {
						j++;
						continue;
					}
					
					//System.out.println("statements");
					for(int k = 0; k < s_size; k++) {
						//System.out.println(k);
						//System.out.println(s.GetStatementAt(k));
						//System.out.println(statement);
						if (statement.equals(s.GetStatementAt(k))) {
							
							start_line.add(cur_line+1);
							end_line.add(cur_line+j+1);
						
							flag = true;
							if (k == s_size-1) {
								cur_line += (j+1);
							}
							else {
								cur_line += j;
							}
							break;
						}
					}
					if (flag == true) {
						break;
					}
					j++;
				}
				if (flag == false) {
				
					System.out.println(i);
					System.out.println(statement);
					System.out.println(cur_line);
					System.out.println("Something is wrong when spliting files into statements with line numbers");
					System.out.print(snippet);
					Statements s = new Statements(snippet);
					int s_size = s.GetStatementCnt();
					System.out.println(s_size);
					for(int k = 0; k < s_size; k++) {
						System.out.println(statement.equals(s.GetStatementAt(k)));
						System.out.println(s.GetStatementAt(k));
					}
					System.exit(-1);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int GetStartLineFor(int pos) {
		return start_line.get(pos);
	}
	
	public int GetEndLineFor(int pos) {
		return end_line.get(pos);
	}
}
