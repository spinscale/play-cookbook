package jobs;
import java.io.Serializable;

import models.ChatRoom;
import play.jobs.Job;
import play.modules.activemq.ActiveMqJob;

public class UpdateChatroomJob extends Job implements ActiveMqJob {

	private Serializable serializable;

	public Serializable getSerializable() {
		return serializable;
	}
	
	public void setSerializable(Serializable serializable) {
		this.serializable = serializable;
	}
	
	public void doJob() {
		if (serializable != null) {
			ChatRoom.get().publishWithoutPluginNotification(serializable);
		}
	}

}
