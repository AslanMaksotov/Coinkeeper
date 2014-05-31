package com.coinkeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.coinkeeper.db.ImagesStore;

public class ShowIconsActivity extends Activity {
	GridView gv;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showicons);
		gv = (GridView) findViewById(R.id.gridView1);
		gv.setAdapter(new CustomAdapter(this));
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				AddCategoryDialog.changeIcon(pos);
				onBackPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class CustomAdapter extends BaseAdapter {

		String[] result;
		Context context;
		int[] imageId;
		private LayoutInflater inflater = null;

		public CustomAdapter(ShowIconsActivity activity) {
			// TODO Auto-generated constructor stub
			context = activity;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ImagesStore.images.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class Holder {
			ImageView img;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = new Holder();
			View rowView;

			rowView = inflater.inflate(R.layout.icon_list, null);
			holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
			holder.img.setImageResource(ImagesStore.images[position]);
			
			return rowView;
		}
	}
}
