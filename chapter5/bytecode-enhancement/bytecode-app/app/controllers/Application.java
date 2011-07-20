package controllers;

import play.Logger;
import play.libs.Crypto;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
    	Logger.error(Crypto.sign("@status"));
        render();
    }

}