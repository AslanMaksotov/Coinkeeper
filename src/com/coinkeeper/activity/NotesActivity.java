package com.coinkeeper.activity;

import java.util.ArrayList;

import com.coinkeeper.classes.Notes;
import com.coinkeeper.db.Bridge;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NotesActivity extends Activity{
	static final int NOTE_DIALOG_ID = 999; // is needed for showing notes dialog
	
	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	SharedPreferences prefs;
	Bridge b;
	ArrayList<Notes> notesList;
	
	TextView date_time;
	
	int lang;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_notes);
		
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_notes, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView title = (TextView) view.findViewById(R.id.title);
        lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.notes);
		title.setText(language[lang]);
		
        ImageView back = (ImageView)view.findViewById(R.id.back);
        
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
        
        
        inflater = LayoutInflater.from(this);
        b = new Bridge(this);
        b.open();
        
        b.close();
        
        
        adapter = new MyAdapter();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        
        
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NOTE_DIALOG_ID:
			return new AddNoteDialog(this);
		}
		
		return null;
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 7;
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v;

		    if(convertView==null)
		    {
		        LayoutInflater li = (NotesActivity.this).getLayoutInflater();
		        v = li.inflate(R.layout.row_notes, null);
		    }else{
		        v = (View)convertView;
		    }
		    
		    
			return v;
		}
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
}
