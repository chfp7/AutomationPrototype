package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class OptionsMenu {
	
	public static boolean lines = false;
	public static boolean map  = false;
	public static boolean pricing  = false;
		
	CustScreen cS = new CustScreen();
	
	OptionsMenu(){
		
	}
	
	
	
	public void startOptionsMenu(BorderPane root) {
		
		handleTopHalf(root); // title and settings

        // Create 3 selectable buttons (like radio buttons)
      //  ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton option1 = new ToggleButton("Lines");
        ToggleButton option2 = new ToggleButton("Map");
        ToggleButton option3 = new ToggleButton("Pricing");

//        option1.setToggleGroup(toggleGroup);
//        option2.setToggleGroup(toggleGroup); do this to only select one item
//        option3.setToggleGroup(toggleGroup);

        // Style the buttons to make them larger
        option1.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");
        option2.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");
        option3.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");

        // Add the buttons to a VBox and center them
        VBox centerBox = new VBox(10, option1, option2, option3);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        // Add the VBox to the center of the BorderPane
        root.setCenter(centerBox);

        //continue button
        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-font-size: 18px; -fx-min-width: 150px; -fx-min-height: 40px;");
        
        // Add the continue button to the bottom of the BorderPane
        StackPane bottomPane = new StackPane(continueButton);
        bottomPane.setPadding(new Insets(20));
        root.setBottom(bottomPane);

        // Handle continue button click
        continueButton.setOnAction(e -> handleContinue(root,option1,option2,option3));

    }



	private void handleContinue(BorderPane root,ToggleButton option1,ToggleButton option2,ToggleButton option3) {
		// ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
		if (option1.isSelected()) {
			System.out.println("Selected option lines");
			lines = true;
		}
		if (option2.isSelected()) {
			System.out.println("Selected option map");
			map = true;
		}
		if (option3.isSelected()) {
			System.out.println("Selected option pricing");
			pricing = true;
		}
		if(!option1.isSelected() && !option2.isSelected() && !option1.isSelected()){
			System.out.println("No option selected!");
		}
		
		cS.handleCustScreen(root);
		
		
	}

	private void handleTopHalf(BorderPane root) {
		 // Create a VBox to hold the menu bar and title label
        VBox topContainer = new VBox();
        topContainer.setAlignment(Pos.CENTER); // Center align the contents
        topContainer.setSpacing(10); // Add spacing between menu bar and title label

        //title
        Label titleLabel = new Label("QUERY");
        titleLabel.setFont(Font.font(24));
        titleLabel.setStyle("-fx-font-weight: bold;");
        StackPane titlePane = new StackPane(titleLabel);
        titlePane.setPadding(new Insets(10));

        // Adding the title label to the top container
        topContainer.getChildren().add(titlePane);

        //settings button: top-right corner
        Button settingsButton = new Button("Settings");
        HBox topRightContainer = new HBox(settingsButton);
        topRightContainer.setAlignment(Pos.TOP_RIGHT);
        topRightContainer.setPadding(new Insets(10));
        
        // Handle settings button click
        settingsButton.setOnAction(e -> handleSettings());

        // Add the top container and settings button to the top region of the BorderPane
        root.setTop(new VBox(topContainer, topRightContainer));		
	}

	private void handleSettings() { //handle later
            System.out.println("Settings button clicked!");  
	}

}
