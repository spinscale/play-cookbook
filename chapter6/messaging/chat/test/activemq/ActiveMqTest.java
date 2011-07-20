package activemq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import models.ChatRoom;
import models.ChatRoom.Event;
import models.ChatRoom.Join;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ActiveMqTest extends UnitTest {

	private TopicConnection receiveConnection;
	private TopicSession receiveSession;
	private Topic receiveTopic;
	private TopicSubscriber receiveSubscriber;
	private TopicConnection sendingConnection;
	private TopicSession sendingSession;
	private Topic sendingTopic;
	private TopicPublisher sendingPublisher;

	@Before
	public void initialize() throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				ActiveMQConnection.DEFAULT_BROKER_URL);

		receiveConnection = connectionFactory.createTopicConnection();
		receiveConnection.start();
		receiveSession = receiveConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		receiveTopic = receiveSession.createTopic("playMessages");
		receiveSubscriber = receiveSession.createSubscriber(receiveTopic);
		
		sendingConnection = connectionFactory.createTopicConnection();
		sendingConnection.start();
		sendingSession = sendingConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		sendingTopic = sendingSession.createTopic("playMessages");
		sendingPublisher = sendingSession.createPublisher(sendingTopic);

		ChatRoom.clean();
	}
	
	@After
	public void shutdown() throws Exception {
		receiveSubscriber.close();
		receiveSession.close();
		receiveConnection.close();
		sendingSubscriber.close();
		sendingSession.close();
		sendingConnection.close();
		
	}

	@Test
	public void assertThatPluginSendsMessages() throws Exception {
		assertEquals(0, ChatRoom.get().archive().size());
		Event event = new Join("user");
		ChatRoom.get().publish(event);

		// Check chatroom
		int currentEventCount = ChatRoom.get().archive().size();
		assertEquals(1, currentEventCount);
		
		// Check for messages
		Message msg = receiveSubscriber.receive(2000);
		Event evt = (ChatRoom.Event) ((ObjectMessage) msg).getObject();
		assertEquals("join", evt.type);
	}
	
	@Test
	public void assertThatPluginReceivesMessages() throws Exception {
		assertEquals(0, ChatRoom.get().archive().size());
		
		// Send event via JMS
		Event event = new ChatRoom.Message("alex", "cool here");
		ObjectMessage objMsg = sendingSession.createObjectMessage(event);
		sendingPublisher.publish(objMsg);
		
		Thread.sleep(1000); // short sleep to make sure the content arrives
		assertEquals(1, ChatRoom.get().archive().size());
	}
	
}
