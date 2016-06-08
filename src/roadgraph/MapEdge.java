package roadgraph;

import geography.GeographicPoint;

/**
 * Created on 5/19/16.
 */
public class MapEdge {

    /** Edge attributes
     *
     */
    private GeographicPoint from;
    private GeographicPoint to;
    private String roadName;
    private String roadType;
    private double length;

    public MapEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length) {
        this.from = from;
        this.to = to;
        this.roadName = roadName;
        this.roadType = roadType;
        this.length = length;
    }

    /** Getter for returning the source vertex of the directed edge
     *
     * @return
     */
    public GeographicPoint getSourcePtFromEdge() {
        return this.from;
    }

    /** Getter for returning the destination vertex of the directed edge
     *
     * @return
     */
    public GeographicPoint getDestPtFromEdge() {
        return this.to;
    }

    public static void main(String[] args) {

    }
}
