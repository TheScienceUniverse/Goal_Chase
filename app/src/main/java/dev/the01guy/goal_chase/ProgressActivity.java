package dev.the01guy.goal_chase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import dev.the01guy.goal_chase.utility.Goal;
import dev.the01guy.goal_chase.utility.Utility;
import dev.the01guy.goal_chase.views.ProgressView;

public class ProgressActivity extends AppCompatActivity {
	private int progress = 0;
	private List <Goal> goals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_progress);

		Utility utility = new Utility (this);
		this.goals = utility.getGoalsFromFile();

		this.calculateProgress();

		ProgressView progressView = new ProgressView (this, this.progress);
		setContentView (progressView);
	}

	private void calculateProgress() {
		int total = goals.size();

		if (total == 0) {
			this.progress = 0;
		} else {
			int completed = 0;
			for (int i = 0; i < total; i++) {
				completed += ((this.goals.get(i).status.equals("Completed")) ? 1 : 0);
			}

			this.progress = (100 * completed) / total;
		}
	}
}