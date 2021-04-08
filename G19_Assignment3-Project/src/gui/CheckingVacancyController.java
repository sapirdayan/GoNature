
package gui;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

/**
 * This class checks if there is a vacancy in the parks !
 *
 */

public class CheckingVacancyController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtn;

	@FXML
	private Button CheckBtn;

	@FXML
	private Button Cancelbtn;

	@FXML
	private DatePicker DateOfVisit;

	@FXML
	private ComboBox<String> TimeCombo;

	@FXML
	private ComboBox<String> NumberOfVisitors;

	public static LocalDate DateOfVisit1;
	@FXML
	private Label ParkName;
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * This function returns back to Home page employees
	 * 
	 * @param event on action event
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageForEmplyees1.fxml", event, "Home page for emplyees");
	}

	/**
	 * This function checks if there is vacancy for chosen number of people on the
	 * specific date and time
	 * 
	 * @param event on action
	 * @throws IOException CLIENT EXCEPTION
	 */

	@FXML
	void CheckFunc(ActionEvent event) throws IOException {
		if (isError() != null) {
			String info = ChatClient.E1.getOrganizationalAffiliation() + " " + DateOfVisit1.toString() + " "
					+ TimeCombo.getValue() + " " + NumberOfVisitors.getValue();
			System.out.println("&&&: " + ChatClient.getAvailability());
			if (CheckVacancyDB(info)) {
				System.out.println(info);
				System.out.println("&&&: " + ChatClient.getAvailability());
				String s = "There is vacancy for " + ChatClient.getAvailability() + " people.";
				MessageBox.DisplayMessage(s, "Vacancy window", "", AlertType.WARNING);
				e.openPage("HomePageForEmplyees1.fxml", event, "Home page for emplyees");

			} else {
				System.out.println("&&&: " + ChatClient.getAvailability());
				if (Integer.parseInt(ChatClient.getAvailability()) < 0)
					ChatClient.setAvailability("0");
				MessageBox.DisplayMessage("There is not enough space.", "Vacancy window",
						"There is vacancy for " + ChatClient.getAvailability() + " people.", AlertType.ERROR);
			}
		}
	}

	/**
	 * This function logs out and returns to the landing page
	 * 
	 * @param event on mouse click
	 * @throws Exception CLIENT EXCEPTION
	 */

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * This object checks if the inputs of the time, date, number of visitors are
	 * correct if its not correct the user will receive a display message that
	 * explains the problem.
	 * 
	 * @return true if all the inputs are correct,else null
	 */
	Object isError() {
		if (TimeCombo.getValue() == null) {
			MessageBox.DisplayMessage("you must write the time of visit.", "Failure page", "Error", AlertType.WARNING);
			return null;

		} else {
			DateOfVisit1 = DateOfVisit.getValue();
			LocalDate currentdate = LocalDate.now();
			if (DateOfVisit1 == null || DateOfVisit1.isBefore(currentdate)) {
				MessageBox.DisplayMessage("You must select a valid date.", "DB Connection", "SQL Error",
						AlertType.WARNING);
				return null;
			} else {
				if (NumberOfVisitors.getValue() == null) {
					MessageBox.DisplayMessage("you must write the number of visitors.", "Failure page", "Error",
							AlertType.WARNING);
					return null;
				} else
					return 1;
			}
		}
	}

	/**
	 * This function send message from client to server to check vacancy in the data
	 * base
	 * 
	 * @param info users data
	 * @return true if there's vacancy, else false
	 */
	public Boolean CheckVacancyDB(String info) {
		String task = "CheckVacancyDB " + info;
		ClientUI.chat.accept(task);
		return ChatClient.ThereIsVacancy;
	}

	/**
	 * Initialize values of hours and number of visitors in comboBoxes
	 * 
	 * @param location  location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TimeCombo.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
				"16:00", "17:00", "18:00", "19:00");
		NumberOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");
		idtxt.setText(ChatClient.E1.getUsername());
		ParkName.setText(ChatClient.E1.getOrganizationalAffiliation());

	}

}
