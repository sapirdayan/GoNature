
package Server;

import java.io.*;
import ocsf.server.*;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EchoServer extends AbstractServer {

	mysqlConnection mySql1 = null;
	public static int discountpriotiy = 0;
	public static int subscriptionnum = 0;
	public static int ordernumber = 0;
	public static int PriorityOfPark1 = 0;
	public static int PriorityOfPark2 = 0;
	public static int PriorityOfPark3 = 0;
	public static int VisitorsInPArk1 = 0;
	public static int VisitorsInPArk2 = 0;
	public static int VisitorsInPArk3 = 0;
	public static String type = "";

	public EchoServer(int port) {
		super(port);
	}
/**
 * A method that receives requests from the client and translates them into a string and splits it into an ArrayList
 * msg=msg
 * client=ConnectionToClient
 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		boolean flag = false;
		String[] parks = { "park1", "park2", "park3" };
		int[] parameters = new int[4];
		mysqlConnection mySql = null;
		String st;
		st = msg.toString();
		String[] s = st.split("\\s");
		String query = null;
		String query1 = null;
		ArrayList<String> userArr = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		String scenario = s[0].toString();
		int i = 0;
		String[] times = { "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
				"17:00", "18:00", "19:00" };

		try {
			mySql = mysqlConnection.GetInstance();

		} catch (SQLException e1) {

			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		try {
			ordernumber = MySqlQuery.InitializeOrderNumber(mySql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int[] initialPriority = null, initialVisitorsInpark = null;

		try {
			initialPriority = MySqlQuery.InitializePriorityPark(parks, mySql);
			initialVisitorsInpark = MySqlQuery.InitializeVisitorsInParks(mySql);
			discountpriotiy = MySqlQuery.InitializePriorityDiscount(mySql);
			subscriptionnum = MySqlQuery.InitializePrioritysubscriptionnum(mySql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PriorityOfPark1 = initialPriority[0];
		PriorityOfPark2 = initialPriority[1];
		PriorityOfPark3 = initialPriority[2];
		VisitorsInPArk1 = initialVisitorsInpark[0];
		VisitorsInPArk2 = initialVisitorsInpark[1];
		VisitorsInPArk3 = initialVisitorsInpark[2];

		try {
			switch (scenario) {

			///////////////////////////////////////// NASMAT/////////////////////////////////////////
			case "CheckVacancyDB":

				int Capacity = 0, temp1 = 0, j = 0, TotalCapacity = 0;
				// TotalCapacity = Integer.parseInt(s[4]);
				TotalCapacity = 0;
				parameters = MySqlQuery.CheckingParameters(s[1], mySql);
				String[] time = BringRelevantTimes(s[3], parameters[1]), res;
				int lenth = time.length + 1 - parameters[1];
				int k = lenth;
				int l = 0;
				for (int i1 = 0; i1 < time.length; i1++) {
					TotalCapacity += MySqlQuery.CheckVacancy(s[1], s[2], time[i1], mySql);
				}
				while (--lenth >= 0) {
					res = Arrays.copyOfRange(time, j, parameters[1] + j);
					for (i = 0; i < res.length; i++) {
						Capacity += MySqlQuery.CheckVacancy(s[1], s[2], res[i], mySql);
					}

					Capacity += Integer.parseInt(s[4]);
					if (Capacity <= parameters[0] - parameters[2]) {
						temp1++;
					}
					j++;
					Capacity = 0;
				}

				if (temp1 == k) {
					userArr.add("CheckVacancyDB");
					userArr.add("yes");
					userArr.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

				} else {
					userArr.add("CheckVacancyDB");
					userArr.add("no");
					userArr.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

				}
				flag = true;
				break;

			case "DeterminingDiscount":
				query = "INSERT INTO discounts(Park, IsApproval, TypeOfDiscount, StartDate, EndDate, DiscountValue, DiscountNum) VALUES ('"
						+ s[1] + "', '0', '" + s[5] + "', '" + s[3] + "', '" + s[4] + "', '" + s[2] + "', '"
						+ (++discountpriotiy) + "');";
				userArr = MySqlQuery.AddDiscount(query, "DeterminingDiscount", mySql);
				flag = true;
				break;

			case "DiscountsThatRequireApproval":
				query = "SELECT * FROM discounts WHERE Park='" + s[1] + "' AND IsApproval='" + 0 + "';";
				userArr = MySqlQuery.DiscountsRequireApproval(query, "DiscountsThatRequireApproval", mySql);
				flag = true;
				break;

			case "GiveMeDiscountDetails":
				query = "SELECT * FROM discounts WHERE Park='" + s[1] + "' AND IsApproval='" + 0
						+ "' AND TypeOfDiscount='" + s[2] + "' AND StartDate='" + s[3] + "' AND EndDate='" + s[4]
						+ "' AND DiscountValue='" + s[5] + "';";
				userArr = MySqlQuery.DiscountsDetails(query, "GiveMeDiscountDetails", mySql);
				flag = true;
				break;

			case "RefusedDiscount":
				query = "DELETE FROM discounts WHERE DiscountNum='" + s[2] + "';";
				userArr = MySqlQuery.UpdateOrDeleteDiscount(query, "RefusedDiscount", mySql);
				flag = true;
				break;

			case "ApprovedDiscount":
				query = "UPDATE discounts SET IsApproval='1' WHERE DiscountNum='" + s[2] + "';";
				userArr = MySqlQuery.UpdateOrDeleteDiscount(query, "ApprovedDiscount", mySql);
				flag = true;
				break;

			case "ReturnTheDiscount":
				if (s[2].equals("1"))
					query = "	SELECT * FROM discounts WHERE Park='" + s[3] + "' AND IsApproval ='1' AND '" + s[1]
							+ "' BETWEEN StartDate AND EndDate;";
				else
					query = "	SELECT * FROM discounts WHERE Park='" + s[3]
							+ "' AND IsApproval ='1' AND TypeOfDiscount='1' AND '" + s[1]
							+ "' BETWEEN StartDate AND EndDate;";

				userArr = MySqlQuery.returnTheDiscount(query, "ReturnTheDiscount", mySql);
				flag = true;
				break;

			case "CheckInWaitingList":
				flag = true;
				int currentC = Integer.parseInt(s[6]);
				i = 0;
				String[] range = BringRelevantTimes(s[5], 4);
				int index = range.length - 1;
				String query2 = "SELECT * FROM waitinglist WHERE Park='" + s[1] + "' AND DateOfVisit='" + s[4]
						+ "' AND VisitingTime BETWEEN '" + range[0] + "' AND '" + range[index] + "';";

				int[] priority = MySqlQuery.returnThePriority(query2, mySql);
				if (priority[0] == 0) {
					userArr.add("CheckInWaitingList");
					userArr.add("ThereIsNoOrders");
				} else {

					while (currentC > 0) {
						if (i < priority.length) {

							query = "SELECT * FROM waitinglist WHERE Park='" + s[1] + "' AND DateOfVisit='" + s[4]
									+ "' AND priority = '" + priority[i++] + "' ;";

							currentC = MySqlQuery.SearchInWaitingList(query, currentC, mySql, "Waiting1");

						} else
							currentC = 0;
					}
					userArr.add("CheckInWaitingList");
					userArr.add("ThereIsOrders");
				}
				break;

			case "SendMeOrdersInWaitingListToThisId":
				////////////////////////////// here we should check in the 3 tables.

				userArr.add("SendMeOrdersInWaitingListToThisId");
				for (i = 0; i < 3; i++) {

					query1 = "SELECT * FROM " + parks[i] + " WHERE Idnumber ='" + s[1] + "';";
					query2 = "SELECT * FROM waitinglist WHERE Idnumber ='" + s[1] + "';";

					userArr.addAll(MySqlQuery.OrdersInWaitingList(query1, query2, parks[i],
							"SendMeOrdersInWaitingListToThisId", mySql));
				}
				flag = true;
				break;

			case "SentMeAllTheInfoToThisOrder":
				// userArr.add("SentMeAllTheInfoToThisOrder");
				query1 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";
				userArr = MySqlQuery.OrdersInWaitingList(query1, null, s[1], "SentMeAllTheInfoToThisOrder", mySql);
				flag = true;
				break;

			case "UpdateOrderToConfirm":
				query1 = "UPDATE " + s[1] + " SET Status = 'Active' WHERE ordernumber ='" + s[4] + "';";
				query2 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";

				userArr = MySqlQuery.OrdersInWaitingList(query1, query2, s[1], "UpdateOrderToConfirm", mySql);

				flag = true;
				break;

			case "UpdateOrderToDeleted":
				// delete it from waiting list
				query1 = "UPDATE " + s[1] + " SET Status = 'Deleted1' WHERE ordernumber ='" + s[4] + "';";

				query2 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";
				userArr = MySqlQuery.OrdersInWaitingList(query1, query2, s[1], "UpdateOrderToDeleted", mySql);

				flag = true;
				break;

			case "SendMeOrdersToConfirm":
				userArr.add("SendMeOrdersToConfirm");
				for (i = 0; i < 3; i++) {
					query1 = "SELECT * FROM " + parks[i] + " WHERE Idnumber ='" + s[1] + "';";
					userArr.addAll(
							MySqlQuery.OrdersInToConfirm(query1, null, parks[i], "SendMeOrdersToConfirm", mySql, null));
				}
				flag = true;
				break;

			case "SentMeTheOrderDetails":
				userArr.add("SentMeTheOrderDetails");
				query1 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";
				userArr.addAll(MySqlQuery.OrdersInToConfirm(query1, null, s[1], "SentMeTheOrderDetails", mySql, null));
				flag = true;
				break;

			case "UpdateOrderToDeleted1":
				userArr.add("UpdateOrderToDeleted1");
				query1 = "UPDATE " + s[1] + " SET Status = 'Deleted2' WHERE ordernumber ='" + s[4] + "';";
				query2 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";

				userArr.addAll(
						MySqlQuery.OrdersInToConfirm(query1, query2, s[1], "UpdateOrderToDeleted1", mySql, s[5]));
				flag = true;
				break;

			case "UpdateOrderToConfirm1":
				userArr.add("UpdateOrderToConfirm1");
				query1 = "UPDATE " + s[1] + " SET Status = 'Approved' WHERE ordernumber ='" + s[4] + "';";
				query2 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[4] + "';";

				userArr.addAll(
						MySqlQuery.OrdersInToConfirm(query1, query2, s[1], "UpdateOrderToConfirm1", mySql, s[5]));
				flag = true;
				break;

			case "EnterToWaitingList":
				int priority1 = whichPriority(s[2]);

				query = "INSERT INTO waitinglist (Idnumber,Park, DateOfVisit,  VisitingTime, NumberOfVisitors, Email,OrderType, priority,TotalPrice,status) VALUES ('"
						+ s[1] + "','" + s[2] + "','" + s[3] + "','" + s[4] + "','" + s[5] + "','" + s[7] + "','" + s[8]
						+ "','" + priority1 + "','" + s[10] + "','" + "Active" + "');";
				userArr.add("EnterToWaitingList");
				if (MySqlQuery.EnterTOWaitingList(query, mySql)) {
					userArr.add("EnterSucceeded");
				} else {
					userArr.add("EnterNotSucceeded");
				}

				flag = true;
				break;

			case "RegisterationAsFamilySubscription":
				query = "INSERT INTO visitor (Firstname, Lastname, Idnumber, Telephone,Email, type, FamilyMembersAmount, CreditCardNumber,CardValidity,3Digits,SubscriptionNum) VALUES ('"
						+ s[1] + "','" + s[2] + "','" + s[3] + "','" + s[4] + "','" + s[5] + "','" + 2 + "','" + s[6]
						+ "','" + s[7] + "','" + s[8] + "','" + s[9] + "','" + (++subscriptionnum) + "');";
				query1 = "SELECT * FROM visitor WHERE Idnumber='" + s[3] + "';";
				String query3 = "UPDATE visitor SET Type = '2', FamilyMembersAmount = '" + s[6]
						+ "', CreditCardNumber = '" + s[7] + "', CardValidity = '" + s[8] + "', CardValidity = '" + s[9]
						+ "' WHERE Idnumber = '" + s[3] + "';";
				userArr = TravelerRegistration(query, query1, query3, "RegisterationAsFamilySubscription", mySql, s, 2);
				flag = true;
				break;

			case "RegisterationAsGuide":
				query = "INSERT INTO visitor (Firstname, Lastname, Idnumber, Telephone,Email, type) VALUES ('" + s[1]
						+ "','" + s[2] + "','" + s[3] + "','" + s[4] + "','" + s[5] + "','" + 1 + "');";
				query1 = "SELECT * FROM visitor WHERE Idnumber='" + s[3] + "';";
				query3 = "UPDATE visitor SET Type = '1' WHERE Idnumber = '" + s[3] + "';";
				userArr = TravelerRegistration(query, query1, query3, "RegisterationAsGuide", mySql, s, 1);
				flag = true;
				break;

			case "updateTheMaxQuota":
				flag = true;
				query = "UPDATE parkdetails SET WFAMaxCover = '" + Integer.parseInt(s[2]) + "'WHERE park = '" + s[1]
						+ "';";
				i = MySqlQuery.UpDateParkDatailsParameter(query, mySql);
				userArr.add("updateTheMaxQuota");
				if (i == 1) {
					userArr.add("Succeeded");
				} else {
					userArr.add("NotSucceeded");
				}
				break;
			case "updateGap":
				flag = true;
				query = "UPDATE parkdetails SET WFAparaGap = '" + Integer.parseInt(s[2]) + "'WHERE park = '" + s[1]
						+ "';";
				i = MySqlQuery.UpDateParkDatailsParameter(query, mySql);
				userArr.add("updateGap");
				if (i == 1) {
					userArr.add("Succeeded");
				} else {
					userArr.add("NotSucceeded");
				}
				break;

			case "updateStayTime":
				flag = true;
				query = "UPDATE parkdetails SET WFAStayTime = '" + Integer.parseInt(s[2]) + "'WHERE park = '" + s[1]
						+ "';";
				i = MySqlQuery.UpDateParkDatailsParameter(query, mySql);
				userArr.add("updateStayTime");
				if (i == 1) {
					userArr.add("Succeeded");
				} else {
					userArr.add("NotSucceeded");
				}
				break;

			case "DeleteDB":
				query = "UPDATE " + s[1] + " SET Status = 'Deleted2' WHERE ordernumber = '" + s[3] + "';";
				
				userArr = MySqlQuery.DeleteFromDB(s[1], query,  "DeleteDB", mySql);
				flag = true;
				break;

			case "UpdateOrder":
				query = "UPDATE " + s[1] + "  SET IsPaid = '" + s[4] + "', TotalPrice = '" + s[3]
						+ "' WHERE  ordernumber ='" + s[2] + "';";
				query1 = "SELECT * FROM " + s[1] + " WHERE  ordernumber ='" + s[2] + "';";
				userArr = MySqlQuery.UpdateOrder(query, query1, s[1], "UpdateOrder", mySql);
				flag = true;
				break;

			case "indeftifictionTraveler":
				flag = true;
				query = "SELECT * FROM visitor WHERE Idnumber='" + s[1] + "';";
				query1 = "SELECT * FROM visitor WHERE SubscriptionNum='" + s[1] + "';";

				userArr = MySqlQuery.indeftifictionInDB(query, query1, "indeftifictionTraveler", mySql);
				if (userArr.get(1).equals("NotExisting")) {
					userArr.add(s[1]);
				}
				break;

			case "AddToVisitorsList":

				flag = true;
				query = "INSERT INTO visitor (Firstname, Lastname, Idnumber, Email, Telephone, Type ,IsConnected) VALUES ('"
						+ s[1] + "','" + s[2] + "','" + s[3] + "','" + s[4] + "','" + s[5] + "','" + "0' ,'1" + "');";
				query1 = "SELECT * FROM visitor WHERE Idnumber='" + s[3] + "';";
				if (!MySqlQuery.ExistingUserOnDB(query1, mySql)) {
					userArr = MySqlQuery.CreatNewVisitor(query, query1, "AddToVisitorsList", mySql);
				} else {
					userArr.add("AddToVisitorsList");
					userArr.add("IsExistVisitor");
				}

				break;

			case "AddToTheOrderList":
				flag = true;

				query2 = null;
				query = "INSERT INTO " + s[1]
						+ " (Dateofvisit, Vistingtime, Numberofvistors, Idnumber, Email,Typeoforder, Status, ordernumber, TotalPrice, IsPaid) VALUES ('"
						+ s[2] + "','" + s[3] + "','" + s[4] + "','" + s[5] + "','" + s[6] + "','" + s[7] + "','" + s[8]
						+ "','" + (++ordernumber) + "','" + s[9] + "','" + s[10] + "');";
				query2 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + ordernumber + "';";

				userArr = MySqlQuery.CreatNewOrder(query, query2, "AddToTheOrderList", mySql);
				break;

			case "SearchOrderNumberWithVisitorID":
				boolean haveOrder1 = false;
				for (i = 0; i < 3; i++) {
					userArr = MySqlQuery.checkTheOrdernumber(parks[i], s[1], s[2], "SearchOrderNumberWithVisitorID",
							mySql, "1");
					if (userArr.get(1).equals("yes")) {
						i = 3;
						haveOrder1 = true;

					}

				}

				if (!haveOrder1) {
					userArr.clear();
					userArr.add("SearchOrderNumberWithVisitorID");
					userArr.add("no");
				}
				flag = true;
				break;

			case "Update":
				query = "UPDATE " + s[1] + "  SET Dateofvisit = '" + s[5] + "', Vistingtime = '" + s[6]
						+ "', Numberofvistors = '" + s[7] + "', Typeoforder = '" + s[8] + "', TotalPrice = '" + s[9]
						+ "', IsPaid = '" + s[10] + "', PriceGap='" + s[11] + "' WHERE ordernumber = '" + s[3] + "';";

				query1 = "SELECT * FROM " + s[1] + " WHERE  ordernumber ='" + s[3] + "';";
				userArr = MySqlQuery.UpdateOrder(query, query1, s[1], "Update", mySql);
				flag = true;
				break;

			case "ReturnRelevantTimesToThisDate":
				flag = true;
				String[] res1 = null;
				int count = 0, count2 = 0, intreval;

				parameters = MySqlQuery.CheckingParameters(s[1], mySql);
				int lenth1;
				userArr.add("ReturnRelevantTimesToThisDate");
				String[] Btime = null;
				for (i = 0; i < times.length; i++) {
					Btime = BringRelevantTimes(times[i], parameters[1]);
					lenth1 = Btime.length + 1 - parameters[1];
					intreval = lenth1;
					j = 0;
					while (--lenth1 >= 0) {
						res1 = Arrays.copyOfRange(Btime, j, parameters[1] + j);
						j++;
						for (int t = 0; t < res1.length; t++) {

							count += MySqlQuery.CheckVacancy(s[1], s[2], res1[t], mySql);

						}
						count += Integer.parseInt(s[3]);

						if (count <= parameters[0] - parameters[2]) {
							count2++;
						}
						count = 0;
					}

					if (count2 == intreval) {
						userArr.add(times[i]);
					}
					count2 = 0;

				}
				break;

			case "indeftifictionEmployee":
				List<String> arr = new ArrayList<String>();
				query1 = "SELECT * FROM employees WHERE UserName='" + s[1] + "';";
				if (MySqlQuery.ExistingUserOnDB(query1, mySql)) {
					query1 = "SELECT * FROM employees WHERE UserName='" + s[1] + "' AND password='" + s[2] + "';";
					userArr.clear();
					userArr.add("indeftifictionEmployee");
					userArr.add("yesUserName");
					if (MySqlQuery.ExistingUserOnDB(query1, mySql)) {
						userArr.add("yesPassword");
						arr = MySqlQuery.indeftifictionAsEmployee(query1, mySql);
						userArr.addAll(arr);
					} else
						userArr.add("noPassword");
				} else {
					userArr.clear();
					userArr.add("indeftifictionEmployee");
					userArr.add("noUserName");
				}

				flag = true;
				break;

			case "EnterOfVisitor":

				LocalTime timeNow1 = LocalTime.now().plusMinutes(30);
				LocalTime timeNow2 = LocalTime.now().minusMinutes(30);
				String[] t = timeNow1.toString().split(":");
				String Time1 = t[0] + ":" + t[1];
				t = timeNow2.toString().split(":");
				String Time2 = t[0] + ":" + t[1];

				query1 = "SELECT * FROM " + s[1] + " WHERE ordernumber='" + s[2]
						+ "' AND Status='Approved' AND Dateofvisit='" + LocalDate.now() + "' AND Vistingtime BETWEEN '"
						+ Time2 + "' AND '" + Time1 + "';";
				userArr = MySqlQuery.EnterToTheParkFunc(query1, s[1], "EnterOfVisitor", mySql);
				flag = true;
				break;

			case "CheckIn":
				flag = true;
				LocalTime clock = LocalTime.now();
				String[] stringarr = clock.toString().split(":");
				String ClockNow = stringarr[0] + ":" + stringarr[1];
				query = "UPDATE " + s[2] + "  SET Status = '" + "Enter" + "', IsPaid = '" + "true" + "',EnteringTime= '"
						+ ClockNow + "' WHERE ordernumber = '" + s[1] + "';";
				query1 = "SELECT Numberofvistors FROM " + s[2] + " WHERE ordernumber='" + s[1] + "';";
				userArr.add("CheckIn");
				if (MySqlQuery.UpDateEnterExit(query, mySql)) {
					userArr.add("Succeeded");
				} else {
					userArr.add("NotSucceeded");
				}
				String paramater = MySqlQuery.ReturnNumberofvistors(query1, mySql);
				int numberVisitors = AddvisitorsNumber(s[2], Integer.parseInt(paramater));

				query2 = "UPDATE parkdetails SET VisitorsOnPark = '" + numberVisitors + "'WHERE park = '" + s[2] + "';";
				i = MySqlQuery.UpDateParkDatailsParameter(query2, mySql);
				break;

			case "ExitOfVisitor":
				flag = true;
				clock = LocalTime.now();
				stringarr = clock.toString().split(":");
				ClockNow = stringarr[0] + ":" + stringarr[1];
				String ID = null;

				query1 = "SELECT * FROM visitor WHERE Idnumber='" + s[2] + "';";
				query2 = "SELECT * FROM visitor WHERE SubscriptionNum='" + s[2] + "';";
				ID = MySqlQuery.returnTheID(query1, query2, mySql);
				if (ID.equals("NotExisting")) {
					ID = s[2];
				}

				query = "SELECT Numberofvistors FROM " + s[1] + " WHERE ordernumber='" + s[3] + "';";
				query3 = "UPDATE " + s[1] + "  SET Status = '" + "Finish' ,ExitingTime= '" + ClockNow
						+ "' WHERE Idnumber='" + ID + "' AND ordernumber = '" + s[3] + "';";

				if (MySqlQuery.UpDateEnterExit(query3, mySql)) {
					paramater = MySqlQuery.ReturnNumberofvistors(query, mySql);

					numberVisitors = AddvisitorsNumber(s[1], -Integer.parseInt(paramater));

					query2 = "UPDATE parkdetails SET VisitorsOnPark = '" + numberVisitors + "'WHERE park = '" + s[1]
							+ "';";
					i = MySqlQuery.UpDateParkDatailsParameter(query2, mySql);
					userArr.add("ExitOfVisitor");
					if (i == 1) {
						userArr.add("Succeeded");
					} else {
						userArr.add("NotSucceeded");
					}
				} else {
					userArr.add("ExitOfVisitor");
					userArr.add("NotSucceeded");
				}

				break;

			case "ReturnNumberOfVisitorsPresentInThePark":
				flag = true;
				userArr.add("ReturnNumberOfVisitorsPresentInThePark");
				if (s[1].equals("park1")) {
					userArr.add(String.valueOf(VisitorsInPArk1));

				}
				if (s[1].equals("park2")) {
					userArr.add(String.valueOf(VisitorsInPArk2));

				}
				if (s[1].equals("park3")) {
					userArr.add(String.valueOf(VisitorsInPArk3));

				} else {
					userArr.add("NotFound");
				}
				break;

///////////////////////////////////////////////// hanaaaaaaaaaaaaaaa///////////////////////////////////////

			case "SendMeIfTheWeekIsFull":
				flag = true;
				String maxCover10 = null;
				String[] days = daily(s[2]);
				index = 0;
				userArr.add(index++, "SendMeIfTheWeekIsFull");
				String query22 = "SELECT MaxCover FROM " + "parkdetails" + " WHERE park ='" + s[1] + "';";
				// SELECT * FROM visitors.parkdetails WHERE park='park1';

				days = week(s[2]);
				int[] Order = MySqlQuery.WeeklyOccupancyReport(s[1], query22, days, mySql);
				String[] str = days[1].split("-");
				userArr.add(str[2]);

				for (int sta : Order) {
					userArr.add(String.valueOf(sta));
				}
				break;

			case "SendMeIfTheHourIsFull":
				query22 = "SELECT MaxCover FROM " + "parkdetails" + " WHERE park ='" + s[1] + "';";
				// SELECT * FROM visitors.parkdetails WHERE park='park1';

				days = month(s[2]);
				int[] Order1 = new int[13];
				for (i = 0; i < 13; i++) {
					Order1[i] = MySqlQuery.HourlyOccupancyReport(s[1], query22, days, times[i], mySql);

					userArr.add("SendMeIfTheHourIsFull");
					for (int sta : Order1) {
						userArr.add(String.valueOf(sta));
					}
				}
				flag = true;
				break;

			case "GiveMeTheReminder":
				flag = true;
				query = "SELECT reminder FROM parkdetails WHERE park = '" + s[1] + "';";
				query1 = "UPDATE parkdetails SET reminder = '" + "The#is#no#messages" + "'WHERE park = '" + s[1] + "';";
				userArr.add("GiveMeTheReminder ");
				userArr.add(MySqlQuery.ReminderFromManager(query, query1, mySql));

				break;

			case "UpdateTheReminder":
				flag = true;

				query = "UPDATE parkdetails SET reminder = '" + s[2] + "'WHERE park = '" + s[1] + "';";
				k = MySqlQuery.UpDateParkDatailsParameter(query, mySql);
				userArr.add("UpdateTheReminder");
				userArr.add(k + "");
				break;

			case "GiveMeTheMaxQuota":
				flag = true;

				query = "Select * FROM parkdetails WHERE park = '" + s[1] + "';";
				userArr = MySqlQuery.GiveTheMaxQuota(query, s[0], mySql);

				break;

			case "GiveMeTheStayTimeParameters":
				flag = true;
				query = "Select * FROM parkdetails WHERE park = '" + s[1] + "';";
				userArr = MySqlQuery.GiveMeTheStayTimeParameters(query, s[0], mySql);

				break;

			case "GiveMeTheGapParameters":
				flag = true;

				query = "Select * FROM parkdetails WHERE park = '" + s[1] + "';";
				userArr = MySqlQuery.GiveMeTheGapParameters(query, s[0], mySql);

				break;
			case "UpdateTheGapInDB":
				flag = true;
				query = "SELECT WFAparaGap FROM parkdetails WHERE park = '" + s[1] + "';";

				int paraGap = MySqlQuery.returnWFAParameter(query, mySql);
				if (paraGap != 0) {
					query1 = "UPDATE parkdetails SET paraGap = '" + paraGap + "',WFAparaGap = '" + -1
							+ "'WHERE park = '" + s[1] + "';";
					k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);
				}
				userArr.add("UpdateTheGapInDB");
				userArr.add("ConfirmParmeter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(paraGap));
				break;

			case "UpdateTheMaxQuotaInDB":
				flag = true;
				query = "SELECT WFAMaxCover FROM parkdetails WHERE park = '" + s[1] + "';";

				int MaxCover = MySqlQuery.returnWFAParameter(query, mySql);

				if (MaxCover != 0) {
					query1 = "UPDATE parkdetails SET MaxCover = '" + MaxCover + "',WFAMaxCover = '" + -1
							+ "'WHERE park = '" + s[1] + "';";
					k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);

				}
				userArr.add("UpdateTheMaxQuotaInDB");
				userArr.add("ConfirmParmeter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(MaxCover));

				break;
			case "UpdateTheStayTimeInDB":
				flag = true;
				query = "SELECT WFAStayTime FROM parkdetails WHERE park = '" + s[1] + "';";
				int stayTime = MySqlQuery.returnWFAParameter(query, mySql);
				if (stayTime != 0) {
					query1 = "UPDATE parkdetails SET StayTime = '" + stayTime + "',WFAStayTime = '" + -1
							+ "'WHERE park = '" + s[1] + "';";
					k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);
				}
				userArr.add("UpdateTheStayTimeInDB");
				userArr.add("ConfirmParmeter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(stayTime));
				break;
			case "deleteTheGapfromDB":
				flag = true;
				query = "SELECT WFAparaGap FROM parkdetails WHERE park = '" + s[1] + "';";
				paraGap = MySqlQuery.returnWFAParameter(query, mySql);

				query1 = "UPDATE parkdetails SET WFAparaGap = '" + -1 + "' WHERE park = '" + s[1] + "';";
				k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);
				userArr.add("deleteTheGapfromDB");
				userArr.add("RefuseParameter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(paraGap));
				break;

			case "deleteTheMaxQuotafromDB":
				flag = true;
				query = "SELECT WFAMaxCover FROM parkdetails WHERE park = '" + s[1] + "';";
				MaxCover = MySqlQuery.returnWFAParameter(query, mySql);
				query1 = "UPDATE parkdetails SET WFAMaxCover = '" + -1 + "' WHERE park = '" + s[1] + "';";
				k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);
				userArr.add("deleteTheMaxQuotafromDB");
				userArr.add("RefuseParameter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(MaxCover));
				break;

			case "deleteTheStayTimefromDB":
				flag = true;
				query = "SELECT WFAStayTime FROM parkdetails WHERE park = '" + s[1] + "';";
				stayTime = MySqlQuery.returnWFAParameter(query, mySql);
				query1 = "UPDATE parkdetails SET  WFAStayTime = '" + -1 + "' WHERE park = '" + s[1] + "';";
				k = MySqlQuery.UpDateParkDatailsParameter(query1, mySql);
				userArr.add("deleteTheStayTimefromDB");
				userArr.add("RefuseParameter");
				userArr.add(s[1]);
				userArr.add(String.valueOf(stayTime));
				break;
			/* case "ReturnTheDetalisReportOfVisitors": */

			case "SendMeInfoToVisitorReportToTheMonth":
				flag = true;
				days = month(s[2]);

				query = "SELECT * FROM " + s[1] + " WHERE Status='Finish' AND Dateofvisit BETWEEN '" + days[0]
						+ "' AND '" + days[1] + "';";
				int[] report = MySqlQuery.monthlyReport(query, mySql);
				userArr.add("SendMeInfoToVisitorReportToTheMonth");
				for (i = 0; i < report.length; i++) {
					userArr.add(String.valueOf(report[i]));
				}
				break;

			case "SendMeInfoToVisitorReportToTheDay":

				flag = true;

				days = daily(s[2]);
				index = 0;
				userArr.add(index++, "SendMeInfoToVisitorReportToTheDay");
				for (String day : days) {

					userArr.add(index++, MySqlQuery.dailyReport(s[1], day, "StandardOrder", mySql));

				}

				for (String day : days) {

					userArr.add(index++, MySqlQuery.dailyReport(s[1], day, "SmallGroupOrInstructions", mySql));

				}

				for (String day : days) {

					userArr.add(index++, MySqlQuery.dailyReport(s[1], day, "FamilySubscription", mySql));

				}
				break;

			case "SendMeInfoToVisitorReportToTheWeek":
				flag = true;
				int[] Standardorder = new int[5];
				int[] SmallGroupORInstructions = new int[5];
				int[] OrderForFamilySubscription = new int[5];
				days = week(s[2]);
				Standardorder = MySqlQuery.WeeklyReport(s[1], days, "StandardOrder", mySql);
				SmallGroupORInstructions = MySqlQuery.WeeklyReport(s[1], days, "SmallGroupOrInstructions", mySql);
				OrderForFamilySubscription = MySqlQuery.WeeklyReport(s[1], days, "OrderForFamilySubscription", mySql);
				userArr.add("SendMeInfoToVisitorReportToTheWeek");
				for (int sta : Standardorder) {
					userArr.add(String.valueOf(sta));
				}
				for (int sma : SmallGroupORInstructions) {
					userArr.add(String.valueOf(sma));
				}
				for (int ord : OrderForFamilySubscription) {
					userArr.add(String.valueOf(ord));
				}

				break;
			case "SendMeInfoToVisitorReportToTheHour":
				int[] Standardorder1 = new int[13];
				int[] SmallGroupORInstructions1 = new int[13];
				int[] OrderForFamilySubscription1 = new int[13];
				flag = true;
				days = month(s[2]);
				for (i = 0; i < 13; i++) {
					Standardorder1[i] = MySqlQuery.HourlyReport(s[1], days, times[i], "StandardOrder", mySql);
					SmallGroupORInstructions1[i] = MySqlQuery.HourlyReport(s[1], days, times[i],
							"SmallGroupOrInstructions", mySql);
					OrderForFamilySubscription1[i] = MySqlQuery.HourlyReport(s[1], days, times[i], "FamilySubscription",
							mySql);
				}
				userArr.add("SendMeInfoToVisitorReportToTheHour");
				for (int sta : Standardorder1) {

					userArr.add(String.valueOf(sta));
				}
				for (int sma : SmallGroupORInstructions1) {

					userArr.add(String.valueOf(sma));
				}
				for (int ord : OrderForFamilySubscription1) {

					userArr.add(String.valueOf(ord));
				}
				break;

			case "SendMeInfoToIncomeReportToTheMonth":
				flag = true;
				days = month(s[2]);

				query = "SELECT * FROM " + s[1] + " WHERE Dateofvisit BETWEEN '" + days[0] + "' AND '" + days[1] + "';";
				report = MySqlQuery.monthlyIncomeReport(query, mySql);
				userArr.add("SendMeInfoToIncomeReportToTheMonth");
				for (i = 0; i < report.length; i++) {
					userArr.add(String.valueOf(report[i]));
				}
				break;

			case "SendMeInfoToIncomeReportToTheDay":
				flag = true;
				days = daily(s[2]);
				index = 0;
				userArr.add(index++, "SendMeInfoToIncomeReportToTheDay");
				for (String day : days) {
					userArr.add(index++, MySqlQuery.dailyIncomeReport(s[1], day, mySql));

				}

				break;

			case "SendMeInfoToIncomeReportToTheWeek":

				Order = new int[5];
				SmallGroupORInstructions = new int[5];
				OrderForFamilySubscription = new int[5];
				String[] days1 = daily(s[2]);
				days = week(s[2]);
				Order = MySqlQuery.WeeklyIncomeReport(s[1], days, mySql);
				userArr.add("SendMeInfoToIncomeReportToTheWeek");
				for (int sta : Order) {
					userArr.add(String.valueOf(sta));
				}
				flag = true;
				break;

			case "SendMeInfoToIncomeReportToTheHour":
				int[] IncomOrders = new int[13];
				flag = true;
				days = month(s[2]);
				for (i = 0; i < 13; i++) {
					IncomOrders[i] = MySqlQuery.HourlyIncomeReport(s[1], days, times[i], mySql);

				}
				userArr.add("SendMeInfoToIncomeReportToTheHour");
				for (int sta : IncomOrders) {

					userArr.add(String.valueOf(sta));
				}
				break;

			case "SendMeInfoToCancellationReportToTheMonth":
				flag = true;
				days = month(s[2]);
				int reportCanel;
				int reportNotCanel;
				
				query = "SELECT * FROM " + s[1] + " WHERE Dateofvisit BETWEEN '" + days[0] + "' AND '" + days[1] + "';";
				reportCanel = MySqlQuery.monthlyCancaltionReport(query, "Cancel", mySql);
				reportNotCanel = MySqlQuery.monthlyCancaltionReport(query, "NotCancel", mySql);
				userArr.add("SendMeInfoToCancellationReportToTheMonth");

				userArr.add(reportCanel + "");
				userArr.add(reportNotCanel + "");
				break;

			case "SendMeInfoToCancellationReportToTheDay":
				flag = true;
				days = daily(s[2]);
				index = 0;
				userArr.add(index++, "SendMeInfoToCancellationReportToTheDay");
				for (String day : days) {
					userArr.add(index++, MySqlQuery.dailyCancaltionReport(s[1], "Cancel", day, mySql));
					if (userArr.size() == 30)
						userArr.add("0");
					if (userArr.size() == 28) {
						userArr.add("0");
						userArr.add("0");
						userArr.add("0");
					}
				}
				for (String day : days) {
					userArr.add(index++, MySqlQuery.dailyCancaltionReport(s[1], "NotCancel", day, mySql));
					if (userArr.size() == 30)
						userArr.add("0");
					if (userArr.size() == 28) {
						userArr.add("0");
						userArr.add("0");
						userArr.add("0");
					}
				}
				break;

			case "SendMeInfoToCancellationReportToTheWeek":
				flag = true;
				int[] OrderCanceled = new int[5];
				int[] OrderNotCanceled = new int[5];
				SmallGroupORInstructions = new int[5];
				OrderForFamilySubscription = new int[5];
				days = week(s[2]);
				OrderCanceled = MySqlQuery.WeeklyCancaltionReport(s[1], "Cancel", days, mySql);
				OrderNotCanceled = MySqlQuery.WeeklyCancaltionReport(s[1], "NotCancel", days, mySql);
				userArr.add("SendMeInfoToCancellationReportToTheWeek");

				for (int sta : OrderCanceled) {
					userArr.add(String.valueOf(sta));
				}
				for (int sta : OrderNotCanceled) {
					userArr.add(String.valueOf(sta));
				}
				break;

			case "SendMeInfoToCancellationReportToTheHour":
				int[] CancelOrders = new int[13];
				int[] CancelNotOrders = new int[13];
				SmallGroupORInstructions1 = new int[13];
				OrderForFamilySubscription1 = new int[13];
				flag = true;
				days = month(s[2]);
				for (i = 0; i < 13; i++) {
					CancelOrders[i] = MySqlQuery.HourlyCancaltionReport(s[1], "Cancel", days, times[i], mySql);
				}
				for (i = 0; i < 13; i++) {
					CancelNotOrders[i] = MySqlQuery.HourlyCancaltionReport(s[1], "NotCancel", days, times[i], mySql);
				}
				userArr.add("SendMeInfoToCancellationReportToTheHour");
				for (int sta : CancelOrders) {
					userArr.add(String.valueOf(sta));
				}
				for (int sta : CancelNotOrders) {
					userArr.add(String.valueOf(sta));
				}
				break;

			case "SendMeIfIsFullByDay":
				parameters = MySqlQuery.CheckingParameters(s[1], mySql);
				userArr.add("SendMeIfIsFullByDay");
				int count11 = 0, count211 = 0;
				String[] Btime11 = null;
				for (i = 0; i < times.length; i++) {
					Btime11 = BringRelevantTimes(times[i], parameters[1]);
					lenth1 = Btime11.length + 1 - parameters[1];
					intreval = lenth1;
					j = 0;
					while (--lenth1 >= 0) {
						res1 = Arrays.copyOfRange(Btime11, j, parameters[1] + j);
						j++;
						for (int t1 = 0; t1 < res1.length; t1++) {
							type = "1";
							count11 += MySqlQuery.CheckVacancy1(s[1], s[2], res1[t1], mySql);

						}
						if (count11 < parameters[0] - parameters[2]) {
							count211++;
						}
						count11 = 0;
					}
					if (count211 != intreval) {
						userArr.add("1");
					} else {
						userArr.add("0");
					}
					count211 = 0;
				}
				flag = true;

				break;

			case "SendMeIfIsFullByMonth":

				flag = true;
				days = daily(s[2]);
				userArr.add("SendMeIfIsFullByMonth");
				for (i = 0; i < days.length; i++) {
					userArr.add(MySqlQuery.OccupancyReport(s[1], days[i], times, mySql));
				}
				break;

			case "CheckIfThereIsReportLikeThat":
							query1 = "SELECT* FROM reportsdetails WHERE yearReport='" + s[1] + "' AND monthReport='" + s[2]
						+ "' AND nameReport='" + s[3] + "'";
				int integer = MySqlQuery.CheckTypereport(query1, mySql);
				flag = true;
				userArr.add("CheckIfThereIsReportLikeThat");
				if (integer == 0)
					userArr.add("No");
				else
					userArr.add("Yes");
				break;

			case "SaveTheReport":
				query1 = "INSERT INTO reportsdetails (yearReport, monthReport, nameReport, typereport) VALUES ('" + s[1]
						+ "', '" + s[2] + "', '" + s[3] + "', '" + s[4] + "');";
				k = MySqlQuery.UpDatereportDatailsParameter(query1, mySql);

				
				flag = true;

				userArr.add("SaveTheReport");
				userArr.add(k + "");
				break;

			case "SendDetalsReportVisitors":
				query1 = "SELECT typereport FROM reportsdetails WHERE yearReport='" + s[1] + "' AND monthReport='"
						+ s[2] + "' AND nameReport='" + s[3] + "'";
				
				String string = MySqlQuery.returnTypereport(query1, mySql);
				flag = true;
				
				userArr.add("SendDetalsReportVisitors");
				
				if (string == null)
					userArr.add("No");
				else
					userArr.add(string);
				break;

			case "SendMeEnteringTimeToDay":
				
				flag = true;
				int[] entering = new int[3];
				entering = MySqlQuery.EnteringReport(s[1], s[2], s[3], s[4], mySql);
				userArr.add("SendMeEnteringTimeToDay");
				for (int e : entering) {
					userArr.add(String.valueOf(e));
				}
				
				break;

			case "SendMeEnteringTimeToMonth":
			
				flag = true;
				days =

						daily(s[2]);
				userArr.add("SendMeEnteringTimeToMonth");
				int[] sr = new int[3];
				sr[0] = 0;
				sr[1] = 0;
				sr[2] = 0;
				for (String day : days) {
					entering = MySqlQuery.EnteringReport(s[1], day, s[3], s[4], mySql);
					sr[0] += entering[0];
					sr[1] += entering[1];
					sr[2] += entering[2];

				}
				userArr.add(String.valueOf(sr[0]));
				userArr.add(String.valueOf(sr[1]));
				userArr.add(String.valueOf(sr[2]));
				
				flag = true;
				break;

			case "SendMevisitotsByStayTimeToDay":
				userArr.add("SendMevisitotsByStayTimeToDay");
				float[] av = MySqlQuery.visitotsByStayTimeToDay(s[1], s[2], s[3], mySql);
				for (float a : av) {
					userArr.add(String.valueOf(a));
				}
				
				flag = true;
				break;

			case "SendMevisitotsByStayTimeToMonth":
				
				userArr.add("SendMevisitotsByStayTimeToMonth");
				days = daily(s[2]);
				float[] avm = new float[3];
				float[] avm1 = new float[3];
				for (String day : days) {
					
					avm = MySqlQuery.visitotsByStayTimeToDay(s[1], day, s[3], mySql);
					avm1[0] += avm[0];
					avm1[1] += avm[1];
					avm1[2] += avm[2];

				}
				

				float count1 = avm1[0] + avm1[1] + avm1[2];
				avm1[0] = avm1[0] * 100 / count1;
				avm1[1] = avm1[1] * 100 / count1;
				avm1[2] = avm1[2] * 100 / count1;
				
				for (float a : avm1) {
					userArr.add(String.valueOf(a));
				}
				
				flag = true;
				break;

			case "UpdateConnected":
				flag = true;
				userArr.add("UpdateConnected");
				if (MySqlQuery.updateInConnectV(s[1], "0", mySql)) {
					userArr.add("V");
				} else {
					MySqlQuery.updateInConnectE(s[1], "0", mySql);
					userArr.add("E");
				}

				break;
			case "justindeftifictionTraveler":
				
				flag = true;
				query = "SELECT * FROM visitor WHERE Idnumber='" + s[1] + "';";
				query1 = "SELECT * FROM visitor WHERE SubscriptionNum='" + s[1] + "';";
				userArr = MySqlQuery.justindeftifictionInDB(query, query1, "justindeftifictionTraveler", mySql);
				if (userArr.get(1).equals("NotExisting")) {
					userArr.add(s[1]);
				}
				break;

			default:
				flag = false;

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!flag) {
			System.out.println("Not** Found");
			try {
				client.sendToClient("Error!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			try {
				client.sendToClient(userArr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());

	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
/**
 * Receives an hour and time of stay and returns an appropriate set of hours according to the time of stay
 * @param time time
 * @param stayTime  stay time
 * @return relevant times
 */
	public static String[] BringRelevantTimes(String time, int stayTime) {

		String[] times = { "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
				"17:00", "18:00", "19:00" };
		int j = 0, s, f;
		for (int i = 0; i < times.length; i++) {
			if (times[i].equals(time)) {
				j = i;
			}
		}
		if (j + 1 - stayTime < 0) {
			s = 0;

		} else {
			s = (j + 1) - stayTime;
		}
		if (j + stayTime > 12) {
			f = 13;

		} else {
			f = (j) + stayTime;
		}
		return Arrays.copyOfRange(times, s, f);
	}
/**
 * Adds to the corresponding priority the value given to the function
 *@param park name
 *@return which
 */
	public int whichPriority(String park) {
		if (park.equals("park1")) {
			return ++PriorityOfPark1;
		}
		if (park.equals("park2")) {
			return ++PriorityOfPark2;
		}
		if (park.equals("park3")) {
			return ++PriorityOfPark3;
		}
		return -1;
	}

	/**
	 * Adds a number of their visitors to the current number of visitors to the park
	 *@param park= park name
	 *@param number=number of the park
	 * @return visitor number
	 */
	public int AddvisitorsNumber(String park, int number) {
		
		if (park.equals("park1")) {
			VisitorsInPArk1 += number;
			return VisitorsInPArk1;
		}
		if (park.equals("park2")) {
			VisitorsInPArk2 += number;
			return VisitorsInPArk2;
		}
		if (park.equals("park3")) {
			VisitorsInPArk3 += number;
			return VisitorsInPArk3;
		}
		return -1;
	}

	public static ArrayList<String> TravelerRegistration(String query, String query1, String query3, String whichCase,
			mysqlConnection mySql, String[] s, int type) throws SQLException {

		ArrayList<String> userArr = new ArrayList<>();
		if (!MySqlQuery.ExistingUserOnDB(query1, mySql)) {
			userArr = MySqlQuery.CreatNewVisitor(query, query1, whichCase, mySql);

		} else {
			if (MySqlQuery.ExistingUser(query1, mySql, type)) {
				userArr = MySqlQuery.CreatNewVisitor(query3, query1, whichCase, mySql);
			} else {
				userArr.add(whichCase);
				userArr.add("IsExist");
			}
		}
		return userArr;
	}

	
/**
 * Get a month and return an array of size 2 the date of the beginning and end of the month
 * @param month =month number
 * @return String[]
 */
	public static String[] month(String month) {
		String[] TowDays = new String[2];
		String[] start = month.split("-");
		
		TowDays[0] = start[0] + "-" + start[1] + "-01";
		if (start[1].equals("01") || start[1].equals("03") || start[1].equals("05") || start[1].equals("07")
				|| start[1].equals("08") || start[1].equals("10") || start[1].equals("12")) {
			TowDays[1] = start[0] + "-" + start[1] + "-31";
		} else if (start[1].equals("04") || start[1].equals("06") || start[1].equals("09") || start[1].equals("11")) {
			TowDays[1] = start[0] + "-" + start[1] + "-30";
		} else {
			TowDays[1] = start[0] + "-" + start[1] + "-28";
			if ((Integer.parseInt(start[0]) % 4.0) == 0) {
				TowDays[1] = start[0] + "-" + start[1] + "-29";
			}
		}
		return TowDays;
	}
/**
 * Make it a month easier and return the days of the beginning of each week in that month
 *@param  month=month
 * @return String[]
 */
	public static String[] week(String month) {
		String[] TowDays = new String[10];
		String[] start = month.split("-");
		TowDays[0] = start[0] + "-" + start[1] + "-01";
		TowDays[1] = start[0] + "-" + start[1] + "-07";
		TowDays[2] = start[0] + "-" + start[1] + "-08";
		TowDays[3] = start[0] + "-" + start[1] + "-14";
		TowDays[4] = start[0] + "-" + start[1] + "-15";
		TowDays[5] = start[0] + "-" + start[1] + "-21";
		TowDays[6] = start[0] + "-" + start[1] + "-22";
		TowDays[7] = start[0] + "-" + start[1] + "-28";
		if (start[1].equals("01") || start[1].equals("03") || start[1].equals("05") || start[1].equals("07")
				|| start[1].equals("08") || start[1].equals("10") || start[1].equals("12")) {
			TowDays[8] = start[0] + "-" + start[1] + "-29";
			TowDays[9] = start[0] + "-" + start[1] + "-31";
		} else if (start[1].equals("04") || start[1].equals("06") || start[1].equals("09") || start[1].equals("11")) {
			TowDays[8] = start[0] + "-" + start[1] + "-29";
			TowDays[9] = start[0] + "-" + start[1] + "-30";
		} else {

			if ((Integer.parseInt(start[0]) % 4.0) == 0) {
				TowDays[8] = start[0] + "-" + start[1] + "-29";
				TowDays[9] = start[0] + "-" + start[1] + "-29";
			} else {
				TowDays[8] = null;
				TowDays[9] = null;
			}
		}
		return TowDays;
	}
/**
 * Receives a month and returns more than a string of dates for all the days of the month
 * @param month =month name
 * @return String []
 */
	public static String[] daily(String month) {
		String[] res = month(month);
		String[] res2 = res[1].split("-");
		String[] days = new String[Integer.parseInt(res2[2])];
		for (int i = 0; i < 9; i++) {
			days[i] = res2[0] + "-" + res2[1] + "-0" + String.valueOf(i + 1);
		}
		for (int i = 9; i < Integer.parseInt(res2[2]); i++) {
			days[i] = res2[0] + "-" + res2[1] + "-" + String.valueOf(i + 1);
		}
		return days;
	}

}
