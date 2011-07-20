package notifier;

import models.Registration;
import play.Play;
import play.mvc.Mailer;

public class Mails extends Mailer {

	public static void sendConfirmation(Registration registration) {
		setSubject("Confirm your registration");
		addRecipient(registration.user.email);
		setFrom(Play.configuration.getProperty("registration.mail.from"));
		send(registration);
	}
}
