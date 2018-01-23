/**
 * Created by huangkaifeng on 2018/1/18.
 */
public class Test {
	

    public static void main(String args[]){
    	new Test().run();
    }
    public void run(){
    	Class a = Boolean.class;
		Class b = String.class;
		String aa ="String";
		String bb = "Boolean";
//		System.out.println(a.equals(b));
		long  t1 = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			boolean c= a.equals(b);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
    }
}
