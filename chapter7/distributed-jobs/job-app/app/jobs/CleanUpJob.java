package jobs;

import org.slf4j.LoggerFactory;

import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class CleanUpJob extends MultiNodeJob {

	org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute() {
		log.info("Doing my job of all greatness");
		try {
			// Emulate some long running process
			log.warn("Before sleeping");
			Thread.sleep(8000);
			log.debug("After sleeping");
		} catch (InterruptedException e) {
		}
	}

}
