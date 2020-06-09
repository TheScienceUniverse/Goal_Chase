package dev.the01guy.goal_chase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.the01guy.goal_chase.R;

import java.util.List;

import dev.the01guy.goal_chase.utility.*;

public class MainActivity extends AppCompatActivity {
	private SharedPreferences settings = null;
	private SharedPreferences.Editor editor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// install time actions
		this.settings = this.getSharedPreferences ("SETTINGS", MODE_PRIVATE);
		this.editor  = this.settings.edit();
		boolean firstTime = this.settings.getBoolean ("FIRST_RUN", false);

		if (!firstTime) {
			// do the thing for the first time
			editor.putBoolean ("FIRST_RUN", true);

			// read phone state permission check
			int permissionResult = ContextCompat.checkSelfPermission (this, Manifest.permission.READ_PHONE_STATE);

			if (permissionResult == PackageManager.PERMISSION_GRANTED) {
				this.editor.putBoolean ("READ_PHONE_STATE", true);
			} else {
				this.editor.putBoolean ("READ_PHONE_STATE", false);
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_PHONE_STATE}, 123);
			}

			permissionResult = this.checkCallingOrSelfPermission ("android.permission.READ_PRIVILEGED_PHONE_STATE");

			if (permissionResult == PackageManager.PERMISSION_GRANTED) {
				this.editor.putBoolean ("READ_PRIVILEGED_PHONE_STATE", true);
			} else {
				this.editor.putBoolean ("READ_PRIVILEGED_PHONE_STATE", false);
			}

			editor.apply();
		}

		// calling utility
		Utility utility = new Utility(this);
		utility.prepareFile();

		// setting up floating add button
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, EditActivity.class));
			}
		});

		Log.d ("Device Information", utility.getDeviceId());

		// custom list-view of activities
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

		CustomArrayAdapter(Context context, int layout, List<Activity> activities) {
			super(context, layout, activities);
			this.context = context;
			this.activities = activities;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View listItem = layoutInflater.inflate(R.layout.activity_list_item, parent, false);

			((TextView) listItem.findViewById (R.id.ActivityName)).setText (this.activities.get(position).name);
			((ImageView) listItem.findViewById (R.id.Priority)).setImageResource (this.activities.get(position).priorityImageSource);
			((ImageView) listItem.findViewById (R.id.Status)).setImageResource (this.activities.get(position).statusImageSource);

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

	@Override
	public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		// super.onRequestPermissionResult (requestCode, permissions, grantResults);

		if (requestCode == 123) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				this.editor.putBoolean ("READ_PHONE_STATE", true);
			} else {
				this.editor.putBoolean ("READ_PHONE_STATE", false);
				Toast.makeText (this, "Permission Denied", Toast.LENGTH_SHORT).show();
			}
		}

		this.editor.apply();
	}
}