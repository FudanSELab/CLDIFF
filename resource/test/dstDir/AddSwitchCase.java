public class AddSwitchCase {
	final double a = 5;
    void printHelloWorld(String test,int m) {
		String str = "Hello";
        System.out.println("HelloWorld!"+str + test);
	    switch(m) {
			case 1:
				System.out.println("1");
				break;
			case 2:
				System.out.println("2");
				break;
			case 3:
                System.out.println("3");
			default:
				System.out.println("default");
				break;
	    }
    }

}
