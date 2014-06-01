package com.coinkeeper.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.coinkeeper.activity.R;
import com.coinkeeper.classes.Category;
import com.coinkeeper.classes.Costs;
import com.coinkeeper.classes.Gain;
import com.coinkeeper.db.Bridge;
import com.coinkeeper.db.ImagesStore;

public class CalendarView extends Fragment {
	protected final Calendar calendar;
	private final Locale locale;
	private ViewSwitcher calendarSwitcher;
	private TextView currentMonth;
	private CalendarAdapter calendarAdapter;
	ListView listView;
	MyAdapter adapter;
	ArrayList<Gain> gainList;
	ArrayList<Costs> costList;
	Bridge b;
	public CalendarView() {
		calendar = Calendar.getInstance();
		locale = Locale.getDefault();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final RelativeLayout calendarLayout = (RelativeLayout)inflater.inflate(R.layout.calendar, null);
		final GridView calendarDayGrid = (GridView)calendarLayout.findViewById(R.id.calendar_days_grid);
		final GestureDetector swipeDetector = new GestureDetector(getActivity(), new SwipeGesture(getActivity()));
		final GridView calendarGrid = (GridView)calendarLayout.findViewById(R.id.calendar_grid);
		//comment = (TextView) calendarLayout.findViewById(R.id.comment);
		listView = (ListView) calendarLayout.findViewById(R.id.list);
		adapter = new MyAdapter();
		gainList = new ArrayList<Gain>();
		costList = new ArrayList<Costs>();
		listView.setAdapter(adapter);
		setListViewHeightBasedOnChildren(listView);
		calendarSwitcher = (ViewSwitcher)calendarLayout.findViewById(R.id.calendar_switcher);
		currentMonth = (TextView)calendarLayout.findViewById(R.id.current_month);
		calendarAdapter = new CalendarAdapter(getActivity(), calendar);
		updateCurrentMonth();
		b = new Bridge(getActivity());
		final TextView nextMonth = (TextView) calendarLayout.findViewById(R.id.next_month);
		nextMonth.setOnClickListener(new NextMonthClickListener());
		final TextView prevMonth = (TextView) calendarLayout.findViewById(R.id.previous_month);
		prevMonth.setOnClickListener(new PreviousMonthClickListener());
		calendarGrid.setOnItemClickListener(new DayItemClickListener());

		calendarGrid.setAdapter(calendarAdapter);
		calendarGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return swipeDetector.onTouchEvent(event);
			}
		});
		
		calendarGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final TextView dayView = (TextView)view.findViewById(R.id.date);
				final CharSequence text = dayView.getText();
				if (String.valueOf(text).trim().equals("")) return;
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = Integer.valueOf(String.valueOf(text).trim());
				String date = new StringBuilder().append(month+1).append("-").append(day).append("-").append(year).append(" ").toString();
				Log.d("Date", date);

				calendarAdapter.setSelected(year, month,day);
				gainList.clear();
				costList.clear();
				b.open();
				ArrayList<Gain> gList = b.getGainList();
				for(int i=0;i<gList.size();i++){
					Log.d(gList.get(i).getDate()+".", date+".");
					if (gList.get(i).getDate().equals(date)){
						gainList.add(gList.get(i));
					}
				}
				Log.d("GainList", gainList.size()+"");
				ArrayList<Costs> cList = b.getCostsList();
				for(int i=0;i<cList.size();i++){
					if (cList.get(i).getDate().equals(date)){
						costList.add(cList.get(i));
					}
				}
				Log.d("CostList", costList.size()+"");
				adapter = new MyAdapter();
				listView.setAdapter(adapter);
				setListViewHeightBasedOnChildren(listView);
				b.close();
			}
		});
		calendarDayGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.day_item, getResources().getStringArray(R.array.days_array)));
		return calendarLayout;
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    BaseAdapter baseAdapter = (BaseAdapter) listView.getAdapter();
	    if (baseAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < baseAdapter.getCount(); i++) {
	        view = baseAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (baseAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
	protected void updateCurrentMonth() {
		calendarAdapter.refreshDays();
		currentMonth.setText(String.format(locale, "%tB", calendar) + " " + calendar.get(Calendar.YEAR));
	}

	private final class DayItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		}
	}

	protected final void onNextMonth() {
		calendarSwitcher.setInAnimation(getActivity(), R.anim.in_from_right);
		calendarSwitcher.setOutAnimation(getActivity(), R.anim.out_to_left);
		calendarSwitcher.showNext();
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			calendar.set((calendar.get(Calendar.YEAR) + 1), Calendar.JANUARY, 1);
		} else {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		}
		updateCurrentMonth();
	}

	protected final void onPreviousMonth() {
		calendarSwitcher.setInAnimation(getActivity(), R.anim.in_from_left);
		calendarSwitcher.setOutAnimation(getActivity(), R.anim.out_to_right);
		calendarSwitcher.showPrevious();
		if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
			calendar.set((calendar.get(Calendar.YEAR) - 1), Calendar.DECEMBER, 1);
		} else {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		}
		updateCurrentMonth();
	}

	private final class NextMonthClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			onNextMonth();
		}
	}

	private final class PreviousMonthClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			onPreviousMonth();
		}
	}

	private final class SwipeGesture extends SimpleOnGestureListener {
		private final int swipeMinDistance;
		private final int swipeThresholdVelocity;

		public SwipeGesture(Context context) {
			final ViewConfiguration viewConfig = ViewConfiguration.get(context);
			swipeMinDistance = viewConfig.getScaledTouchSlop();
			swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
	            onNextMonth();
	        }  else if (e2.getX() - e1.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
	            onPreviousMonth();
	        }
	        return false;
		}
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("Size",(gainList.size()+costList.size())+"");
			return (gainList.size()+costList.size());
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
		        LayoutInflater li = (getActivity()).getLayoutInflater();
		        v = li.inflate(R.layout.row_gain, null);
		    }else{
		        v = (View)convertView;
		    }
	        ImageView category_icon = (ImageView) v.findViewById(R.id.category_icon);
	        TextView title = (TextView) v.findViewById(R.id.title);
	        TextView comments = (TextView) v.findViewById(R.id.comments);
	        TextView money = (TextView) v.findViewById(R.id.money);
	        TextView date = (TextView) v.findViewById(R.id.date);
	        if (pos<gainList.size()) {
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
	        } else {
	        	pos = pos - gainList.size();
	        	b.open();
		        Category cat = b.getCategoryById(costList.get(pos).getCategoryId());
		        b.close();
		        Log.d("ImageId", cat.getImage_id()+"");
		        category_icon.setBackgroundColor(getResources().getColor(R.color.red));
		        category_icon.setImageResource(ImagesStore.images[cat.getImage_id()]);
		        title.setText(costList.get(pos).getName());
		        comments.setText(costList.get(pos).getComments());
		        date.setText(costList.get(pos).getDate());
		        money.setText(costList.get(pos).getMoney()+"тг");
	        }
			return v;
		}
		
	}

}
