package jobs;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.jobs.Job;

public abstract class MultiNodeJob extends Job {

	protected final String cacheName = this.getClass().getSimpleName();

	public abstract void execute();
	
	public void doJob() {
		boolean isRunning = Cache.get(cacheName) != null;
		if (isRunning) {
			Logger.info("Not executing job %s here, someone other node seems to", cacheName);
			return;
		}
		// Possible race condition here
		Cache.safeSet(cacheName, Play.id, "10min");

		try {
			execute();
		} finally {
			if (!isRunning) {
				Cache.safeDelete("CleanUpJob");
			}
		}

	}

}
