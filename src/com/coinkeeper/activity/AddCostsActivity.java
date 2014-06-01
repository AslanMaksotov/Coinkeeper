package com.coinkeeper.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coinkeeper.classes.Budget;
import com.coinkeeper.classes.Costs;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class AddCostsActivity extends Activity implements OnClickListener {
	static final int DATE_DIALOG_ID = 999; // is needed for showing date in datepicker
	static final int CATEGORY_DIALOG_ID = 888;
	CategoryDialog categoryDialog;
	ActionBar actionBar;
	LayoutInflater inflater;
	Button cancel, save, changeData;
	
	ImageView addCategory, addBudget;
	
	EditText money, name, comment;
	TextView category_name, date;
	Spinner spinnerCount, spinnerType,spinnerBudget;
	LinearLayout content; // this one
	CheckBox repeat;
	TextView tvDisplayDate;
	DatePicker dpResult;
	private int year;
	private int month;
	private int day;

	int categoryId;

	Bridge b;
	String[] countArray, typeArray, budgetArray;
	int [] budgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_addcosts);
		Log.d("check", "it's worked!!!");
		
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_costs1, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = (ImageView)view.findViewById(R.id.costicon);
        imageView.setBackground(getResources().getDrawable(R.drawable.edit));
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Добавить расход");
        
		countArray = new String[50];
		for (int i = 0; i < 50; i++)
			countArray[i] = (i + 1) + ""; // filling array with numbers from 1
											// to 50, will be used in spinner of
											// repeat part
		typeArray = new String[4];
		typeArray[0] = "День дни";
		typeArray[1] = "Неделя (-и)";
		typeArray[2] = "Месяц (ы)";
		typeArray[3] = "Год (-ы)";


		b = new Bridge(this);
		
		b.open();
			ArrayList<Budget> list = b.getBudgetList();
			//Log.d("list size", list.size()+"");
			budgetArray = new String [list.size()+1];
			budgetArray[0] = "Другое";
			
			budgetId = new int[list.size()+1];
			budgetId[0] = -1;
			
			for(int i = 0; i < list.size() ; i++ ){
				budgetArray[i+1] = list.get(i).getName();
				budgetId[i+1] = list.get(i).getId();
			}
		b.close();
		
		
		cancel = (Button) findViewById(R.id.cancel);
		save = (Button) findViewById(R.id.save);
		
		addCategory = (ImageView) findViewById(R.id.add_category);
		addBudget = (ImageView) findViewById(R.id.add_budget);
		
		
		changeData = (Button) findViewById(R.id.change_data);
		
		addCategory.setImageResource(ImagesStore.images[0]);
		
		addCategory.setBackgroundColor(getResources().getColor(R.color.red));
		
		cancel.setOnClickListener(this); // setting click listener, make events
											// when button is clicked
		save.setOnClickListener(this);
		
		addCategory.setOnClickListener(this);
		
		changeData.setOnClickListener(this);

		money = (EditText) findViewById(R.id.money);
		name = (EditText) findViewById(R.id.name);
		comment = (EditText) findViewById(R.id.comment);
		category_name = (TextView) findViewById(R.id.category_name);
		
		setCurrentDateOnView();
		repeat = (CheckBox) findViewById(R.id.repeat);
		repeat.setOnClickListener(this);

		content = (LinearLayout) findViewById(R.id.content);
		content.setVisibility(View.GONE);

		spinnerCount = (Spinner) findViewById(R.id.count);
		spinnerType = (Spinner) findViewById(R.id.type);
		spinnerBudget = (Spinner) findViewById(R.id.spBudget);
		
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countArray);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCount.setAdapter(dataAdapter1);
		
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeArray);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(dataAdapter2);
		
		ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, budgetArray);
		dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBudget.setAdapter(dataAdapter3);
		
	}
	public void setCategory(int id, String name, int imageId){
		category_name.setText(name);
		categoryId = id;
		addCategory.setImageResource(ImagesStore.images[imageId]);
	}
	private void setCurrentDateOnView() {
		tvDisplayDate = (TextView) findViewById(R.id.date);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		tvDisplayDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(cancel)) {
			onBackPressed();
		} else if (v.equals(changeData)) {
			showDialog(DATE_DIALOG_ID);
		} else if (v.equals(addCategory)) {
			showDialog(CATEGORY_DIALOG_ID);
		} else if (v.equals(repeat)) {
			if (repeat.isChecked()) {
				content.setVisibility(View.VISIBLE);
				Log.d("Visibility", "Visible");
			} else {
				content.setVisibility(View.GONE);
				Log.d("Visibility", "Gone");
			}
		} else if (v.equals(save)) {
			float sMoney = 0;
			String temp;
			temp = money.getText().toString();
			if (!temp.isEmpty()) sMoney = Float.parseFloat(temp);

			String sName = name.getText().toString();
			String sComment = comment.getText().toString();
			String sDate = tvDisplayDate.getText().toString();
			int sCount = Integer.parseInt(String.valueOf(spinnerCount .getSelectedItem()));// getting from spinner selected number
			int sType = (int) spinnerType.getSelectedItemId();// getting from spinner selected type id
			
			int sBudgetId = (int) spinnerBudget.getSelectedItemId(); // getting from spinner selected budget id
			
			if (!sName.isEmpty() && sMoney != 0 && !sComment.isEmpty() && categoryId!=-1){
				b.open();
				Costs costs = new Costs(sMoney, categoryId, sName, sDate, sComment, repeat.isChecked(), sCount, sType, budgetId[sBudgetId]);
				b.createCosts(costs);
				b.close();
				onBackPressed();
			} else {
				Toast.makeText(AddCostsActivity.this, "Пожалуйста заполните пустые поля!", Toast.LENGTH_LONG).show();
			}
		
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, year, month,day);
		case CATEGORY_DIALOG_ID:
			categoryDialog =  new CategoryDialog(this, 2);
			categoryDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					setCategory(categoryDialog.categoryId, categoryDialog.categoryName, CategoryDialog.imageId);
				}
			});
			return categoryDialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			// set selected date into textview
			tvDisplayDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

			// set selected date into datepicker also
			// dpResult.init(year, month, day, null);

		}
	};

}
