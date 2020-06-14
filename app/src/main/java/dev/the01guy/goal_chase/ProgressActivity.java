package dev.the01guy.goal_chase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import dev.the01guy.goal_chase.utility.Activity;
import dev.the01guy.goal_chase.utility.Utility;
import dev.the01guy.goal_chase.views.ProgressView;

public class ProgressActivity extends AppCompatActivity {
	private int progress = 0;
	private List <Activity> activities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_progress);

		Utility utility = new Utility (this);
		this.activities = utility.getActivitiesFromFile();

		this.calculateProgress();

		ProgressView progressView = new ProgressView (this, this.progress);
		setContentView (progressView);
	}

	private void calculateProgress() {
		int total = activities.size();
		int completed = 0;

		for (int i = 0; i < total; i++) {
			completed += ((activities.get(i).status.equals("Completed")) ? 1 : 0);
		}

		this.progress = 100 * (completed / total);
	}
}