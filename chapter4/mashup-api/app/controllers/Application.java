package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import models.User;

import org.w3c.dom.DOMException;

import play.Logger;
import play.cache.Cache;
import play.libs.F.Promise;
import play.libs.Crypto;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;

import com.google.gson.JsonObject;

public class Application extends Controller {

	@Before(priority=1, unless={"createTicket", "quotes", "thing"})
	public static void checkAuth() {
		Header ticket = request.headers.get("x-authorization");
		if (ticket == null) {
			error("Please provide a ticket");
		}
		
		String cacheTicket = Cache.get("ticket:" + ticket.value(), String.class);
		if (cacheTicket == null) {
			error("Please renew your ticket");
		
		}
		Cache.set("ticket:" + ticket.value(), cacheTicket, "5min");
	}
	
	public static void index() {
        render();
    }
	
	public static void createTicket(String user, String pass) {
		User u = User.find("byNameAndPassword", user, pass).first();
		if (u == null) {
			error("No authorization granted");
		}
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Cache.set("ticket:" + uuid, u.name, "5min");
		renderText(uuid);
	}
	
	public static void quote() {
		String username = Cache.get("ticket:" + request.headers.get("x-authorization").value(), String.class);
		User user = User.find("byName", username).first();
		String quote = user.quote;
		render(quote);
	}
	
	public static void quotes() throws DOMException, InterruptedException, ExecutionException {
    	Promise<HttpResponse> promise2 = WS.url("http://www.iheartquotes.com/api/v1/random?format=json").getAsync();
    	Promise<HttpResponse> promise1 = WS.url("http://jamocreations.com/widgets/travel-quotes/get.php").getAsync();
    	Map map = new HashMap<String, String>();
    	
    	// code here, preferably long running like db queries...
    	List<HttpResponse> resps = Promise.waitAll(promise1, promise2).get();
    	if(resps.get(0) != null) {
    		map.put("first", resps.get(0).getXml().getElementsByTagName("quote").item(0).getChildNodes().item(1).getTextContent());
    	}
    	if(resps.get(1) != null) {
    		map.put("second", ((JsonObject) resps.get(1).getJson()).get("quote").getAsString());
    	}		
		renderJSON(map);
	}

}