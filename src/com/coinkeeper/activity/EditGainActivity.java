package com.coinkeeper.activity;

import java.util.StringTokenizer;

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

import com.coinkeeper.classes.Category;
import com.coinkeeper.classes.Gain;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class EditGainActivity extends Activity implements OnClickListener{
	
	static final int DATE_DIALOG_ID = 999;  // is needed for showing date in datepicker
	static final int CATEGORY_DIALOG_ID = 888;
	CategoryDialog categoryDialog;
	ActionBar actionBar;
	LayoutInflater inflater;
	Button delete, edit, changeData;
	ImageView addCategory;
	EditText money, name, comment;
	TextView category_name;
	Spinner spinnerCount, spinnerType;
	CheckBox repeat;
	LinearLayout content;
	TextView tvDisplayDate;
	DatePicker dpResult;
	private int year;
	private int month;
	private int day;
	int id;
	int categoryId;
	Bridge b;
	String[] countArray, typeArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editgain);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_gain1, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = (ImageView)view.findViewById(R.id.gainicon);
        imageView.setBackground(getResources().getDrawable(R.drawable.edit));
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Изменить доход");
		countArray = new String[50];
		for(int i=0;i<50;i++) countArray[i] = (i+1)+""; //filling array with numbers from 1 to 50, will be used in spinner of repeat part
		typeArray = new String[4];
		typeArray[0] = "День дни";
		typeArray[1] = "Неделя (-и)";
		typeArray[2] = "Месяц (ы)";
		typeArray[3] = "Год (-ы)";
		
		id = getIntent().getExtras().getInt("id");
		
		b = new Bridge(this);
		delete = (Button) findViewById(R.id.delete); // Button to cancel addition of gain
		edit  = (Button) findViewById(R.id.edit); // Button to save data and add to SQLite database
		addCategory = (ImageView) findViewById(R.id.add_category);
		changeData = (Button) findViewById(R.id.change_data);
		
		delete.setOnClickListener(this);	// setting click listener, make events when button is clicked
		edit.setOnClickListener(this);
		addCategory.setOnClickListener(this);
		changeData.setOnClickListener(this);
		money = (EditText) findViewById(R.id.money);
		name = (EditText) findViewById(R.id.name);
		comment = (EditText) findViewById(R.id.comment);
		category_name = (TextView) findViewById(R.id.category_name);
		addCategory.setImageResource(ImagesStore.images[0]);
		addCategory.setBackgroundColor(getResources().getColor(R.color.green));
		repeat = (CheckBox) findViewById(R.id.repeat);
		repeat.setOnClickListener(this);
		content = (LinearLayout) findViewById(R.id.content);
		spinnerCount = (Spinner) findViewById(R.id.count);
		spinnerType = (Spinner) findViewById(R.id.type);
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countArray);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCount.setAdapter(dataAdapter1);
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeArray);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(dataAdapter2);
		b.open();
		Gain gain = b.getGainById(id);
		Category category = b.getCategoryById(gain.getCategoryId());
		categoryId = category.getId();
		Log.d("Id", id+"");
		Log.d("Category", category.getName());
		b.close();
		addCategory.setImageResource(ImagesStore.images[category.getImage_id()]);
		money.setText(gain.getMoney()+"тг");
		name.setText(gain.getName());
		comment.setText(gain.getComments());
		category_name.setText(category.getName());
		if (gain.isRepeat()) {
			repeat.setChecked(true);
			content.setVisibility(View.VISIBLE);
			spinnerCount.setSelection(gain.getCount()-1);
			spinnerType.setSelection(gain.getType());
		} else {
			content.setVisibility(View.GONE);
		}
		setCurrentDateOnView(gain.getDate());
	}	
	// display current date
		public void setCurrentDateOnView(String date) {
	 
			tvDisplayDate = (TextView) findViewById(R.id.date);
			//dpResult = (DatePicker) findViewById(R.id.);
			Log.d("Date", date);
			StringTokenizer token = new StringTokenizer(date, "-");
			month = Integer.parseInt(token.nextToken().toString().trim());
			day = Integer.parseInt(token.nextToken().toString().trim());
			year = Integer.parseInt(token.nextToken().toString().trim());
	 
			// set current date into textview
			tvDisplayDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));
	 
			// set current date into datepicker
			//dpResult.init(year, month, day, null);
	 
		}
	public void setCategory(int id, String name, int imageId){
		category_name.setText(name);
		categoryId = id;
		addCategory.setImageResource(ImagesStore.images[imageId]);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(delete)) {
			b.open();
			b.deleteGain(id);
			b.close();
			onBackPressed();
		} else
		if (v.equals(addCategory)){
			showDialog(CATEGORY_DIALOG_ID);
		} else
		if (v.equals(changeData)){
			showDialog(DATE_DIALOG_ID);
		} else
		if (v.equals(repeat)){
			if (repeat.isChecked()){
				content.setVisibility(View.VISIBLE);
				Log.d("Visibility","Visible");
			} else {
				content.setVisibility(View.GONE);
				Log.d("Visibility","Gone");
			}
		} else
		if (v.equals(edit)){  // if save button is pressed
			float sMoney = 0;
			String temp = money.getText().toString();
			temp = temp.substring(0, temp.length()-2);
			Log.d("Without tg", temp);
			if (!temp.isEmpty()) sMoney = (float) Float.parseFloat(temp);
			String sName = name.getText().toString(); 
			String sComment = comment.getText().toString();
			String sDate = tvDisplayDate.getText().toString();
			int sCount = Integer.parseInt(String.valueOf(spinnerCount.getSelectedItem())); // getting from spinner selected number
			int sType  = (int) spinnerType.getSelectedItemId(); // getting from spinner selected type id
			if (!sName.isEmpty() && sMoney!=0 && !sComment.isEmpty()) {
				b.open();
				Log.d("CategoryId", categoryId+"");
				Gain gain = new Gain(sMoney, categoryId, sName, sDate, sComment, repeat.isChecked(), sCount, sType);
				b.editGainById(gain, id);
				b.close();
				onBackPressed();
			} else {
				Toast.makeText(EditGainActivity.this, "Пожалуйста заполните пустые поля!", Toast.LENGTH_LONG).show();
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
			categoryDialog =  new CategoryDialog(this, 1);
			categoryDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					setCategory(categoryDialog.categoryId, categoryDialog.categoryName, categoryDialog.imageId);
				}
			});
			return categoryDialog;
			
		}
		return null;
	}
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
		year = selectedYear;
		month = selectedMonth;
		day = selectedDay;
		
		// set selected date into textview
		tvDisplayDate.setText(new StringBuilder().append(month + 1)
		   .append("-").append(day).append("-").append(year)
		   .append(" "));
		
		// set selected date into datepicker also
		//dpResult.init(year, month, day, null);
		
		}
	};
}
