package plugin;

import play.Logger;
import play.PlayPlugin;

public class EventPlugin extends PlayPlugin {

	@Override
	public void onEvent(String event, Object context) {
		if ("JPASupport.objectDeleted".equals(event)) {
			Logger.info("Enttiy deleted: %s", context);
		} else if ("JPASupport.objectPersisted".equals(event)) {
			Logger.info("Enttiy created: %s", context);
		} else if ("JPASupport.objectUpdated".equals(event)) {
			Logger.info("Enttiy updated: %s", context);
		} else if (event.startsWith("Event.")) {
			Logger.info("Cool event: %s with content %s", event, context);
		}
	}
}
