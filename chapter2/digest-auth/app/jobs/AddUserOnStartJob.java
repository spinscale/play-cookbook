package jobs;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Crypto;

@OnApplicationStart
public class AddUserOnStartJob extends Job {

	public void doJob() {
		if (User.count("byName", "alex") == 0) {
			User user = new User();
			user.name = "alex";
			user.password = Crypto.passwordHash("foo");
			user.apiPassword = "test";
			user.save();
		}
	}
}
