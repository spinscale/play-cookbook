package controllers;

import models.User;
import play.Logger;
import play.libs.Crypto;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
    	User user = User.find("byUserAndPassword", username, Crypto.passwordHash(password)).first();
    	return user != null;
    }
    
    static boolean check(String profile) {
    	if ("admin".equals(profile)) {
    		User user = User.find("byUser", connected()).first();
    		if (user != null) {
    			return user.isAdmin;
    		}
    	} else if ("user".equals(profile)) {
    		return connected().equals("user");
    	}
        return false;
    }
}
