package com.example.goal_chase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
	private String string = "";
	private Utility utility = new Utility(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Edit Activity");
		actionBar.setDisplayHomeAsUpEnabled(true);

		this.setListeners();
	}

	void setListeners() {
		findViewById(R.id.SetStartTime).setOnClickListener(this);
		findViewById(R.id.SetEndTime).setOnClickListener(this);
		findViewById(R.id.Reset).setOnClickListener(this);
		findViewById(R.id.Save).setOnClickListener(this);
		findViewById(R.id.Cancel).setOnClickListener(this);
		findViewById(R.id.Delete).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.SetStartTime:
				pickDateTime(R.id.SetStartTime);
				break;
			case R.id.SetEndTime:
				pickDateTime(R.id.SetEndTime);
				break;
			case R.id.Reset:
				reset();
				break;
			case R.id.Save:
				save();
				break;
			case R.id.Cancel:
				cancel();
				break;
			case R.id.Delete:
				delete();
				break;
			default:
				break;
		}
	}

	void reset() {
		Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
	}

	void save() {
		if (dataReady()) {
			this.utility.writeIntoFile(
				"\n\nName: " + ((EditText)findViewById(R.id.EditName)).getText().toString()
				+ "\nStartTime: " + ((TextView)findViewById(R.id.StartTime)).getText().equals("YYYY/MM/DD HH:MM:SS")
				+ "\nEndTine: " + ((TextView)findViewById(R.id.EndTime)).getText().equals("YYYY/MM/DD HH:MM:SS")
			);
			// Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Data not ready", Toast.LENGTH_SHORT).show();
		}
	}

	void cancel() {
		Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
	}

	void delete() {
		Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
	}

	void pickDateTime(int id) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd hh:mm:ss");
		this.string = simpleDateFormat.format(date);

		int year = parseInt(this.string.substring(0, 4));
		int month = parseInt(this.string.substring(5, 7));
		int day = parseInt(this.string.substring(8, 10));
		int hour = parseInt(this.string.substring(11, 13));
		int minute = parseInt(this.string.substring(14, 16));
		int second = parseInt(this.string.substring(17, 19));

		if (id == R.id.SetStartTime) {
			new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					TextView textView = findViewById(R.id.StartTime);
					textView.append(" " + hourOfDay + ":" + minute);
				}
			}, hour, minute, true).show();

			new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					TextView textView = findViewById(R.id.StartTime);
					textView.setText(year + "/" + month + "/" + dayOfMonth);
				}
			}, year, month, day).show();
		} else if (id == R.id.SetEndTime) {
			new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					TextView textView = findViewById(R.id.EndTime);
					textView.append(" " + hourOfDay + ":" + minute);
				}
			}, hour, minute, true).show();

			new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					TextView textView = findViewById(R.id.EndTime);
					textView.setText(year + "/" + month + "/" + dayOfMonth);
				}
			}, year, month, day).show();
		}
	}

	boolean dataReady() {
		return !((EditText)findViewById(R.id.EditName)).getText().toString().equals("")
			&& !((TextView)findViewById(R.id.StartTime)).getText().equals("YYYY/MM/DD HH:MM:SS")
			&& !((TextView)findViewById(R.id.EndTime)).getText().equals("YYYY/MM/DD HH:MM:SS");
	}
}