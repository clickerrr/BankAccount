package bankAccountStorage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

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
	
	private String username;
	
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	
	@FXML
	private void initialize() throws Exception
	{
		buttonChangePassword.setOnAction(new ChangePasswordButtonHandler());
		
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
				changePassController.initData(username);
				accountDetails.show();		
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	
		}
		
	}
	
	public void initData(String username)
	{
	
		this.username = username;
		
		ArrayList<String> userDetails;
		ConnectionManager cm;
		
		try
		{
			cm = new ConnectionManager();
			
			userDetails = cm.getUserDetails(this.username);

			for(int i = 0; i< userDetails.size(); i++)
			{
				System.out.println(userDetails.get(i));
			}
			
			if(userDetails.get(1) == null)
			{
				lblBalance.setText(this.format.format( Double.parseDouble(userDetails.get(0)) ));
				lblSavings.setText("$0");
			}
			else
			{
				lblBalance.setText(this.format.format( Double.parseDouble(userDetails.get(0)) + Double.parseDouble(userDetails.get(1)) ));
				lblSavings.setText("$0");
			}
			lblName.setText(userDetails.get(2));
			lblUsername.setText(userDetails.get(3));
			lblEmail.setText(userDetails.get(4));
			
			cm.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
}
