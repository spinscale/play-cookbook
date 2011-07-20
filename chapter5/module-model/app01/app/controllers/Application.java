package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import models.*;

public class Application extends Controller {

    public static void index() {
    	User u = new User();
    	u.login = RandomStringUtils.random(10);
    	u.name = RandomStringUtils.random(10);
    	u.bla = RandomStringUtils.random(10);
    	u.save();
    	
    	List<User> users = User.findAll();
        render(users);
    }

}