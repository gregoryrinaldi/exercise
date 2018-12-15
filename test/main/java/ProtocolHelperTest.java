package main.java;

import main.java.constants.ProtocolHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static main.java.Main.weightedMultigraph;

@RunWith(JUnit4.class)
public class ProtocolHelperTest {

    private ProtocolHelper weightedEdgeHelper = new ProtocolHelper();

    private String uid1 = UUID.randomUUID().toString();
    private String uid2 = UUID.randomUUID().toString();
    private String uid3 = UUID.randomUUID().toString();
    private String uid4 = UUID.randomUUID().toString();

    @Before
    public void before(){
        weightedMultigraph.addVertex(uid1);
        weightedMultigraph.addVertex(uid2);
        weightedMultigraph.addVertex(uid3);
        weightedMultigraph.addVertex(uid4);
    }

    @Test
    public void testRemoveNode(){
        assertTrue(weightedEdgeHelper.removeNode(uid1));
        assertFalse(weightedEdgeHelper.removeNode(uid1));
        assertTrue(weightedEdgeHelper.removeNode(uid4));
        assertFalse(weightedEdgeHelper.removeNode(uid4));
    }

    @Test
    public void testAddMultipleEdges(){
        assertTrue(weightedEdgeHelper.addEdge(uid1, uid2, "100"));
        assertTrue(weightedEdgeHelper.addEdge(uid1, uid2, "100"));
        assertTrue(weightedEdgeHelper.addEdge(uid2, uid3, "10"));
        assertTrue(weightedEdgeHelper.addEdge(uid2, uid3, "102"));
        assertTrue(weightedEdgeHelper.addEdge(uid1, uid4, "102"));
    }

    @Test
    public void testRemoveEdges(){
        weightedEdgeHelper.addEdge(uid1, uid2, "100");
        weightedEdgeHelper.removeEdge(uid1, uid2);
    }

}
