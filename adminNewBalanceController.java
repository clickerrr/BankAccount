package bankAccountStorage;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class adminNewBalanceController
{
	@FXML
	private Stage mainStage;
	@FXML
	private Scene mainScene;
	@FXML
	private BorderPane mainPane;
	@FXML
	private TextField amountField;
	@FXML
	private Button submitButton;
	@FXML
	private Label errorText;
	
	private String username;
	private String type;
	
	@FXML
	public void initialize()
	{
		errorText.setText("");
		submitButton.setOnAction(new SubmitButtonHandler());
		
	}
	
	public void initData(String username, String type)
	{
		this.username = username;	
		this.type = type;
	}
	
	private class SubmitButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
			
			double newBalance;
			ConnectionManager cm;
			try
			{
				cm = new ConnectionManager();
				
				newBalance = Double.parseDouble(amountField.getText());
				
				newBalance = Math.floor(newBalance * 100) / 100;

				if(newBalance == 0)
				{
					errorText.setText("Please enter a non zero number");
					new Exception("Zero is entered");
				}
				else
				{
					if(type.equals("balance"))
						cm.adminUpdateBalance(username, newBalance);
					else	
						cm.adminUpdateSavingsBalance(username, newBalance);
						
					mainStage.close();
				}
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(NumberFormatException e)
			{
				errorText.setText("Please enter a number");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
}
