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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This controller operates two monitors. One to view the report produced by the
 * park manager. And one for the production of the report by the park manager.
 * 
 * @author Nasmat Amr
 *
 */
public class IncomeReport1Controller implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private BarChart<String, Integer> CancelRep;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	@FXML
	private Button Okbutton;

	@FXML
	private Button BackBtn4;

	@FXML
	private Button SubmitBtn4;
	@FXML
	private Label IncomeTxt;

	@FXML
	private Label IncomeLabel;

	@FXML
	private Circle circleIncome;

	@FXML
	private Label StatusIncomeLabel;

	@FXML
	private Label statusDayes;

	Boolean flag = false;

	@FXML
	private Button Okeybtn;

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	void BackFunc(ActionEvent event) throws IOException {

		if (ChatClient.whois.equals("dep")) {
			e.openPage("BforeDepartment.fxml", event, "Choosing department");
		} else {
			e.openPage("BeforeReport.fxml", event, "Choosing park maneger");
		}
	}

	/**
	 * In this method, we present information for the requested report before the
	 * data in DB and at the request of the manager.
	 * 
	 * @param string
	 * @param MonthCombo
	 * @param s
	 */
	void fullreport(String string, String MonthCombo, String s) {

		ParkManagerController tempManeger = new ParkManagerController();
		if (tempManeger.chcekfields(MonthCombo.toString(), string) != null) {
			String task1;
			flag = true;
			CancelRep.getData().clear();
			int numbermonth = ChatClient.TranslateMonthName(MonthCombo);
			System.out.println(numbermonth + " ");

			if (numbermonth > 9) {
				String str = s.toString() + "-" + numbermonth + "-01";

				task1 = "SendMeInfoToIncomeReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			} else {

				String str = s.toString() + "-" + "0" + numbermonth + "-01";
				task1 = "SendMeInfoToIncomeReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			}

			ClientUI.chat.accept(task1);
			IncomeTxt.setText("Total income to the month " + "'" + MonthCombo + "'" + "is : "
					+ ChatClient.IncomeByMonth[0] + " NIS");
			if (ChatClient.myAtoi(ChatClient.IncomeByMonth[0]) > 1000) {
				circleIncome.setFill(Color.DARKGREEN);
				StatusIncomeLabel.setText(
						"Reaching the destination - in " + "'" + MonthCombo + "'" + " the income was over 1000 NIS");
			} else {
				StatusIncomeLabel.setText("Failure to reach the destination in " + "'" + MonthCombo + "'"
						+ " the income was less than 1000 NIS");

			}
			if (string.equals("Regular")) {
				String m = "Month " + ChatClient.MonthCombo;
				Series<String, Integer> set1 = tempManeger
						.SetIndevdualRegularSetINParChart(ChatClient.myAtoi(ChatClient.IncomeByMonth[0]), m, "");
				CancelRep.getData().add(set1);
			} else {
				if (string.equals("By day")) {

					CancelRep.getData().clear();
					numbermonth = ChatClient.TranslateMonthName(MonthCombo);
					System.out.println(numbermonth + " ");

					if (numbermonth > 9) {
						String str = s.toString() + "-" + numbermonth + "-01";

						task1 = "SendMeInfoToIncomeReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation() + " "
								+ str;
					} else {

						String str = s.toString() + "-" + "0" + numbermonth + "-01";
						task1 = "SendMeInfoToIncomeReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation() + " "
								+ str;
					}
					ClientUI.chat.accept(task1);

					XYChart.Series<String, Integer> set1 = tempManeger.SetIncomeByDay(ChatClient.IncomeByDay, 0);
					set1.setName("Day");
					CancelRep.getData().add(set1);

				} else {
					if (string.equals("Weekly")) {
						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";

							task1 = "SendMeInfoToIncomeReportToTheWeek " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task1 = "SendMeInfoToIncomeReportToTheWeek " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						}
						ClientUI.chat.accept(task1);

						CancelRep.getData().clear();

						XYChart.Series<String, Integer> set1 = tempManeger.SetByWeek(ChatClient.IncomeByweek, 0, "");
						CancelRep.getData().add(set1);

					} else if (string.equals("Hourly")) {

						CancelRep.getData().clear();
						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";

							task1 = "SendMeInfoToIncomeReportToTheHour " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task1 = "SendMeInfoToIncomeReportToTheHour " + ChatClient.E1.getOrganizationalAffiliation()
									+ " " + str;
						}
						ClientUI.chat.accept(task1);

						XYChart.Series<String, Integer> set1 = tempManeger.setByHour(ChatClient.IncomeByHour, 0, "");
						CancelRep.getData().add(set1);

					}
				}
			}
		}

	}

	/**
	 * By clicking the Submit button This method is used to check if this report was
	 * previously produced, or if you are trying to produce the report before the
	 * end of the month. If neither this nor that information the information for
	 * this report is stored in DB
	 * 
	 * @param event
	 */
	@FXML
	void SubmitFunc(ActionEvent event) {
		Year s = Year.now();

		String task1 = null;
		int numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo.toString());
		if (numbermonth < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + numbermonth + " " + "Income";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + +numbermonth + " " + "Income";
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
				flag = false;
				;
				String task2 = null;
				if (numbermonth < 9) {
					task2 = "SaveTheReport " + s + " 0" + numbermonth + " " + "Income" + " " + ChatClient.ViewCombo;
				} else {
					task2 = "SaveTheReport " + s + " " + numbermonth + " " + "Income" + " " + ChatClient.ViewCombo;
				}
				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task2); // send message from client to server
				if (ChatClient.getSucceeded() == true) {

					IncomeTxt.setText("");
					circleIncome.setFill(Color.RED);
					StatusIncomeLabel.setText("");
					MessageBox.DisplayMessage("The report was sent to the department manager successfully", "",
							"park maneger", AlertType.INFORMATION);

				} else {
				}
			}
		}
	}

//		} else
//			MessageBox.DisplayMessage("You can only submit the report at the end of the month ", "",
//					"Department manger", AlertType.INFORMATION);

	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (!ChatClient.whois.equals("dep")) {
			if (ChatClient.ViewCombo.equals("Regular"))
				SubmitBtn4.setDisable(false);
			else
				SubmitBtn4.setDisable(true);
			fullreport(ChatClient.ViewCombo, ChatClient.MonthCombo, Year.now().toString());
		} else {
			fullreport(ChatClient.display, ChatClient.MonthCombo, ChatClient.yearreport);
		}

		idtxt.setText(ChatClient.E1.getUsername());

	}

}
