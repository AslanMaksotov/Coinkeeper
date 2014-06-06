package com.coinkeeper.classes;

public class Notes {
	private int id;
	private String content;
	private String date;
	private String time;
	private int timeFormat;
	
	
	public Notes() {}
	
	public Notes(String content, String time, String date ){
		this.content = content;
		this.date = date;
		this.time = time;
	}

	
	
	public int getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(int timeFormat) {
		this.timeFormat = timeFormat;
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
