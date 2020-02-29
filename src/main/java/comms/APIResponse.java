package comms;

public class APIResponse {
	String status, data, error;

	public APIResponse(String data, boolean fail) {
		if (!fail) {
			this.status = "success";
			this.data = data;
		} else {
			this.status = "failure";
			this.error = data;
		}
	}

}
