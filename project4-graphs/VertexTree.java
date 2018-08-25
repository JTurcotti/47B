public class VertexTree {
    //root of MST
    final Vertex ROOT = new Vertex(null);

    VertexHeap heap = new VertexHeap();
    
    public void heapToTree() {
	Vertex next;
	while (!heap.isEmpty()) {
	    next = heap.remove();
	    next.addToTree();
	    for (Vertex v: next.neighbors()) {
		if (!v.inTree && next.distanceTo(v) < v.distance) {
		    heap.changeDistance(v, next.distanceTo(v));
		    v.godparent = next;
		}
	    }
	}	    
    }

    public boolean treeContains(Vertex v) {
	return subtreeContains(ROOT, v);
    }
    
    private boolean subtreeContains(Vertex root, Vertex v) {
	return (root != null) &&
	    ((root == v) ||
	     subtreeContains(root.left, v) ||
	     subtreeContains(root.right, v));
    }
    
    public boolean test() {

	Vertex A = new Vertex(ROOT);
	Vertex B = new Vertex(ROOT);
	Vertex C = new Vertex(ROOT);
	Vertex D = new Vertex(ROOT);
	
	Vertex.addEdge(A, C, 1);
	Vertex.addEdge(A, B, 2);
	Vertex.addEdge(B, C, 3);
	Vertex.addEdge(B, D, 4);
	Vertex.addEdge(C, D, 5);
	
	heap.add(A);
	heap.add(B);
	heap.add(C);
	heap.add(D);

	heapToTree();

	return treeContains(A) && treeContains(B) && treeContains(C) && treeContains(C) && treeContains(D);
    }

    public static void main(String[] args) {

	VertexTree vt = new VertexTree();
	System.out.println(vt.test());
	
    }
}
	
	
