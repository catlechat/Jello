package application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Project {
	@FXML
	ListView toDoList;
	@FXML
	ListView curentList;
	@FXML
	ListView doneList;
	@FXML
	Label projectName;
	
	String userToken;
	String projectID;
	String taskID;
	
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
		fillData();
		
	}
	
	public void fillData() throws IOException, InterruptedException {
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
        JSONObject payload = (JSONObject)parent.get("response");
        
        
        String name = payload.get("name").toString();
        System.out.println(name);
        projectName.setText(name);
        
	}
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("chooseProject.fxml", new Token(userToken)); 
	}
	
	
	public void options() throws IOException {
		Main m = new Main();
		m.changeScene("projectOptions.fxml", new Token(userToken, projectID)); 
	}
	
	public void addTaskToDo() throws IOException {
		Main m = new Main();
		m.changeScene("addTask.fxml", new Token(userToken, projectID, "To Do")); 
	}
	public void addTaskCurent() throws IOException {
		Main m = new Main();
		m.changeScene("addTask.fxml", new Token(userToken, projectID, "Curent")); 
	}
	public void addTaskDone() throws IOException {
		Main m = new Main();
		m.changeScene("addTask.fxml", new Token(userToken, projectID, "Done")); 
	}
	
	public void taskInfo() throws IOException {
		Main m = new Main();
		m.changeScene("taskInfo.fxml", new Token(userToken, taskID)); 
	}

}
