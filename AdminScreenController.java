package bankAccountStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminScreenController 
{
	private ArrayList<User> allUsers;
	
	@FXML
	private Stage primaryStage;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private TableView<User> mainTable;
	
	@FXML
	private TableColumn firstNameCol;
	
	@FXML
	private TableColumn lastNameCol;
	
	@FXML
	private TableColumn emailCol;
	
	@FXML
	private TableColumn balanceCol;
	
	@FXML
	private TableColumn sPlanCol;
	
	@FXML
	private TableColumn sBalanceCol;
	
	@FXML
	private ContextMenu contextMenu;
	@FXML
	private BorderPane mainBorderPane;
	
	@FXML
	private Button refresh;
	
	@FXML
	private CheckBox deleteModeCheck;
	@FXML
	private Label offonLabel;
	@FXML
	private Label deleteModeError;
	@FXML
	private Label errorText;
	@FXML
	private TextField lastNameSearch;
	
	private double newBalance;
	private boolean deleteMode;
	
	@FXML
	private void initialize() throws Exception
	{    
		deleteModeError.setText("");
		deleteModeError.setTextFill(Color.RED);
		deleteModeCheck.setOnAction(new DeleteModeHandler());
		
		errorText.setText("");
		
		deleteMode = false;
		logoutButton.setOnAction(new LogoutButtonHandler());
		refresh.setOnAction(new LogoutButtonHandler());
		lastNameSearch.setOnKeyTyped(new SearchHandler());
	    mainTable.getColumns().addListener(new ListChangeListener<Object>() {
	          public boolean suspended;
	          @Override
	          public void onChanged(Change change) {
	              change.next();
	              if (change.wasReplaced() && !suspended) {
	                  this.suspended = true;
	                  mainTable.getColumns().setAll(firstNameCol, lastNameCol, emailCol, balanceCol, sPlanCol, sBalanceCol);
	                  this.suspended = false;
	              }
	          }
	      });
	    
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
		sPlanCol.setCellValueFactory(new PropertyValueFactory<>("savingsPlan"));
		sBalanceCol.setCellValueFactory(new PropertyValueFactory<>("savingsBalance"));

		mainTable.setOnMousePressed(new RightClickHandler());
		mainTable.setOnKeyPressed(new DeleteKeyEventHandler(mainTable));
		
		allUsers = null;
		ConnectionManager cm = null;
		try
		{
			cm = new ConnectionManager();
			allUsers = cm.adminGetAllUserData();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		for(int i = 0; i < allUsers.size(); i++)
		{
			allUsers.get(i).formatDataFields();
			mainTable.getItems().add(allUsers.get(i));
		}
		
		mainTable.getSelectionModel().selectFirst();
		
	}
	
	private class SearchHandler implements EventHandler<KeyEvent>
	{

		@Override
		public void handle(KeyEvent e)
		{
			if(e.getSource() == lastNameSearch)
			{
				String lastName = lastNameSearch.getText();
				lastName = lastName.trim();
				if(lastName.isBlank())
				{
					mainTable.getItems().clear();
					if( mainTable.getItems().isEmpty() )
					{
						errorText.setText("");
						for(int i = 0; i < allUsers.size(); i++)
						{
							allUsers.get(i).formatDataFields();
							mainTable.getItems().add(allUsers.get(i));
						}
						
						mainTable.getSelectionModel().selectFirst();	
					}
				}
				else
				{
					ArrayList<User> users = findUserByLastName(lastName);
					if(users.isEmpty())
					{
						mainTable.getItems().clear();
						errorText.setText("No Users Found");
					}
					else
					{
						errorText.setText("");
						mainTable.getItems().clear();
						for(int i = 0; i < users.size(); i++)
						{
							allUsers.get(i).formatDataFields();
							mainTable.getItems().add(users.get(i));
						}	
					}
					
				}
				
			}
			
	
		}
		
	}
	
	private class DeleteModeHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent e)
		{

			if(e.getSource() == deleteModeCheck)
			{
				if(deleteModeCheck.isSelected())
				{
					if(!deleteModeError.getText().isBlank())
						deleteModeError.setText("");
					deleteMode = true;
					offonLabel.setText("Enabled");
					offonLabel.setTextFill(Color.RED);
				}
				else
				{
					deleteMode = false;
					offonLabel.setText("Disabled");
					offonLabel.setTextFill(Color.GREEN);
				}
				
				
			}
	
		}
		
		
		
		
	}
	
	private class LogoutButtonHandler implements EventHandler<ActionEvent>
	{

		@Override
		public void handle(ActionEvent e) 
		{
			if(e.getSource() == logoutButton)
			{

				primaryStage.close();
				try {
					Stage login = FXMLLoader.load(getClass().getResource("/bankAccountStorage/LoginScreen.fxml"));
					login.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			else if (e.getSource() == refresh)
			{
				refreshTable();
			}
			
		}
		
	}
	
	private class RightClickHandler implements EventHandler<MouseEvent>
	{

		@Override
		public void handle(MouseEvent e)
		{
			if(e.getButton() == MouseButton.SECONDARY)
			{
				try
				{
					contextMenu = FXMLLoader.load(getClass().getResource("/bankAccountStorage/AdminContextMenu.fxml"));
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				((TableView)e.getSource()).setContextMenu(contextMenu);
				contextMenu.setOnAction(new ContextMenuHandler((TableView)e.getSource()));
			}
			else if(e.getButton() == MouseButton.PRIMARY)
			{
			}

		}
		
	}
	
	private class ContextMenuHandler implements EventHandler<ActionEvent>
	{
		
		TableView node = null;
		public ContextMenuHandler(TableView target)
		{
			node = target;
		}
		
		public void handle(ActionEvent e)
		{
			int selectedRow = node.getSelectionModel().getSelectedIndex();

			MenuItem mu = (MenuItem)e.getTarget();
			
			switch(mu.getText())
			{
				case "Update Balance":
					updateUserBalance(selectedRow);
					break;
				case "Update Savings Balance":
					updateUserSavingsBalance(selectedRow);
					break;
				case "Delete User":
					if(deleteMode)
					{
						deleteUser(selectedRow);
					}
					else
					{
						deleteModeError.setText("Enable Deletion Mode to Delete Users");
					}
					break;
			}

		}
		
		
		
	}
	
	
	private class DeleteKeyEventHandler implements EventHandler<KeyEvent>
	{
		TableView node = null;
		public DeleteKeyEventHandler(TableView target)
		{
			node = target;
		}

		@Override
		public void handle(KeyEvent e)
		{
			if(e.getCode().equals(KeyCode.DELETE))
			{
				int selectedRow = node.getSelectionModel().getSelectedIndex();

				if(deleteMode)
				{
					deleteUser(selectedRow);
				}
				else
				{
					deleteModeError.setText("Enable Deletion Mode to Delete Users");
				}
			}
			
		}
		
	}
	
	private void updateUserBalance(int index)
	{	
		try 
		{
			User user = findUser(emailCol.getCellData(index).toString());
			errorText.setText("");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/adminNewBalance.fxml"));

			Stage newBalanceStage = null;
			newBalanceStage = loader.load();
			
			adminNewBalanceController newBalanceController = loader.getController();
			newBalanceController.initData(user.getUsername(), "balance");
			newBalanceStage.showAndWait();
			refreshTable();
		
		}
		catch(NullPointerException e)
		{
			errorText.setText("No User selected");
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
		
	
	private void updateUserSavingsBalance(int index)
	{
		try
		{
			User user = findUser(emailCol.getCellData(index).toString());
			errorText.setText("");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankAccountStorage/adminNewBalance.fxml"));
			Stage newBalanceStage = null;
			
			if(user.getSavingsPlan().equals("None"))
			{
				errorText.setText("User Does Not Have a Savings Account");
			}
			else
			{
				if(!errorText.getText().isBlank())
				{
					errorText.setText("");
				}
			
				newBalanceStage = loader.load();
				
				adminNewBalanceController newBalanceController = loader.getController();
				newBalanceController.initData(user.getUsername(), "savings");
				newBalanceStage.showAndWait();
				refreshTable();
			}
		
		}
		catch(NullPointerException e)
		{
			errorText.setText("No User selected");
			e.printStackTrace();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
			
		
	}
	
	private void deleteUser(int index)
	{	
		ConnectionManager cm = null;
		try
		{
			User user = findUser(emailCol.getCellData(index).toString());
			cm = new ConnectionManager();
			cm.adminDeleteUser(user.getUsername());
			errorText.setText("");
			refreshTable();
				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			errorText.setText("No User selected");
			e.printStackTrace();
		}
		
	}
	
	private User findUser(String email)
	{
		
		User returnUser = null;
		
		int start = 0;
		int middle = allUsers.size() / 2;
		int end = allUsers.size() - 1;
		
		while(returnUser == null)
		{
			if(email.compareTo(allUsers.get(middle).getEmail()) > 0)
			{
				start = middle + 1;
				middle = start + ((end - start) / 2); 
			}
			else if(email.compareTo(allUsers.get(middle).getEmail()) < 0)
			{
				end = middle - 1;
				middle = (end - start) / 2;
			}
			else
			{
				returnUser = allUsers.get(middle);
			}
			
		}
		
		return returnUser;
		
	}
	
	private ArrayList<User> findUserByLastName(String lastName)
	{
		ArrayList<User> returnUsers = new ArrayList<User>();
		for(int i = 0; i < allUsers.size(); i++)
		{
			if(allUsers.get(i).getLastName().toLowerCase().equals(lastName.toLowerCase()))	
				returnUsers.add(allUsers.get(i));
		}
		
		return returnUsers;
		
		
	}
	
	

	public void refreshTable()
	{
		allUsers.clear();
		int index = mainTable.getSelectionModel().getSelectedIndex();

		ConnectionManager cm = null;
		try
		{
			cm = new ConnectionManager();
			allUsers = cm.adminGetAllUserData();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		mainTable.getItems().clear();
		String lastName = lastNameSearch.getText();
		if(lastName.isBlank())
		{
			for(int i = 0; i < allUsers.size(); i++)
			{
				allUsers.get(i).formatDataFields();
				mainTable.getItems().add(allUsers.get(i));
			}
			
			if(index == mainTable.getItems().size())
			{
				mainTable.getSelectionModel().selectLast();
			}
			else
			{
				mainTable.getSelectionModel().select(index);
			}
		}
		else
		{
			ArrayList<User> users = findUserByLastName(lastName);
			if(users.isEmpty())
			{
				mainTable.getItems().clear();
				errorText.setText("No Users Found");
			}
			else
			{
				errorText.setText("");
				mainTable.getItems().clear();
				for(int i = 0; i < users.size(); i++)
				{
					users.get(i).formatDataFields();
					mainTable.getItems().add(users.get(i));
				}	
			}
			
		}
		
		System.out.println("Table refreshed!");
	}
	
}
