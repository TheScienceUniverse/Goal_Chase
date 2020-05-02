package com.example.goal_chase;

public class Activity {
	String activityName;
	String startTime;
	String endTime;
	String priorityImageSource;
	String statusImageSource;

	public Activity (String activityName, String startTime, String endTime, String priority, String status) {
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priorityImageSource = priority;
		this.statusImageSource = status;
	}

	String getPriorityImageSource (String priority) {
		String url = "";

		switch (priority) {
			case "Priority-1":
				url = "@drawable/red";
				break;
			case "Priority-2":
				url = "@drawable/orange";
				break;
			case "Priority-3":
				url = "@drawable/yellow";
				break;
			case "Priority-4":
				url = "@drawable/green";
				break;
			case "Priority-5":
				url = "@drawable/blue";
				break;
			default:
				break;
		}

		return url;
	}

	String getStatusImageSource (String status) {
		String url = "";

		switch (status) {
			case "Status - Started":
				url = "@drawable/";
				break;
			case "Status - Suspended":
				url = "@drawable/";
				break;
			case "Status - Closed":
				url = "@drawable/cross";
				break;
			case "Status - Completed":
				url = "@drawable/tick";
				break;
			default:
				break;
		}

		return url;
	}
}