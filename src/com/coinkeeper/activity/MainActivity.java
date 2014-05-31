package com.coinkeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ScrollView;

import com.coinkeeper.classes.Category;
import com.coinkeeper.db.Bridge;

public class MainActivity extends Activity implements OnTouchListener{
	Button gains, costs, budget, statistics, notes, calendar, settings;
	Bridge b;
	int lang;
	String[] language;
	SharedPreferences prefs; // for storing settings in device. ex current language
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
		scrollView.requestDisallowInterceptTouchEvent(false);
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		gains = (Button) findViewById(R.id.gains);
		gains.setOnTouchListener(this);
		
		costs = (Button) findViewById(R.id.costs);
		costs.setOnTouchListener(this);
		
		budget = (Button) findViewById(R.id.budget);
		budget.setOnTouchListener(this);
		
		statistics = (Button) findViewById(R.id.statistics);
		statistics.setOnTouchListener(this);
		
		notes = (Button) findViewById(R.id.notes);
		notes.setOnTouchListener(this);
		
		settings = (Button) findViewById(R.id.settings);
		settings.setOnTouchListener(this);
		
		calendar = (Button) findViewById(R.id.calendar);
		calendar.setOnTouchListener(this);
		
		b = new Bridge(this);
		b.open();
		if (!b.existsCategory()){
			Category cat = new Category(0, "Акции", 1);
			b.createCategory(cat);
			cat = new Category(13, "Арендная плата", 1);
			b.createCategory(cat);
			cat = new Category(19, "Ежегодная рента", 1);
			b.createCategory(cat);
			cat = new Category(8, "Зарплата", 1);
			b.createCategory(cat);
			cat = new Category(15, "Личные сбережения", 1);
			b.createCategory(cat);
			cat = new Category(23, "Пенсия", 1);
			b.createCategory(cat);
			cat = new Category(0, "Авиабилеты", 2);
			b.createCategory(cat);
			cat = new Category(4, "Автомобиль", 2);
			b.createCategory(cat);
			cat = new Category(13, "Гостиница/аренда жилья", 2);
			b.createCategory(cat);
			cat = new Category(14, "Другое", 2);
			b.createCategory(cat);
			cat = new Category(20, "Здоровье и фитнес", 2);
			b.createCategory(cat);
			cat = new Category(2, "Медицинские услуги", 2);
			b.createCategory(cat);
			cat = new Category(6, "Одежда", 2);
			b.createCategory(cat);
		}
		b.close();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		changeLanguage();
	}
	public void changeLanguage(){
		lang = prefs.getInt("com.coinkeeper.language", 1);
		language = getResources().getStringArray(R.array.gain);
		gains.setText(language[lang]);
		language = getResources().getStringArray(R.array.costs);
		costs.setText(language[lang]);
		language = getResources().getStringArray(R.array.budget);
		budget.setText(language[lang]);
		language = getResources().getStringArray(R.array.statistics);
		statistics.setText(language[lang]);
		language = getResources().getStringArray(R.array.notes);
		notes.setText(language[lang]);
		language = getResources().getStringArray(R.array.settings);
		settings.setText(language[lang]);
		language = getResources().getStringArray(R.array.calendar);
		calendar.setText(language[lang]);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	@Override
	public boolean onTouch(View view, MotionEvent e) {
		// TODO Auto-generated method stub
		Intent intent;
		ScaleAnimation scale = new ScaleAnimation((float)1.0, (float)0.9, (float)1.0, (float)0.9, (float)50, (float)50);
		scale.setFillAfter(true);
		scale.setDuration(200);
		ScaleAnimation scale2 = new ScaleAnimation((float)0.9, (float)1.0, (float)0.9, (float)1.0, (float)50, (float)50);
		scale2.setFillAfter(true);
		scale2.setDuration(200);
		if (view.getId()==R.id.gains) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				gains.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				gains.startAnimation(scale2);
				intent = new Intent(MainActivity.this, GainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else 
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				gains.startAnimation(scale2);
			}
		} else
		if (view.getId()==R.id.costs) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				costs.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				costs.startAnimation(scale2);
				intent = new Intent(MainActivity.this, CostsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else 
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				costs.startAnimation(scale2);
			}
		} else
		if (view.getId()==R.id.budget) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				budget.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				budget.startAnimation(scale2);
				intent = new Intent(MainActivity.this, BudgetActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				budget.startAnimation(scale2);
			}
		} else
		if (view.getId()==R.id.statistics) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				statistics.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				statistics.startAnimation(scale2);
				intent = new Intent(MainActivity.this, StatisticsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				statistics.startAnimation(scale2);
			}
		} else 
		if (view.getId()==R.id.notes) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				notes.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				notes.startAnimation(scale2);
				intent = new Intent(MainActivity.this, NotesActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				notes.startAnimation(scale2);
			}
		} else
		if (view.getId()==R.id.settings) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				settings.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				settings.startAnimation(scale2);
				intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				settings.startAnimation(scale2);
			}
		} else
		if (view.getId()==R.id.calendar) {
			if (e.getAction()==MotionEvent.ACTION_DOWN){
				calendar.startAnimation(scale);
			} else
			if (e.getAction()==MotionEvent.ACTION_UP){
				calendar.startAnimation(scale2);
				intent = new Intent(MainActivity.this, CalendarView.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			} else
			if (e.getAction()==MotionEvent.ACTION_CANCEL){
				calendar.startAnimation(scale2);
			}
		}
		return true;
	}
}
