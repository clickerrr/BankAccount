package bankAccountStorage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ChangePasswordController 
{

	@FXML
	private TextField newPassInput;
	@FXML
	private TextField rPassInput;
	@FXML
	private Button buttonConfirm;
	@FXML
	private Label errorText;
	
	private String username;
	
	@FXML
	private void initialize() throws Exception
	{
		errorText.setText("");
		buttonConfirm.setOnAction(new ChangePasswordButtonHandler());
		
	}

	private class ChangePasswordButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
			ConnectionManager cm;
			try
			{
				
				if(newPassInput.getText().equals(rPassInput.getText()))
				{
					
					cm = new ConnectionManager();
					
					cm.createNewPassword(username, newPassInput.getText());
					cm.close();
					errorText.setTextFill(Color.GREEN);
					errorText.setText("Password Changed");
					buttonConfirm.setDisable(true);
				}
				else
				{
					buttonConfirm.setDisable(false);
					errorText.setTextFill(Color.RED);
					errorText.setText("Passwords do not match");
					throw new Exception();
				}
				
				
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

	}

}