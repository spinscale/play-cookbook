package service;

import java.util.UUID;

import models.Registration;
import models.User;
import notifier.Mails;
import play.modules.registration.RegistrationService;
import play.mvc.Scope.Flash;

public class RegistrationServiceImpl implements RegistrationService {

	@Override
	public void createRegistration(Object context) {
		if (context instanceof User) {
			User user = (User) context;
			Registration r = new Registration();
			r.uuid = UUID.randomUUID().toString().replaceAll("-", "");
			r.user = user;
			r.create();
		}
	}

	@Override
	public void triggerEmail(Object context) {
		if (context instanceof User) {
			User user = (User) context;
			Registration registration = Registration.find("byUser", user).first();
			Mails.sendConfirmation(registration);
		}
	}

	@Override
	public boolean isAllowedToExecute(Object context) {
		if (context instanceof User) {
			User user = (User) context;
			return !user.active;
		}
		return false;
	}

	@Override
	public void confirm(Object context) {
		if (context != null) {
			Registration r = Registration.find("byUuid", context.toString()).first();
			if (r == null) {
				return;
			}
			User user = r.user;
			user.active = true;
			user.create();
			r.delete();
			Flash.current().put("registration", "Thanks for registering");
		}
	}
}
