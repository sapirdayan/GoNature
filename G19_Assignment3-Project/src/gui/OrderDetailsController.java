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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


/**
 * The department responsible for presenting to the park employee the details of
 * the booking of the relevant hiker
 * 
 * @author hana
 *
 */
public class OrderDetailsController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private TextField RequestedParkTxt;

	@FXML
	private TextField DateOfVisitTxt;

	@FXML
	private TextField VisitingTimeTxt;

	@FXML
	private TextField NumberOfVisitorsTxt;

	@FXML
	private TextField EmailTxt;

	@FXML
	private TextField PaymentTxt;

	@FXML
	private ComboBox<?> TypeCombo1;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button PayingCash;

	@FXML
	private Button PayingCredit;
	@FXML
	private Label date;

	@FXML
	private Label paidup;

	@FXML
	private Label pay;

	@FXML
	private Label type;

	@FXML
	private Label email;

	@FXML
	private Label noofpeople;

	@FXML
	private Label time;

	@FXML
	private Label OrderNumber;

	@FXML
	private Button CheckIn;

	@FXML
	private Label reqpark;

	@FXML
	private TextField PaymentTxt1;

	@FXML
	private Label GapPrice;

	String payment = null;
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * Department responsible for credit payment at the entrance to the park
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void PayingCashFunc(ActionEvent event) throws IOException {
		System.out.println("$1");

		ChatClient.setSucceeded(false);
		String task = "CheckIn " + ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getRequestedpark() + " "
				+ ChatClient.o1.getNumberofvisitors();
		System.out.println(task);
		ClientUI.chat.accept(task); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			System.out.println("$2");

			if (paidup.getText().equals("YES")) {
				System.out.println("$3");

				System.out.println(task);
				MessageBox.DisplayMessage("The order has already been paid.", "Successfully page", "",
						AlertType.INFORMATION);

				e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");

			} else {
				System.out.println("$4");

				System.out.println(task);
				MessageBox.DisplayMessage("The payment was succesful", "Successfully page", "", AlertType.INFORMATION);
				e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
			}
		} else
			MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
	}

	@FXML
	void CheckInFunc(ActionEvent event) throws IOException {
		System.out.println("1");
		System.out.println("2");

		if (!paidup.getText().equals("YES")) {
			System.out.println("3");
			MessageBox.DisplayMessage("The order has not yet been paid.", "Error", "", AlertType.INFORMATION);
		} else {
			System.out.println("4");
			String task = "CheckIn " + ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getRequestedpark() + " "
					+ ChatClient.o1.getNumberofvisitors();
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.getSucceeded() == true) {
				System.out.println("5");
				MessageBox.DisplayMessage("Check-in was successful", "Successfully page", "", AlertType.INFORMATION);
				e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
			} else
				MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
		}

	}

	@FXML
	void PayingCreditFunc(ActionEvent event) throws IOException {

		if (paidup.getText().equals("YES")) {

			MessageBox.DisplayMessage("The order has already been paid.", "Successfully page", "",
					AlertType.INFORMATION);
			e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
		} else {
			System.out.println("#5");
			ChatClient.setTypeofCreditwindow("2");
			e.openPage("CreditPaymentPage1.fxml", event, "Credit Payment Page");

		}

	}

	@FXML
	void CancelFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyeess");
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {

		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		System.out.println("ChatClient.o1.getIsPaid(): " + ChatClient.o1.getIsPaid());
		type.setText(ChatClient.o1.getType());
		OrderNumber.setText(ChatClient.o1.getNoOfOrder());
		reqpark.setText(ChatClient.o1.getRequestedpark());
		date.setText(ChatClient.o1.getDateofvisit());
		time.setText(ChatClient.o1.getVisittime());
		noofpeople.setText(ChatClient.o1.getNumberofvisitors());
		email.setText(ChatClient.o1.getEmail());
		pay.setText(ChatClient.o1.getPrice());
		if (ChatClient.o1.getGapPrice().equals("-1"))
			if (ChatClient.o1.getIsPaid().equals("false"))
				GapPrice.setText(ChatClient.o1.getPrice());
			else
				GapPrice.setText("0");
		else
			GapPrice.setText(ChatClient.o1.getGapPrice());

		if (ChatClient.o1.getIsPaid().equals("true"))
			paidup.setText("YES");
		else
			paidup.setText("NOT YET");

	}

}
