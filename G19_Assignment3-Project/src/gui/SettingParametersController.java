package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
 * In this department the department manager sends to the park manager the
 * parameters for approval of three types of parameters
 * 
 * @author hana
 *
 */

public class SettingParametersController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button GapBtn;

	@FXML
	private Button Maxquotabtn;

	@FXML
	private Button StayTimebtn;

	@FXML
	private TextField Gaptxt;

	@FXML
	private TextField Maxquotatxt;
	@FXML
	private Button Homepage;

	@FXML
	private TextField StayTimetxt;

	public static String reminder = "";
	public static String s = "";

	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	public boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	/**
	 * Send the Gap parameter to the department maneger
	 * 
	 * @param event
	 */

	@FXML
	void GapFunc(ActionEvent event) {
		if (!Gaptxt.getText().isEmpty()) {
			if (isNumeric(Gaptxt.getText())) {
				String task = "updateGap " + ChatClient.E1.getOrganizationalAffiliation() + " " + Gaptxt.getText();
				Gaptxt.setText("");
				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task); // send message from client to server
				// send to the department manger
				if (ChatClient.getSucceeded() == true)
					MessageBox.DisplayMessage("The gap parameter is sent for approval to the department manager",
							"Sent successfully for confirmation", "", AlertType.INFORMATION);
				s = "The#park#maneger#sent#you#gap#parameter#for#conformation#";
				String task6 = "GiveMeTheReminder " + ChatClient.E1.getOrganizationalAffiliation();
				ClientUI.chat.accept(task6);
				System.out.println("ChatClient.Reminder: " + ChatClient.Reminder);
				if (ChatClient.Reminder.equals("The#is#no#messages")) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation()
							+ " &&*The#park#maneger#sent#you#gap#parameter#for#conformation#\n";
					System.out.println("r: " + reminder);

					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				} else if (!ChatClient.Reminder.contains(s)) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation() + " "
							+ ChatClient.Reminder + "&&*The#park#maneger#sent#you#gap#parameter#for#conformation#\n";
					System.out.println("r1: " + reminder);

					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				}
			} else
				MessageBox.DisplayMessage("you must choose the valid value", "Error", "", AlertType.WARNING);

		} else
			MessageBox.DisplayMessage("you must choose the value of gap parameter", "Error", "", AlertType.WARNING);
	}

	/**
	 * send the max quota parameter to the separtment maneger
	 * 
	 * @param event
	 */

	@FXML
	void MaxquotaFunc(ActionEvent event) {

		if (!Maxquotatxt.getText().isEmpty()) {
			if (isNumeric(Maxquotatxt.getText())) {
				String task = "updateTheMaxQuota " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ Maxquotatxt.getText();
				Maxquotatxt.setText("");
				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true)
					// send to the department manger
					MessageBox.DisplayMessage("The max quota parameter is sent for approval to the department manager",
							"Sent successfully for confirmation", "", AlertType.INFORMATION);

				String task6 = "GiveMeTheReminder " + ChatClient.E1.getOrganizationalAffiliation();
				ClientUI.chat.accept(task6);
				s = "The#park#maneger#sent#you#max#quota#time#parameter#for#conformation#";
				System.out.println("ChatClient.Reminder: " + ChatClient.Reminder);
				if (ChatClient.Reminder.equals("The#is#no#messages")) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation()
							+ " &&*The#park#maneger#sent#you#max#quota#time#parameter#for#conformation#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				} else if (!ChatClient.Reminder.contains(s)) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation() + " "
							+ ChatClient.Reminder
							+ "&&*The#park#maneger#sent#you#max#quota#time#parameter#for#conformation#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				}

			} else
				MessageBox.DisplayMessage("you must choose the valid value", "Error", "", AlertType.WARNING);

		} else
			MessageBox.DisplayMessage("you must choose the value of max quota parameter", "Error", "",
					AlertType.WARNING);
	}

	/**
	 * send the stay time parametr to the department maneger
	 * 
	 * @param event
	 */

	@FXML
	void StayTimeFunc(ActionEvent event) {
		if (!StayTimetxt.getText().isEmpty()) {
			if (isNumeric(StayTimetxt.getText())) {
				String task = "updateStayTime " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ StayTimetxt.getText();
				StayTimetxt.setText("");
				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true)
					// send to the department manger
					MessageBox.DisplayMessage("The stay time parameter is sent for approval to the department manager",
							"Sent successfully for confirmation", "", AlertType.INFORMATION);

				String task6 = "GiveMeTheReminder " + ChatClient.E1.getOrganizationalAffiliation();
				s = "park#maneger#sent#you#stay#time#parameter#for#conformation#";
				ClientUI.chat.accept(task6);
				System.out.println("ChatClient.Reminder: " + ChatClient.Reminder);
				if (ChatClient.Reminder.equals("The#is#no#messages")) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation()
							+ " &&*The#park#maneger#sent#you#stay#time#parameter#for#conformation#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				} else if (!ChatClient.Reminder.contains(s)) {
					reminder = "UpdateTheReminder " + ChatClient.E1.getOrganizationalAffiliation() + " "
							+ ChatClient.Reminder
							+ "&&*The#park#maneger#sent#you#stay#time#parameter#for#conformation#\n";
					System.out.println("reminder: " + reminder);
					ClientUI.chat.accept(reminder);
				}
			} else
				MessageBox.DisplayMessage("you must choose the valid value", "Error", "", AlertType.WARNING);
		} else
			MessageBox.DisplayMessage("you must choose the value of stay time parameter", "Error", "",
					AlertType.WARNING);
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * return to the park manger home page
	 * 
	 * @param event
	 * @throws IOException
	 */

	@FXML
	void ReturnToTheHomePage(ActionEvent event) throws IOException {
		ParkManagerController home = new ParkManagerController();
		home.returnToTheHomePage(event);

	}

	/**
	 * Initialize the ID field
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());

	}

}
