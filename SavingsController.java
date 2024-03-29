package bankAccountStorage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class SavingsController 
{

	@FXML
	private Label savingBalance;
	@FXML
	private Label savingPlan;
	
	@FXML
	private MenuButton actionSelection;
	@FXML
	private MenuItem transferBalance;
	@FXML
	private MenuItem changeSavings;
	
	@FXML
	private MenuButton savingsPlanSelection;
	@FXML
	private MenuItem miA;
	@FXML
	private MenuItem miB;
	@FXML
	private MenuItem miC;
	
	@FXML
	private TextField amountTransferField;
	
	@FXML
	private Label errorText;
	
	@FXML
	private HBox actionHbox;
	
	
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	private User user;
	private double savingsBalance;
	private String savingsPlan;
	
	private Label superBalance;
	
	@FXML
	public void initialize() throws Exception
	{
		errorText.setText("");
		transferBalance.setOnAction(new MenuItemSelection());
		changeSavings.setOnAction(new MenuItemSelection());
		miA.setOnAction(new MenuItemSelection());
		miB.setOnAction(new MenuItemSelection());
		miC.setOnAction(new MenuItemSelection());
		amountTransferField.setOnKeyPressed(new EnterKeyHandler());
		actionHbox.getChildren().remove(savingsPlanSelection);
		actionHbox.getChildren().remove(amountTransferField);
		actionHbox.getChildren().remove(errorText);
	}
	
	public void initData(User user, Label superBalance)
	{
		this.user = user;
		this.superBalance = superBalance;
		savingsBalance = Double.parseDouble(user.getSavingsBalance());
		savingsPlan = user.getSavingsPlan();
		savingBalance.setText("Current Savings Balance: " + format.format(savingsBalance));
		savingPlan.setText("Current Savings Plan: " + savingsPlan);
		
		calculateInterest();
	}
	
	private class EnterKeyHandler implements EventHandler<KeyEvent>
	{

		@Override
		public void handle(KeyEvent e) 
		{
			if(e.getCode().equals(KeyCode.ENTER))
			{
				double amount;
				ConnectionManager cm;
				try
				{
					amount = Double.parseDouble(amountTransferField.getText());
					
					if(amount <= 0)
					{
						errorText.setText("Please enter a value greather than 0");
						throw new Exception("Negative Number");
					}
					
					amount = Math.floor(amount * 100) / 100;
					
					if(amount > savingsBalance)
					{
						errorText.setText("Insufficient Funds");
						throw new Exception("Insufficient Funds");
					}
					cm = new ConnectionManager();
					
					savingsBalance -= amount;
					cm.depositAmount(amount, user.getUsername());
					user = cm.getUser(user.getUsername());
					
					cm.withdrawFromSavings(user.getUsername(), amount);
					user = cm.getUser(user.getUsername());
					
					savingBalance.setText("Current Savings Balance: " + format.format(savingsBalance));
					superBalance.setText("Balance: " + format.format(Double.parseDouble(user.getBalance())));
					
					cm.close();

					actionHbox.getChildren().clear();
					actionHbox.getChildren().addAll(actionSelection);
					errorText.setText("");
					
				}
				catch(SQLException ex)
				{
					ex.printStackTrace();
				}
				catch(NumberFormatException ex)
				{
					errorText.setText("Please enter a number");
					ex.printStackTrace();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}

			}

		}
		
	}
	
	private class MenuItemSelection implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent e) 
		{
			if(e.getSource() == transferBalance)
			{
				actionHbox.getChildren().clear();
				actionHbox.getChildren().addAll(actionSelection, amountTransferField, errorText);
				errorText.setText("");
			}
			else if(e.getSource() == changeSavings)
			{
				actionHbox.getChildren().clear();
				actionHbox.getChildren().addAll(actionSelection, savingsPlanSelection, errorText);
				errorText.setText("");
			}
			else if(e.getSource() == miA)
			{
				if(!savingsPlan.equals("A"))
				{
					ConnectionManager cm = null;
					try
					{
						cm = new ConnectionManager();
						cm.changeSavingsPlan(user.getUsername(), "A");
						user = cm.getUser(user.getUsername());
						cm.close();
						savingsPlan = "A";
						savingPlan.setText("Current Savings Plan: " + savingsPlan);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					actionHbox.getChildren().clear();
					actionHbox.getChildren().addAll(actionSelection);
				}
				else
				{
					errorText.setText("Can Not Change to Current Savings Plan");
				}
			}
			else if(e.getSource() == miB)
			{
				if(!savingsPlan.equals("B"))
				{
					ConnectionManager cm = null;
					try
					{
						cm = new ConnectionManager();
						cm.changeSavingsPlan(user.getUsername(), "B");
						user = cm.getUser(user.getUsername());
						cm.close();
						savingsPlan = "B";
						savingPlan.setText("Current Savings Plan: " + savingsPlan);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					actionHbox.getChildren().clear();
					actionHbox.getChildren().addAll(actionSelection);
					
				}
				else
				{
					errorText.setText("Can Not Change to Current Savings Plan");
				}
			}
			else if(e.getSource() == miC)
			{
				if(!savingsPlan.equals("C"))
				{
					ConnectionManager cm = null;
					try
					{
						cm = new ConnectionManager();
						cm.changeSavingsPlan(user.getUsername(), "C");
						user = cm.getUser(user.getUsername());
						cm.close();
						savingsPlan = "C";
						savingPlan.setText("Current Savings Plan: " + savingsPlan);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					
					actionHbox.getChildren().clear();
					actionHbox.getChildren().addAll(actionSelection);
				}
				else
				{
					errorText.setText("Can Not Change to Current Savings Plan");
				}
			}
	
		}
		
		
	}
	
	public void calculateInterest()
	{
		Timestamp data = null;
		try
		{
			ConnectionManager cm = new ConnectionManager();
			
			data = cm.getSavingsCreationTime(user.getUsername());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(data != null)
		{
			// Pe^rt
			// INTEREST RATE: 0.01%
			double rate = .01;
			double principal = savingsBalance;
			

			String temp = data.toString();
			String[] splitTime = temp.split(" ");
			temp = splitTime[1];
			String[] date = splitTime[0].split("-");
			String[] time = splitTime[1].split(":");
			
			time[2] = (int)Double.parseDouble(time[2]) + "";
			
			LocalDateTime lastTimeAccessed = new LocalDateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
			LocalDateTime timeNow = new LocalDateTime();

			int[] timeDifference = 
				{
						Math.abs(Years.yearsBetween(lastTimeAccessed, timeNow).getYears()),
						Math.abs(Months.monthsBetween(lastTimeAccessed, timeNow).getMonths()),
						Math.abs(Days.daysBetween(lastTimeAccessed, timeNow).getDays()),
						Math.abs(Hours.hoursBetween(lastTimeAccessed, timeNow).getHours()),
						Math.abs(Minutes.minutesBetween(lastTimeAccessed, timeNow).getMinutes()),
						Math.abs(Seconds.secondsBetween(lastTimeAccessed, timeNow).getSeconds())
				};

			/*
			 * time difference indexes:
			 * 0: Years
			 * 1: Months
			 * 2: Days
			 * 3: Hours
			 * 4: Minutes
			 * 5: Seconds
			 */

			double newBalance = 0;
			if(user.getSavingsPlan().equals("A"))
			{
				newBalance = principal * Math.pow(Math.E, rate * timeDifference[3]);
			}
			else if(user.getSavingsPlan().equals("B"))
			{
				newBalance = principal * Math.pow(Math.E, rate * timeDifference[2]);
			}
			else if(user.getSavingsPlan().equals("C")) 
			{
				newBalance = principal * Math.pow(Math.E, rate * timeDifference[4]);
			}
			newBalance =  Math.floor(newBalance * 100) / 100;
			// now we have to insert and update the new balance, and then update the time accessed

			if(newBalance != principal)
			{
				try
				{
					ConnectionManager cm = new ConnectionManager();	
					cm.updateSavingsBalance(user.getUsername(), newBalance);
					cm.updateSavingsTimeIndexed(user.getUsername());
					user = cm.getUser(user.getUsername());
					savingBalance.setText("Current Savings Balance: " + format.format(newBalance));
					
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}	
			}
			
		}
			
	}
	
	
}
