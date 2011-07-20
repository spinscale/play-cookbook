package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class EnsureSSL extends Controller {

	@Before 
	static void verifySSL() { 
		if(!request.secure) { 
			redirect("https://" + request.host + request.url); 
		} 
	} 
}
