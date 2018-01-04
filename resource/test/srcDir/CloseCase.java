import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CloseCase {
	public static void main(String[] args) {
		Set<Integer> numbers = new HashSet<>(Arrays.asList(4, 3, 2, 1));
		List<Integer> sameOrder = numbers.stream()
				.sorted()
		.collect(Collectors.toList());
		sameOrder.stream()
		.forEach(System.out::println);
	}
	public void main(String s){
		File file = new File(s);
		BufferedInputStream inputStream=null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			inputStream.read();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
