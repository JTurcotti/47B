public class VertexTree {


    public static void main(String[] args) {
	VertexHeap heap = new VertexHeap();
	for (int i = 0; i < 10; i++) {
	    Vertex v = new Vertex();
	    v.distance = i;
	    heap.add(v);
	    heap.display();
	}
	for (int i = 0; i < 10; i++) {
	    System.out.println(heap.remove().distance);
	}
    }
}
	
	
