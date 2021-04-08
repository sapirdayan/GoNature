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
 * A class used by us to operate the card reader's screens. One for entering the
 * park - to display a screen at the park employee with the ID number received
 * from the card reader and the employee enters the order number. And one for
 * leaving the park - a screen is displayed at the park employee with the ID
 * number received from the card reader and the employee enters the order
 * number.
 * 
 * These screens are used to to identify and register entry and exit from the
 * park. For the purpose of tracking the current number of people in the park.
 * 
 * @author Nasmat Amr
 *
 */
public class ScreensOfCardReaderController implements Initializable {
	@FXML
	private Label username;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnSend;

	@FXML
	private TextField idtxt;

	@FXML
	private TextField ordernum;

	/**
	 * e- A variable of the EmployeeIdentificationPage1Controller class. This
	 * variable is used by us to activate the screen opening method.
	 */
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	void ExitFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
	}

	/**
	 * A method is activated by clicking on the confirmation button, which checks
	 * with the help of the information in DB whether the order exists and the
	 * identifier is the owner of the order.
	 * 
	 * @param event
	 * @throws Exception
	 */

	@FXML
	void SendFunc(ActionEvent event) throws Exception {

		if (isError() != null) {
			String task = "ExitOfVisitor " + ChatClient.E1.getOrganizationalAffiliation() + " " + idtxt.getText() + " "
					+ ordernum.getText();
			ChatClient.setSucceeded(false);
			System.out.println("task: " + task);
			ClientUI.chat.accept(task);
			if (ChatClient.getSucceeded() == true) {
				System.out.println(task);
				MessageBox.DisplayMessage("Exit registration completed successfully", "Exit registration", "Done",
						AlertType.INFORMATION);
				e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
			} else
				MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
		}
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * A method is activated by clicking on the confirmation button, which checks
	 * whether the hiker was indeed in the park and if so, makes an exit
	 * registration for him and updates the current number of people in the park.
	 * (All things are done with the help of the information found in DB).
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void searchFunc(ActionEvent event) throws IOException {
		if (isError() != null) {
			String task = "EnterOfVisitor " + ChatClient.E1.getOrganizationalAffiliation() + " " + ordernum.getText()
					+ " " + idtxt.getText();
			System.out.println("task: " + task);
			ChatClient.ExistOrder = "";
			ClientUI.chat.accept(task);
			if (ChatClient.ExistOrder.equals("yes")) {
				e.openPage("OrderDetailsPage1.fxml", event, "Order Details Page");
			} else
				MessageBox.DisplayMessage("Order number is invalid or does not match ID number.", "Error", "",
						AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username.setText(ChatClient.E1.getUsername());
	}

	/**
	 * Check if all the fields are full and correct
	 * @return
	 */
	Object isError() {
		if (idtxt.getText().isEmpty()) {
			MessageBox.DisplayMessage("There is an error identifying the number.", "Error window", "Error",
					AlertType.ERROR);
			return null;
		} else {
			if (ordernum.getText().isEmpty()) {
				MessageBox.DisplayMessage("Order number is incorrect", "Error window", "Error", AlertType.ERROR);
				return null;
			} else
				return 1;
		}
	}
}
