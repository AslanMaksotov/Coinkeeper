package com.coinkeeper.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticsActivity extends Activity{
	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	SharedPreferences prefs;
	int lang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		setContentView(R.layout.activity_statistics);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_statistics, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView title = (TextView) view.findViewById(R.id.title);
        lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.statistics);
		title.setText(language[lang]);
        ImageView back = (ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        
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
		        LayoutInflater li = (StatisticsActivity.this).getLayoutInflater();
		        v = li.inflate(R.layout.row_gain, null);
		    }else{
		        v = (View)convertView;
		    }
		    
			
	        
	        ImageView category_icon = (ImageView) v.findViewById(R.id.category_icon);
	        TextView title = (TextView) v.findViewById(R.id.title);
	        TextView comments = (TextView) v.findViewById(R.id.comments);
	        TextView money = (TextView) v.findViewById(R.id.money);
	        TextView date = (TextView) v.findViewById(R.id.date);
	        
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
