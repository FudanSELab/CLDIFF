
public class DrawThreadSynchronized extends Thread
{
	private Account account;

	private double drawAmount;

	public void run()
	{
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
}