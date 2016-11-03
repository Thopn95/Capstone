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
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import json.SplitJson;


public class Json_Process {
	public static void runJson() throws IOException, SQLException{
		URL url = new URL("http://environment.data.gov.uk/flood-monitoring/id/stations");
		try (InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is)) {
			JsonObject obj = rdr.readObject(); 
			JsonArray results = obj.getJsonArray("items");
			
			int i=0;
			JsonObject jsoMax = null;
			int max = 0;
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				Set<String> set = result.keySet();
				if(set.size() > max){
					jsoMax = result;
					max = set.size();
				}				
			}
			if(jsoMax != null){
				SplitJson a = new SplitJson();			
				if(!a.ifExistTable("TREE"))
				{
					Set<String> set = jsoMax.keySet();
					Object[] ss = set.toArray();
					String createTable="Create table TREE  (ID_ss NUMBER(*, 0)";
					for (Object object : ss) {
						if (!(object instanceof JsonArray))
						{
							createTable+= ",f_"+object.toString() +"  varchar(1024)";
						}
					}
					createTable+=")";		    		
					a.prepStatExecuteCreateTable(createTable.replace('@','i'));	

				}
				if(a.ifExistTable("TREE"))
				{
					for (JsonObject result : results.getValuesAs(JsonObject.class)) {	
						Set<String> set = result.keySet();
						Object[] ss = set.toArray();
						String insert ="INSERT INTO TREE";
						String column="(ID_ss";
						String values="('"+i++;
						for (Object object : ss) {
							if (!(result.get(object.toString()) instanceof JsonArray))
							{
							if(object!= null)
							{
								column +=",f_"+object.toString();

								values += "','"+ result.get(object).toString();
							}
							}							
						}

						column+=")";
						values+="')";
						insert += column.replace('@','i') + " VALUES " + values.replace('@','i');		  
						a.prepStatExecuteCreateTable(insert);
						System.out.println(i + " row(s) inserted.");
					}
				}
			}
		}		
		System.out.println("Finished");
	}
	public static void main(String[] args) throws IOException, SQLException {
		runJson();

	}

}
