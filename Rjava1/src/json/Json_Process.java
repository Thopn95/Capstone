package json;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;

import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
public class Json_Process {
	public static void test2() throws IOException, SQLException{
		URL url = new URL("http://environment.data.gov.uk/flood-monitoring/data/readings?latest");
		try (InputStream is = url.openStream();
	      JsonReader rdr = Json.createReader(is)){
		      JsonObject obj = rdr.readObject();
		      System.out.println(obj.toString());
		      JsonArray results = obj.getJsonArray("items");
		      System.out.println(results.size());
//		      for (JsonObject result : results.getValuesAs(JsonObject.class)) {
//		    	String hostName = "localhost";
//		 	    String sid = "RJAVAGROUP";
//		 	    String userName = "rjgroup";
//		 	    String password = "rjgroup123";
//		 		String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
//		 		Connection dbConn = DriverManager.getConnection(connectionURL, userName,password);
//		 		  // Turn auto commit off (turned on by default)
//		 		  dbConn.setAutoCommit(false);
//		 		  // Create insert statement
//		 		  PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO JSON VALUES (?)");
//		 		 Clob clob = dbConn.createClob();
//			      // Store my JSON into the CLOB
//			      clob.setString(1, result.toString());
//			      // Set clob instance as input parameter for INSERT statement
//			      stmt.setClob(1, clob);
//			      // Execute the INSERT statement
//			      int affectedRows = stmt.executeUpdate();
//			      // Free up resource
//			      clob.free();
//			      // Commit inserted row to the database
//			      dbConn.commit();
//			      System.out.println(affectedRows + " row(s) inserted.");
//	     }
		      for(int i=0;i<results.size();i++){
		    	  Iterator in = results.iterator();
		    	  while(in.hasNext()){
		    		  JsonObject innerObj = (JsonObject) in.next();
		    		  System.out.println("value at "+i+" : "+innerObj.get("value"));
		    	  }
		      }
	 }
		 System.out.println("Finished");
	}
	public static void main(String[] args) throws IOException, SQLException{
		// TODO Auto-generated method stub
		 test2();
	}

}
