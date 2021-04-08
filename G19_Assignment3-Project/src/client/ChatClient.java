// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import gui.EmployeeIdentificationPage1Controller;
import gui.LandingPageContoller;
import gui.MessageBox;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Employee;
import logic.Order;
import logic.Park;
import logic.Discount;
import logic.Visitor;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ChatClient extends AbstractClient {
// Instance variables ****************

	ChatIF clientUI;
	public static String[] DiscountsList = null;
	public static boolean ForApproval = false;
	public static Order o1 = new Order(null, null, null, null, null, null, null, null, null, null);
	public static Visitor v1 = new Visitor(null, null, null, null, null, null, null, null, null, null, null);
	public static Discount D1 = new Discount(null, null, null, null, null, null, null);
	public static Employee E1 = new Employee(null, null, null, null, null, null, null);
	public static boolean awaitResponse = false;
	public static boolean ThereIsVacancy = false;
	public static boolean ValidateOrderNumber = false;
	public static boolean ValidateUserName = false;
	public static boolean ValidatePassword = false;
	public static boolean isEmployee = false;
	public static boolean isTraveler = false;
	public static Boolean NotExistGuide = false;
	public static Boolean Succeeded = false;
	public static String ExistOrder = "";
	public static boolean ExistTraveler = false;
	public static Boolean NotExistfamiltsubscription = false;
	public static Integer[] Discount = null;
	public static String totalPrice = null;
	public static String typeofIdentificationwindow = "";
	public static String typeofVacancywindow = "";
	public static String Availability = "";
	public static String typeofCreditwindow = "";
	public static String typeofWindow = "";
	public static String staytime = null;
	public static String entertime1 = null;
	public static String entertime2 = null;
	public static String Statusentertime = null;
	public static int[] arrayInfo = null;
	public static String TypeOfView = "";
	public static String SingleVisitors = null;
	public static String OrgnaizedVisitor = null;
	public static String Subscribers = null;
	public static String totalVistors = null;
	public static int[] IncomeDayInt = new int[31];
	public static int TotalIncome = 0;
	public static String parameters = null;
	public static String reports = null;
	public static String discount = null;
	public static boolean VisitorsReport1 = false;
	public static boolean IncomeReport1 = false;
	public static boolean OccupancyReport1 = false;
	public static boolean discount1 = false;
	public static int FullMonth;
	public static String VisitorsReport = null;
	public static String IncomeReport = null;
	public static String OccupancyReport = null;
	public static String Gapparameters = null;
	public static String MaxqoutaParameters = null;
	public static String StayTimeParameters = null;
	public static boolean Gapparameters1 = false;
	public static boolean MaxqoutaParameters1 = false;
	public static boolean StayTimeParameters1 = false;
	public static String[] IncomeDayString = new String[31];
	public static String[] FullDayes = new String[31];
	public static String Reminder = null;
	public static List<String> discountlist = new ArrayList<>();
	public static List<String> DiscountDetails = new ArrayList<>();
	public static String whois = null;
	public static String isdep = null;
	public static String display = null;
	public static String type = null;
	public static String IsExist = null;
	public static int[] FullBymonth = new int[32];
	public static String TypeOfReport = null;
	public static String yearreport = null;
	public static String TypeReportMang = null;
	public static String YearCombo = null;
	public static String[] VisitorByMonth = new String[3];
	public static String[] VisitorByDay = new String[93];
	public static String[] VisitorByweek = new String[15];
	public static String[] VisitorByHour = new String[45];
	public static String[] IncomeByMonth = new String[3];
	public static String[] IncomeByDay = new String[93];
	public static String[] IncomeByweek = new String[5];
	public static String[] IncomeByHour = new String[45];
	public static String[] CancellationByMonth = new String[2];
	public static String[] CancellationByDay = new String[62];
	public static String[] CancellationByweek = new String[10];
	public static String[] CancellationByHour = new String[45];
	public static String[] FullByDay = new String[13];
	public static String[] FullByweek = new String[5];
	public static String[] FullByHour = new String[20];
	public static int NumerOfVisitorInThePark = 0;
	public static String[] arrayesOfOrdersInWaitingList = null;
	public static String[] InformationToTheOrders = new String[10];
	public static boolean IsConnected = false;
	public static String[] FullByDayAndMonth = new String[100];
	public static String NumberOfDays = null;
	public static int length = 0;
	public static String[] VisitorBytothisdate = new String[4];
	public static String[] VisitorByMonthAndYear = new String[100];
	public static String[] IncomeByMonthAndYear = new String[100];
	public static String[] Entertimeregular = new String[36];
	public static String MonthCombo;
	public static LocalDate ComboDay;
	public static String ViewCombo;
	public static List<String> arrayTimes = new ArrayList<>();

// Constructors ******************

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;

	}

