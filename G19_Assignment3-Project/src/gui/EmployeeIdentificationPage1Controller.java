package gui;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * A class used by us to identify employees and open their home screens, each
 * according to its role.
 * 
 * @author Nasmat Amr
 *
 */
public class EmployeeIdentificationPage1Controller {

	@FXML
	private Button EmployeeBtn;

	@FXML
	private Button TravelerBtn;

	@FXML
	private Pane pane1;

	@FXML
	private TextField Usernametxt;

	@FXML
	private PasswordField PasswordTxt;

	@FXML
	private Button logInBtn;

	@FXML
	private Button Cancelbtn;

	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		openPage("LandingPage.fxml", event, "Landing Page");
	}

	/**
	 * This method, for verifying the information entered by the employee, and if
	 * the information is verified opens the appropriate screen for the employee.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void SendFunc(ActionEvent event) throws IOException {
		String UserName, Password;

		if (checkfields() != null) {
			UserName = Usernametxt.getText();
			Password = PasswordTxt.getText();
			String task = "indeftifictionEmployee " + UserName + " " + Password;
			System.out.println("+++++++" + task);
			ChatClient.ValidateUserName = false;
			ChatClient.ValidatePassword = false;
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.IsConnected == false) {
				if (printmessage() != null) {
					if (ChatClient.E1.getRole().equals("1")) {
						openPage("ServiceResresentativeHomePage1.fxml", event, "Service Resresentative Home Page");
					}
					if (ChatClient.E1.getRole().equals("2")) {
						openPage("HomePageDepartmentManeger1.fxml", event, "Home Page Department Maneger");
					}
					if (ChatClient.E1.getRole().equals("3")) {
						openPage("HomePageParkManeger1.fxml", event, "Home Page For Park Maneger");
					}
					if (ChatClient.E1.getRole().equals("4")) {
						openPage("HomePageForEmplyees1.fxml", event, "HomePage For Emplyees");
					}
				}
			} else
				MessageBox.DisplayMessage("You are already logged in from elsewhere.", "Error window", "ERROR",
						AlertType.ERROR);
		}
	}

	/**
	 * This method is used by us for all employee screens. Use this method to open
	 * the desired screen.
	 * By clicking X. We handle the event, 
	 * perform an update in DB that the user has disconnected from the system.
	 * @param nameOfPage A string of the fxml file name.
	 * @param event
	 * @param name       The title of the screen you want to open.
	 * @throws IOException
	 */
	void openPage(String nameOfPage, Event event, String name) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Parent pane = FXMLLoader.load(getClass().getResource(nameOfPage));
		Stage primaryStage = new Stage();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				String task = "UpdateConnected " + ChatClient.E1.getUsername();
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true) {
					Platform.exit();
				}
			}
		});
		Scene scene = new Scene(pane);
		primaryStage.setTitle(name);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	/**
	 * method for checking that the fields are not empty.
	 * @return Returns null if one exists. else, 1;
	 */
	public Object checkfields() {

		if (Usernametxt.getText().isEmpty()) {
			MessageBox.DisplayMessage("You must enter your User name", " Input error in User name", "ERROR",
					AlertType.ERROR);
			return null;
		} else {
			if (PasswordTxt.getText().isEmpty()) {
				MessageBox.DisplayMessage("You must enter your Password", " Input error in Password", "ERROR",
						AlertType.ERROR);
				return null;
			} else {
				return 1;
			}
		}
	}
	/**
	 * A method that displays an alert window to display user information.
	 * @return object
	 */

	public Object printmessage() {
		if (ChatClient.ValidateUserName == false) {
			MessageBox.DisplayMessage("The user name doesn't exist in the system ", " Input error in User name", " ",
					AlertType.ERROR);
			return null;
		} else {
			if (ChatClient.ValidateUserName == true && ChatClient.ValidatePassword == false) {
				MessageBox.DisplayMessage("The Password is not correct please try again! ", " Input error in password",
						" ", AlertType.ERROR);
				return null;
			} else
				return 1;
		}

	}
}