package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.input.MouseEvent;

/**
 * editing an order controller
 *
 */
public class EditAnOrderController implements Initializable {
	TravelersIdentificationPage1Controller t = new TravelersIdentificationPage1Controller();
	@FXML
	private Label ParkName;
	@FXML
	private Label idtxt;

	@FXML
	private Label logoutbtn1;

	@FXML
	private DatePicker DateOfVisit;

	@FXML
	private TextField EmailTxt;

	@FXML
	private Button CancelBtn;

	@FXML
	private Button UpdateBtn;

	@FXML
	private Button DeleteOrderBtn;

	@FXML
	private ComboBox<String> NumberOfVisitors;

	@FXML
	private ComboBox<String> TypeCombo;

	@FXML
	private ComboBox<String> TimeCombo;

	@FXML
	private TextField OrderNum;

	@FXML
	private Button OkBtn;
	public static LocalDate DateOfVisit1;
	public static boolean IsPositiveDifference = false;
	LocalTime clock = LocalTime.now();
	String[] result = clock.toString().split(":");
	String t1 = result[0];

	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	/**
	 * checks is the string is a number or not .
	 * 
	 * @param strNum string
	 * @return true is its number,else return false
	 */
	public boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	/**
	 * This func sends to server the data of the new order that the user did.
	 * 
	 * @param event on action
	 */
	@FXML
	void OkFunc(ActionEvent event) {

		if (isNumeric(OrderNum.getText())) {
			String task = "SearchOrderNumberWithVisitorID" + " " + OrderNum.getText() + " "
					+ ChatClient.v1.getVisitorID();
			ClientUI.chat.accept(task); // send message from client to server
			System.out.println(ChatClient.o1.toString());
			System.out.println("ChatClient.ValidateOrderNumber: " + ChatClient.isValidateOrderNumber());
			if (ChatClient.isValidateOrderNumber()) {
				ParkName.setText(ChatClient.o1.getRequestedpark());

				String[] result = ChatClient.o1.getDateofvisit().split("-");
				LocalDate localDate = LocalDate.of(ChatClient.myAtoi(result[0]), ChatClient.myAtoi(result[1]),
						ChatClient.myAtoi(result[2]));

				DateOfVisit.setValue(localDate);
				TimeCombo.setValue(ChatClient.o1.getVisittime());
				NumberOfVisitors.setValue(ChatClient.o1.getNumberofvisitors());
				EmailTxt.setText(ChatClient.o1.getEmail());
				TypeCombo.setValue(ChatClient.o1.getType());
			} else {
				ParkName.setText("");
				DateOfVisit.setValue(null);
				TimeCombo.setValue("");
				NumberOfVisitors.setValue("");
				EmailTxt.setText("");
				TypeCombo.setValue("");
				MessageBox.DisplayMessage("You do not have the order number as requested", "Sorry :(", "",
						AlertType.WARNING);
			}
		} else
			MessageBox.DisplayMessage("You must enter a valid order number.", "Error", "", AlertType.WARNING);
	}

	/**
	 * This function returns back to Home page for traveler
	 * 
	 * @param event on action event
	 * @throws IOException
	 */
	@FXML
	void CancelFunc(ActionEvent event) throws IOException {
		t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");
	}

