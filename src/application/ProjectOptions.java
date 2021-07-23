package application;

import java.io.IOException;
import java.net.URI;
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
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ProjectOptions {
	
	@FXML
	TextField nameLabbel;
	@FXML
	ListView<String> allUsersList;
	ObservableList<String> items = FXCollections.observableArrayList();
	Map<String,String> usersID = new HashMap<String, String>();

	@FXML
	ListView<String> teamList;
	ObservableList<String> items2 = FXCollections.observableArrayList();

	
	@FXML
	Label getUsersLabel;
	
	Boolean clicked = false;

		
	String userID;
	String userToken; 
	String projectID;
	String projectName;


	
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	public void back() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("project.fxml", new Token(userToken, projectID,null,userID)); 
		}
	}
	
	public void changeName(ActionEvent event) throws Exception {
		if(!clicked) {
			String newName = nameLabbel.getText();
			Node node = (Node) event.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Token t = (Token) stage.getUserData();
			userToken = t.getToken();
			projectID = t.getProjectID();
			userID = t.getUserID();
		
			changeNameAPI(newName);
		}
		
	}
	
	public void getUsers(MouseEvent event) throws Exception {
		if(!clicked) {
			Node node = (Node) event.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Token t = (Token) stage.getUserData();
			userToken = t.getToken();
			projectID = t.getProjectID();
			userID = t.getUserID();

			setUsersList();
			setTeamList();
		}
	}
	
	public void setTeamList() {
		JSONObject payload = null;
		try {
			payload = getTeamUsersAPI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray resTeamList = (JSONArray) payload.get("team");
		
		ArrayList<JSONObject> team = new ArrayList<JSONObject>();
		
		if (resTeamList != null) { 
		   int len = resTeamList.size();
		   for (int i=0;i<len;i++){ 
			   team.add((JSONObject) resTeamList.get(i));
		   } 
		} 
		for(JSONObject user : team){			
			items2.add(""+user.get("name"));
		}
		teamList.setItems(items2);
		
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
		}
		allUsersList.setItems(items);
	}
	
	
	
	public JSONArray getUsersAPI() throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://benevold.herokuapp.com/jello/users"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", userToken)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
	
	public JSONObject getTeamUsersAPI() throws Exception {
		String body = new StringBuilder()
                .append("{")
                .append("\"project_id\":\""+projectID+"\"")
                .append("}").toString();
		HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("https://benevold.herokuapp.com/jello/projectInfo"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", userToken)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Object obj = null;
	    JSONParser parser = new JSONParser();
        try {
			obj = parser.parse(response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONObject parent = (JSONObject)obj;
        JSONObject payload = (JSONObject)parent.get("response");
		return payload;
	}
	
	
	
	
	public void addUsersAPI(ActionEvent event) throws Exception{
		String selectedUser = allUsersList.getSelectionModel().getSelectedItem().toString();
		if(!items2.contains(selectedUser)) {
			String body = new StringBuilder()
	                .append("{")
	                .append("\"project_id\":\""+projectID+"\",")
	                .append("\"user_id\":\""+usersID.get(selectedUser)+"\"")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .PUT(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/project/add/user"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        @SuppressWarnings("unused")
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	        teamList.getItems().clear();
	        setTeamList();
		}
	}

	public void done() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("project.fxml", new Token(userToken, projectID, null, userID)); 
		}
	}
	
	public void changeNameAPI(String labbel) throws Exception {		
		String body = new StringBuilder()
                .append("{")
                .append("\"projectId\":\""+projectID+"\",")
                .append("\"new_name\":\""+labbel+"\"")
                .append("}").toString();
		HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("https://benevold.herokuapp.com/jello/project/name"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", userToken)
                .build();
        @SuppressWarnings("unused")
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	}

}
