package controllers;

import play.*;
import play.mvc.*;
import rights.Right;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    @Before(unless = "login")
    public static void checkForRight() {
    	String sessionUser = session.get("user");
        User user = User.find("byUsername", sessionUser).first();
        notFoundIfNull(user);
     
        Right right = getActionAnnotation(Right.class);
        if (!user.hasRight(right.value())) {
            forbidden("User has no right to do this");
        }
    }

    public static void login(String username, String password) {
        User user = User.find("byUsernameAndPassword", username, password).first();
        if (user == null) {
        	forbidden();
        }
    	session.put("user", user.username);
    }

    @Right("Secret")
	public static void secret() {
    	renderText("This is secret");
	}

    @Right("TopSecret")
	public static void topsecret() {
    	renderText("This is top secret");		
	}

}