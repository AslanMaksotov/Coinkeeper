package com.coinkeeper.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	public static final String db_name = "coinkeeper.db";
	
	public static final String db_table_category = "category";
	public static final String d_category_id = "_id";
	public static final String d_category_image_id = "image_id";
	public static final String d_category_name = "name";
	public static final String d_category_type = "type";

	public static final String db_table_budget = "budget";
	public static final String d_budget_id = "_id";
	public static final String d_budget_name = "name";
	public static final String d_budget_money = "money";
	public static final String d_budget_paid = "paid";
	public static final String d_budget_status= "status";
	
	public static final String db_table_costs = "costs";
	public static final String d_costs_id = "_id";
	public static final String d_costs_money = "money";
	public static final String d_costs_category_id = "category_id";
	public static final String d_costs_name = "name";
	public static final String d_costs_date = "date";
	public static final String d_costs_comments = "comments";
	public static final String d_costs_repeat = "repeat";
	public static final String d_costs_count = "count";
	public static final String d_costs_type = "type";
	public static final String d_costs_budget_id = "budget_id";
	
	public static final String db_table_gain = "gain";
	public static final String d_gain_id = "_id";
	public static final String d_gain_money = "money";
	public static final String d_gain_category_id = "category_id";
	public static final String d_gain_name = "name";
	public static final String d_gain_date = "date";
	public static final String d_gain_comments = "comments";
	public static final String d_gain_repeat = "repeat";
	public static final String d_gain_count = "count";
	public static final String d_gain_type = "type";
	
	public static final String db_table_notes = "notes";
	public static final String d_notes_id = "_id";
	public static final String d_notes_name = "name";
	public static final String d_notes_money = "money";
	public static final String d_notes_date = "date";
	
	private static final String db_create_table_category = "create table "+db_table_category+"("+
											d_category_id+" integer primary key, "+
											d_category_image_id+" integer, "+
											d_category_name+" text not null, "+
											d_category_type+" integer);";
	
	private static final String db_create_table_budget = "create table "+db_table_budget+"("+
			d_budget_id+" integer primary key, "+
			d_budget_name+" text not null, "+
			d_budget_money+" real,"+
			d_budget_paid+" real,"+
			d_budget_status+" integer);";
	
	private static final String db_create_table_costs = "create table "+db_table_costs+"("+
			d_costs_id+" integer primary key , "+
			d_costs_category_id+" integer, "+
			d_costs_name+" text not null, "+
			d_costs_money+" real, "+
			d_costs_date+" text not null, "+
			d_costs_repeat+" integer, "+
			d_costs_count+" integer, "+
			d_costs_type+" integer, "+
			d_costs_budget_id+" integer, "+
			d_costs_comments+" text not null);";
	
	private static final String db_create_table_gain = "create table "+db_table_gain+"("+
			d_gain_id+" integer primary key , "+
			d_gain_category_id+" integer, "+
			d_gain_name+" text not null, "+
			d_gain_money+" real, "+
			d_gain_date+" text not null, "+
			d_gain_repeat+" integer, "+
			d_gain_count+" integer, "+
			d_gain_type+" integer, "+
			d_gain_comments+" text not null);";
	
	private static final String db_create_table_notes = "create table "+db_table_notes+"("+
			d_notes_id+" integer primary key , "+
			d_notes_name+" text not null, "+
			d_notes_money+" real, "+
			d_notes_date+" text not null);";
	
	
	public DBOpenHelper(Context context) {
		super(context, db_name, null, 1);	
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(db_create_table_budget);
		database.execSQL(db_create_table_category);
		database.execSQL(db_create_table_costs);
		database.execSQL(db_create_table_gain);
		database.execSQL(db_create_table_notes);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + db_create_table_budget);
		db.execSQL("DROP TABLE IF EXISTS " + db_create_table_category);
		db.execSQL("DROP TABLE IF EXISTS " + db_create_table_costs);
		db.execSQL("DROP TABLE IF EXISTS " + db_create_table_gain);
		db.execSQL("DROP TABLE IF EXISTS " + db_create_table_notes);
		onCreate(db);
	}

}
