package jobs;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class BootstrapJob extends Job {

	public void doJob() {
		// Check if the database is empty, then load the fixtures
		if(User.count() == 0) {
			Fixtures.load("initial-data.yml");
		}
	}

}
