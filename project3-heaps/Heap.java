import java.util.*;

public class Heap
{
    private final boolean DEBUGGING = true;

    private int size = 0;
    private Element root = null;
    private Element last = null;

    //checks if heap is empty, O(1)
    public boolean isEmpty() {
	return root == null;
    }

    //returns the max element, if it exists, O(1)
    public int top() {
	if (isEmpty()) {
	    throw new NoSuchElementException("Heap is empty!");
	} else {
	    return root.value;
	}
    }
    
    //adds a number to the heap, log n (going up) + log n (going down) + log n (bubbling up) = O(log n)
    public void add(int n) {
	if (root == null) {
	    root = new Element(n, null);
	    last = root;
	} else if (last.isLeft()) {
	    last = last.parent.addRight(n);
	} else {
	    Element e = last;
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
	    last = e.addLeft(n);
	}
	size++;
	bubbleUp(last);
    }

    //removes top value from heap and returns it, log n (going up) + log n (going down) + log n (bubbling down) = O(log n)
    public int remove() {
	int top = top();
	
	swap(root, last);

	if (last == root) {
	    root = null;
	    last = null;
	} else if (last.isRight()) {
	    last.parent.right = null;
	    last = last.parent.left;
	} else {
	    Element e = last;
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
    private void bubbleUp(Element e) {
	if (e != root && e.value > e.parent.value) {
	    swap(e, e.parent);
	    bubbleUp(e.parent);
	}
    }

    //swap values with max child until heap property is restored, O(log n)
    private void bubbleDown(Element e) {
	if (e != null && e.left != null) {
	    if (e.right != null && e.right.value > e.left.value) {
		swap(e, e.right);
		bubbleDown(e.right);
	    } else {
		swap(e, e.left);
		bubbleDown(e.left);
	    }
	}
    }

    //swap the values of two nodes, O(1)
    private void swap(Element e, Element f) {
	int temp = e.value;
	e.value = f.value;
	f.value = temp;
    }
    
    //display the heap, O(n)
    public void display()
    {
        System.out.println("Heap (" + size + " elements):");
        displayHelper(root, 0);
        System.out.println();
    }

    private void displayHelper(Element e, int indentLevel)
    {
	if (e == null) return;

        displayHelper(e.right, indentLevel + 1);
        for (int j = 0; j < indentLevel; j++)
        {
            System.out.print("  ");
        }
        System.out.println(e.value);
        displayHelper(e.left, indentLevel + 1);
    }


    //check if tree obeys max-heap property, O(n)
    public void check() {
	if (root != null)
	    root.check();
    }

    //nodes of tree
    class Element {
	int value;
	Element left, right, parent;

	public Element(int value, Element parent) {
	    this.value = value;
	    this.parent = parent;
	}

	//check if tree rooted at this element obeys the max-heap property, O(n)
	public void check() {
	    if (left != null) {
		assert left.value < value: "checking left value of " + value;
		left.check();
	    }
	    if (right != null) {
		assert right.value < value: "checking right value of " + value;
		right.check();
	    }
	}

	//checks if node is a left node, O(1)
	public boolean isLeft() {
	    return !(this == root) && this == parent.left;
	}

	//checks if node is a right node, O(1)
	public boolean isRight() {
	    return !(this == root) && this == parent.right;
	}

	//adds and returns new node to left, O(1)
	public Element addLeft(int n) {
	    assert left == null: "checking if " + value + " is open to the left";
	    left = new Element(n, this);
	    return left;
	}

	//adds and returns new node to right, O(1)
	public Element addRight(int n) {
	    assert right == null: "checking if " + value + " is open to the right";
	    right = new Element(n, this);
	    return right;
	}
    }


    public static void main(String[] args) {
	Heap h = new Heap();
	for (int i = 0; i < 5; i++) {
	    System.out.println("Adding " + i + " to heap");
	    h.add(i);
	    h.display();
	}
	for (int i = 0; i < 5; i++) {
	    System.out.println("Removing " + h.remove() + " from heap");
	    h.display();
	}
    }
}
