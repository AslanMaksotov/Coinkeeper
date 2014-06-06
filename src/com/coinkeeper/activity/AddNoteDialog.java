package com.coinkeeper.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.coinkeeper.classes.Notes;
import com.coinkeeper.db.Bridge;

public class AddNoteDialog extends Dialog implements OnClickListener {

	static final int TIME_DIALOG_ID = 888;
	static final int DATE_DIALOG_ID = 777;

	private NotesActivity con;
	private EditText noteContent;

	private String note, sDate, sTime;

	private Button save, cancel, time, date;

	private int year, month, day;
	private int hour, minute, am_pm;

	private Bridge b;

	public AddNoteDialog(NotesActivity context) {
		super(context);
		this.con = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // to remove title bar on
														// the upper side
		setContentView(R.layout.dialog_addnotes);
		b = new Bridge(con);

		noteContent = (EditText) findViewById(R.id.note_content);

		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);

		time = (Button) findViewById(R.id.time);
		date = (Button) findViewById(R.id.date);

		time.setOnClickListener(this);
		date.setOnClickListener(this);

		save.setOnClickListener(this);
		cancel.setOnClickListener(this);

		clear();
		setCurrentTimeOnView();

	}

	private void setCurrentTimeOnView() {
		final Calendar c = Calendar.getInstance();

		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		String timeSet = "";
		if (hour > 12) {
			hour -= 12;
			timeSet = " PM";
		} else if (hour == 0) {
			hour += 12;
			timeSet = " AM";
		} else if (hour == 12){
			timeSet = " PM";
		}else{
			timeSet = " AM";
		}
		sTime =pad(hour) + "." + pad(minute)+timeSet;
		
		time.setText(sTime);

		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		sDate = pad(day) + "-" + pad(month+1) + "-" + pad(year);
		
		date.setText(sDate);
	}

	private void clear() {
		Log.d("Clear", "Worked");
		noteContent.setText("");
		setCurrentTimeOnView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time:
			con.show(TIME_DIALOG_ID, this);
			break;
		case R.id.date:
			con.show(DATE_DIALOG_ID, this);
			break;
		case R.id.save:
			if (check()) {
				b.open();
				Notes notes = getNotes();
				b.createNote(notes);
				b.close();
				
				con.refresh();
				clear();
				dismiss();
			} else {
				Toast.makeText(con, "Заполните поля!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		case R.id.cancel:
			clear();
			dismiss();
			break;

		}
	}

	private Notes getNotes() {
		if(sTime.isEmpty() && sDate.isEmpty()){
			setCurrentTimeOnView();
		}
		return new Notes(note, sTime, sDate);
	}

	public void setTime(String t) {
		this.sTime = t;
		time.setText(t);
	}

	public void setDate(String d) {
		this.sDate = d;
		date.setText(d);
	}

	private boolean check() {
		note = noteContent.getText().toString().trim();
		if (!note.isEmpty() || !note.equals("")) {
			return true;
		}
		return false;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}
