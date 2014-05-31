package com.coinkeeper.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coinkeeper.classes.Category;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class CategoryDialog extends Dialog{
	static final int ADDCATEGORY_DIALOG_ID = 777;
	Button add_category;
	RelativeLayout layout;
	static int categoryId, imageId;
	static String categoryName;
	int type;
	ArrayList<Category> categoryList;
	Context con;
	MyAdapter adapter;
	Bridge b;
	public CategoryDialog(Context context, int type) {
		super(context);
		con = context;
		this.type = type;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		categoryId = - 1;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_categories);
		layout = (RelativeLayout) findViewById(R.id.background);
		if (type==1) layout.setBackground(con.getResources().getDrawable(R.color.green)); else
		if (type==2) layout.setBackground(con.getResources().getDrawable(R.color.red));
		
		add_category = (Button) findViewById(R.id.add_category);
		add_category.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View v) {
            	showDialog(ADDCATEGORY_DIALOG_ID);
            }
          });
		b = new Bridge(con);
		b.open();
		categoryList = b.getCategories(type);
		Log.d("Size", categoryList.size()+"");
		b.close();
		ListView list = (ListView) findViewById(R.id.category_list);
		adapter = new MyAdapter(con);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				categoryList.get(pos);
				categoryId = categoryList.get(pos).getId();
				categoryName = categoryList.get(pos).getName();
				imageId = categoryList.get(pos).getImage_id();
				dismiss();
			}
		});
	}

	public void showDialog(int id) {
			Log.d("Dialog must", "show");
			new AddCategoryDialog(con, this, type).show();
	}
	public void refresh(){
		b.open();
		categoryList = b.getCategories(type);
		//Log.d("Size", categoryList.size()+"");
		b.close();
		adapter.notifyDataSetChanged();
	}
	private class MyAdapter extends BaseAdapter{
		Context con;
		public MyAdapter(Context context){
			con = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v;

		    if(convertView==null)
		    {
		        LayoutInflater li =  (CategoryDialog.this).getLayoutInflater();
		        v = li.inflate(R.layout.row_category, null);
		    }else{
		        v = (View)convertView;
		    }
		    ImageView image = (ImageView) v.findViewById(R.id.category_image);
		    TextView name = (TextView) v.findViewById(R.id.category_name);
		    name.setText(categoryList.get(pos).getName());
		    if (type==1)image.setBackgroundColor(con.getResources().getColor(R.color.green)); else
		    if (type==2)image.setBackgroundColor(con.getResources().getColor(R.color.red)); 
		    image.setImageResource(ImagesStore.images[categoryList.get(pos).getImage_id()]);
			return v;
		}
	}
}