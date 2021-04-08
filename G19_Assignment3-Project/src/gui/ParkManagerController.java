package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Class in charge of the park manager's home page
 * 
 * @author hana
 *
 */
public class ParkManagerController implements Initializable {
	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
	@FXML
	private Label idtxt;

	@FXML
	private Label DeterminingADiscount;

	@FXML
	private Label SettingParameters;

	@FXML
	private ImageView ContactUsBtn;

	@FXML
	private Label LogoutBtn;

	@FXML
	private ComboBox<String> ReporeCombo;

	@FXML
	private Button NumberVisitors;

	@FXML
	void ContactUsFunc(MouseEvent event) throws IOException {

		e.openPage("ContactUs.fxml", event, "Contact us page");
	}

	/**
	 * Method that opens the relevant report page
	 * 
	 * @param event on ActionEvent
	 * @throws IOException
	 */
	@FXML
	void CreateReport(ActionEvent event) throws IOException {
		ChatClient.whois = "mang";
		if (ReporeCombo.getValue() == null) {
			MessageBox.DisplayMessage(
					"Park maneger, \nYou must choose the name of the report you want to generate for the department manager ",
					"Error", "Error", AlertType.WARNING);
		} else {
			if (ReporeCombo.getValue().equals("Total number of visitors report")) {
				ChatClient.TypeOfReport = "visitors";
				e.openPage("BeforeReport.fxml", event, "Choosing report");
			} else {
				if (ReporeCombo.getValue().equals("Income report for a specific month")) {
					ChatClient.TypeOfReport = "Income";
					e.openPage("BeforeReport.fxml", event, "Choosing report");
				} else {
					ChatClient.TypeOfReport = "Occupancy";
					e.openPage("BeforeReport.fxml", event, "Choosing report");
				}

			}
		}
	}

	/**
	 * Opens a confirmation page or disapproval of parameters sent by the park
	 * manager
	 * 
	 * @param event on MouseEvent
	 * @throws IOException
	 */
	@FXML
	void DeterminingADiscountFunc(MouseEvent event) throws IOException {
		e.openPage("DeterminingADiscount1.fxml", event, "Determining a discount page");
	}

	@FXML
	void LogoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * A method that returns a number of visitors present in the park
	 * 
	 * @param event on ActionEvent
	 */
	@FXML
	void NumberVisitorsFunc(ActionEvent event) {

		String task = "ReturnNumberOfVisitorsPresentInThePark " + ChatClient.E1.getOrganizationalAffiliation();
		ClientUI.chat.accept(task); // send message from client to server

		String NumberOfVisitor = "The Number of visitors present in the park is: " + ChatClient.NumerOfVisitorInThePark;
		MessageBox.DisplayMessage(NumberOfVisitor, "Information for park maneger", "Information for park maneger",
				AlertType.WARNING);
	}

	/**
	 * Opens a confirmation page or disapproval of parameters sent by the park
	 * manager
	 * 
	 * @param event on MouseEvent
	 * @throws IOException
	 */
	@FXML
	void SettingParametersFunc(MouseEvent event) throws IOException {
		e.openPage("SettingParameters1.fxml", event, "Setting parameters page");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
		ReporeCombo.getItems().addAll("Total number of visitors report", "Usage report",
				"Income report for a specific month");
	}

	void returnToTheHomePage(ActionEvent event) throws IOException {
		e.openPage("HomePageParkManeger1.fxml", event, "Home page park maneger");

	}

	Object chcekfields(String MonthCombo, String string) {
		if (string == null) {
			MessageBox.DisplayMessage("You should choose the month before before click ok.", "", "Park maneger",
					AlertType.WARNING);
			return null;

		} else {
			if (MonthCombo == null) {

				MessageBox.DisplayMessage("You should choose the report view  before click ok.", "", "Park maneger",
						AlertType.WARNING);
				return null;
			} else
				return true;

		}
	}

	public Object checkfullreport(Boolean flag) {
		if (flag == false) {
			MessageBox.DisplayMessage("The report is empty", "", "Park maneger", AlertType.WARNING);
			return null;
		} else
			return true;

	}

