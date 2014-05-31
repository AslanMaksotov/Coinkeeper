package com.coinkeeper.classes;

public class Category {
	private int id;
	private int image_id;
	private String name;
	private int type;
	
	public Category(int image_id, String name, int type) {
		super();
		this.image_id = image_id;
		this.name = name;
		this.type = type;
	}
	public Category(){}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getImage_id() {
		return image_id;
	}
	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
