package dev.the01guy.goal_chase.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Utility {
	private File file = null;
	private Context context = null;
	private String deviceId = "";

	public Utility (Context context) {
		this.context = context;
	}

	public String padLeftZeros (String string, int numberOfDigits) {
		String leftZeros = "";
		for (int i = 0; i < numberOfDigits - string.length(); i++) {
			leftZeros += "0";
		}
		return leftZeros + string;
	}

	public String readFromFile() {
		String fileText = "";

		this.prepareFile();

		try {
			FileReader fileReader = new FileReader(file);
			int c;

			while ((c = fileReader.read()) != -1) {
				fileText += (char) c;
			}

			fileReader.close();
			// Toast.makeText(context, "Reading Successful", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(context, "Reading Failed", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		return fileText;
	}

	public void writeIntoFile(String fileText) {
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

	public void updateFile(Goal goal, String command) {
		List<Goal> goals = getGoalsFromFile();
		int i;

		for (i = 0; i < goals.size(); i++) {
			if (goal.id == goals.get(i).id) {
				break;
			}
		}

		if (command.equals("Delete")) {
			if (goal.id != -1) {
				goals.remove(i);
			}
		} else {
			if (goal.id == -1) {
				goal.id = goals.size();
				goals.add (goal);
			} else {
				goals.set(i, goal);
			}
		}

		String fileText = "";
		for (i = 0; i < goals.size(); i++) {
			fileText += goals.get(i).id
					+ "\n" + goals.get(i).name
					+ "\n" + goals.get(i).startTime
					+ "\n" + goals.get(i).endTime
					+ "\n" + goals.get(i).priority
					+ "\n" + goals.get(i).status
					+ "\n";
		}

		this.prepareFile();
		try {
			FileWriter fileWriter = new FileWriter(this.file, false);
			fileWriter.write(fileText);
			fileWriter.flush();
			fileWriter.close();
			// Toast.makeText(context, "Writing Successful", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(context, "Writing Failed", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void syncWithFirebase() {
		Toast.makeText(this.context, "Goals Synchronized", Toast.LENGTH_SHORT).show();
	}

	public void prepareFile() {
		String fileName = "goals.txt";
		File directory = new File(this.context.getExternalFilesDir("/") + "/" + this.getDeviceId() + "/");

		if (!directory.exists()) {
			if (!directory.mkdir()) {
				Toast.makeText(this.context, "Directory creation failed!", Toast.LENGTH_SHORT).show();
			}
		}

		this.file = new File(directory, fileName);

		if (!this.file.exists()) {
			try {
				boolean result = this.file.createNewFile();
				// Toast.makeText(this.context, "File creation success!", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(this.context, "File creation failed!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	public String getDeviceId() {
		DeviceInformation deviceInformation = new DeviceInformation (context);
		Hash hash = new Hash();
		this.deviceId = hash.sha256 (deviceInformation.deviceId);
		// Toast.makeText(this.context, this.deviceId, Toast.LENGTH_SHORT).show();
		return this.deviceId;
	}

	public List<Goal> getDummyActivityList() {
		List<Goal> goals = new ArrayList<>();

		goals.add(new Goal (0,"Activity-1", "01/01/2000", "01/01/2000", "P1", "Pending"));
		goals.add(new Goal (1,"Activity-2", "01/01/2000", "01/01/2000", "P2", "Pending"));
		goals.add(new Goal (2,"Activity-3", "01/01/2000", "01/01/2000", "P3", "Suspended"));
		goals.add(new Goal (3,"Activity-4", "01/01/2000", "01/01/2000", "P4", "Suspended"));
		goals.add(new Goal (4,"Activity-5", "01/01/2000", "01/01/2000", "P5", "Completed"));
		goals.add(new Goal (5,"Activity-6", "01/01/2000", "01/01/2000", "P5", "Completed"));
		goals.add(new Goal (6,"Activity-7", "01/01/2000", "01/01/2000", "P5", "Completed"));
		goals.add(new Goal (7,"Activity-8", "01/01/2000", "01/01/2000", "P5", "Completed"));

		return  goals;
	}

	public List<Goal> getGoalsFromFile() {
		List<Goal> goals = new ArrayList<>();
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

			goals.add (new Goal (parseInt (id, 10), name, startTime, endTime, priority, status));
		}

		String msg = "";
		for (i = 0; i < goals.size(); i++) {
			msg += goals.get(i).id + " ";
		}
		Log.d("id", "" +  msg);

		return goals;
	}
}