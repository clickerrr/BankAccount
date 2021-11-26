package bankAccountStorage;

import java.text.NumberFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MyAccountDetailsController
{

	@FXML
	private Label lblBalance;
	@FXML
	private Label lblSavings;
	@FXML
	private Label lblName;
	@FXML
	private Label lblUsername;
	@FXML
	private Label lblEmail;
	@FXML
	private Button buttonChangePassword;

	private User user;
	
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	@FXML
	private void initialize() throws Exception
	{
		buttonChangePassword.setOnAction(new ChangePasswordButtonHandler());
		
	}
	
	public void initData(User user)
	{
		this.user = user;
	
		System.out.println(user.toString());
		
		if(user.getSavingsPlan() == null)
		{
			lblBalance.setText(this.format.format( Double.parseDouble(user.getBalance()) ));
			lblSavings.setText("$0");
		}
		else
		{
			lblBalance.setText(this.format.format( Double.parseDouble(user.getBalance()) + Double.parseDouble(user.getSavingsBalance()) ));
			lblSavings.setText(this.format.format( Double.parseDouble(user.getSavingsBalance()) ));
		}
		lblName.setText(user.getFirstName() + " " +user.getLastName());
		lblUsername.setText(user.getUsername());
		lblEmail.setText(user.getEmail());
		
	}
	
	private class ChangePasswordButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{
			try 
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/ChangePassword.fxml"));
				Stage accountDetails = loader.load();
				ChangePasswordController changePassController = loader.getController();
				changePassController.initData(user);
				accountDetails.show();		
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	
		}
		
	}
	
}
