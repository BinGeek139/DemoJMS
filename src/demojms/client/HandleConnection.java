/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demojms.client;

import demojms.util.Const;
import static demojms.util.Const.PASSWORD;
import static demojms.util.Const.URL;
import static demojms.util.Const.USERNAME;
import form.Client;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
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

    /**
     * Tạo kết nôi dến ActiveMQ
     *
     * @param topicChat: là tên groupchat
     * @param listener: Nơi lăng nghe và xử lý các message
     */
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

    public void sendMessageToPoint(String strQueue, String message) {
        QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, URL);
        try {
            QueueConnection queueConnection = connectionFactory.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue(strQueue);
            QueueSender queueSender = queueSession.createSender(queue);
            TextMessage txtMessage = queueSession.createTextMessage();
            txtMessage.setText(message);
            queueSender.send(txtMessage);
            queueConnection.close();
        } catch (JMSException ex) {
            Logger.getLogger(HandleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createEndPoint(String strQueue) {
        QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, URL);
        try {
            QueueConnection queueConnection = connectionFactory.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue(strQueue);
            QueueReceiver receiver = queueSession.createReceiver(queue);
            
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    TextMessage message = (TextMessage) msg;
                    try {
                        Client.getInstance().addMessage(message.getText());
                    } catch (JMSException ex) {
                        Logger.getLogger(HandleConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            receiver.setMessageListener(listener);
            
            queueConnection.start();

        } catch (JMSException ex) {
            Logger.getLogger(HandleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gửi messgae
     *
     * @param content
     * @return
     */
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
    
    
    TopicSession topicSessionOnline=null;

    public void startOnline() {
        try {
            TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, URL);

            TopicConnection connection = connectionFactory.createTopicConnection();
            topicSessionOnline = connection.createTopicSession(Boolean.FALSE, TopicSession.AUTO_ACKNOWLEDGE);
            Topic topic = topicSessionOnline.createTopic(Const.TOPIC_ONLINE);
            publisher = topicSessionOnline.createPublisher(topic);
            subscriber = topicSessionOnline.createSubscriber(topic);
            subscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    TextMessage message = (TextMessage) msg;
                    try {
                        Client.getInstance().addUser(message.getText());
                          System.out.println("topic online đã  nhận được: "+message.getText());
                    } catch (JMSException ex) {
                        Logger.getLogger(HandleConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            connection.start();
            

        } catch (JMSException ex) {
            ex.printStackTrace();
        }

    }
    
    public void sendUsernameOnline(String user){
        TextMessage message;
            try {
                message = topicSessionOnline.createTextMessage();
                message.setText(user);
                this.publisher.send(message);
                System.out.println(user+" đã online");
         
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
    }

}
