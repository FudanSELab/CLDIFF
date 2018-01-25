
public class TestJava {
	
	public static void main(String args[]){
		final String a = null;
		B aa = null;
		new TestJava().a(aa);
		System.out.println(aa);
		
	}
	public void a(B b){
		b = new B(1,2);
	}
	class B{
		int a;
		int b;
		public B(int aa,int bb){
			this.a = aa;
			this.b = bb;
		}
	}
}
