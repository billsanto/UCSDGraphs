/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.*;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 2

	HashMap<GeographicPoint, MapNode> nodeMap;
	HashSet<MapEdge> edgeSet;

	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 2
		nodeMap = new HashMap<>();
		edgeSet = new HashSet<>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 2
		return nodeMap.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 2
		return this.nodeMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 2
		return this.edgeSet.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 2

		if (this.nodeMap.containsKey(location)) {
			return false;
		}

		MapNode node = new MapNode(location);
		this.nodeMap.put(location, node);

		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method in WEEK 2
		Set<GeographicPoint> points = this.getVertices();

		if (from == null || to == null || roadName == null || roadType == null || length < 0 ||
				(! points.contains(from)) ||
				(! points.contains(to)) ) {
			throw new IllegalArgumentException("Invalid or missing arguments to add an edge.");
		}

		MapEdge edge = new MapEdge(from, to, roadName, roadType, length);
		edgeSet.add(edge);
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}

	/** Given that the edge set has been built, iterate though the edges and add neighbors (destination vertex of the edge)
	 *   to the node instance (source vertex of the edge)
	 *
	 * @param node
	 * @return: list of neighbor nodes as a convenience
     */
	private void computeNeighbors(MapNode node) {
		GeographicPoint nodePoint = node.getLocation();

		for (MapEdge edge: edgeSet) {
			if (edge.getSourcePtFromEdge().equals(nodePoint)) {
				boolean addedNeighbor = node.addNeighbor(nodeMap.get(edge.getDestPtFromEdge()));
				if (! addedNeighbor) {
					System.out.println("Unable to add a neighbor to a node");
					throw new IllegalArgumentException();
				}
			}
		}

//		return node.getNeighbors();
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 2
		// This is adapted from bfs in the Maze class

		MapNode startNode = this.nodeMap.get(start); //new MapNode(start);
		MapNode goalNode = this.nodeMap.get(goal); //new MapNode(goal);


		HashSet<MapNode> visited = new HashSet<MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashMap<GeographicPoint, MapNode> parentMap = new HashMap<GeographicPoint, MapNode>();
		toExplore.add(startNode);

		while (!toExplore.isEmpty()) {
			MapNode currentNode = toExplore.remove();
//			System.out.println("currentNode: " + currentNode.getLocation().toString());
			if (currentNode == goalNode) {
				break;
			}

			this.computeNeighbors(currentNode);
			List<MapNode> neighbors = currentNode.getNeighbors();  // see new method above
			ListIterator<MapNode> it = neighbors.listIterator(neighbors.size());

			while (it.hasPrevious()) {
				MapNode next = it.previous();

//				System.out.println("next: " + next.getLocation().toString());
				if (! visited.contains(next)) {
					// Hook for visualization.  See writeup.
					nodeSearched.accept(next.getLocation());

					visited.add(next);
					parentMap.put(next.getLocation(), currentNode);
					toExplore.add(next);
				}
			}
		}

		// Needed for the second test, in the event there is no path, since the edges are directed
		if (parentMap.size() == 0) {
			System.out.println("No path exists");
			return null;  // null is expected, rather than an empty list as in the Maze
		}

		// reconstruct the path
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode currentNode = goalNode;

		// This loop backs out from the goal node and adds each path node to the path variable
		while (currentNode != startNode) {
			GeographicPoint currentLocation = currentNode.getLocation();
			path.addFirst(currentLocation);
			MapNode parentNode = parentMap.get(currentLocation);
			currentNode = parentNode;

			// Deprecated.  Formerly, parentMap had a node, node structure, now has geopoint, node for fast lookups
//			for (MapNode n: parentMap.keySet()) {
//
//				// Note that parentMap is a hash which maps the next node as key to the current node as value
//				//  So if the current node matches the key of parentMap (key is the next node in parentMap)
//				//  return the value of parentMap, which is the node prior to the current node.
//				if (n.getLocation().equals(currentNode.getLocation())) {
//					currentNode = parentMap.get(n);
//					break;
//				}
//			}
		}

		path.addFirst(startNode.getLocation());  // finally, add the start node to the path

		return path;
	}