	/**
	 * delete an existing order, in case the user already have paid, he will receive
	 * a refund
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void DeleteOrderFunc(ActionEvent event) throws Exception {
		if (ChatClient.ValidateOrderNumber) {
			String info = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
					+ ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getDateofvisit() + " "
					+ ChatClient.o1.getVisittime() + " delete";
			String task = "DeleteDB " + info;
			System.out.println(task);
			ChatClient.setSucceeded(false);
			ClientUI.chat.accept(task); // send message from client to server
			if (ChatClient.getSucceeded().equals(true)) {

				String str = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
						+ ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getDateofvisit() + " "
						+ ChatClient.o1.getVisittime() + " " + ChatClient.o1.getNumberofvisitors();
				task = "CheckInWaitingList " + str;
				ChatClient.setSucceeded(false);
				System.out.println("$1: " + task);
				ClientUI.chat.accept(task); // send message from client to server

				if (ChatClient.getSucceeded().equals(true)) {
					if (ChatClient.o1.getIsPaid().equals("true")) {

						MessageBox.DisplayMessage(
								"The order was canceled and the money was transferred to your bank account.",
								"Delete An Order", "", AlertType.WARNING);
					} else {

						MessageBox.DisplayMessage("Your order has been deleted!", "Delete An Order", "",
								AlertType.WARNING);
					}
					t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

				}
				ChatClient.ValidateOrderNumber = false;
			}
			ChatClient.ValidateOrderNumber = false;
		} else {
			MessageBox.DisplayMessage("You do not have the order number as requested, so we can't delete it",
					"Sorry :(", "", AlertType.ERROR);
		}
	}

	/**
	 * This function will update an existing order and checks the vacancy and all
	 * the relevant details.
	 * 
	 * @param event on action
	 * @throws Exception
	 */
	@FXML
	void UpdateFunc(ActionEvent event) throws Exception {
		String info1, task;
		if (ChatClient.ValidateOrderNumber) {
			if (isError() != null) {

				DateOfVisit1 = DateOfVisit.getValue();
				String info = null;
				System.out.println("6" + DateOfVisit1.toString());

				String type = "";
				switch (TypeCombo.getValue()) {
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

				boolean flag = false;
				boolean flag1 = false;
				int numofvisitors = Integer.parseInt(NumberOfVisitors.getValue())
						- Integer.parseInt(ChatClient.o1.getNumberofvisitors());

				if (numofvisitors > 0) {
					info = ParkName.getText() + " " + DateOfVisit1.toString() + " " + TimeCombo.getValue() + " "
							+ numofvisitors + " " + type;
					if (CheckVacancyDB(info)) {
						System.out.println("$$$$ " + info);
						flag = true;
					} else
						flag = false;
					flag1 = true;

				}else
					flag=true;
				

				info = ParkName.getText() + " " + DateOfVisit1.toString() + " " + TimeCombo.getValue() + " "
						+ NumberOfVisitors.getValue() + " " + type;

				System.out.println("7 " + info);

// Want to check if with the new data there is a place

				if (flag) {
					System.out.println("8 " + info);

// If space is available, update the booking information in DB
					ChatClient.o1.setDateofvisit(DateOfVisit1.toString());
					ChatClient.o1.setVisittime(TimeCombo.getValue());
					ChatClient.o1.setNumberofvisitors(NumberOfVisitors.getValue());
					ChatClient.o1.setType(TypeCombo.getValue());
					ChatClient.o1.setEmail(EmailTxt.getText());
					String OldPrice = ChatClient.o1.getPrice();

					MakeAnOrderPageController makeAn = new MakeAnOrderPageController();
					makeAn.final_price_calculation(ChatClient.o1);

					String NewPrice = ChatClient.o1.getPrice();
					String a1 = NewPrice.substring(0, NewPrice.length() - 1);
					String a2 = OldPrice.substring(0, OldPrice.length());
					System.out.println("a1: " + a1 + "a2: " + a2 + "=");
					int difference = Integer.parseInt(a1) - Integer.parseInt(a2);

					if (ChatClient.o1.getIsPaid().equals("false")) {
						info1 = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
								+ ChatClient.o1.getNoOfOrder() + " " + info + " " + ChatClient.o1.getPrice()
								+ ChatClient.o1.getIsPaid() + " " + "-1 " + ChatClient.o1.getEmail();

					} else {
						if (difference < 0) {
							info1 = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
									+ ChatClient.o1.getNoOfOrder() + " " + info + " " + ChatClient.o1.getPrice()
									+ ChatClient.o1.getIsPaid() + " " + "-1 " + ChatClient.o1.getEmail();

						} else {
							ChatClient.o1.setIsPaid("false");
							info1 = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
									+ ChatClient.o1.getNoOfOrder() + " " + info + " " + ChatClient.o1.getPrice()
									+ ChatClient.o1.getIsPaid() + " " + difference + " " + ChatClient.o1.getEmail();

						}
					}

					task = "Update " + info1;

					System.out.println(task);
					ChatClient.setSucceeded(false);
					ClientUI.chat.accept(task); // send message from client to server
					System.out.println("ChatClient.Succeeded: " + ChatClient.getSucceeded());

					if (ChatClient.getSucceeded().equals(true)) {
						System.out.println("#1");
						if (ChatClient.o1.getIsPaid().equals("true")) {
							
							if (difference > 0)
								MessageBox.DisplayMessage("You have " + difference
										+ " NIS left to pay, you are asked to pay them at the entrance to the park!",
										"Update an order", "", AlertType.WARNING);

							else {
								if (difference < 0)
									MessageBox.DisplayMessage("You have " + -difference
											+ " NIS left over, the money has been transferred back to your bank account.",
											"Update an order", "", AlertType.WARNING);
								else
									MessageBox.DisplayMessage("Your order is update!", "Update an order", "",
											AlertType.WARNING);
							}
						} else {
							MessageBox.DisplayMessage(
									"You will need to display this number at the entrance and exit of the park.",
									"Update an order",
									"Order updated!\r\n" + "The order price has been updated accordingly.\r\n"
											+ "Order number:" + ChatClient.o1.getNoOfOrder() + "\r\n"
											+ "Updated total price:" + NewPrice,
									AlertType.WARNING);
						}
						
						if (numofvisitors <= 0) {
							flag = true;
							System.out.println("_________________" + (-numofvisitors));
							int num = 10 + (-numofvisitors);
							System.out.println("num: " + num);
							String str = ChatClient.o1.getRequestedpark() + " " + ChatClient.v1.getVisitorID() + " "
									+ ChatClient.o1.getNoOfOrder() + " " + ChatClient.o1.getDateofvisit() + " "
									+ ChatClient.o1.getVisittime() + " " + (-numofvisitors);
							task = "CheckInWaitingList " + str;
							ChatClient.setSucceeded(false);
							System.out.println("$1: " + task);
							ClientUI.chat.accept(task); // send message from client to server
							if (ChatClient.getSucceeded().equals(true)) {
								System.out.println("$end");
							}
						}

						t.openPage("HomePageForTravelers1.fxml", event, "Home page for traveler");

					} else
						MessageBox.DisplayMessage(
								"You do not have the order number as requested, so we can't update it", "Sorry :(", "",
								AlertType.ERROR);

					ChatClient.ValidateOrderNumber = false;

				} else {
// Will display a message that there is no place on the requested date and must
// either delete the reservation or try another time.
					if (!flag)
						MessageBox.DisplayMessage(
								"There is no space available at the time you chose, please choose another time that suits you :)",
								"Sorry :(", "", AlertType.WARNING);
					if (!flag1)
						MessageBox.DisplayMessage("Something wrong.", "Sorry :(", "", AlertType.WARNING);
					ChatClient.ValidateOrderNumber = false;

				}
				ChatClient.ValidateOrderNumber = false;

			}
			ChatClient.ValidateOrderNumber = false;
		} else
			MessageBox.DisplayMessage("You do not have the order number as requested, so we can't update it",
					"Sorry :(", "", AlertType.ERROR);
	}

	/**
	 * This object check if all the input values are correct and displaying a
	 * suitable message in case there were a problem.
	 * 
	 * @return true if all the inputs are correct,else null
	 */
	@FXML
	Object isError() {
		DateOfVisit1 = DateOfVisit.getValue();
		LocalDate currentdate = LocalDate.now();
		if (DateOfVisit1 == null || DateOfVisit1.isBefore(currentdate.plusDays(1))) {
			MessageBox.DisplayMessage("You must select a valid date.", "DB Connection", "SQL Error", AlertType.WARNING);
			return null;
		} else {
			if (TimeCombo.getValue() == null) {
				MessageBox.DisplayMessage("you must write the time of the visit.", "DB Connection", "SQL Error",
						AlertType.WARNING);
				return null;
			} else {
				if (EmailTxt.getText().isEmpty() || EmailTxt.getText().contains(" ")) {
					MessageBox.DisplayMessage("you must write valid Email.", "DB Connection", "SQL Error",
							AlertType.WARNING);
					return null;
				} else {
					if (NumberOfVisitors.getValue() == null) {
						MessageBox.DisplayMessage("the number of visitors must be less than 15", "DB Connection",
								"SQL Error", AlertType.WARNING);
						return null;
					} else {
						System.out.println("the value is" + TypeCombo.getValue());
						if (TypeCombo.getValue() == null) {
							MessageBox.DisplayMessage("You must choose your order type", "DB Connection", "SQL Error",
									AlertType.WARNING);
							System.out.println("1");
							return null;
						} else {
							String[] result2 = TimeCombo.getValue().split(":");
							if (ChatClient.myAtoi(result[0]) > ChatClient.myAtoi(result2[0])
									&& DateOfVisit1.equals(currentdate)) {
								MessageBox.DisplayMessage("You have to choose a future time for toaday's order",
										"Error", "", AlertType.WARNING);
								return null;
							} else {
								if (TypeCombo.getValue().equals("Order for family subscription")) {
									System.out.println("2");
									if (Integer.parseInt(NumberOfVisitors.getValue()) > Integer
											.parseInt(ChatClient.v1.getNumOfPeople())) {
										System.out.println("3");
										MessageBox.DisplayMessage(
												"The number you selected is greater than the number of people in this subscription.",
												"Error", "", AlertType.WARNING);
										return null;
									} else {
										System.out.println("4");
										return 1;
									}
								} else {
									System.out.println("5");
									return 1;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Initialize values of hours and number of visitors in comboBoxes and the order
	 * type
	 * 
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idtxt.setText(TravelersIdentificationPage1Controller.Idnumber);
		TimeCombo.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
				"16:00", "17:00", "18:00", "19:00");
		NumberOfVisitors.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15");

		if (ChatClient.isExistTraveler() == false) {
			TypeCombo.getItems().addAll("Standard order");
		} else {
			TypeCombo.getItems().addAll("Standard order");
			if (ChatClient.v1.getType().equals("1"))
				TypeCombo.getItems().addAll("Instructions");
			if (ChatClient.v1.getType().equals("2"))
				TypeCombo.getItems().addAll("Order for family subscription");
		}
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
	 * send message from client to server with the info to check if there is a
	 * vacancy
	 * 
	 * @param info users data
	 * @return true if there is a vacancy, else return false
	 */
	public Boolean CheckVacancyDB(String info) {
		String task = "CheckVacancyDB " + info;
		ClientUI.chat.accept(task);
		return ChatClient.ThereIsVacancy;
	}
}
