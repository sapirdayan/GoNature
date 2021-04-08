package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.Date;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * In this department presents the selection page of the department manager when
 * he wants to produce a prestigious report for saving relevant data such as
 * month, day ...
 * 
 * @author hana
 *
 */

public class StayTimeVisitsController implements Initializable {

	@FXML
	private Button backbtn;

	@FXML
	private Button okbtn;

	@FXML
	private ComboBox<String> MonthCombo;

	@FXML
	private ComboBox<String> ComboView;

	@FXML
	private DatePicker dailycombo;

	@FXML
	private ComboBox<String> staytime;

	@FXML
	private ComboBox<String> fromcombo;

	@FXML
	private ComboBox<String> tocombo;

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtn1;

	String task = "", type = "";

	/**
	 * Return to the hime page department maneger
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void backfunc(ActionEvent event) throws IOException {
		EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
		e.openPage("HomePageDepartmentManeger1.fxml", event, "Home Page Department Maneger");
	}

	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * Stores the relevant information in the report to enable the opening of a
	 * report with proper information
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void okfunc(ActionEvent event) throws IOException {
		EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
		Date today = new Date();
		int mm = today.getMonth() + 1;
		int dd = today.getDate();
		if (isError() != null) {

			if (ChatClient.TranslateMonthName(MonthCombo.getValue()) > mm) {
				MessageBox.DisplayMessage("You can not generate a report for a later month ", "", " Department maneger",
						AlertType.INFORMATION);
			} else {
				ChatClient.MonthCombo = MonthCombo.getValue();
				ChatClient.ViewCombo = ComboView.getValue();
				if (type.equals("Stay")) {
					if (ComboView.getValue().equals("Daily")) {
						if (ChatClient.TranslateMonthName(MonthCombo.getValue()) < 10) {
							task = "SendMevisitotsByStayTimeToDay " + ChatClient.E1.getOrganizationalAffiliation() + " "
									+ dailycombo.getValue() + " " + staytime.getValue();

						} else
							task = "SendMevisitotsByStayTimeToDay " + ChatClient.E1.getOrganizationalAffiliation() + " "
									+ dailycombo.getValue() + " " + staytime.getValue();

						ChatClient.setSucceeded(false);
						ClientUI.chat.accept(task); // send message from client to server
						if (ChatClient.getSucceeded() == true) {
							ChatClient.TypeOfView = "StayTimeToDay";
						}

					}
					if (ComboView.getValue().equals("Monthly")) {
						if (ChatClient.TranslateMonthName(MonthCombo.getValue()) < 10) {
							task = "SendMevisitotsByStayTimeToMonth " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + Year.now() + "-0" + ChatClient.TranslateMonthName(MonthCombo.getValue())
									+ "-01" + " " + staytime.getValue();

						} else
							task = "SendMevisitotsByStayTimeToMonth " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + Year.now() + "-" + ChatClient.TranslateMonthName(MonthCombo.getValue())
									+ "-01" + " " + staytime.getValue();
						ChatClient.setSucceeded(false);
						ClientUI.chat.accept(task); // send message from client to server
						if (ChatClient.getSucceeded() == true) {
							System.out.println("yesssss stay time contt");
							ChatClient.TypeOfView = "StayTimeToMonth";
						}
					}
					ChatClient.staytime = staytime.getValue();
					e.openPage("VistingReport.fxml", event, "Visting Report");
				} else {

					if (ComboView.getValue().equals("Daily")) {
						ChatClient.Statusentertime = " in:- " + dailycombo.getValue();
						ChatClient.entertime1 = fromcombo.getValue();
						ChatClient.entertime2 = tocombo.getValue();
						task = "SendMeEnteringTimeToDay " + ChatClient.E1.getOrganizationalAffiliation() + " "
								+ dailycombo.getValue() + " " + fromcombo.getValue() + " " + tocombo.getValue();

						ChatClient.setSucceeded(false);
						ClientUI.chat.accept(task); // send message from client to server
						if (ChatClient.getSucceeded() == true) {
							System.out.println("yessssssssssss hs shs hs sh enter time!!!!");
							ChatClient.TypeOfView = "EnterTimeToDay";
						}
						e.openPage("Visitng-Enterytime.fxml", event, "Visting Report");
					} else {
						if (ComboView.getValue().equals("Monthly")) {
							ChatClient.Statusentertime = " in month:- " + MonthCombo.getValue();
							ChatClient.entertime1 = fromcombo.getValue();
							ChatClient.entertime2 = tocombo.getValue();
							if (ChatClient.TranslateMonthName(MonthCombo.getValue()) < 10) {
								task = "SendMeEnteringTimeToMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
										+ Year.now() + "-0" + ChatClient.TranslateMonthName(MonthCombo.getValue())
										+ "-01" + " " + fromcombo.getValue() + " " + tocombo.getValue();

							} else
								task = "SendMeEnteringTimeToMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
										+ Year.now() + "-" + ChatClient.TranslateMonthName(MonthCombo.getValue())
										+ "-01" + " " + fromcombo.getValue() + " " + tocombo.getValue();
							ChatClient.setSucceeded(false);
							ClientUI.chat.accept(task); // send message from client to server
							if (ChatClient.getSucceeded() == true) {
								System.out.println("yesssss stay time contt");
								ChatClient.TypeOfView = "EnterTimeToMonth";
							}
							e.openPage("Visitng-Enterytime.fxml", event, "Visting Report");
						}

					}
				}
			}

		}
	}

	/**
	 * Initialize fields like the combo box of the page and the type of the report
	 * if dtay or enter time
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		MonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");
		ComboView.getItems().addAll("Monthly", "Daily");
		if (ChatClient.TypeOfReport.equals("VisitsStay")) {
			type = "Stay";
			staytime.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");
		}
		if (ChatClient.TypeOfReport.equals("VisitsEntry")) {
			type = "Enter";
			for (int i = 7; i < 10; i++) {
				tocombo.getItems().add("0" + i + ":00");
				fromcombo.getItems().add("0" + i + ":00");
			}
			for (int i = 10; i < 20; i++) {
				tocombo.getItems().add(i + ":00");
				fromcombo.getItems().add(i + ":00");
			}
		}
	}

	/**
	 * Check fields
	 * 
	 * @return 1 if the input is valid else null
	 */
	Object isError() {
		if (ComboView.getValue() == null) {
			MessageBox.DisplayMessage("You need to choose the form of display.", "Error", "Department maneger",
					AlertType.WARNING);
			return null;
		} else if (MonthCombo.getValue() == null) {
			MessageBox.DisplayMessage("You must choose a month.", "Error", "Department maneger", AlertType.WARNING);
			return null;
		} else {
			int MonthNum = ChatClient.TranslateMonthName(MonthCombo.getValue());
			if (ComboView.getValue().equals("Daily")
					&& (dailycombo.getValue() == null || dailycombo.getValue().getMonthValue() != MonthNum)) {
				MessageBox.DisplayMessage(
						"If you have selected 'Daily' view,\n you must select a valid date that matches the month you selected.",
						"Error", "Department maneger", AlertType.WARNING);
				return null;
			} else {
				if (type.equals("Stay")) {
					if (staytime.getValue() == null) {
						MessageBox.DisplayMessage("You must select a stay time.", "Error", "Department maneger",
								AlertType.WARNING);
						return null;
					} else
						return 1;
				}
				if (type.equals("Enter")) {
					if (fromcombo.getValue() == null || tocombo.getValue() == null) {
						MessageBox.DisplayMessage("You need to enter valid hours.", "Error", "Department maneger",
								AlertType.WARNING);
						return null;
					} else {
						String[] str = fromcombo.getValue().split(":");
						String[] ste = tocombo.getValue().split(":");
						if (ChatClient.myAtoi(str[0]) > ChatClient.myAtoi(ste[0])) {
							MessageBox.DisplayMessage("Entry time 'from' should be greater than the entery time 'to'.",
									"Error", "Department maneger", AlertType.WARNING);
							return null;
						} else
							return 1;

					}

				}
			}
		}
		return 1;
	}
}
