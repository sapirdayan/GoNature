package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import logic.Order;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Make an order controller
 *
 */
public class MakeAnOrderPageController implements Initializable {
	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();
	@FXML
	private TextField EmailTxt;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button MakeAnOrderBtn;

	@FXML
	private Button HomeBtn2;

	@FXML
	private Label idtxt;

	@FXML
	private DatePicker DateOfVisit;

	@FXML
	private ComboBox<String> TypeCombo;

	@FXML
	private ComboBox<String> TimeCombo;

	@FXML
	private ComboBox<String> ParkCombo;

	@FXML
	private ComboBox<String> NumberOfVisitors;

	@FXML
	private ComboBox<String> typecombo;

	@FXML
    private Label help;

	public static Order o1 = null;
	public static String order = null;
	public static String task = null;
	public static LocalDate DateOfVisit1;
	public static Integer priceforindevidual = 50;
	LocalTime clock = LocalTime.now();
	String[] result = clock.toString().split(":");
	String t1 = result[0];

	/**
	 * this function return back to the Home page for traveler
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
	}

	/**
	 * making an order to requested park and to do the math for the price and add to
	 * order list
	 * 
	 * @param o1    order
	 * @param event on action
	 * @throws Exception
	 */
	void MakeAnOrderFunc(Order o1, ActionEvent event) throws Exception {

		String type = "";
		switch (o1.getType()) {
		case "Standard order":
			type = "StandardOrder";
			break;
		case "Order for a small group":
			type = "OrderForASmallGroup";
			break;
		case "Order for family subscription":
			type = "OrderForFamilySubscription";
			break;
		case "Instructions":
			type = "Instructions";
			break;
		}

		String info = o1.getRequestedpark() + " " + o1.getDateofvisit() + " " + o1.getVisittime() + " "
				+ o1.getNumberofvisitors();

		final_price_calculation(o1);

		System.out.println("makeanorder price: " + o1.getPrice());
		order = o1.getRequestedpark() + " " + o1.getDateofvisit() + " " + o1.getVisittime() + " "
				+ o1.getNumberofvisitors() + " " + ChatClient.v1.getVisitorID() + " " + o1.getEmail() + " " + type + " "
				+ "Active";

		System.out.println("order: " + order);
		System.out.println("info: " + info);

		if (CheckVacancyDB(info)) {

			if (ChatClient.isExistTraveler() == true) {
				ChatClient.setTypeofWindow("2");

				t.openPage("OrderSuccessfullyReceivedPage.fxml", event, "Order Successfully Received Page");

			} else {
				ChatClient.setTypeofwindow("2");
				task = "AddToTheOrderList " + MakeAnOrderPageController.order + " "
						+ MakeAnOrderPageController.o1.getPrice();

				t.openPage("VisitorDetails.fxml", event, "Visitor details page");

			}

		} else {

			t.openPage("SelectionPageIfThereIsNoSpace1.fxml", event, "Selection Page");
		}

	}

