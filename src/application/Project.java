package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class Project {
	@FXML
	ListView toDoList;
	@FXML
	ListView curentList;
	@FXML
	ListView doneList;
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("chooseProject.fxml", null); 
	}
	
	
	public void options() throws IOException {
		Main m = new Main();
		m.changeScene("projectOptions.fxml", null); 
	}
	
	public void addTask() throws IOException {
		Main m = new Main();
		m.changeScene("addTask.fxml", null); 
	}
	
	public void taskInfo() throws IOException {
		Main m = new Main();
		m.changeScene("taskInfo.fxml", null); 
	}

}
