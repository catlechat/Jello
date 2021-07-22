module JelloFX {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires json.simple;
	requires javafx.base;
	requires java.net.http;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
