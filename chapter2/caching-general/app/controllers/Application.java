package controllers;

import java.util.Date;

import javax.inject.Inject;

import org.joda.time.DateTime;

import play.cache.CacheFor;
import play.mvc.Controller;
import play.mvc.results.NotModified;
import utils.EtagCacheCalculator;

public class Application extends Controller {

	public static void index() {
		Date date = new Date();
		render(date);
	}
	
	@CacheFor("5s")
	public static void indexCacheFor() {
		Date date = new Date();
		renderText("Current time is: " + date);
	}

	public static void proxyCache() {
		response.cacheFor("1h");
		renderText("Foo");
	}
	
	@Inject
	private static EtagCacheCalculator calculator;
	
	public static void etagCache(String name) {
		Date lastModified = new DateTime().minusDays(1).toDate();
		String etag = calculator.calculate(name);
		if(!request.isModified(etag, lastModified.getTime())) {
			throw new NotModified();
		}
		response.cacheFor(etag, "3h", lastModified.getTime());
		renderText("Learn to use etags, dumbass!");
	}
}