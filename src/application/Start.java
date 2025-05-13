package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Start {
	
	 private Sign sign = new Sign();
	 OptionsMenu oM = new OptionsMenu();

	 
	 public Start(BorderPane root) {
		 startSignScreen(root);
		}
	 
	 private void startSignScreen(BorderPane root){
		  // Create a VBox to hold the menu bar and title label
		    VBox topContainer = new VBox();
		    topContainer.setAlignment(Pos.CENTER); // Center align the contents
		    topContainer.setSpacing(10); // Add spacing between menu bar and title label

		    // Add the menu bar
		  //  topContainer.getChildren().add(createMenuBar());

		    // Add the title label
		    
		    Label titleLabel = new Label("QUERY");
		    titleLabel.setFont(Font.font(220));
		    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		    StackPane titlePane = new StackPane(titleLabel);
		    titlePane.setPadding(new Insets(10));

		    // Add the title label to the top container
		    topContainer.getChildren().add(titlePane);

		    // Set the top container as the top region of the BorderPane
		    root.setTop(topContainer);
		    // Add login and register buttons
		    Button loginButton = new Button("Login");
		    Button registerButton = new Button("Register");

		    loginButton.setOnAction(e -> sign.logIn(root));
		    registerButton.setOnAction(e -> sign.register(root));

		    VBox buttonBox = new VBox(10, loginButton, registerButton);
		    buttonBox.setAlignment(Pos.CENTER);
		    buttonBox.setPadding(new Insets(20));
		    root.setBottom(buttonBox);
		    buttonBox.setStyle("-fx-background-color: green;");
		    
		   
		   
		  //any color  root.setStyle();
	 }
	 
	 public void endSignScreen(BorderPane root,VBox buttonBox) {//??? LATER
			root.getChildren().remove(buttonBox);
		}
	
//	private MenuBar createMenuBar() {
//        // Create the menu bar
//        MenuBar menuBar = new MenuBar();
//
//        // Create menus
//        Menu fileMenu = new Menu("File");
//        MenuItem exitItem = new MenuItem("Exit");
//        exitItem.setOnAction(e -> Main.primaryStage.close());
//
//        Menu helpMenu = new Menu("Help");
//        MenuItem aboutItem = new MenuItem("About");
//
//        fileMenu.getItems().add(exitItem);
//        helpMenu.getItems().add(aboutItem);
//
//        menuBar.getMenus().addAll(fileMenu, helpMenu);
//        menuBar.setStyle("-fx-background-color: green;"); // Set menu bar color to green
//
//        return menuBar;
//    }
	
}