package main.java.core;

import main.java.constants.Commands;
import main.java.constants.Messages;
import main.java.constants.ProtocolHelper;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static main.java.constants.Messages.*;

public class Protocol {

    public static final String PATTERN_UUID = "[a-zA-Z0-9-]*$";

    private static final int WAITING = 0;
    private static final int SENTNAME = 1;
    private static final int SENTHI = 2;
    private static final int SENTBY = 3;

    private int state = WAITING;
    private String clientName = null;
    private ProtocolHelper helper = new ProtocolHelper();

    LocalDateTime startConversation;

    public Protocol(LocalDateTime startConversation) {
        this.startConversation = startConversation;
    }

    public synchronized String processInput(String request) {
        System.out.println("Client: " + request);
        String response = null;

        if (StringUtils.startsWith(request, Commands.BYE_MATE)) {
            response = BYE + " " + clientName + ", WE SPOKE FOR " +  Duration.between(startConversation, LocalDateTime.now()).toMillis()+ " MS";
            state = SENTBY;
        }
        else if (state == WAITING) {
            response = Commands.HI_IM + randomUUID();
            state = SENTNAME;
        } else if (state == SENTNAME) {
            if (StringUtils.startsWith(request, Commands.HI_IM)) {
                clientName = request.substring(8);
                response = HI + clientName;
                state = SENTHI;
            } else {
                response = Messages.SORRY_DID_NOT_UNDERSTAND_THAT;
            }
        } else if (state == SENTHI) {
            if (StringUtils.startsWith(request, Commands.ADD_NODE)) {
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
                    return helper.shortestPath(parts[2], parts[3]);
                }
            } else if (StringUtils.startsWith(request, Commands.CLOSER_THAN)){
                response = Messages.ERROR_NODE_NOT_FOUND;
            } else {
                response = Messages.SORRY_DID_NOT_UNDERSTAND_THAT;
                state = WAITING;
            }
        }
        System.out.println("Server: " + response);
        return response;
    }

}
