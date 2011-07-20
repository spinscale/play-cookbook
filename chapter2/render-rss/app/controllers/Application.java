package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import static render.FeedResult.*;

public class Application extends Controller {

    public static void index() {
    	List<Post> posts = Post.find("order by createdAt DESC").fetch();
        render(posts);
    }

	public static void renderRss() {
    	List<Post> posts = Post.findLatest();
		renderFeedRss(posts);
	}

	public static void renderRss2() {
    	List<Post> posts = Post.findLatest();
		renderFeedRss2(posts);
	}

	public static void renderAtom() {
    	List<Post> posts = Post.findLatest();
		renderFeedAtom(posts);
	}

	public static void showPost(Long id) {
		List<Post> posts = Post.find("byId", id).fetch();
		notFoundIfNull(posts);
		renderTemplate("Application/index.html", posts);
	}

}