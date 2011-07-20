package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index(String tenant) {
    	Logger.info("Foo: %s", Play.configuration.get("host"));
        renderText(tenant + request.domain);
    }

}