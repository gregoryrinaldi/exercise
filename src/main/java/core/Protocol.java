package main.java.core;

import main.java.constants.Commands;
import main.java.constants.Messages;
import main.java.constants.Patterns;
import main.java.constants.ProtocolHelper;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static main.java.constants.Messages.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class Protocol {

    private boolean idle = true;
    private String clientName = null;
    private ProtocolHelper helper = new ProtocolHelper();
    LocalDateTime startConversation;

    public Protocol(LocalDateTime startConversation) {
      this.startConversation = startConversation;
    }

    public String processInput(String request) {
        System.out.println("Client: " + request);
        String response = null;

        if (StringUtils.startsWith(request, Commands.HI_IM)) {
            if (isNotEmpty(request) && request.matches(HI_I_AM + Patterns.UUID)) {
                clientName = request.substring(8);
                response = HI + clientName;
            }
        } else if (StringUtils.startsWith(request, Commands.BYE_MATE)) {
            if (BYE_MATE.equals(request)) {
                response = BYE + " " + clientName + ", WE SPOKE FOR " +  Duration.between(startConversation, LocalDateTime.now()).toMillis()+ " MS";
            }
        } else if (StringUtils.startsWith(request, Commands.ADD_NODE)) {
            response = ERROR_NODE_ALREADY_EXISTS;
            if (helper.addNode(request)) {
                response = NODE_ADDED;
            }
        } else if (StringUtils.startsWith(request, Commands.REMOVE_NODE)) {
            response = ERROR_NODE_NOT_FOUND;
            String value = request.substring(12);
            if (helper.removeNode(value)) {
                response = NODE_REMOVED;
            }
        } else if (StringUtils.startsWith(request, Commands.ADD_EDGE)) {
            response = Messages.ERROR_NODE_NOT_FOUND;
            String[] parts = request.split(" ");
            if (parts.length == 5) {
                if (helper.addEdge(parts[2], parts[3], parts[4])) {
                    response = EDGE_ADDED;
                }
            }
        } else if (StringUtils.startsWith(request, Commands.REMOVE_EDGE)) {
            response = EDGE_REMOVED;
            String[] parts = request.split(" ");
            if (parts.length == 4) {
                DefaultWeightedEdge defaultWeightedEdge = helper.removeEdge(parts[2], parts[3]);
                if (defaultWeightedEdge == null){
                    response = Messages.ERROR_NODE_NOT_FOUND;
                }
            }
        } else if (StringUtils.startsWith(request, Commands.SHORTEST_PATH)){
            String[] parts = request.split(" ");
            if (parts.length == 4) {
                String positiveInfinity = String.valueOf(Double.POSITIVE_INFINITY);
                double shortestPath = helper.shortestPath(parts[2], parts[3]);
                String shortestPathAsString = String.valueOf(shortestPath);
                if (shortestPathAsString.equals(positiveInfinity)){
                    response = String.valueOf(Integer.MAX_VALUE);
                }
                else {
                    response = shortestPathAsString;
                }
            }
        } else if (StringUtils.startsWith(request, Commands.CLOSER_THAN)){
            response = Messages.ERROR_NODE_NOT_FOUND;
        } else {
            if (idle) {
                response = HI_I_AM + UUID.randomUUID();
                idle = false;
            } else {
                response = Messages.SORRY_DID_NOT_UNDERSTAND_THAT;
            }
        }

        System.out.println("Server: " + response);
        return response;
    }

}
