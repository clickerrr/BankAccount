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
	// primary stage
	@FXML
	private Stage primaryStage;
	// field for username
	@FXML
	private TextField usernameField;
	// field for password
	@FXML
	private TextField passwordField;
	// login button
	@FXML
	private Button loginButton;
	// create account button
	@FXML
	private Button createAccountBtn;
	// error label that displays any errors
	@FXML
	private Label errorLabel;
	
	// initialize that runs when the loader is loaded
	@FXML
	private void initialize() throws Exception
	{
		createAccountBtn.setOnAction(new ButtonHandler());
		loginButton.setOnAction(new ButtonHandler());
		usernameField.setOnAction(new ButtonHandler());
		passwordField.setOnAction(new ButtonHandler());
		errorLabel.setText("");
	}
	
	// button handler that handles login events
	private class ButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent event)
		{
			// if the event comes from the create account button
			if(event.getSource() == createAccountBtn) 
			{
				// launch the create account stage
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
			// otherwise if the event comes from the login button, user name field, or the password field
			else if(event.getSource() == loginButton || event.getSource() == usernameField || event.getSource() == passwordField)
			{
				try 
				{
					// if any of the fields are empty
					if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty())
					{
						// sets the error label text to red and shows an error message
						errorLabel.setTextFill(Color.RED);
						errorLabel.setText("Please ensure all fields are filled");
						throw new Exception("Empty Fields");
					}
					
					// otherwise if the admin login is provided
					if(usernameField.getText().equals("admin") && passwordField.getText().equals("password"))
					{
						// launches the admin screen stage
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/AdminScreen.fxml"));
						Stage adminStage = loader.load();
						adminStage.show();
						// closes current stage
						primaryStage.close();
					}
					else
					{
						// otherwise we get a conenction and gets any users with this username and password
						ConnectionManager connection = new ConnectionManager();
						boolean loggedIn = connection.loginUser(usernameField.getText(), passwordField.getText());
						
						// if the user does not exist
						if (!loggedIn)
						{
							// we have an incorrect username or password
							errorLabel.setTextFill(Color.RED);
							errorLabel.setText("Incorrect Username or Password");
							throw new Exception("User not found");
						}
						
						// passes the user to  
						User user = connection.getUser(usernameField.getText());
						connection.close();
						
						if(user != null)
						{	
							// launches the account stage 
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/AccountScreen.fxml"));
							Stage accountStage = loader.load();
							AccountScreenController accController = loader.getController();
							
							accController.initData(user);
							accountStage.show();
						}

						// closes the stage
						primaryStage.close();
					}
					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				catch (NoSuchAlgorithmException e) 
				{
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
	
}
