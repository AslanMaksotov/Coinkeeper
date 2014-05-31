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
import com.coinkeeper.classes.Costs;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class CostsActivity extends Activity{
	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	Bridge b;
	ArrayList<Costs> costsList;
	SharedPreferences prefs;
	int lang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_costs);
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		actionBar = getActionBar();
		
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_costs, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView title = (TextView) view.findViewById(R.id.title);
        lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.costs);
		title.setText(language[lang]);
        ImageView back = (ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        
        Button addCosts = (Button) findViewById(R.id.add_costs);
        addCosts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CostsActivity.this, AddCostsActivity.class);
				startActivity(intent);
			}
		});
        
        
        inflater = LayoutInflater.from(this);
        
        b = new Bridge(this);
        b.open();
        
        costsList = b.getCostsList();
        Log.d("Size",costsList.size()+"");
        b.close();
        
        
        Log.d("Size", costsList.size()+"");
        
        adapter = new MyAdapter();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Intent intent = new Intent(CostsActivity.this, EditCostsActivity.class);
				intent.putExtra("id", costsList.get(pos).getId());
				startActivity(intent);
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		b.open();
        costsList = b.getCostsList();
        b.close();
		adapter.notifyDataSetChanged();
	}
	
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return costsList.size();
		}

		@Override
		public Object getItem(int pos) {
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
		        LayoutInflater li = (CostsActivity.this).getLayoutInflater();
		        v = li.inflate(R.layout.row_costs, null);
		    }else{
		        v = (View)convertView;
		    }
		    b.open();
	        Category cat = b.getCategoryById(costsList.get(pos).getCategoryId());
	        b.close();
	        
	        ImageView category_icon = (ImageView) v.findViewById(R.id.category_icon);
	        TextView title = (TextView) v.findViewById(R.id.title);
	        TextView comments = (TextView) v.findViewById(R.id.comments);
	        TextView money = (TextView) v.findViewById(R.id.money);
	        TextView date = (TextView) v.findViewById(R.id.date);
	        category_icon.setBackgroundColor(getResources().getColor(R.color.red));
	        category_icon.setImageResource(ImagesStore.images[cat.getImage_id()]);
	        title.setText(costsList.get(pos).getName());
	        comments.setText(costsList.get(pos).getComments());
	        date.setText(costsList.get(pos).getDate());
	        money.setText(costsList.get(pos).getMoney()+"тг");
	        
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
