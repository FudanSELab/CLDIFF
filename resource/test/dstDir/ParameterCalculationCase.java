import java.util.ArrayList;

public class ParameterCalculationCase {
	public void test()
	{
		ArrayList<Integer> list = new ArrayList();
		for(int i=0;i<10;i++)
			list.add(i);
		int a=1;
		int b=2;
		int c=3;
		list.add(a-b+c);	
	}
}
