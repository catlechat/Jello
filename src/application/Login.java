package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Login {

	@FXML
	Button popupButton;
	
	@FXML
	Button singinButton;
	
	@FXML
	TextField loginField;
	
	@FXML
	PasswordField passwordField;
	
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
		
	public void singin() throws Exception {
		
		String login = loginField.getText();
		String password = passwordField.getText();
		
		String body = new StringBuilder()
                .append("{")
                .append("\"email\":\""+login+"\",")
                .append("\"password\":\""+password+"\"")
                .append("}").toString();
		HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("https://benevold.herokuapp.com/jello/auth?time=50000000"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print status code
        System.out.println(response.statusCode());
        // print response body
        System.out.println(response.body()); 
        Object obj = null;
	    JSONParser parser = new JSONParser();
		obj = parser.parse(response.body());
        JSONObject parent = (JSONObject)obj;
		String token = (String) parent.get("token");
        System.out.println("Token : " + token);
		
		if(token != null) {
			Main m = new Main();
			m.changeScene("chooseProject.fxml", new Token(token)); 
		}else {
			final Stage dialog = new Stage();
	        VBox dialogVbox = new VBox(20);
	        dialogVbox.setStyle(""
	        		+ "    -fx-font: 30 arial;"
	        		+ "    -fx-padding: 5 ;");
	        Text text = new Text("Wrong Username or Password !");
	        text.setWrappingWidth(300);
	        text.setStyle(""
	        		+ "    -fx-text-alignment: center;");
	        dialogVbox.getChildren().add(text);
	        Scene dialogScene = new Scene(dialogVbox, 300, 120);
	        dialog.setScene(dialogScene);
	        dialog.show();
		}
		   
	}
	
	
	
	
	public void openPopup() {
		final Stage dialog = new Stage();
        VBox dialogVbox = new VBox(20);
        dialogVbox.setStyle(""
        		+ "    -fx-font: 24 arial;"
        		+ "    -fx-padding: 5 ;");
        Text text = new Text("If you don't have an account, please "
        		+ "contact your system administrator to make one");
        text.setWrappingWidth(300);
        text.setStyle(""
        		+ "    -fx-text-alignment: center;");
        dialogVbox.getChildren().add(text);
        Scene dialogScene = new Scene(dialogVbox, 300, 120);
        dialog.setScene(dialogScene);
        dialog.show();
	}
}
