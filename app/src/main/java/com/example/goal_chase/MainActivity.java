package com.example.goal_chase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Utility utility = new Utility(this);
		utility.prepareFile();

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, EditActivity.class));
			}
		});

		CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.activity_list_item, utility.getActivitiesFromFile());
		ListView listView = findViewById(R.id.ActivityList);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
				break;
			case R.id.copy:
				Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
				break;
			case R.id.download:
				Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	class CustomArrayAdapter extends ArrayAdapter<Activity> {
		Context context;
		List<Activity> activities;

		public CustomArrayAdapter(Context context, int layout, List<Activity> activities) {
			super(context, layout, activities);
			this.context = context;
			this.activities = activities;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View listItem = layoutInflater.inflate(R.layout.activity_list_item, parent, false);

			((TextView)listItem.findViewById(R.id.ActivityName)).setText(this.activities.get(position).name);
			((ImageView)listItem.findViewById(R.id.Priority)).setImageResource(this.activities.get(position).priorityImageSource);
			((ImageView)listItem.findViewById(R.id.Status)).setImageResource(this.activities.get(position).statusImageSource);

			listItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditActivity.class);
					intent.putExtra("Activity", activities.get(position));
					startActivity(intent);
				}
			});

			return listItem;
		}
	}
}