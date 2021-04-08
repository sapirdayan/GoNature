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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Discount controller
 *
 */
public class DiscountController implements Initializable {

	@FXML
	private ImageView ContactUsBtn;
	@FXML
	private Label idtxt;
	@FXML
	private Label LogoutBtn;

	@FXML
	private ComboBox<String> ComboDiscount;

	@FXML
	private Button Details;

	@FXML
	private Button Back;

	@FXML
	private Button Approvebtn;

	@FXML
	private Button RefuseBtn;

	@FXML
	private Label DiscountValue;

	@FXML
	private Label StartDate;

	@FXML
	private Label EndDate;

	@FXML
	private Label DiscountType;
	boolean flag = false;
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * approval discount, in case it was approved, the DiscountConfirmationPage will
	 * be opened
	 * 
	 * @param event on action
	 * @throws IOException
	 */
	@FXML
	void ApproveFunc(ActionEvent event) throws IOException {
		if (flag != false) {

			MessageBox.DisplayMessage("The discount was approved.", "", "", AlertType.INFORMATION);
			String task = "ApprovedDiscount " + ChatClient.E1.getOrganizationalAffiliation() + " "
					+ ChatClient.D1.getDiscountNum();
			ChatClient.setSucceeded(false);
			System.out.println("task: " + task);
			ClientUI.chat.accept(task);
			if (ChatClient.getSucceeded() == true) {
				e.openPage("DiscountConfirmationPage.fxml", event, "Discount page");
			} else
				MessageBox.DisplayMessage("something wrong", "", "", AlertType.INFORMATION);
		} else
			MessageBox.DisplayMessage("you must choose the discount before approval.", "", "", AlertType.INFORMATION);
	}

	/**
	 * Open contact us page
	 * 
	 * @param event on mouse click
	 * @throws IOException
	 */
	@FXML
	void ContactUsFunc(MouseEvent event) throws IOException {
		e.openPage("ContactUs.fxml", event, "Contact us page");
	}

	/**
	 * checking the comboBox input to set to the details of the discount
	 * 
	 * @param event on action
	 */

	@FXML
	void DetailsFunc(ActionEvent event) {
		if (ComboDiscount.getValue() == null) {
			MessageBox.DisplayMessage("Department manager, \nYou have to choose a discount.", "Error", "Error",
					AlertType.WARNING);
		} else {
			flag = true;
			String str1 = "";
			String str = ComboDiscount.getValue().replace("%", "");
			String[] arr = str.split(" ");
			System.out.println("@@@@@@: " + arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[4]+" ");
			if (arr[2].equals("subscription"))
				str1 = "1";
			else
				str1 = "2";
			String task1 = "GiveMeDiscountDetails " + ChatClient.E1.getOrganizationalAffiliation() + " " + str1 + " "
					+ arr[3] + " " + arr[4] + " " + arr[5];
			ChatClient.setSucceeded(false);
			System.out.println("task1: " + task1);
			ClientUI.chat.accept(task1);
			if (ChatClient.getSucceeded() == true) {
				DiscountValue.setText(ChatClient.DiscountDetails.get(5) + "%");
				DiscountType.setText(ChatClient.D1.getTypeOfDiscount());
				StartDate.setText(ChatClient.DiscountDetails.get(3));
				EndDate.setText(ChatClient.DiscountDetails.get(4));
				ChatClient.DiscountDetails.clear();
			}
		}
	}

	@FXML
	void LogoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * refusing discount
	 * 
	 * @param event on action
	 * @throws IOException
	 */
	@FXML
	void RefuseFunc(ActionEvent event) throws IOException {
		if (flag != false) {
			MessageBox.DisplayMessage("The discount was refused.", "", "", AlertType.INFORMATION);
			String task1 = "RefusedDiscount " + ChatClient.E1.getOrganizationalAffiliation() + " "
					+ ChatClient.D1.getDiscountNum();

			ChatClient.setSucceeded(false);
			System.out.println("task: " + task1);
			ClientUI.chat.accept(task1);
			if (ChatClient.getSucceeded() == true) {
				e.openPage("DiscountConfirmationPage.fxml", event, "Discount page");

//				DeparmentManegerController m = new DeparmentManegerController();
//				m.OpenRelevantPage(event, "DiscountConfirmationPage.fxml", "Discount page");
			}
		} else
			MessageBox.DisplayMessage("you must choose the discount before refusal.", "", "", AlertType.INFORMATION);

	}

	/**
	 * returning to the home page department manager
	 * 
	 * @param event on action
	 * @throws IOException
	 */

	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		e.openPage("HomePageDepartmentManeger1.fxml", event, "home page department maneger");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		String task = "DiscountsThatRequireApproval " + ChatClient.E1.getOrganizationalAffiliation();
		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task); // send message from client to server
		// send to the department manger
		if (ChatClient.getSucceeded() == true) {
			for (int j = 0; j < ChatClient.DiscountsList.length; j++) {
				ComboDiscount.getItems().add(ChatClient.DiscountsList[j]);
			}
		} else {
			MessageBox.DisplayMessage("There is no Discounts that require approval", "", "Department maneger",
					AlertType.WARNING);
		}
	}

}
