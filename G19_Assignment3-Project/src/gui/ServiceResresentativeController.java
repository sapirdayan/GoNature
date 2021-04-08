package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The department is responsible for the home page of a service representative
 * 
 * @author hana
 *
 */
public class ServiceResresentativeController implements Initializable {

	@FXML
	private ImageView ContactUsBtn;

	@FXML
	private Label Guide;

	@FXML
	private Label FamilySubScription;

	@FXML
	private Label LogoutBtn;

	@FXML
	private Label idtxt;

	EmployeeIdentificationPage1Controller employee = new EmployeeIdentificationPage1Controller();

	@FXML
	void ContactUsFunc(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Parent root = FXMLLoader.load(getClass().getResource("ContactUs.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Contact us page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Opening a family subscription registration page for the system
	 * 
	 * @param event on MouseEvent
	 * @throws IOException
	 */
	@FXML
	void FamilySubScriptionFunc(MouseEvent event) throws IOException {
		employee.openPage("CreateFamilySubscriptionPage1.fxml", event, "Creation a family subscription page");
	}

	/**
	 * Open a system directory registration page
	 * 
	 * @param event on MouseEvent
	 * @throws IOException
	 */
	@FXML
	void GuideFunc(MouseEvent event) throws IOException {

		employee.openPage("RegisterAsGuidePage1.fxml", event, "Registeration as a guide page");

	}

	@FXML
	void LogoutFunc(MouseEvent event) throws Exception {

		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());

	}

}
