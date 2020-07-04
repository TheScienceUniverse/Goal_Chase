package dev.the01guy.goal_chase.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.the01guy.goal_chase.R;
import dev.the01guy.goal_chase.utility.Goal;
import dev.the01guy.goal_chase.utility.Utility;

import static java.lang.Integer.parseInt;

public class CalendarView extends LinearLayout {
	private ImageButton calendarBackward, calendarForward;
	private TextView calendarYear, calendarMonth;
	private GridView daysView;

	private static final int MAXIMUM_CALENDAR_DAYS = 42;

	Calendar calendar = Calendar.getInstance();
	Context context;
	List<Date> dates = new ArrayList<>();
	Utility utility = new Utility (this.getContext());
	List<Goal> goals = utility.getGoalsFromFile();

	public CalendarView (Context context) {
		super(context);
		this.context = context;
		this.setupElements();
		this.setupListeners();
		this.setupCalendar();
	}

	public CalendarView (Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setupElements();
		this.setupListeners();
		this.setupCalendar();
	}

	private void setupElements() {
		LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate (R.layout.event_calendar, this);
		this.calendarBackward = findViewById (R.id.calendar_backward);
		this.calendarYear = findViewById (R.id.calendar_year);
		this.calendarMonth = findViewById (R.id.calendar_month);
		this.calendarForward = findViewById (R.id.calendar_forward);
		this.daysView = findViewById (R.id.calendar_days);
	}

	private void setupListeners() {
		this.calendarBackward.setOnClickListener (new OnClickListener() {
			@Override
			public void onClick (View v) {
				calendar.add (Calendar.MONTH, -1);
				setupCalendar();
			}
		});

		this.calendarForward.setOnClickListener (new OnClickListener() {
			@Override
			public void onClick (View v) {
				calendar.add (Calendar.MONTH, 1);
				setupCalendar();
			}
		});
	}

	private void setupCalendar() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMMMM", Locale.ENGLISH);
		String dateString = simpleDateFormat.format (this.calendar.getTime());
		this.calendarYear.setText (dateString.substring (0, 4));
		this.calendarMonth.setText (dateString.substring (4));

		Calendar monthCalendar = (Calendar) this.calendar.clone();
		monthCalendar.set (Calendar.DAY_OF_MONTH, 1);

		int firstDayOfMonth = monthCalendar.get (Calendar.DAY_OF_WEEK) - 1;
		monthCalendar.add (Calendar.DAY_OF_MONTH, -firstDayOfMonth);

		dates.clear();
		while (dates.size() < MAXIMUM_CALENDAR_DAYS) {
			dates.add (monthCalendar.getTime());
			monthCalendar.add (Calendar.DAY_OF_MONTH, 1);
		}

		List<Goal> events = new ArrayList<>();
		GridAdapter gridAdapter = new GridAdapter (context, dates, calendar, events);
		daysView.setAdapter (gridAdapter);
	}

	class GridAdapter extends ArrayAdapter {
		List<Date> dates;
		Calendar currentDate;
		List<Goal> events;
		LayoutInflater inflater;

		public GridAdapter (@NonNull Context context, List<Date> dates, Calendar currentDate, List<Goal> events) {
			super (context, R.layout.event_calendar_day);

			this.dates = dates;
			this.currentDate = currentDate;
			this.events = events;
			this.inflater = LayoutInflater.from (context);
		}

		@NonNull
		@Override
		public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			Date monthDate = dates.get (position);
			Calendar dateCalendar = Calendar.getInstance();
			dateCalendar.setTime (monthDate);
			int dayNumber = dateCalendar.get (Calendar.DAY_OF_MONTH);
			int displayMonth = dateCalendar.get (Calendar.MONTH) + 1;
			int displayYear = dateCalendar.get (Calendar.YEAR);

			int currentMonth = this.currentDate.get (Calendar.MONTH) + 1;
			int currentYear = this.currentDate.get (Calendar.YEAR);

			View view = convertView;
			if (view == null) {
				view = this.inflater.inflate (R.layout.event_calendar_day, parent, false);
			}

			if (displayMonth == currentMonth && displayYear == currentYear) {
				view.setBackgroundColor (getContext().getResources().getColor (R.color.white));
			} else {
				view.setBackgroundColor (Color.parseColor ("#CCCCCC"));
			}

			TextView textView = view.findViewById (R.id.calendar_day);
			textView.setText (String.valueOf (dayNumber));

			boolean[] goalDays = getDaysByMonthHavingGoals (currentMonth, currentYear);

			if (goalDays [dayNumber - 1] && displayMonth == currentMonth && displayYear == currentYear) {
				int sdk = Build.VERSION.SDK_INT;

				if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
					view.setBackgroundDrawable (ContextCompat.getDrawable (getContext(), R.drawable.border_red));
				} else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
					view.setBackground(getResources().getDrawable (R.drawable.border_red));
				}
			}

			return view;
		}

		@Override
		public int getCount() {
			return this.dates.size();
		}

		@Override
		public int getPosition (@Nullable Object item) {
			return this.dates.indexOf (item);
		}

		@Nullable
		@Override
		public Object getItem (int position) {
			return this.dates.get (position);
		}

		boolean[] getDaysByMonthHavingGoals (int month, int year) {
			String year_month = utility.padLeftZeros (Integer.toString (year), 4)
					+ "/" + utility.padLeftZeros (Integer.toString (month), 2);
			Goal goal;
			boolean[] goalDays = new boolean[31];

			for (int i = 0; i < goals.size(); i++) {
				goal = goals.get (i);

				if (goal.startTime.substring (0, 7).equals (year_month)) {
					goalDays [parseInt (goal.startTime.substring (8, 10)) - 1] = true;
				}

				if (goal.endTime.substring (0, 7).equals (year_month)) {
					goalDays [parseInt (goal.endTime.substring (8, 10)) - 1] = true;
				}
			}

			return goalDays;
		}
	}
}