package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ProjectOptions {
	
	@FXML
	TextField nameLabbel;
	@FXML
	ListView allUsersList;
	@FXML
	ListView teamList;
	
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("project.fxml", null); 
	}
	
	public void changeName() throws Exception {
		String newName = nameLabbel.getText();
		changeNameAPI(newName);
	}
	
	
	public void done() throws IOException {
		Main m = new Main();
		m.changeScene("project.fxml", null); 
	}
	
	public void changeNameAPI(String labbel) throws Exception {		
		
		String body = new StringBuilder()
                .append("{")
                .append("\"project_id\":\"60f524f745e9db596201d597\",")
                .append("\"new_name\":\""+labbel+"\"")
                .append("}").toString();
		HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("https://benevold.herokuapp.com/jello/project/name"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwZjQ3ZDNmMjY5MjRlMWMxOTgxYTBjZCIsImVtYWlsIjoiaXZhbiIsImlhdCI6MTYyNjY5NDUyMywiZXhwIjoxNjI2NzgwOTIzfQ.Qxo-pAy-pCpeiLUSO-cJSaALWAfsxxdyjfK_ZyV4h60")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print status code
        System.out.println(response.statusCode());
        // print response body
        System.out.println(response.body()); 
	}

}
