/**
 * Class: SQLAccess
 * @author Darrell Percey
 * This class is for connection and gathering information from the 
 * MySQL database. This will pull items and return them in an array
 * list of strings so that they will be displayed for the user
 * to select.
 */
package ffxiArmory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SQLAccess {
	
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	final private String host = "host name here"
	final private String user = "User name here"
	final private String password = "Password here"
	
	public SQLAccess() {
		
	}
	
	
	/* Function: getConnection()
	 * Opens the connection to the MySQL server when this
	 * method is called.
	 */
	private void getConnection() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://"+host, user, password);
			statement = connect.createStatement();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/* Function: updateNotice(String) / Return String
	 * Grabs the update string from the database that will open on
	 * launch to tell if there are any updates to the list of items
	 * in the database.
	 */
	public String updateNotice(String input) throws Exception{
		String temp;
		getConnection();
		
		resultSet = statement.executeQuery(input);
		resultSet.next();
		temp = resultSet.getString("up");
		
		return temp;
	}
	
	
	/* Function: updateList(String) / Return String
	 * Opens the connection to the MySQL server when this
	 * method is called.
	 */
	public String[] updateList(String input) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		
		getConnection();
		resultSet = statement.executeQuery(input);
		
		while(resultSet.next()) {
			list.add(resultSet.getString("name"));
		}
		
		String[] stringArray = new String[list.size()];
		for(int i = 0; i < stringArray.length; i++ ) {
			  stringArray[i] = list.get(i).toString();
			//  System.out.println(stringArray[i]);
		  }
		
		
		close();
		return stringArray;
	}
	
	
	/* Function: findLink(String) / Return String
	 * Attached the link to the item's website that is hosted on 
	 * BG-wiki (a source for FFXI information).
	 */
	public String[] findLink(String input) throws Exception {
		String[] temp = {"","",""};
		int augId = 0;
		String temp2 = "";
		getConnection();
		
		input = checkName(input);
		
		temp2 = "SELECT img, info, augId FROM weapons WHERE name='" + input + "'";
		resultSet = statement.executeQuery(temp2);
		resultSet.next();
		temp[0] = resultSet.getString("img");
		temp[1] = resultSet.getString("info");
		augId = resultSet.getInt("augId");
		temp2 = "SELECT augLink FROM augments WHERE id=" + augId;
		resultSet = statement.executeQuery(temp2);
		if(resultSet.next()) {
			temp[2] = resultSet.getString("augLink");
		}
		close();
		return temp;
	}
	
	
	/* Function: checkName(String) / Return String
	 * Checks for the a "'" in the name. Some zones and items
	 * have this in the name and most be fixed for the SQL
	 * Query to understand.
	 */
	private String checkName(String input) {
		String temp = "";
		int tempNum;
		
		tempNum = input.indexOf('\'');
		if(tempNum != -1) {
			temp = input.substring(0, tempNum) + "\\" + input.substring(tempNum, input.length()); 
		}
		else {
			temp = input;
		}
		return temp;
	}
	
	
	/* Function: close()
	 * Closes the connection to the MySQL server when this
	 * method is called.
	 */
	private void close(){
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} 
		catch (Exception e) {
			System.out.println("Failed to close connections.");
		}
	}
}
