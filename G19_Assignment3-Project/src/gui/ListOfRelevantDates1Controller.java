
package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * List of relevant controller
 *
 */

public class ListOfRelevantDates1Controller implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private DatePicker Dates;

	@FXML
	private Button Showbtn;

	@FXML
	private Button BackBtn;

	@FXML
	private ComboBox<String> TimeCombo;

	@FXML
	private Button ContinueBtn1;

	MakeAnOrderPageController makeAn = new MakeAnOrderPageController();

	/**
	 * backing to selection page
	 * 
	 * @param event on action
	 * @throws IOException client exception
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("SelectionPageIfThereIsNoSpace1.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Selection Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * making an order by set suitable date and time
	 * 
	 * @param event on action
	 * @throws Exception client exception
	 */
	@FXML
	void MakeAnOrderFunc(ActionEvent event) throws Exception {
		if (isError2() == true) {
			MakeAnOrderPageController.o1.setDateofvisit(Dates.getValue().toString());
			MakeAnOrderPageController.o1.setVisittime(TimeCombo.getValue().toString());
			makeAn.MakeAnOrderFunc(MakeAnOrderPageController.o1, event);
		}
	}

	/**
	 * send message from client to server to return if there is a vacancy or not
	 * with suitable message
	 * 
	 * @param event on action
	 */
	@FXML
	void ShowRelevantTimes(ActionEvent event) {
		if (isError1() == true) {
			String date = Dates.getValue().toString();

			String task = "ReturnRelevantTimesToThisDate " + MakeAnOrderPageController.o1.getRequestedpark() + " "
					+ date + " " + MakeAnOrderPageController.o1.getNumberofvisitors();
			ChatClient.setSucceeded(false);
			System.out.println(task);
			ClientUI.chat.accept(task); // send message from client to server

			if (ChatClient.getSucceeded().equals(true)) {
				if (ChatClient.arrayTimes.get(0).equals("null")) {
					MessageBox.DisplayMessage("There is no place for the desired number of people on this day.",
							"Error", "Please select a different date.", AlertType.WARNING);
				} else
					for (int i = 0; i < ChatClient.arrayTimes.size(); i++) {
						TimeCombo.getItems().addAll(ChatClient.arrayTimes.get(i));
					}
				ChatClient.arrayTimes.clear();
			} else
				MessageBox.DisplayMessage("Something wrong", "SQL Error", "", AlertType.ERROR);
		}

	}

	/**
	 * This function logs out and returns to the landing page
	 * 
	 * @param event on mouse click
	 * @throws Exception client exception
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
	Boolean isError1() {
		LocalDate relevantDate = MakeAnOrderPageController.DateOfVisit1;
		LocalDate currentdate = LocalDate.now();

		if (Dates.getValue() == null || Dates.getValue().isBefore(currentdate)
				|| Dates.getValue().isAfter(relevantDate.plusWeeks(1))) {
			MessageBox.DisplayMessage("Choose a date (from today to 7 days)" + ".", "DB Connection", "SQL Error",
					AlertType.WARNING);
			return false;

		} else
			return true;
	}

	Boolean isError2() {
		if (TimeCombo.getValue() == null) {
			MessageBox.DisplayMessage("You must choose an hour", "DB Connection", "SQL Error", AlertType.WARNING);
			return false;
		} else
			return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
	}
}
