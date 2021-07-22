package application;

public class Token {

	private String token;
	private String userID;
	private String projectID;
	private String status;
	private String taskID;
	
	public Token(String token) {
		this.token = token;
	}
	
	public Token(String token, String projectID) {
		this(token);
		this.projectID = projectID;
	}
	public Token(String token, String projectID, String status) {
		this(token,projectID);
		this.status = status;
	}
	
	public Token(String token, String projectID, String status, String userID) {
		this(token,projectID,status);
		this.userID = userID;
	}
	public Token(String token, String projectID, String status, String userID, String taskID) {
		this(token,projectID,status,userID);
		this.taskID = taskID;
	}

	
	public String getToken() {
		return this.token;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	public String getProjectID() {
		return this.projectID;
	}
	public String getStatus() {
		return this.status;
	}
	public String getTaskID() {
		return this.taskID;
	}
	
}

