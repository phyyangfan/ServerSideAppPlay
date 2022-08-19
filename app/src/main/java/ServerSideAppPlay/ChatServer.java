package ServerSideAppPlay;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {
    private static final int PORT = 7273;

    private Map<Socket,DataOutputStream> m = new HashMap<>();

    public void run() {
        ServerSocket serverSocket = null;
        //Socket socket = null;
    
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server has started on " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort() + ".\r\nWaiting for connections...");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // new thread for a client
                    System.out.println("A client connected. Created socket: " + socket.toString());
                    new ConnectionThread(socket, m).start();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
            }        
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
                System.out.println("Server has stopped.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
