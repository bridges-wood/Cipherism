package com.polytonic.comms;

import java.sql.Timestamp;
import java.util.Date;

public class APIResponse {
	String status, data, error;
	Timestamp time;

	public APIResponse(String data, boolean fail) {
		if (!fail) {
			this.status = "success";
			this.data = data;
		} else {
			this.status = "failure";
			this.error = data;
		}
		Date date = new Date();
		this.time = new Timestamp(date.getTime());
	}

}
