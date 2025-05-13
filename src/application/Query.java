package application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Query {
	
	 private DBMSConnection dbms;
	
	Query(){
		dbms = new DBMSConnection(DBMSConnection.getURL(),DBMSConnection.getUsername() , DBMSConnection.getPassword());
	}
	
	public void startQuery(BorderPane root, Customer selectedCustomer) {
	  

	    // Labels and Inputs
	    Label mTypeALabel = new Label("Machine Type A:");
	    TextField mTypeAInput = new TextField();

	    Label mTypeBLabel = new Label("Machine Type B:");
	    TextField mTypeBInput = new TextField();

	    Label numMachineALabel = new Label("Number of Machine A:");
	    TextField numMachineAInput = new TextField();

	    Label numMachineBLabel = new Label("Number of Machine B:");
	    TextField numMachineBInput = new TextField();

	    Label sizeALabel = new Label("Size A:");
	    TextField sizeAInput = new TextField();

	    Label sizeBLabel = new Label("Size B:");
	    TextField sizeBInput = new TextField();

	    Button submitButton = new Button("Submit");

	    // Save Project to DB on Button Click
	    submitButton.setOnAction(e -> saveProjectToDB(root,mTypeAInput,mTypeBInput,numMachineAInput,numMachineBInput,sizeAInput,sizeBInput,selectedCustomer));

	    // Layout
	    VBox layout = new VBox(10);
	    layout.setPadding(new Insets(10));
	    layout.getChildren().addAll(
	        mTypeALabel, mTypeAInput,
	        mTypeBLabel, mTypeBInput,
	        numMachineALabel, numMachineAInput,
	        numMachineBLabel, numMachineBInput,
	        sizeALabel, sizeAInput,
	        sizeBLabel, sizeBInput
	    );
	    
	    VBox sBox = new VBox();
	    sBox.getChildren().add(submitButton);
	    sBox.setAlignment(Pos.CENTER);
	    submitButton.setAlignment(Pos.TOP_CENTER);

	    root.setCenter(layout);
	    root.setBottom(sBox);
	}

	
	private List<String> loadCustomers() {
        return dbms.loadCustomers("custInfo").stream().map(Customer::getEmail).toList();
    }
	
	public Project proDuce(TextField mTypeAInput,TextField mTypeBInput,TextField numMachineAInput,TextField numMachineBInput,TextField sizeAInput,TextField sizeBInput,Customer selectedCustomer) {
		LocalDateTime time = LocalDateTime.now();
   	 // Creating Project Object with Inputs and Customer Data
	    Project project = new Project(
	        mTypeAInput.getText(),
	        mTypeBInput.getText(),
	        Integer.parseInt(numMachineAInput.getText()),
	        Integer.parseInt(numMachineBInput.getText()),
	        Double.parseDouble(sizeAInput.getText()),
	        Double.parseDouble(sizeBInput.getText()),
	        time,
	        selectedCustomer.getEmail(),
	        Sign.uEmail
	    );
	    
		return project;
	}
    
    
    
    private void saveProjectToDB(BorderPane root,TextField mTypeAInput,TextField mTypeBInput,TextField numMachineAInput,TextField numMachineBInput,TextField sizeAInput,TextField sizeBInput,Customer selectedCustomer) {
    	  if (selectedCustomer == null) {
  	        System.out.println("No customer selected!");
  	        return;
  	    }
    	  
          
	    Project project = proDuce(mTypeAInput,mTypeBInput,numMachineAInput,numMachineBInput,sizeAInput,sizeBInput,selectedCustomer);
	    
	    
        String tableName = "tempProjInfo";
        String[] columns = {"mTypeA", "mTypeB", "numMachineA", "numMachineB", "sizeA", "sizeB", "issued", "custEmail", "userEmail"};
        Object[] values = {
            project.getMTypeA(),
            project.getMTypeB(),
            project.getNumMachineA(),
            project.getNumMachineB(),
            project.getSizeA(),
            project.getSizeB(),
            project.getIssued(),
            project.getCustEmail(),
            project.getUserEmail()
        };			

        dbms.saveData(tableName, columns, values);
        lahlahlah(root,OptionsMenu.pricing,OptionsMenu.map,OptionsMenu.lines);
    }
    
    public void lahlahlah(BorderPane root,boolean pricing, boolean map, boolean lines) {
    	
    	if(lines == true) {
    		MWordDocX m = new MWordDocX();
    		m.CreateDoc();
    	}
    	if(map == true) {
    		Map m = new Map();
    		m.startMapEditor(root);
    		
    	}
    	if(pricing == true) {
    		ExcelAuto e = new ExcelAuto();
    		e.excelSheet();
    	}

    }

}
