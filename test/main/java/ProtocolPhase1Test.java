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
        Server.weightedMultigraph = new AsSynchronizedGraph<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));
    }

    public ProtocolPhase1Test(String inputString, String expectedResult) {
        this.inputString = inputString;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection commands() {
        return Arrays.asList(new Object[][] {
                { null, Commands.HI_IM + Protocol.PATTERN_UUID},
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { Commands.HI_IM + UUID.randomUUID(), Messages.HI + Protocol.PATTERN_UUID},
                { "UNKNOWN", Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { Commands.HI_IM, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
                { null, Messages.SORRY_DID_NOT_UNDERSTAND_THAT },
        });
    }

    @Test
    public void testProcessInputForPhase1(){
        System.out.println("Parameterized Command is : " + inputString);
        Pattern pattern = Pattern.compile(expectedResult);
        String serverOutput = protocol.processInput(inputString);
        assertTrue(pattern.matcher(serverOutput).matches());
    }
}
