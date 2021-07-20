package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TaskInfo {
	@FXML
	Label titleLabbel;
	@FXML
	Label descriptionLabbel;
	@FXML
	ListView teamList;
	@FXML
	ListView activityList;
	@FXML
	TextField commentField;
	@FXML
	Label initialsLabbel;
	
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("project.fxml",null); 
	}
	
	
	public void addToTask() throws IOException {
		Main m = new Main();
		m.changeScene("addToTask.fxml",null); 
	}
	public void quitTask() throws IOException {
		Main m = new Main();
		m.changeScene("addToTask.fxml",null); 
	}
	

}
