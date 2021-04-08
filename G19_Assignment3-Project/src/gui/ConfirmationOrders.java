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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * This class, operates 2 screens, One for booking confirmation after a message
 * has been sent to the traveler. (If he has an appointment for the day after).
 * One to confirm the booking after it was on the waiting list and a seat became
 * available.
 * 
 * @author Nasmat Amr
 *
 */
public class ConfirmationOrders implements Initializable {

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	private Pane panewaitinglist;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button DeleteBtn;

	@FXML
	private Button confirmbtn;

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtn1;

	@FXML
	private ComboBox<String> comboorder;

	@FXML
	private Button CancelBtn1;

	@FXML
	private Label email;

	@FXML
	private Label VisitingTime;

	@FXML
	private Label NumberOfVisitors;

	@FXML
	private Label DateOfVisit;

	@FXML
	private Label ParkName;

	@FXML
	private Label Idnumber;

	@FXML
	private Label TotalOfprice;

	@FXML
	private Label TotalOfprice1;

	@FXML
	private Label Email1;

	@FXML
	private Label NumberOfVisitors1;

	@FXML
	private Label DateOfVisit1;

	@FXML
	private Label ParkName1;

	@FXML
	private Label Idnumber1;

	@FXML
	private Label VisitingTime1;

	@FXML
	private Label OrderType;

	@FXML
	private Label OrderType1;

	@FXML
	private Label title;

	/**
	 * If the traveler decides not to cancel the reservation. Clicking the delete
	 * button activates this method. You have sent a request to the server to delete
	 * the order. And takes care to check if there is room for someone else in line.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void DeleteFunc(ActionEvent event) throws Exception {
		String task = HomePageForTravelersController.query2 + comboorder.getValue() + " "
				+ ChatClient.o1.getNumberofvisitors();
		System.out.println("666 task: " + task);
		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task);
		if (ChatClient.getSucceeded() == true) {
			String str = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
					+ ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getDateofvisit() + " "
					+ ChatClient.o1.getVisittime() + " " + ChatClient.o1.getNumberofvisitors();
			task = "CheckInWaitingList " + str;
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server

			if (ChatClient.getSucceeded().equals(true)) {
				MessageBox.DisplayMessage("Your Order was deleted!.", "", "", AlertType.INFORMATION);

				e.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
			}
		} else
			MessageBox.DisplayMessage("Something wrong.", "", "", AlertType.INFORMATION);
	}

	/**
	 * A method that takes us back to the home page of a department manager
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		e.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
	}

	/**
	 * If the traveler decides to confirm the reservation. Clicking the OK button
	 * activates this method. You have sent a request to the server to update that
	 * the order has been confirmed. And if the booking has not yet been paid, the
	 * traveler will have to go to the payment screen.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void ConfirmFunc(ActionEvent event) throws Exception {

		String task = HomePageForTravelersController.query1 + comboorder.getValue() + " "
				+ ChatClient.o1.getNumberofvisitors();
		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task);
		if (ChatClient.getSucceeded() == true) {
			if (HomePageForTravelersController.query1.equals("UpdateOrderToConfirm1 ")) {
				MessageBox.DisplayMessage("Order confirmed!\r\n" + "Looking forward to seeing you tomorrow.", "", "",
						AlertType.INFORMATION);

				e.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
			} else {
				ChatClient.setTypeofWindow("1");

				e.openPage("OrderSuccessfullyReceivedPage.fxml", event, "Order successfully received page");
			}
		} else
			MessageBox.DisplayMessage("Something wrong.", "", "", AlertType.INFORMATION);
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * A method that displays the data of the order on the screen
	 * 
	 * @param event
	 */
	@FXML
	void waitinglistfunc(ActionEvent event) {
		String task2 = HomePageForTravelersController.query + comboorder.getValue();
		System.out.println("task2: " + task2);
		ClientUI.chat.accept(task2);
		if (ChatClient.ForApproval == true) {
			confirmbtn.setDisable(false);
			DeleteBtn.setDisable(false);
		} else
			DeleteBtn.setDisable(false);

		ParkName.setText("The park name is: ");
		OrderType.setText("Order type: ");
		VisitingTime.setText("visiting time: ");
		NumberOfVisitors.setText("number of visitors: ");
		DateOfVisit.setText("Date of visit: ");
		Idnumber.setText("ID number: ");
		email.setText("Email: ");
		TotalOfprice.setText("Total of price: ");

		ParkName1.setText(ChatClient.InformationToTheOrders[0]);
		OrderType1.setText(ChatClient.InformationToTheOrders[1]);
		VisitingTime1.setText(ChatClient.InformationToTheOrders[2]);
		NumberOfVisitors1.setText(ChatClient.InformationToTheOrders[3]);
		DateOfVisit1.setText(ChatClient.InformationToTheOrders[4]);
		Idnumber1.setText(ChatClient.InformationToTheOrders[5]);
		Email1.setText(ChatClient.InformationToTheOrders[6]);
		TotalOfprice1.setText(ChatClient.InformationToTheOrders[7]);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(TravelersIdentificationPage1Controller.Idnumber);

		System.out.println("@111");
		title.setText(HomePageForTravelersController.nameofpage);
		for (int j = 0; j < ChatClient.arrayesOfOrdersInWaitingList.length; j++) {
			comboorder.getItems().add(ChatClient.arrayesOfOrdersInWaitingList[j]);
		}
		ChatClient.arrayesOfOrdersInWaitingList = null;

	}
}
