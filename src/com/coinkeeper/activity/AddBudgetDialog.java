package com.coinkeeper.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coinkeeper.classes.Budget;
import com.coinkeeper.db.Bridge;

public class AddBudgetDialog extends Dialog implements OnClickListener{
	BudgetActivity con;
	EditText name,money,paid;
	Button save, cancel;
	
	String sName;
	
	float fMoney, fPaid;
	
	
	Bridge b;
	
	public AddBudgetDialog(BudgetActivity context) {
		super(context);
		con = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  // to remove title bar on the upper side
		setContentView(R.layout.dialog_addbudget);
		b = new Bridge(con);
		name = (EditText) findViewById(R.id.name);
		money = (EditText) findViewById(R.id.money);
		paid = (EditText) findViewById(R.id.paid);
		clear();
		
		save = (Button)findViewById(R.id.save);
		cancel = (Button)findViewById(R.id.cancel);
		
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	private void clear() {
		Log.d("Clear","Worked");
		name.setText("");
		money.setText("");
		paid.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
				if(check()){
					
					b.open();
					
					Budget budget = new Budget();
					budget.setName(sName);
					budget.setMoney(fMoney);
					budget.setPaid(fPaid);
					
					b.createBudget(budget);
					
					b.close();
					con.refresh();
					clear();
					dismiss();
				}else{
					Toast.makeText(con, "Заполните поля!", Toast.LENGTH_LONG).show();
				}
			break;

		case R.id.cancel:
				clear();
				dismiss();
			break;
		}
		
		
	}
	
	
	
	private boolean check() {
		sName = name.getText().toString().trim();
		String sMoney = money.getText().toString().trim();
		
		if(!(sName.isEmpty() || sMoney.isEmpty())){
			fMoney = Float.parseFloat(sMoney);
			String sPaid = paid.getText().toString().trim();
			
			if(!sPaid.isEmpty()){
				fPaid = Float.parseFloat(sPaid);
			}else{
				fPaid = 0;
			}	
			return true;
		}
		
		
		return false;
	}
	
	

}
