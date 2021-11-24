package bankAccountStorage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SavingsController 
{

	@FXML
	private Label savingBalance;
	@FXML
	private Label savingPlan;
	
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	private String username;
	private double savingsBalance;
	
	@FXML
	public void initialize() throws Exception
	{
		
	}
	
	public void initData(String username)
	{
		this.username = username;
		try
		{
			ConnectionManager cm = new ConnectionManager();
			ArrayList<String> data = cm.getSavingData(this.username);
			savingsBalance = Double.parseDouble(data.get(0));
			savingBalance.setText("Current Savings Balance: " + format.format(Double.parseDouble(data.get(0))) );
			savingPlan.setText("Current Savings Plan: " + data.get(1));
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		calculateInterest();
		
	}
	
	public void calculateInterest()
	{
		Timestamp data = null;
		try
		{
			ConnectionManager cm = new ConnectionManager();
			
			data = cm.getSavingsCreationTime(this.username);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(data != null)
		{
			// compounded at the end of every day fuck it
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
			
			System.out.println(Arrays.toString(date));
			System.out.println(Arrays.toString(time));
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
			
			double newBalance = principal * Math.pow(Math.E, rate * timeDifference[2]);
			newBalance =  Math.floor(newBalance * 100) / 100;
			// now we have to insert and update the new balance, and then update the time accessed

			System.out.println("TD[3] " + timeDifference[3]);
			System.out.println("New balance: " + newBalance);
			
			try
			{
				ConnectionManager cm = new ConnectionManager();	
				cm.updateSavingsBalance(this.username, newBalance);
				cm.updateSavingsTimeIndexed(this.username);
				savingBalance.setText("Current Savings Balance: " + format.format(newBalance));
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			
			
			
			
			

		}
		
		
		
	}
	
	
}
