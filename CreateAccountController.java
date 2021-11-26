package bankAccountStorage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CreateAccountController 
{
	@FXML
	private Stage primaryStage;
	@FXML
	private TextField fNameField;
	@FXML
	private TextField lNameField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField rPassField;
	@FXML
	private Button createAccountButton;
	@FXML
	private Label errorLabel;
	
	@FXML
	public void initialize() throws Exception
	{
		createAccountButton.setOnAction(new CreateAccountButtonHandler());
		errorLabel.setText("");
	}

	private class CreateAccountButtonHandler implements EventHandler<ActionEvent>
	{
		
		@Override
		public void handle(ActionEvent arg0)
		{
			if(!usernameField.getText().equals("admin"))
			{
				
				if(passwordField.getText().equals(rPassField.getText()))
				{
					ConnectionManager cm = null;
					try
					{
						 cm = new ConnectionManager();
						 cm.createAccount(fNameField.getText(), lNameField.getText(), usernameField.getText(), emailField.getText(), passwordField.getText());
						 primaryStage.close();
						 errorLabel.setTextFill(Color.GREEN);
						 
						 errorLabel.setText("Account Successfully Created");
						 createAccountButton.setDisable(true);
						 cm.close();
					} 
					catch (SQLException e) 
					{
						errorLabel.setTextFill(Color.RED);
						errorLabel.setText(e.getMessage());
						createAccountButton.setDisable(false);
						e.printStackTrace();
					}
					catch (NoSuchAlgorithmException e)
					{
						errorLabel.setTextFill(Color.RED);
						errorLabel.setText(e.getMessage());
						createAccountButton.setDisable(false);
						e.printStackTrace();
					}
					
				}
				else
				{
					errorLabel.setTextFill(Color.RED);
					createAccountButton.setDisable(false);
					errorLabel.setText("Passwords do not match");
				}
				
			}
			else
			{
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("Username unavailable");
			}
			
		}
		
	}
	
}
