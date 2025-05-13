package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

public class CustScreen {
	
	 private ObservableList<Customer> customers = FXCollections.observableArrayList();
	 private TableView<Customer> customerTable;

	 String tableName = "custInfo";
	 String[] columns = {"name", "email", "pn"};
	
	 private DBMSConnection dbms;
	 
	 private Query query;

	 CustScreen() {
	     dbms = new DBMSConnection(DBMSConnection.getURL(),DBMSConnection.getUsername() , DBMSConnection.getPassword());
	 }
	
	public void handleCustScreen(BorderPane root){
		
		
		 // Create the table and columns
        customerTable = new TableView<>();
       // customerTable.setEditable(true);
        
        this.loadCustomersFromDatabase();
        
        Button editButton = new Button("Edit");// START 
        customerTable.setEditable(false); // Initially not editable

        editButton.setVisible(false); // Initially hidden

        // Show the edit button when a customer is selected
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editButton.setVisible(newSelection != null);
        });

        // Handle edit button click
        editButton.setOnAction(e -> {
            customerTable.setEditable(true);
            // You can add additional logic here if needed
        });// FINISH

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        handleCustomCol(nameCol, "name", 105);

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        handleCustomCol(emailCol, "email", 150);

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        handleCustomCol(phoneCol, "phone", 105);
        
        customerTable.getColumns().addAll(nameCol, emailCol, phoneCol);
        customerTable.setItems(customers);//HERE 
        //EDIT BTN

        editButton.setOnAction(e -> {
            customerTable.setEditable(true);
            // You can add additional logic here if needed
        });

        // Add new customer button
        Button addCustomerButton = new Button("Add New Customer");
        addCustomerButton.setOnAction(e -> showAddCustomerDialog(root));

        //continue button
        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-font-size: 18px; -fx-min-width: 150px; -fx-min-height: 40px;");

     // Handle continue button click
        continueButton.setOnAction(e -> handleContinue(root));
        
        nameCol.setOnEditCommit(event -> {
    	    Customer customer = event.getRowValue();
    	    customer.setName(event.getNewValue());
    	    updateCustomerInDatabase(customer);
    	});

    	emailCol.setOnEditCommit(event -> {
    	    Customer customer = event.getRowValue();
    	    customer.setEmail(event.getNewValue());
    	    updateCustomerInDatabase(customer);
    	});

    	phoneCol.setOnEditCommit(event -> {
    	    Customer customer = event.getRowValue();
    	    customer.setPhone(event.getNewValue());
    	    updateCustomerInDatabase(customer);
    	});
        
        // Add the continue button to the bottom of the BorderPane
        StackPane bottomPane = new StackPane(continueButton);
        bottomPane.setPadding(new Insets(20));
        continueButton.setAlignment(Pos.CENTER);
        root.setBottom(bottomPane);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(customerTable, addCustomerButton, continueButton,editButton);
        
       // customerTable.setMaxHeight(350);
        
        root.setCenter(layout);

	}
	
	

	private void updateCustomerInDatabase(Customer customer) {

	    String[] columns = {"name", "email", "pn"};
	    Object[] values = {customer.getName(), customer.getEmail(), customer.getPhone()};
	    String conditionColumn = "email";  // Stable identifier
	    Object conditionValue = customer.getEmail();  // Get PK of customer

	    dbms.updateData(tableName, columns, values, conditionColumn, conditionValue);
	}

	
	 private void showAddCustomerDialog(BorderPane root) {
		 
      //   customerTable.setMaxHeight(350);
	       
	        Button addBtn = new Button("Add");
	       // dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

	        TextField nameField = new TextField();
	        nameField.setPromptText("Name");
	        TextField emailField = new TextField();
	        emailField.setPromptText("Email");
	        TextField pnField = new TextField();
	        pnField.setPromptText("Phone");

	        VBox dialogContent = new VBox(10);
	        dialogContent.setPadding(new Insets(10));
	        dialogContent.getChildren().addAll(nameField, emailField, pnField,addBtn);
	        
	        addBtn.setOnAction(e -> handleAdd(root,nameField,emailField,pnField));
	        
	        root.setBottom(dialogContent);
//	       // dialog.getDialogPane().setContent(dialogContent);
//
//	        // Convert the result to a Customer object when the add button is clicked
//	        dialog.setResultConverter(dialogButton -> {
//	            if (dialogButton == addButtonType) {
//	                return new Customer(nameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()));
//	            }
//	            return null;
//	        });

//	        // Add the new customer to the list
//	        dialog.showAndWait().ifPresent(customer -> customers.add(customer));
	    }
	 
	 public Customer getSelectedCustomer() {
		    return customerTable.getSelectionModel().getSelectedItem();
		}

	 
	 private void handleAdd(BorderPane root, TextField nameField, TextField emailField, TextField pnField){
		 		 
      // Create User object
         Customer customer = new Customer(nameField.getText(),emailField.getText(), pnField.getText());

         // Ensure columns match your DB schema
         Object[] values = {customer.getName(), customer.getEmail(), customer.getPhone()};

         // Save customer data
         dbms.saveData(tableName, columns, values);
         
         customers.add(customer);
         		 
		// handleCustScreen(root);
		 
	 }
	 
	 private void handleCustomCol( TableColumn<Customer, String> col, String pVF, int width) {
		 col.setCellValueFactory(new PropertyValueFactory<>(pVF));
	        col.setCellFactory(TextFieldTableCell.forTableColumn());
	        col.setResizable(false);
	        col.setMinWidth(width);
	 }
	 
	 public void loadCustomersFromDatabasee() {
		    customers.clear(); // Clear the existing list
		    customers.addAll(dbms.loadCustomers(tableName)); // Assuming dbms.loadCustomers() returns a List<Customer>
		}
	 
	 public void loadCustomersFromDatabase() {
		    customers.setAll(dbms.loadCustomers(tableName)); // Replaces old list with new data
		}
	 
	 private void handleContinue(BorderPane root) {
	        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
	        if (selectedCustomer != null) {
	        	query = new Query();
	        	query.startQuery(root, selectedCustomer);
	            System.out.println("Selected Customer: " + selectedCustomer.getName());
	        } else {
	            System.out.println("No customer selected!");
	        }
	    }
}
