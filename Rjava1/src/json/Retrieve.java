package json;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Retrieve {
	/**
	 * This example shows how to retrieve JSON attributes from the Oracle Database
	 * @author gvenzl
	 **/


	  public static void main(String[] args) throws SQLException {
	    // Connect to the database (ojdbcN.jar needs to be in the classpath)
		  String hostName = "localhost";
		     String sid = "RJAVAGROUP";
		     String userName = "rjgroup";
		     String password = "rjgroup123";
			String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
			Connection dbConn = DriverManager.getConnection(connectionURL, userName,password);
	    // Create SELECT statement, retrieve first name and age
	    // Oracle allows me to use the Simple-Dot-Notation to
	    // directly extract attributes from the JSON
	    // In order to do so I have to specify an identifier for the table,
	    // in this case "j"
	    PreparedStatement stmt = dbConn.
	        prepareStatement("SELECT j.DOC.catchmentName,j.DOC.dateOpened FROM JSON j");
	    // Run the query
	    ResultSet result = stmt.executeQuery();

	    // loop over results and print the to the console
	    while (result.next()) {
	        System.out.println(result.getString(1) + ", " + result.getString(2));
	    }
	  }
	}
