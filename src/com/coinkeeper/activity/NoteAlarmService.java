package com.coinkeeper.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NoteAlarmService extends Service {
	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		/*
		 * }
		 * 
		 * @SuppressWarnings("static-access")
		 * 
		 * @Override public void onStart(Intent intent, int startId) {
		 * super.onStart(intent, startId);
		 */
		
		try {
			//msgContent = getMsgContent();
			String msgContent = intent.getExtras().getString("content");

			mManager = (NotificationManager) this.getApplicationContext()
					.getSystemService(
							this.getApplicationContext().NOTIFICATION_SERVICE);

			Intent intent1 = new Intent(this.getApplicationContext(),
					NotesActivity.class);// ShowNoteActivity.class
			Notification notification = new Notification(R.drawable.coins,
					msgContent, System.currentTimeMillis());
			intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
					this.getApplicationContext(), 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(this.getApplicationContext(),
					"Финансовый менеджер", msgContent, pendingNotificationIntent);

			mManager.notify(0, notification);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);

	}

}
