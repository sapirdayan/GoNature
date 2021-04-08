package gui;


import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import logic.Order;

public class SeccfullyPageController implements Initializable {

	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();

	@FXML
	private Label idtxt;

	@FXML
	private Label logout;

	@FXML
	private Button CriedtCardBtn;

	@FXML
	private Button BackToHomePagebtn;

	@FXML
	private Label TotalPrice;

	public static String task = null;

	@FXML
	void BackFunc(ActionEvent event) throws Exception {

		if (ChatClient.getTypeofWindow().equals("1")) {

			t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

		} else {
			task = "AddToTheOrderList " + MakeAnOrderPageController.order + " "
					+ MakeAnOrderPageController.o1.getPrice() + "false";
			System.out.println(task);
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.getSucceeded() == true) {
				String s = "It's yours\n" + "The invitation has been accepted.\n" + "Order number: "
						+ ChatClient.o1.getNoOfOrder()
						+ "\nYou will need to display it at the entrance and exit of the park.";
				MessageBox.DisplayMessage(s, "Successfully page", "", AlertType.INFORMATION);

				t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

			} else
				MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
		}
	}

	/**
	 * Simulat for making the payment on the website in advance via the credit card
	 * 
	 * @param event on ActionEvent
	 * @throws Exception
	 */
	@FXML
	void CriedtCardFunc(ActionEvent event) throws Exception {
		Boolean istrue = false;
		System.out.println("Tracking2");

		if (MakeAnOrderPageController.o1.getType().equals("Instructions")) {
			ChatClient.totalPrice = ChatClient.myAtoi(ChatClient.totalPrice)
					- ((ChatClient.myAtoi(ChatClient.totalPrice) * 12) / 100) + " ";
			ChatClient.o1.setPrice(ChatClient.totalPrice);

			MakeAnOrderPageController.o1.setPrice(ChatClient.totalPrice);

			if (ChatClient.getTypeofWindow().equals("1")) {
				task = "UpdateOrder " + ChatClient.o1.getRequestedpark() + " " + ChatClient.o1.getNoOfOrder() + " "
						+ ChatClient.o1.getPrice() + "true";

			} else {
				task = "AddToTheOrderList " + MakeAnOrderPageController.order + " "
						+ MakeAnOrderPageController.o1.getPrice() + " true";
			}

			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server

			if (ChatClient.getSucceeded() == true) {

				String s = "The payment was succesful, you got another 12% sale because your are a guide .\n The total price is "
						+ ChatClient.totalPrice + "\n" + "Order number: " + ChatClient.o1.getNoOfOrder()
						+ "\nYou will need to display it at the entrance and exit of the park.";

				MessageBox.DisplayMessage(s, "Another sale", "congratulations", AlertType.INFORMATION);

				System.out.println("the total price is :________" + ChatClient.totalPrice);
				istrue = true;
			} else
				MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
		} else {
			if (ChatClient.getTypeofWindow().equals("1")) {
				System.out.println("Tracking8");
				task = "UpdateOrder " + ChatClient.o1.getRequestedpark() + " " + ChatClient.o1.getNoOfOrder() + " "
						+ ChatClient.o1.getPrice() + " true";
			} else {
				task = "AddToTheOrderList " + MakeAnOrderPageController.order + " "
						+ MakeAnOrderPageController.o1.getPrice() + "true";
			}

			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.getSucceeded() == true) {
				System.out.println("Tracking9");
				String s = "It's yours\n" + "The payment was succesful.\n" + "Order number: "
						+ ChatClient.o1.getNoOfOrder()
						+ "\nYou will need to display it at the entrance and exit of the park.";
				MessageBox.DisplayMessage(s, "Successfully page", "", AlertType.INFORMATION);

			} else
				MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
		}

		if (ChatClient.getSucceeded() == true) {

			t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

		} else
			MessageBox.DisplayMessage("Something wrong", "ERROR", "SQL error", AlertType.INFORMATION);
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {

		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(TravelersIdentificationPage1Controller.Idnumber);
		TotalPrice.setText(ChatClient.totalPrice);

	}

	/**
	 * Method responsible for displaying a pop-up window
	 * 
	 * @param o1 -the order
	 */
	public void showmessage(Order o1) {
		if (o1.getVisitorID() != null && o1.getEmail() != null) {
			String message = "The message:\r\n" + "Order number " + o1.getNoOfOrder() + " was successfully received\r\n"
					+ "Sent to phone number: " + ChatClient.v1.getPhoneNumber() + " and email address: "
					+ o1.getEmail();
			System.out.println("The message is: " + message);
			System.out.println("11");
			JOptionPane.showMessageDialog(null, message);
			System.out.println("22");
		}
	}

}
