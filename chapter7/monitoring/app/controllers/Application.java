package controllers;

import java.util.List;

import models.StatMonitor;
import play.cache.Cache;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	List<StatMonitor> monitors = Cache.get("MonitorStats", List.class);
    	StatMonitor monitor = null;
    	if (monitors != null) {
    		for (StatMonitor currentMonitor : monitors) {
    			if (currentMonitor.name.startsWith("RemoteApiCall")) {
    				monitor = currentMonitor;
    			}
    		}
    	}
        render(monitor);
    }

}