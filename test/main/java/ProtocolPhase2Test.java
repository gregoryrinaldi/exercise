package main.java;

import main.java.constants.Commands;
import main.java.constants.Messages;
import main.java.core.Protocol;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ProtocolPhase2Test {

    private Protocol protocol;

    @Before
    public void initialize() {
        protocol = new Protocol(null);
        Server.weightedMultigraph = new AsSynchronizedGraph<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));
    }

    @Test
    public void testProcessInput(){

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        String serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_ALREADY_EXISTS));

        serverOutput = protocol.processInput(Commands.REMOVE_NODE + UUID.randomUUID());
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));

        serverOutput = protocol.processInput(Commands.REMOVE_NODE + UUID.randomUUID());
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));

        serverOutput = protocol.processInput(Commands.REMOVE_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.NODE_REMOVED));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_ALREADY_EXISTS));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid2);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));

        serverOutput = protocol.processInput(Commands.ADD_EDGE + uuid1);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));

        serverOutput = protocol.processInput(Commands.ADD_EDGE + uuid1 + " " +  UUID.randomUUID() + " " + 3);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));

        serverOutput = protocol.processInput(Commands.ADD_EDGE + uuid1 + " " +  uuid2 + " " + 3);
        assertTrue(serverOutput.matches(Messages.EDGE_ADDED));
      /*
        serverOutput = protocol.processInput(Commands.ADD_EDGE + uuid1 + " " +  uuid2 + " " + 3);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));
        */
    }
}
