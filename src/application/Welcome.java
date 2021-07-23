package application;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Welcome implements Initializable{


	@FXML
	private Label welcomeMessage;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		welcomeMessage.setText(getTextByHour());
		fadeInOut(welcomeMessage);
		timeIt(3400l);
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
	
	public String getTextByHour() {
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour < 12) {
			return "Good Morning";
		}else if(hour >= 12 && hour <=16) {
			return "Good Afternoon";
		}else if(hour > 16 && hour < 20){
			return "Good Evening";
		}else {
			return "Good Night";
		}
		
	}
	
	@SuppressWarnings("unused")
	public void nextScene() {
		Main m = new Main();
    	if(false) {
    		try {
    			m.changeScene("login.fxml", null);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}else {
    		try {
    			m.changeScene("welcomeBack.fxml", null);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
	}
	
}
