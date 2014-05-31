package com.coinkeeper.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coinkeeper.classes.Budget;
import com.coinkeeper.db.Bridge;

public class BudgetActivity extends Activity {

	static final int AIM_DIALOG_ID = 999;// is needed for showing AIM dialog
	ActionBar actionBar;
	LayoutInflater inflater;
	MyAdapter adapter;
	Bridge b;
	ArrayList<Budget> budgetList;
	TextView currSum;
	float cSum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
		currSum = (TextView) findViewById(R.id.tvCurrentSum);
		
		
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar_budget, null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		ImageView back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		Button addBudget = (Button) findViewById(R.id.add_budget);
		addBudget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(AIM_DIALOG_ID);
			}
		});

		inflater = LayoutInflater.from(this);
		b = new Bridge(this);
		b.open();
		budgetList = b.getBudgetList();
		cSum = b.getCurrentSum();
		b.close();

		currSum.setText((int)cSum+"тг");
		Log.d("Size", budgetList.size() + "");

		adapter = new MyAdapter();
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AIM_DIALOG_ID:
			return new AddBudgetDialog(this);
		}
		return null;
	}

	public void refresh() {
		b.open();
		budgetList = b.getBudgetList();
		b.close();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		b.open();
		budgetList = b.getBudgetList();
		b.close();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return budgetList.size();
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

			if (convertView == null) {
				LayoutInflater li = (BudgetActivity.this).getLayoutInflater();
				v = li.inflate(R.layout.row_budget, null);
			} else {
				v = (View) convertView;
			}

			TextView name = (TextView) v.findViewById(R.id.name);
			TextView money = (TextView) v.findViewById(R.id.money);

			name.setText(budgetList.get(pos).getName());
			money.setText((int)budgetList.get(pos).getMoney() + "тг");

			float percent = getPaidPrecentage(budgetList.get(pos).getMoney(),
					budgetList.get(pos).getPaid());
			TextProgressBar textProgressBar = (TextProgressBar) v.findViewById(R.id.progressBar1);

			if (percent <= 33) {
				textProgressBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_green));
			} else if (percent > 33
					&& percent <= 66) {
				textProgressBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_color));

			} else if (percent > 66) {
				textProgressBar.setProgressDrawable(getResources().getDrawable(
						R.drawable.progress_red));
			}
			
			textProgressBar.setText(percent + "%");
			textProgressBar.setTextSize(28);
			textProgressBar.setProgress((int)percent);
			
			Log.d("budget", "id: "+budgetList.get(pos).getId()+", name: "+budgetList.get(pos).getName()+", money: " 
						+budgetList.get(pos).getMoney()+", paid: "+budgetList.get(pos).getPaid()+", status: "+budgetList.get(pos).getStatus()+", percent: "+percent);

			

			return v;
		}

		private float getPaidPrecentage(float money, float paid) {
			if (paid == 0) {
				return 0;
			} else
				return (float) (paid * 100 / money);
		}

	}

}
