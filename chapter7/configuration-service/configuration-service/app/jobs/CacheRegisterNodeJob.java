package jobs;

import java.util.HashSet;
import java.util.Set;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class CacheRegisterNodeJob extends Job {

	public void doJob() {
		String key = "CS:nodes";
		Set<String> nodes = Cache.get(key, Set.class);
		if (nodes == null) {
			Logger.info("%s: Creating new node list in cache", this.getClass().getSimpleName());
			nodes = new HashSet();
		}
		nodes.add(Play.id);
		Cache.set(key, nodes);
	}
}
