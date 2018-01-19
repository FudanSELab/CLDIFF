/**
 * Created by huangkaifeng on 2018/1/18.
 */
public class Test {
    public static void main(String args[]){
        String a = new String("aaa");
        String b = "aaa";
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.print(a.equals(b));
    }
}
