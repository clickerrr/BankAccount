package bankAccountStorage;

/*
 * Name: AccountScreenController.java
 * Author: Bartosz Swiech
 * Version: 1.0
 * Description: This class is an FXML controller for the main account screen
 */

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountScreenController 
{
	// FXML References for the controller
	@FXML
	private Stage primaryStage;
	@FXML
	private Button logoutButton;
	@FXML
	private Button myAccountButton;
	@FXML
	private Button depositSubmit;
	@FXML
	private Button withdrawSubmit;
	@FXML
	private Button transferSubmit;
	@FXML
	private Label lblBalance;
	@FXML
	private TextField depositAmount;
	@FXML
	private TextField withdrawAmount;
	@FXML
	private TextField transferAmount;
	@FXML
	private Label depositError;
	@FXML
	private Label withdrawError;
	@FXML
	private Label transferError;
	@FXML
	private Label lblNameDisplay;
	@FXML
	private BorderPane savingsPane;
	@FXML
	private TabPane accountTabPane;
	
	// username String for the database references
	private String username;
	
	// connection manager for any database functions
	private ConnectionManager cm;
	// balance of the user
	private double balance;
	
	// format for currency
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	// initialize all FXML buttons and elements that change
	@FXML
	private void initialize() throws Exception
	{
		logoutButton.setOnAction(new LogoutButtonHandler());
		depositSubmit.setOnAction(new DepositButtonHandler());
		withdrawSubmit.setOnAction(new WithdrawButtonHandler());
		myAccountButton.setOnAction(new MyAccountDetailsButtonHandler());
		transferSubmit.setOnAction(new TransferSubmitButtonHandler());
		transferError.setText("");
		depositError.setText("");
		withdrawError.setText("");
	}

	// init data for when this panel is called
	/**
	 * @param username Takes the username that will be used for the databse calls and comparisons
	 */
	public void initData(String username)
	{
		// sets the username instance variable for this class to the parameter
		this.username = username;
		// connection manager
		try {
			// initializes a new connection
			cm = new ConnectionManager();
			// sets the label balance
			this.balance = cm.getUserBalance(this.username);
			lblBalance.setText("Balance: " + format.format(this.balance));
			
			// gets the user's name and displays a welcome message
			lblNameDisplay.setText("Welcome, " + cm.getUsersName(this.username));
			// close the connection to the MYSQL database
			cm.close();
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		
		// manages the savings account tab
		try
		{
			// another connection
			ConnectionManager cm = new ConnectionManager();
			
			// if the user has a savings account
			if(cm.hasSavings(username))
			{
				// loads another fxml class which consists of a simple panel containing the savings account
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/Savings.fxml"));
				VBox savingsData = loader.load();
				SavingsController savingsController = loader.getController();
				savingsController.initData(username);
				
				savingsPane.setCenter(savingsData);
		
			}
			else
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/CreateSavingsButton.fxml"));
				VBox createSavingsPane = loader.load();
				CreateSavingsButtonController csbController = loader.getController();
				csbController.initData(username, savingsPane);
		
				savingsPane.setCenter(createSavingsPane);
			}
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
	}
	
	private class TransferSubmitButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{

			double newBalance;
			ConnectionManager cm;
			try
			{
				cm = new ConnectionManager();
				if(!cm.hasSavings(username))
				{
					transferError.setText("No account to transfer to");
					throw new Exception();
				}
				
				newBalance = Double.parseDouble(transferAmount.getText());
				
				if(newBalance <= 0)
				{
					transferError.setText("Please enter a value greather than 0");
					throw new Exception();
				}

				
				newBalance = Math.floor(newBalance * 100) / 100;

				if(newBalance <= cm.getUserBalance(username))
				{
					cm.transferAmount(username, newBalance);
					
					lblBalance.setText("Balance: " + format.format(cm.getUserBalance(username)));	

					transferError.setText("");
					
				}
				else
				{
					transferError.setText("Insufficient Funds");
				}
				
				
				cm.close();
				
				savingsPane.getChildren().clear();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/Savings.fxml"));
				VBox savingsData = loader.load();
				SavingsController savingsController = loader.getController();
				savingsController.initData(username);
		
				savingsPane.setCenter(savingsData);
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(NumberFormatException e)
			{
				transferError.setText("Please enter a number");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	private class MyAccountDetailsButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
			try
			{
			
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/MyAccountDetails.fxml"));
				Stage accountDetails = loader.load();
				MyAccountDetailsController detailsController = loader.getController();
				detailsController.initData(username);
				accountDetails.show();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class LogoutButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{

			primaryStage.close();
			try {
				Stage login = FXMLLoader.load(getClass().getResource("/bankAccountStorage/LoginScreen.fxml"));
				login.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		
	}
	
	private class DepositButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
			
			double newBalance;
			ConnectionManager cm;
			try
			{
				cm = new ConnectionManager();
				
				
				newBalance = Double.parseDouble(depositAmount.getText());
				
				if(newBalance <= 0)
				{
					depositError.setText("Please enter a value greather than 0");
					throw new Exception();
				}
				
				newBalance = Math.floor(newBalance * 100) / 100;
				
				cm.depositAmount(newBalance, username);
				
				lblBalance.setText("Balance: " + format.format(cm.getUserBalance(username)));


				depositError.setText("");
				
				cm.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(NumberFormatException e)
			{
				depositError.setText("Please enter a number");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	private class WithdrawButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{
			double newBalance;
			ConnectionManager cm;
			try
			{
				cm = new ConnectionManager();
				
				
				newBalance = Double.parseDouble(withdrawAmount.getText());
				
				if(newBalance <= 0)
				{
					withdrawError.setText("Please enter a value greather than 0");
					throw new Exception();
				}
				
				if(newBalance > cm.getUserBalance(username))
				{
					withdrawError.setText("Insufficient Funds");
					throw new Exception("Insufficient Funds");
				}
				
				newBalance = Math.floor(newBalance * 100) / 100;
				
				cm.withdrawAmount(newBalance, username);
				
				lblBalance.setText("Balance: " + format.format(cm.getUserBalance(username)));

				withdrawError.setText("");
				cm.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(NumberFormatException e)
			{
				withdrawError.setText("Please enter a number");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

	
	
}
