package com.coinkeeper.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coinkeeper.classes.Category;
import com.coinkeeper.classes.Gain;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class GainActivity extends Activity{
	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	Bridge b;
	ArrayList<Gain> gainList;
	SharedPreferences prefs;
	int lang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		setContentView(R.layout.activity_gain);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_gain, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView title = (TextView) view.findViewById(R.id.title);
        lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.gain);
		title.setText(language[lang]);
        ImageView back = (ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        Button addGain = (Button) view.findViewById(R.id.add_gain);
        addGain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GainActivity.this, AddGainActivity.class);
				startActivity(intent);
			}
		});

        inflater = LayoutInflater.from(this);
        b = new Bridge(this);
        b.open();
        gainList = b.getGainList();
        b.close();
        Log.d("Size", gainList.size()+"");
        adapter = new MyAdapter();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GainActivity.this, EditGainActivity.class);
				intent.putExtra("id", gainList.get(pos).getId());
				startActivity(intent);
			}
        	
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		b.open();
        gainList = b.getGainList();
        b.close();
		adapter.notifyDataSetChanged();
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gainList.size();
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
		        LayoutInflater li = (GainActivity.this).getLayoutInflater();
		        v = li.inflate(R.layout.row_gain, null);
		    }else{
		        v = (View)convertView;
		    }
	        ImageView category_icon = (ImageView) v.findViewById(R.id.category_icon);
	        TextView title = (TextView) v.findViewById(R.id.title);
	        TextView comments = (TextView) v.findViewById(R.id.comments);
	        TextView money = (TextView) v.findViewById(R.id.money);
	        TextView date = (TextView) v.findViewById(R.id.date);
	        
	        b.open();
	        Category cat = b.getCategoryById(gainList.get(pos).getCategoryId());
	        b.close();
	        Log.d("ImageId", cat.getImage_id()+"");
	        category_icon.setBackgroundColor(getResources().getColor(R.color.green));
	        category_icon.setImageResource(ImagesStore.images[cat.getImage_id()]);
	        title.setText(gainList.get(pos).getName());
	        comments.setText(gainList.get(pos).getComments());
	        date.setText(gainList.get(pos).getDate());
	        money.setText(gainList.get(pos).getMoney()+"тг");
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

