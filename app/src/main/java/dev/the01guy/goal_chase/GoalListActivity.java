package dev.the01guy.goal_chase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dev.the01guy.goal_chase.utility.Activity;
import dev.the01guy.goal_chase.utility.Goal;
import dev.the01guy.goal_chase.utility.Utility;

public class GoalListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal_list);

		Utility utility = new Utility (this);

		CustomArrayAdapter adapter = new CustomArrayAdapter (this, R.layout.goal_list_item, utility.cloneGoalsFromActivities (utility.getActivitiesFromFile()));
		ListView listView = findViewById (R.id.GoalList);
		listView.setAdapter (adapter);
	}

	class CustomArrayAdapter extends ArrayAdapter<Goal> {
		Context context;
		List<Goal> goals;

		CustomArrayAdapter(Context context, int layout, List<Goal> goals) {
			super(context, layout, goals);
			this.context = context;
			this.goals = goals;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			View listItem = layoutInflater.inflate (R.layout.goal_list_item, parent, false);

			((TextView) listItem.findViewById (R.id.Name)).setText (this.goals.get(position).name);
			((ImageView) listItem.findViewById (R.id.Priority)).setImageResource (this.goals.get(position).priorityImageSource);
			((ImageView) listItem.findViewById (R.id.Status)).setImageResource (this.goals.get(position).statusImageSource);

			listItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent (context, EditActivity.class);
					intent.putExtra ("Activity", goals.get (position));
					startActivity (intent);
				}
			});

			return listItem;
		}
	}
}