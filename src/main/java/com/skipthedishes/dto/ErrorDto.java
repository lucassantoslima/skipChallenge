package com.skipthedishes.dto;

public class ErrorDto {

	private String userMsg;
	private String developerMsg;

	public ErrorDto(String userMsg, String developerMsg) {
		this.userMsg = userMsg;
		this.developerMsg = developerMsg;
	}

	public String getUserMsg() {
		return userMsg;
	}

	public String getDeveloperMsg() {
		return developerMsg;
	}

	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}

	public void setDeveloperMsg(String developerMsg) {
		this.developerMsg = developerMsg;
	}

}
