package main.java.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Server extends Thread {
    private Socket socket = null;
    private LocalDateTime startConversation;
    private String clientName;

    public Server(Socket socket) {
        super("Server");
        this.socket = socket;
    }

    public void run() {

        PrintWriter out = null;

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            socket.setSoTimeout(29000);
            String fromClient;
            String fromServer;

            startConversation = LocalDateTime.now();

            Protocol protocol = new Protocol(startConversation);
            fromServer = protocol.processInput(null);
            out.println(fromServer);

            while ((fromClient = in.readLine()) != null) {
                fromServer = protocol.processInput(fromClient);
                out.println(fromServer);
                if (fromServer != null && fromClient.startsWith("HI, I'M ")){
                    clientName = fromClient.substring(8);
                }
            }

        } catch (SocketTimeoutException e) {
            out.println("BYE " + clientName + ", WE SPOKE FOR " + Duration.between(startConversation, LocalDateTime.now()).toMillis() + " MS");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}