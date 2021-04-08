
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Department manager controller
 *
 */
public class DeparmentManegerController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private ComboBox<String> ReporeCombo;

	@FXML
	private ImageView ContactUsBtn;

	@FXML
	private Label LogoutBtn;

	@FXML
	private Button CreateReportBtn;

	@FXML
	private TextArea Reminder;

	@FXML
	private Button NumberVisitors;

	@FXML
	private Label DepDiscount;

	@FXML
	private Button DiscountCombo;

	@FXML
	private Label ConfirmationOParameters;

	@FXML
	private ComboBox<String> ListOfReports;

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * This function for confirmation parameters
	 * 
	 * @param event on mouse click
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void ConfirmationOParametersFunc(MouseEvent event) throws IOException {

		e.openPage("Confirmationofparameters1.fxml", event, "Confirmation Parameters Page");
	}

	/**
	 * This function opens DiscountConfirmationPage
	 * 
	 * @param event on mouse click
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void DepDiscountFunc(MouseEvent event) throws IOException {
		e.openPage("DiscountConfirmationPage.fxml", event, "Discount page");
	}

	/**
	 * This function checks is the report was submitted or not.
	 * 
	 * @param flag boolean parameter
	 */
	public void checkfullreport(Boolean flag) {
		if (flag == false)
			MessageBox.DisplayMessage("The report is empty", "", "Park maneger", AlertType.WARNING);

		else
			MessageBox.DisplayMessage("The report was submeted.", "", "Park maneger", AlertType.WARNING);
	}

	/**
	 * For choosing a type of report
	 * 
	 * @param event on action
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void ListOfReportfunc(ActionEvent event) throws IOException {
		ChatClient.whois = "dep";
		if (ListOfReports.getValue().equals("Total number of visitors report")) {
			ChatClient.TypeOfReport = "visitors";
			ChatClient.type = "VisitReport";
			e.openPage("BforeDepartment.fxml", event, "Choosing page");

		}

		else {
			if (ListOfReports.getValue().equals("Usage report")) {
				ChatClient.type = "Occupancy";
				ChatClient.TypeOfReport = "Occupancy";
				e.openPage("BforeDepartment.fxml", event, "Choosing page");

			} else {
				ChatClient.TypeOfReport = "Income";
				ChatClient.type = "Income";
				e.openPage("BforeDepartment.fxml", event, "Choosing page");

			}
		}

	}

	/**
	 * send message from client to server about the current number of the visitors
	 * in the park
	 * 
	 * @param event on action
	 */
	@FXML
	void NumberVisitorsFunc(ActionEvent event) {

		String task = "ReturnNumberOfVisitorsPresentInThePark " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task);

		String NumberOfVisitor = "The Number of visitors present in the park is: " + ChatClient.NumerOfVisitorInThePark;
		MessageBox.DisplayMessage(NumberOfVisitor, "Information for department maneger",
				"Information for department maneger", AlertType.WARNING);

	}

	/**
	 * set reminder text
	 * 
	 * @param string reminder text
	 */
	public void WriteInReminder(String string) {
		Reminder.setText(string);
	}

	/**
	 * Open contact us page
	 * 
	 * @param event on mouse click
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void ContactUsFunc(MouseEvent event) throws IOException {
		e.openPage("ContactUs.fxml", event, "Contact us page");

	}

	/**
	 * creating a report with a suitable name of the type of the report, after
	 * checking the input existing.
	 * 
	 * @param event on action
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void CreateReport(ActionEvent event) throws IOException {

		ChatClient.whois = "dep";
		if (ReporeCombo.getValue() == null) {
			MessageBox.DisplayMessage(
					"Department manager, \nYou must choose the name of the report you want to generate.", "Error",
					"Error", AlertType.WARNING);
		} else {

			switch (ReporeCombo.getValue()) {
			case "Visit report - by length of stay":
				ChatClient.TypeOfReport = "VisitsStay";
				e.openPage("VisitingTimeStayTime.fxml", event, "Choosing report");

				break;

			case "Visit report - by time of entry":
				ChatClient.TypeOfReport = "VisitsEntry";
				e.openPage("VisitingReportEnterTime.fxml", event, "Choosing report");

				break;

			case "Cancellation report":
				ChatClient.TypeOfReport = "Cancel";
				e.openPage("BeforeReport.fxml", event, "Choosing report");

				break;
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
	void LogoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * initialize the the parameters and the reminder text
	 * 
	 * @param location  location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String st = "";
		String task = "GiveMeTheReminder " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task);
		if (ChatClient.Reminder.equals("The#is#no#messages")) {
			st = "";
		} else {
			st = ChatClient.Reminder.replace("#", " ");
			st = st.replace("&", "\n");
			System.out.println("st: " + st);
			System.out.println("s[2]: " + ChatClient.Reminder);
		}
		Reminder.setText(st);
		idtxt.setText(ChatClient.E1.getUsername());
		ReporeCombo.getItems().addAll("Cancellation report", "Visit report - by length of stay",
				"Visit report - by time of entry");
		ListOfReports.getItems().addAll("Total number of visitors report", "Usage report",
				"Income report for a specific month");
	}

}
