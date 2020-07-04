package dev.the01guy.goal_chase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import dev.the01guy.goal_chase.utility.Goal;
import dev.the01guy.goal_chase.utility.Utility;
import dev.the01guy.goal_chase.views.DeviceIdView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private SharedPreferences settings = null;
	private SharedPreferences.Editor editor = null;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// calling utility
		Utility utility = new Utility (this);
		utility.prepareFile();

		// install time actions
		this.settings = this.getSharedPreferences ("SETTINGS", MODE_PRIVATE);
		this.editor  = this.settings.edit();
		boolean firstTime = this.settings.getBoolean ("FIRST_RUN", false);

		if (!firstTime) {
			// do the thing for the first time
			this.editor.putBoolean ("FIRST_RUN", true);

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

		// setting up floating add button
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, EditGoalActivity.class));
			}
		});

		// setting up navigation drawer
		DrawerLayout drawerLayout = findViewById (R.id.navigation_drawer);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener (toggle);
		toggle.syncState();
		getSupportActionBar().setDisplayHomeAsUpEnabled (true);

		NavigationView navigationView = (NavigationView) findViewById (R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener (this);

		// set calculated hashed device id
		((TextView) ((View) navigationView.getHeaderView (0)).findViewById (R.id.device_id_hash)).setText (utility.getDeviceId());
		DeviceIdView deviceIdView = findViewById (R.id.device_id_image);

		// custom list-view of activities
		CustomArrayAdapter adapter = new CustomArrayAdapter (this, R.layout.activity_list_item, utility.getGoalsFromFile());
		ListView listView = findViewById (R.id.ActivityList);
		listView.setAdapter (adapter);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigate_dashboard:
				Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navigate_events:
				startActivity (new Intent (MainActivity.this, EventCalendarActivity.class));
				// Toast.makeText(this, "Events", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navigate_search:
				// Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
				startActivity (new Intent (MainActivity.this, ListGoalsActivity.class));
				break;
			case R.id.navigate_settings:
				Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navigate_progress:
				startActivity (new Intent (MainActivity.this, ProgressActivity.class));
				break;
			case R.id.navigate_exit:
				Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
				break;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected (@NonNull MenuItem item) {
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

		return super.onOptionsItemSelected (item);
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
			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View listItem = layoutInflater.inflate (R.layout.goal_list_item, parent, false);

			((TextView) listItem.findViewById (R.id.Name)).setText (this.goals.get(position).name);
			((ImageView) listItem.findViewById (R.id.Priority)).setImageResource (this.goals.get(position).priorityImageSource);
			((ImageView) listItem.findViewById (R.id.Status)).setImageResource (this.goals.get(position).statusImageSource);

			listItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditGoalActivity.class);
					intent.putExtra("Goal", goals.get(position));
					startActivity(intent);
				}
			});

			return listItem;
		}
	}
}