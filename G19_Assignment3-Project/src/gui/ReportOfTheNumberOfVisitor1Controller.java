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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class in charge of report number of visitor -park maneger and department
 * maneger
 * 
 * @author hana
 *
 */

public class ReportOfTheNumberOfVisitor1Controller implements Initializable {
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Label OrganizedGroups;

	@FXML
	private Label TotalVisitors;

	@FXML
	private Label Subscribers;

	@FXML
	private BarChart<String, Integer> CancelRep1;

	@FXML
	private BarChart<String, Integer> CancelRep;

	@FXML
	private CategoryAxis X;

	@FXML
	private Button Okeybtn;

	@FXML
	private NumberAxis Y;
	@FXML
	private Button Okbutton;

	@FXML
	private RadioButton weeklyRadio;

	@FXML
	private RadioButton DayRadio;

	@FXML
	private RadioButton HourRadio;

	@FXML
	private Button BackBtn;

	@FXML
	private Button SubmitBtn;

	@FXML
	private Label totalvisitors;

	@FXML
	private Label TotalIndividual;

	@FXML
	private Label TotalSubscrbition;

	@FXML
	private Label Totalorgnized;

	@FXML
	private Circle CircleStatus;

	@FXML
	private Button Ok;

	@FXML
	private Label statuslabel;

	boolean flag = false;

	/**
	 * Filling out the report with the relevant information
	 * 
	 * @param string
	 * @param monthCombo2
	 * @param s
	 */

