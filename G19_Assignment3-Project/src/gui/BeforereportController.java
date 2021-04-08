package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import client.ChatClient;
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
 * A class for opening report screens that managers can generate. Depending on
 * the values entered in the fields, the report screen will open with the data
 * contained in the DB.
 *
 * @author Nasmat Amr
 *
 */
public class BeforereportController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private ComboBox<String> MonthCombo;

	@FXML
	private Button BackBtn;

	@FXML
	private Button Okbutton;
	@FXML
	private Label occupancytext;

	@FXML
	private ComboBox<String> ComboView;

	@FXML
	private DatePicker fromdatepicker;
	public static boolean flag = false;
	public static String typemaneger = "";

	/**
	 * Home return method is adapted to the user who opened the screen operated by
	 * this controller.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		if (ChatClient.whois.equals("dep")) {
			EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
			e.openPage("HomePageDepartmentManeger1.fxml", event, "Home Page Department Maneger");
		} else {
			ParkManagerController home = new ParkManagerController();
			home.returnToTheHomePage(event);
		}
	}

	/**
	 * Method Activated by clicking the "Ok" button In this click, we are taken to
	 * the screen of the relevant report.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void OkeyFunc(ActionEvent event) throws IOException {
		EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
		Date today = new Date();
		int mm = today.getMonth() + 1;

		if (MonthCombo.getValue() != null && ComboView.getValue() != null) {
			if (!(ComboView.getValue().equals("Hourly") && (ChatClient.TypeOfReport.equals("Occupancy"))
					&& fromdatepicker.getValue() == null)) {

				if (ChatClient.TranslateMonthName(MonthCombo.getValue()) > mm) {
					MessageBox.DisplayMessage("You can not generate a report for a later month ", "",typemaneger,AlertType.INFORMATION);

				} else {
					if (ComboView.getValue().equals("Hourly") && ChatClient.TypeOfReport.equals("Occupancy")) {
						if (checkDatesFields() != null) {

							ChatClient.ComboDay = fromdatepicker.getValue();
							ChatClient.MonthCombo = MonthCombo.getValue();
							ChatClient.ViewCombo = ComboView.getValue();
							e.openPage("ReportOccupancy.fxml", event, "Occpancy report");
						}
					} else {
						ChatClient.MonthCombo = MonthCombo.getValue();
						ChatClient.ViewCombo = ComboView.getValue();
						if (ChatClient.TypeOfReport.equals("visitors")) {
							e.openPage("ReportOfNumberOfVisitors1.fxml", event, "Total number of visitors report");
						} else {
							if (ChatClient.TypeOfReport.equals("Income")) {
								e.openPage("ReportIncome1.fxml", event, "Income report");
							} else {
								if (ChatClient.TypeOfReport.equals("Occupancy"))
									e.openPage("ReportOccupancy.fxml", event, "Occpancy report");
								else {
									if (ChatClient.TypeOfReport.equals("Cancel"))
										e.openPage("cancaltionReport.fxml", event, "Cancelation report");
									else
										e.openPage("ReportOccupancy.fxml", event, "Occpancy report");

								}
							}

						}
					}
				}
			} else {
				MessageBox.DisplayMessage("please choose date ", "", typemaneger, AlertType.WARNING);
			}
		} else
			MessageBox.DisplayMessage("You must fill all fields.", "", typemaneger, AlertType.INFORMATION);

	}

	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * In this method, we fill in the combobox according to the possible views for
	 * the report type. In addition we initialize the rest of the text fields and
	 * labels of the screen.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		System.out.println("whois: " + ChatClient.whois);
		if (ChatClient.whois.equals("dep"))
			typemaneger = "Department maneger";
		else
			typemaneger = "Park maneger";
		switch (ChatClient.TypeOfReport) {
		case "visitors":
			fromdatepicker.setDisable(true);
			occupancytext.setText("");
			ComboView.getItems().addAll("Regular", "By day", "Weekly", "Hourly");
			break;

		case "Income":
			fromdatepicker.setDisable(true);
			occupancytext.setText("");
			ComboView.getItems().addAll("Regular", "By day", "Weekly", "Hourly");
			break;

		case "Occupancy":
			fromdatepicker.setDisable(false);
			occupancytext.setText("If you choose view 'By day' please choose date");
			ComboView.getItems().addAll("Monthly", "Hourly");
			break;

		case "Vists":
			occupancytext.setText("");
			fromdatepicker.setDisable(true);
			ComboView.getItems().addAll("Regular", "By day");
			break;
		case "Cancel":
			occupancytext.setText("");
			fromdatepicker.setDisable(true);
			ComboView.getItems().addAll("Regular", "By day", "Weekly", "Hourly");
			break;
		}

		MonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");

	}

	/**
	 * Check for empty fields. And if the date is correct.
	 * 
	 * @return
	 */
	Object checkDatesFields() {
		LocalDate currentdate = LocalDate.now();

		if (fromdatepicker.getValue() == null) {
			MessageBox.DisplayMessage("You must select a valid from date.", "", "", AlertType.WARNING);
			return null;
		}

		if (fromdatepicker.getValue().isAfter(currentdate)) {
			MessageBox.DisplayMessage("You need to go by a date that has already passed.", "", "", AlertType.WARNING);
			return null;
		}

		return true;
	}

}
