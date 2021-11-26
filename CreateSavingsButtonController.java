package bankAccountStorage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CreateSavingsButtonController extends AccountScreenController
{
	
	@FXML
	private VBox parentVbox;
	@FXML
	private Button createSavings;
	
	private BorderPane superPane;
	
	private User user;
	private Label superBalance;
	
	@FXML
	public void initialize() throws Exception
	{
		createSavings.setOnAction(new ButtonHandler());
		
	}
	
	public void initData(User user, BorderPane superPane, Label superBalance)
	{
		this.user = user;
		
		this.superPane = superPane;
		this.superBalance = superBalance;
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{

			superPane.getChildren().remove(parentVbox);
			try
			{
				ConnectionManager cm = new ConnectionManager();
				user = cm.getUser(user.getUsername());
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/CreateSavingsForm.fxml"));
				VBox createSavingsPane = loader.load();
				CreateSavingsFormController savingsFormController = loader.getController();
				savingsFormController.initData(user, superPane, superBalance);

				superPane.setCenter(createSavingsPane);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		
	}
	
}
