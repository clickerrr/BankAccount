package bankAccountStorage;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionManager
{

	private byte[] passSalt;
	private Connection connect;
	
	public ConnectionManager() throws SQLException
	{
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
		System.out.println("Successfully Connected");
		
	}
	
	public void transferAmount(String username, double amount) throws SQLException
	{
		withdrawAmount(amount, username);
		
		String query = "select savingBalance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double newBalance = 0;
		
		while(rs.next())
		{
			newBalance = rs.getDouble("savingBalance");
		}
		
		newBalance += amount;
		
		
		query = "update accounts.account_information set savingBalance=? where username=?";
		preparedStatement = connect.prepareStatement(query);
		

		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
		
	}
	
	public ArrayList<String> getSavingData(String username) throws SQLException
	{
		ArrayList<String> returnList = new ArrayList<String>();
		
		String query = "select savingPlan, savingBalance from accounts.account_information where username=?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			returnList.add(rs.getString("savingBalance"));
			returnList.add(rs.getString("savingPlan"));
		}
		
		return returnList;
		
	}
	
	public void createSavings(String username, String savingPlan, double amount) throws SQLException
	{

		if(amount > 0)
		{
			withdrawAmount(amount, username);
		}
		
		String query = "update accounts.account_information set savings=1, savingPlan=?, savingBalance=? where username=?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, savingPlan);
		preparedStatement.setDouble(2, amount);
		preparedStatement.setString(3, username);
		
		preparedStatement.execute();
		
	}
	
	
	public boolean hasSavings(String username) throws SQLException
	{
		String query = "select savings from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			int value = rs.getInt("savings");
			if(value == 0)
			{
				return false;
			}
			else if(value == 1)
			{
				return true;
			}
		}
		return false;
		
	}
	
	
	
	public void createNewPassword(String username, String password) throws SQLException, NoSuchAlgorithmException
	{
		passSalt = EncryptionManager.createSalt();
		
		String query = "update accounts.account_information set password=?, salt=? where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setBytes(1, EncryptionManager.generateHash(password, "SHA-256", passSalt));
		preparedStatement.setBytes(2, passSalt);
		preparedStatement.setString(3, username);
		
		preparedStatement.execute();
	}
	
	public void createAccount(String firstName, String lastName, String username, String email, String password) throws SQLException, NoSuchAlgorithmException
	{
		passSalt = EncryptionManager.createSalt();
		String query = "select * from accounts.account_information where email=? or username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, username);

		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			throw new SQLException("Account Already Exists");
		}
		
		query = "insert into accounts.account_information (firstName, lastName, username, email, password, salt, balance) values (?, ?, ?, ?, ?, ?, 0)";
		
		preparedStatement = connect.prepareStatement(query);
			
		preparedStatement.setString(1, firstName);
		preparedStatement.setString(2, lastName);
		preparedStatement.setString(3, username);
		preparedStatement.setString(4, email);
		preparedStatement.setBytes(5, EncryptionManager.generateHash(password, "SHA-256", passSalt));
		preparedStatement.setBytes(6, passSalt);
		
		preparedStatement.execute();
		
		
	}
	
	public void depositAmount(double balance, String username) throws SQLException
	{
		String query = "select balance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double newBalance = 0;
		
		while(rs.next())
		{
			newBalance = rs.getDouble("balance");
		}
		
		newBalance += balance;
		
		query = "update accounts.account_information set balance=? where username=?";
		
		preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
		
	}
	
	public void withdrawAmount(double amount, String username) throws SQLException
	{
		String query = "select balance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double newBalance = 0;
		
		while(rs.next())
		{
			newBalance = rs.getDouble("balance");
		}
		
		newBalance -= amount;
		
		query = "update accounts.account_information set balance=? where username=?";
		
		preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
		
	}
	
	public String getUsersName(String username) throws SQLException
	{
		String returnString = "";
		
		String query = "select firstName, lastName from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);

		preparedStatement.setString(1, username);

		ResultSet rs = preparedStatement.executeQuery();
		
		
		while(rs.next())
		{
			returnString += rs.getString("firstName");
			returnString += (" " + rs.getString("lastName"));
		}
		
		return returnString;
		
	}
	
	public String getUser(String username, String password) throws SQLException, NoSuchAlgorithmException
	{
		String query = "select password, salt, username from accounts.account_information where username=?";
		
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);

		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			byte[] pass = rs.getBytes("password");
			byte[] salt = rs.getBytes("salt");
			
			byte[] hashedPass = EncryptionManager.generateHash(password, "SHA-256", salt);

			boolean areEqual = true;
			
			for(int i = 0 ; i < pass.length; i++)
			{
				if(pass[i] == hashedPass[i])
				{
					areEqual = true;
				}
				else
				{
					areEqual = false;
				}
			}
			
			if(areEqual)
			{
				return rs.getString("username");
			}
		}
		
		return null;
	}
	
	public ArrayList<String> getUserDetails(String username) throws SQLException, NoSuchAlgorithmException
	{
		ArrayList<String> returnList = new ArrayList<String>();
		
		String query = "select * from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);

		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			returnList.add(rs.getString("balance"));
			returnList.add(rs.getString("firstName") + " " + rs.getString("lastName"));
			returnList.add(rs.getString("username"));
			returnList.add(rs.getString("email"));
		}
		
		return returnList;
	}
	
	
	
	
	public double getUserBalance(String username) throws SQLException
	{
		double returnValue = 0;
		
		String query = "select balance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();

		while(rs.next())
		{
			returnValue = Double.parseDouble(rs.getString("balance"));
		}

		return returnValue;
	}
	
	public void close() throws SQLException
	{
		connect.close();
	}

	
	
}