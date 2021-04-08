//package Server;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//
//// TODO: Auto-generated Javadoc
///**
// * The Class mysqlConnection.
// */
//public class mysqlConnection {
//
//	/** The connection. */
//	public Connection connection = null;
//	
//	/** The data base. */
//	private static mysqlConnection dataBase;
//	
//	
//	
//
//	/**
//	 * Instantiates a new mysql connection.
//	 *
//	 * @throws SQLException the SQL exception
//	 */
//	public mysqlConnection() throws SQLException
//    {
//		try
//		{
//           Class.forName("com.mysql.jc.jdbc.Driver").newInstance();
//        } catch (Exception ex) {/* handle the error*/}
//
//        try
//          {
//        	System.out.println("starttoconect");
//        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/visitors?serverTimezone=IST","root","root");
//        
//        	
//
//            //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
//            System.out.println("SQL connection succeed");
//        
//     	    } catch (SQLException ex)
//     	    {/* handle any errors*/
//          
//            }
//          }
//
//	   /**
//   	 * Gets the connection.
//   	 *
//   	 * @return the mysql connection
//   	 * @throws SQLException the SQL exception
//   	 */
//   	public static mysqlConnection GetConnection() throws SQLException {
//			if (dataBase == null) {
//			    dataBase = new mysqlConnection();
//			}
//			return dataBase;
//		    }
//}
//
//

package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//
//import db.mysqlConnection;

// TODO: Auto-generated Javadoc
/**
 * The Class mysqlConnection.
 */
public class mysqlConnection {

	/** The connection. */
	public Connection connection = null;

	/** The data base. */
	// private static mysqlConnection dataBase;

	public static mysqlConnection instance;

	public static mysqlConnection GetInstance() throws SQLException {
		if (instance == null) {
			instance = new mysqlConnection();
		}
		return instance;
	}

	/**
	 * Gets the connection.
	 *
	 * @return the mysql connection
	 * @throws SQLException the SQL exception
	 */
	private mysqlConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jc.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}

		try {
			System.out.println("starttoconect");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/visitors?serverTimezone=IST", "root",
					"root");

			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */

		}

	}

	/**
	 * Instantiates a new mysql connection.
	 *
	 * @throws SQLException the SQL exception
	 */
}
