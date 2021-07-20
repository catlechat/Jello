package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AddToTask {
	@FXML
	ListView allUsersList;
	@FXML
	ListView teamList;
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("taskInfo.fxml", null); 
	}
	
	
	public void done() throws IOException {
		//ajouter les gens a la tache
		//verifier si la personne n'est pas deja dedans
		Main m = new Main();
		m.changeScene("taskInfo.fxml", null); 
	}
}
