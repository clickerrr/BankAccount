package bankAccountStorage;

import java.text.NumberFormat;
import java.util.ArrayList;

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
			ArrayList<String> data = cm.getSavingData(username);
			
			savingBalance.setText("Current Savings Balance: " + format.format(Double.parseDouble(data.get(0))) );
			savingPlan.setText("Current Savings Plan: " + data.get(1));
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
}
