package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * Department responsible for registering a guide in the system by the service
 * representative
 * 
 * @author hana
 *
 */
public class RegisterAsGuideController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private TextField Idtxt;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button RegistrationBtn;

	@FXML
	private TextField FNametxt;

	@FXML
	private TextField LNametxt;

	@FXML
	private TextField IdtPhoneTxt;

	@FXML
	private TextField Emailtxt;
	EmployeeIdentificationPage1Controller employee = new EmployeeIdentificationPage1Controller();
	ServiceResresentativeController servuceemployee = new ServiceResresentativeController();

	@FXML
	void CancelFunc(ActionEvent event) throws IOException {
		employee.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
	}

	/**
	 * Saving the guide's data in the database and checking that it is no longer
	 * registered in advance
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void RegistrationFunc(ActionEvent event) throws IOException {
		if (checkFields() == true) {
			String info = FNametxt.getText() + " " + LNametxt.getText() + " " + Idtxt.getText() + " "
					+ IdtPhoneTxt.getText() + " " + Emailtxt.getText();
			String task = "RegisterationAsGuide " + info;
			System.out.println(task);

			ChatClient.NotExistGuide = false;
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.NotExistGuide == true) {
				MessageBox.DisplayMessage("You have successfully registered the user as a guide",
						"Sign up successfullys", "Sign up successfully", AlertType.INFORMATION);
				employee.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
			} else
				MessageBox.DisplayMessage("The user is already registered as guide!", "Error",
						"Error registering as guide", AlertType.WARNING);
		}

	}

	/**
	 * Checking the correctness of all the data entered by the employee on the page
	 * 
	 * @return null if their is error input and else true
	 */
	public Boolean checkFields() {
		if (FNametxt.getText().isEmpty() || Emailtxt.getText().isEmpty() || Idtxt.getText().isEmpty()
				|| LNametxt.getText().isEmpty() || IdtPhoneTxt.getText().isEmpty()) {

			MessageBox.DisplayMessage("You must fill in all the required fields please.", "DB Connection", "SQL Error",
					AlertType.WARNING);
			return false;
		}

		return true;
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
	}

}
