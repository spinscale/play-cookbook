package controllers;

import play.Logger;

public class SimpleSecurity extends Secure.Security {
    
    static boolean authenticate(String username, String password) {
    	return "admin".equals(username) && "secret".equals(password) ||
    			"user".equals(username) && "secret".equals(password);
    }    
    
    static boolean check(String profile) {
    	if ("admin".equals(profile)) {
    		return connected().equals("admin");
    	}
    	return false;
    }
    
    static void onAuthenticated() {
    	Logger.info("Got auth for user %s", connected());
    }
    static void onDisconnect() {
    	Logger.info("Got disconnected for user %s", connected());
    }
    static void onCheckFailed(String profile) {
    	Logger.error("Failed auth for profile %s", profile);
    	forbidden();
    }
}
