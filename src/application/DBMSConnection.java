package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBMSConnection {

    private String url;      // Database URL
    private String username; // Database username
    private String password; // Database password
    
    // the ones to use
    private final static String urll = "jdbc:mysql://localhost:3306/demoBase";
    private final static String usernamee = "root";
    private final static String passwordd = "DBPasswordHere";
    
    // Constructor to initialize the database connection details
    public DBMSConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Method to establish a connection to the database
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // method to save data into the database using an SQL INSERT query
    public void saveData(String tableName, String[] columns, Object[] values) {
        // Validate input
        if (columns == null || values == null || columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values must be non-null and of the same length.");
        }

        // Build the SQL query dynamically
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName).append(" (");

        // Add column names to the query
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(") VALUES (");

        // Add placeholders for values
        for (int i = 0; i < values.length; i++) {
            queryBuilder.append("?");
            if (i < values.length - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(")");

        String query = queryBuilder.toString();

        // Execute the query
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set values for the placeholders
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkCredentials(String query, Object[] params) {
        try (Connection connection = getConnection(); 
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Customer> loadCustomers(String tableName) {//CHECK
        List<Customer> customers = new ArrayList<>();

        String query = "SELECT name, email, pn FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("pn");

                customers.add(new Customer(name, email, phone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    public Project loadProject() {
        Project project = null;
        String sql = "SELECT projID, mTypeA, mTypeB, numMachineA, numMachineB, sizeA, sizeB, issued, custEmail, userEmail " +
                     "FROM tempProjInfo ORDER BY projID DESC LIMIT 1";  // Order by projID in descending order

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("projID");  // Fetch auto-incremented projID
                String mTypeA = rs.getString("mTypeA");
                String mTypeB = rs.getString("mTypeB");
                int numMachineA = rs.getInt("numMachineA");
                int numMachineB = rs.getInt("numMachineB");
                double sizeA = rs.getDouble("sizeA");
                double sizeB = rs.getDouble("sizeB");
                LocalDateTime issued = rs.getTimestamp("issued").toLocalDateTime();
                String custEmail = rs.getString("custEmail");
                String userEmail = rs.getString("userEmail");

                project = new Project(mTypeA, mTypeB, numMachineA, numMachineB, sizeA, sizeB, issued, custEmail, userEmail);
                project.projID = id;  // Set projID from the database
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle properly in production
        }

        return project;
    }
    
//    public Project loadProject(String cusEmail) {
//        Project project = null;
//        String sql = "SELECT * FROM Projects WHERE custEmail = ?";  // Adjust table/column names as needed
//
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//           // pstmt.setInt(1, projID);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String mTypeA = rs.getString("mTypeA");
//                String mTypeB = rs.getString("mTypeB");
//                int numMachineA = rs.getInt("numMachineA");
//                int numMachineB = rs.getInt("numMachineB");
//                double sizeA = rs.getDouble("sizeA");
//                double sizeB = rs.getDouble("sizeB");
//                LocalDateTime issued = rs.getTimestamp("issued").toLocalDateTime();
//                String custEmail = rs.getString("custEmail");
//                String userEmail = rs.getString("userEmail");
//
//                project = new Project(mTypeA, mTypeB, numMachineA, numMachineB, sizeA, sizeB, issued, custEmail, userEmail);
//                project.setProjID(projID);  
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();  
//        }
//
//        return project;
//    }
    
    public void updateData(String tableName, String[] columns, Object[] values, String conditionColumn, Object conditionValue) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values array lengths must match.");
        }

        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE ").append(conditionColumn).append(" = ?"); // Dynamic condition

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]); // Set column values
            }
            stmt.setObject(values.length + 1, conditionValue); // Set WHERE condition

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String getURL() {
    	return urll;
    }
    
    public static String getUsername() {
    	return usernamee;
    }
    public static String getPassword() {
    	return passwordd;
    }
    

    // Example usage
    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/demoBase";
        String username = "root"; 
        String password = "DBPasswordHere";

        // Create an instance of DBMSConnection
        DBMSConnection dbmsConnection = new DBMSConnection(url, username, password);
        int  i = 2;
        // Define table name, columns, and values
        String tableName = "userInfo";
        String[] columns = {"userID","name" , "email","pWord", "age"};
        Object[] values = {i++,"John Doe" , "john.doe@example.com","AMAZING", 25};

        // Save data to the database
        dbmsConnection.saveData(tableName, columns, values);
        System.out.println(i);
    }
}