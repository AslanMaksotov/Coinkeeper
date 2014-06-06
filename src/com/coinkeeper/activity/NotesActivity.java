package com.coinkeeper.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coinkeeper.classes.Notes;
import com.coinkeeper.db.Bridge;

public class NotesActivity extends Activity {
	static final int NOTE_DIALOG_ID = 999; // is needed for showing notes dialog
	static final int TIME_DIALOG_ID = 888;
	static final int DATE_DIALOG_ID = 777;
	private AddNoteDialog con;
	private long currentTime;

	private ArrayList<PendingIntent> intentArray;
	private PendingIntent pi;

	private AlarmManager alarmManager;
	private int day, month, year, hours, minute;

	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	SharedPreferences prefs;
	Bridge b;
	ArrayList<Notes> notesList;

	TextView date_time;

	int lang;

	String sDate, sTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);

		currentTime = System.currentTimeMillis();

		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		intentArray = new ArrayList<PendingIntent>();
		cancelAlarms();

		prefs = getSharedPreferences("com.coinkeeper.activity",
				Context.MODE_PRIVATE);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar_notes, null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		TextView title = (TextView) view.findViewById(R.id.title);
		lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.notes);
		title.setText(language[lang]);

		ImageView back = (ImageView) view.findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		Button addNote = (Button) findViewById(R.id.add_note);
		addNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(NOTE_DIALOG_ID);
			}
		});

		final Calendar c = Calendar.getInstance();

		hours = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);

		inflater = LayoutInflater.from(this);
		b = new Bridge(this);
		b.open();
		notesList = b.getNoteList();
		b.close();

		setAlarmManager();

		adapter = new MyAdapter();
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						NotesActivity.this);
				alertDialogBuilder.setTitle("Удалить");
				alertDialogBuilder
						.setMessage(
								"Вы действительно хотите удалить этот заметки!")
						.setCancelable(false)
						.setPositiveButton("Да",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										b.open();
										b.deleteNote(notesList.get(position)
												.getId());
										notesList = b.getNoteList();
										b.close();
										adapter.notifyDataSetChanged();
										// dialog.cancel();
									}
								})
						.setNegativeButton("Нет",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				return false;
			}
		});

	}

	public void refresh() {
		b.open();
		cancelAlarms();
		notesList = b.getNoteList();
		b.close();
		setAlarmManager();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		b.open();
		cancelAlarms();
		notesList = b.getNoteList();
		b.close();
		setAlarmManager();
		adapter.notifyDataSetChanged();

	}

	public void show(int id, AddNoteDialog con) {
		Log.d("dialog id", id + "");
		this.con = con;

		switch (id) {
		case TIME_DIALOG_ID:
			showDialog(id);
			break;

		case DATE_DIALOG_ID:
			showDialog(id);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NOTE_DIALOG_ID:
			return new AddNoteDialog(this);

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hours,
					minute, false);

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}

		return null;
	}

	private void setAlarmManager() {
		currentTime = System.currentTimeMillis();
		cancelAlarms();
		for (int pos = 0; pos < notesList.size(); pos++) {

			Calendar calendar = getDateAndTime(notesList.get(pos));
			if ((calendar.getTimeInMillis() - currentTime) > 0) {
				Intent myIntent = new Intent(NotesActivity.this,
						NoteReceiver.class);

				myIntent.putExtra("noteId", notesList.get(pos).getId());

				pi = PendingIntent.getBroadcast(NotesActivity.this, pos,
						myIntent, 0);
				alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
						pi);
				intentArray.add(pi);
			}

		}

	}

	private Calendar getDateAndTime(Notes notes) {
		Calendar calendar = Calendar.getInstance();
		String ampm = "";
		int hours = 0, minute = 0, day = 0, month = 0, year = 0;

		String time = notes.getTime();
		StringTokenizer stTime = new StringTokenizer(time, ". ");

		if (stTime.hasMoreElements()) {
			String shours = stTime.nextElement().toString();
			String sminute = stTime.nextElement().toString();
			hours = Integer.parseInt(shours);
			minute = Integer.parseInt(sminute);
			ampm = stTime.nextElement().toString();
			Log.d("calendar date", shours + "." + sminute + " " + ampm);
		}

		String date = notes.getDate();
		StringTokenizer stDate = new StringTokenizer(date, "-");

		if (stDate.hasMoreElements()) {
			String sday = stDate.nextElement().toString();
			String smonth = stDate.nextElement().toString();
			String syear = stDate.nextElement().toString();

			Log.d("calendar date", sday + "-" + smonth + "-" + syear);

			day = Integer.parseInt(sday);
			month = Integer.parseInt(smonth) - 1;
			year = Integer.parseInt(syear);
		}

		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, day);

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);

		if (ampm.equals("AM")) {
			calendar.set(Calendar.AM_PM, Calendar.AM);
		} else if (ampm.equals("PM")) {
			calendar.set(Calendar.AM_PM, Calendar.PM);
		}

		return calendar;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return notesList.size();
		}

		@Override
		public Object getItem(int pos) {
			return null;
		}

		@Override
		public long getItemId(int pos) {
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v;

			if (convertView == null) {
				LayoutInflater li = (NotesActivity.this).getLayoutInflater();
				v = li.inflate(R.layout.row_notes, null);
			} else {
				v = (View) convertView;
			}

			TextView tvNotes = (TextView) v.findViewById(R.id.notes);
			TextView tvTime = (TextView) v.findViewById(R.id.time);
			TextView tvDate = (TextView) v.findViewById(R.id.date);

			tvNotes.setText(notesList.get(pos).getContent());
			tvTime.setText(notesList.get(pos).getTime());
			tvDate.setText(notesList.get(pos).getDate());

			return v;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hours = selectedHour;
			minute = selectedMinute;

			String timeSet = "";
			if (hours > 12) {
				hours -= 12;
				timeSet = " PM";
			} else if (hours == 0) {
				hours += 12;
				timeSet = " AM";
			} else if (hours == 12) {
				timeSet = " PM";
			} else {
				timeSet = " AM";
			}
			sTime = pad(hours) + "." + pad(minute) + timeSet;
			con.setTime(sTime);
		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			sDate = pad(day) + "-" + pad(month + 1) + "-" + pad(year);

			con.setDate(sDate);
		}
	};

	private void cancelAlarms() {
		if (intentArray.size() > 0) {
			for (int i = 0; i < notesList.size(); i++) {
				alarmManager.cancel(intentArray.get(i));
			}
			intentArray.clear();
		}
	}
}
