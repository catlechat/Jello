package application;

public class Token {

	private String token;
	private String data;
	private String status;
	
	
	public Token(String token) {
		this.token = token;
		this.data = null;
		this.status = null;
	}
	
	public Token(String token, String data) {
		this.token = token;
		this.data = data;
		this.status = null;
	}
	public Token(String token, String data, String status) {
		this.token = token;
		this.data = data;
		this.status = status;
	}

	
	public String getToken() {
		return this.token;
	}
	
	public String getData() {
		return this.data;
	}
	public String getStatus() {
		return this.status;
	}
	
}

