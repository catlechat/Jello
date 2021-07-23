package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TaskInfo {
	@FXML
	Label titleLabbel;
	@FXML
	Label descriptionLabbel;
	@FXML
	ListView<String> teamList;
	ObservableList<String> items = FXCollections.observableArrayList();

	@FXML
	ListView<String> activityList;
	ObservableList<String> items2 = FXCollections.observableArrayList();

	@FXML
	TextField commentField;
	@FXML
	Label initialsLabbel;
	
	String userToken;
	String projectID;
	String taskID;
	String userID;
	String initi;
	Boolean clicked = false;
	

	
	
	@FXML
	private void receiveData(MouseEvent event) throws IOException, InterruptedException {
		if(!clicked) {
			Node node = (Node) event.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Token t = (Token) stage.getUserData();
			userToken = t.getToken();
			projectID = t.getProjectID();
			taskID = t.getTaskID();		
			userID = t.getUserID();
			initi = String.valueOf(getUsernameFromFile().charAt(0));
			initialsLabbel.setText(initi);
			fillData();
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
			      }
			      myReader.close();
		      }
		      
		    } catch (FileNotFoundException e) {
		    }
		return data;
	}


	
	private void fillData() throws IOException, InterruptedException {
		String body = new StringBuilder()
                .append("{")
                .append("\"task_id\":\""+taskID+"\"")
                .append("}").toString();
		HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("https://benevold.herokuapp.com/jello/taskInfo"))
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
        titleLabbel.setText(payload.get("name").toString());
        descriptionLabbel.setText(payload.get("description").toString());
        JSONArray resTeamList = (JSONArray) payload.get("team");
        JSONArray resComments = (JSONArray) payload.get("comments");
		ArrayList<JSONObject> team = new ArrayList<JSONObject>();
		ArrayList<JSONObject> comment = new ArrayList<JSONObject>();
		if (resTeamList != null) { 
		   int len = resTeamList.size();
		   for (int i=0;i<len;i++){ 
			   team.add((JSONObject) resTeamList.get(i));
		   } 
		} 
		for(JSONObject user : team){			
			items.add(""+user.get("name"));
		}
		teamList.setItems(items);
		
		
		if (resComments != null) { 
			   int len = resComments.size();
			   for (int i=0;i<len;i++){ 
				   comment.add((JSONObject) resComments.get(i));
			   } 
		} 
		for(JSONObject com : comment){			
			items2.add(com.get("short")+" : "+com.get("date_time")+" : "+com.get("message"));
		}
		activityList.setItems(items2);
		
	}
	
	



	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	public void back() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("project.fxml", new Token(userToken, projectID, null, userID, taskID)); 
		}
	}
	
	
	public void addToTask() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("addToTask.fxml", new Token(userToken, projectID, null, userID, taskID)); 
		}
	}
	public void quitTask() throws IOException, InterruptedException {
		if(userToken != null) {
			String body = new StringBuilder()
	                .append("{")
	                .append("\"task_id\":\""+taskID+"\",")
	                .append("\"user_id\":\""+userID+"\"")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .PUT(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/task/delete/user"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        @SuppressWarnings("unused")
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			Main m = new Main();
			m.changeScene("project.fxml", new Token(userToken, projectID, null, userID, taskID)); 

		}
	}
	
	
	public void sendComent() throws IOException, InterruptedException {
		if(userToken != null) {
			Date date= new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String message = commentField.getText();
			String body = new StringBuilder()
	                .append("{")
	                .append("\"task_id\":\""+taskID+"\",")
	                .append("\"date_time\":\""+ts+"\",")
	                .append("\"message\":\""+message+"\",")
	                .append("\"short\":\""+initi+"\"")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .POST(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/task/activity"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        @SuppressWarnings("unused")
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	        commentField.setText("");
	        teamList.getItems().clear();
	        items.clear();
	        activityList.getItems().clear();
	        items2.clear();
	        fillData();
		}
	}
	

}
