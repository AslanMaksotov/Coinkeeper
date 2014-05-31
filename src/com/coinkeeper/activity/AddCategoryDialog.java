package com.coinkeeper.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.coinkeeper.classes.Category;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class AddCategoryDialog extends Dialog  implements OnClickListener{
	Context con;
	EditText name;
	static ImageView icon;
	Button cancel, save;
	static int selectedIcon = 0;
	Bridge b;
	int type;
	CategoryDialog parent;
	public AddCategoryDialog(Context context, CategoryDialog p, int type) {
		super(context);
		con = context;
		parent = p;
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_addcategory);  // to remove title bar on the upper side
		b = new Bridge(con);
		name = (EditText) findViewById(R.id.name);
		icon = (ImageView) findViewById(R.id.icon);
		cancel = (Button) findViewById(R.id.cancel);
		save = (Button) findViewById(R.id.save);
		cancel.setOnClickListener(this);
		save.setOnClickListener(this);
		icon.setOnClickListener(this);
		icon.setImageResource(ImagesStore.images[selectedIcon]);
		if (type==1) icon.setBackgroundColor(con.getResources().getColor(R.color.green)); else
		if (type==2) icon.setBackgroundColor(con.getResources().getColor(R.color.red));
	}
	public static void changeIcon(int id){
		selectedIcon = id;
		icon.setImageResource(ImagesStore.images[selectedIcon]);
	}
	@Override
	public void onClick(View v) {
		if (v.equals(cancel)) {
			dismiss();
		} else
		if (v.equals(save)){
			if (!name.getText().toString().isEmpty()) {
				b.open();
				Category category = new Category();
				category.setImage_id(selectedIcon);
				category.setName(name.getText().toString());
				category.setType(type);
				b.createCategory(category);
				b.close();
				parent.refresh();
				dismiss();
			} else {
				Toast.makeText(con, "Пожалуйста заполните пустые поля!", Toast.LENGTH_LONG).show();
			}
		} else
		if (v.equals(icon)){
			Intent intent = new Intent(con, ShowIconsActivity.class);
			con.startActivity(intent);
		}
	}
}
