package play.modules.activemq;

import java.io.Serializable;

public interface ActiveMqJob {
	public void setSerializable(Serializable serializable);
	public Serializable getSerializable();
}
