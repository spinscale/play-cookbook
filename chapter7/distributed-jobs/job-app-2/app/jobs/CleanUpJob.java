package jobs;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class CleanUpJob extends MultiNodeJob {

	@Override
	public void execute() {
		Logger.info("Doing my job of all greatness");
		try {
			// Emulate some long running process
			Thread.sleep(8000);
		} catch (InterruptedException e) {
		}
	}

}
