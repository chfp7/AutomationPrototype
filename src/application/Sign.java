package application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Sign {
	
	public static String uEmail = "";

	DBMSConnection dbms = new DBMSConnection(DBMSConnection.getURL(),DBMSConnection.getUsername() , DBMSConnection.getPassword());

    String tableName = "userInfo";
    String[] columns = {"name", "email", "pWord", "age"};
    int userID;
    
    OptionsMenu oM = new OptionsMenu();
    
    boolean loggedIn = false;

    public Sign() {}

    public void register(BorderPane root) {
        VBox registerScreen = new VBox(10);
        TextField nField = new TextField();
        TextField eField = new TextField();
        PasswordField pField = new PasswordField();
        TextField ageField = new TextField();

        nField.setPromptText("Enter your name");
        eField.setPromptText("Enter your email");
        pField.setPromptText("Enter your password");
        ageField.setPromptText("Enter your age");

        registerScreen.setAlignment(Pos.CENTER);
        registerScreen.setPadding(new Insets(20));

        Label registerLabel = new Label("Sign Up Here!");
        registerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser(nField, eField, pField, ageField, errorLabel));

        registerScreen.getChildren().addAll(registerLabel, nField, eField, pField, ageField, registerButton, errorLabel);
        root.setCenter(registerScreen);
    }

    public void logIn(BorderPane root) {
        VBox loginScreen = new VBox(10);
        loginScreen.setAlignment(Pos.CENTER);
        loginScreen.setPadding(new Insets(20));

        Label loginLabel = new Label("Log In");
        loginLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Enter your password");
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);

        // Binding the text properties so both fields are always in sync
        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        Button toggleButton = new Button("👁️");

        // Toggle password visibility
        toggleButton.setOnAction(e -> makeVisible(passwordField,visiblePasswordField));

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e -> log(emailField, passwordField, visiblePasswordField, root, errorLabel));

        HBox passwordBox = new HBox(5, passwordField, visiblePasswordField, toggleButton);
        passwordBox.setAlignment(Pos.CENTER);

        loginScreen.getChildren().addAll(loginLabel, emailField, passwordBox, loginButton, errorLabel);
        root.setCenter(loginScreen);
        
//        loginButton.setOnAction(e -> {
//            String email = emailField.getText();
//            String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();
//
//            if (email.isEmpty() || password.isEmpty()) {
//                errorLabel.setText("Please enter both email and password.");
//                return;
//            }
//
//            boolean isValid = validateCredentials(email, password);
//
//            if (isValid) {
//                System.out.println("Log In Successful");  // Console message
//                errorLabel.setStyle("-fx-text-fill: green;"); // Change color to indicate success
//                errorLabel.setText("Log In Successful");     // Display success message
//
//                root.setCenter(new StackPane(new Label("Welcome! You are now logged in.")));
//            } else {
//                errorLabel.setStyle("-fx-text-fill: red;");   // Keep error messages red
//                errorLabel.setText("Invalid email or password. Please try again.");
//            }
//        });

    }


 // Method to validate user credentials against the database
    private boolean validateCredentials(String email, String password) {
        try {

            // Hash the password before comparison
          //  String hashedPassword = hashPassword(password); 

            // Secure query
            String query = "SELECT * FROM " + tableName + " WHERE email = ? AND pWord = ?";
            Object[] params = {email, password};

            // Check credentials
            return dbms.checkCredentials(query, params);
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
            return false;
        }
    }
    
 // Method to validate user credentials against the database
    private boolean duplicateCredentials(String email) {
        try {

            String query = "SELECT * FROM " + tableName + " WHERE email = ?";
            Object[] params = {email};

            // Check credentials
            return dbms.checkCredentials(query, params);
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

//    // Example method to hash passwords securely
//    private String hashPassword(String password) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
//        StringBuilder sb = new StringBuilder();
//        for (byte b : hashedBytes) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }


    void registerUser(TextField nField, TextField eField, PasswordField pField, TextField ageField, Label errorLabel) {
        if (nField.getText().isEmpty()) {
            errorLabel.setText("Please enter your name");
            return;
        }
        if (eField.getText().isEmpty() || !eField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorLabel.setText("Please enter a valid email");
            return;
        }
        if (pField.getText().isEmpty() || pField.getText().length() < 8) {
            errorLabel.setText("Password must be at least 8 characters long");
            return;
        }
        if (ageField.getText().isEmpty() || !ageField.getText().matches("\\d+")) {
            errorLabel.setText("Please enter a valid age");
            return;
        }
        if(duplicateCredentials(eField.getText())) {
        	errorLabel.setText("Email already in use");
        	return;
        }

        try {
            // Validate user input
            if (nField.getText().isEmpty() || ageField.getText().isEmpty() || 
                eField.getText().isEmpty() || pField.getText().isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            int age = Integer.parseInt(ageField.getText());
            if (age < 0) {
                errorLabel.setText("Age cannot be negative.");
                return;
            }

            // Create User object
            Person user = new User(nField.getText(), age, eField.getText(), pField.getText());

            // Ensure columns match your DB schema
            Object[] values = {user.getName(), user.getEmail(), user.getPassword(), user.getAge()};

            // Save user data
            dbms.saveData(tableName, columns, values);

            errorLabel.setText("Registration successful!");
        } catch (NumberFormatException nfe) {
            errorLabel.setText("Please enter a valid number for age.");
        } catch (Exception e) {
            errorLabel.setText("An unexpected error occurred.");
            e.printStackTrace();
        }

    }
    
    public void log(TextField emailField,PasswordField passwordField,TextField visiblePasswordField,BorderPane root, Label errorLabel) {
    	
            String email = emailField.getText();
            String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both email and password.");
                return;
            }

            boolean isValid = validateCredentials(email, password);

            if (isValid) {
            	uEmail = email;//we are using the static value to retrieve it to the project
               root.setCenter(new StackPane(new Label("Welcome! You are now logged in.")));
               oM.startOptionsMenu(root);
            } else {
                errorLabel.setText("Invalid email or password. Please try again.");
            }
        
    }
    
    public void makeVisible(PasswordField passwordField, TextField visiblePasswordField) {
    	{
            boolean isVisible = visiblePasswordField.isVisible();
            visiblePasswordField.setVisible(!isVisible);
            visiblePasswordField.setManaged(!isVisible);
            passwordField.setVisible(isVisible);
            passwordField.setManaged(isVisible);
        }
    }
}