package com.coinkeeper.classes;

public class Gain {
	private int id;
	private float money;
	private int categoryId;
	private String name;
	private String date;
	private String comments;
	private boolean repeat;
	private int count;
	private int type;
	
	public Gain(float money, int categoryId, String name, String date,
			String comments, boolean repeat, int count, int type) {
		super();
		this.money = money;
		this.categoryId = categoryId;
		this.name = name;
		this.date = date;
		this.comments = comments;
		this.repeat = repeat;
		this.count = count;
		this.type = type;
	}
	public Gain(){}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
