import java.util.*;

public class VertexHeap {

    private int size = 0;
    private Vertex root = null;
    private Vertex last = null;

    //checks if heap is empty, O(1)
    public boolean isEmpty() {
	return root == null;
    }

    //returns the max element, if it exists, O(1)
    public Vertex top() {
	if (isEmpty()) {
	    throw new NoSuchElementException("Heap is empty!");
	} else {
	    return root;
	}
    }
    
    //adds a Vertex to the heap, log n (going up) + log n (going down) + log n (bubbling up) = O(log n)
    public void add(Vertex v) {
	if (root == null) {
	    root = v;
	} else if (last.isLeft()) {
	    last.parent.right = v;
	    v.parent = last.parent;
	} else {
	    Vertex e = last;
	    while (true) {
		if (e == root) {
		    break;
		} else if (e.isLeft()) {
		    e = e.parent.right;
		    break;
		}
		e = e.parent;
	    }
	    while (e.left != null) {
		e = e.left;
	    }
	    e.left = v;
	    v.parent = e;
	}
	last = v;
	size++;
	bubbleUp(v);
    }

    //removes top value from heap and returns it, log n (going up) + log n (going down) + log n (bubbling down) = O(log n)
    public Vertex remove() {
	Vertex top = top();

	swap(root, last);

	if (last == root) {
	    root = null;
	    last = null;
	} else if (last.isRight()) {
	    last.parent.right = null;
	    last = last.parent.left;
	} else {
	    Vertex e = last;
	    while (true) {
		if (e == root) {
		    break;
		} else if (e.isRight()) {
		    e = e.parent.left;
		    break;
		}
		e = e.parent;
	    }
	    while (e.right != null) {
		e = e.right;
	    }
	    last.parent.left = null;
	    last = e;
	}
	
	size--;

	bubbleDown(root);

	return top;
    }	    

    //swap values with parent until heap prperty is restored, O(log n)
    private void bubbleUp(Vertex v) {
	while (v != root && v.distance < v.parent.distance) {
	    swap(v, v.parent);
	}
    }

    //swap values with max child until heap property is restored, O(log n)
    private void bubbleDown(Vertex v) {
	while (v != null && v.left != null) {
	    if (v.right != null && v.right.distance < v.left.distance && v.right.distance < v.distance) {
		swap(v, v.right);
	    } else if (v.left.distance < v.distance) {
		swap(v, v.left);
	    } else {
		break;
	    }
	}
    }

    
    //swap the positions of two vertices, O(1)
    private void swap(Vertex v, Vertex u) {
	
	if (root == v) root = u;
	else if (root == u) root = v;

	if (last == v) last = u;
	else if (last == u) last = v;

	//represent nearby nodes
	Vertex parent0, left0, right0, parent1, left1, right1;
	//represent position of v and u
	boolean leftChild0, leftChild1;

	//note special cases in following two sections for consecutive nodes
	
	//store v's position as 0 
	parent0 = (v.parent == u)? v: v.parent;
	left0 = (v.left == u)? v: v.left;
	right0 = (v.right == u)? v: v.right;
	leftChild0 = ((v.parent != null) && (v == v.parent.left));

	//store u's position as 1
	parent1 = (u.parent == v)? u: u.parent;
	left1 = (u.left == v)? u: u.left;
	right1 = (u.right == v)? u: u.right;
	leftChild1 = ((u.parent != null) && (u == u.parent.left));

	//give v position 1
	v.parent = parent1;
	if (parent1 != null) {
	    if (leftChild1) {
		parent1.left = v;
	    } else {
		parent1.right = v;
	    }
	}
	v.left = left1;
	if (left1 != null) {
	    left1.parent = v;
	}
	v.right = right1;
	if (right1 != null) {
	    right1.parent = v;
	}
	
	//give u position 0
	u.parent = parent0;
	if (parent0 != null) {
	    if (leftChild0) {
		parent0.left = u;
	    } else {
		parent0.right = u;
	    }
	}
	u.left = left0;
	if (left0 != null) {
	    left0.parent = u;
	}
	u.right = right0;
	if (right0 != null) {
	    right0.parent = u;
	}

    }

    
    //display the heap, O(n)
    public void display()
    {
        System.out.println("Heap (" + size + " elements):");
        displayHelper(root, 0);
        System.out.println();
    }

    private void displayHelper(Vertex v, int indentLevel)
    {
	if (v == null) return;

        displayHelper(v.right, indentLevel + 1);
        for (int j = 0; j < indentLevel; j++)
        {
            System.out.print("  ");
        }
        System.out.println(v.distance);
        displayHelper(v.left, indentLevel + 1);
    }


    //check if tree obeys max-heap property, O(n)
    public void check() {
	if (root != null)
	    root.check();
    }

}
