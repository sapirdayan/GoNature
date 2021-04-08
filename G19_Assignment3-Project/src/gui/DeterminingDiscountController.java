
package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This controller to determined the discount
 *
 *
 */
public class DeterminingDiscountController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private TextField valuetxt;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button SubmitBtn;
	@FXML
	private DatePicker StartDate;

	@FXML
	private DatePicker EndDate;
	@FXML
	private ComboBox<String> typecombo;
	ParkManagerController home = new ParkManagerController();
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * This function returns back to Home page employees
	 * 
	 * @param event on action event
	 * @throws IOException client exception
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageParkManeger1.fxml", event, "Home page park maneger");

	}

	/**
	 * This function submit the discount to the department manager
	 * 
	 * @param event on action
	 * @throws IOException client exception
	 */
	@FXML
	void SubmitFunc(ActionEvent event) throws IOException {
		if (checkFields() != null) {
			String type = "";
			System.out.println(("type: " + typecombo.getValue()));
			if (typecombo.getValue().equals("For family subscription"))
				type = "1";
			else
				type = "2";
			String task = "DeterminingDiscount " + ChatClient.E1.getOrganizationalAffiliation() + " "
					+ valuetxt.getText() + " " + StartDate.getValue() + " " + EndDate.getValue() + " " + type;
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task);
			System.out.println("task##: " + task);
			if (ChatClient.getSucceeded() == true) {

				String task6 = "GiveMeTheReminder " + ChatClient.E1.getOrganizationalAffiliation();
				ClientUI.chat.accept(task6);
				String s = "The#park#manager#sent#you#a#discount#update#messege.#";
				System.out.println("ChatClient.Reminder: " + ChatClient.Reminder);
				if (ChatClient.Reminder.equals("The#is#no#messages")) {
					String reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation()
							+ " &&*The#park#manager#sent#you#a#discount#update#messege.#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				} else if (!ChatClient.Reminder.contains(s)) {
					String reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation() + " "
							+ ChatClient.Reminder + "&&*The#park#manager#sent#you#a#discount#update#messege.#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				}

				if (ChatClient.getSucceeded() == true) {

					String s1 = "\nValue of discount: " + valuetxt.getText() + "%\n" + "Start Date: "
							+ StartDate.getValue().toString() + "\n" + "End date :" + EndDate.getValue().toString();
					s = "The discount  details are: " + s1 + "\n"
							+ "Sent successfully for approval by the department manager";
					MessageBox.DisplayMessage(s, "Success", "", AlertType.INFORMATION);
					ChatClient.discount1 = true;
					e.openPage("HomePageParkManeger1.fxml", event, "Home page park maneger");
				}
			}
		}
	}

	/**
	 * This object check if all the input values are correct and displaying a
	 * suitable message in case there were a problem.
	 * 
	 * @return true if all the inputs are correct,else null
	 */
	Object checkFields() {
		if (valuetxt.getText().isEmpty()) {
			MessageBox.DisplayMessage(
					"you must choose the value of the discount before sending for approval to the department manager",
					"Error", "", AlertType.WARNING);
			return null;
		} else {
			LocalDate currentdate = LocalDate.now();
			if (StartDate.getValue() == null) {

				MessageBox.DisplayMessage("You must choose a valid start date of the discount", "Error", "",
						AlertType.WARNING);
				return null;
			} else {
				if (StartDate.getValue().isBefore(currentdate.plusDays(1))) {
					MessageBox.DisplayMessage("Start date must be after today", "Park Manager\n", "",
							AlertType.WARNING);
					return null;
				} else {
					if (EndDate.getValue() == null) {
						MessageBox.DisplayMessage("you must choose a valid end date of the discount", "Error", "",
								AlertType.WARNING);
						return null;
					} else {
						if (EndDate.getValue().isBefore(StartDate.getValue())) {
							MessageBox.DisplayMessage("The discount must be at least one day after the start date",
									"Park Manager", "", AlertType.WARNING);
							return null;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This function logs out and returns to the landing page
	 * 
	 * @param event on mouse click
	 * @throws Exception client exception
	 */
	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * initializing the type of the order in the comboBox
	 * 
	 * @param location  location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		typecombo.getItems().addAll("For family subscription", "For regular travelers");
		idtxt.setText(ChatClient.E1.getUsername());
	}

}
