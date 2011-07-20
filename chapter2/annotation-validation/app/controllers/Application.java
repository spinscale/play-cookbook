package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import annotations.Uuid;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

	public static void showUuid(@Uuid String uuid) {
		if (validation.hasErrors()) {
	        flash.error("Fishy uuid");
	        error();
	    }
		renderText(uuid + " is valid");
	}
}