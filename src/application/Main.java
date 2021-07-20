package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
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