	/**
	 * This object check if all the input values are correct and displaying a
	 * suitable message in case there were a problem.
	 * 
	 */
	@FXML
	void isError(ActionEvent event) throws Exception {
		DateOfVisit1 = DateOfVisit.getValue();
		LocalDate currentdate = LocalDate.now().plusDays(1);
		if (DateOfVisit1 == null || DateOfVisit1.isBefore(currentdate)) {
			MessageBox.DisplayMessage("You must select a valid date.", "DB Connection", "SQL Error", AlertType.WARNING);

		} else {
			if (ParkCombo.getValue() == null) {
				MessageBox.DisplayMessage("you must write the requestedPark", "DB Connection", "SQL Error",
						AlertType.WARNING);

			} else {
				if (TimeCombo.getValue() == null) {
					MessageBox.DisplayMessage("you must write the time of the visit.", "DB Connection", "SQL Error",
							AlertType.WARNING);

				} else {
					if (EmailTxt.getText().isEmpty()||EmailTxt.getText().contains(" ")) {
						MessageBox.DisplayMessage("you must write valid email.", "DB Connection", "SQL Error",
								AlertType.WARNING);

					} else {
						if (NumberOfVisitors.getValue() == null) {
							MessageBox.DisplayMessage("you must write the number of visitors.", "DB Connection",
									"SQL Error", AlertType.WARNING);
						} else {
							System.out.println("the value is" + TypeCombo.getValue());
							if (TypeCombo.getValue() == null)
								MessageBox.DisplayMessage("You must choose your order type", "DB Connection",
										"SQL Error", AlertType.WARNING);
							else {
								String[] result2 = TimeCombo.getValue().split(":");
								if (ChatClient.myAtoi(result[0]) > ChatClient.myAtoi(result2[0])
										&& DateOfVisit1.equals(LocalDate.now()))
									MessageBox.DisplayMessage("You have to choose a future time for toaday's order",
											"Error", "", AlertType.WARNING);

								else {
									if (TypeCombo.getValue().equals("Order for family subscription")) {
										if (Integer.parseInt(NumberOfVisitors.getValue()) > Integer
												.parseInt(ChatClient.v1.getNumOfPeople())) {
											MessageBox.DisplayMessage(
													"The number you selected is greater than the number of people in this subscription.",
													"Error", "", AlertType.WARNING);
										} else {
											o1 = new Order(null, null, null, null, null, null, null, null, null, null);
											o1.setDateofvisit(DateOfVisit1.toString());
											o1.setEmail(EmailTxt.getText());
											o1.setIsDone("waiting");
											o1.setNumberofvisitors(NumberOfVisitors.getValue());
											o1.setRequestedpark(ParkCombo.getValue());
											o1.setVisittime(TimeCombo.getValue());
											o1.setType(TypeCombo.getValue());
											o1.setVisitorID(ChatClient.v1.getVisitorID());
											System.out.println("o1: " + o1);
											MakeAnOrderFunc(o1, event);
										}
									} else {
										o1 = new Order(null, null, null, null, null, null, null, null, null, null);
										o1.setDateofvisit(DateOfVisit1.toString());
										o1.setEmail(EmailTxt.getText());
										o1.setIsDone("waiting");
										o1.setNumberofvisitors(NumberOfVisitors.getValue());
										o1.setRequestedpark(ParkCombo.getValue());
										o1.setVisittime(TimeCombo.getValue());
										o1.setType(TypeCombo.getValue());
										o1.setVisitorID(ChatClient.v1.getVisitorID());
										System.out.println("o1: " + o1);
										MakeAnOrderFunc(o1, event);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Initializing the parameters
	 * 
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		help.setTooltip(new Tooltip("The park closes at 8 and you will have to leave the park before then."));
		idtxt.setText(TravelersIdentificationPage1Controller.Idnumber);
		TimeCombo.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
				"16:00", "17:00", "18:00", "19:00");
		ParkCombo.getItems().addAll("park1", "park2", "park3");

		if (ChatClient.isExistTraveler() == false) {
			TypeCombo.getItems().addAll("Standard order");

		} else {
			TypeCombo.getItems().addAll("Standard order");

			if (ChatClient.v1.getType().equals("1"))
				TypeCombo.getItems().addAll("Instructions");
			if (ChatClient.v1.getType().equals("2"))
				TypeCombo.getItems().addAll("Order for family subscription");
		}
		NumberOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");
	}

	public Boolean CheckVacancyDB(String info) {
		String task = "CheckVacancyDB " + info;
		ChatClient.ThereIsVacancy = false;
		ClientUI.chat.accept(task); // send message from client to server
		return ChatClient.ThereIsVacancy;
	}

	/**
	 * This function logs out and returns to the landing page
	 * 
	 * @param event on mouse click
	 * @throws Exception
	 */
	@FXML
	void logoutfunc(MouseEvent event) throws Exception {
		ChatClient.logoutfunc(event);
	}

	/**
	 * This object check if all the input values are correct and displaying a
	 * suitable message in case there were a problem.
	 * 
	 * @return true if all the inputs are correct,else null
	 */
	Object CheckGuide() {
		if (ChatClient.myAtoi(NumberOfVisitors.getValue()) == 1) {
			MessageBox.DisplayMessage("Booking a guide must include two or more people", "Error window", "Error",
					AlertType.ERROR);
			return null;
		} else
			return 1;
	}

	/**
	 * making math to final price of the order
	 * 
	 * @param o1 order
	 */
	void final_price_calculation(Order o1) {

		Integer Price = null;

		String type1 = o1.getType();
		if (type1.equals("Order for family subscription"))
			type1 = "1";
		else
			type1 = "2";
		String task = "ReturnTheDiscount" + " " + o1.getDateofvisit() + " " + type1 + " " + o1.getRequestedpark();
		ChatClient.setSucceeded(false);
		System.out.println("task: " + task);
		ClientUI.chat.accept(task);
		if (ChatClient.getSucceeded() == true) {
			Integer[] arr = ChatClient.Discount;
			for (int i = 0; i < ChatClient.Discount.length; i++)
				System.out.println(ChatClient.Discount[i]);
			System.out.println("priceforindevidual: " + priceforindevidual);

			System.out.println("###########################################");
			for (int i = 0; i < arr.length; i++)
				priceforindevidual = (priceforindevidual - ((arr[i] * priceforindevidual) / 100));
			System.out.println(priceforindevidual);
			Price = priceforindevidual;
			priceforindevidual = 50;
			System.out.println("o1.getType(): " + o1.getType());
			if (o1.getType().equals("Standard order") || (o1.getType().equals("Order for a small group"))) {
				System.out.println("defult"); // send to server to return the discount
				Price = Price - ((Price * 15) / 100);
				Price = Price * (ChatClient.myAtoi(o1.getNumberofvisitors()));
				ChatClient.totalPrice = Price + " ";
				System.out.println("the price1 is" + ChatClient.totalPrice);
			}
			if (o1.getType().equals("Order for family subscription")) {

				System.out.println("family");

				Price = Price - ((Price * 15) / 100);
				Price = Price - ((Price * 20) / 100);
				Price = Price * (ChatClient.myAtoi(o1.getNumberofvisitors()));
				ChatClient.totalPrice = Price + " ";
				System.out.println("the price2 is" + ChatClient.totalPrice);
			}
			if (o1.getType().equals("Instructions")) {
				System.out.println("Instructions");

				if (ChatClient.myAtoi(o1.getNumberofvisitors()) > 1) {
					Price = Price - ((Price * 25) / 100);
					Price = Price * (ChatClient.myAtoi(o1.getNumberofvisitors()) - 1);
					ChatClient.totalPrice = Price + " ";
					System.out.println("the price3 is" + ChatClient.totalPrice);
				} else if (ChatClient.myAtoi(o1.getNumberofvisitors()) == 1) {
					Price = Price - ((Price * 25) / 100);
					Price = Price * (ChatClient.myAtoi(o1.getNumberofvisitors()));
					ChatClient.totalPrice = Price + " ";
				}

				System.out.println("the total price is" + ChatClient.totalPrice);
			}

			o1.setPrice(ChatClient.totalPrice);
			System.out.println("ChatClient.totalPrice: " + ChatClient.totalPrice);

		} else
			MessageBox.DisplayMessage("Something wrong", "DB Connection", "SQL Error", AlertType.WARNING);

	}
}
