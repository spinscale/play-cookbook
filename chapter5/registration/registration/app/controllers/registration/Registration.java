package controllers.registration;

import play.modules.registration.RegistrationPlugin;
import play.mvc.Controller;
import controllers.Application;

public class Registration extends Controller {

	public static void confirm(String uuid) {
		RegistrationPlugin.confirm(uuid);
		Application.index();
	}
}
