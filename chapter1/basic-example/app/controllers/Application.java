package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Tweet;
import models.User;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void login(String login, String password) {
    	User user = User.find("byLoginAndPassword", login, password).first();
    	notFoundIfNull(user);
    	session.put("login", user.login);
    	showTweets(user.login);
    }

	public static void showUser(String id) {
		User user = User.find("byId", id).first();
		notFoundIfNull(user);
		render(user);
	}

	public static void showTweets(String username) {
		User user = User.find("byLogin", username).first();
		notFoundIfNull(user);
		List<Tweet> tweets = Tweet.find("user = ? order by postedAt DESC", user).fetch(20);
		render(tweets, user);
	}

}