package application;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateProject implements Initializable{
	@FXML
	Button createButton;
	@FXML
	TextField nameField;
	@FXML
	ListView<String> allUsersList;
	ObservableList<String> items = FXCollections.observableArrayList();
	@FXML
	ListView<String> teamList;
	ObservableList<String> items2 = FXCollections.observableArrayList();
	
	Map<String,String> usersID = new HashMap<String, String>();
	
	String userToken; 
	String userID;
	Boolean clicked = false;
	
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	@FXML
	private void receiveData(ActionEvent event) throws IOException, InterruptedException {
		if(!clicked) {
			Node node = (Node) event.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Token t = (Token) stage.getUserData();
			userToken = t.getToken();
			userID = t.getUserID();
			setUsersList();
			clicked = true;
		}
	}
	
	public void create() throws IOException, InterruptedException {
		if(userToken != null) {

			String user = "";
			int taille = items2.size();
			if(items2.size() <= 1) {
				for(String teamUser : items2){
					user = "{\"user_id\" : \""+usersID.get(teamUser)+"\","
							+ "\"name\" : \""+teamUser+"\"}";
				}
			}else {
				int cur = 1;
				for(String teamUser : items2){
					user += "{\"user_id\" : \""+usersID.get(teamUser)+"\","
							+ "\"name\" : \""+teamUser+"\"}";
					if(cur != taille) {
						user += ",";
					}
					cur ++;
				}
			}
			String body = new StringBuilder()
	                .append("{")
	                .append("\"name\":\""+nameField.getText()+"\",")
	                .append("\"team\":["+user+"]")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .POST(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/project"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        @SuppressWarnings("unused")
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	       			
			Main m = new Main();
			m.changeScene("chooseProject.fxml", new Token(userToken,null,null, userID)); 
		}
	}
	public void back() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("chooseProject.fxml", new Token(userToken, null, null, userID)); 
		}
		
	}
	
	public void addToTeam() {
		if(allUsersList.getSelectionModel().getSelectedItem() != null) {
			if(!items2.contains(allUsersList.getSelectionModel().getSelectedItem())) {
				items2.add(allUsersList.getSelectionModel().getSelectedItem());
				teamList.setItems(items2);
			}
		}
	}
	
	public void removeFromTeam() {
			items2.remove(teamList.getSelectionModel().getSelectedItem().toString());
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
