package play.modules.firstmodule;

import play.Logger;
import play.PlayPlugin;

public class MyPlugin extends PlayPlugin {

	public void onApplicationStart() {
		Logger.info("Yeeha, firstmodule started");
	}
	
}

