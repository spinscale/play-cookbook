package jobs;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import models.StatMonitor;
import play.Logger;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import com.jamonapi.RangeHolder;

@Every("5s")
public class RemoteApiJob extends Job {

	public void doJob() {
		Monitor monitor = MonitorFactory.start("RemoteApiCall");
		WSRequest req = WS.url("http://localhost:9000/@status.json");
		req.setHeader("Authorization", "f2bebb4c2cb8ad819ec67c3b2ff356159dea1fb8");
		HttpResponse resp = req.get();
		RangeHolder rangeHolder = new RangeHolder();
		rangeHolder.add("max", 1000);
		MonitorFactory.setRangeDefault("RemoteApiCall", rangeHolder);
		monitor.stop();

		// JSON parsing
		JsonObject obj = (JsonObject)  resp.getJson();
		JsonArray monitors = obj.getAsJsonObject("play.CorePlugin").getAsJsonArray("monitors");
		Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<StatMonitor>>(){}.getType();
        List<StatMonitor> stats = gson.fromJson(monitors, collectionType);
        
        // Into cache
        Cache.set("MonitorStats", stats);
	}
}
