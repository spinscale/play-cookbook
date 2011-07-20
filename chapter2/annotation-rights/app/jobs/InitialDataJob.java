package jobs;

import java.util.Arrays;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class InitialDataJob extends Job {
	public void doJob() {
		if (User.count() == 0) {
			Fixtures.load("initial.yaml");
		}
	}
}
