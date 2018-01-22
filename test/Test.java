/**
 * Created by huangkaifeng on 2018/1/18.
 */
public class Test {
	
	class B{
		public B(String aa,String bb){
			a = aa;
			b = bb;
		}
		String a;
		String b;
	}
    public static void main(String args[]){
    	new Test().run();
    }
    public void run(){
    	B b1 = new B("aa","bb");
    	B b2 = new B("aa","bb");
        System.out.println(b1.hashCode());
        System.out.println(b2.hashCode());
        System.out.print(b1.equals(b2));
    }
}
