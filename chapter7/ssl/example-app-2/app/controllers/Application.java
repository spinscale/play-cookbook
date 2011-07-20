package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import org.apache.commons.lang.math.RandomUtils;

import models.*;

public class Application extends Controller {

    public static void index() {
    	try {
			Thread.sleep(RandomUtils.nextInt(250));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        render();
    }
    
    public static void login() {
    	session.put("state", "foo");
    	index();
    }
    
    public static void logout() {
    	session.clear();
    	index();
    }

    public static void secondMethod() {
    	try {
			Thread.sleep(RandomUtils.nextInt(250));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		renderText("foo");
    }
}