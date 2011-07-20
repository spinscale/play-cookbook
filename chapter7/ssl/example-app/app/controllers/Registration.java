package controllers;

import org.apache.commons.lang.math.RandomUtils;

import play.mvc.Controller;
import play.mvc.With;

@With(EnsureSSL.class)
public class Registration extends Controller {

    public static void register() {
    	try {
			Thread.sleep(RandomUtils.nextInt(250));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		renderText("foo");
    }

}
