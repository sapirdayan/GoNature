package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * A class that keeps the information in the visits report according to the
 * entery time , department manager
 * 
 * @author hana
 *
 */

public class visitngtimeEnteryTime implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button BackBtn;

	@FXML
	private Button SubmitBtn;

	@FXML
	private BarChart<String, Integer> VisitReport;

	@FXML
	private Label Total;
	@FXML
	private Label Total1;

	@FXML
	private Label Total3;

	@FXML
	private Label Total2;
	public static EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * In this method we return to the page where data is selected for a report
	 * 
	 * @param event on action
	 * @throws IOException
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		e.openPage("VisitingReportEnterTime.fxml", event, "Choosing report");

	}

	/**
	 * In this method, the information for the report is stored in the database
	 * 
	 * @param event on action
	 */
	@FXML
	void SubmitFunc(ActionEvent event) {
		Year s = Year.now();

		String task1 = null;
		if (ChatClient.myAtoi(ChatClient.MonthCombo) < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + ChatClient.TranslateMonthName(ChatClient.MonthCombo)
					+ " " + "visitrepEnter";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + ChatClient.TranslateMonthName(ChatClient.MonthCombo)
					+ " " + "visitrepEnter";
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
							+ "visitrepEnter" + " " + ChatClient.ViewCombo;
				} else {
					task2 = "SaveTheReport " + s + " " + ChatClient.TranslateMonthName(ChatClient.MonthCombo) + " "
							+ "visitrepEnter" + " " + ChatClient.ViewCombo;
				}

				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task2); // send message from client to server
				if (ChatClient.getSucceeded().equals(true)) {
					Total.setText("");
					Total1.setText("");
					Total2.setText(" ");
					Total3.setText(" ");
					VisitReport.getData().clear();
					MessageBox.DisplayMessage("The report is submeted.", "", "Department maneger", AlertType.WARNING);
				}

			}
		}
	}

	/**
	 * In this method, exit the current page and return to Landing page
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
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
		case "EnterTimeToDay":
			SubmitBtn.setDisable(true);
			visitorsPieFunc("in this day");

			break;
		case "EnterTimeToMonth":
			SubmitBtn.setDisable(false);
			visitorsPieFunc("in this month");
			break;
		}
	}

	/**
	 * In this method we fill in the report according to the relevant data
	 * 
	 * @param s
	 */
	void visitorsPieFunc(String s) {

		int total = ChatClient.arrayInfo[0] + ChatClient.arrayInfo[1] + ChatClient.arrayInfo[2];
		Total3.setText("Total visitors enter to the park " + ChatClient.Statusentertime + " Between "
				+ ChatClient.entertime1 + " to " + ChatClient.entertime2 + " is: " + total);
		Total.setText("Total Individual visitors :" + ChatClient.arrayInfo[0]);
		Total1.setText("Total Subscribers : " + ChatClient.arrayInfo[2]);
		Total2.setText("Total Organized groups : " + ChatClient.arrayInfo[1]);

		XYChart.Series<String, Integer> set1 = new XYChart.Series<String, Integer>();
		set1.getData().add(new XYChart.Data<>("Individual visitors", ChatClient.arrayInfo[0]));
		set1.setName("Individual visitors");
		XYChart.Series<String, Integer> set2 = new XYChart.Series<String, Integer>();
		set2.getData().add(new XYChart.Data<>("Subscribers", ChatClient.arrayInfo[2]));
		set2.setName("Subscribers");
		XYChart.Series<String, Integer> set3 = new XYChart.Series<String, Integer>();
		set3.getData().add(new XYChart.Data<>("Organized groups", ChatClient.arrayInfo[1]));
		set3.setName("Organized groups");

		VisitReport.getData().addAll(set1, set2, set3);
	}

}
