/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demojms.util;

import javax.jms.Connection;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Admin
 */
public interface Const {

    String USERNAME = "admin";
    String PASSWORD = "admin";
    String URL = "tcp://localhost:61616";
    String TOPIC_CHAT="topic/chat";
    String STRING_EMPTY="";
    String TOPIC_ONLINE="topic/online";

}
