import java.io.Serializable;

import models.ChatRoom;
import play.jobs.Job;
import play.modules.activemq.ActiveMqJob;

@ActiveMqJob
public class UpdateChatroomJob extends Job {

	public Serializable serializable;

	public void setSerializable(Serializable serializable) {
		this.serializable = serializable;
	}
	
	public void doJob() {
		if (serializable != null) {
			ChatRoom.get().publishWithoutPluginNotification(serializable);
		}
	}
}
