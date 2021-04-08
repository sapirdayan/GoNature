package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * The department responsible for the information selection page that the
 * department manager wants to see from the reports produced for him by the park
 * manager
 * 
 * @author hana
 *
 */

public class reportChoosingDepController implements Initializable {

	@FXML
	private Pane LabelStatus;

	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtntxt;

	@FXML
	private ComboBox<String> MonthhCombo;

	@FXML
	private Button BackBtn;

	@FXML
	private Button Okeybtn;
	@FXML
	private ComboBox<String> YearComboNumber;
	public static String typemaneger = "";

	EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

	@FXML
	void BackFunc(ActionEvent event) throws IOException {
		if (typemaneger.equals("Department maneger"))
			e.openPage("HomePageDepartmentManeger1.fxml", event, "Home Page Department Maneger");
		else {
			ParkManagerController home = new ParkManagerController();
			home.returnToTheHomePage(event);
		}
	}

	/**
	 * Filling in the data in the auditors' report
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ComboViewFunc(ActionEvent event) throws IOException {
		if (MonthhCombo.getValue() != null && YearComboNumber.getValue() != null) {

			String task = null;
			int numbermonth = ChatClient.TranslateMonthName(MonthhCombo.getValue().toString());
			if (numbermonth < 9)
				task = "SendDetalsReportVisitors " + YearComboNumber.getValue() + " " + "0" + numbermonth + " "
						+ ChatClient.type;
			else
				task = "SendDetalsReportVisitors " + YearComboNumber.getValue() + " " + numbermonth + " "
						+ ChatClient.type;
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.getSucceeded() == true) {

				if (ChatClient.display.equals("No")) {
					MessageBox.DisplayMessage("The report doesn't Exit", "", typemaneger, AlertType.INFORMATION);
				} else {

					ChatClient.MonthCombo = MonthhCombo.getValue();
					ChatClient.yearreport = YearComboNumber.getValue();
					if (ChatClient.TypeOfReport.equals("visitors")) {
						e.openPage("VisitorsReportDepartmentManeger.fxml", event, "Total number of visitors report");
					} else {
						if (ChatClient.TypeOfReport.equals("Income")) {
							e.openPage("IncomereportsDepartmentManger.fxml", event, "Income report");
						} else
							e.openPage("OccpancyDepartmentManger.fxml", event, "Occpancy report");
					}
				}
			}
		} else {
			MessageBox.DisplayMessage("You must fill the two fields before click 'Ok' ", "", typemaneger,
					AlertType.INFORMATION);

		}
	}

	@FXML
	void logoutFunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	@FXML
	void monthhCombo(ActionEvent event) {
		ChatClient.MonthCombo = MonthhCombo.getValue();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (ChatClient.whois.equals("dep"))
			typemaneger = "Department maneger";
		else
			typemaneger = "Park maneger";
// TODO Auto-generated method stub
		MonthhCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");
		YearComboNumber.getItems().addAll("2010", "2011", "2012", "2013", "2014", "2015", "2020", "2021");
	}
}
