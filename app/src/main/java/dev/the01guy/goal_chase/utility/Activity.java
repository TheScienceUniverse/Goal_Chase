package dev.the01guy.goal_chase.utility;

import com.the01guy.goal_chase.R;

import java.io.Serializable;

public class Activity implements Serializable {
	public int id;
	public String name;
	public String startTime;
	public String endTime;
	public String priority;
	public String status;
	public int priorityImageSource;
	public int statusImageSource;

	public Activity (int id, String name, String startTime, String endTime, String priority, String status) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priority = priority;
		this.status = status;
		this.priorityImageSource = getImageSource(priority);
		this.statusImageSource = getImageSource(status);
	}

	public int getImageSource (String string) {
		int id = 0;

		String[] strings = new String[] {
				"P1", "P2", "P3", "P4", "P5",
				"Pending", "Started", "Suspended", "Terminated", "Completed"
		};

		int[] ints = new int[] {
				R.drawable.red,
				R.drawable.orange,
				R.drawable.yellow,
				R.drawable.green,
				R.drawable.blue,
				R.drawable.exclamation,
				R.drawable.play,
				R.drawable.pause,
				R.drawable.cross,
				R.drawable.tick
		};

		for (id = 0; id < strings.length; id++) {
			if (string.equals(strings[id])) {
				break;
			}
		}

		return (id == strings.length) ? 0 : ints[id];
	}

	public int getRadioButtonId (String string) {
		int id = 0;
		String[] strings = new String[] {
				"P1", "P2", "P3", "P4", "P5",
				"Pending", "Started", "Suspended", "Terminated", "Completed"
		};

		int[] ints = new int[] {
				R.id.Priority1,
				R.id.Priority2,
				R.id.Priority3,
				R.id.Priority4,
				R.id.Priority5,
				R.id.Pending,
				R.id.Started,
				R.id.Suspended,
				R.id.Terminated,
				R.id.Completed
		};

		for (id = 0; id < strings.length; id++) {
			if (string.equals(strings[id])) {
				break;
			}
		}

		return (id == strings.length) ? 0 : ints[id];
	}
}