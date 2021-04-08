package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.Date;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * A class that keeps the information in the visits report according to the stay
 * time , department manager
 * 
 * @author hana
 *
 */

public class VisitngReportController implements Initializable {
	/**
	 * e-show of EmployeeIdentificationPage1Controller
	 */
	public static EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
	@FXML
	private Button backBTN;

	@FXML
	private Label labelIndev;

	@FXML
	private Label labelSub;

	@FXML
	private Label labelgrpups;

	@FXML
	private Button submitbtn;

	@FXML
	private PieChart visitorsPie;

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtn1;

	/**
	 * In this method, exit the current page and return to Landing page
	 * 
	 * @param event on MouseEvent
	 * @throws Exception
	 */
	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * In this method, the type of report view is checked if the sumbit button is
	 * activated monthly, otherwise it is hidden because a report can only be saved
	 * in a monthly view.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		idtxt.setText(ChatClient.E1.getUsername());

		switch (ChatClient.TypeOfView) {
		case "StayTimeToDay":
			submitbtn.setDisable(true);
			visitorsPieFunc("in this day");
			break;
		case "StayTimeToMonth":
			submitbtn.setDisable(false);
			visitorsPieFunc("in this month");
			break;

		}

	}

	/**
	 * In this method we return to the page where data is selected for a report
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void BackFUNC(ActionEvent event) throws IOException {
		e.openPage("VisitingTimeStayTime.fxml", event, "Choosing report");
	}

	/**
	 * In this method, the information for the report is stored in the database
	 * 
	 * @param event on ActionEvent
	 */
	@FXML
	void submitFunc(ActionEvent event) {
		Year s = Year.now();
		Date today = new Date();
		int dd = today.getDate();
		int mm = today.getMonth() + 1;

		String task1 = null;
		if (ChatClient.myAtoi(ChatClient.MonthCombo) < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + ChatClient.TranslateMonthName(ChatClient.MonthCombo)
					+ " " + "visitrepStay";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + ChatClient.TranslateMonthName(ChatClient.MonthCombo)
					+ " " + "visitrepStay";
		}

		ChatClient.setSucceeded(false);
		ClientUI.chat.accept(task1); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			System.out.println("ChatClient.IsExist: " + ChatClient.IsExist);
			if (ChatClient.IsExist.equals("Yes")) {
				MessageBox.DisplayMessage("You have already submitted this report", "", "Department maneger",
						AlertType.INFORMATION);
			} else {
				String task2 = null;
				if (ChatClient.myAtoi(ChatClient.MonthCombo) < 9) {
					task2 = "SaveTheReport " + s + " 0" + ChatClient.TranslateMonthName(ChatClient.MonthCombo) + " "
							+ "visitrepStay" + " " + ChatClient.ViewCombo;
				} else {
					task2 = "SaveTheReport " + s + " " + ChatClient.TranslateMonthName(ChatClient.MonthCombo) + " "
							+ "visitrepStay" + " " + ChatClient.ViewCombo;
				}

				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task2); // send message from client to server
				if (ChatClient.getSucceeded().equals(true)) {
					labelIndev.setText("");
					labelSub.setText("");
					labelgrpups.setText(" ");
					visitorsPie.getData().clear();
					MessageBox.DisplayMessage("The report is submeted.", "", "Department maneger", AlertType.WARNING);

				}

			}
		}

	}

	/**
	 * In this method we fill in the report according to the relevant data
	 * 
	 * @param s s="in this day" OR "in this month"
	 */
	void visitorsPieFunc(String s) {

		String m = " stayed for up to " + ChatClient.staytime + " hours";
		labelIndev.setText("Individual visitors: " + ChatClient.arrayInfo[0] + "% " + s + m);

		labelSub.setText("Subscribers: " + ChatClient.arrayInfo[2] + "% " + s + m);

		labelgrpups.setText("Organized groups: " + ChatClient.arrayInfo[1] + "% " + s + m);
		ObservableList<PieChart.Data> piechart = FXCollections.observableArrayList(
				new PieChart.Data("Individual visitors", ChatClient.arrayInfo[0]),
				new PieChart.Data("Subscribers", ChatClient.arrayInfo[2]),
				new PieChart.Data("Organized groups", ChatClient.arrayInfo[1]));
		visitorsPie.setData(piechart);

	}

}
