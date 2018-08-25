import java.util.*;

class Vertex {
    //represents connected nodes in GRAPH, never changes once set
    private Hashtable<Vertex, Integer> connections = new Hashtable<>();

    //following two fields represent HEAP structure
    //represents distance to MST = priority in heap, possibly changed whenever a neighbor is added to MST
    int distance = Integer.MAX_VALUE;
    Vertex left = null;
    Vertex right = null;
    Vertex parent = null;


    //following two fields represent MST strucutre
    //represents the node that 'added' this one to the heap, overrwritten whenever distance is updated, becomes parent in MST
    Vertex godparent = null;
    //when a node is added to MST with this as parent, it is added to children
    private List<Vertex> children = new LinkedList<>();

    //MST methods:
    
    public void setChild(Vertex v) {
	assert(v.godparent == this): "checking node add to MST";
	children.add(v);
    }

    //GRAPH methods:
    
    public Set<Vertex> neighbors() {
	return connections.keySet();
    }

    public int distanceTo(Vertex v) {
	if (connections.containsKey(v)) {
	    return connections.get(v);
	} else {
	    //infinite distance = not connected
	    return Integer.MAX_VALUE;
	}
    }


    //HEAP methods:

    
    //check if tree rooted at this element obeys the max-heap property, O(n)
    public void check() {
	if (left != null) {
	    assert left.distance >= distance: "checking left value of node";
	    left.check();
	}
	if (right != null) {
	    assert right.distance >= distance: "checking right value of node";
	    right.check();
	}
    }

    //checks if node is a left node, O(1)
    public boolean isLeft() {
	return (parent != null) && (this == parent.left);
    }

    //checks if node is a right node, O(1)
    public boolean isRight() {
	return (parent != null) && (this == parent.right);
    }
}



    
