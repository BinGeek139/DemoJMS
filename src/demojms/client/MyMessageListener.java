/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demojms.client;

import form.Client;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author ngocquang
 */
public class MyMessageListener implements MessageListener{

    /**
     * Xử lý khi nhận được các message từ publicer ở đây 
     * @param msg 
     */
    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage message=(TextMessage)msg;
            System.out.println(message.getText());
            Client.getInstance().addMessage(message.getText());
            
        } catch (JMSException ex) {
           ex.printStackTrace();
        }
        
    }
    
   
    
}
