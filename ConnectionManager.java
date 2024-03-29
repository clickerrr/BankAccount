package bankAccountStorage;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ConnectionManager
{
	// salt for the password
	private byte[] passSalt;
	// MySQL connection
	private Connection connect;
	
	// constructor that initializes the connection and creates it
	public ConnectionManager() throws SQLException
	{
		// connection established to local mySQL server on port 3306 with username: root and password: password
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
		System.out.println("Successfully Connected");	
	}
	
	public boolean loginUser(String username, String password) throws SQLException, NoSuchAlgorithmException
	{
		String query = "select password, salt from accounts.account_information where username=?";
		
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
	
		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		boolean areEqual = false;
		
		while(rs.next())
		{
			byte[] pass = rs.getBytes("password");
			byte[] salt = rs.getBytes("salt");
			
			byte[] hashedPass = EncryptionManager.generateHash(password, "SHA-256", salt);
			
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
			
		}
	
		return areEqual;
	}

	// GOOD AND CHECKED WORKS
	public User getUser(String username) throws SQLException
	{
		String query = "select * from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
	
		preparedStatement.setString(1, username);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		User user = null;
		while(rs.next())
		{
			user = new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("email"), rs.getString("balance"), rs.getString("savingPlan"), rs.getString("savingBalance"));
		}	
		return user;
	}

	public void createAccount(String firstName, String lastName, String username, String email, String password) throws SQLException, NoSuchAlgorithmException
	{
		passSalt = EncryptionManager.createSalt();
		String query = "select * from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
	
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			throw new SQLException("Username Already Exists");
		}
		
		query = "select * from accounts.account_information where email=?";
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.setString(1, email);
		
		rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			throw new SQLException("Email Already Exists");
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

	public void changePassword(String username, String password) throws SQLException, NoSuchAlgorithmException
	{
		passSalt = EncryptionManager.createSalt();
		
		String query = "update accounts.account_information set password=?, salt=? where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setBytes(1, EncryptionManager.generateHash(password, "SHA-256", passSalt));
		preparedStatement.setBytes(2, passSalt);
		preparedStatement.setString(3, username);
		
		preparedStatement.execute();
	}

	public void changeSavingsPlan(String username, String newSavingsPlan) throws SQLException
	{
		
		String query = "update accounts.account_information set savingPlan=? where username=?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		

		preparedStatement.setString(1, newSavingsPlan);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
		
	}
	
	public void updateSavingsBalance(String username, double amount) throws SQLException
	{
		String query = "select savingbalance from accounts.account_information where username=?";
		
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
	
	
	
	
	
	public void updateSavingsTimeIndexed(String username) throws SQLException
	{
		String query = "update accounts.account_information set timeCreated=? where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		long now = System.currentTimeMillis();
		Timestamp ts = new Timestamp(now);
		preparedStatement.setTimestamp(1, ts);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
	}
	
	public void createSavings(String username, String savingPlan, double amount) throws SQLException
	{

		if(amount > 0)
		{
			withdrawAmount(amount, username);
		}
		
		String query = "update accounts.account_information set savings=1, savingPlan=?, savingBalance=?, timeCreated=? where username=?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		long now = System.currentTimeMillis();
		Timestamp ts = new Timestamp(now);
		
		preparedStatement.setString(1, savingPlan);
		preparedStatement.setDouble(2, amount);
		preparedStatement.setTimestamp(3, ts);
		preparedStatement.setString(4, username);
		
		preparedStatement.execute();
		
	}
	
	public Timestamp getSavingsCreationTime(String username) throws SQLException
	{
		Timestamp ts = null;
		String query = "select timeCreated from accounts.account_information where username=?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			ts = rs.getTimestamp("timeCreated");
		}
		
		return ts;
		
	}
	
	
	public void depositAmount(double amount, String username) throws SQLException
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
		
		newBalance += amount;
		
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
	
	public void withdrawFromSavings(String username, double amount) throws SQLException
	{
		
		String query = "select savingBalance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double newBalance = 0;
		
		while(rs.next())
		{
			newBalance = rs.getDouble("savingBalance");
		}
		
		newBalance -= amount;
		
		query = "update accounts.account_information set savingBalance=? where username=?";
		
		preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
		
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
	
	public void adminUpdateBalance(String username, double newBalance) throws SQLException
	{
		String query = "select balance from accounts.account_information where username=?";
				
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double oldBalance = 0;
		
		while(rs.next())
		{
			oldBalance = rs.getDouble("balance");
		}
		
		newBalance += oldBalance;
		
		
		query = "update accounts.account_information set balance=? where username=?";
		
		preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();

	}
	
	public void adminUpdateSavingsBalance(String username, double newBalance) throws SQLException
	{
		String query = "select savingBalance from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		ResultSet rs = preparedStatement.executeQuery();
		
		double oldBalance = 0;
		
		while(rs.next())
		{
			oldBalance = rs.getDouble("savingBalance");
		}
		
		newBalance += oldBalance;
		
		
		query = "update accounts.account_information set savingBalance=? where username=?";
		
		preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setDouble(1, newBalance);
		preparedStatement.setString(2, username);
		
		preparedStatement.execute();
	}
	
	public ArrayList<User> adminGetAllUserData() throws SQLException
	{
		ArrayList<User> returnData = new ArrayList<User>();
		
		String query = "select * from accounts.account_information order by email";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next())
		{
			returnData.add(new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("email"), rs.getString("balance"), rs.getString("savingPlan"), rs.getString("savingBalance")));
		}
		
		return returnData;
		
		
		
	}
	
	public void adminDeleteUser(String username) throws SQLException
	{
		String query = "delete from accounts.account_information where username=?";
		
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		
		preparedStatement.setString(1, username);
		
		preparedStatement.execute();
		
	}
	
	
	public void close() throws SQLException
	{
		connect.close();
	}
	
}
