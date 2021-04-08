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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * Class in charge of a page if there was no space available
 * 
 * @author hana
 *
 */
public class SelectionPageIfThereIsNoSpaceController implements Initializable {
	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();
	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button WaitingListBtn;

	@FXML
	private Button DifferentDateBtn;

	@FXML
	private Button HomeBtn;

	/**
	 * return to relevant dates to the user that That match his order details
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void DifferentDateFunc(ActionEvent event) throws IOException {
		t.openPage("ListOfRelevantDates1.fxml", event, "List of relevant dates");

	}

	/**
	 * return user to the home page
	 * 
	 * @param event on ActionEvent
	 * @throws Exception
	 */
	@FXML
	void HomeFunc(ActionEvent event) throws Exception {

		t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
	}

	/**
	 * enter user to the waiting list in data base
	 * 
	 * @param event on ActionEvent
	 * @throws Exception
	 */
	@FXML
	void WaitingListFun(ActionEvent event) throws Exception {
		if (ChatClient.isExistTraveler() == false) {
			ChatClient.setTypeofwindow("1");

			t.openPage("VisitorDetails.fxml", event, "Visitor details page");
		} else {
			String task = "EnterToWaitingList " + ChatClient.v1.getVisitorID() + " " + MakeAnOrderPageController.order
					+ " " + MakeAnOrderPageController.o1.getPrice();
			System.out.println(task);
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			System.out.println("tred:: " + ChatClient.getSucceeded());
			if (ChatClient.getSucceeded().equals(true)) {
				MessageBox.DisplayMessage("You have been successfully placed on the waiting list", "Successfully page",
						"", AlertType.INFORMATION);

				t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

			} else
				MessageBox.DisplayMessage("You are already on the waiting list for the requested time.", "SQL Error",
						"", AlertType.ERROR);
		}
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {

		ChatClient.logoutfunc(event);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(TravelersIdentificationPage1Controller.Idnumber);

	}

}
