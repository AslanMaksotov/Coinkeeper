package com.coinkeeper.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.coinkeeper.classes.Budget;
import com.coinkeeper.classes.Category;
import com.coinkeeper.classes.Costs;
import com.coinkeeper.classes.Gain;

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
	public void createGain(Gain g){
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_gain_money, g.getMoney());
		values.put(DBOpenHelper.d_gain_name, g.getName());
		values.put(DBOpenHelper.d_gain_category_id, g.getCategoryId());
		values.put(DBOpenHelper.d_gain_date, g.getDate());
		values.put(DBOpenHelper.d_gain_repeat, g.isRepeat());
		values.put(DBOpenHelper.d_gain_count, g.getCount());
		values.put(DBOpenHelper.d_gain_type, g.getType());
		values.put(DBOpenHelper.d_gain_comments, g.getComments());
		try{
			db.insert(DBOpenHelper.db_table_gain, null, values);
		}catch(SQLException e){
			Log.e("Sql Exceptionm", e.getMessage());
		}
	}

	public void createCosts(Costs c) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_costs_money, c.getMoney());
		values.put(DBOpenHelper.d_costs_name, c.getName());
		values.put(DBOpenHelper.d_costs_category_id, c.getCategoryId());
		values.put(DBOpenHelper.d_costs_date, c.getDate());
		values.put(DBOpenHelper.d_costs_repeat, c.isRepeat());
		values.put(DBOpenHelper.d_costs_count, c.getCount());
		values.put(DBOpenHelper.d_costs_type, c.getType());
		values.put(DBOpenHelper.d_costs_comments, c.getComments());
		try{
			db.insert(DBOpenHelper.db_table_costs, null, values);
		}catch(SQLException e){
			Log.e("Sql Exceptionm", e.getMessage());
		}
		Log.d("Costs is", "created");
	}
	public void createCategory(Category c){
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_category_image_id, c.getImage_id());
		values.put(DBOpenHelper.d_category_name, c.getName());
		values.put(DBOpenHelper.d_category_type, c.getType());
		try{
			db.insert(DBOpenHelper.db_table_category, null, values);
		}catch(SQLException e){
			Log.e("Sql Exception", e.getMessage());
		}
	}
	public void createBudget(Budget b) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_budget_name, b.getName());
		values.put(DBOpenHelper.d_budget_money, b.getMoney());
		values.put(DBOpenHelper.d_budget_paid, b.getPaid());
		values.put(DBOpenHelper.d_budget_status, 0);
		
		try{
			db.insert(DBOpenHelper.db_table_budget, null, values);
		}catch(SQLException e){
			Log.e("Sql Exceptionm", e.getMessage());
		}
		Log.d("Budget is", "created");
	}
	public void editGainById(Gain g, int id){
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_gain_money, g.getMoney());
		values.put(DBOpenHelper.d_gain_name, g.getName());
		values.put(DBOpenHelper.d_gain_category_id, g.getCategoryId());
		values.put(DBOpenHelper.d_gain_date, g.getDate());
		values.put(DBOpenHelper.d_gain_repeat, g.isRepeat());
		values.put(DBOpenHelper.d_gain_count, g.getCount());
		values.put(DBOpenHelper.d_gain_type, g.getType());
		values.put(DBOpenHelper.d_gain_comments, g.getComments());
		try{
			db.update(DBOpenHelper.db_table_gain, values, DBOpenHelper.d_gain_id+" = "+ id, null);
		} catch(SQLException e){
			Log.e("SQLError", e.getMessage());
		}
	}
	public Gain getGainById(int id){
		Gain gain = new Gain();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gain + " WHERE " + DBOpenHelper.d_gain_id + " = " + id, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			gain.setId(c.getInt(0));
			gain.setCategoryId(c.getInt(1));
			gain.setName(c.getString(2));
			gain.setMoney(c.getFloat(3));
			gain.setDate(c.getString(4));
			if (c.getInt(5)==1)gain.setRepeat(true); else gain.setRepeat(false);
			gain.setCount(c.getInt(6));
			gain.setType(c.getInt(7));
			gain.setComments(c.getString(8));
			c.moveToNext();
		}
		c.close();
		return gain;
	}
	public boolean existsCategory(){
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_category, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			c.close();
			return true;
		}
		c.close();
		return false;
	}
	public Category getCategoryById(int id){
		Category category = new Category();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_category + " WHERE " + DBOpenHelper.d_category_id + " = " + id, null);
		c.moveToFirst();
			category.setId(c.getInt(0));
			category.setImage_id(c.getInt(1));
			category.setName(c.getString(2));
			category.setType(c.getInt(3));
		c.close();
		return category;
	}
	public ArrayList<Budget> getBudgetList() {
		ArrayList<Budget> result = new ArrayList<Budget>();
		Cursor c = db.rawQuery("SELECT * FROM "+DBOpenHelper.db_table_budget, null);
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
	public ArrayList<Gain> getGainList(){
		ArrayList<Gain> result = new ArrayList<Gain>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gain, null);
		c.moveToFirst();
		Gain gain;
		while (!c.isAfterLast()) {
			gain = new Gain();
			gain.setId(c.getInt(0));
			gain.setCategoryId(c.getInt(1));
			gain.setName(c.getString(2));
			gain.setMoney(c.getFloat(3));
			gain.setDate(c.getString(4));
			if (c.getInt(5)==1)gain.setRepeat(true); else gain.setRepeat(false);
			gain.setCount(c.getInt(6));
			gain.setType(c.getInt(7));
			gain.setComments(c.getString(8));
			result.add(gain);
			c.moveToNext();
		}
		c.close();
		
		return result;
	}
	public ArrayList<Gain> getGainlistByDate(String date){
		ArrayList<Gain> result = new ArrayList<Gain>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gain + " WHERE "+DBOpenHelper.d_gain_date +" = "+ date, null);
		c.moveToFirst();
		Gain gain;
		while (!c.isAfterLast()) {
			gain = new Gain();
			gain.setId(c.getInt(0));
			gain.setCategoryId(c.getInt(1));
			gain.setName(c.getString(2));
			gain.setMoney(c.getFloat(3));
			gain.setDate(c.getString(4));
			if (c.getInt(5)==1)gain.setRepeat(true); else gain.setRepeat(false);
			gain.setCount(c.getInt(6));
			gain.setType(c.getInt(7));
			gain.setComments(c.getString(8));
			result.add(gain);
			c.moveToNext();
		}
		c.close();
		
		return result;
	}
	public ArrayList<Costs> getCostlistByDate(String date){
		ArrayList<Costs> result = new ArrayList<Costs>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_costs + " WHERE "+DBOpenHelper.d_costs_date +" = "+ date, null);
		c.moveToFirst();
		Costs cost;
		while (!c.isAfterLast()) {
			cost = new Costs();
			cost.setId(c.getInt(0));
			cost.setCategoryId(c.getInt(1));
			cost.setName(c.getString(2));
			cost.setMoney(c.getFloat(3));
			cost.setDate(c.getString(4));
			if (c.getInt(5)==1)cost.setRepeat(true); else cost.setRepeat(false);
			cost.setCount(c.getInt(6));
			cost.setType(c.getInt(7));
			cost.setComments(c.getString(8));
			result.add(cost);
			c.moveToNext();
		}
		c.close();
		
		return result;
	}
	public ArrayList<Costs> getCostsList() {
		ArrayList<Costs> result = new ArrayList<Costs>();
		Cursor c = db.rawQuery("SELECT * FROM "+DBOpenHelper.db_table_costs, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Costs costs = new Costs();
			costs.setId(c.getInt(0));
			costs.setCategoryId(c.getInt(1));
			costs.setName(c.getString(2));
			costs.setMoney(c.getInt(3));
			costs.setDate(c.getString(4));
			if (c.getInt(5)==1)costs.setRepeat(true); else costs.setRepeat(false);
			costs.setCount(c.getInt(6));
			costs.setType(c.getInt(7));
			costs.setComments(c.getString(8));
			result.add(costs);
			c.moveToNext();
		}
		c.close();
		return result;
	}
	public ArrayList<Category> getCategories(int type){
		ArrayList<Category> result = new ArrayList<Category>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_category + " WHERE " + DBOpenHelper.d_category_type + " = " + type, null);
		c.moveToFirst();
		Category cat;
		int i = 0;
		while (!c.isAfterLast()) {
			cat = new Category();
			cat.setId(c.getInt(0));
			cat.setImage_id(c.getInt(1));
			cat.setName(c.getString(2));
			cat.setType(c.getInt(3));
			result.add(cat);
			c.moveToNext();
			i++;
		}
		Log.d("Prev size", i+"");
		c.close();
		return result;
	}
	public void deleteGain(int id){
		try{
			db.delete(DBOpenHelper.db_table_gain, DBOpenHelper.d_gain_id + " = "+ id, null);
		} catch (SQLException e) {
			Log.e("SQL", e.getMessage());
		}
	}
	public void deleteCosts(int id){
		try{
			db.delete(DBOpenHelper.db_table_costs, DBOpenHelper.d_costs_id + " = "+ id, null);
		} catch (SQLException e) {
			Log.e("SQL", e.getMessage());
		}
	}
	public float getCurrentSum() {
		float costs = 0;
		float gain = 0;
		
		Cursor c1 = db.rawQuery("SELECT sum(money) FROM " + DBOpenHelper.db_table_costs, null);
		c1.moveToFirst();
		while (!c1.isAfterLast()) {
			costs += c1.getFloat(0);
			c1.moveToNext();
		}
		c1.close();
		
		Cursor c2 = db.rawQuery("SELECT sum(money) FROM " + DBOpenHelper.db_table_gain, null);
		c2.moveToFirst();
		while (!c2.isAfterLast()) {
			gain += c2.getFloat(0);
			c2.moveToNext();
		}
		c2.close();
		
		return (gain - costs);
	}

	public void editCostsById(Costs c, int id){
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.d_costs_money, c.getMoney());
		values.put(DBOpenHelper.d_costs_name, c.getName());
		values.put(DBOpenHelper.d_costs_category_id, c.getCategoryId());
		values.put(DBOpenHelper.d_costs_date, c.getDate());
		values.put(DBOpenHelper.d_costs_repeat, c.isRepeat());
		values.put(DBOpenHelper.d_costs_count, c.getCount());
		values.put(DBOpenHelper.d_costs_type, c.getType());
		values.put(DBOpenHelper.d_costs_comments,c.getComments());
		try{
			db.update(DBOpenHelper.db_table_costs, values, DBOpenHelper.d_costs_id+" = "+ id, null);
		} catch(SQLException e){
			Log.e("SQLError", e.getMessage());
		}
	}
	
	public Costs getCostsById(int id){
		Costs costs = new Costs();
		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_costs + " WHERE " + DBOpenHelper.d_costs_id + " = " + id, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			costs.setId(c.getInt(0));
			costs.setCategoryId(c.getInt(1));
			costs.setName(c.getString(2));
			costs.setMoney(c.getFloat(3));
			costs.setDate(c.getString(4));
			if (c.getInt(5)==1)costs.setRepeat(true); else costs.setRepeat(false);
			costs.setCount(c.getInt(6));
			costs.setType(c.getInt(7));
			costs.setComments(c.getString(8));
			c.moveToNext();
		}
		c.close();
		return costs;
	}
	public float f() {
		float costs = 0;
		float gain = 0;
		
		Cursor c1 = db.rawQuery("SELECT sum(money) FROM " + DBOpenHelper.db_table_costs, null);
		c1.moveToFirst();
		while (!c1.isAfterLast()) {
			costs += c1.getFloat(0);
			c1.moveToNext();
		}
		c1.close();
		
		Cursor c2 = db.rawQuery("SELECT sum(money) FROM " + DBOpenHelper.db_table_gain, null);
		c2.moveToFirst();
		while (!c2.isAfterLast()) {
			gain += c2.getFloat(0);
			c2.moveToNext();
		}
		c2.close();
		
		return (gain - costs);
	}

		///	User Part/////
