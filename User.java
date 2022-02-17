package bankAccountStorage;

import java.text.NumberFormat;

public class User 
{

	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String balance;
	private String savingsPlan;
	private String savingsBalance;
	
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	
	public User(String firstName, String lastName, String username, String email, String balance, String savingsPlan, String savingsBalance)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.balance = balance;
		this.savingsPlan = savingsPlan;
		this.savingsBalance = savingsBalance;
	}
	
	public void formatDataFields()
	{
		balance = format.format(Double.parseDouble(balance));
		if(savingsPlan == null)
		{
			savingsPlan = "None";
		}
		if(savingsBalance == null)
		{
			savingsBalance = "$0";
		}
		else
		{
			savingsBalance = format.format(Double.parseDouble(savingsBalance));
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
		return "firstName: " + getFirstName() + "\nlastName: " + getLastName() + "\nusername: " + getUsername() + "\nemail: " + getEmail() + "\nbalance: " + getBalance() + "\nsavingsPlan: " + getSavingsPlan() + "\nsavingsBalance: " + getSavingsBalance();
	}
}
