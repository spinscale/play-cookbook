package jobs;

import java.util.Calendar;
import java.util.List;

import models.Registration;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("7d")
public class RegistrationCleanupJob extends Job {

	public void doJob() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		List<Registration> registrations = Registration.find("createdAt < ", cal.getTime()).fetch();
		for (Registration registration : registrations) {
			registration.delete();
		}
		Logger.info("Deleted %s stale registrations", registrations.size());
	}
}
