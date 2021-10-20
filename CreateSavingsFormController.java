package bankAccountStorage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CreateSavingsFormController
{
	@FXML
	private VBox parentVbox;
	@FXML
	private HBox yesNoHbox;
	@FXML
	private HBox transferHbox;
	@FXML
	private HBox planHbox;
	@FXML
	private HBox btnHbox;
	@FXML
	private HBox errorHbox;
	
	@FXML
	private ChoiceBox<String> choosePlanCb;
	@FXML
	private ChoiceBox<String> yesNoCb;
	@FXML
	private TextField amountTransferField;
	@FXML
	private Button submitButton;
	@FXML
	private Label errorText;
	
	private String username;
	
	@FXML
	public void initialize() throws Exception
	{
		choosePlanCb.getItems().add("A");
		choosePlanCb.getItems().add("B");
		choosePlanCb.getItems().add("C");
		choosePlanCb.setValue("A");
		
		yesNoCb.getItems().add("Yes");
		yesNoCb.getItems().add("No");
		yesNoCb.setValue("No");
		yesNoCb.setOnAction(new YesNoHandler());
		errorText.setText("");
		
		submitButton.setOnAction(new SubmitButtonHandler());
	}
	
	public void initData(String username)
	{
		this.username = username;
		parentVbox.getChildren().clear();
		
		try
		{
			ConnectionManager cm = new ConnectionManager();
			
			if(cm.getUserBalance(username) > 0)
			{
				parentVbox.getChildren().addAll(planHbox, yesNoHbox, btnHbox, errorHbox);
			}
			else
			{
				parentVbox.getChildren().addAll(planHbox, btnHbox, errorHbox);	
			}
			cm.close();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private class YesNoHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent event) 
		{
			if(parentVbox.getChildren().contains(yesNoHbox))
			{
				if(yesNoCb.getValue().equals("Yes"))
				{
					parentVbox.getChildren().clear();
					
					parentVbox.getChildren().addAll(planHbox, yesNoHbox, transferHbox, btnHbox, errorHbox);
					
					
				}
				else if(yesNoCb.getValue().equals("No"))
				{
					parentVbox.getChildren().clear();
					parentVbox.getChildren().addAll(planHbox, yesNoHbox, btnHbox, errorHbox);
					
				}
				
			}
			
		}
		
	}
	
	private class SubmitButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0) 
		{
		
			try
			{
				ConnectionManager cm = new ConnectionManager();
				String savingPlan = choosePlanCb.getValue();
				double balance = 0;
				if(parentVbox.getChildren().contains(transferHbox))
				{
					balance = Double.parseDouble(amountTransferField.getText());
					if(balance > cm.getUserBalance(username))
					{
						errorText.setText("Insufficient Funds");
						throw new Exception();
					}
				}
				
				cm.createSavings(username, savingPlan, balance);
				
				errorText.setTextFill(Color.GREEN);
				errorText.setText("Savings Account Created, Please Re-Login\nOr Transfer Funds to view");
				cm.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	
}
