/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demojms;

import demojms.client.HandleConnection;
import demojms.client.MyMessageListener;
import demojms.util.Const;
import java.util.Scanner;
import jdk.internal.org.objectweb.asm.Handle;

/**
 *
 * @author Admin
 */
public class DemoJms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HandleConnection connection = new HandleConnection();
        connection.createConnection(Const.TOPIC_CHAT, new MyMessageListener());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhập tên đăng nhập");
        String userName = scanner.nextLine();
        System.out.println("---------------------------------------------");
        System.out.println("Bắt đầu chat");
        System.out.println("---------------------------------------------");
        while (true) {
            String content=scanner.nextLine();
            connection.sendMessage(userName+": "+content);
        }
    }

}
