package bankAccountStorage;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try
		{
			primaryStage = FXMLLoader.load(getClass().getResource("/bankAccountStorage/LoginScreen.fxml"));
			
		} 
	
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
