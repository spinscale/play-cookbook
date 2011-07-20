package controllers;

import java.util.Collection;
import java.util.Collections;

import models.SearchResult;
import play.Logger;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	public static void twitterSearch() {
		Collection<SearchResult> results = Cache.get("twitterSearch", Collection.class);
		if (results == null) {
			results = Collections.emptyList();
		}
		renderArgs.put("twitterSearch", results);
	}
	
    public static void index() {
        render();
    }

}