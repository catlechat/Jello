module JelloFX {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires json.simple;
	requires javafx.base;
	requires java.net.http;
	
	opens application to javafx.graphics, javafx.fxml;
}
