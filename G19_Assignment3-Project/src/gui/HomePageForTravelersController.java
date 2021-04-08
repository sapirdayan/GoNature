package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Traveler's Home Page. Which lists all the interactions that the traveler can
 * perform in the system.
 * 
 * @author Nasmat Amr
 *
 */
public class HomePageForTravelersController implements Initializable {
	@FXML
	private Button btnContactUs1;

	@FXML
	private Button btnContactUs2;

	@FXML
	private Label logout;

	@FXML
	private ImageView Facebook;

	@FXML
	private ImageView Insta;

	@FXML
	private ImageView ContactUs1;

	@FXML
	private ImageView ContactUs2;

	@FXML
	private Label EditingAnOrder;

	@FXML
	private Label MakeAnOrder1;

	@FXML
	private Label MakeAnOrder2;

	@FXML
	private Label AboutUs1;

	@FXML
	private Label AboutUs2;

	@FXML
	private Button BackBtn;

	@FXML
	private Button homebtn1;

	@FXML
	private Label label11;

	@FXML
	private Label idtxt;

	@FXML
	private Label Confirmbtn;

	public static String nameofpage = "";
	public static String query = "";
	public static String query1 = "";
	public static String query2 = "";
	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * A method that requests information from the user for the relevant orders that
	 * are awaiting approval. And saves them for use on the screen you switch to by
	 * clicking the Confirm Order button.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ConfirmationFunc(MouseEvent event) throws IOException {
		String task = "SendMeOrdersToConfirm " + ChatClient.v1.getVisitorID();
		System.out.println(task);
		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task);
		if (ChatClient.getSucceeded() == true) {
			nameofpage = "Orders that require confirmation";
			query = "SentMeTheOrderDetails ";
			query1 = "UpdateOrderToConfirm1 ";
			query2 = "UpdateOrderToDeleted1 ";
			t.openPage("WaitingListPage.fxml", event, "Orders that require confirmation");
		} else
			MessageBox.DisplayMessage("There Is Not Orders", "", "", AlertType.WARNING);

	}

	@FXML
	void BackFunc(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		System.out.println(ChatClient.isTraveler);
		System.out.println(ChatClient.isEmployee);
		if (ChatClient.isTraveler == true)
			e.openPage("HomePageForTravelers1.fxml", event, "Home Page for travelers");
		else {
			switch (ChatClient.E1.getRole()) {
			case "1":
				e.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
				break;
			case "2":
				e.openPage("HomePageDepartmentManeger1.fxml", event, "Home Page Department Maneger");
				break;
			case "3":
				e.openPage("HomePageParkManeger1.fxml", event, "Home Page For Park Maneger");
				break;
			case "4":
				e.openPage("HomePageForEmplyees1.fxml", event, "HomePage For Emplyees");
				break;
			}
		}

	}

	/**
	 * A method that takes us to the screen for editing / deleting an order.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void EditingAnOrderFunc(MouseEvent event) throws IOException {
		t.openPage("EditAnOrder.fxml", event, "Edit an order");
	}

	/**
	 * A method that takes us to a group information page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void AboutAsFunc(MouseEvent event) throws IOException {
		t.openPage("AboutUs1.fxml", event, "About us page");
	}

	/**
	 * A method that takes us to a contact page for a company.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ContactUsFunc(MouseEvent event) throws IOException {
		t.openPage("ContactUs.fxml", event, "Contact us page");
	}

	/**
	 * A method is activated by clicking on the Facebook icon, and it opens our
	 * Facebook page for us.
	 * 
	 * @param event
	 */
	@FXML
	void FaceBookFunc(MouseEvent event) {
		Stage stage = new Stage();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				String task = "UpdateConnected " + ChatClient.v1.getVisitorID();
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true) {
					Platform.exit();
				}
			}
		});
		WebView web = new WebView();
		web.getEngine().load("https://www.facebook.com/goo.naturee");
		Scene scene = new Scene(web);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * A method that takes us to the screen to confirm orders that will make room
	 * for them from the waiting list.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void WaitingListFunc(MouseEvent event) throws IOException {
		String task = "SendMeOrdersInWaitingListToThisId " + ChatClient.v1.getVisitorID();
		System.out.println(task);
		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task);
		if (ChatClient.getSucceeded() == true) {
			nameofpage = "Waiting list page";
			query = "SentMeAllTheInfoToThisOrder ";
			query1 = "UpdateOrderToConfirm ";
			query2 = "UpdateOrderToDeleted ";
			t.openPage("WaitingListPage.fxml", event, "Waiting list page");
		} else
			MessageBox.DisplayMessage("There Is No Orders", "", "", AlertType.WARNING);

	}

	/**
	 * A method is activated by clicking on the instagram icon, and it opens our
	 * instagram page for us.
	 * 
	 * @param event
	 */
	@FXML
	void InstaFunc(MouseEvent event) {
		Stage stage = new Stage();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				String task = "UpdateConnected " + ChatClient.v1.getVisitorID();
				ClientUI.chat.accept(task); // send message from client to server
				if (ChatClient.getSucceeded() == true) {
					Platform.exit();
				}
			}
		});
		WebView web = new WebView();
		web.getEngine().load("https://www.instagram.com/g0nature/");
		Scene scene = new Scene(web);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * A method that takes us to the screen to place an order.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void MakeAnOrderFunc(MouseEvent event) throws IOException {
		t.openPage("MakeAnOrderPage.fxml", event, "Make an order page");

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