//	public void createTask(Task task){
//		ContentValues values = new ContentValues();
//		values.put(DBOpenHelper.d_task_id, task.getId());
//		values.put(DBOpenHelper.d_task_name, task.getName());
//		values.put(DBOpenHelper.d_task_description, task.getDescription());
//		values.put(DBOpenHelper.d_task_order_id, task.getTaskOrderId());
//		
//		db.insert(DBOpenHelper.db_table_tasks, null, values);
//		List<Route> routes = task.getRoutes();
//		for(Route r : routes){
//			values = new ContentValues();
//			values.put(DBOpenHelper.d_routes_id, r.getId());
//			values.put(DBOpenHelper.d_routes_lat, r.getLat());
//			values.put(DBOpenHelper.d_routes_lng, r.getLng());
//			values.put(DBOpenHelper.d_routes_point_order, r.getPointOrder());
//			values.put(DBOpenHelper.d_routes_task_id, r.getTaskId());
//			try{
//				db.insert(DBOpenHelper.db_table_routes, null, values);
//			} catch (SQLException e) {
//				Log.e("SQL", e.getMessage());
//				
//			}
//		}
//	}
//	public void setUpdateTask(int task_id){
//		ContentValues values = new ContentValues();
//		values.put(DBOpenHelper.d_task_status, 1);
//		try{
//			db.update(DBOpenHelper.db_table_tasks, values, DBOpenHelper.d_task_id+" = "+ task_id, null);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public void createTask(int id, String name, String description) {
//		ContentValues values = new ContentValues();
////		db.delete(DBOpenHelper.db_table, DBOpenHelper.d_id + " = "+ 1, null);
//		values.put(DBOpenHelper.d_task_id, id);
//		values.put(DBOpenHelper.d_task_name, name);
//		values.put(DBOpenHelper.d_task_description, description);
//		try{
//			db.insert(DBOpenHelper.db_table_tasks, null, values);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public void createRoute(int id, String lat, String lng, int point_order, int task_id) {
//		ContentValues values = new ContentValues();
//		values.put(DBOpenHelper.d_routes_id, id);
//		values.put(DBOpenHelper.d_routes_lat, lat);
//		values.put(DBOpenHelper.d_routes_lng, lng);
//		values.put(DBOpenHelper.d_routes_point_order, point_order);
//		values.put(DBOpenHelper.d_routes_task_id, task_id);
//		db.insert(DBOpenHelper.db_table_routes, null, values);
//	}
//	public void createGasStation(GasStation gasStation){
//		ContentValues values = new ContentValues();
//		values.put(DBOpenHelper.d_gas_stations_id, gasStation.getId());
//		values.put(DBOpenHelper.d_gas_stations_name, gasStation.getName());
//		values.put(DBOpenHelper.d_gas_stations_lat, gasStation.getLat());
//		values.put(DBOpenHelper.d_gas_stations_lng, gasStation.getLng());
//		values.put(DBOpenHelper.d_gas_stations_description, gasStation.getDescription());
//		try {
//		db.insert(DBOpenHelper.db_table_gas_stations, null, values);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public void createWaterStation(WaterStation waterStation){
//		ContentValues values = new ContentValues();
//		values.put(DBOpenHelper.d_water_stations_id, waterStation.getId());
//		values.put(DBOpenHelper.d_water_stations_name, waterStation.getName());
//		values.put(DBOpenHelper.d_water_stations_lat, waterStation.getLat());
//		values.put(DBOpenHelper.d_water_stations_lng, waterStation.getLng());
//		values.put(DBOpenHelper.d_water_stations_description, waterStation.getDescription());
//		try {
//		db.insert(DBOpenHelper.db_table_water_stations, null, values);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public Task getTaskById(int id){
//		Task task = new Task();
//		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_tasks + " WHERE " + DBOpenHelper.d_task_id + " = " + id, null);
//		c.moveToFirst();
//		while (!c.isAfterLast()) {
//			task = new Task();
//			task.setId(c.getInt(0));
//			task.setName(c.getString(1));
//			task.setDescription(c.getString(2));
//			task.setTaskOrderId(c.getInt(3));
//			task.setStatus(c.getInt(4));
//			List<Route> routes;
//			routes = getRoutesByTaskId(task.getId());
//			task.setRoutes(routes);
//			c.moveToNext();
//		}
//		c.close();
//		return task;
//		
//	}
//	public ArrayList<Task> getTasks(){
//		ArrayList<Task> list = new ArrayList<Task>();
//		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_tasks, null);
//		c.moveToFirst();
//		Task task;
//		while (!c.isAfterLast()) {
//			task = new Task();
//			task.setId(c.getInt(0));
//			task.setName(c.getString(1));
//			task.setDescription(c.getString(2));
//			task.setTaskOrderId(c.getInt(3));
//			task.setStatus(c.getInt(4));
//			List<Route> routes;
//			routes = getRoutesByTaskId(task.getId());
//			task.setRoutes(routes);
//			list.add(task);
//			c.moveToNext();
//		}
//		c.close();
//		return list; 
//	}
//	public List<Route> getRoutesByTaskId(int id){
//		List<Route> list = new ArrayList<Route>();
//		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_routes + " WHERE " + DBOpenHelper.d_routes_task_id + " = " + id, null);
//		c.moveToFirst();
//		Route route;
//		while (!c.isAfterLast()) {
//			route = new Route();
//			route.setId(c.getInt(0));
//			route.setLat(Double.parseDouble(c.getString(1)));
//			route.setLng(Double.parseDouble(c.getString(2)));
//			route.setPointOrder(c.getInt(3));
//			route.setTaskId(id);
//			list.add(route);
//			c.moveToNext();
//		}
//		c.close();
//		return list;
//	}
//	public ArrayList<GasStation> getGasStations(){
//		ArrayList<GasStation> list = new ArrayList<GasStation>();
//		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_gas_stations, null);
//		c.moveToFirst();
//		GasStation gas;
//		while (!c.isAfterLast()) {
//			gas = new GasStation();
//			gas.setId(c.getInt(0));
//			gas.setName(c.getString(1));
//			gas.setLat(Double.parseDouble(c.getString(2)));
//			gas.setLng(Double.parseDouble(c.getString(3)));
//			gas.setDescription(c.getString(4));
//			list.add(gas);
//			c.moveToNext();
//		}
//		c.close();
//		return list; 
//	}
//	public ArrayList<WaterStation> getWaterStations(){
//		ArrayList<WaterStation> list = new ArrayList<WaterStation>();
//		Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.db_table_water_stations, null);
//		c.moveToFirst();
//		WaterStation water;
//		while (!c.isAfterLast()) {
//			water = new WaterStation();
//			water.setId(c.getInt(0));
//			water.setName(c.getString(1));
//			water.setLat(Double.parseDouble(c.getString(2)));
//			water.setLng(Double.parseDouble(c.getString(3)));
//			water.setDescription(c.getString(4));
//			list.add(water);
//			c.moveToNext();
//		}
//		c.close();
//		return list; 
//	}
//	public void deleteAllGasStations(){
//		try{
//			db.delete(DBOpenHelper.db_table_gas_stations, null, null);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public void deleteAllWaterStations(){
//		try{
//			db.delete(DBOpenHelper.db_table_water_stations, null, null);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
//	public void deleteTask(int id){
//		try{
//			db.delete(DBOpenHelper.db_table_tasks, DBOpenHelper.d_task_order_id + " = "+ id, null);
//		} catch (SQLException e) {
//			Log.e("SQL", e.getMessage());
//			
//		}
//	}
	
	
	
}
