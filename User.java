package bankAccountStorage;

public class User 
{

	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String balance;
	private String savingsPlan;
	private String savingsBalance;
	
	public User(String firstName, String lastName, String username, String email, String balance, String savingsPlan, String savingsBalance)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.balance = "$" + balance;
		if(savingsPlan == null)
		{
			this.savingsPlan = "None";
		}
		else
		{
			this.savingsPlan = savingsPlan;
		}
		if(savingsBalance == null)
		{
			this.savingsBalance = "$0";
		}
		else
		{
			this.savingsBalance = "$" + savingsBalance;
		}
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	public String getLastName()
	{
		return this.lastName;
	}
	public String getUsername()
	{
		return this.username;
	}
	public String getEmail()
	{
		return this.email;
	}
	public String getBalance()
	{
		return this.balance;
	}
	public String getSavingsPlan()
	{
		return this.savingsPlan;
	}
	public String getSavingsBalance()
	{
		return this.savingsBalance;
	}
	public String toString()
	{
		return "firstName: " + getFirstName() + "\nlastName: " + getLastName() + "\nemail: " + getEmail() + "\nbalance: " + getBalance() + "\nsavingsPlan: " + getSavingsPlan() + "\nsavingsBalance: " + getSavingsBalance();
	}
}
