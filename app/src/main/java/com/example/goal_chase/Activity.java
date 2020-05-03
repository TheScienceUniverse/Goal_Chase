package com.example.goal_chase;

public class Activity {
	String activityName;
	String startTime;
	String endTime;
	int priorityImageSource;
	int statusImageSource;

	Activity (String activityName, String startTime, String endTime, String priority, String status) {
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priorityImageSource = getPriorityImageSource(priority);
		this.statusImageSource = getStatusImageSource(status);
	}

	int getPriorityImageSource (String priority) {
		int id = 0;

		switch (priority) {
			case "Priority-1":
				id = R.drawable.red;
				break;
			case "Priority-2":
				id = R.drawable.orange;
				break;
			case "Priority-3":
				id = R.drawable.yellow;
				break;
			case "Priority-4":
				id = R.drawable.green;
				break;
			case "Priority-5":
				id = R.drawable.blue;
				break;
			default:
				break;
		}

		return id;
	}

	int getStatusImageSource (String status) {
		int id = 0;

		switch (status) {
			case "Pending":
				id = R.drawable.exclamation;
				break;
			case "Started":
				id = R.drawable.play;
				break;
			case "Suspended":
				id = R.drawable.pause;
				break;
			case "Terminated":
				id = R.drawable.cross;
				break;
			case "Completed":
				id = R.drawable.tick;
				break;
			default:
				break;
		}

		return id;
	}
}