//	private class QueueNode {
//
//		MapNode node;
//		int distance;
//
//		/** A node for exclusive use for the current node, removed from the priority queue
//		 *  This class provides a means to associate the node with its shortest distance from source
//		 *
//		 * @param node A neighbor node, relative to the current node.
//		 * @param distance: Reflects distance from current Node to the next neighbor node.
//         */
//		public QueueNode(MapNode node, int distance) {
//			this.node = node;
//			this.distance = distance;
//		}
//
//		public MapNode getNode() {
//			return node;
//		}
//
//		public int getDistance() {
//			return distance;
//		}
//	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		MapNode startNode = this.nodeMap.get(start); // Don't do new MapNode(start) b/c we need references intact for computeNeighbors():
		MapNode goalNode = this.nodeMap.get(goal);

		HashSet<MapNode> visited = new HashSet<MapNode>();
		PriorityQueue<MapNode> priorityQueue = new PriorityQueue<>(new Comparator<MapNode>() {
			@Override
			public int compare(MapNode o1, MapNode o2) {
				return ((Double) o1.getDistanceFromSource()).compareTo(o2.getDistanceFromSource());
			}
		});

		for (MapNode node: this.nodeMap.values()) {
			node.initializeDistances();
		}

		// in contrast to bfs, do not stop if current == goal, but exhaust the queue to explore all possible routes
		HashMap<GeographicPoint, MapNode> parentMap = new HashMap<GeographicPoint, MapNode>();
		startNode.setDistanceFromSource(0);
		priorityQueue.add(startNode);
		visited.add(startNode);
		System.out.println("New Map");
		int counter = 0;

		while (! priorityQueue.isEmpty()) {
//			System.out.printf("Queue Size: %d%n", priorityQueue.size());
			MapNode currentNode = priorityQueue.remove();
			counter++;

			this.computeNeighbors(currentNode);
			System.out.printf("current: %s %s%n", currentNode.getLocation().toString(), currentNode.getDistanceFromSource());

			if (currentNode == goalNode) {
				break;
			}

			List<MapNode> neighbors = currentNode.getNeighbors();  // see new method above
			ListIterator<MapNode> it = neighbors.listIterator(neighbors.size());


			while (it.hasPrevious()) {
				// In contrast to bfs, removed the if visited condition and allow nodes to be revisited
				MapNode nextNeighborNode = it.previous();
				nextNeighborNode.setEstimatedDistanceToGoal(goalNode);

				System.out.printf("next: %s %s%n", nextNeighborNode.getLocation().toString(), nextNeighborNode.getAstarDistance());
				// Hook for visualization.  See writeup.
				nodeSearched.accept(nextNeighborNode.getLocation());
				visited.add(nextNeighborNode);

				double distFromCurrentToNext = nextNeighborNode.getLocation().distance(currentNode.getLocation());
				double distFromSourceToCurrent = currentNode.getDistanceFromSource();
				double distFromSourceToNext = nextNeighborNode.getDistanceFromSource();
				double proposedDistFromSourceToNext = distFromSourceToCurrent + distFromCurrentToNext;

				if (proposedDistFromSourceToNext < distFromSourceToNext) {
//					System.out.println("added to queue");
					nextNeighborNode.setDistanceFromSource(proposedDistFromSourceToNext);
					parentMap.put(nextNeighborNode.getLocation(), currentNode);
					priorityQueue.add(nextNeighborNode);
				}
			}
//			parentMap.put(goalNode.getLocation(), currentNode);
		}
		System.out.println("count removed: " + counter);

		// Needed for the second test, in the event there is no path, since the edges are directed
		if (parentMap.size() == 0) {
			System.out.println("No path exists");
			return null;  // null is expected, rather than an empty list as in the Maze
		}

		// reconstruct the path
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();

		MapNode currentNode = goalNode;

		System.out.println("Back tracing path:");
		// This loop backs out from the goal node and adds each path node to the path variable
		while (currentNode != startNode) {
			GeographicPoint currentLocation = currentNode.getLocation();
			path.addFirst(currentLocation);
			System.out.printf("node: %s %s%n", currentLocation.toString(), currentNode.getDistanceFromSource());
			MapNode parentNode = parentMap.get(currentLocation);
			currentNode = parentNode;
		}

		path.addFirst(startNode.getLocation());  // finally, add the start node to the path

		return path;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		MapNode startNode = this.nodeMap.get(start); // Don't do new MapNode(start) b/c we need references intact for computeNeighbors():
		MapNode goalNode = this.nodeMap.get(goal);

		HashSet<MapNode> visited = new HashSet<MapNode>();

		PriorityQueue<MapNode> priorityQueue = new PriorityQueue<>(new Comparator<MapNode>() {
			@Override
			public int compare(MapNode o1, MapNode o2) {
				return ((Double) o1.getAstarDistance()).compareTo(o2.getAstarDistance());
			}
		});

		for (MapNode node: this.nodeMap.values()) {
			node.initializeDistances();
		}

		HashMap<GeographicPoint, MapNode> parentMap = new HashMap<GeographicPoint, MapNode>();
		startNode.setDistanceFromSource(0);
		priorityQueue.add(startNode);
		visited.add(startNode);
		System.out.println("New Map");
		int counter = 0;

		while (! priorityQueue.isEmpty()) {
//			System.out.printf("Queue Size: %d%n", priorityQueue.size());
			MapNode currentNode = priorityQueue.remove();
			counter++;

			this.computeNeighbors(currentNode);
			System.out.printf("current: %s %s%n", currentNode.getLocation().toString(), currentNode.getDistanceFromSource());

			if (currentNode == goalNode) {
				break;
			}

			List<MapNode> neighbors = currentNode.getNeighbors();  // see new method above
			ListIterator<MapNode> it = neighbors.listIterator(neighbors.size());

			while (it.hasPrevious()) {
				// In contrast to bfs, removed the if visited condition and allow nodes to be revisited
				MapNode nextNeighborNode = it.previous();
				nextNeighborNode.setEstimatedDistanceToGoal(goalNode);

//				System.out.printf("next: %s %s%n", nextNeighborNode.getLocation().toString(), nextNeighborNode.getAstarDistance());
				// Hook for visualization.  See writeup.
				nodeSearched.accept(nextNeighborNode.getLocation());
				visited.add(nextNeighborNode);

				double distFromCurrentToNext = nextNeighborNode.getLocation().distance(currentNode.getLocation());
				double distFromSourceToCurrent = currentNode.getDistanceFromSource();
				double distFromSourceToNext = nextNeighborNode.getDistanceFromSource();
				double proposedDistFromSourceToNext = distFromSourceToCurrent + distFromCurrentToNext;

				if (proposedDistFromSourceToNext < distFromSourceToNext) {
//					System.out.println("added to queue");
					nextNeighborNode.setDistanceFromSource(proposedDistFromSourceToNext);
					parentMap.put(nextNeighborNode.getLocation(), currentNode);
					priorityQueue.add(nextNeighborNode);
				}
			}
		}
		System.out.println("count removed: " + counter);

		// Needed for the second test, in the event there is no path, since the edges are directed
		if (parentMap.size() == 0) {
			System.out.println("No path exists");
			return null;  // null is expected, rather than an empty list as in the Maze
		}

		// reconstruct the path
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode currentNode = goalNode;

		// This loop backs out from the goal node and adds each path node to the path variable
		while (currentNode != startNode) {
			GeographicPoint currentLocation = currentNode.getLocation();
			path.addFirst(currentLocation);
			MapNode parentNode = parentMap.get(currentLocation);
			currentNode = parentNode;
		}

		path.addFirst(startNode.getLocation());  // finally, add the start node to the path

		return path;
	}

	
	
	public static void main(String[] args)
	{
////		System.out.print("Making a new map...");
//		MapGraph theMap = new MapGraph();
//		System.out.print("DONE. \nLoading the map...");
//		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
////		System.out.println("DONE.");
////
////
////
//		GeographicPoint start = new GeographicPoint(1,1);
//		GeographicPoint end = new GeographicPoint(8,-1);
////		List<GeographicPoint> route = theMap.bfs(start,end);
//
//		// You can use this method for testing.
//
//		// Use this code in Week 3 End of Week Quiz
////		MapGraph theMap = new MapGraph();
////		System.out.print("DONE. \nLoading the map...");
////		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
////		System.out.println("DONE.");
////
////		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
////		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
//
//
////		List<GeographicPoint> route = theMap.dijkstra(start,end);
//		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		
	}
	
}
