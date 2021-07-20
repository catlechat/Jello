package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AddTask {
	
	@FXML
	TextField nameField;
	@FXML
	TextField descriptionField;
	@FXML
	ListView usersList;
	
	
	public void back() throws IOException {
		
		
		
		Main m = new Main();
		m.changeScene("project.fxml", null); 
	}
	
	
	public void task() throws IOException {
		//TODO: on recup tt les champs et on fait un api call
		Main m = new Main();
		m.changeScene("project.fxml", null); 
	}


}
