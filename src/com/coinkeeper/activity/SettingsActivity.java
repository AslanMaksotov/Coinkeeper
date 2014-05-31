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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity{
	ActionBar actionBar;
	LayoutInflater inflater;
	SharedPreferences prefs;
	TextView choose, title;
	CheckBox lang_kaz, lang_rus;
	int lang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences("com.coinkeeper.activity", Context.MODE_PRIVATE);
		setContentView(R.layout.activity_settings);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_settings, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        title = (TextView) view.findViewById(R.id.title);
        lang = prefs.getInt("com.coinkeeper.language", 1);
		String[] language = getResources().getStringArray(R.array.settings);
		title.setText(language[lang]);
		
        ImageView back = (ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        choose = (TextView) findViewById(R.id.choose);
        language = getResources().getStringArray(R.array.chooselang);
        choose.setText(language[lang]);
        lang_kaz = (CheckBox) findViewById(R.id.lang_kaz);
        lang_rus = (CheckBox) findViewById(R.id.lang_rus);
        if (lang==0) lang_kaz.setChecked(true); else lang_rus.setChecked(true);
        lang_kaz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lang_rus.setChecked(false);
				prefs.edit().putInt("com.coinkeeper.language", 0).commit();
				String[] language = getResources().getStringArray(R.array.chooselang);
				choose.setText(language[0]);
			}
		});
        lang_rus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lang_kaz.setChecked(false);
				prefs.edit().putInt("com.coinkeeper.language", 1).commit();
				String[] language = getResources().getStringArray(R.array.chooselang);
				choose.setText(language[1]);
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
}
