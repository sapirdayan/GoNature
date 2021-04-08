
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This controller for the credit payment
 *
 */
public class CreditPaymentController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private TextField Payments;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button Paying;

	@FXML
	private TextField CreditCardNumber;

	@FXML
	private TextField CreditCardValidity;

	@FXML
	private TextField CVVNumber;

	@FXML
	private Label pricelabel;

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * This function returns back to Home page employees
	 * 
	 * @param event on action event
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
	}

	/**
	 * This function for the payment if type of credit window = 1 thats mean its a
	 * unplanned visit and must add it to order list and checking if it succeeded or
	 * not1
	 * 
	 * @param event on action
	 * @throws Exception CLIENT EXCEPTION
	 */
	@FXML
	void Paying1Func(ActionEvent event) throws Exception {
		if (!isError().equals(null)) {
			ChatClient.setSucceeded(true);
			if (ChatClient.getTypeofCreditwindow().equals("1")) {
				ChatClient.setSucceeded(false);
				String task = "AddToTheOrderList " + UnplannedVisitController.order;
				ClientUI.chat.accept(task);
			}

			if (ChatClient.getSucceeded() == true) {
				String task = "CheckIn " + ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getRequestedpark() + " "
						+ ChatClient.o1.getNumberofvisitors();
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
	 * This object check if all the input values are correct and displaying a
	 * suitable message in case there were a problem.
	 * 
	 * @return true if all the inputs are correct,else null
	 */
	Object isError() {
		if (Payments.getText().isEmpty()) {
			MessageBox.DisplayMessage("you must write the The number of payments.", "DB Connection", "SQL Error",
					AlertType.WARNING);

			return null;
		} else {
			if (CreditCardNumber.getText().isEmpty()) {
				MessageBox.DisplayMessage("you must write the The Credit Card Number.", "DB Connection", "SQL Error",
						AlertType.WARNING);

				return null;

			} else {
				if (CreditCardValidity.getText().isEmpty()) {
					MessageBox.DisplayMessage("You must enter the validity of the card.", "DB Connection", "SQL Error",
							AlertType.WARNING);

					return null;
				} else {
					if (CVVNumber.getText().isEmpty()) {
						MessageBox.DisplayMessage("you must write the CVV number.", "DB Connection", "SQL Error",
								AlertType.WARNING);

						return null;
					} else {
						return 1;
					}
				}
			}
		}
	}

	/**
	 * This function initialize the price for the payment
	 * 
	 * @param location  location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CVVNumber.setTooltip(new Tooltip("three digits on the back of the card"));
		idtxt.setText(ChatClient.E1.getUsername());

		if (!ChatClient.getTypeofCreditwindow().equals("1")) {
			if (ChatClient.o1.getGapPrice().equals("-1"))
				pricelabel.setText(ChatClient.o1.getPrice());
			else
				pricelabel.setText(ChatClient.o1.getGapPrice());
		} else
			pricelabel.setText(ChatClient.o1.getPrice());
	}
}
