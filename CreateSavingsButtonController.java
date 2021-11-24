package bankAccountStorage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CreateSavingsButtonController extends AccountScreenController
{
	
	@FXML
	private VBox parentVbox;
	@FXML
	private Button createSavings;
	
	private BorderPane superPane;
	
	private String username;
	
	@FXML
	public void initialize() throws Exception
	{
		createSavings.setOnAction(new ButtonHandler());
		
	}
	
	public void initData(String username, BorderPane superPane)
	{
		this.username = username;
		
		this.superPane = superPane;
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent arg0)
		{

			superPane.getChildren().remove(parentVbox);
			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/CreateSavingsForm.fxml"));
				VBox createSavingsPane = loader.load();
				CreateSavingsFormController savingsFormController = loader.getController();
				savingsFormController.initData(username, superPane);

				superPane.setCenter(createSavingsPane);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		
	}
	
}
