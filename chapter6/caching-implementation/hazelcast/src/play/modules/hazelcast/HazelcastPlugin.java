package play.modules.hazelcast;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.cache.Cache;

public class HazelcastPlugin extends PlayPlugin {

	public void onApplicationStart() {
		Boolean isEnabled = new Boolean(Play.configuration.getProperty("hazelcast.enabled"));
		if (isEnabled) {
			Logger.info("Setting cache to hazelcast implementation");
			Cache.forcedCacheImpl = HazelCastCacheImpl.getInstance();
			Cache.init();
		}
	}
}
