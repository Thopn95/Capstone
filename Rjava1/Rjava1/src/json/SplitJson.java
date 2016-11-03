package json;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SplitJson {
	/**
	 * This example shows how to retrieve JSON attributes from the Oracle Database
	 * @author gvenzl
	 * @throws SQLException 
	 **/
	void getData() throws SQLException
	{
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
				prepareStatement("SELECT j.doc.catchmentName,j.doc.dateOpened FROM JSON j");
		// Run the query
		ResultSet result = stmt.executeQuery();

		// loop over results and print the to the console
		int i=1;
		while (result.next()) {	    	
			System.out.println(i++ +":  "+result.getString(1) + ", " + result.getString(2));
			if(i==600)
				break;
		}
	}
	Connection returnConnection() throws SQLException{
		String hostName = "localhost";
		String sid = "RJAVAGROUP";
		String userName = "rjgroup";
		String password = "rjgroup123";
		String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
		Connection dbConn = DriverManager.getConnection(connectionURL, userName,password);
		return dbConn;
	}
	public void prepStatExecuteCreateTable(String name){
		 String createTable= name;
		//String createTable= name;
		System.out.println(createTable);
		PreparedStatement ps=null;
		try{
			ps = returnConnection().prepareStatement(createTable);

		}catch(SQLException e){
		}

		try{
			
			ps.execute();
			returnConnection().commit();
			ps.close();
			//  closeConnection();
		}
		catch(SQLException e){
		}

	}


	public boolean ifExistTable(String tablename) throws SQLException{
		String sql = "Select TABLE_NAME from user_tables where table_name='"+tablename.toUpperCase()+"' ";  
		System.out.println(sql);
		ResultSet rs =null;
		Statement ps = null;
		try{
			ps = returnConnection().createStatement();
		}catch(SQLException e){
		}
		try{
			rs=ps.executeQuery(sql);
			while (rs.next()){
				System.out.println (rs.getString(1));   // Print col 1
				if(rs.getString(1).equals(tablename)){
					return true;
				}
			}
		}
		catch(SQLException e){
		}
		ps.close();
		return false;
	}
	public static void main(String[] args) throws SQLException {
		SplitJson sj = new SplitJson();
		String table_name="TREE";
	    System.out.println(sj.ifExistTable(table_name));
	    sj.prepStatExecuteCreateTable(table_name);
	}
}
