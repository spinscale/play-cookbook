package jobs;

import org.apache.commons.lang.math.RandomUtils;

import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;

import com.jamonapi.MonKey;
import com.jamonapi.MonKeyBase;
import com.jamonapi.MonKeyImp;
import com.jamonapi.MonKeyItem;
import com.jamonapi.MonKeyItemBase;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Every("1s")
public class TestCacheJob extends Job {

	public void doJob() {
		String foo = Cache.get("anything", String.class);

		String monitorName = foo == null ? "cacheMiss" : "cachehHit";
		MonitorFactory.add(monitorName, "cnt", 1);

		Cache.set("anything", "foo", RandomUtils.nextInt(4) + "s");
		
	}
}
