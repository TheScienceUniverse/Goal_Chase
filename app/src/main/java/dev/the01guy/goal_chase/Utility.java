package dev.the01guy.goal_chase;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

class Utility {
	private File file = null;
	private Context context = null;

	Utility (Context context) {
		this.context = context;
	}

	String padLeftZeros (String string, int numberOfDigits) {
		String leftZeros = "";
		for (int i = 0; i < numberOfDigits - string.length(); i++) {
			leftZeros += "0";
		}
		return leftZeros + string;
	}

	String readFromFile() {
		String fileText = "";

		this.prepareFile();

		try {
			FileReader fileReader = new FileReader(file);
			int c;

			while ((c = fileReader.read())!=-1) {
				fileText += (char) c;
			}

			fileReader.close();
			Toast.makeText(context, "Reading Successful", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Reading Failed", Toast.LENGTH_SHORT).show();
		}

		return fileText;
	}

	void writeIntoFile (String fileText) {
		this.prepareFile();

		try {
			FileWriter fileWriter = new FileWriter(this.file, true);
			fileWriter.append(fileText);
			fileWriter.flush();
			fileWriter.close();
			Toast.makeText(context, "Writing Successful", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "Writing Failed", Toast.LENGTH_SHORT).show();
		}
	}

	void updateFile (Activity activity, String command) {
		List<Activity> activities = getActivitiesFromFile();
		int i;

		for (i = 0; i < activities.size(); i++) {
			if (activity.id == activities.get(i).id) {
				break;
			}
		}

		if (command.equals("Delete")) {
			if (activity.id != -1) {
				activities.remove(i);
			}
		} else {
			if (activity.id == -1) {
				activity.id = activities.size();
				activities.add(activity);
			} else {
				activities.set(i, activity);
			}
		}

		String fileText = "";
		for (i = 0; i < activities.size(); i++) {
			fileText += activities.get(i).id
				+ "\n" + activities.get(i).name
				+ "\n" + activities.get(i).startTime
				+ "\n" + activities.get(i).endTime
				+ "\n" + activities.get(i).priority
				+ "\n" + activities.get(i).status
				+ "\n";
		}

		this.prepareFile();
		try {
			FileWriter fileWriter = new FileWriter(this.file, false);
			fileWriter.write(fileText);
			fileWriter.flush();
			fileWriter.close();
			Toast.makeText(context, "Writing Successful", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "Writing Failed", Toast.LENGTH_SHORT).show();
		}
	}

	void syncWithFirebase() {
		Toast.makeText(this.context, "Goals Synchronized", Toast.LENGTH_SHORT).show();
	}

	void prepareFile() {
		String fileName = "goals.txt";
		File directory = this.context.getExternalFilesDir("/");

		if (!directory.exists()) {
			if (!directory.mkdir()) {
				Toast.makeText(this.context, "Directory creation failed!", Toast.LENGTH_SHORT).show();
			}
		}

		this.file = new File(directory, fileName);

		if (!this.file.exists()) {
			try {
				boolean result = this.file.createNewFile();
				Toast.makeText(this.context, "File creation success!", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(this.context, "File creation failed!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	String getDeviceInformation() {
		String uniqueId = "";

		uniqueId += "Serial: " + android.os.Build.SERIAL;
		uniqueId += "\nSecureId: " + Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
		uniqueId += "\nUUID: " + UUID.randomUUID().toString();

		return uniqueId;
	}

	List<Activity> getDummyActivityList() {
		List<Activity> activities = new ArrayList<Activity>();

		activities.add(new Activity(0,"Activity-1", "01/01/2000", "01/01/2000", "P1", "Pending"));
		activities.add(new Activity(1,"Activity-2", "01/01/2000", "01/01/2000", "P2", "Pending"));
		activities.add(new Activity(2,"Activity-3", "01/01/2000", "01/01/2000", "P3", "Suspended"));
		activities.add(new Activity(3,"Activity-4", "01/01/2000", "01/01/2000", "P4", "Suspended"));
		activities.add(new Activity(4,"Activity-5", "01/01/2000", "01/01/2000", "P5", "Completed"));
		activities.add(new Activity(5,"Activity-6", "01/01/2000", "01/01/2000", "P5", "Completed"));
		activities.add(new Activity(6,"Activity-7", "01/01/2000", "01/01/2000", "P5", "Completed"));
		activities.add(new Activity(7,"Activity-8", "01/01/2000", "01/01/2000", "P5", "Completed"));

		return  activities;
	}

	List<Activity> getActivitiesFromFile() {
		List<Activity> activities = new ArrayList<>();
		String string = readFromFile();
		int i = 0;

		String id = null;
		String name = null;
		String startTime = null;
		String endTime = null;
		String priority = null;
		String status = null;

		while (i < string.length()) {
			id = "";
			name = "";
			startTime = "";
			endTime = "";
			priority = "";
			status = "";

			while(string.charAt(i) != '\n') {
				id += string.charAt(i);
				++i;
			}
			++i;

			while(string.charAt(i) != '\n') {
				name += string.charAt(i);
				++i;
			}
			++i;

			while(string.charAt(i) != '\n') {
				startTime += string.charAt(i);
				++i;
			}
			++i;

			while(string.charAt(i) != '\n') {
				endTime += string.charAt(i);
				++i;
			}
			++i;

			while(string.charAt(i) != '\n') {
				priority += string.charAt(i);
				++i;
			}
			++i;

			while(string.charAt(i) != '\n') {
				status += string.charAt(i);
				++i;
			}
			++i;

			activities.add(new Activity(parseInt(id, 10), name, startTime, endTime, priority, status));
		}

		String msg = "";
		for (i = 0; i < activities.size(); i++) {
			msg += activities.get(i).id + " ";
		}
		Log.d("id", "" +  msg);

		return activities;
	}
}