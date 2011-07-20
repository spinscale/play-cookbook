package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
        render();
    }

    @Check("admin")
    public static void admin() {
    	index();
    }
}