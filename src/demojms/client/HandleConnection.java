/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demojms.client;

import static demojms.util.Const.PASSWORD;
import static demojms.util.Const.URL;
import static demojms.util.Const.USERNAME;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author ngocquang
 */
public class HandleConnection {

    private TopicPublisher publisher = null;
    private TopicSubscriber subscriber = null;
    private TopicSession session = null;

    public void createConnection(String topicChat, MessageListener listener) {
        try {
            TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, URL);

            TopicConnection connection = connectionFactory.createTopicConnection();
            session = connection.createTopicSession(Boolean.FALSE, TopicSession.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicChat);
            publisher = session.createPublisher(topic);
            subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(listener);
            connection.start();

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean sendMessage(String content) {
        TextMessage message;
        try {
            message = this.session.createTextMessage();
            message.setText(content);
            this.publisher.send(message);
            return Boolean.TRUE;
        } catch (JMSException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }

    }
}
