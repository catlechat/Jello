package application;

import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChooseProject implements Initializable{
		
	@FXML
	Button createNewButton;
	@FXML
	ListView<String> projectList;
	ObservableList<String> items = FXCollections.observableArrayList();
	@FXML
	Label nameLabbel;
	
	Boolean clicked = false;
	
	Map<String,String> projectsID = new HashMap<String, String>();

	
	String userToken;
	String userID;
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	@FXML
	private void receiveData(MouseEvent event) throws IOException, InterruptedException {
		if(!clicked) {
			Node node = (Node) event.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Token t = (Token) stage.getUserData();
			userToken = t.getToken();
			userID = t.getUserID();
			fillProjects();
			
			
		    System.out.println("User ID "+ userID);
		    

			clicked = true;
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
		      System.out.println("File not found");
		    }
		return data;
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
			projectsID.put(project.get("name").toString(), project.get("_id").toString());
			items.add(""+project.get("name"));
		}
		projectList.setItems(items);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameLabbel.setText(getUsernameFromFile().toString());

		
	}

	
	public void createNew() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("createProject.fxml", new Token(userToken, null, null, userID)); 
		}
	}
	
	public void select() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("project.fxml", new Token(userToken, projectsID.get(projectList.getSelectionModel().getSelectedItem().toString()), null, userID)); 
		}
	}
	
	public void exit() throws IOException {
        System.exit(0);
	}
	
	public void leaveProject() throws IOException, InterruptedException {
		if(userToken != null && projectList.getSelectionModel().getSelectedItem() != null) {
			String selected = projectList.getSelectionModel().getSelectedItem().toString();
			//projectId
			//userID
			String user_id = userID;
			String project_id = projectsID.get(selected);
			
	        System.out.println("User id is : " +user_id);
	        System.out.println("Project id is " +project_id);

			
			
			String body = new StringBuilder()
	                .append("{")
	                .append("\"project_id\":\""+project_id+"\",")
	                .append("\"user_id\":\""+user_id+"\"")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .PUT(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/project/delete/user"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	        System.out.println(response.statusCode());
	        System.out.println(response.body());
	        
	        projectList.getItems().clear();
	        items.clear();
	        fillProjects();
	        
	        
		}
	}
	
	
	public void disconnect() throws IOException {
		Main m = new Main();
		m.changeScene("login.fxml", null); 
	}
}