// Instance methods ****************
	/**
	 * handle meesage from server- Get an ArrayList from the server and divide it
	 * into an array of strings
	 */
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof ArrayList) {

			List<String> l = (List<String>) msg;
			if (l.get(0).equals("GiveMeTheReminder"))
				Reminder = l.get(1);
			else {

				System.out.println("--> handleMessageFromServer");
				awaitResponse = false;
				String[] result = ConvertMsgToStringArray(msg);
				switch (result[0].toString()) {
				case "indeftifictionTraveler":
					isTraveler = true;
					isEmployee = false;

					switch (result[1].toString()) {
					case "TheVisitorConnectAlready":
						IsConnected = false;
						break;
					case "Existing":
						IsConnected = true;
						setExistTraveler(true);
						String type = result[7];
						if (type.equals("2")) {
							v1.setFirstName(result[2]);
							v1.setLastName(result[3]);
							v1.setVisitorID(result[4]);
							v1.setMail(result[5]);
							v1.setPhoneNumber(result[6]);
							v1.setType(result[7]);
							v1.setNumOfPeople(result[8]);
							v1.setCardNumber(result[9]);
							v1.setCreditCardValidity(result[10]);
							v1.setCVVNumber(result[11]);
						} else {
							v1.setFirstName(result[2]);
							v1.setLastName(result[3]);
							v1.setVisitorID(result[4]);
							v1.setMail(result[5]);
							v1.setPhoneNumber(result[6]);
							v1.setType(result[7]);
						}

						break;

					case "NotExisting":
						IsConnected = true;
						v1.setVisitorID(result[2]);
						setExistTraveler(false);
						break;
					}
					break;

				case "CheckVacancyDB":

					if (result[1].equals("yes")) {
						ThereIsVacancy = true;
						setAvailability(result[2]);
					} else {
						ThereIsVacancy = false;
						setAvailability(result[2]);
					}
					break;

				case "UpdateOrder":
					setSucceeded(true);
					break;

				case "CheckInWaitingList":
					setSucceeded(true);
					break;

				case "SendMeOrdersToConfirm":
					String st = "";
					arrayesOfOrdersInWaitingList = new String[result.length - 1];
					for (int i = 1; i < result.length; i++) {

						st = result[i].replace("#", " ");

						ChatClient.arrayesOfOrdersInWaitingList[i - 1] = st;
					}

					if (arrayesOfOrdersInWaitingList[0].equals("ThereIsNotOrders"))
						ChatClient.setSucceeded(false);
					else {
						ChatClient.setSucceeded(true);

					}

					break;

				case "SendMeOrdersInWaitingListToThisId":
					st = "";
					arrayesOfOrdersInWaitingList = new String[result.length - 1];
					for (int i = 1; i < result.length; i++) {

						st = result[i].replace("#", " ");

						ChatClient.arrayesOfOrdersInWaitingList[i - 1] = st;
					}
					if (arrayesOfOrdersInWaitingList[0].equals("ThereIsNotOrders"))
						ChatClient.setSucceeded(false);
					else
						ChatClient.setSucceeded(true);
					break;

				case "SentMeAllTheInfoToThisOrder":
					int j = 0;
					if (!result[1].equals("ThereIsNoOrders")) {
						ForApproval = true;
					} else
						ForApproval = false;

					o1 = new Order(null, null, null, null, null, null, null, null, null, null);
					o1.setDateofvisit(result[5]);
					o1.setEmail(result[7]);
					o1.setNoOfOrder(result[9]);
					o1.setNumberofvisitors(result[4]);
					o1.setPrice(result[8]);
					o1.setRequestedpark(result[1]);
					o1.setType(result[2]);
					o1.setVisittime(result[3]);
					o1.setVisitorID(result[6]);
					o1.setIsPaid("false");
					o1.setIsDone(result[10]);
					ChatClient.totalPrice = result[8];

					for (int i = 1; i < result.length; i++) {
						ChatClient.InformationToTheOrders[j] = result[i];
						j++;
					}

					break;

				case "SentMeTheOrderDetails":
					j = 0;
					if (!result[1].equals("no")) {
						ForApproval = true;
					} else
						ForApproval = false;

					for (int i = 1; i < result.length; i++) {
						ChatClient.InformationToTheOrders[j] = result[i];
						j++;
					}
					o1 = new Order(null, null, null, null, null, null, null, null, null, null);
					o1.setDateofvisit(result[5]);
					o1.setEmail(result[7]);
					o1.setNoOfOrder(result[9]);
					o1.setNumberofvisitors(result[4]);
					o1.setPrice(result[8]);
					o1.setRequestedpark(result[1]);
					o1.setType(result[2]);
					o1.setVisittime(result[3]);
					o1.setVisitorID(result[6]);
					o1.setIsPaid("false");
					o1.setIsDone(result[10]);

					break;

				case "DeterminingDiscount":
					if (result[1].equals("Succeeded"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "GiveMeDiscountDetails":
					String type = "";
					if (result.length == 1)
						setSucceeded(false);
					else {
						D1.setDiscountNum(result[7]);
						D1.setDiscountValue(result[6]);
						D1.setEndDate(result[5]);
						D1.setIsApproval(result[2]);
						D1.setPark(result[1]);
						D1.setStartDate(result[4]);
						if (result[3].equals("1"))
							type = "For family subscription";
						else
							type = "For regular travelers";

						D1.setTypeOfDiscount(type);
						setSucceeded(true);
					}

					for (int i = 0; i < result.length - 1; i++) {
						DiscountDetails.add(result[i + 1]);
					}
					break;

				case "DiscountsThatRequireApproval":
					if (result.length == 1)
						setSucceeded(false);
					else {
						DiscountsList = new String[result.length - 1];

						type = "";
						for (int i = 1; i < result.length; i++) {
							DiscountsList[i - 1] = "";

							String[] str = result[i].split("#");
							if (str[1].equals("1"))
								type = "For family subscription";
							else
								type = "For regular travelers";

							for (int q = 1; q < str.length; q++) {
								if (q == 1)
									DiscountsList[i - 1] += type + " ";
								else if (q == 4) {
									DiscountsList[i - 1] += str[q] + "%";
								} else
									DiscountsList[i - 1] += str[q] + " ";
							}
						}
						setSucceeded(true);
					}
					break;

				case "RefusedDiscount":
					if (!result[1].equals("no"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "ApprovedDiscount":
					if (!result[1].equals("no"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "ReturnTheDiscount":
					if (!result[1].equals("ThereIsNoDiscount")) {
						Discount = new Integer[result.length - 1];

						for (int i = 1; i < result.length; i++) {

							Discount[i - 1] = Integer.parseInt(result[i]);

						}

						setSucceeded(true);
					} else {
						setSucceeded(true);
						Discount = new Integer[result.length - 1];
						Discount[0] = 0;
					}

					break;
				case "UpdateTheReminder":
					setSucceeded(true);
					break;

				case "UpdateOrderToDeleted1":
					if (!result[1].equals("no"))
						setSucceeded(true);
					else
						setSucceeded(false);

					break;

				case "UpdateOrderToConfirm1":
					if (!result[1].equals("no")) {
						setSucceeded(true);
					}

					else
						setSucceeded(false);

					break;

				case "UpdateOrderToConfirm":
					if (!result[1].equals("no")) {
						setSucceeded(true);

					} else
						setSucceeded(false);

					break;

				case "UpdateOrderToDeleted":
					if (!result[1].equals("no")) {

						setSucceeded(true);
					} else
						setSucceeded(false);
					break;

				case "Update":
					setSucceeded(true);
					break;

				case "AddToVisitorsList":
					setExistTraveler(true);
					v1.setFirstName(result[2]);
					v1.setLastName(result[3]);
					v1.setVisitorID(result[4]);
					v1.setMail(result[5]);
					v1.setPhoneNumber(result[6]);
					v1.setType(result[7]);
					setSucceeded(true);
					break;

				case "EnterToWaitingList":

					if (result[1].equals("EnterSucceeded"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "AddToTheOrderList":
					if (!result[1].equals("Error!")) {
						o1.setNoOfOrder(result[1]);
						setSucceeded(true);
					} else
						setSucceeded(false);
					break;

				case "DeleteDB":
					for (int i = 0; i < result.length; i++)
						if (result[1].equals("Succeeded"))
							setSucceeded(true);
					break;

				case "SeccuedPayed":
					break;

				case "SearchOrderNumberWithVisitorID":

					if (result[1].equals("yes")) {
// ValidateOrderNumber = true;
						setValidateOrderNumber(true);

						o1.setNoOfOrder(result[2]);
						o1.setRequestedpark(result[3]);
						o1.setDateofvisit(result[4]);
						o1.setVisittime(result[5]);
						o1.setNumberofvisitors(result[6]);
						o1.setEmail(result[7]);
						o1.setPrice(result[11]);
						o1.setIsPaid(result[12]);
						type = "";
						switch (result[8]) {
						case "StandardOrder":
							type = "Standard order";
							break;
						case "OrderForASmallGroup":
							type = "Order for a small group";
							break;
						case "OrderForFamilySubscription":
							type = "Order for family subscription";
							break;
						case "Instructions":
							type = "Instructions";
							break;
						}
						o1.setType(type);
						o1.setIsDone(result[9]);
						o1.setVisitorID(result[10]);
					} else
						setValidateOrderNumber(false);
					break;

				case "ReturnRelevantTimesToThisDate":
					setSucceeded(true);

					if (result.length == 1)
						arrayTimes.add("null");
					for (int i = 1; i < result.length; i++)
						arrayTimes.add(result[i]);
					break;

				case "indeftifictionEmployee":
					isTraveler = false;
					isEmployee = true;
					if (result[1].equals("yesUserName")) {
						if (result[2].equals("yesPassword")) {
							if (!result[3].equals("theEmployeeConnectAlready")) {
								IsConnected = false;
								ValidateUserName = true;
								ValidatePassword = true;
								E1.setFirstName(result[3]);
								E1.setLastName(result[4]);
								E1.setUsername(result[5]);
								E1.setEmail(result[6]);
								E1.setRole(result[7]);
								E1.setOrganizationalAffiliation(result[8]);
								E1.setPassword(result[9]);
							} else
								IsConnected = true;
						} else {
							ValidateUserName = true;
							ValidatePassword = false;
						}
					} else {
						ValidateUserName = false;
					}

					break;

				case "EnterOfVisitor":

					if (result.length > 1) {

						ExistOrder = result[1];
						o1.setRequestedpark(result[3]);
						o1.setDateofvisit(result[4]);
						o1.setVisittime(result[5]);
						o1.setNumberofvisitors(result[6]);
						o1.setEmail(result[7]);
						o1.setType(result[8]);
						o1.setPrice(result[11]);
						o1.setIsPaid(result[12]);
						o1.setNoOfOrder(result[2]);
						o1.setVisitorID(result[10]);
						o1.setGapPrice(result[13]);

					}

					break;

				case "justindeftifictionTraveler":
					isTraveler = false;
					isEmployee = true;

					switch (result[1].toString()) {
					case "Existing":
						setExistTraveler(true);
						type = result[7];
						if (type.equals("2")) {
							v1.setFirstName(result[2]);
							v1.setLastName(result[3]);
							v1.setVisitorID(result[4]);
							v1.setMail(result[5]);
							v1.setPhoneNumber(result[6]);
							v1.setType(result[7]);
							v1.setNumOfPeople(result[8]);
							v1.setCardNumber(result[9]);
							v1.setCreditCardValidity(result[10]);
							v1.setCVVNumber(result[11]);
						} else {
							v1.setFirstName(result[2]);
							v1.setLastName(result[3]);
							v1.setVisitorID(result[4]);
							v1.setMail(result[5]);
							v1.setPhoneNumber(result[6]);
							v1.setType(result[7]);
						}

						break;

					case "NotExisting":
						v1.setVisitorID(result[2]);
						setExistTraveler(false);
						break;
					}
					break;

				case "CheckIn":
					if (result[1].equals("Succeeded")) {
						setSucceeded(true);
					}
					break;

				case "ExitOfVisitor":
					if (result[1].equals("Succeeded")) {
						setSucceeded(true);
					}

					break;

				case "RegisterationAsFamilySubscription":
					if (result[1].equals("IsExist"))
						NotExistfamiltsubscription = false;
					if (result[1].equals("NotExist"))
						NotExistfamiltsubscription = true;
					break;

				case "RegisterationAsGuide":
					if (result[1].equals("IsExist"))
						NotExistGuide = false;
					if (result[1].equals("NotExist"))
						NotExistGuide = true;
					break;

				case "ReturnNumberOfVisitorsPresentInThePark":
					ChatClient.NumerOfVisitorInThePark = myAtoi(result[1]);
					break;

				case "ReturnTheDetalisReportOfVisitors":
					ChatClient.SingleVisitors = result[1];
					ChatClient.OrgnaizedVisitor = result[2];
					ChatClient.Subscribers = result[3];
					int single = myAtoi(ChatClient.SingleVisitors);
					int org = myAtoi(ChatClient.OrgnaizedVisitor);
					int sub = myAtoi(ChatClient.Subscribers);
					int total = single + org + sub;
					ChatClient.totalVistors = total + " ";
					break;

				case "ReturnNumberOfPeople":
					for (int i = 0; i < 31; i++) {
						IncomeDayString[i] = null;
						IncomeDayInt[i] = 0;
					}
					for (int i = 0; i < 31; i++) {
						IncomeDayString[i] = result[i];
						IncomeDayInt[i] = myAtoi(result[i]);
					}
					for (int i = 0; i < 31; i++)
						TotalIncome = TotalIncome + IncomeDayInt[i];
					break;
				case "ReturnFullDayes":
					for (int i = 0; i < 31; i++) {
						FullDayes[i] = result[i];
					}
					break;

				case "TellMeIfThereIsVisitorsReport":

					if (result[1].equals("VisitorsReport")) {
						VisitorsReport1 = true;
						VisitorsReport = "The park maneger sent you a visitor report for confirmation";
					}
					break;

				case "TellMeIfThereIsIncomeReport":
					if (result[1].equals("IncomeReport")) {
						IncomeReport1 = true;
						IncomeReport = "The park maneger sent you a Income report for confirmation";
					}
					break;

				case "TellMeIfThereIsOccupancyReport":
					if (result[1].equals("OccupancyReport")) {
						OccupancyReport1 = true;
						OccupancyReport = "The park maneger sent you a Occupancy report for confirmation";
					}
					break;

				case "GiveMeTheMaxQuota":
					if (result[1].equals("MaxqoutaParameters")) {
						MaxqoutaParameters1 = true;
						MaxqoutaParameters = result[2];
					}

					break;

				case "GiveMeTheGapParameters":

					if (result[1].equals("GapParameters1")) {
						Gapparameters1 = true;
						Gapparameters = result[2];
					}
					break;

				case "GiveMeTheStayTimeParameters":
					if (result[1].equals("StayTimeParameters")) {
						StayTimeParameters1 = true;
						StayTimeParameters = result[2];
					}
					break;

				case "SendMeInfoToVisitorReportToTheMonth":
					for (int i = 0; i < result.length - 1; i++)
						VisitorByMonth[i] = result[i + 1];

					break;

				case "SendMeInfoToVisitorReportToTheDay":

					for (int i = 0; i < result.length - 1; i++)
						VisitorByDay[i] = result[i + 1];

					break;

				case "SendMeInfoToVisitorReportToTheWeek":

					for (int i = 0; i < result.length - 1; i++)
						VisitorByweek[i] = result[i + 1];
					for (int g = 0; g < VisitorByweek.length; g++)
						break;

				case "SendMeInfoToVisitorReportToTheHour":
					for (int i = 0; i < result.length - 1; i++)
						VisitorByHour[i] = result[i + 1];
					for (int h = 0; h < VisitorByHour.length; h++)

						break;

				case "SendMeInfoToIncomeReportToTheMonth":
					for (int i = 0; i < result.length - 1; i++)
						IncomeByMonth[i] = result[i + 1];
					break;

				case "SendMeInfoToIncomeReportToTheDay":
					for (int i = 0; i < result.length - 1; i++)
						IncomeByDay[i] = result[i + 1];

					break;

				case "SendMeInfoToIncomeReportToTheWeek":

					for (int i = 0; i < result.length - 1; i++)
						IncomeByweek[i] = result[i + 1];
					for (int h = 0; h < IncomeByweek.length; h++)

						break;

				case "SendMeInfoToIncomeReportToTheHour":
					for (int i = 0; i < result.length - 1; i++)
						IncomeByHour[i] = result[i + 1];
					break;

				case "SendMeInfoToCancellationReportToTheMonth":
					for (int i = 0; i < result.length - 1; i++)
						CancellationByMonth[i] = result[i + 1];
					break;

				case "SendMeInfoToCancellationReportToTheDay":
					for (int i = 0; i < result.length - 1; i++)
						CancellationByDay[i] = result[i + 1];
					break;

				case "SendMeInfoToCancellationReportToTheWeek":

					for (int i = 0; i < result.length - 1; i++)
						CancellationByweek[i] = result[i + 1];
					break;

				case "SendMeInfoToCancellationReportToTheHour":
					for (int i = 0; i < result.length - 1; i++)
						CancellationByHour[i] = result[i + 1];
					break;

				case "SendMeIfTheWeekIsFull":
					for (int i = 0; i < result.length - 1; i++) {
						FullByweek[i] = result[i + 1];
					}

					break;
				case "SendMeIfTheHourIsFull":
					for (int i = 0; i < result.length - 1; i++)
						FullByHour[i] = result[i + 1];
					break;

				case "GiveMeTheReminder":
					Reminder = result[1];
					break;

				case "SendMeAllTheDiscount":
					for (int i = 0; i < result.length - 1; i++)
						discountlist.add(result[i + 1]);
					break;

				case "SendDetalsReportVisitors":
					display = result[1];
					setSucceeded(true);
					break;

				case "SaveTheReport":
					setSucceeded(true);

					break;
				case "CheckIfThereIsReportLikeThat":

					IsExist = result[1];
					setSucceeded(true);

					break;
				case "SendMeIfIsFullByDay":
					for (int i = 0; i < result.length - 1; i++) {
						FullByDay[i] = result[i + 1];
					}
					setSucceeded(true);
					break;
				case "SendMeIfIsFullByMonth":
					for (int i = 0; i < result.length - 1; i++) {
						FullBymonth[i] = myAtoi(result[i + 1]);
					}
					setSucceeded(true);
					break;

				case "updateGap":
					if (result[1].equals("Succeeded"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "updateTheMaxQuota":
					if (result[1].equals("Succeeded"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "updateStayTime":
					if (result[1].equals("Succeeded"))
						setSucceeded(true);
					else
						setSucceeded(false);
					break;

				case "SendMeEnteringTimeToMonth":
					arrayInfo = new int[3];
					for (int i = 0; i < result.length - 1; i++) {
						arrayInfo[i] = myAtoi(result[i + 1]);
					}
					setSucceeded(true);
					break;
				case "SendMeEnteringTimeToDay":
					arrayInfo = new int[3];
					for (int i = 0; i < result.length - 1; i++) {
						arrayInfo[i] = myAtoi(result[i + 1]);
					}
					setSucceeded(true);
					break;

				case "SendMevisitotsByStayTimeToDay":
					arrayInfo = new int[3];
					for (int i = 0; i < result.length - 1; i++) {
						arrayInfo[i] = myAtoi(result[i + 1]);
					}
					setSucceeded(true);
					break;

				case "SendMevisitotsByStayTimeToMonth":
					arrayInfo = new int[3];
					for (int i = 0; i < result.length - 1; i++) {
						arrayInfo[i] = myAtoi(result[i + 1]);
					}

					setSucceeded(true);
					break;
				case "UpdateConnected":
					setSucceeded(true);

				}
			}
		}
	}

	

	@FXML
	public static void logoutfunc(MouseEvent event) throws Exception {
		EmployeeIdentificationPage1Controller e = new EmployeeIdentificationPage1Controller();

		String task = null;
		if (isTraveler == true && isEmployee == false)
			task = "UpdateConnected " + ChatClient.v1.getVisitorID();
		else if (isTraveler == false && isEmployee == true)
			task = "UpdateConnected " + ChatClient.E1.getUsername();
		ClientUI.chat.accept(task); // send message from client to server
		if (ChatClient.getSucceeded() == true) {
			Stage primaryStage = new Stage();

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			LandingPageContoller landingPage = new LandingPageContoller();
			landingPage.start(primaryStage);
		}
	}

	/**
	 * translate the month name
	 * @param name name of month
	 * @return number
	 */
	public static int TranslateMonthName(String name) {
		switch (name) {
		case "January":
			return 1;

		case "February":
			return 2;

		case "March":
			return 3;

		case "April":
			return 4;

		case "May":
			return 5;

		case "June":
			return 6;

		case "July":
			return 7;

		case "August":
			return 8;

		case "September":
			return 9;

		case "October":
			return 10;

		case "November":
			return 11;

		case "December":
			return 12;

		}
		return 0;

	}

	/**
	 * handle message from client
	 * @param message msg
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * Convert message to String array
	 */
	private String[] ConvertMsgToStringArray(Object msg) {

		String st;
		String st1;
		String st2;
		st = msg.toString();
		st1 = st.replace("[", "");
		st2 = st1.replace("]", "");
		if (st2 != "empty") {
			String[] result = st2.split(",");
			String[] result2 = new String[result.length];
			for (int i = 0; i < result.length; i++) {
				result2[i] = result[i].replace(" ", "");
			}
			return result2;
		}

		String[] result3 = new String[1];
		result3[0] = st2;
		return result3;
	}

	public static String getTypeofWindow() {
		return typeofWindow;
	}

	public static void setTypeofWindow(String typeofWindow) {
		ChatClient.typeofWindow = typeofWindow;
	}

	public static boolean isValidateOrderNumber() {
		return ValidateOrderNumber;
	}

	public static void setValidateOrderNumber(boolean validateOrderNumber) {
		ValidateOrderNumber = validateOrderNumber;
	}

	public static boolean isExistTraveler() {
		return ExistTraveler;
	}

	public static void setExistTraveler(boolean existTraveler) {
		ExistTraveler = existTraveler;
	}

	public static Boolean getSucceeded() {
		return Succeeded;
	}

	public static void setSucceeded(Boolean succeeded) {
		Succeeded = succeeded;
	}

	public static String getTypeofCreditwindow() {
		return typeofCreditwindow;
	}

	public static void setTypeofCreditwindow(String typeofCreditwindow) {
		ChatClient.typeofCreditwindow = typeofCreditwindow;
	}

	public static String getAvailability() {
		return Availability;
	}

	public static void setAvailability(String availability) {
		Availability = availability;
	}

	public static String getTypeofwindow() {
		return typeofIdentificationwindow;
	}

	public static void setTypeofwindow(String typeofwindow) {
		ChatClient.typeofIdentificationwindow = typeofwindow;
	}

	public static String getTypeofVacancywindow() {
		return typeofVacancywindow;
	}

	public static void setTypeofVacancywindow(String typeofVacancywindow) {
		ChatClient.typeofVacancywindow = typeofVacancywindow;
	}

	/**
	 * Change string to integer
	 * @param str string
	 * @return result
	 */
	public static int myAtoi(String str) {
		int i = 0;
		while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
			++i;
		}
		if (i == str.length()) {
			return 0;
		}
		int result = 0;
		while (i < str.length() && Character.isDigit(str.charAt(i))) {
			try {
				result = Math.multiplyExact(result, 10);
				result = Math.addExact(result, Character.getNumericValue(str.charAt(i)));
			} catch (ArithmeticException e) {
				return Integer.MAX_VALUE;
			}
			++i;
		}
		return result;
	}
}
//End of ChatClient class
