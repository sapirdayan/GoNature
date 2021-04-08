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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


/**
 * A class that describes the screen of the cancellations report that the
 * department manager produces. This report has several views and the relevant
 * graph is displayed according to the view.
 * 
 * @author Nasmat Amr
 *
 */
public class CancellationReport implements Initializable {

	@FXML
	private BarChart<String, Integer> CancelRep;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	@FXML
	private Button Okbutton;

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private Button BackBtn4;
	@FXML
	private Label cancel;

	@FXML
	private Label Notcanel;

	@FXML
	private Button SubmitBtn4;
	Boolean flag = false;

	@FXML
	private Pane Canelpage;
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	/**
	 * A method that takes us back to the home page of a department manager
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		if (ChatClient.whois.equals("dep")) {
			e.openPage("BeforeReport.fxml", event, "Choosing department maneger");
		} else {
			e.openPage("BeforeReport.fxml", event, "Choosing park maneger");
		}
	}

	/**
	 * A method that shows me the relevant graph according to the user's request. In
	 * this method, we request information from the DB to display it on the screen
	 * graphically.
	 */
	void ViewFunc() {
		ParkManagerController tempManeger = new ParkManagerController();
		if (tempManeger.chcekfields(ChatClient.MonthCombo, ChatClient.ViewCombo) != null) {
			flag = true;
			String task;
			Year s = Year.now();
			int numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo);
			if (numbermonth > 9) {
				String str = s.toString() + "-" + numbermonth + "-01";

				task = "SendMeInfoToCancellationReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			} else {

				String str = s.toString() + "-" + "0" + numbermonth + "-01";
				task = "SendMeInfoToCancellationReportToTheMonth " + ChatClient.E1.getOrganizationalAffiliation() + " "
						+ str;
			}
			ClientUI.chat.accept(task);
			/////////////////////////////////////////////////////

			cancel.setText("Total canceled orders " + ChatClient.CancellationByMonth[0]);
			Notcanel.setText("Total unfulfilled orders " + ChatClient.CancellationByMonth[1]);
			if (ChatClient.ViewCombo.equals("Regular")) {
				SubmitBtn4.setDisable(false);
				CancelRep.getData().clear();

				XYChart.Series<String, Integer> set1 = tempManeger.SetIndevdualRegularSetINParChart(
						ChatClient.myAtoi(ChatClient.CancellationByMonth[0]), "Cancellation orders",
						"Cancellation orders");
				CancelRep.getData().add(set1);

				XYChart.Series<String, Integer> set2 = tempManeger.SetIndevdualRegularSetINParChart(
						ChatClient.myAtoi(ChatClient.CancellationByMonth[1]), "Not fulfilled orders",
						"Not fulfilled orders");
				CancelRep.getData().add(set2);

			} else {
				if (ChatClient.ViewCombo.equals("By day")) {
					SubmitBtn4.setDisable(true);
					CancelRep.getData().clear();
					numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo);
					System.out.println(numbermonth + " ");

					if (numbermonth > 9) {
						String str = s.toString() + "-" + numbermonth + "-01";

						task = "SendMeInfoToCancellationReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation()
								+ " " + str;
					} else {

						String str = s.toString() + "-" + "0" + numbermonth + "-01";
						task = "SendMeInfoToCancellationReportToTheDay " + ChatClient.E1.getOrganizationalAffiliation()
								+ " " + str;
					}
					ClientUI.chat.accept(task);
					////////////////////////////////////////////
					XYChart.Series<String, Integer> set1 = tempManeger.SetByDay(ChatClient.CancellationByDay, 0,
							"Cancellation orders");
					CancelRep.getData().add(set1);
					XYChart.Series<String, Integer> set2 = tempManeger.SetByDay(ChatClient.CancellationByDay, 31,
							"Not fulfilled orders");
					CancelRep.getData().add(set2);
				} else {
					if (ChatClient.ViewCombo.equals("Weekly")) {
						SubmitBtn4.setDisable(true);
						CancelRep.getData().clear();
						numbermonth = ChatClient.TranslateMonthName(ChatClient.MonthCombo);

						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";
							task = "SendMeInfoToCancellationReportToTheWeek "
									+ ChatClient.E1.getOrganizationalAffiliation() + " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task = "SendMeInfoToCancellationReportToTheWeek "
									+ ChatClient.E1.getOrganizationalAffiliation() + " " + str;
						}
						ClientUI.chat.accept(task);
						///////////////////////////////////////////////
//						
						XYChart.Series<String, Integer> set1 = tempManeger.SetByWeek(ChatClient.CancellationByweek, 0,
								"Cancellation orders");
						CancelRep.getData().add(set1);

						XYChart.Series<String, Integer> set2 = tempManeger.SetByWeek(ChatClient.CancellationByweek, 5,
								"Not fulfilled orders");
						CancelRep.getData().add(set2);

					} else if (ChatClient.ViewCombo.equals("Hourly")) {
						SubmitBtn4.setDisable(true);

						CancelRep.getData().clear();

						if (numbermonth > 9) {
							String str = s.toString() + "-" + numbermonth + "-01";

							task = "SendMeInfoToCancellationReportToTheHour "
									+ ChatClient.E1.getOrganizationalAffiliation() + " " + str;
						} else {

							String str = s.toString() + "-" + "0" + numbermonth + "-01";
							task = "SendMeInfoToCancellationReportToTheHour "
									+ ChatClient.E1.getOrganizationalAffiliation() + " " + str;
						}
						ClientUI.chat.accept(task);

						////////////////////////////////////////

						XYChart.Series<String, Integer> set1 = tempManeger.setByHour(ChatClient.CancellationByHour, 0,
								"Cancellation orders");
						CancelRep.getData().add(set1);

						XYChart.Series<String, Integer> set2 = tempManeger.setByHour(ChatClient.CancellationByHour, 13,
								"Not fulfilled orders");
						CancelRep.getData().add(set2);

					}
				}
			}
		}

	}

	/**
	 * In this method I pass information about the report and its display to the DB
	 * to allow the department manager to view it later.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void SubmitFunc(ActionEvent event) {
		flag = false;
		Year s = Year.now();

		String task1 = null;
		if (ChatClient.myAtoi(ChatClient.MonthCombo) < 9) {
			task1 = "CheckIfThereIsReportLikeThat " + s + " 0" + ChatClient.myAtoi(ChatClient.MonthCombo) + " "
					+ "Canelrep";
		} else {
			task1 = "CheckIfThereIsReportLikeThat " + s + " " + +ChatClient.myAtoi(ChatClient.MonthCombo) + " "
					+ "Canelrep";
		}

		ChatClient.setSucceeded(false);
		CancelRep.getData().clear();
		ClientUI.chat.accept(task1); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			System.out.println("ChatClient.IsExist: " + ChatClient.IsExist);
			if (ChatClient.IsExist.equals("Yes")) {
				MessageBox.DisplayMessage("You have already submitted this report", "", "Department maneger",
						AlertType.INFORMATION);
			} else {
				cancel.setText("");
				Notcanel.setText("");
				String task2 = null;
				if (ChatClient.myAtoi(ChatClient.MonthCombo) < 9) {
					task2 = "SaveTheReport " + s + " 0" + ChatClient.myAtoi(ChatClient.MonthCombo) + " " + "Canelrep"
							+ " " + ChatClient.ViewCombo;
				} else {
					task2 = "SaveTheReport " + s + " " + ChatClient.myAtoi(ChatClient.MonthCombo) + " " + "Canelrep"
							+ " " + ChatClient.ViewCombo;
				}

				ChatClient.setSucceeded(false);
				ClientUI.chat.accept(task2); // send message from client to server
				if (ChatClient.getSucceeded().equals(true)) {
					cancel.setText("");
					Notcanel.setText("");
					CancelRep.getData().clear();
					MessageBox.DisplayMessage("The report is submeted.", "", "Department maneger", AlertType.WARNING);
				}

			}
		}
	}

	
	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		ViewFunc();
	}
}
