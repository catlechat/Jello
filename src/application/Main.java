package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
	
	private static Stage stg;
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	stg = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        primaryStage.setTitle("Jello");
        Scene scene = new Scene(root, 1280, 800); 
        primaryStage.setScene(scene);
        primaryStage.show();        
    }
    
    
    public void changeScene(String fxml, Object obj) throws IOException{
    	Parent pane = FXMLLoader.load(getClass().getResource(fxml));
    	stg.getScene().setRoot(pane);
    	stg.setUserData(obj);
    }
    
    
}