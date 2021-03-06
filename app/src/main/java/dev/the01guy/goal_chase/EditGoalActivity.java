package dev.the01guy.goal_chase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.the01guy.goal_chase.utility.Goal;
import dev.the01guy.goal_chase.utility.Utility;

import static java.lang.Integer.parseInt;

public class EditGoalActivity extends AppCompatActivity implements View.OnClickListener {
	private String string = "";
	private Utility utility = new Utility(this);
	private Goal goal = null;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_edit_goal);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle ("Edit Activity");
		actionBar.setDisplayHomeAsUpEnabled(true);

		this.goal = (Goal) getIntent().getSerializableExtra ("Goal");

		if (this.goal == null) {
			this.goal = new Goal (-1, "", "", "", "", "");
		} else {
			this.setValues();
		}

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

	void reset() {}

	void save() {
		if (this.dataReady()) {
			if (this.goal.id == -1) {
				this.utility.updateFile (this.goal, "Add");
			} else {
				this.utility.updateFile (this.goal, "Update");
			}

			startActivity(new Intent (EditGoalActivity.this, MainActivity.class));
		} else {
			Toast.makeText(this, "Data not ready", Toast.LENGTH_SHORT).show();
		}
	}

	void cancel() {
		startActivity (new Intent(this, MainActivity.class));
	}

	void delete() {
		if (this.dataReady() && this.goal.id != -1) {
			this.utility.updateFile(this.goal, "Delete");
		}

		startActivity(new Intent(EditGoalActivity.this, MainActivity.class));
	}

	void pickDateTime(int id) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd hh:mm:ss", Locale.US);
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
					textView.append(
							" " + utility.padLeftZeros("" + hourOfDay, 2)
							+ ":" + utility.padLeftZeros("" + minute, 2)
					);
				}
			}, hour, minute, true).show();

			new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					TextView textView = findViewById(R.id.StartTime);
					string = utility.padLeftZeros("" + year, 4)
							+ "/" + utility.padLeftZeros("" + month, 2)
							+ "/" + utility.padLeftZeros("" + dayOfMonth, 2);
					textView.setText(string);
				}
			}, year, month, day).show();
		} else if (id == R.id.SetEndTime) {
			new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					TextView textView = findViewById(R.id.EndTime);
					textView.append(
							" " + utility.padLeftZeros("" + hourOfDay, 2)
							+ ":" + utility.padLeftZeros("" + minute, 2)
					);
				}
			}, hour, minute, true).show();

			new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					TextView textView = findViewById(R.id.EndTime);
					string = utility.padLeftZeros("" + year, 4)
							+ "/" + utility.padLeftZeros("" + month, 2)
							+ "/" + utility.padLeftZeros("" + dayOfMonth, 2);
					textView.setText(string);
				}
			}, year, month, day).show();
		}
	}

	void getValues() {
		this.goal.name = ((EditText)findViewById(R.id.EditName)).getText().toString();
		this.goal.startTime = ((TextView)findViewById(R.id.StartTime)).getText().toString();
		this.goal.endTime = ((TextView)findViewById(R.id.EndTime)).getText().toString();
		this.goal.priority = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.SetPriority)).getCheckedRadioButtonId())).getText().toString();
		this.goal.status = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.SetStatus)).getCheckedRadioButtonId())).getText().toString();
	}

	void setValues() {
		((EditText) findViewById (R.id.EditName)).setText (this.goal.name);
		((TextView) findViewById (R.id.StartTime)).setText (this.goal.startTime);
		((TextView) findViewById (R.id.EndTime)).setText (this.goal.endTime);
		((RadioButton) findViewById (this.goal.getRadioButtonId (this.goal.priority))).setChecked (true);
		((RadioButton) findViewById (this.goal.getRadioButtonId (this.goal.status))).setChecked (true);
	}

	boolean dataReady() {
		this.getValues();

		return !this.goal.name.equals("")
			&& !this.goal.startTime.equals("YYYY/MM/DD HH:MM:SS")
			&& !this.goal.endTime.equals("YYYY/MM/DD HH:MM:SS")
			&& !this.goal.priority.equals("")
			&& !this.goal.status.equals("");
	}
}