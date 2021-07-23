package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login implements Initializable{

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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginField.setText(getUsernameFromFile());
		
	}
		
	public void singin() throws Exception  {
		
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
        Object obj = null;
	    JSONParser parser = new JSONParser();
		obj = parser.parse(response.body());
        JSONObject parent = (JSONObject)obj;
        String token = (String) parent.get("token");
		
		if(token != null) {
	        String username = parent.get("username").toString();
	        String userID = parent.get("user_id").toString();
	        writeLoginFile(username);
			Main m = new Main();
			m.changeScene("chooseProject.fxml", new Token(token,null,null,userID)); 
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
	private String getUsernameFromFile() {
		String data = null;
		try {
		      File myObj = new File("user.txt");
		      if(myObj.exists()) {
		    	  Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) {
			        data = myReader.nextLine();
			        System.out.println(data);
			      }
			      myReader.close();
		      }
		      
		    } catch (FileNotFoundException e) {
		    }
		return data;
	}
	private void writeLoginFile(String username) {
        File fold = new File("user.txt");
        if(fold.exists()) {
            fold.delete();
        }
        File fnew = new File("user.txt");
	    try {
	        FileWriter f2 = new FileWriter(fnew, false);
	        f2.write(username);
	        f2.close();
	    } catch (IOException e) {
	        e.printStackTrace();
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
