package com.gatech.gameswap.commons;

public class ErrorResponse
{
	public ErrorResponse(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	private String id;
	private String message;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ErrorResponse [id=" + id + ", message=" + message + "]";
	}
}
