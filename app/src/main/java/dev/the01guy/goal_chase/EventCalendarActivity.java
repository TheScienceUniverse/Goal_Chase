package dev.the01guy.goal_chase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.the01guy.goal_chase.views.CalendarView;

public class EventCalendarActivity extends AppCompatActivity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_event_calendar);

		CalendarView calendarView = (CalendarView) findViewById (R.id.calendar_view);
	}
}