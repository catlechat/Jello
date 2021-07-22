package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class WelcomeBack implements Initializable{
	
	
	
	@FXML
	private Label welcomeBackLabbel;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		welcomeBackLabbel.setText(welcomeBackLabbel.getText()+" "+getUsernameFromFile());
		fadeInOut(welcomeBackLabbel);
		timeIt(3400l);
		
		
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

	public void timeIt(long d) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){public void run(){nextScene();}};
		timer.schedule(task,d);
	}
	
	public void fadeInOut(Label l) {
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(1700));
		fade.setFromValue(0);
		fade.setToValue(10);
		fade.setCycleCount(2);
		fade.setAutoReverse(true);
		fade.setNode(l);
		fade.play();
	}
	
	
	public void nextScene() {
		Main m = new Main();
		try {
			m.changeScene("login.fxml", null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}

}
