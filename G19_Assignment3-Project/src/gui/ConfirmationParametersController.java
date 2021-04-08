
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
 * This controller gives an option for approval or cancellation of the
 * department for the parameter sent by the park manager
 *
 */
public class ConfirmationParametersController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button GapconfirmBtn;

	@FXML
	private Button Maxquotaconfirmbtn;

	@FXML
	private Button StayTimeconfirmbtn;

	@FXML
	private Button BackToHomePage;

	@FXML
	private Button refusingbtn;

	@FXML
	private Label MaxqoutaParameters;

	@FXML
	private Label GapParameters;

	@FXML
	private Label StayTimeParameters;
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * This function returns back to home page department manager
	 * 
	 * @param event on action
	 * @throws IOException client exception
	 */
	@FXML
	void BackToHomePageFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageDepartmentManeger1.fxml", event, "home page department maneger");
	}

	/**
	 * The department manager confirm or refuse to gap parameters
	 * 
	 * @param event on action
	 */
	@FXML
	void GapconfirmFunc(ActionEvent event) {
//
		if (!GapParameters.getText().isEmpty()) {
			String task2 = "UpdateTheGapInDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been updated successfully!", "Update", "", AlertType.WARNING);
			GapParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);
	}

	/**
	 * The department manager confirm or refuse to max quota parameters
	 * 
	 * @param event on action
	 */
	@FXML
	void MaxquotaconfirmFunc(ActionEvent event) {
		if (!MaxqoutaParameters.getText().isEmpty()) {
			String task2 = "UpdateTheMaxQuotaInDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been updated successfully!", "Update", "", AlertType.WARNING);
			MaxqoutaParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);
	}

	/**
	 * update the stay time in data base
	 * 
	 * @param event on action
	 */
	@FXML
	void StayTimeconfirmFunc(ActionEvent event) {
		if (!StayTimeParameters.getText().isEmpty()) {
			String task2 = "UpdateTheStayTimeInDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been updated successfully!", "Update", "", AlertType.WARNING);
			StayTimeParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);
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
	 * This function sends to the park manager that the gap was refused.
	 * 
	 * @param event on action
	 */
	@FXML
	void refusingGapFunc(ActionEvent event) {
		if (!GapParameters.getText().isEmpty()) {
			String task2 = "deleteTheGapfromDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been deleted successfully!", "Update", "", AlertType.WARNING);
			GapParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);
	}

	/**
	 * This function send to the park manager that the max quota was refused .
	 * 
	 * @param event on action
	 */

	@FXML
	void refusingMaxquotaFunc(ActionEvent event) {
		if (!MaxqoutaParameters.getText().isEmpty()) {
			String task2 = "deleteTheMaxQuotafromDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been deleted successfully!", "Update", "", AlertType.WARNING);
			MaxqoutaParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);
	}

	/**
	 * This function send to the park manager that the stay time was refused .
	 * 
	 * @param event on action
	 */
//
	@FXML
	void refusingStayTimeFunc(ActionEvent event) {
		if (!StayTimeParameters.getText().isEmpty()) {
			String task2 = "deleteTheStayTimefromDB " + ChatClient.E1.getOrganizationalAffiliation();
			ClientUI.chat.accept(task2);
			MessageBox.DisplayMessage("The parameter has been deleted successfully!", "Update", "", AlertType.WARNING);
			StayTimeParameters.setText("");
		} else
			MessageBox.DisplayMessage("There is no new value you need to confirm /delete.", "Update", "",
					AlertType.WARNING);

	}

	/**
	 * This function initialize the values of gap parameter, max quota, stay time
	 * from the data base.
	 * 
	 * @param resources resources
	 * @param location  location
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		idtxt.setText(ChatClient.E1.getUsername());
		String task = "GiveMeTheMaxQuota " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task); // send message from client to server

		String task1 = "GiveMeTheStayTimeParameters " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task1); // send message from client to server

		String task2 = "GiveMeTheGapParameters " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task2); // send message from client to server

// TODO Auto-generated method stub
		if (ChatClient.MaxqoutaParameters1 == true) {
			MaxqoutaParameters.setText(ChatClient.MaxqoutaParameters);
		}
		if (ChatClient.Gapparameters1 == true) {
			GapParameters.setText(ChatClient.Gapparameters);
		}
		if (ChatClient.StayTimeParameters1 == true) {
			StayTimeParameters.setText(ChatClient.StayTimeParameters);
		}

	}

}
