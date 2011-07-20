package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void addUser(User user) {
    	user.active = false;
    	if (validation.hasErrors()) {
    		error("Validation errors");
    	}
    	
    	user.create();
    	index();
    }

}