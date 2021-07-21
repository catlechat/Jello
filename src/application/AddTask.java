package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddTask {
	
	@FXML
	TextField nameField;
	@FXML
	TextArea descriptionField;
	@FXML
	ListView<String> usersList;
	ObservableList<String> items = FXCollections.observableArrayList();
	Map<String,String> usersID = new HashMap<String, String>();
	
	String userToken;
	String projectID;
	String taskID;
	String taskStatus;
	
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	@FXML
	private void receiveData(ActionEvent event) throws IOException, InterruptedException {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Token t = (Token) stage.getUserData();
		userToken = t.getToken();
		projectID = t.getData();
		taskStatus = t.getStatus();
		setUsersList();
		System.out.print("ok");
	}
	
	
	public void taskMe() throws IOException, InterruptedException {
		if(userToken != null) {
			String name = nameField.getText();
			String description = descriptionField.getText();
			String userName = usersList.getSelectionModel().getSelectedItem().toString();
			String user_id = usersID.get(userName);
			String shortN = "OO";
			
			String body = new StringBuilder()
	                .append("{")
	                .append("\"name\":\""+name+"\",")
	                .append("\"projectId\":\""+projectID+"\",")
	                .append("\"status\":\""+taskStatus+"\",")
	                .append("\"description\":\""+description+"\",")
	                .append("\"team\":[")
	                .append("{ \"user_id\":\""+user_id+"\",")
	                .append("\"name\":\""+userName+"\",")
	                .append("\"short\":\""+shortN+"\"}]")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .POST(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/task"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	        // print status code
	        System.out.println(response.statusCode() + " pour l'ajout d'une tache");
	        System.out.println(response.body() + "pour l'ajout d'une tache");
	        
			
		}
	}
	
	public void setUsersList() {
		JSONArray payload = null;
		try {
			payload = getUsersAPI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<JSONObject> users = new ArrayList<JSONObject>();     
		if (payload != null) { 
		   int len = payload.size();
		   for (int i=0;i<len;i++){ 
			   users.add((JSONObject) payload.get(i));
		   } 
		} 
		for(JSONObject user : users){
			usersID.put(user.get("username").toString(), user.get("_id").toString());
			
			items.add(""+user.get("username"));
			System.out.println("User name : "+ user.get("username"));
		}
		usersList.setItems(items);
	}
	
	public JSONArray getUsersAPI() throws Exception {
		System.out.println("Getting Users with token : " + userToken);
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://benevold.herokuapp.com/jello/users"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", userToken)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print status code
        System.out.println(response.statusCode());
        // print response body
        System.out.println(response.body()); 
		System.out.println("Reading body");
		Object obj = null;
	    JSONParser parser = new JSONParser();
        try {
			obj = parser.parse(response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONObject parent = (JSONObject)obj;

		return (JSONArray)parent.get("response");
	}
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("project.fxml", new Token(userToken, projectID)); 
	}
	


}
