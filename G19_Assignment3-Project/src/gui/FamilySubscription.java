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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseDragEvent;


/**
 * A class used by us to operate a travel registration screen as a family
 * subscription in the system.
 * 
 * @author Nasmat Amr
 *
 */
public class FamilySubscription implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private TextField FNametxt;

	@FXML
	private TextField LNametxt;

	@FXML
	private TextField PhoneNumTxt;

	@FXML
	private TextField EmailTxt;

	@FXML
	private TextField NumTxt;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button AddBtn;

	@FXML
	private TextField CreditcardTxt;

	@FXML
	private TextField creditExptxt;

	@FXML
	private TextField IDnumtxt;

	@FXML
	private TextField txtcvv;
	@FXML
	private ImageView help;

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * Check if the user is already registered as a subscriber in the system, if he
	 * is presented with an error message, otherwise a successful registration
	 * message in DB.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void AddFunc(ActionEvent event) throws IOException {
		if (checkFields() == true) {
			String info = "";
			if (CreditcardTxt.getText().isEmpty()) {
				info = FNametxt.getText().toString() + " " + LNametxt.getText().toString() + " " + IDnumtxt.getText()
						+ " " + PhoneNumTxt.getText().toString() + " " + EmailTxt.getText().toString() + " "
						+ NumTxt.getText().toString() + " " + null + " " + null + " " + null;

				String task = "RegisterationAsFamilySubscription " + info;
				System.out.println("task: " + task);
				ClientUI.chat.accept(task); // send message from client to server
				ChatClient.NotExistGuide = true;
				if (ChatClient.NotExistfamiltsubscription == true) {
					MessageBox.DisplayMessage("You have successfully registered the user as a family subscription",
							"Sign up successfullys", "Sign up successfully", AlertType.INFORMATION);
					e.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
				} else
					MessageBox.DisplayMessage("The user is already registered as family subscription!", "Error",
							"Error registering as family subscription", AlertType.WARNING);

			} else if (checkFieldsCreditCard() == true) {

				info = FNametxt.getText().toString() + " " + LNametxt.getText().toString() + " " + IDnumtxt.getText()
						+ " " + PhoneNumTxt.getText().toString() + " " + EmailTxt.getText().toString() + " "
						+ NumTxt.getText().toString() + " " + CreditcardTxt.getText().toString() + " "
						+ creditExptxt.getText().toString() + " " + txtcvv.getText();

				String task = "RegisterationAsFamilySubscription " + info;
				System.out.println("task: " + task);
				ClientUI.chat.accept(task); // send message from client to server
				ChatClient.NotExistGuide = true;
				if (ChatClient.NotExistfamiltsubscription == true) {
					MessageBox.DisplayMessage("You have successfully registered the user as a family subscription",
							"Sign up successfullys", "Sign up successfully", AlertType.INFORMATION);
					e.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
				} else
					MessageBox.DisplayMessage("The user is already registered as family subscription!", "Error",
							"Error registering as family subscription", AlertType.WARNING);
			}

		}

	}

	/**
	 * Check that all required fields are filled.
	 * 
	 * @return true if are filled. else if not
	 */
	private boolean checkFields() {
		if (FNametxt.getText().isEmpty()) {
			MessageBox.DisplayMessage("You must enter your first name", "Error", "Errot in first name",
					AlertType.WARNING);
			return false;
		} else {
			if (LNametxt.getText().isEmpty()) {
				MessageBox.DisplayMessage("you must enter last name", "Error", "Errot in last name", AlertType.WARNING);
				return false;

			} else {
				if (PhoneNumTxt.getText().isEmpty()) {
					MessageBox.DisplayMessage("you must enter your phone number.", "Error", "Error in phone number",
							AlertType.WARNING);
					return false;

				} else {
					if (EmailTxt.getText().isEmpty()) {
						MessageBox.DisplayMessage("you must write your Email.", "Error", "Error in email",
								AlertType.WARNING);
						return false;

					} else {
						if (NumTxt.getText().isEmpty()) {
							MessageBox.DisplayMessage(
									"You must enter the number of members you want to be in the family subscription",
									"Error", "Error in num of members", AlertType.WARNING);
							return false;
						} else {
							if (IDnumtxt.getText().isEmpty()) {
								MessageBox.DisplayMessage("You must write your ID number", "Error",
										"Error in num of members", AlertType.WARNING);
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	@FXML
	void CancelFunc(ActionEvent event) throws IOException {

		e.openPage("ServiceResresentativeHomePage1.fxml", event, "Home Page ServiceResresentative");
	}

	@FXML
	void logoutfunc(MouseDragEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * The traveler has the option to save information for his credit card. This
	 * method is used by us to test whether he chose to enter these details.
	 * 
	 * @return
	 */
	private boolean checkFieldsCreditCard() {
		if (creditExptxt.getText().isEmpty()) {
			MessageBox.DisplayMessage(
					"If you entered a credit card number, you must enter the rest of the card details", "Error",
					"Error in first name", AlertType.WARNING);
			return false;
		} else {
			if (txtcvv.getText().isEmpty()) {
				MessageBox.DisplayMessage(
						"If you entered a credit card number, you must enter the rest of the card details", "Error",
						"Error in last name", AlertType.WARNING);
				return false;
			} else
				return true;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		txtcvv.setTooltip(new Tooltip("three digits on the back of the card"));
	}

}
