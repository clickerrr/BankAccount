package bankAccountStorage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScreenController 
{
	@FXML
	private Stage primaryStage;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Button loginButton;
	@FXML
	private Button createAccountBtn;
	@FXML
	private Label errorLabel;
	
	@FXML
	private void initialize() throws Exception
	{
	
		createAccountBtn.setOnAction(new CreateAccountButtonHandler());
		loginButton.setOnAction(new LoginButtonHandler());
		errorLabel.setText("");
		
		
		
	}
	
	private class CreateAccountButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{
			Stage primaryStage = null;
			try
			{
				primaryStage = FXMLLoader.load(getClass().getResource("/bankAccountStorage/CreateAccount.fxml"));
			} 
		
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		
			primaryStage.show();
			
		}
		
	}
	
	private class LoginButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{
			
			
			ConnectionManager connection = null;
			try {
				connection = new ConnectionManager();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				
				if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty())
				{
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("Please ensure all fields are filled");
					throw new Exception();
				}
				
				String username = connection.getUser(usernameField.getText(), passwordField.getText());
				if (username == null)
				{
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("Incorrect Username or Password");
					throw new SQLException();
				}
				
				errorLabel.setTextFill(Color.GREEN);
				errorLabel.setText("Logged In");
				primaryStage.close();
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/AccountScreen.fxml"));
					Stage accountStage = loader.load();
					AccountScreenController accController = loader.getController();
					accController.initData(username);
					accountStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				connection.close();
			} catch (SQLException e) {
				loginButton.setDisable(false);
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				loginButton.setDisable(false);
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("fucky wucky something");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}

	
}
