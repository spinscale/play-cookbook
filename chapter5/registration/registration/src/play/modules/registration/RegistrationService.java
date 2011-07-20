package play.modules.registration;

public interface RegistrationService {
	public void createRegistration(Object context);
	public void triggerEmail(Object context);
	public boolean isAllowedToExecute(Object context);
	public void confirm(Object context);
}
