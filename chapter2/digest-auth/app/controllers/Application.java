package controllers;

import java.util.Date;

import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	static void checkDigestAuth() {
		if (!DigestRequest.isAuthorized(request)) {
			throw new UnauthorizedDigest("Super Secret Stuff");				
		}
	}

	public static void index() {
		renderText("This is top secret!\n");
	}

}
