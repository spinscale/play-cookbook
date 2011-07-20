package jobs;

import java.util.Date;
import java.util.List;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class CacheAliveJob extends Job {

	private static final String key = "CS:alive:" + Play.id;
	
	public void doJob() {
		Date now = new Date();
		Logger.info("%s: %s", this.getClass().getSimpleName(), now);
		Cache.set(key, now.getTime(), "15s");
	}
}
