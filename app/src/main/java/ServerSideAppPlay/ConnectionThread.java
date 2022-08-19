package ServerSideAppPlay;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class ConnectionThread extends Thread {
    protected Socket socket;

    private Map<Socket,DataOutputStream> m;

    public ConnectionThread(Socket clientSocket, Map<Socket,DataOutputStream> m) {
        this.socket = clientSocket;
        this.m = m;
    }

    public void run() {
        System.out.println("running thread with socket == " + socket.toString());
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
            m.put(socket, out);
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("quit")) {
                    m.remove(socket);
                    socket.close();
                    return;
                } else {
                    for (Map.Entry<Socket,DataOutputStream> e : m.entrySet()) {
                        if (e.getKey().equals(socket)) continue;
                        DataOutputStream outputStream = e.getValue();
                        outputStream.writeBytes(line + "\n\r");
                        outputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
