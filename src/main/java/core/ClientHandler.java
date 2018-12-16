package main.java.core;

import main.java.constants.Commands;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;

public class ClientHandler extends Thread {

    private static final String US_ASCII = "US-ASCII";
    private Socket socket;

    public ClientHandler(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread # " + threadId + " is doing this task");

        LocalDateTime startConversation = null;
        String clientName = null;
        PrintWriter out = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName(US_ASCII)));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName(US_ASCII)), true);
            startConversation = LocalDateTime.now();

            socket.setSoTimeout(29000);

            String fromClient;
            String fromServer;

            Protocol protocol;

            protocol = new Protocol(startConversation);
            fromServer = protocol.processInput(null);
            out.println(fromServer);

            while ((fromClient = in.readLine()) != null) {
                fromServer = protocol.processInput(fromClient);
                out.println(fromServer);
                if (clientName == null && fromClient.startsWith(Commands.HI_IM)){
                    clientName = fromClient.substring(8);
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println(e);
            out.println("BYE " + clientName + ", WE SPOKE FOR " + Duration.between(startConversation, LocalDateTime.now()).toMillis() + " MS");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}