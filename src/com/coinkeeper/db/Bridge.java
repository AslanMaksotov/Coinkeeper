package com.coinkeeper.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;

import com.coinkeeper.classes.Budget;
import com.coinkeeper.classes.Category;
import com.coinkeeper.classes.Costs;
import com.coinkeeper.classes.Gain;
import com.coinkeeper.classes.Notes;

public class Bridge {
	SQLiteDatabase db;
	DBOpenHelper adapter;

	public Bridge(Context context) {
		adapter = new DBOpenHelper(context);
	}

	public void open() {
		db = adapter.getWritableDatabase();
	}

	public void close() {
		adapter.close();
	}

	public void createGain(Gain g) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_gain_money, g.getMoney());
		values.put(DBOpenHelper.d_gain_name, g.getName());
		values.put(DBOpenHelper.d_gain_category_id, g.getCategoryId());
		values.put(DBOpenHelper.d_gain_date, g.getDate());
		values.put(DBOpenHelper.d_gain_repeat, g.isRepeat());
		values.put(DBOpenHelper.d_gain_count, g.getCount());
		values.put(DBOpenHelper.d_gain_type, g.getType());
		values.put(DBOpenHelper.d_gain_comments, g.getComments());
		try {
			db.insert(DBOpenHelper.db_table_gain, null, values);
		} catch (SQLException e) {
			Log.e("Sql Exceptionm", e.getMessage());
		}
		Log.d("Gain is", "created");

	}

	public void createCosts(Costs c) {
		ContentValues values = new ContentValues();

		int bId = c.getBudgetId();
		float money = c.getMoney();

		values.put(DBOpenHelper.d_costs_money, money);
		values.put(DBOpenHelper.d_costs_name, c.getName());
		values.put(DBOpenHelper.d_costs_category_id, c.getCategoryId());
		values.put(DBOpenHelper.d_costs_date, c.getDate());
		values.put(DBOpenHelper.d_costs_repeat, c.isRepeat());
		values.put(DBOpenHelper.d_costs_count, c.getCount());
		values.put(DBOpenHelper.d_costs_type, c.getType());
		values.put(DBOpenHelper.d_costs_budget_id, bId);
		values.put(DBOpenHelper.d_costs_comments, c.getComments());

		if (bId != -1) {
			setBudget(bId, money);
		}

		try {
			db.insert(DBOpenHelper.db_table_costs, null, values);
		} catch (SQLException e) {
			Log.e("Sql Exceptionm", e.getMessage());
		}
		Log.d("Costs is", "created");
	}

	private void setBudget(int bId, float money) {
		Budget budget = getBudgetById(bId);

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_budget_paid, (budget.getPaid() + money));

		try {
			db.update(DBOpenHelper.db_table_budget, values,
					DBOpenHelper.d_budget_id + " = " + bId, null);
		} catch (SQLException e) {
			Log.e("SQLError", e.getMessage());
		}
	}

	private Budget getBudgetById(int bId) {
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_budget
				+ " WHERE " + DBOpenHelper.d_budget_id + "=" + bId, null);
		Budget budget = new Budget();

		c.moveToFirst();
		while (!c.isAfterLast()) {

			budget.setId(c.getInt(0));
			budget.setName(c.getString(1));
			budget.setMoney(c.getFloat(2));
			budget.setPaid(c.getFloat(3));
			budget.setStatus(c.getInt(4));
			c.moveToNext();
		}
		c.close();

		return budget;
	}

	public void createBudget(Budget b) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_budget_name, b.getName());
		values.put(DBOpenHelper.d_budget_money, b.getMoney());
		values.put(DBOpenHelper.d_budget_paid, b.getPaid());
		values.put(DBOpenHelper.d_budget_status, 0);

		try {
			db.insert(DBOpenHelper.db_table_budget, null, values);
		} catch (SQLException e) {
			Log.e("Sql Exceptionm", e.getMessage());
		}
		Log.d("Budget is", "created");
	}

	public ArrayList<Gain> getGainList() {
		ArrayList<Gain> result = new ArrayList<Gain>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gain,
				null);
		c.moveToFirst();
		Gain gain;
		while (!c.isAfterLast()) {
			gain = new Gain();
			gain.setId(c.getInt(0));
			gain.setCategoryId(c.getInt(1));
			gain.setName(c.getString(2));
			gain.setMoney(c.getInt(3));
			gain.setDate(c.getString(4));
			if (c.getInt(5) == 1)
				gain.setRepeat(true);
			else
				gain.setRepeat(false);
			gain.setCount(c.getInt(6));
			gain.setType(c.getInt(7));
			gain.setComments(c.getString(8));
			result.add(gain);
			c.moveToNext();
		}
		c.close();

		return result;
	}

	public ArrayList<Costs> getCostsList() {
		ArrayList<Costs> result = new ArrayList<Costs>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_costs,
				null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Costs costs = new Costs();
			costs.setId(c.getInt(0));
			costs.setCategoryId(c.getInt(1));
			costs.setName(c.getString(2));
			costs.setMoney(c.getInt(3));
			costs.setDate(c.getString(4));
			if (c.getInt(5) == 1)
				costs.setRepeat(true);
			else
				costs.setRepeat(false);
			costs.setCount(c.getInt(6));
			costs.setType(c.getInt(7));
			costs.setBudgetId(c.getInt(8));
			costs.setComments(c.getString(9));

			result.add(costs);
			c.moveToNext();
		}
		c.close();
		return result;
	}

	public ArrayList<Budget> getBudgetList() {
		// Log.d("getBudgetList", "entered to this method!!!");
		ArrayList<Budget> result = new ArrayList<Budget>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_budget,
				null);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			Budget budget = new Budget();

			budget.setId(c.getInt(0));
			budget.setName(c.getString(1));
			budget.setMoney(c.getFloat(2));
			budget.setPaid(c.getFloat(3));
			budget.setStatus(c.getInt(4));

			result.add(budget);
			c.moveToNext();
		}
		c.close();
		return result;
	}

	public void deleteGain(int id) {
		try {
			db.delete(DBOpenHelper.db_table_gain, DBOpenHelper.d_gain_id
					+ " = " + id, null);
		} catch (SQLException e) {
			Log.e("SQL", e.getMessage());
		}
	}

	public void deleteCosts(int id) {
		try {
			resetCosts(id);

			db.delete(DBOpenHelper.db_table_costs, DBOpenHelper.d_costs_id
					+ " = " + id, null);
		} catch (SQLException e) {
			Log.e("SQL", e.getMessage());
		}
	}

	private void resetCosts(int id) {
		Costs costs = getCostsById(id);
		int bId = costs.getBudgetId();
		Budget budget = getBudgetById(bId);
		float money = budget.getPaid() - costs.getMoney();

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_budget_paid, money);

		try {
			db.update(DBOpenHelper.db_table_budget, values,
					DBOpenHelper.d_budget_id + " = " + bId, null);
		} catch (SQLException e) {
			Log.e("SQLError", e.getMessage());
		}
	}

	public boolean existsCategory() {
		Cursor c = db.rawQuery("SELECT * FROM "
				+ DBOpenHelper.db_table_category, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			c.close();
			return true;
		}
		c.close();
		return false;
	}

	public Category getCategoryById(int id) {
		Category category = new Category();
		Cursor c = db.rawQuery("SELECT * FROM "
				+ DBOpenHelper.db_table_category + " WHERE "
				+ DBOpenHelper.d_category_id + " = " + id, null);
		c.moveToFirst();
		category.setId(c.getInt(0));
		category.setImage_id(c.getInt(1));
		category.setName(c.getString(2));
		category.setType(c.getInt(3));
		c.close();
		return category;
	}

	public ArrayList<Category> getCategories(int type) {
		ArrayList<Category> result = new ArrayList<Category>();
		Cursor c = db.rawQuery("SELECT * FROM "
				+ DBOpenHelper.db_table_category + " WHERE "
				+ DBOpenHelper.d_category_type + " = " + type, null);
		c.moveToFirst();
		Category cat;
		while (!c.isAfterLast()) {
			cat = new Category();
			cat.setId(c.getInt(0));
			cat.setImage_id(c.getInt(1));
			cat.setName(c.getString(2));
			result.add(cat);
			c.moveToNext();
		}
		c.close();
		return result;
	}

	public Gain getGainById(int id) {
		Gain gain = new Gain();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gain
				+ " WHERE " + DBOpenHelper.d_gain_id + " = " + id, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			gain.setId(c.getInt(0));
			gain.setCategoryId(c.getInt(1));
			gain.setName(c.getString(2));
			gain.setMoney(c.getFloat(3));
			gain.setDate(c.getString(4));
			if (c.getInt(5) == 1)
				gain.setRepeat(true);
			else
				gain.setRepeat(false);
			gain.setCount(c.getInt(6));
			gain.setType(c.getInt(7));
			gain.setComments(c.getString(8));
			c.moveToNext();
		}
		c.close();
		return gain;
	}

	public void editGainById(Gain g, int id) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_gain_money, g.getMoney());
		values.put(DBOpenHelper.d_gain_name, g.getName());
		values.put(DBOpenHelper.d_gain_category_id, g.getCategoryId());
		values.put(DBOpenHelper.d_gain_date, g.getDate());
		values.put(DBOpenHelper.d_gain_repeat, g.isRepeat());
		values.put(DBOpenHelper.d_gain_count, g.getCount());
		values.put(DBOpenHelper.d_gain_type, g.getType());
		values.put(DBOpenHelper.d_gain_comments, g.getComments());
		try {
			db.update(DBOpenHelper.db_table_gain, values,
					DBOpenHelper.d_gain_id + " = " + id, null);
		} catch (SQLException e) {
			Log.e("SQLError", e.getMessage());
		}
	}

	public void editCostsById(Costs c, int id) {
		ContentValues values = new ContentValues();
		resetCosts(id);

		int bId = c.getBudgetId();
		float money = c.getMoney();

		values.put(DBOpenHelper.d_costs_money, money);
		values.put(DBOpenHelper.d_costs_name, c.getName());
		values.put(DBOpenHelper.d_costs_category_id, c.getCategoryId());
		values.put(DBOpenHelper.d_costs_date, c.getDate());
		values.put(DBOpenHelper.d_costs_repeat, c.isRepeat());
		values.put(DBOpenHelper.d_costs_count, c.getCount());
		values.put(DBOpenHelper.d_costs_type, c.getType());
		values.put(DBOpenHelper.d_costs_budget_id, c.getBudgetId());
		values.put(DBOpenHelper.d_costs_comments, c.getComments());

		if (bId != -1) {
			setBudget(bId, money);
		}

		try {
			db.update(DBOpenHelper.db_table_costs, values,
					DBOpenHelper.d_costs_id + " = " + id, null);
		} catch (SQLException e) {
			Log.e("SQLError", e.getMessage());
		}
	}

	public Costs getCostsById(int id) {
		Costs costs = new Costs();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_costs
				+ " WHERE " + DBOpenHelper.d_costs_id + " = " + id, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			costs.setId(c.getInt(0));
			costs.setCategoryId(c.getInt(1));
			costs.setName(c.getString(2));
			costs.setMoney(c.getInt(3));
			costs.setDate(c.getString(4));
			if (c.getInt(5) == 1)
				costs.setRepeat(true);
			else
				costs.setRepeat(false);
			costs.setCount(c.getInt(6));
			costs.setType(c.getInt(7));
			costs.setBudgetId(c.getInt(8));
			costs.setComments(c.getString(9));
			c.moveToNext();
		}
		c.close();
		return costs;
	}
	
	
	public Notes getNoteById(int noteId) {
		Notes notes = new Notes();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_notes
				+ " WHERE " + DBOpenHelper.d_notes_id + " = " + noteId, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.setId(c.getInt(0));
			notes.setContent(c.getString(1));
			notes.setTime(c.getString(2));
			notes.setDate(c.getString(3));
			c.moveToNext();
		}
		c.close();
		
		return notes;
	}

	public void createCategory(Category c) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_category_image_id, c.getImage_id());
		values.put(DBOpenHelper.d_category_name, c.getName());
		values.put(DBOpenHelper.d_category_type, c.getType());
		try {
			db.insert(DBOpenHelper.db_table_category, null, values);
		} catch (SQLException e) {
			Log.e("Sql Exception", e.getMessage());
		}
	}

	public float getCurrentSum() {
		float costs = 0;
		float gain = 0;

		Cursor c1 = db.rawQuery("SELECT sum(money) FROM "
				+ DBOpenHelper.db_table_costs, null);
		c1.moveToFirst();
		while (!c1.isAfterLast()) {
			costs += c1.getFloat(0);
			c1.moveToNext();
		}
		c1.close();

		Cursor c2 = db.rawQuery("SELECT sum(money) FROM "
				+ DBOpenHelper.db_table_gain, null);
		c2.moveToFirst();
		while (!c2.isAfterLast()) {
			gain += c2.getFloat(0);
			c2.moveToNext();
		}
		c2.close();

		return (gain - costs);
	}

	public void createNote(Notes notes) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_notes_content, notes.getContent());
		values.put(DBOpenHelper.d_notes_time, notes.getTime());
		values.put(DBOpenHelper.d_notes_date, notes.getDate());
		try {
			db.insert(DBOpenHelper.db_table_notes, null, values);
		} catch (SQLException e) {
			Log.e("Sql Exception", e.getMessage());
		}
		Log.d("Note is", "created");
	}
	
	public ArrayList<Notes> getNoteList(){
		ArrayList<Notes> result = new ArrayList<Notes>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_notes,
				null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Notes notes = new Notes();
			
			notes.setId(c.getInt(0));
			notes.setContent(c.getString(1));
			notes.setTime(c.getString(2));
			notes.setDate(c.getString(3));
			result.add(notes);
			c.moveToNext();
		}
		c.close();
		
		return result;
	}
	
	public void deleteNote(int id){
		try {
			db.delete(DBOpenHelper.db_table_notes, DBOpenHelper.d_notes_id
					+ " = " + id, null);
		}catch (SQLException e) {
			Log.e("SQL", e.getMessage());
		}
	}

	
	
}
