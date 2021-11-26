package bankAccountStorage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChangePasswordController 
{
	@FXML
	private Stage mainStage;
	@FXML
	private TextField newPassInput;
	@FXML
	private TextField rPassInput;
	@FXML
	private Button buttonConfirm;
	@FXML
	private Label errorText;
	
	private User user;
	
	@FXML
	private void initialize() throws Exception
	{
		errorText.setText("");
		buttonConfirm.setOnAction(new ChangePasswordButtonHandler());
		
	}

	public void initData(User user)
	{
		this.user = user;

	}
	
	private class ChangePasswordButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
			try
			{
				
				if(newPassInput.getText().equals(rPassInput.getText()))
				{
					
					ConnectionManager cm = new ConnectionManager();
					
					cm.changePassword(user.getUsername(), newPassInput.getText());
					cm.close();
					errorText.setTextFill(Color.GREEN);
					errorText.setText("Password Changed");
					buttonConfirm.setDisable(true);
					mainStage.close();
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

}
