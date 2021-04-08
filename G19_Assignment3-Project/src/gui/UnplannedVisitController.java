package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


/**
 * This department is used by us to place an order for an occasional visit. The
 * reservation is made by the park employee.
 * 
 * @author Nasmat Amr
 *
 */
public class UnplannedVisitController implements Initializable {
	public static EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private ComboBox<String> NumberOfVisitors;

	@FXML
	private TextField EmailTxt;

	@FXML
	private ComboBox<String> TypeCombo1;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button PayingCash;
	@FXML
	private Label ParkName;

	@FXML
	private Label DateOfVisit;

	@FXML
	private Label timeofvisit;

	@FXML
	private Button PayingCredit;
	public static String order;
	public static String task;
	public static LocalDate DateOfVisit1;
	LocalTime clock = LocalTime.now();
	String[] result2 = clock.toString().split(".");
	String[] result = clock.toString().split(":");
	String t = result[0];
	LocalDate currentdate = LocalDate.now();
	String type = "";

	@FXML
	void CancelFunc(ActionEvent event) throws IOException {

		e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
	}
/**
 * Beyond the cash payment
 * @param event
 * @throws IOException
 */
	@FXML
	void PayingCashFunc(ActionEvent event) throws IOException {
		if (isError() != null) {

			updateorderdetails();

			task = "AddToTheOrderList " + order;
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			System.out.println(task);

			if (ChatClient.getSucceeded() == true) {
				String task = "CheckIn " + ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getRequestedpark() + " "
						+ ChatClient.o1.getNumberofvisitors();
				System.out.println(task);
				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true) {

					MessageBox.DisplayMessage("The payment was succesful", "Successfully page", "",
							AlertType.INFORMATION);

					e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
				}
			}
		}
	}

	/**
	 * Beyond credit card payment
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void PayingCreditFunc(ActionEvent event) throws IOException {
		if (isError() != null) {
			ChatClient.setTypeofCreditwindow("1");

			updateorderdetails();

			e.openPage("CreditPaymentPage1.fxml", event, "Credit Payment Page");
		}
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {

		ChatClient.logoutfunc(event);
	}

	
	/**
	 * Check that the fields are full and correct.
	 * @return
	 */
	Object isError() {
		if (EmailTxt.getText().isEmpty()) {
			MessageBox.DisplayMessage("you must write email.", "Failure page", "Error", AlertType.WARNING);
			return null;
		} else {
			if (NumberOfVisitors.getValue() == null) {

				MessageBox.DisplayMessage("you must write the number of visitors.", "Failure page", "Error",
						AlertType.WARNING);
				return null;
			} else {
				if (TypeCombo1.getValue() == null) {
					MessageBox.DisplayMessage("You must choose your order type", "DB Connection", "SQL Error",
							AlertType.WARNING);
					return null;
				} else {
					if (TypeCombo1.getValue().equals("Order for family subscription")) {
						if (Integer.parseInt(NumberOfVisitors.getValue()) > Integer
								.parseInt(ChatClient.v1.getNumOfPeople()))
							MessageBox.DisplayMessage(
									"The number you selected is greater than the number of people in this subscription.",
									"Error", "", AlertType.WARNING);
					}
				}
				return 1;
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timeofvisit.setText(ChatClient.myAtoi(t) + 1 + ":00");
		DateOfVisit.setText(currentdate.toString());
		NumberOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");
		idtxt.setText(ChatClient.E1.getUsername());
		ParkName.setText(ChatClient.E1.getOrganizationalAffiliation());

		if (ChatClient.isExistTraveler() == false) {
			TypeCombo1.getItems().addAll("Standard order");
// TypeCombo1.getItems().addAll("Order for a small group");
		} else {
			TypeCombo1.getItems().addAll("Standard order");
			if (ChatClient.v1.getType().equals("1"))
				TypeCombo1.getItems().addAll("Instructions");
			if (ChatClient.v1.getType().equals("2"))
				TypeCombo1.getItems().addAll("Order for family subscription");
		}
	}

	/**
	 * Method for storing the information requested by the user.
	 */
	public void updateorderdetails() {
		ChatClient.o1.setType(TypeCombo1.getValue());
		ChatClient.o1.setVisittime(EmailTxt.getText());
		ChatClient.o1.setNumberofvisitors(NumberOfVisitors.getValue());
		ChatClient.o1.setEmail(EmailTxt.getText());
		ChatClient.o1.setIsDone("true");
		ChatClient.o1.setVisitorID(ChatClient.v1.getVisitorID());
		ChatClient.o1.setRequestedpark(ChatClient.E1.getOrganizationalAffiliation());
		ChatClient.o1.setDateofvisit(currentdate.toString());

		ChatClient.o1.setVisittime(ChatClient.myAtoi(t) + 1 + ":00");

		if (TypeCombo1.getValue().equals("Standard order"))
			type = "StandardOrder";
		if (TypeCombo1.getValue().equals("Order for a small group"))
			type = "OrderForASmallGroup";
		if (TypeCombo1.getValue().equals("Order for family subscription"))
			type = "OrderForFamilySubscription";
		if (TypeCombo1.getValue().equals("Instructions"))
			type = "Instructions";

		MakeAnOrderPageController makeAn = new MakeAnOrderPageController();
		makeAn.final_price_calculation(ChatClient.o1);

		order = ChatClient.o1.getRequestedpark() + " " + ChatClient.o1.getDateofvisit() + " "
				+ ChatClient.o1.getVisittime() + " " + ChatClient.o1.getNumberofvisitors() + " "
				+ ChatClient.v1.getVisitorID() + " " + ChatClient.o1.getEmail() + " " + type + " " + "done" + " "
				+ ChatClient.o1.getPrice() + "true";

	}

}
