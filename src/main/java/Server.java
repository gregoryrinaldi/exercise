package main.java;

import main.java.core.ClientHandler;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;

import java.io.IOException;
import java.net.ServerSocket;

public class Server
{
    public static AsSynchronizedGraph<String, DefaultWeightedEdge> weightedMultigraph = new AsSynchronizedGraph<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("java <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        ClientHandler server;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started");
            while (listening) {
                server = new ClientHandler(serverSocket.accept());
                server.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}