	XYChart.Series<String, Integer> SetIndevdualRegularSetINParChart(int number, String name, String namecoulmn) {
		XYChart.Series<String, Integer> set1 = new XYChart.Series<String, Integer>();
		set1.setName(name);
		set1.getData().add(new XYChart.Data<>(namecoulmn, new Integer(number)));
		System.out.println(set1);
		return set1;
	}

	/**
	 * Function for filling a monthly view form for a report
	 * 
	 * @param m      the array if information
	 * @param number Where to start from the array
	 * @param name   name the name of the Series in bar chart
	 * @return
	 */
	XYChart.Series<String, Integer> SetByDay(String[] m, int number, String name) {
		XYChart.Series<String, Integer> set1 = new XYChart.Series<String, Integer>();

		set1.setName(name);
		for (int i = 1; i < 32; i++) {
			set1.getData().add(new XYChart.Data<>("Day" + i, new Integer(ChatClient.myAtoi(m[number + i - 1]))));
		}

		return set1;
	}

	/**
	 * Function for filling a daily view form for a report
	 * 
	 * @param m      the array if information
	 * @param number Where to start from the array
	 * @return
	 */
	XYChart.Series<String, Integer> SetIncomeByDay(String[] m, int number) {
		XYChart.Series<String, Integer> set1 = new XYChart.Series<String, Integer>();
		for (int i = 1; i < 32; i++) {
			set1.getData().add(new XYChart.Data<>("Day" + i, new Integer(ChatClient.myAtoi(m[number + i - 1]))));
		}

		return set1;
	}

	/**
	 * 
	 * @param m      the array if information
	 * @param number Where to start from the array
	 * @param name   the name of the Series in bar chart
	 * @return
	 */
	XYChart.Series<String, Integer> SetByWeek(String[] m, int number, String name) {
		Series<String, Integer> set1 = new XYChart.Series<>();
		set1.setName(name);
		set1.getData().addAll(new XYChart.Data<>("Week1", new Integer(ChatClient.myAtoi(m[number]))),
				new XYChart.Data<>("Week2", new Integer(ChatClient.myAtoi(m[number + 1]))),
				new XYChart.Data<>("Week3", new Integer(ChatClient.myAtoi(m[number + 2]))),
				new XYChart.Data<>("Week4", new Integer(ChatClient.myAtoi(m[number + 3]))),
				new XYChart.Data<>("Week5", new Integer(ChatClient.myAtoi(m[number + 4]))));

		return set1;
	}

	/**
	 * To fill out a report in an hourly view
	 * 
	 * @param m      the array if information
	 * @param number number Where to start from the array
	 * @param name   name the name of the Series in bar chart
	 * @return
	 */
	XYChart.Series<String, Integer> setByHour(String[] m, int number, String name) {
		XYChart.Series<String, Integer> set1 = new XYChart.Series<String, Integer>();
		set1.setName(name);
		set1.getData().add(new XYChart.Data<>("07:00", ChatClient.myAtoi(m[number])));
		set1.getData().add(new XYChart.Data<>("08:00", ChatClient.myAtoi(m[number + 1])));
		set1.getData().add(new XYChart.Data<>("09:00", ChatClient.myAtoi(m[number + 2])));

		set1.getData().add(new XYChart.Data<>("10:00", ChatClient.myAtoi(m[number + 3])));
		set1.getData().add(new XYChart.Data<>("11:00", ChatClient.myAtoi(m[number + 4])));
		set1.getData().add(new XYChart.Data<>("12:00", ChatClient.myAtoi(m[number + 5])));

		set1.getData().add(new XYChart.Data<>("13:00", ChatClient.myAtoi(m[number + 6])));

		set1.getData().add(new XYChart.Data<>("14:00", ChatClient.myAtoi(m[number + 7])));
		set1.getData().add(new XYChart.Data<>("15:00", ChatClient.myAtoi(m[number + 8])));
		set1.getData().add(new XYChart.Data<>("16:00", ChatClient.myAtoi(m[number + 9])));

		set1.getData().add(new XYChart.Data<>("17:00", ChatClient.myAtoi(m[number + 10])));
		set1.getData().add(new XYChart.Data<>("18:00", ChatClient.myAtoi(m[number + 11])));
		set1.getData().add(new XYChart.Data<>("19:00", ChatClient.myAtoi(m[number + 12])));

		return set1;

	}

}
