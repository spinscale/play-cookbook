package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import json.UserSerializer;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

	public static void showUser(Long id) {
		User user = User.find("byId", id).first();
		notFoundIfNull(user);
		renderJSON(user, new UserSerializer());
	}

}