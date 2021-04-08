package gui;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


 
/**
 *  A class that operates the park employee's home page.
 */
public class HomePageRegularEmployeeController implements Initializable {

    @FXML
    private Label idtxt;

    @FXML
    private ImageView SearchForAPaymentBtn;

    @FXML
    private ImageView CheckingTheVacancyBtn;

    @FXML
    private ImageView MakeAnOrderBtn;

    @FXML
    private ImageView ExitBtn;

    @FXML
    private ImageView ContactUsBtn;

    @FXML
    private Label LogoutBtn;

    EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();
    
    /**
     * A method that takes us to the screen to check a free space in the park.
     * @param event
     * @throws IOException
     */
    @FXML
    void CheckingTheVacancyFunc(MouseEvent event) throws IOException {
    	ChatClient.setTypeofVacancywindow("2");
    	e.openPage("Checkingthevacancy1.fxml", event, "Checking the vacancy Page");
    }

    @FXML
    void ContactUsFunc(MouseEvent event) throws IOException {
    	e.openPage("ContactUs.fxml", event, "Contact us page");
    }

    /**
     * A method that takes us to a screen that receives information from the ticket reader at the exit of the park.
     * @param event
     * @throws IOException
     */
    @FXML
    void ExitFunc(MouseEvent event) throws IOException {
    	e.openPage("ExitOfVisitor1.fxml", event, "Exit page");

    }

    @FXML
    void LogoutFunc(MouseEvent event) throws Exception {
    	ChatClient.logoutfunc(event);
    }

    /**
     * A method that takes us to the screen to place an invitation for an occasional visit.
     * @param event
     * @throws IOException
     */
    @FXML
    void MakeAnOrder(MouseEvent event) throws IOException {
    	//To update which page to go to
    	ChatClient.setTypeofwindow("2");
    	ChatClient.setTypeofCreditwindow("1");
    	e.openPage("TravelersIdentificationPage2.fxml", event, "Travelers Identification Page");
    }

    /**
     * A method that takes us to a screen that receives information from the ticket reader at the entrance to the park.
     * @param event
     * @throws IOException
     */
    @FXML
    void SearchForAPaymentFunc(MouseEvent event) throws IOException {
    	e.openPage("SearchForAPaymentPage1.fxml", event, "Searching page");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idtxt.setText(ChatClient.E1.getUsername());
	}

}
