package com.coinkeeper.classes;

public class Budget {
	
	private int id;
	private String name;
	private float money;
	private float paid;
	private int status;
	
	public Budget() {}
	public Budget(String  name,float money, float paid, int status) {
		this.name = name;
		this.money = money;
		this.paid = paid;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public float getPaid() {
		return paid;
	}
	public void setPaid(float paid) {
		this.paid = paid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	} 
	
	
	
	
}
