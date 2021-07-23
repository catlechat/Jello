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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Project {
	@FXML
	ListView<String> toDoList;
	@FXML
	ListView<String> curentList;
	@FXML
	ListView<String> doneList;
	@FXML
	Label projectName;
	
	String userToken;
	String projectID;
	String userID;
	String taskID;
	
	Boolean clicked = false;
	
	//<Name,ID>
	Map<String,String> tasksID = new HashMap<String, String>();
	ObservableList<String> itemsToDo = FXCollections.observableArrayList();
	ObservableList<String> itemsCurent = FXCollections.observableArrayList();
	ObservableList<String> itemsDone = FXCollections.observableArrayList();


	
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
			projectID = t.getProjectID();
			userID = t.getUserID();
			fillData();
			clicked = true;
		}
		
		
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
        Object obj = null;
	    JSONParser parser = new JSONParser();
        try {
			obj = parser.parse(response.body());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        JSONObject parent = (JSONObject)obj;
        JSONObject payload = (JSONObject)parent.get("response");
        
        projectName.setText(payload.get("name").toString());
               
        JSONArray tasks = (JSONArray)payload.get("tasks");

        ArrayList<JSONObject> tasksArray = new ArrayList<JSONObject>();     
		if (tasks != null) { 
		   int len = tasks.size();
		   for (int i=0;i<len;i++){ 
			   tasksArray.add((JSONObject) tasks.get(i));
		   } 
		} 
		
		for(JSONObject task : tasksArray){
			switch(task.get("status").toString()) {
			  case "To Do":
				  tasksID.put(task.get("name").toString(),task.get("task_id").toString());
				  itemsToDo.add(task.get("name").toString());
			    break;
			  case "Curent":
				  tasksID.put(task.get("name").toString(),task.get("task_id").toString());
				  itemsCurent.add(task.get("name").toString());
			    break;
			  case "Done":
				  tasksID.put(task.get("name").toString(),task.get("task_id").toString());
				  itemsDone.add(task.get("name").toString());
				break;
			  default:
			    // code block
			}
		}
		toDoList.setItems(itemsToDo);
		curentList.setItems(itemsCurent);
		doneList.setItems(itemsDone);
		
        
	}
	public void handle(KeyEvent ke) throws IOException, InterruptedException {
        if (ke.getCode() == KeyCode.RIGHT || ke.getCode() == KeyCode.LEFT) {
			Object node = ke.getSource();
			@SuppressWarnings("unchecked")
			ListView<String> v = (ListView<String>)node;
			
			String selectedItem = v.getSelectionModel().getSelectedItem().toString();
			String selectedTask = tasksID.get(selectedItem);
			
			
			
			String status = null;
			
			String selectedStatus = null;
			
			if(itemsToDo.contains(selectedItem)) {
				selectedStatus = "To Do";
			}
			if(itemsCurent.contains(selectedItem)) {
				selectedStatus = "Curent";
			}
			if(itemsDone.contains(selectedItem)) {
				selectedStatus = "Done";
			}
						
	        if (ke.getCode() == KeyCode.RIGHT) {
	
	        	if(selectedStatus.equals("To Do")) {
	    			status = "Curent";
	    		}else if(selectedStatus.equals("Curent")){
	    			status = "Done";
	    		}else if(selectedStatus.equals("Done")) {
	    			status = "To Do";
	    		}
	        }
	
	        if (ke.getCode() == KeyCode.LEFT) {
	        	if(selectedStatus.equals("To Do")) {
	    			status = "Done";
	    		}else if(selectedStatus.equals("Curent")) {
	    			status = "To Do";
	    		}else if(selectedStatus.equals("Done")) {
	    			status = "Curent";
	    		}
	        }
	        
	        String body = new StringBuilder()
	                .append("{")
	                .append("\"task_id\":\""+selectedTask+"\",")
	                .append("\"new_status\":\""+status+"\"")
	                .append("}").toString();
			HttpRequest request = HttpRequest.newBuilder()
	                .PUT(HttpRequest.BodyPublishers.ofString(body))
	                .uri(URI.create("https://benevold.herokuapp.com/jello/task/status"))
	                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
	                .header("Content-Type", "application/json")
	                .header("access-token", userToken)
	                .build();
	        @SuppressWarnings("unused")
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	    	toDoList.getItems().clear();
	    	curentList.getItems().clear();
	    	doneList.getItems().clear();
	    	itemsToDo.clear();
	    	itemsCurent.clear();
	    	itemsDone.clear();
	    	fillData();
        }
    }
	
	
	public void back() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("chooseProject.fxml", new Token(userToken, null,null,userID));
		}
	}
	
	
	public void options() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("projectOptions.fxml", new Token(userToken, projectID,null,userID));
		}
	}
	
	public void addTaskToDo() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("addTask.fxml", new Token(userToken, projectID, "To Do",userID));
		}
	}
	public void addTaskCurent() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("addTask.fxml", new Token(userToken, projectID, "Curent",userID)); 
		}
	}
	public void addTaskDone() throws IOException {
		if(userToken != null) {
			Main m = new Main();
			m.changeScene("addTask.fxml", new Token(userToken, projectID, "Done",userID)); 
		}
	}
	
	public void taskInfo(ActionEvent event) throws IOException {
		if(userToken != null) {
			if(toDoList.getSelectionModel().getSelectedItem() != null) {
				String selectedName = toDoList.getSelectionModel().getSelectedItem().toString();
				String selectedTaskID = tasksID.get(selectedName);
				Main m = new Main();
				m.changeScene("taskInfo.fxml", new Token(userToken, projectID, null, userID, selectedTaskID));
			}else if(curentList.getSelectionModel().getSelectedItem() != null) {
				String selectedName = curentList.getSelectionModel().getSelectedItem().toString();
				String selectedTaskID = tasksID.get(selectedName);
				Main m = new Main();
				m.changeScene("taskInfo.fxml", new Token(userToken, projectID, null, userID, selectedTaskID));
			}else if(doneList.getSelectionModel().getSelectedItem() != null) {
				String selectedName = doneList.getSelectionModel().getSelectedItem().toString();
				String selectedTaskID = tasksID.get(selectedName);
				Main m = new Main();
				m.changeScene("taskInfo.fxml", new Token(userToken, projectID, null, userID, selectedTaskID));
			}
		}
	}
	
	
	
}
