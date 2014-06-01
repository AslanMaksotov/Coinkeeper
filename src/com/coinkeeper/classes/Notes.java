package com.coinkeeper.classes;

public class Notes {
	private int id;
	private String content;
	private String date;
	private String time;
	
	public Notes() {}
	
	public Notes(String content, String date, String time){
		this.content = content;
		this.date = date;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
