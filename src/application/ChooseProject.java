package application;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChooseProject implements Initializable{
		
	@FXML
	Button createNewButton;
	@FXML
	ListView<String> projectList;
	ObservableList<String> items = FXCollections.observableArrayList();
	@FXML
	Label nameLabbel;
	
	String userToken;
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	@FXML
	private void receiveData(MouseEvent event) throws IOException, InterruptedException {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Token t = (Token) stage.getUserData();
		userToken = t.getToken();
		
		fillProjects();
		
	}
	
	public void fillProjects() throws IOException, InterruptedException {		
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://benevold.herokuapp.com/jello/projects"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", userToken)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body()); 
        Object obj = null;
	    JSONParser parser = new JSONParser();
        try {
			obj = parser.parse(response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONObject parent = (JSONObject)obj;
        JSONArray payload = (JSONArray)parent.get("response");
		ArrayList<JSONObject> projects = new ArrayList<JSONObject>();     
		if (payload != null) { 
		   int len = payload.size();
		   for (int i=0;i<len;i++){ 
			   projects.add((JSONObject) payload.get(i));
		   } 
		} 	
		for(JSONObject project : projects){
			items.add(""+project.get("name"));
			System.out.println("User name : "+ project.get("username"));
		}
		projectList.setItems(items);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void createNew() throws IOException {
		Main m = new Main();
		m.changeScene("createProject.fxml", new Token(userToken)); 
	}
	
	public void select() throws IOException {
		Main m = new Main();
		m.changeScene("project.fxml", null); 
	}
	
	public void exit() throws IOException {
        System.exit(0);
	}
	
	public void leaveProject() throws IOException {
		System.out.print(userToken);
	}
	
	
	public void disconnect() throws IOException {
		Main m = new Main();
		m.changeScene("login.fxml", null); 
	}
}
