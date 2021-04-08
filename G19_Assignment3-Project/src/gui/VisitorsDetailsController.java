package gui;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * In this class Take all user data that is not stored in our system and store
 * it in the database
 * 
 * @author hana
 *
 */
public class VisitorsDetailsController {
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
	@FXML
	private TextField txtMail;

	@FXML
	private TextField txtFname;

	@FXML
	private TextField txtLname;

	@FXML
	private TextField txtVisitorID;

	@FXML
	private TextField txtPhoneNumber;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button OkBtn;

	/**
	 * In this func we return to the traveler home page
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		e.openPage("HomePageForNewTravelers1.fxml", event, "Home page for traveler");
	}

	/**
	 * In this func we add visitor to the visitor list and also its order to the
	 * order list
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void OkFunc(ActionEvent event) throws Exception {
		if (isError() != null) {

			String info = txtFname.getText() + " " + txtLname.getText() + " " + txtVisitorID.getText() + " "
					+ txtMail.getText() + " " + txtPhoneNumber.getText();
			String task = "AddToVisitorsList " + info;
			System.out.println(task);
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task);
			if (ChatClient.getSucceeded().equals(true)) {

				if (ChatClient.getTypeofwindow().equals("1")) {
					task = "EnterToWaitingList " + ChatClient.v1.getVisitorID() + " " + MakeAnOrderPageController.order
							+ " " + MakeAnOrderPageController.o1.getPrice();
					System.out.println(task);
					ChatClient.setSucceeded(false);
					ClientUI.chat.accept(task); // send message from client to server
					System.out.println("tred:: " + ChatClient.getSucceeded());
					if (ChatClient.getSucceeded().equals(true)) {
						MessageBox.DisplayMessage("You have been successfully placed on the waiting list",
								"Successfully page", "", AlertType.INFORMATION);

						e.openPage("HomePageForNewTravelers1.fxml", event, "Home page for traveler");
					} else
						MessageBox.DisplayMessage("You are already on the waiting list for the requested time.",
								"SQL Error", "", AlertType.ERROR);

				} else {
					e.openPage("OrderSuccessfullyReceivedPage.fxml", event, "Order Successfully Received Page");
				}
			}
		}
	}

	/**
	 * In thie func we check if all the fields are valid and not empty
	 * 
	 * @return
	 */
	@FXML
	Object isError() {
		if (txtFname.getText().isEmpty()) {
			MessageBox.DisplayMessage("you must write your first name", "DB Connection", "SQL Error",
					AlertType.WARNING);
			return null;
		} else {
			if (txtLname.getText().isEmpty()) {
				MessageBox.DisplayMessage("you must write your last name.", "DB Connection", "SQL Error",
						AlertType.WARNING);
				return null;
			} else {
				if (txtMail.getText().isEmpty()) {
					MessageBox.DisplayMessage("you must write your Email.", "DB Connection", "SQL Error",
							AlertType.WARNING);
					return null;
				} else {
					if (txtVisitorID.getText().isEmpty()) {
						MessageBox.DisplayMessage("You must write your ID number", "DB Connection", "SQL Error",
								AlertType.WARNING);
						return null;
					} else {
						if (txtPhoneNumber.getText().isEmpty()) {
							MessageBox.DisplayMessage("You must write your phone number", "DB Connection", "SQL Error",
									AlertType.WARNING);
							return null;
						} else {
							return 1;
						}
					}
				}
			}
		}
	}

}
