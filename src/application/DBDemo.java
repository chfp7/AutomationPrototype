package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBDemo {
	
	//DBPasswordHere
		String url = "jdbc:mysql://localhost:3306/demoBase";
		String username = "root";
		String password = "DBPasswordHere";
		
		
	DBDemo(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public void connect() {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			//creates the connection
			Connection connection = DriverManager.getConnection(url,username,password);
			
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM userInfo;");
			
			while(resultSet.next()) {
				System.out.println( resultSet.getInt(1)+ " " + resultSet.getString(2) + " " + resultSet.getString(3)+ " " + resultSet.getString(4) + " " + resultSet.getInt(5));
			}
			
			connection.close(); 
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}

}
