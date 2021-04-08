package gui;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Landing page controller
 *
 */
public class LandingPageContoller {

	@FXML
	private Button EmployeeBtn;

	@FXML
	private Button TravelerBtn;

	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();

	/**
     * To open EmployeeIdentificationPage
     * @param event on action
     * @throws Exception client exception
     */
    @FXML
    void EmployeeFunc(ActionEvent event) throws Exception {
    ChatClient.isEmployee=true;
    ChatClient.isTraveler=false;
    
    t.openPage("EmployeeIdentificationPage1.fxml", event, "EmployeeIdentificationPage");
    
    }

	/**
	 * To open TravelersIdentificationPage
	 * 
	 * @param event on action
	 * @throws Exception client exception
	 */
	@FXML
	void TravelerFunc(ActionEvent event) throws IOException {
		ChatClient.isTraveler = true;
		ChatClient.isEmployee = false;
		ChatClient.setTypeofwindow("1");

		t.openPage("TravelersIdentificationPage2.fxml", event, "TravelersIdentificationPage");

	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Landing Page");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
