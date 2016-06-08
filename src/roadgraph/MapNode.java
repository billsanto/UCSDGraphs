package roadgraph;

import geography.GeographicPoint;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created on 5/19/16.
 */
public class MapNode {

    private GeographicPoint location;
    private List<MapNode> neighbors;
    private double distanceFromSource;
    private double estimatedDistanceToGoal;
    private double astarDistance;
    private MapNode sourceNode;
    private MapNode destNode;

    /**
     * Create a MapNode and populate the member variables
     **/
    public MapNode(GeographicPoint location) {
        this.location = location;
        this.neighbors = new ArrayList<>();

        this.distanceFromSource = Double.MAX_VALUE;
        this.estimatedDistanceToGoal = Double.MAX_VALUE;
        this.astarDistance = Double.MAX_VALUE;
    }

    /**
     * Return the location associated with this node
     *
     * @return
     */
    public GeographicPoint getLocation() {
        return this.location;
    }

    /**
     * Add each neighbor of this node, which is associated with the destination of each directed edge
     *
     * @param neighbor
     */
    public boolean addNeighbor(MapNode neighbor) {
        try {
            neighbors.add(neighbor);
            return true;
        } catch (Exception e) {
            System.out.println("Error when adding neighbor");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reinitialize as a new search algorithm begins
     */
    public void initializeDistances() {
        this.distanceFromSource = Double.MAX_VALUE;
        this.estimatedDistanceToGoal = Double.MAX_VALUE;
        this.astarDistance = Double.MAX_VALUE;
    }

    public double getEstimatedDistanceToGoal() {
        return estimatedDistanceToGoal;
    }

    /**
     * The estimated distance from source to the destination, the sum of the current shortest distance from source
     * to this node, plus the heuristic of the direct distance from this node to the destination.
     * @param goal
     */
    public void setEstimatedDistanceToGoal(MapNode goal) {
        this.estimatedDistanceToGoal = this.location.distance(goal.getLocation());
        this.astarDistance = this.distanceFromSource + this.estimatedDistanceToGoal;
    }

    /**
     *
     * @return aStar distance--the estimated distance from source to the destination
     */
    public double getAstarDistance() {
        return astarDistance;
    }

    /**
     *
     * @param node Allow the node to keep track of the source node for this search
     */
    public void setSourceNode(MapNode node) {
        this.sourceNode = node;
    }

    /**
     * Set the destination node to compute the distance to goal, which can be computed if the distance to source has been computed
     * @param node A
     */
    public void setDestNode(MapNode node) {
        this.destNode = node;

        if (this.distanceFromSource < Double.MAX_VALUE) {
            setEstimatedDistanceToGoal(node);
        }
    }

    /**
     *
     * @return The currently shortest distance between this node and the source
     */
    public double getDistanceFromSource() {
        return this.distanceFromSource;
    }

    /**
     * Set an updated distance from source, as calculated by the algorithm, then update aStar
     * @param distanceFromSource
     */
    public void setDistanceFromSource(double distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
        this.astarDistance = this.distanceFromSource + this.estimatedDistanceToGoal;
    }

    /**
     * Return the list of neighbors of this node.
     *
     * @return list
     */
    public List<MapNode> getNeighbors() {
        return neighbors;
    }

    public static void main(String[] args) {

    }
}
