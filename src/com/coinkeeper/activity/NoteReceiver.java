package com.coinkeeper.activity;

import com.coinkeeper.classes.Notes;
import com.coinkeeper.db.Bridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NoteReceiver extends BroadcastReceiver {
	private Bridge b;

	@Override
	public void onReceive(Context context, Intent intent) {
		//NoteAlarmService alarm = new NoteAlarmService();
		b = new Bridge(context);
		int noteId = intent.getIntExtra("noteId", -1);
		
		b.open();
		Notes notes = b.getNoteById(noteId);
		b.close();
		Intent service1 = new Intent(context, NoteAlarmService.class);
		
		Log.d("noteId", noteId + "");
		service1.putExtra("content", notes.getContent());
		//alarm.setMsgContent(notes.getContent());
		
		context.startService(service1);

	}

}
