package gui;

import java.io.IOException;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Department responsible for a traveler's identification page for the system
 * 
 * @author hana
 *
 */
public class TravelersIdentificationPage1Controller {

	@FXML
	private Button EmployeeBtn;

	@FXML
	private Button TravelerBtn;

	@FXML
	private Pane pane1;

	@FXML
	private TextField idtxt;

	@FXML
	private Button SendBtn;

	@FXML
	private Button Cancelbtn;

	public static String Idnumber = "";
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		if (ChatClient.getTypeofwindow().equals("2")) {

			e.openPage("HomePageForEmplyees1.fxml", event, "home page for emplyees");
		} else if (ChatClient.getTypeofwindow().equals("1")) {
			openPage("LandingPage.fxml", event, "Landing page");

		}

	}

	private String getId() {
		return idtxt.getText();
	}

	/**
	 * Sending the parameters to the database for testing
	 * 
	 * @param event on ActionEvent
	 * @throws Exception
	 */
	@FXML
	void SendFunc(ActionEvent event) throws Exception {
		if (isError() != null) {

			if (ChatClient.typeofIdentificationwindow.equals("1")) {
				Idnumber = getId();
				String task = "indeftifictionTraveler " + Idnumber;
				System.out.println(task);
				ClientUI.chat.accept(task);
				System.out.println("ChatClient.IsConnected: " + ChatClient.IsConnected);
				if (ChatClient.IsConnected == true) {

					openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

				} else
					MessageBox.DisplayMessage(
							"You can not connect because you are already connected to the system elsewhere.", "Error",
							"", AlertType.ERROR);

			} else if (ChatClient.typeofIdentificationwindow.equals("2")) {
				ChatClient.setTypeofVacancywindow("1");

				Idnumber = getId();
				String task = "justindeftifictionTraveler " + Idnumber;
				System.out.println(task);
				ClientUI.chat.accept(task);
				openPage("UnplannedOrder.fxml", event, "Unplanned Visit Page");

			}
		}
	}

	/**
	 * Open relevant page and update the status of the traveler to connect
	 * 
	 * @param nameOfPage
	 * @param event      on Event
	 * @param name
	 * @throws IOException
	 */
	void openPage(String nameOfPage, Event event, String name) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Parent pane = FXMLLoader.load(getClass().getResource(nameOfPage));
		Stage primaryStage = new Stage();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				String task = "UpdateConnected " + ChatClient.v1.getVisitorID();
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true) {
					Platform.exit();
				}
			}
		});
		Scene scene = new Scene(pane);
		primaryStage.setTitle(name);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Check fields
	 * 
	 * @return
	 */
	Object isError() {
		if (idtxt.getText().isEmpty()) {
			MessageBox.DisplayMessage("You must enter an ID number.", "Error window", "Error", AlertType.INFORMATION);
			return null;
		} else
			return 1;

	}

}
