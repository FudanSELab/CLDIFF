import java.util.List;

/**
 * Allows applications to request the system to perform a network scan.@hide
 * {@link #stop()} for stopping the in-progress scan.
 * @hide
 * @deprecated 
 * @author
 */


public class FullElementTest extends Thread
{
	final private Account account;

	private double drawAmount;

   /** 
	* comment
	* comment block test;
   */
	public void printHelloWorld(String test,int m) {
		String str = "Hello";
		if("Hello".equals(str))
            System.out.println("HelloWorld"+str+test);
		else
            System.out.println("else HelloWorld");
	}
	
	public void run()
	{
		//synchronized���
		//语句测试
		synchronized (account)
		{
			if (account.getBalance() >= drawAmount)
			{
				System.out.println(getName() + 
					"Success to get money :" + drawAmount);
				try
				{
					Thread.sleep(1);			
				}
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
				account.setBalance(account.getBalance() - drawAmount);
				System.out.println("\tBalance: " + account.getBalance());
			}
			else
			{
				System.out.println(getName() + "Fail to get money");
			}
		}
	}

	void printHello(String test,int m) {
		String str = "Hello";
        System.out.println("HelloWorld!"+str + test);
	    switch(m) {
			case 1:
				System.out.println(1);
				break;
			case 2:
				System.out.println(2);
				break;
			case 3:
                System.out.println(3);
			default:
				System.out.println("default");
				break;
	    }
    }

	static final Context getApplicationContextIfAvailable(Context context) {
        return ActivityThread.currentApplication().getApplicationContext();
	}
	
	boolean testEx() throws Exception{
        boolean ret = true;
        try{
            ret = testEx1();
        }
        catch (Exception e){
            System.out.println("testEx, catch exception");
            ret = false;
            throw e;
        }
        finally{
            System.out.println("testEx, finally; return value=" + ret);
            return ret;
        }
    }
}