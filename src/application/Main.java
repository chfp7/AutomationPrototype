package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage primaryStage;
    private BorderPane root;
   

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("Query");
   
        // Create the root layout
        root = new BorderPane();
        Scene scene = new Scene(root, 380, 620);

	    Start start = new Start(root);//this starts all the functions
	    
	    
	    scene.setFill(Color.LIGHTGREEN); // Set background color of the scene

        primaryStage.setScene(scene);// Set the scene and show the stage	
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}