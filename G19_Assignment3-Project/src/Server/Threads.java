package Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import Server.mysqlConnection;
import javax.swing.JOptionPane;
import logic.Order;
/**In this class, we use a thread that operates methods that work at any given time
 * 
 * @author liranhaim
 *
 */
public class Threads {
	private String es = null;
	String[] park = { "park1", "park2", "park3" };
	ArrayList<Integer> OrNum1 = new ArrayList<>();
	ArrayList<Integer> OrNum2 = new ArrayList<>();
	ArrayList<Integer> OrNum3 = new ArrayList<>();

//	private static ThreadOrderReminder instance = new ThreadOrderReminder();

	private static Timer timer;
	LocalDate nextDay = LocalDate.now().plusDays(1);

	mysqlConnection mySql = null;

	String query;
	ResultSet rs, rs1, rs2;
	Statement stmt;

	Threads(String es) {
		this.es = es;
	}
     /**
      * A thread runs every minute and operates a number of methods
      */
	public void start1() {
		try {
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					try {
						for (int j = 0; j < park.length; j++) {
							OrNum1 = reminderDayBefore(j);
							for (int i = 0; i < OrNum1.size(); i++) {
								UpdateStatusWaiting2(j, OrNum1.get(i));
							}
						}
						for (int j = 0; j < park.length; j++) {
							OrNum2 = ExpiredDate(j);
							for (int i = 0; i < OrNum2.size(); i++) {
								UpdateStatusDelete(j, OrNum2.get(i));
							}
						}
						for (int j = 0; j < park.length; j++) {
							OrNum3 = Witing1EX(j);
							for (int i = 0; i < OrNum3.size(); i++) {
								UpdateWiting1ToDelete1(j, OrNum3.get(i));
							}
						}
						for (int j = 0; j < park.length; j++) {
							OrNum3 = Witing2EX(j);
							for (int i = 0; i < OrNum3.size(); i++) {
								UpdateWiting2ToDelete2(j, OrNum3.get(i));
							}
						}
						waitingList();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			try {
				mySql = mysqlConnection.GetInstance();

			} catch (SQLException e1) {

				// TODO Auto-generated catch block
				e1.printStackTrace();

			}
			try {
				stmt = mySql.connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Check which action needs to be taken.
			timer = new Timer(true);
			timer.scheduleAtFixedRate(timerTask, 0, 60 * 1000);

		} catch (Exception e) {

		}
	}
   /**A method that seeks an order for another 24 hours and adds the order number to ArrayList
    * 
    * @param index the index of the park
    * @return ArrayList<Integer> of the ordernum.
    * @throws SQLException
    */
	private ArrayList<Integer> reminderDayBefore(int index) throws SQLException {
		ArrayList<Integer> ordernum = new ArrayList<>();
		
		String id = null, email = null, phonenumber = null;
		String[] park = { "park1", "park2", "park3" };
		LocalDate nextDay = LocalDate.now().plusDays(1);
		LocalTime time = LocalTime.now();
		String[] t = time.toString().split(":");
		String Time = t[0]+":00";
		
		String date = nextDay.toString();
		
		query = "SELECT * FROM " + park[index] + " WHERE Vistingtime='" +Time+"' AND Dateofvisit = '" + date + "';";
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			if (rs.getString("Status").equals("Active")) {

				ordernum.add(rs.getInt("ordernumber"));
				id = rs.getString("Idnumber");
				email = rs.getString("Email");
				
			}

		}
		rs.close();
		if (id != null && email != null) {
			String query4 = "SELECT * FROM visitor WHERE Idnumber='" + id + "';";
			rs = stmt.executeQuery(query4);
			
			if (rs.next()) {
				phonenumber = rs.getString("Telephone");
			}
			rs.close();
			if (phonenumber != null) {
				String message = "The message:\r\n"
						+ "Dear traveler! Remind you, you have booked a visit for one of the parks tomorrow.\r\n"
						+ "You are asked to confirm / cancel this visit within two hours through the system.\r\n" + ""
						+ " Otherwise, unfortunately the booking will be canceled automatically.\r\n"
						+ "Sent to phone number: " + phonenumber + " and email address: " + email;
				JOptionPane.showMessageDialog(null, message);
			}
		}

		return ordernum;
	}
	
    /**A method that looks for expiration orders that are on the waiting list and deletes them.
     * 
     * @throws SQLException
     */
	private void waitingList() throws SQLException {
		LocalDate d = LocalDate.now();
		LocalTime time = LocalTime.now().plusHours(2);
		String[] t = time.toString().split(":");
		String Date = d.toString();
		int i = 0;
		String Time = t[0] + ":" + t[1];
		query = "DELETE FROM waitinglist WHERE Dateofvisit = '" + Date + "' AND VisitingTime < '" + Time + "';";
		try {
			int k = stmt.executeUpdate(query);
			if (k > 0) {
				
			}
		} catch (Exception e) {

		}

	}
     /**A method that checks if an order has been placed after an hour and announces an increase as expired.
      * 
      * @param index the index of park
      * @return ArrayList<Integer> of ordernum.
      * @throws SQLException
      */
	private ArrayList<Integer> ExpiredDate(int index) throws SQLException {
		ArrayList<Integer> ordernum = new ArrayList<>();
		LocalDate d = LocalDate.now();
		LocalTime time = LocalTime.now().plusHours(1);
		String[] t = time.toString().split(":");
		String Date = d.toString();
		String Time = t[0] + ":" + t[1];
		String query = "SELECT * FROM " + park[index] + " WHERE Dateofvisit = '" + Date + "' AND Vistingtime < '" + Time
				+ "';";
		rs1 = stmt.executeQuery(query);
		while (rs1.next()) {
			if (rs1.getString("Status").equals("Approved")) {
				ordernum.add(rs1.getInt("ordernumber"));
			}

		}
		rs1.close();
		return ordernum;

	}
     /**A method that updates the status to expiration.
      * 
      * @param index the index of park
      * @param order is the order num
      * @throws SQLException
      */
	private void UpdateStatusDelete(int index, int order) throws SQLException {
		String query1 = "UPDATE  " + park[index] + " SET Status = '" + "Expired" + "' WHERE ordernumber  = '" + order
				+ "';";
		try {
			int k = stmt.executeUpdate(query1);

		} catch (Exception e) {

		}

	}
     /**A method that updates the status of an invitation to be waiting2
      * 
      * @param index the index of park
      * @param order thr order number
      * @throws SQLException
      */
	private void UpdateStatusWaiting2(int index, int order) throws SQLException {
		LocalTime time = LocalTime.now();
		String[] t = time.toString().split(":");
		String Time = t[0] + ":" + t[1];
		String query1 = "UPDATE  " + park[index] + " SET Status = '" + "Waiting2" + "',Hour2 = '" + Time
				+ "' WHERE ordernumber  = '" + order + "';";
		try {
			int k = stmt.executeUpdate(query1);

		} catch (Exception e) {

		}

	}
    /**A method looking for an order that was not approved after an hour since it left the waiting list
     * 
     * @param  index the index of park
     * @return the ordernumber
     * @throws SQLException
     */
	private ArrayList<Integer> Witing1EX(int index) throws SQLException {
		ArrayList<Integer> ordernum = new ArrayList<>();
		LocalTime time = LocalTime.now().minusHours(1);
		String[] t = time.toString().split(":");
		String Time = t[0] + ":" + t[1];
		String query = "SELECT * FROM " + park[index] + " WHERE Status = '" + "Waiting1" + "' AND Hour1 < '" + Time
				+ "';";
		rs1 = stmt.executeQuery(query);
		while (rs1.next()) {
			ordernum.add(rs1.getInt("ordernumber"));
		}
		rs1.close();
		return ordernum;

	}
     /**
      * A method that updates the status of an invitation to be Deleted1
      * @param index the index of park
      * @param order is the ordernumber
      * @throws SQLException
      */
	private void UpdateWiting1ToDelete1(int index, int order) throws SQLException {
		String query1 = "UPDATE  " + park[index] + " SET Status = '" + "Deleted1" + "' WHERE ordernumber  = '" + order
				+ "';";

		try {
			int k = stmt.executeUpdate(query1);
			if (k > 0) {
		

				String query = "SELECT * FROM " + park[index] + " WHERE ordernumber  = '" + order + "';";
				String[] arr = OrderInfo(query, park[index]);
				SearchInWaitingList1(arr, "Waiting1");

			}
	

		} catch (Exception e) {

		}
	}
    /**A method that retrieves order information in order to search the waiting list
     * 
     * @param query is a query that returns order details
     * @param park the park requested
     * @return  order details
     * @throws SQLException
     */
	private String[] OrderInfo(String query, String park) throws SQLException {

		rs = stmt.executeQuery(query);	
		String[] s1 = new String[4];
		if (rs.next()) {
			s1[0] = park;
			s1[1] = rs.getString("Dateofvisit");
			s1[2] = rs.getString("Vistingtime");
			s1[3] = rs.getString("Numberofvistors");
		

		}
		rs.close();
		return s1;
	}
   /**A method that looks for a relevant invitation from the waiting list
    * 
    * @param s is Order details.
    * @param str is the park
    * @throws SQLException
    */
	private void SearchInWaitingList1(String[] s, String str) throws SQLException {
		int currentC = Integer.parseInt(s[3]);
		int i = 0;
		
		String[] range = EchoServer.BringRelevantTimes(s[2], 4);
		
		int index = range.length - 1;
		String query2 = "SELECT * FROM waitinglist WHERE Park='" + s[0] + "' AND DateOfVisit='" + s[1]
				+ "' AND VisitingTime BETWEEN '" + range[0] + "' AND '" + range[index] + "';";
		

		int[] priority = MySqlQuery.returnThePriority(query2, mySql);
		
		if (priority[0] == 0) {
			return;
		} else {
			while (currentC > 0 || i < priority.length) {
				query = "SELECT * FROM waitinglist WHERE Park='" + s[0] + "' AND DateOfVisit='" + s[1]
						+ "' AND priority = '" + priority[i++] + "' ;";

				currentC = MySqlQuery.SearchInWaitingList(query, currentC, mySql, str);
				
			}
			return;
		}
	}
     /**A method that seeks an unproved order after two hours of receiving the reminder
      * 
      * @param index the index of park
      * @return the order number 
      * @throws SQLException
      */
	private ArrayList<Integer> Witing2EX(int index) throws SQLException {
		ArrayList<Integer> ordernum = new ArrayList<>();
		LocalTime time = LocalTime.now().minusHours(2);
		String[] t = time.toString().split(":");
		String Time = t[0] + ":" + t[1];
		String query = "SELECT * FROM " + park[index] + " WHERE Status = '" + "Waiting2" + "' AND Hour2 < '" + Time
				+ "';";
		rs1 = stmt.executeQuery(query);
		while (rs1.next()) {
			ordernum.add(rs1.getInt("ordernumber"));
		}
		rs1.close();

		return ordernum;
	}
   /**A method that refines the status to Deleted2
    * 
    * @param index the index of park
    * @param order is the order number 
    * @throws SQLException
    */
	private void UpdateWiting2ToDelete2(int index, int order) throws SQLException {
		String query1 = "UPDATE  " + park[index] + " SET Status = '" + "Deleted2" + "' WHERE ordernumber  = '" + order
				+ "';";
		try {
			int k = stmt.executeUpdate(query1);
			String query = "SELECT * FROM " + park[index] + " WHERE ordernumber  = '" + order + "';";
			String[] arr = OrderInfo(query, park[index]);
			SearchInWaitingList1(arr, "Waiting1");
		} catch (Exception e) {

		}
	}
}
