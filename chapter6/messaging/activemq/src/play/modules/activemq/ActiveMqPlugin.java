package play.modules.activemq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang.builder.ToStringBuilder;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.jobs.Job;

public class ActiveMqPlugin extends PlayPlugin {

	private ActiveMQConnectionFactory connectionFactory;
	
	private TopicConnection sendingConnection;
	private TopicSession sendingSession;
	private Topic sendingTopic;
	private TopicPublisher sendingPublisher;
	
	private TopicConnection receiveConnection;
	private TopicSession receiveSession;
	private Topic receiveTopic;
	private TopicSubscriber receiveSubscriber;
	private static final String uuid = UUID.randomUUID().toString();


	@Override
	public void onApplicationStart() {
		Logger.info("ActiveMQ Plugin started");
		try {
			List<Class> jobClasses = new ArrayList<Class>();
			for (ApplicationClass applicationClass : Play.classes.getAssignableClasses(ActiveMqJob.class)) {
				if (Job.class.isAssignableFrom(applicationClass.javaClass)) {
					jobClasses.add(applicationClass.javaClass);
				}
			}
			MessageListener listener = new ActiveMqConsumer(jobClasses);

			
			connectionFactory = new ActiveMQConnectionFactory();
			
			// receive
			receiveConnection = connectionFactory.createTopicConnection();
			receiveConnection.start();
			receiveSession = receiveConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			receiveTopic = receiveSession.createTopic("playMessages");
			receiveSubscriber = receiveSession.createSubscriber(receiveTopic);
			receiveSubscriber.setMessageListener(listener);
			
			// send
			sendingConnection = connectionFactory.createTopicConnection();
			sendingConnection.start();
			sendingSession = sendingConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			sendingTopic = sendingSession.createTopic("playMessages");
			sendingPublisher = sendingSession.createPublisher(sendingTopic);
		} catch (Exception e) {
			Logger.error(e, "Could not start activemq broker");
		}
	}

	@Override
	public void onApplicationStop() {
		Logger.info("Stopping activemq connections");
		try {
			sendingPublisher.close();
			receiveSubscriber.close();
			sendingSession.close();
			receiveSession.close();
			sendingConnection.close();
			receiveConnection.close();
		} catch (JMSException e) {
			Logger.error(e, "Could closing connection");
		}
	}

	@Override
	public void onEvent(String message, Object context) {
		if ("chatEvent".equals(message) && context instanceof Serializable) {
			Serializable event = (Serializable) context;
			try {
				ObjectMessage objMsg = sendingSession.createObjectMessage(event);
				objMsg.setStringProperty("hostId", uuid);
				sendingPublisher.publish(objMsg);
				Logger.info("Sent event to queue: %s", ToStringBuilder.reflectionToString(event));
			} catch (Exception e) {
				Logger.error(e, "Could not publish message");
			}
		}
	}

	public static class ActiveMqConsumer implements MessageListener {

		private List<Class> jobs;

		public ActiveMqConsumer(List<Class> jobs) {
			this.jobs = jobs;
		}
		
		@Override
		public void onMessage(Message message) {
			try {
				if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					if (uuid.equals(objectMessage.getStringProperty("hostId"))) {
						Logger.debug("Ignoring activemq event because it came from this host");
						return;
					}
					Serializable event = (Serializable) objectMessage.getObject();
					Logger.debug("Received activemq event to plugin: %s", ToStringBuilder.reflectionToString(event));
					for (Class jobClass : jobs) {
						Job job = (Job) jobClass.newInstance();
						job.getClass().getField("serializable").set(job, event);
						job.now().get(); // run in sync
						
					}
				}
			} catch (Exception e) {
				Logger.error(e, "Error getting message");
			}
		}
	}
}
