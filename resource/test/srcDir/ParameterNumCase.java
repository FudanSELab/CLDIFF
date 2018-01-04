import java.util.ArrayList;

public class ParameterNumCase {

	String b = "bbbb";
	public void test()
	{
		ArrayList<Integer> list = new ArrayList();
		for(int i=0;i<10;i++)
			list.add(i);
		int temp=9;
		int index=10;
		list.add(temp);
	}
	
}
