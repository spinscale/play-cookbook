package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.results.Status;

public class Application extends Controller {

	public static void getConfig(String key) {
		String value = (String) Cache.get(key);
		String message = String.format("{ %s: \"%s\" }", key, value);
		response.setContentTypeIfNotSet("application/json; charset=utf-8");
		renderText(message);
	}
	
	public static void setConfig(String key) {
		try {
			Cache.set(key, IOUtils.toString(request.body));
		} catch (IOException e) {
			Logger.error(e, "Fishy request");
		}
		throw new Status(204);
	}

    public static void nodes() {
    	Set<String> nodes = Cache.get("CS:nodes", Set.class);
    	Map<String, List> nodeMap = new HashMap();
    	nodeMap.put("alive", new ArrayList<String>());
    	nodeMap.put("dead", new ArrayList<String>());
    	
    	for (String node : nodes) {
    		if (Cache.get("CS:alive:" + node) == null) {
    			nodeMap.get("dead").add(node);
    		} else {
    			nodeMap.get("alive").add(node);
    		}
    	}
    	
    	renderJSON(nodeMap);
    }
}