package play.modules.registration;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.db.jpa.JPABase;

public class RegistrationPlugin extends PlayPlugin {
	
	private static boolean pluginActive = false;
	private static RegistrationService service;
	
	public void onApplicationStart() {
		ApplicationClass registrationService = Play.classes.getAssignableClasses(RegistrationService.class).get(0);
		
		if (registrationService == null) {
			Logger.error("Registration plugin disabled. No class implements RegistrationService interface");
		} else {
			try {
				service = (RegistrationService) registrationService.javaClass.newInstance();
				pluginActive = true;
			} catch (Exception e) {
				Logger.error(e, "Registration plugin disabled. Error when creating new instance");
			}
		}
	}
	
	public void onEvent(String message, Object context) {
		boolean eventMatched = "JPASupport.objectPersisted".equals(message);
		if (pluginActive && eventMatched && service.isAllowedToExecute(context)) {
			service.createRegistration(context);
			service.triggerEmail(context);
		}
	}
    
	public static void confirm(Object uuid) {
		if (pluginActive) {
			service.confirm(uuid);
		}
	}
}