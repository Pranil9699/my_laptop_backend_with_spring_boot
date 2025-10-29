package com.mylaptop.org.payload;

public class JwtAuthResponse {
    private String token;
    private String email;
    private String role;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public JwtAuthResponse(String token, String email, String role) {
		super();
		this.token = token;
		this.email = email;
		this.role = role;
	}
	@Override
	public String toString() {
		return "JwtAuthResponse [token=" + token + ", email=" + email + ", role=" + role + "]";
	}
	public JwtAuthResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
