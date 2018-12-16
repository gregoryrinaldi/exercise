package main.java.constants;

import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static main.java.Server.weightedMultigraph;

public final class ProtocolHelper {

    public synchronized boolean removeNode(String value){
        boolean removed = false;
        try {
            Set<DefaultWeightedEdge> incomingEdgesOf = weightedMultigraph.incomingEdgesOf(value);
            Set<DefaultWeightedEdge> outgoingEdgesOf = weightedMultigraph.outgoingEdgesOf(value);
            Set<DefaultWeightedEdge> collect = Stream.concat(
                    incomingEdgesOf.stream(), outgoingEdgesOf.stream()).collect(Collectors.toSet());
            weightedMultigraph.removeAllEdges(collect);
            removed = weightedMultigraph.removeVertex(value);
        } catch (Exception e){
            // TODO log a warning
        }
        return removed;
    }

    public synchronized boolean addNode(String request){
        return weightedMultigraph.addVertex(request.substring(9));
    }

    public synchronized boolean addEdge(String x, String y, String weight) {
        boolean added = false;
        try {
            if (weightedMultigraph.containsVertex(x) && weightedMultigraph.containsVertex(y)) {
                DefaultWeightedEdge defaultWeightedEdge = weightedMultigraph.addEdge(x, y);
                added = true;
                if (defaultWeightedEdge != null) {
                    weightedMultigraph.setEdgeWeight(defaultWeightedEdge, Double.parseDouble(weight));
                }
            }
        } catch (Exception e){
            // TODO log a warning
        }

        return added;
    }

    public synchronized DefaultWeightedEdge removeEdge(String x, String y) {
        return weightedMultigraph.removeEdge(x, y);
    }

    public synchronized String shortestPath(String x, String y) {

        BellmanFordShortestPath<String, DefaultWeightedEdge> bellmanFordShortestPath = new BellmanFordShortestPath<>(weightedMultigraph);
        return String.valueOf((int) bellmanFordShortestPath.getPathWeight(x, y));
    }
}
