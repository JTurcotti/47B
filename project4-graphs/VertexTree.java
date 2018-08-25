/*  Each Vertex is given a godparent, ie its prospective parent in the tree, this reflects the node that most recently updated its distance to the tree, ie the closest node in the tree
 */

import java.util.*;

public class VertexTree {
    //root of MST
    Vertex root;

    VertexHeap heap;

    //builds tree, precondition: root points to the root
    public void buildTree(List<Vertex> vertices) {
	//build heap
	heap = new VertexHeap();
	for (Vertex v: vertices) {
	    v.setGodparent(root);
	    if (v != root) {
		heap.add(v);
	    }
	}

	heapToTree(root);
    }
	
    
    //takes the first vertex, the root, and proceeds to add all other vertices from heap to a tree around it
    public void heapToTree(Vertex r) {
	root = r;
	
	Vertex next = r;
	
	do {
	    //add it to the tree
	    next.addToTree();

	    //update the distances of its neighbors in the heap
	    for (Vertex v: next.neighbors()) {
		if (!v.inTree && next.distanceTo(v) < v.distance) {
		    heap.changeDistance(v, next.distanceTo(v));

		    //if a distance was changed of a heap node, make this the new godparent of that heap node
		    v.setGodparent(next);
		}
	    }
	    
	    //get next closest vertex
	    next = heap.remove();
	    //stop when heap is exhausted
	} while (next != null);
    }

    //testing methods:

    public boolean treeContains(Vertex v) {
	return subtreeContains(root, v);
    }
    
    private boolean subtreeContains(Vertex r, Vertex v) {
	if (r == v) return true;
	for (Vertex child: r.children())
	    if (subtreeContains(child, v))
		return true;
	return false;
    }

    public int treeLength() {
	return subtreeLength(root);
    }

    private int subtreeLength(Vertex r) {
	int length = 0;
	for (Vertex child: r.children())
	    length += r.distanceTo(child) + subtreeLength(child);
	return length;
    }
			    
    
    public void test(int rootNum) {

	//declare vertices
	Vertex one = new Vertex();
	Vertex two = new Vertex();
	Vertex three = new Vertex();
	Vertex four = new Vertex();
	Vertex five = new Vertex();
	Vertex six = new Vertex();
	
	Vertex[] vertices = {one, two, three, four, five, six};

	//set edge lenghths

	Vertex.addEdge(one, two, 1);
	Vertex.addEdge(one, three, 2);
	Vertex.addEdge(two, three, 1);
	Vertex.addEdge(two, five, 2);
	Vertex.addEdge(two, four, 1);
	Vertex.addEdge(three, four, 2);
	Vertex.addEdge(three, six, 1);
	Vertex.addEdge(four, five, 2);
	Vertex.addEdge(four, six, 1);
	Vertex.addEdge(five, six, 1);

	//set root and build MST!
	root = vertices[rootNum];
	buildTree(Arrays.asList(vertices));


	//test if MST contains all nodes

	boolean containsAll = true;
	for (int i = 0; i < 6; i++) {
	    if (!treeContains(vertices[i]))
		containsAll = false;
	}
	System.out.println("Tree contains all nodes? " + containsAll);

	//test length of tree

	System.out.println("Length of tree: " + treeLength());
    }

    public static void main(String[] args) {

	for (int rootNum = 0; rootNum < 6; rootNum++) {
	    VertexTree vt = new VertexTree();
	    System.out.println("\nTesting with rootNum: " + rootNum);
	    vt.test(rootNum);
	}
	
    }
}
	
	
