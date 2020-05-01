package com.example.goal_chase;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

class Utility {
	private File file = null;
	private Context context = null;

	Utility(Context context) {
		this.context = context;
	}

	String padLeftZeros(String string, int numberOfDigits) {
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
			while (fileReader.read() == -1) {
				fileText += (char) fileReader.read();
			}
			fileReader.close();
			Toast.makeText(context, "Reading Successful", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Reading Failed", Toast.LENGTH_SHORT).show();
		}

		return fileText;
	}

	void writeIntoFile(String fileText) {
		this.prepareFile();

		try {
			FileWriter fileWriter = new FileWriter(this.file);
			fileWriter.append(fileText);
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
		// String filePath = "/Goal_Chase/";
		String fileName = "goals.txt";

		// File directory = new File(Environment.getExternalStorageDirectory(), filePath);
		File directory = this.context.getExternalFilesDir("/");

		if (!directory.exists()) {
			if (!directory.mkdir()) {
				Toast.makeText(this.context, "Directory creation failed!", Toast.LENGTH_SHORT).show();
			}
		}

		this.file = new File(directory, fileName);

		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
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
}