package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Occupancy report controller
 *
 */
public class OccupancyReport1Controller implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label totaLtext;
	@FXML
	private Label logoutbtntxt;

	@FXML
	private Label OrganizedGroups;

	@FXML
	private Label TotalVisitors;

	@FXML
	private Label Subscribers;

	@FXML
	private LineChart<String, Integer> CancelRep;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	@FXML
	private Button Okeybtn;

	@FXML
	private Button BackBtn;

	@FXML
	private Button SubmitBtn;

	@FXML
	private Label TotalHours;

	@FXML
	private Button Okbutton;
	boolean flag = false;

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * back function to open page for park manager or department manager
	 * 
	 * @param event on action
	 * @throws IOException client exception
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		if (ChatClient.whois.equals("dep")) {
			DeparmentManegerController d = new DeparmentManegerController();
			e.openPage("BforeDepartment.fxml", event, "Choosing department");
		} else {
			ParkManagerController p = new ParkManagerController();
			e.openPage("BeforeReport.fxml", event, "Choosing park maneger");
		}
	}

	/**
	 * Fill in the report according to the information received
	 * 
	 * @param string     series name
	 * @param MonthCombo chosen month
	 * @param year       chosen year
	 */
	void full(String string, String MonthCombo, String year) {
		ParkManagerController tempManeger = new ParkManagerController();
		CancelRep.getData().clear();
		int sum = 0;
		flag = true;
		if (tempManeger.chcekfields(MonthCombo, string) != null) {
			String task = null;
			int i = 0;
			Series<String, Integer> set2 = new XYChart.Series<String, Integer>();
			if (!string.equals("Monthly")) {

				LocalDate local = ChatClient.ComboDay;
				local = local.minusDays(1);
				task = "SendMeIfIsFullByDay " + ChatClient.E1.getOrganizationalAffiliation() + " " + local.plusDays(1);
				System.out.println("the task is: " + task);
				ClientUI.chat.accept(task);
				System.out.println("server return:----------------" + ChatClient.FullByDay[0]);
				set2.getData().add(new XYChart.Data<>("07:00", ChatClient.myAtoi(ChatClient.FullByDay[0])));
				set2.getData().add(new XYChart.Data<>("08:00", ChatClient.myAtoi(ChatClient.FullByDay[1])));
				set2.getData().add(new XYChart.Data<>("09:00", ChatClient.myAtoi(ChatClient.FullByDay[2])));
				set2.getData().add(new XYChart.Data<>("10:00", ChatClient.myAtoi(ChatClient.FullByDay[3])));
				set2.getData().add(new XYChart.Data<>("11:00", ChatClient.myAtoi(ChatClient.FullByDay[4])));
				set2.getData().add(new XYChart.Data<>("12:00", ChatClient.myAtoi(ChatClient.FullByDay[5])));
				set2.getData().add(new XYChart.Data<>("13:00", ChatClient.myAtoi(ChatClient.FullByDay[6])));
				set2.getData().add(new XYChart.Data<>("14:00", ChatClient.myAtoi(ChatClient.FullByDay[7])));
				set2.getData().add(new XYChart.Data<>("15:00", ChatClient.myAtoi(ChatClient.FullByDay[8])));
				set2.getData().add(new XYChart.Data<>("16:00", ChatClient.myAtoi(ChatClient.FullByDay[9])));
				set2.getData().add(new XYChart.Data<>("17:00", ChatClient.myAtoi(ChatClient.FullByDay[10])));
				set2.getData().add(new XYChart.Data<>("18:00", ChatClient.myAtoi(ChatClient.FullByDay[11])));
				set2.getData().add(new XYChart.Data<>("19:00", ChatClient.myAtoi(ChatClient.FullByDay[12])));
				set2.setName("Hour");
				int count = 0;
				for (int a = 0; a < ChatClient.FullByDay.length; a++) {
					if (ChatClient.FullByDay[a].equals("0"))
						count++;
				}
				CancelRep.getData().add(set2);
				totaLtext.setText(" ");
				totaLtext.setText("Total hours when it was not fully occupied: ");
				TotalHours.setText(count + "");

			} else {
				LocalDate localDate = LocalDate.of(ChatClient.myAtoi(year), ChatClient.TranslateMonthName(MonthCombo),
						1);
				System.out.println("localDate" + localDate);
				task = "SendMeIfIsFullByMonth " + ChatClient.E1.getOrganizationalAffiliation() + " " + localDate;
				ClientUI.chat.accept(task);
				for (int j = 1; j < 32; j++) {
					set2.getData().add(new XYChart.Data<>("Day" + j, ChatClient.FullBymonth[j - 1]));
				}

				set2.setName("Day");
				CancelRep.getData().add(set2);
				int count = 0;
				System.out.println("ChatClient.FullBymonth[5]) " + ChatClient.FullBymonth[5]);
				System.out.println("ChatClient.FullBymonth.length " + ChatClient.FullBymonth.length);
				for (int a = 0; a < ChatClient.FullBymonth.length - 1; a++) {
					if (ChatClient.FullBymonth[a] == 0)
						count++;
				}
				TotalHours.setText(count + "");
				totaLtext.setText(" ");
				totaLtext.setText("Total days when it was not fully occupied: ");
			}
		}

	}

	/**
	 * This function save the report in data base.
	 * 
	 * @param event on action
	 */
	@FXML
	void SubmitFunc(ActionEvent event) {

		Date today = new Date();
		int dd = today.getDate();
		int mm = today.getMonth() + 1;
		Year s = Year.now();
// if ((dd == 31 && (mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8)) || (dd == 30 && (mm == 4 || mm == 6 || mm == 9 || mm == 11))
// || ((dd == 28 || dd == 29) && (mm == 2))) {

		int numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo);
		String task1 = null;
		if (numbermonth < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + numbermonth + " " + "Occupancy";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + +numbermonth + " " + "Occupancy";
		}
		ChatClient.setSucceeded(false);
		CancelRep.getData().clear();
		ClientUI.chat.accept(task1); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			System.out.println("ChatClient.IsExist: " + ChatClient.IsExist);
			if (ChatClient.IsExist.equals("Yes")) {
				MessageBox.DisplayMessage("You have already submitted this report", "", "park maneger",
						AlertType.INFORMATION);
			}

			else {
				CancelRep.getData().clear();
				ParkManagerController tempManeger = new ParkManagerController();
				tempManeger.checkfullreport(flag);
				flag = false;
				TotalHours.setText("");
				if (numbermonth < 9) {
					task1 = "SaveTheReport " + s + " 0" + numbermonth + " " + "Occupancy" + " " + ChatClient.ViewCombo;
				} else {
					task1 = "SaveTheReport " + s + " " + numbermonth + " " + "Occupancy" + " " + ChatClient.ViewCombo;
				}

				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task1); // send message from client to server
				if (ChatClient.getSucceeded() == true) {

					TotalHours.setText(" ");
					totaLtext.setText(" ");
					MessageBox.DisplayMessage("The report was sent to the department manager successfully", "",
							"park maneger", AlertType.INFORMATION);

				}
			}
		}

	}

	/**
	 * This function logs out and returns to the landing page
	 * 
	 * @param event on mouse click
	 * @throws Exception client exception
	 */
	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);

	}

	/**
	 * Initializing the parameters
	 * 
	 * @param resources resources
	 * @param location  location
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParkManagerController tempManeger = new ParkManagerController();

		Year s = Year.now();
		if (!ChatClient.whois.equals("dep")) {
			if (ChatClient.ViewCombo.equals("Monthly"))
				SubmitBtn.setDisable(false);
			else
				SubmitBtn.setDisable(true);
			full(ChatClient.ViewCombo, ChatClient.MonthCombo, Year.now().toString());
		} else {

			full(ChatClient.display, ChatClient.MonthCombo, ChatClient.yearreport);
		}

	}

}
