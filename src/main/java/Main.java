package main.java;

import main.java.core.Server;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;

public class Main
{
    public static Graph<String, DefaultWeightedEdge> weightedMultigraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public static void main(String[] args) {

        System.out.println("Server started at " + LocalDateTime.now());

        boolean listening = true;

        Server server;
        try (ServerSocket serverSocket = new ServerSocket(50000)) {
            while (listening) {
                server = new Server(serverSocket.accept());
                System.out.println("ssdsqdqdsds");
                server.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + 50000);
            System.exit(-1);
        }
    }
}