	void fullreport(String string, String monthCombo2, String s) {
		ParkManagerController tempManeger = new ParkManagerController();
		if (tempManeger.chcekfields(monthCombo2, string) != null) {
			flag = true;
			int numbermonth = ChatClient.TranslateMonthName(monthCombo2);
			System.out.println(numbermonth + " ");
			String task;
			if (numbermonth > 9) {
				String str = s.toString() + "-" + numbermonth + "-01";

				task = "SendMeInfoToVisitorReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			} else {

				String str = s.toString() + "-" + "0" + numbermonth + "-01";
				task = "SendMeInfoToVisitorReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			}

			System.out.println("the task with zero is  " + task);
			ClientUI.chat.accept(task);

			totalvisitors.setText(
					ChatClient.myAtoi(ChatClient.VisitorByMonth[0]) + ChatClient.myAtoi(ChatClient.VisitorByMonth[1])
							+ ChatClient.myAtoi(ChatClient.VisitorByMonth[2]) + "");
			TotalIndividual.setText(ChatClient.myAtoi(ChatClient.VisitorByMonth[0]) + "");
			TotalSubscrbition.setText((ChatClient.VisitorByMonth[2]) + "");
			Totalorgnized.setText((ChatClient.VisitorByMonth[1]) + "");

			if (ChatClient.myAtoi(ChatClient.VisitorByMonth[0]) + ChatClient.myAtoi(ChatClient.VisitorByMonth[1])
					+ ChatClient.myAtoi(ChatClient.VisitorByMonth[2]) > 10) {
				CircleStatus.setFill(Color.DARKGREEN);
				statuslabel.setText(
						"Reaching Destination - Over 10 visitors visited in the month " + "'" + monthCombo2 + "'");
			} else
				statuslabel.setText("Failure to reach destination This month -less than 100 visitors visited");

			if (string.equals("Regular")) {
				CancelRep.getData().clear();

				XYChart.Series<String, Integer> set1 = tempManeger.SetIndevdualRegularSetINParChart(

						ChatClient.myAtoi(ChatClient.VisitorByMonth[0]), "Individual visitors", "Individual visitors");

				CancelRep.getData().add(set1);
				XYChart.Series<String, Integer> set2 = tempManeger.SetIndevdualRegularSetINParChart(
						ChatClient.myAtoi(ChatClient.VisitorByMonth[2]), "Subscribers ", "Subscribers");
				CancelRep.getData().add(set2);

				XYChart.Series<String, Integer> set3 = tempManeger.SetIndevdualRegularSetINParChart(
						ChatClient.myAtoi(ChatClient.VisitorByMonth[1]), "Organized groups ", "Organized groups");
				CancelRep.getData().add(set3);
			} else {
				if (string.equals("By day")) {
					CancelRep.getData().clear();
					numbermonth = ChatClient.TranslateMonthName(monthCombo2);
					System.out.println(numbermonth + " ");
					String task1;
					if (numbermonth > 9) {
						String str = s.toString() + "-" + numbermonth + "-01";

						task1 = "SendMeInfoToVisitorReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation()
								+ " " + str;
					} else {

						String str = s.toString() + "-" + "0" + numbermonth + "-01";
						task1 = "SendMeInfoToVisitorReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation()
								+ " " + str;
					}
					ClientUI.chat.accept(task1);
					XYChart.Series<String, Integer> set1 = tempManeger.SetByDay(ChatClient.VisitorByDay, 0,
							"Individual visitors");
					CancelRep.getData().add(set1);
					XYChart.Series<String, Integer> set2 = tempManeger.SetByDay(ChatClient.VisitorByDay, 62,
							"Subscribers");
					CancelRep.getData().add(set2);
					XYChart.Series<String, Integer> set3 = tempManeger.SetByDay(ChatClient.VisitorByDay, 31,
							"Organized groups");
					CancelRep.getData().add(set3);

				} else {
					if (string.equals("Weekly")) {
						CancelRep.getData().clear();

						numbermonth = ChatClient.TranslateMonthName(monthCombo2);

						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";

							task = "SendMeInfoToVisitorReportToTheWeek " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task = "SendMeInfoToVisitorReportToTheWeek " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						}

						ClientUI.chat.accept(task);

						XYChart.Series<String, Integer> set1 = tempManeger.SetByWeek(ChatClient.VisitorByweek, 0,
								"Individual visitors");
						CancelRep.getData().add(set1);

						XYChart.Series<String, Integer> set2 = tempManeger.SetByWeek(ChatClient.VisitorByweek, 10,
								"Subscribers");
						CancelRep.getData().add(set2);

						XYChart.Series<String, Integer> set3 = tempManeger.SetByWeek(ChatClient.VisitorByweek, 5,
								"Organized groups");
						CancelRep.getData().add(set3);

					} else if (string.equals("Hourly")) {
						CancelRep.getData().clear();
						numbermonth = ChatClient.TranslateMonthName(monthCombo2);
						String task3;

						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";

							task3 = "SendMeInfoToVisitorReportToTheHour " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task3 = "SendMeInfoToVisitorReportToTheHour " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						}
						ClientUI.chat.accept(task3);

						XYChart.Series<String, Integer> set1 = tempManeger.setByHour(ChatClient.VisitorByHour, 0,
								"Individual visitors");
						CancelRep.getData().add(set1);

						XYChart.Series<String, Integer> set2 = tempManeger.setByHour(ChatClient.VisitorByHour, 26,
								"Subscribers");
						CancelRep.getData().add(set2);

						XYChart.Series<String, Integer> set3 = tempManeger.setByHour(ChatClient.VisitorByHour, 13,
								"Organized groups");
						CancelRep.getData().add(set3);

					}
				}
			}
		}
	}

	/**
	 * return to the relevant page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		if (ChatClient.whois.equals("dep")) {
			e.openPage("BforeDepartment.fxml", event, "Choosing department");
		} else {
			e.openPage("BeforeReport.fxml", event, "Choosing park maneger");
		}
	}

	/**
	 * Saving the report produced by the park manager
	 * 
	 * @param event on ActionEvent
	 */
	@FXML
	void SubmitFunc(ActionEvent event) {
		Date today = new Date();
		int dd = today.getDate();
		int mm = today.getMonth() + 1;
// if ((dd == 31
// && (mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 ))
// || (dd == 30 && (mm == 4 || mm == 6 || mm == 9 || mm == 11) || ((dd == 28 || dd == 29) && (mm == 2))))
//
// {

		Year s = Year.now();

		int numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo);
		String task1 = null;
		if (numbermonth < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + numbermonth + " " + "VisitReport";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + +numbermonth + " " + "VisitReport";
		}

		ChatClient.setSucceeded(false);
		CancelRep.getData().clear();
		ClientUI.chat.accept(task1); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			System.out.println("ChatClient.IsExist: " + ChatClient.IsExist);
			if (ChatClient.IsExist.equals("Yes")) {
				MessageBox.DisplayMessage("You have already submitted this report", "", "Park maneger",
						AlertType.INFORMATION);
			}

			else {
				ParkManagerController tempManeger = new ParkManagerController();
				tempManeger.checkfullreport(flag);

				String task2 = null;
				if (numbermonth < 9) {
					task2 = "SaveTheReport " + s + " 0" + numbermonth + " " + "VisitReport" + " "
							+ ChatClient.ViewCombo;
				} else {
					task2 = "SaveTheReport " + s + " " + numbermonth + " " + "VisitReport" + " " + ChatClient.ViewCombo;
				}

				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task2); // send message from client to server
				if (ChatClient.getSucceeded() == true) {
					flag = false;
					totalvisitors.setText("");
					TotalIndividual.setText("");
					TotalSubscrbition.setText("");
					Totalorgnized.setText("");
					CircleStatus.setFill(Color.RED);
					MessageBox.DisplayMessage("The report was sent to the department manager successfully", "",
							"park maneger", AlertType.INFORMATION);
				}
			}
		}

// } else
// MessageBox.DisplayMessage("You can only submit the report at the end of the month ", "",
// " ", AlertType.INFORMATION);
	}

	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (!ChatClient.whois.equals("dep")) {
			if (ChatClient.ViewCombo.equals("Regular"))
				SubmitBtn.setDisable(false);
			else
				SubmitBtn.setDisable(true);
			fullreport(ChatClient.ViewCombo, ChatClient.MonthCombo, Year.now().toString());
		} else {
			System.out.println("ChatClient.display" + ChatClient.display);
			fullreport(ChatClient.display, ChatClient.MonthCombo, ChatClient.yearreport);
		}
		idtxt.setText(ChatClient.E1.getUsername());

	}

}
