package Server;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import gui.MessageBox;
import javafx.scene.control.Alert.AlertType;

/**
 * This department runs the queries we built from the EchoServer. In each
 * method, the database is contacted, and each method returns the necessary
 * details to the accessory in case of queries asking for information.
 * 
 * @author liranhaim
 *
 */
public class MySqlQuery {
	public static int numberoforder = 0;
	static Statement stmt;
	static String[] parks = { "park1", "park2", "park3" };

	/**
	 * A method that searches the database for the visitor's information according
	 * to an identification number or subscription number and,if found, returns all
	 * his details and also updates the database that the visitor is connected by
	 * calling an auxiliary method.
	 * 
	 * 
	 * 
	 * @param query     is a query that requests information for a traveler by ID
	 *                  number.
	 * @param query1    is a query that requests information for a traveler by
	 *                  subscription number.
	 * @param whichCase is String with the name of the request we received from a
	 *                  Client
	 * @param mySql     is a single instance of the DB connection
	 * @return The details of the visitor if he is in the database. It also updates
	 *         the database it is connected to. In case there is no return in the
	 *         second place, does "NotExisting"
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> indeftifictionInDB(String query, String query1, String whichCase,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be taken.
		returnTOserver.add(whichCase);
		ResultSet rs = stmt.executeQuery(query);
		String id = null;
		String connect = null;
		while (rs.next()) {
			id = rs.getString("Idnumber");
			connect = rs.getString("IsConnected");
			returnTOserver.add("Existing");
			returnTOserver.add(rs.getString("Firstname"));
			returnTOserver.add(rs.getString("Lastname"));
			returnTOserver.add(rs.getString("Idnumber"));
			returnTOserver.add(rs.getString("Email"));
			returnTOserver.add(rs.getString("Telephone"));
			returnTOserver.add(rs.getString("Type"));
			if (rs.getString("Type").equals("2")) {
				returnTOserver.add(rs.getString("FamilyMembersAmount"));
				returnTOserver.add(rs.getString("CreditCardNumber"));
				returnTOserver.add(rs.getString("CardValidity"));
				returnTOserver.add(rs.getString("3Digits"));
				returnTOserver.add(rs.getString("SubscriptionNum"));

			}
		}

		rs.close();
		if (returnTOserver.size() == 1) {
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				id = rs.getString("Idnumber");
				connect = rs.getString("IsConnected");
				returnTOserver.add("Existing");
				returnTOserver.add(rs.getString("Firstname"));
				returnTOserver.add(rs.getString("Lastname"));
				returnTOserver.add(rs.getString("Idnumber"));
				returnTOserver.add(rs.getString("Email"));
				returnTOserver.add(rs.getString("Telephone"));
				returnTOserver.add(rs.getString("Type"));
				returnTOserver.add(rs.getString("FamilyMembersAmount"));
				returnTOserver.add(rs.getString("CreditCardNumber"));
				returnTOserver.add(rs.getString("CardValidity"));
				returnTOserver.add(rs.getString("3Digits"));
				returnTOserver.add(rs.getString("SubscriptionNum"));

			}
		}

		rs.close();
		if (returnTOserver.size() == 1) {
			returnTOserver.clear();
			returnTOserver.add(whichCase);
			returnTOserver.add("NotExisting");
			return returnTOserver;

		}

		if (connect.equals("1")) {
			returnTOserver.clear();
			returnTOserver.add(whichCase);
			returnTOserver.add("TheVisitorConnectAlready");
			return returnTOserver;
		}
		if (connect.equals("0")) {

			boolean ti = updateInConnectV(id, "1", mySql);

		}

		return returnTOserver;
	}

	/**
	 * A method that helps us initialize the subscription number to the updated
	 * number
	 * 
	 * @param mySql is a single instance of the DB connection
	 * @return The last subscription number found in the database
	 * @throws SQLException is Exception of sql.
	 */
	public static int InitializePrioritysubscriptionnum(mysqlConnection mySql) throws SQLException {
		int res = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		String query = "SELECT MAX(SubscriptionNum) FROM visitor;";

		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			res = rs.getInt(1);

		}
		rs.close();
		return res;
	}

	/**
	 * The method helps us to initialize the ID number of the discount to the
	 * current number.
	 * 
	 * @param mySql is a single instance of the DB connection.
	 * @return Returns the ID number of the last discount.
	 * @throws SQLException is Exception of sql.
	 */
	public static int InitializePriorityDiscount(mysqlConnection mySql) throws SQLException {
		int res = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		String query = "SELECT MAX(DiscountNum) FROM discounts;";

		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			res = rs.getInt(1);

		}
		rs.close();
		return res;

	}

	/**
	 * A method that puts a visitor on a waiting list and gives him a number that is
	 * used as a priority in the queue
	 * 
	 * @param query is a query that puts the order in the waiting list.
	 * @param mySql is a single instance of the DB connection.
	 * @return true if the insert was successful or false if wasn't.
	 * @throws SQLException is Exception of sql.
	 */
	public static boolean EnterTOWaitingList(String query, mysqlConnection mySql) throws SQLException {

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		int d = stmt.executeUpdate(query);

		if (d > 0) {
			return true;
		}
		return false;
	}

	/**
	 * The method helps us understand if the visitor is already in a database.
	 * 
	 * @param query is a query that checks if the visitor already exists in a
	 *              database.
	 * @param mySql is a single instance of the DB connection.
	 * @param type  is a variable that indicates the type of visitor.
	 * @return True if the visitor exists and false if not.
	 * @throws SQLException is Exception of sql.
	 */
	public static boolean ExistingUser(String query, mysqlConnection mySql, int type) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			returnTOserver.add(rs.getString("type"));
		}
		if (returnTOserver.get(0).equals(type + ""))
			return false;
		return true;

	}

	/**
	 * A method that contains several scenarios. The scenario for order confirmation
	 * a day ahead of time. Scenario two Retrieving information for a specific
	 * order. The third scenario for order confirmation And a fourth scenario for
	 * deleting an order.
	 * 
	 * 
	 * @param query1        is a query that searches for an order number from a
	 *                      particular park on whom the identity number of the
	 *                      orderer.
	 * @param query2        is a query that searches for an order number from a
	 *                      particular park on whom the ID number of the order is
	 * @param park          is the requested park.
	 * @param whichCase     a string indicating the client's request
	 * @param mySql         is a single instance of the DB connection.
	 * @param numofvisitors the number of Visitors.
	 * @return order details
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> OrdersInToConfirm(String query1, String query2, String park, String whichCase,
			mysqlConnection mySql, String numofvisitors) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		switch (whichCase) {
		case "SendMeOrdersToConfirm":
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				if (rs.getString("Status").equals("Waiting2")) {
					str.append(park);
					str.append("#");
					str.append(rs.getString("Dateofvisit"));
					str.append("#");
					str.append(rs.getString("Vistingtime"));
					str.append("#");
					str.append(rs.getString("ordernumber"));
					returnTOserver.add(str.toString());
					str.delete(0, str.length());
				}
			}
			break;

		case "SentMeTheOrderDetails":
			rs = stmt.executeQuery(query1);
			if (rs.next()) {
				returnTOserver.add(park);
				returnTOserver.add(rs.getString("Typeoforder"));
				returnTOserver.add(rs.getString("Vistingtime"));
				returnTOserver.add(rs.getString("Numberofvistors"));
				returnTOserver.add(rs.getString("Dateofvisit"));
				returnTOserver.add(rs.getString("Idnumber"));
				returnTOserver.add(rs.getString("Email"));
				returnTOserver.add(rs.getString("TotalPrice"));
				returnTOserver.add(rs.getString("ordernumber"));
				returnTOserver.add(rs.getString("Status"));
			}
			rs.close();
			if (returnTOserver.size() == 0) {
				returnTOserver.add("ThereIsNotOrders");
			}
			break;

		case "UpdateOrderToConfirm1":
			returnTOserver.add(whichCase);
			int k = stmt.executeUpdate(query1);
			if (k > 0) {
				rs = stmt.executeQuery(query2);
				if (rs.next()) {
					returnTOserver.add("yes");
				}
			} else
				returnTOserver.add("no");
			break;

		case "UpdateOrderToDeleted1":
			returnTOserver.add(whichCase);
			k = stmt.executeUpdate(query1);

			if (k > 0) {
				rs = stmt.executeQuery(query2);
				if (rs.next()) {
					returnTOserver.add("yes");
				}
			} else
				returnTOserver.add("no");
			break;
		}
		return returnTOserver;
	}

	/**
	 * A method with four scenarios Scenario 1 Retrieving order information on a
	 * waiting list Scenario Two Retrieving Order Information Third scenario
	 * confirmation or update Scenario IV Update or delete
	 * 
	 * @param query1    is query requesting order information by ID number.
	 * @param query2    is query requesting order information from waiting list by
	 *                  ID number.
	 * @param park      is the requested park.
	 * @param whichCase a string indicating the client's request
	 * @param mySql     is a single instance of the DB connection.
	 * @return order details
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> OrdersInWaitingList(String query1, String query2, String park, String whichCase,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		switch (whichCase) {
		case "SendMeOrdersInWaitingListToThisId":
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				if (rs.getString("Status").equals("Waiting1")) {
					str.append(park);
					str.append("#");
					str.append(rs.getString("Dateofvisit"));
					str.append("#");
					str.append(rs.getString("Vistingtime"));
					str.append("#");
					str.append(rs.getString("ordernumber"));
					returnTOserver.add(str.toString());
					str.delete(0, str.length());
				}
			}
			rs.close();
			break;

		case "SentMeAllTheInfoToThisOrder":
			returnTOserver.add(whichCase);
			rs = stmt.executeQuery(query1);
			if (rs.next()) {
				returnTOserver.add(park);
				returnTOserver.add(rs.getString("Typeoforder"));
				returnTOserver.add(rs.getString("Vistingtime"));
				returnTOserver.add(rs.getString("Numberofvistors"));
				returnTOserver.add(rs.getString("Dateofvisit"));
				returnTOserver.add(rs.getString("Idnumber"));
				returnTOserver.add(rs.getString("Email"));
				returnTOserver.add(rs.getString("TotalPrice"));
				returnTOserver.add(rs.getString("ordernumber"));
				returnTOserver.add(rs.getString("Status"));

			}
			rs.close();
			if (returnTOserver.size() == 1) {
				returnTOserver.add("ThereIsNoOrders");
			}
			break;

		case "UpdateOrderToConfirm":
			returnTOserver.add(whichCase);
			int k = stmt.executeUpdate(query1);
			if (k > 0) {
				rs = stmt.executeQuery(query2);
				if (rs.next()) {
					returnTOserver.add("yes");
				}
				rs.close();
			} else
				returnTOserver.add("no");
			break;

		case "UpdateOrderToDeleted":
			returnTOserver.add(whichCase);
			k = stmt.executeUpdate(query1);
			if (k > 0) {
				rs = stmt.executeQuery(query2);
				if (rs.next()) {
					returnTOserver.add("yes");
				}
			} else
				returnTOserver.add("no");
			break;

		}
		return returnTOserver;

	}

	/**
	 * A method that deletes an order from the database.
	 * 
	 * @param park      is the requested park.
	 * @param query     is a query that updates the status to Deleted2.
	 * @param whichCase a string indicating the client's request
	 * @param mySql     is a single instance of the DB connection.
	 * @return String whether successful or not.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> DeleteFromDB(String park, String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		returnTOserver.add(whichCase);
		int d = stmt.executeUpdate(query);
		if (d > 0) {
			returnTOserver.add("Succeeded");
		} else {
			returnTOserver.add("NotSucceeded");
		}

		return returnTOserver;
	}

	/**
	 * A method that adds a new auditor to a database.
	 * 
	 * @param query     is a query that adds the visitor's information to a table in
	 *                  a database
	 * @param query1    is a query that retrieves information by ID number
	 * @param whichCase a string indicating the client's request
	 * @param mySql     is a single instance of the DB connection.
	 * @return Returns the visitor's details.
	 * @throws SQLException is Exception of sql.
	 */

	public static ArrayList<String> CreatNewVisitor(String query, String query1, String whichCase,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0) {
			ResultSet rs = stmt.executeQuery(query1);
			returnTOserver.add(whichCase);
			returnTOserver.add("NotExist");

			while (rs.next()) {
				for (int i = 1; i < 7; i++) {
					returnTOserver.add(rs.getString(i));
				}
				if (rs.getString(6).equals("2")) {
					returnTOserver.add(rs.getString(7));
					returnTOserver.add(rs.getString(8));
					returnTOserver.add(rs.getString(9));
					returnTOserver.add(rs.getString(10));
				}
			}
			if (returnTOserver.size() == 1) {
				returnTOserver.clear();
				returnTOserver.add(whichCase);
				returnTOserver.add("NotExistSubscription");

			}
		} else {
			returnTOserver.add("not succeed");

		}
		return returnTOserver;

	}

	/**
	 * A method that creates a new order.
	 * 
	 * @param query     is a query that puts the visitor's details in the visitors
	 *                  table
	 * @param query1    is a query that retrieves information from the visitor by ID
	 *                  number
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return The order number
	 * @throws SQLException is Exception of sql.
	 */

	public static ArrayList<String> CreatNewOrder(String query, String query1, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		String id = null, email = null, phonenumber = null, ordernumber = null;

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0) {
			ResultSet rs = stmt.executeQuery(query1);
			returnTOserver.add(whichCase);
			while (rs.next()) {
				returnTOserver.add(String.valueOf(rs.getInt("ordernumber")));
				ordernumber = String.valueOf(rs.getInt("ordernumber"));
				id = rs.getString("Idnumber");
				email = rs.getString("Email");
			}
			rs.close();

		} else {
			returnTOserver.clear();
			returnTOserver.add(whichCase);
			returnTOserver.add("Error!");
		}
		return returnTOserver;
	}

	/**
	 * A method that checks if there is space available in the park. according to
	 * the relevant time range for a specific time
	 * 
	 * @param park  is the requested park.
	 * @param date  is the requested date.
	 * @param time  is the requested time.
	 * @param mySql a single instance of the DB connection.
	 * @return number of bookings within the above hours
	 * @throws SQLException is Exception of sql.
	 */
	public static int CheckVacancy(String park, String date, String time, mysqlConnection mySql) throws SQLException {
		int counter = 0;
		String query = null;
		query = "SELECT * FROM " + park + " WHERE Vistingtime='" + time + "' AND Dateofvisit ='" + date + "';";
		try {
			stmt = mySql.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				if (!rs.getString("Status").equals("Deleted2") && !rs.getString("Status").equals("Deleted1")
						&& !rs.getString("Status").equals("Finish")) {
					counter += Integer.parseInt(rs.getString("Numberofvistors"));
				}
			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		return counter;
	}

	/**
	 * A method that checks how many orders have already been placed.
	 * 
	 * @param park  is the requested park.
	 * @param date  is the requested date.
	 * @param time  is the requested time.
	 * @param mySql a single instance of the DB connection.
	 * @return the Number of orders already placed
	 * @throws SQLException is Exception of sql.
	 */
	public static int CheckVacancy1(String park, String date, String time, mysqlConnection mySql) throws SQLException {
		int counter = 0;
		String query = null;
		query = "SELECT * FROM " + park + " WHERE Vistingtime='" + time + "' AND Dateofvisit ='" + date + "';";
		try {
			stmt = mySql.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				if (rs.getString("Status").equals("Finish"))
					counter += Integer.parseInt(rs.getString("Numberofvistors"));
			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		return counter;
	}

	/**
	 * A method that returns the details of the primers from the requested park.
	 * 
	 * @param park  is the requested park
	 * @param mySql a single instance of the DB connection.
	 * @return the parameter.
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] CheckingParameters(String park, mysqlConnection mySql) throws SQLException {
		int[] parameters = new int[4];
		String query = null;

		query = "SELECT * FROM parkdetails WHERE park='" + park + "';";

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			parameters[0] = rs.getInt("MaxCover");
			parameters[1] = rs.getInt("StayTime");
			parameters[2] = rs.getInt("paraGap");
			parameters[3] = rs.getInt("VisitorsOnPark");

		}
		return parameters;

	}

	/**
	 * 
	 * @param park      is the requested park
	 * @param ordernum  the number of order.
	 * @param id        the id of visitor
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @param type      is the type of order.
	 * @return Order details
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> checkTheOrdernumber(String park, String ordernum, String id, String whichCase,
			mysqlConnection mySql, String type) throws SQLException {
		String ID = null;
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		String query1 = "SELECT * FROM visitor WHERE Idnumber='" + id + "';";
		String query2 = "SELECT * FROM visitor WHERE SubscriptionNum='" + id + "';";

		ID = returnTheID(query1, query2, mySql);

		if (ID != "NotExisting") {

			String query = "SELECT * FROM " + park + " WHERE ordernumber='" + ordernum + "' AND Idnumber='" + ID + "';";
			ResultSet rs = stmt.executeQuery(query);
			returnTOserver.add(whichCase);
			while (rs.next()) {
				if (type.equals("1")) {
					String s = rs.getString("Status");
					if (s.equals("Active") || s.equals("Waiting1") || s.equals("Waiting2")) {
						returnTOserver.add("yes");
						returnTOserver.add(String.valueOf(rs.getInt("ordernumber")));
						returnTOserver.add(park);
						returnTOserver.add(rs.getString("Dateofvisit"));
						returnTOserver.add(rs.getString("Vistingtime"));
						returnTOserver.add(rs.getString("Numberofvistors"));
						returnTOserver.add(rs.getString("Email"));
						returnTOserver.add(rs.getString("Typeoforder"));
						returnTOserver.add(rs.getString("Status"));
						returnTOserver.add(rs.getString("Idnumber"));
						returnTOserver.add(rs.getString("TotalPrice"));
						returnTOserver.add(rs.getString("IsPaid"));
					}
				}
				if (type.equals("2")) {
					String s = rs.getString("Status");
					if (s.equals("Approved")) {
						returnTOserver.add("yes");
						returnTOserver.add(String.valueOf(rs.getInt("ordernumber")));
						returnTOserver.add(park);
						returnTOserver.add(rs.getString("Dateofvisit"));
						returnTOserver.add(rs.getString("Vistingtime"));
						returnTOserver.add(rs.getString("Numberofvistors"));
						returnTOserver.add(rs.getString("Email"));
						returnTOserver.add(rs.getString("Typeoforder"));
						returnTOserver.add(rs.getString("Status"));
						returnTOserver.add(rs.getString("Idnumber"));
						returnTOserver.add(rs.getString("TotalPrice"));
						returnTOserver.add(rs.getString("IsPaid"));
						returnTOserver.add(rs.getString("PriceGap"));
					}
				}
				if (type.equals("3")) {
					String s = rs.getString("Status");
					if (s.equals("Enter")) {
						returnTOserver.add("yes");
					}
				}
			}
			returnTOserver.add("no");
			rs.close();
		}
		return returnTOserver;
	}

	/**
	 * A method that checks whether the order has been approved in advance and is on
	 * the appropriate date and time.
	 * 
	 * @param query     is a query that checks if the order has been confirmed by
	 *                  fire and if it is on the appropriate date and, time range
	 * @param park      is the requested park
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return Order details
	 * @throws SQLException is Exception of sql.
	 */

	public static ArrayList<String> EnterToTheParkFunc(String query, String park, String whichCase,
			mysqlConnection mySql) throws SQLException {

		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		returnTOserver.add(whichCase);
		while (rs.next()) {

			returnTOserver.add("yes");
			returnTOserver.add(String.valueOf(rs.getInt("ordernumber")));
			returnTOserver.add(park);
			returnTOserver.add(rs.getString("Dateofvisit"));
			returnTOserver.add(rs.getString("Vistingtime"));
			returnTOserver.add(rs.getString("Numberofvistors"));
			returnTOserver.add(rs.getString("Email"));
			returnTOserver.add(rs.getString("Typeoforder"));
			returnTOserver.add(rs.getString("Status"));
			returnTOserver.add(rs.getString("Idnumber"));
			returnTOserver.add(rs.getString("TotalPrice"));
			returnTOserver.add(rs.getString("IsPaid"));
			returnTOserver.add(rs.getString("PriceGap"));
		}
		return returnTOserver;
	}

	/**
	 * Method that updates order
	 * 
	 * @param query     is a query that updates the order details.
	 * @param query1    Query that returns the order details.
	 * @param park      is the requested park
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return Order details
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> UpdateOrder(String query, String query1, String park, String whichCase,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		int k = stmt.executeUpdate(query);
		if (k > 0) {
			ResultSet rs = stmt.executeQuery(query1);

			returnTOserver.add(whichCase);
		}

		return returnTOserver;
	}

	/**
	 * A query that initializes the order number to be the updated number
	 * 
	 * @param mySql a single instance of the DB connection.
	 * @return The last order number
	 * @throws SQLException is Exception of sql.
	 */
	public static int InitializeOrderNumber(mysqlConnection mySql) throws SQLException {

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		// String query1="SELECT * FROM park1E WHERE ordernumber = max" + "';";
		int[] maxValue = new int[3];
		String query1 = "SELECT MAX(ordernumber) FROM park1;";
		String query2 = "SELECT MAX(ordernumber) FROM park2;";
		String query3 = "SELECT MAX(ordernumber) FROM park3;";
		ResultSet rs = stmt.executeQuery(query1);
		if (rs.next()) {
			maxValue[0] = rs.getInt(1);
		}
		rs.close();
		rs = stmt.executeQuery(query2);
		if (rs.next()) {
			maxValue[1] = rs.getInt(1);
		}
		rs.close();
		rs = stmt.executeQuery(query3);
		if (rs.next()) {
			maxValue[2] = rs.getInt(1);
		}
		rs.close();
		int max = 0;

		for (int i = 0; i < maxValue.length; i++) {
			if (maxValue[i] > max) {
				max = maxValue[i];
			}
		}
		return max;

	}

	/**
	 * A method that initializes the last priority number by park
	 * 
	 * @param parks is the requested park
	 * @param mySql a single instance of the DB connection.
	 * @return the max priority number of the park.
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] InitializePriorityPark(String[] parks, mysqlConnection mySql) throws SQLException {
		int[] res = { 0, 0, 0 };
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		String query = "SELECT MAX(priority) FROM waitinglist WHERE park = '" + parks[0] + "';";
		String query1 = "SELECT MAX(priority) FROM waitinglist WHERE park = '" + parks[1] + "';";
		String query2 = "SELECT MAX(priority) FROM waitinglist WHERE park = '" + parks[2] + "';";
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			res[0] = rs.getInt(1);
			;
		}
		rs.close();
		rs = stmt.executeQuery(query1);
		if (rs.next()) {
			res[1] = rs.getInt(1);
			;
		}
		rs.close();
		rs = stmt.executeQuery(query2);
		if (rs.next()) {
			res[2] = rs.getInt(1);
		}
		rs.close();

		return res;

	}

	/**
	 * Initializes the number of people currently in the park
	 * 
	 * @param mySql a single instance of the DB connection.
	 * @return the number of visitors.
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] InitializeVisitorsInParks(mysqlConnection mySql) throws SQLException {
		int[] res = { 0, 0, 0 };
		String query = null;
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		for (int i = 0; i < 3; i++) {
			query = "SELECT VisitorsOnPark FROM parkdetails WHERE park='" + parks[i] + "';";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				res[i] = rs.getInt(1);
			}
			rs.close();

		}

		return res;
	}

	/**
	 * A method that checks if an auditor exists in a database
	 * 
	 * @param query is a query that returns the visitor's information
	 * @param mySql a single instance of the DB connection.
	 * @return true if the visitor is exist and false isn't.
	 * @throws SQLException is Exception of sql.
	 */
	public static boolean ExistingUserOnDB(String query, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {

			return true;
		}

		return false;
	}

	/**
	 * A method that updates the booking details at the entrance to the park
	 * 
	 * @param query A query that updates the order details at the moment of entry
	 * @param mySql a single instance of the DB connection.
	 * @return true if Succeeded and false if not.
	 * @throws SQLException is Exception of sql.
	 */
	public static boolean UpDateEnterExit(String query, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0) {
			return true;
		}
		return false;

	}

	/**
	 * A method that returns the number of visitors to the park
	 * 
	 * @param query A query that retrieves the figure of the number of visitors to
	 *              the park
	 * @param mySql a single instance of the DB connection.
	 * @return Current visitors to the park
	 * @throws SQLException is Exception of sql.
	 */
	public static String ReturnNumberofvistors(String query, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			return rs.getNString(1);
		}
		return null;
	}

	/**
	 * A method that updates the parameters of a park
	 * 
	 * @param query is a query that updates the parameters of the park
	 * @param mySql a single instance of the DB connection.
	 * @return 1 if Succeeded 0 if not.
	 * @throws SQLException is Exception of sql.
	 */
	public static int UpDateParkDatailsParameter(String query, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * A method that returns the manager's reminder
	 * 
	 * @param query  is a query that returns the reminder
	 * @param query1 A query that updates the reminder to be blank
	 * @param mySql  a single instance of the DB connection.
	 * @return the reminder.
	 * @throws SQLException is Exception of sql.
	 */
	public static String ReminderFromManager(String query, String query1, mysqlConnection mySql) throws SQLException {
		String reminder = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			reminder = rs.getString(1);
		}
		int k = stmt.executeUpdate(query1);
		return reminder;
	}

	/**
	 * A method that returns the parameter of the difference of the number of people
	 * 
	 * @param query     is a query that returns the requested parameter
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return the gap parameter.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> GiveMeTheGapParameters(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		returnTOserver.add(whichCase);
		while (rs.next()) {
			Integer s = rs.getInt("WFAparaGap");
			if (s.equals(-1)) {
				returnTOserver.add("NotGapparameters1");
			} else {
				returnTOserver.add("GapParameters1");
				returnTOserver.add(s.toString());
			}
		}
		rs.close();
		return returnTOserver;
	}

	/**
	 * A method that returns the parameter of the max number of people in park
	 * 
	 * @param query     is a query that returns the requested parameter
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return the maxQuota parameter.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> GiveTheMaxQuota(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		returnTOserver.add(whichCase);
		while (rs.next()) {
			Integer s = rs.getInt("WFAMaxCover");

			if (s.equals(-1)) {
				returnTOserver.add("NotMaxQuotaParameters");
			} else {
				returnTOserver.add("MaxqoutaParameters");
				returnTOserver.add(s.toString());
			}

		}
		rs.close();
		return returnTOserver;
	}

	/**
	 * A method that returns the parameter of the stay tine
	 * 
	 * @param query     is a query that returns the requested parameter
	 * @param whichCase a string indicating the client's request
	 * @param mySql     a single instance of the DB connection.
	 * @return the StayTime parameter.
	 * @throws SQLException is Exception of sql.
	 */

	public static ArrayList<String> GiveMeTheStayTimeParameters(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		returnTOserver.add(whichCase);

		while (rs.next()) {
			Integer s = rs.getInt("WFAStayTime");

			if (s.equals(-1)) {
				returnTOserver.add("NotStayTimeParameters");
			} else {
				returnTOserver.add("StayTimeParameters");
				returnTOserver.add(s.toString());
			}

		}
		rs.close();
		return returnTOserver;

	}

	/**
	 * A method that returns the parameter of the gap Before approval
	 * 
	 * @param query     is a query that returns the requested paramete
	 * @param mySql     a single instance of the DB connection.
	 * @return the gap parameter.
	 * @throws SQLException is Exception of sql.
	 */

	public static int returnWFAParameter(String query, mysqlConnection mySql) throws SQLException {
		int para = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {

			para = rs.getInt(1);

		}
		rs.close();
		return para;

	}

	/**
	 * A method that generates a monthly income statement
	 * 
	 * @param query is a query that returns all orders on the requested dates
	 * @param mySql a single instance of the DB connection.
	 * @return The proceeds from the park this month
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] monthlyIncomeReport(String query, mysqlConnection mySql) throws SQLException {
		int[] returnTOserver = new int[1];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		ResultSet rs = stmt.executeQuery(query);

		returnTOserver[0] = 0;

		while (rs.next()) {
			if (rs.getString("Status").equals("Finish"))
				returnTOserver[0] = returnTOserver[0] + Integer.parseInt(rs.getString("TotalPrice"));

		}
		rs.close();
		return returnTOserver;
	}

	/**
	 * A method that returns a weekly income statement
	 * 
	 * @param park  the park requested
	 * @param days  the days requested
	 * @param mySql a single instance of the DB connection.
	 * @return The proceeds from the park this week.
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] WeeklyIncomeReport(String park, String[] days, mysqlConnection mySql) throws SQLException {
		int[] returnTOserver = new int[5];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query;
		ResultSet rs = null;
		int index = 0;
		for (int i = 0; i < 9; i = i + 2) {
			query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[i] + "' AND '" + days[i + 1] + "';";
			if (days[i] == null) {
				returnTOserver[i] = -1;

			} else {
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					if (rs.getString("Status").equals("Finish"))
						returnTOserver[index] = returnTOserver[index] + Integer.parseInt(rs.getString("TotalPrice"));
				}
				rs.close();
				index++;
			}
		}

		return returnTOserver;
	}

	/**
	 * Hourly income statement
	 * 
	 * @param park  the park requested
	 * @param days  the days requested
	 * @param time  the time requested
	 * @param mySql a single instance of the DB connection.
	 * @return The proceeds from the park this hour.
	 * @throws SQLException is Exception of sql.
	 */
	public static int HourlyIncomeReport(String park, String[] days, String time, mysqlConnection mySql)
			throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[0] + "' AND '" + days[1]
				+ "' AND Vistingtime = '" + time + "';";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			if (rs.getString("Status").equals("Finish"))
				returnTOserver = returnTOserver + Integer.parseInt(rs.getString("TotalPrice"));

		}
		rs.close();
		return returnTOserver;
	}

	/**
	 * daily income statement
	 * 
	 * @param park  the park requested
	 * @param day   the day requested
	 * @param mySql a single instance of the DB connection.
	 * @return The proceeds from the park this day.
	 * @throws SQLException is Exception of sql.
	 */
	public static String dailyIncomeReport(String park, String day, mysqlConnection mySql) throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Dateofvisit ='" + day + "';";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			if (rs.getString("Status").equals("Finish"))
				returnTOserver = returnTOserver + Integer.parseInt(rs.getString("TotalPrice"));

		}

		rs.close();
		return String.valueOf(returnTOserver);

	}

	/**
	 * Weekly Occupancy Report of the Park
	 * 
	 * @param park    is the park requested
	 * @param query22 is a query that returns the park quota number
	 * @param days    is the the days requested
	 * @param mySql   a single instance of the DB connection.
	 * @return The amount of people who were in the park this week
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] WeeklyOccupancyReport(String park, String query22, String[] days, mysqlConnection mySql)
			throws SQLException {
		String maxCover = null;
		int[] returnTOserver = new int[5];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		ResultSet rss = stmt.executeQuery(query22);
		while (rss.next()) {
			maxCover = rss.getString("MaxCover");
		}
		rss.close();
		String query;
		int sum = 0;
		ResultSet rs = null;

		for (int i = 0; i < 9; i = i + 2) {
			sum = 0;
			query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[i] + "' AND '" + days[i + 1] + "';";
			if (days[i] == null) {
				returnTOserver[i] = -1;
			} else {
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					if (rs.getString("Status").equals("Done"))

					{

						sum = sum + Integer.parseInt(rs.getString("Numberofvistors"));
					}
				}

				if (sum == 7 * Integer.parseInt(maxCover))
					returnTOserver[i] = 1;
				else
					returnTOserver[i] = 0;

			}
			rs.close();

		}
		return returnTOserver;
	}

	/**
	 * Hourly Occupancy Report of the Park
	 * 
	 * @param park    is the park requested
	 * @param query22 is a query that returns the park quota number
	 * @param days    is the the days requested
	 * @param time    is the time requested.
	 * @param mySql   a single instance of the DB connection.
	 * @return The amount of people who were in the park this hourly
	 * @throws SQLException is Exception of sql.
	 */
	public static int HourlyOccupancyReport(String park, String query22, String[] days, String time,
			mysqlConnection mySql) throws SQLException {
		int returnTOserver = 0, sum = 0;
		String maxCover = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		ResultSet rss3 = stmt.executeQuery(query22);
		while (rss3.next()) {
			maxCover = rss3.getString("MaxCover");
		}
		rss3.close();

		String query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[0] + "' AND '" + days[1]
				+ "' AND Vistingtime = '" + time + "';";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			if (rs.getString("Status").equals("Done"))
				sum = sum + Integer.parseInt(rs.getString("Numberofvistors"));
		}
		if (sum == Integer.parseInt(maxCover))
			returnTOserver = 1;
		else
			returnTOserver = 0;
		rs.close();
		return returnTOserver;
	}

	/**
	 * A method that searches for an order from the waiting list and passes it to an
	 * order list with status "Waiting1"
	 * 
	 * @param query1        is a query that returns the order details in the waiting
	 *                      list
	 * @param numofvisitors the number of visitors.
	 * @param mySql         a single instance of the DB connection.
	 * @param str           is the status "Waiting1"
	 * @return The number of people who can add.
	 * @throws SQLException is Exception of sql.
	 */
	public static int SearchInWaitingList(String query1, int numofvisitors, mysqlConnection mySql, String str)
			throws SQLException {

		String query2 = null, query3 = null, query4 = null, id = null, email = null, phonenumber = null, park = null,
				query5 = null, time = null, date = null, numofpeople = null;
		ResultSet rs = null;
		ArrayList<String> array = new ArrayList<>();
		int per = 0;
		LocalTime clock = LocalTime.now();
		String[] stringarr = clock.toString().split(":");
		String ClockNow = stringarr[0] + ":" + stringarr[1];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		rs = stmt.executeQuery(query1);

		if (rs.next()) {
			park = rs.getString("Park");
			date = rs.getString("DateOfVisit");
			time = rs.getString("VisitingTime");
			numofpeople = rs.getString("NumberOfVisitors");
			// if (Integer.parseInt(rs.getString("NumberOfVisitors")) > numofvisitors) {
			// return 0;
			array = checkforwaitinglist(park, date, time, numofpeople, mySql);
			System.out.println(array.get(1));
			if (array.get(1).equals("no"))
				return numofvisitors;
			else {
				id = rs.getString("Idnumber");
				email = rs.getString("Email");
				per = numofvisitors - Integer.parseInt(rs.getString("NumberOfVisitors"));
				query2 = "INSERT INTO " + rs.getString("Park")
						+ " (Dateofvisit, Vistingtime, Numberofvistors, Idnumber, Email,Typeoforder, Status, ordernumber, TotalPrice, IsPaid,Hour1) VALUES ('"
						+ rs.getString("DateOfVisit") + "','" + rs.getString("VisitingTime") + "','"
						+ rs.getString("NumberOfVisitors") + "','" + rs.getString("Idnumber") + "','"
						+ rs.getString("Email") + "','" + rs.getString("OrderType") + "','" + str + "','"
						+ (++EchoServer.ordernumber) + "','" + rs.getString("TotalPrice") + "', 'false','" + ClockNow
						+ "');";
				query3 = "DELETE FROM waitinglist WHERE (Park = '" + rs.getString("Park") + "') AND (priority = '"
						+ rs.getInt("priority") + "');";

				query4 = "SELECT * FROM visitor WHERE Idnumber='" + id + "';";

				query5 = "SELECT * FROM " + park + " WHERE ordernumber='" + EchoServer.ordernumber + "';";
			}
		}
		rs.close();

		if (query3 != null && query2 != null && query4 != null)

		{
			rs = stmt.executeQuery(query4);
			if (rs.next()) {

				phonenumber = rs.getString("Telephone");
			}
			rs.close();
			int d = stmt.executeUpdate(query2);
			if (d > 0) {
				int s = stmt.executeUpdate(query3);
				if (s > 0) {
					String message = "The message:\r\n"
							+ "\"A place will be available for you on the date you requested. You are asked to log in and confirm the order within an hour from now. Otherwise your place will move next in line on the list.\r\n"
							+ "Sent to phone number: " + phonenumber + " and email address: " + email;
					JOptionPane.showMessageDialog(null, message);

				}
			}
		}

		return per;
	}

	/**
	 * A method that updates an order
	 * 
	 * @param query the update or delete
	 * @param mySql a single instance of the DB connection.
	 * @throws SQLException is Exception of sql.
	 */
	public static void delete1(String query, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int s = stmt.executeUpdate(query);
		if (s > 0) {

		}

	}

	/**
	 * A method that returns the priority numbers
	 * 
	 * @param query A query that returns priorities
	 * @param mySql a single instance of the DB connection.
	 * @return Sorted array of priorities
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] returnThePriority(String query, mysqlConnection mySql) throws SQLException {
		int[] priority = null;
		ArrayList<Integer> returnTOserver = new ArrayList<>();
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {

			returnTOserver.add(rs.getInt("priority"));
		}
		rs.close();

		if (returnTOserver.size() > 0) {
			priority = new int[returnTOserver.size()];
			for (int i = 0; i < returnTOserver.size(); i++) {
				priority[i] = returnTOserver.get(i);
			}
			for (int i = 0; i < priority.length; i++) {
			}
			Arrays.sort(priority);
			return priority;
		} else {
			priority = new int[1];
			priority[0] = 0;
			return priority;
		}
	}

	/**
	 * Method for adding a discount
	 * 
	 * @param query     is a query that introduces a new discount
	 * @param whichCase a string indicating the client's request.
	 * @param mySql     a single instance of the DB connection.
	 * @return "Succeeded" if Succeeded and "NotSucceeded" if not.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> AddDiscount(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		returnTOserver.add(whichCase);
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0)
			returnTOserver.add("Succeeded");
		else
			returnTOserver.add("NotSucceeded");
		return returnTOserver;
	}

	/**
	 * 
	 * @param query     is a query that returns the assumption
	 * @param whichCase a string indicating the client's request.
	 * @param mySql     a single instance of the DB connection.
	 * @return the discount details.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> DiscountsRequireApproval(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		returnTOserver.add(whichCase);
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			str.append(rs.getString("Park"));
			str.append("#");
			str.append(rs.getString("TypeOfDiscount"));
			str.append("#");
			str.append(rs.getString("StartDate"));
			str.append("#");
			str.append(rs.getString("EndDate"));
			str.append("#");
			str.append(rs.getString("DiscountValue"));
			returnTOserver.add(str.toString());
			str.delete(0, str.length());
		}
		return returnTOserver;
	}

	/**
	 * A method that returns the discount details
	 * 
	 * @param query     is a query that returns the discount details
	 * @param whichCase a string indicating the client's request.
	 * @param mySql     a single instance of the DB connection.
	 * @return the discount details.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> DiscountsDetails(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		returnTOserver.add(whichCase);
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		rs = stmt.executeQuery(query);
		if (rs.next()) {
			returnTOserver.add(rs.getString("Park"));
			returnTOserver.add(rs.getString("IsApproval"));
			returnTOserver.add(rs.getString("TypeOfDiscount"));
			returnTOserver.add(rs.getString("StartDate"));
			returnTOserver.add(rs.getString("EndDate"));
			returnTOserver.add(rs.getString("DiscountValue"));
			returnTOserver.add(rs.getString("DiscountNum"));
		}
		rs.close();
		if (returnTOserver.size() == 1) {
			returnTOserver.add("ThereIsNoOrders");
		}
		return returnTOserver;
	}

	/**
	 * A method that updates or deletes the assumption
	 * 
	 * @param query     is Delete query or discount delicacy
	 * @param whichCase a string indicating the client's request.
	 * @param mySql     a single instance of the DB connection.
	 * @return return yes or no.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> UpdateOrDeleteDiscount(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {

		ArrayList<String> returnTOserver = new ArrayList<>();
		returnTOserver.add(whichCase);
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		int k = stmt.executeUpdate(query);

		if (k > 0)
			returnTOserver.add("yes");
		else
			returnTOserver.add("no");
		return returnTOserver;
	}

	/**
	 * A method that returns the assumption
	 * 
	 * @param query     is a query that returns the assumption
	 * @param whichCase a string indicating the client's request.
	 * @param mySql     a single instance of the DB connection.
	 * @return return the discount.
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> returnTheDiscount(String query, String whichCase, mysqlConnection mySql)
			throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		returnTOserver.add(whichCase);
		ResultSet rs = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take

		rs = stmt.executeQuery(query);
		while (rs.next()) {
			returnTOserver.add(rs.getString("DiscountValue"));
		}
		rs.close();
		if (returnTOserver.size() == 1) {
			returnTOserver.add("ThereIsNoDiscount");
		}
		return returnTOserver;
	}

	/**
	 * A method that returns the number ID
	 * 
	 * @param query  is a query that returns the ID number by searching by ID number
	 * @param query1 is a query that returns the ID number by searching by
	 *               subscription number
	 * @param mySql  a single instance of the DB connection.
	 * @return the id
	 * @throws SQLException is Exception of sql.
	 */
	public static String returnTheID(String query, String query1, mysqlConnection mySql) throws SQLException {
		String ID = null;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be taken.
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next())
			ID = rs.getString("Idnumber");
		rs.close();
		if (ID == null) {
			rs = stmt.executeQuery(query1);

			while (rs.next()) {
				ID = rs.getString("Idnumber");

			}
			rs.close();
			if (ID == null) {
				ID = "NotExisting";
			}
		}

		return ID;

	}

	/**
	 * Method More Occupancy Report
	 * 
	 * @param park  the park
	 * @param date  the date
	 * @param times is array of string of the activity time
	 * @param mySql a single instance of the DB connection
	 * @return 1 If the park is fully occupied on this day or 0 if not.
	 * @throws SQLException is Exception of sql.
	 */
	public static String OccupancyReport(String park, String date, String[] times, mysqlConnection mySql)
			throws SQLException {
		int[] parameters = CheckingParameters(park, mySql);
		int allDay = 0;
		int count11 = 0, count211 = 0;
		String[] Btime11, res = null;
		for (int i = 0; i < times.length; i++) {
			Btime11 = EchoServer.BringRelevantTimes(times[i], parameters[1]);
			int lenth1 = Btime11.length + 1 - parameters[1];
			int intreval = lenth1;
			int j = 0;
			while (--lenth1 >= 0) {
				res = Arrays.copyOfRange(Btime11, j, parameters[1] + j);
				j++;
				for (int t = 0; t < res.length; t++) {
					count11 += MySqlQuery.CheckVacancy1(park, date, res[t], mySql);
				}

				if (count11 < parameters[0] - parameters[2]) {
					count211++;
				}
				count11 = 0;
			}
			if (count211 != intreval) {
				allDay++;
			}
			count211 = 0;

		}

		return allDay == 13 ? "1" : "0";
	}

	/**
	 * 
	 * @param query A query that returns the desired report
	 * @param mySql a single instance of the DB connection
	 * @return the type
	 * @throws SQLException is Exception of sql.
	 */
	public static String returnTypereport(String query, mysqlConnection mySql) throws SQLException {
		String para = null;

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {

			para = rs.getString(1);

		}
		rs.close();
		return para;

	}

	/**
	 * Checks if the report if the rs we extracted it from the database is different
	 * from null I mean there is such a report return one and zero another
	 * 
	 * @param query query A query that returns the desired report
	 * @param mySql a single instance of the DB connection
	 * @return 1 or 0
	 * @throws SQLException is Exception of sql.
	 */
	public static int CheckTypereport(String query, mysqlConnection mySql) throws SQLException {

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {

			return 1;
		}
		rs.close();
		return 0;

	}

	/**
	 * Saves information about a particular report in the database
	 * 
	 * @param query query query A query that returns the desired report
	 * @param mySql a single instance of the DB connection
	 * @return return 1 or 0
	 * @throws SQLException is Exception of sql.
	 */
	public static int UpDatereportDatailsParameter(String query, mysqlConnection mySql) throws SQLException {

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		int k = stmt.executeUpdate(query);
		if (k > 0) {
			return 1;
		}
		return 0;

	}

	/**
	 * Returns monthly cancellation report information
	 * 
	 * @param query is a query that returns orders from the table
	 * @param s     is case
	 * @param mySql a single instance of the DB connection
	 * @return number of orders that canceled
	 * @throws SQLException is Exception of sql.
	 */
	public static int monthlyCancaltionReport(String query, String s, mysqlConnection mySql) throws SQLException {
		int returnTOserver = 0;

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		ResultSet rs = stmt.executeQuery(query);

		if (s.equals("Cancel")) {
			while (rs.next()) {
				if (rs.getString("Status").equals("Deleted2"))
					returnTOserver++;

			}
		} else {
			while (rs.next()) {
				if (rs.getString("Status").equals("Expired"))
					returnTOserver++;

			}
		}
		rs.close();

		return returnTOserver;
	}

	/**
	 * Returns weekly cancellation report information
	 * 
	 * @param park  is park
	 * @param s     is case
	 * @param days  is the days
	 * @param mySql a single instance of the DB connection
	 * @return number of order that canceled
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] WeeklyCancaltionReport(String park, String s, String[] days, mysqlConnection mySql)
			throws SQLException {
		int[] returnTOserver = new int[5];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query;
		ResultSet rs = null;
		int index = 0;
		for (int i = 0; i < 9; i = i + 2) {
			query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[i] + "' AND '" + days[i + 1] + "';";
			if (days[i] == null) {

				returnTOserver[index] = -1;

			} else {
				rs = stmt.executeQuery(query);
				if (s.equals("Cancel")) {
					while (rs.next()) {

						if (rs.getString("Status").equals("Deleted2"))
							returnTOserver[index]++;
					}
					rs.close();
				} else {
					while (rs.next()) {

						if (rs.getString("Status").equals("Expired"))
							returnTOserver[index]++;
					}
					rs.close();
				}
			}
			index++;
		}

		return returnTOserver;
	}

	/**
	 * Returns information about cancellation report in hourly view
	 * 
	 * @param park  is park
	 * @param s     is case
	 * @param days  is days
	 * @param time  is the time
	 * @param mySql a single instance of the DB connection
	 * @return number of order that canceled
	 * @throws SQLException is Exception of sql.
	 */
	public static int HourlyCancaltionReport(String park, String s, String[] days, String time, mysqlConnection mySql)
			throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Dateofvisit BETWEEN '" + days[0] + "' AND '" + days[1]
				+ "' AND Vistingtime = '" + time + "';";
		ResultSet rs = stmt.executeQuery(query);
		if (s.equals("Cancel")) {
			while (rs.next()) {
				if (rs.getString("Status").equals("Deleted2"))
					returnTOserver++;
			}
		} else {
			while (rs.next()) {
				if (rs.getString("Status").equals("Expired"))
					returnTOserver++;
			}
			rs.close();

		}
		rs.close();

		return returnTOserver;
	}

	/**
	 * Returns daily cancellation report information
	 * 
	 * @param park  is the park
	 * @param s     is case
	 * @param day   is the days
	 * @param mySql a single instance of the DB connection
	 * @return number of order that canceled
	 * @throws SQLException is Exception of sql.
	 */
	public static String dailyCancaltionReport(String park, String s, String day, mysqlConnection mySql)
			throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Dateofvisit ='" + day + "';";
		ResultSet rs = stmt.executeQuery(query);

		if (s.equals("Cancel")) {
			while (rs.next()) {
				if (rs.getString("Status").equals("Deleted2"))
					returnTOserver++;
			}
		} else {
			while (rs.next()) {
				if (rs.getString("Status").equals("Expired"))
					returnTOserver++;
			}
			rs.close();

		}
		rs.close();

		rs.close();
		return String.valueOf(returnTOserver);

	}

	/**
	 * Returns information about the number of visitors per month segmented by
	 * visitor types
	 * 
	 * @param query is a query that returns orders from the table in the status
	 *              "Finish"
	 * @param mySql a single instance of the DB connection
	 * @return the number of orders
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] monthlyReport(String query, mysqlConnection mySql) throws SQLException {
		int[] returnTOserver = new int[3];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		ResultSet rs = stmt.executeQuery(query);

		for (int i = 0; i < 3; i++)
			returnTOserver[i] = 0;

		while (rs.next()) {
			if (rs.getString("Typeoforder").equals("StandardOrder")) {
				returnTOserver[0] = returnTOserver[0] + Integer.parseInt(rs.getString("Numberofvistors"));
			}

			if (rs.getString("Typeoforder").equals("Instructions")) {
				returnTOserver[1] = returnTOserver[1] + Integer.parseInt(rs.getString("Numberofvistors"));
			}
			if (rs.getString("Typeoforder").equals("OrderForFamilySubscription")) {
				returnTOserver[2] = returnTOserver[2] + Integer.parseInt(rs.getString("Numberofvistors"));
			}
		}
		rs.close();

		return returnTOserver;
	}

	/**
	 * Returns information about the number of visitors per month segmented by
	 * visitor types by monthly view
	 * 
	 * @param park  is the park
	 * @param days  days is the days
	 * @param which is which case
	 * @param mySql a single instance of the DB connection
	 * @return the number of order
	 * @throws SQLException is Exception of sql.
	 */
	public static int[] WeeklyReport(String park, String[] days, String which, mysqlConnection mySql)
			throws SQLException {
		int[] returnTOserver = new int[5];
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query;
		ResultSet rs = null;
		int index = 0;
		for (int i = 0; i < 9; i = i + 2) {
			query = "SELECT * FROM " + park + " WHERE Status='Finish' AND Dateofvisit BETWEEN '" + days[i] + "' AND '"
					+ days[i + 1] + "';";
			if (days[i] == null) {
				returnTOserver[i] = -1;

			} else {
				rs = stmt.executeQuery(query);
				while (rs.next()) {

					if (which.equals("StandardOrder") && rs.getString("Typeoforder").equals("StandardOrder")) {
						returnTOserver[index] = returnTOserver[index]
								+ Integer.parseInt(rs.getString("Numberofvistors"));
					}

					if (which.equals("SmallGroupOrInstructions")
							&& rs.getString("Typeoforder").equals("SmallGroupOrInstructions")) {
						returnTOserver[index] = returnTOserver[index]
								+ Integer.parseInt(rs.getString("Numberofvistors"));
					}
					if (which.equals("FamilySubscription")
							&& rs.getString("Typeoforder").equals("FamilySubscription")) {
						returnTOserver[index] = returnTOserver[index]
								+ Integer.parseInt(rs.getString("Numberofvistors"));
					}
				}
			}
			rs.close();
			index++;
		}

		return returnTOserver;
	}

	/**
	 * Returns information about the number of visitors per month segmented by
	 * visitor types by Hourly view
	 * 
	 * @param park  is the park
	 * @param days  is the days
	 * @param time  is the time
	 * @param which is the case
	 * @param mySql a single instance of the DB connection
	 * @return the number of orders
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static int HourlyReport(String park, String[] days, String time, String which, mysqlConnection mySql)
			throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Status='Finish' AND Dateofvisit BETWEEN '" + days[0]
				+ "' AND '" + days[1] + "' AND Vistingtime = '" + time + "';";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {

			if (which.equals("StandardOrder") && rs.getString("Typeoforder").equals("StandardOrder")) {
				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}

			if (which.equals("SmallGroupOrInstructions") && rs.getString("Typeoforder").equals("Instructions")) {
				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}
			if (which.equals("FamilySubscription")
					&& rs.getString("Typeoforder").equals("OrderForFamilySubscription")) {
				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}
		}

		rs.close();
		return returnTOserver;
	}

	/**
	 * Returns information about the number of visitors per month segmented by
	 * visitor types by daily view
	 * 
	 * @param park  is the park
	 * @param day   is the days
	 * @param which is the case
	 * @param mySql a single instance of the DB connection
	 * @return the number of orders
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static String dailyReport(String park, String day, String which, mysqlConnection mySql) throws SQLException {
		int returnTOserver = 0;
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "SELECT * FROM " + park + " WHERE Status='Finish' AND Dateofvisit ='" + day + "';";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {

			if (which.equals("StandardOrder") && rs.getString("Typeoforder").equals("StandardOrder")) {

				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}

			if (which.equals("SmallGroupOrInstructions") && rs.getString("Typeoforder").equals("Instructions")) {
				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}
			if (which.equals("FamilySubscription")
					&& rs.getString("Typeoforder").equals("OrderForFamilySubscription")) {
				returnTOserver += Integer.parseInt(rs.getString("Numberofvistors"));
			}
		}

		rs.close();
		return String.valueOf(returnTOserver);
	}

	/**
	 * Returns the visit report of the department manager according to park entry
	 * times
	 * 
	 * @param park      is the park
	 * @param day       is the days
	 * @param EnterTime is when the visitor entered
	 * @param ExitTime  is when the visitor left
	 * @param mySql     a single instance of the DB connection
	 * @return returnTOserver with the number of order
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static int[] EnteringReport(String park, String day, String EnterTime, String ExitTime,
			mysqlConnection mySql) throws SQLException {
		int[] returnTOserver = { 0, 0, 0 };
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.

		String query = "SELECT * FROM " + park + " WHERE Dateofvisit = '" + day + "' AND EnteringTime BETWEEN '"
				+ EnterTime + "' AND '" + ExitTime + "';";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			if (rs.getString("Typeoforder").equals("StandardOrder")) {
				returnTOserver[0] += Integer.parseInt(rs.getString("Numberofvistors"));
			}

			if (rs.getString("Typeoforder").equals("Instructions")) {
				returnTOserver[1] += Integer.parseInt(rs.getString("Numberofvistors"));
			}
			if (rs.getString("Typeoforder").equals("OrderForFamilySubscription")) {
				returnTOserver[2] += Integer.parseInt(rs.getString("Numberofvistors"));
			}
		}
		rs.close();
		return returnTOserver;
	}

	/**
	 * Returns the visit report of the department manager according to park Stay
	 * times
	 * 
	 * @param park  is the park
	 * @param date  date is date
	 * @param range is the range of time
	 * @param mySql a single instance of the DB connection
	 * @return Percentages of Visitors by Type
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static float[] visitotsByStayTimeToDay(String park, String date, String range, mysqlConnection mySql)
			throws SQLException {

		ResultSet rs = null;
		int[] count = { 0, 0, 0, 0 };
		float[] res = { 0, 0, 0 };
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be take
		String query = "SELECT * FROM " + park + " WHERE  Dateofvisit = '" + date + "' AND Status ='" + "Finish" + "';";
		rs = stmt.executeQuery(query);
		while (rs.next()) {

			if (returntheStayTime(rs.getString("ExitingTime"), rs.getString("EnteringTime")) <= Float.parseFloat(range)
					* 60) {
				if (rs.getString("Typeoforder").equals("StandardOrder")) {
					count[0] += Integer.parseInt(rs.getString("Numberofvistors"));
				}
				if (rs.getString("Typeoforder").equals("Instructions")) {
					count[1] += Integer.parseInt(rs.getString("Numberofvistors"));
				}
				if (rs.getString("Typeoforder").equals("OrderForFamilySubscription")) {
					count[2] += Integer.parseInt(rs.getString("Numberofvistors"));
				}

			}
		}
		rs.close();
		count[3] = count[0] + count[1] + count[2];
		if (count[3] != 0) {
			res[0] = count[0] * 100 / count[3];
			res[1] = count[1] * 100 / count[3];
			res[2] = count[2] * 100 / count[3];
		}

		return res;
	}

	/**
	 * return stay time between the exit and enter
	 * 
	 * @param exit  is the time that the visitor left
	 * @param enter is the time that the visitor entered
	 * @return the time that the visitor was in the park
	 */
	public static float returntheStayTime(String exit, String enter) {
		String[] ex = exit.split(":");
		String[] en = enter.split(":");
		int k = (60 * Integer.parseInt(ex[0]) + Integer.parseInt(ex[1]))
				- (60 * Integer.parseInt(en[0]) + Integer.parseInt(en[1]));
		return k;

	}

	/**
	 * Check if the user is employee in one park
	 * 
	 * @param query is a query that returns the employee's information
	 * @param mySql a single instance of the DB connection
	 * @return Employee details
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static ArrayList<String> indeftifictionAsEmployee(String query, mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String user = null;
		String connect = null;
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			user = rs.getString("UserName");
			connect = rs.getString("IsConnected");

			returnTOserver.add(rs.getString("FirstName"));
			returnTOserver.add(rs.getString("LastName"));
			returnTOserver.add(rs.getString("UserName"));
			returnTOserver.add(rs.getString("Email"));
			returnTOserver.add(rs.getString("Role"));
			returnTOserver.add(rs.getString("OrganizationalAffiliation"));
			returnTOserver.add(rs.getString("password"));

		}

		if (returnTOserver.size() == 1) {
			returnTOserver.clear();
			returnTOserver.add("NotExisting");
		}
		rs.close();

		if (connect.equals("1")) {
			returnTOserver.clear();
			returnTOserver.add("theEmployeeConnectAlready");
			return returnTOserver;
		}
		if (connect.equals("0")) {
			boolean e = updateInConnectE(user, "1", mySql);
		}
		return returnTOserver;

	}

	/**
	 * update the visitor status conennect in Db
	 * 
	 * @param id    is the id number
	 * @param state is the state of connect
	 * @param mySql a single instance of the DB connection
	 * @return true or false
	 * @throws SQLException SQLException is Exception of sql.
	 */
	public static boolean updateInConnectV(String id, String state, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "UPDATE visitor SET IsConnected = '" + state + "' WHERE Idnumber='" + id + "';";
		int t = stmt.executeUpdate(query);
		if (t > 0) {
			return true;
		}
		return false;
	}

	/**
	 * update the employee status conennect in Db
	 * 
	 * @param username is the username
	 * @param state    is the state
	 * @param mySql    a single instance of the DB connection
	 * @return true or false
	 * @throws SQLException is Exception of sql.
	 */
	public static boolean updateInConnectE(String username, String state, mysqlConnection mySql) throws SQLException {
		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} // Check which action needs to be taken.
		String query = "UPDATE employees SET IsConnected = '" + state + "' WHERE UserName ='" + username + "';";
		int t = stmt.executeUpdate(query);
		if (t > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies the user without changing their login status Online Offline or
	 * vice versa
	 * 
	 * @param query     is a query that returns visitor information by ID number
	 * @param query1    is a query that returns visitor information by subscription
	 *                  number
	 * @param whichCase is the string of the request from client
	 * @param mySql     a single instance of the DB connection
	 * @return Visitor details
	 * @throws SQLException is Exception of sql.
	 */
	public static ArrayList<String> justindeftifictionInDB(String query, String query1, String whichCase,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();

		try {
			stmt = mySql.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Check which action needs to be taken.
		returnTOserver.add(whichCase);
		ResultSet rs = stmt.executeQuery(query);
		String id = null;
		String connect = null;
		while (rs.next()) {
			returnTOserver.add("Existing");
			returnTOserver.add(rs.getString("Firstname"));
			returnTOserver.add(rs.getString("Lastname"));
			returnTOserver.add(rs.getString("Idnumber"));
			returnTOserver.add(rs.getString("Email"));
			returnTOserver.add(rs.getString("Telephone"));
			returnTOserver.add(rs.getString("Type"));
			if (rs.getString("Type").equals("2")) {
				returnTOserver.add(rs.getString("FamilyMembersAmount"));
				returnTOserver.add(rs.getString("CreditCardNumber"));
				returnTOserver.add(rs.getString("CardValidity"));
				returnTOserver.add(rs.getString("3Digits"));
				returnTOserver.add(rs.getString("SubscriptionNum"));

			}
		}
		rs.close();
		if (returnTOserver.size() == 1) {
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				returnTOserver.add("Existing");
				returnTOserver.add(rs.getString("Firstname"));
				returnTOserver.add(rs.getString("Lastname"));
				returnTOserver.add(rs.getString("Idnumber"));
				returnTOserver.add(rs.getString("Email"));
				returnTOserver.add(rs.getString("Telephone"));
				returnTOserver.add(rs.getString("Type"));
				returnTOserver.add(rs.getString("FamilyMembersAmount"));
				returnTOserver.add(rs.getString("CreditCardNumber"));
				returnTOserver.add(rs.getString("CardValidity"));
				returnTOserver.add(rs.getString("3Digits"));
				returnTOserver.add(rs.getString("SubscriptionNum"));

			}
		}
		rs.close();
		if (returnTOserver.size() == 1) {
			returnTOserver.clear();
			returnTOserver.add(whichCase);
			returnTOserver.add("NotExisting");
			return returnTOserver;

		}
		return returnTOserver;
	}

	public static ArrayList<String> justindeftifictionInDB(String park, String date, String time, String numofpeople,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		int Capacity = 0, temp1 = 0, j = 0, TotalCapacity = 0;
		// TotalCapacity = Integer.parseInt(s[4]);
		TotalCapacity = 0;
		int[] parameters = MySqlQuery.CheckingParameters(park, mySql);
		String[] time1 = EchoServer.BringRelevantTimes(time, parameters[1]), res;
		int lenth = time1.length + 1 - parameters[1];
		int k = lenth;
		for (int i1 = 0; i1 < time1.length; i1++) {
			TotalCapacity += MySqlQuery.CheckVacancy(park, date, time1[i1], mySql);
		}
		while (--lenth >= 0) {
			res = Arrays.copyOfRange(time1, j, parameters[1] + j);
			for (int i = 0; i < res.length; i++) {
				Capacity += MySqlQuery.CheckVacancy(park, date, res[i], mySql);
			}

			Capacity += Integer.parseInt(numofpeople);
			if (Capacity <= parameters[0] - parameters[2]) {
				temp1++;
			}
			j++;
			Capacity = 0;
		}

		if (temp1 == k) {
			returnTOserver.add("CheckVacancyDB");
			returnTOserver.add("yes");
			returnTOserver.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

		} else {
			returnTOserver.add("CheckVacancyDB");
			returnTOserver.add("no");
			returnTOserver.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

		}
		return returnTOserver;
	}

	public static ArrayList<String> checkforwaitinglist(String park, String date, String time, String numofpeople,
			mysqlConnection mySql) throws SQLException {
		ArrayList<String> returnTOserver = new ArrayList<>();
		int Capacity = 0, temp1 = 0, j = 0, TotalCapacity = 0;
		// TotalCapacity = Integer.parseInt(s[4]);
		TotalCapacity = 0;
		int[] parameters = MySqlQuery.CheckingParameters(park, mySql);
		String[] time1 = EchoServer.BringRelevantTimes(time, parameters[1]), res;
		int lenth = time1.length + 1 - parameters[1];
		int k = lenth;
		for (int i1 = 0; i1 < time1.length; i1++) {
			TotalCapacity += MySqlQuery.CheckVacancy(park, date, time1[i1], mySql);
		}
		while (--lenth >= 0) {
			res = Arrays.copyOfRange(time1, j, parameters[1] + j);
			for (int i = 0; i < res.length; i++) {
				Capacity += MySqlQuery.CheckVacancy(park, date, res[i], mySql);
			}

			Capacity += Integer.parseInt(numofpeople);
			if (Capacity <= parameters[0] - parameters[2]) {
				temp1++;
			}
			j++;
			Capacity = 0;
		}

		if (temp1 == k) {
			returnTOserver.add("CheckVacancyDB");
			returnTOserver.add("yes");
			returnTOserver.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

		} else {
			returnTOserver.add("CheckVacancyDB");
			returnTOserver.add("no");
			returnTOserver.add(String.valueOf((parameters[0] - parameters[2] - TotalCapacity)));

		}
		return returnTOserver;
	}
}
