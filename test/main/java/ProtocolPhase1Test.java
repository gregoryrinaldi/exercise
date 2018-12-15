package main.java;

import main.java.constants.Commands;
import main.java.constants.Messages;
import main.java.constants.Patterns;
import main.java.core.Protocol;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ProtocolPhase1Test {

    private String inputString;
    private String expectedResult;
    private static Protocol protocol = new Protocol(null);


    @Before
    public void initialize() {
        Main.weightedMultigraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public ProtocolPhase1Test(String inputString, String expectedResult) {
        this.inputString = inputString;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection commands() {
        return Arrays.asList(new Object[][] {
                { null, Messages.HI_I_AM + Patterns.UUID },
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { Messages.HI_I_AM + UUID.randomUUID(), Messages.HI + Patterns.UUID },
                { Messages.UNKNOWN, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { Messages.HI_I_AM + UUID.randomUUID(), Messages.HI + Patterns.UUID },
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                //{ null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT }

        });
    }

    @Test
    public void testProcessInputForPhase1(){
        System.out.println("Parameterized Command is : " + inputString);
        Pattern pattern = Pattern.compile(expectedResult);

        String serverOutput = protocol.processInput(inputString);
        assertTrue(pattern.matcher(serverOutput).matches());

       // assertTrue(serverOutput.contains("WE SPOKE FOR"));

    }


    @Test
    public void testProcessInputWithAddNode(){

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        String serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_ALREADY_EXISTS));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid1);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_ALREADY_EXISTS));

        serverOutput = protocol.processInput(Commands.ADD_NODE + uuid2);
        assertTrue(serverOutput.matches(Messages.NODE_ADDED));
    }

    @Test
    public void testProcessInputWithRemoveNode(){

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

        serverOutput = protocol.processInput(Commands.ADD_EDGE + uuid1 + " " +  uuid2 + " " + 3);
        assertTrue(serverOutput.matches(Messages.ERROR_NODE_NOT_FOUND));
    }
}
