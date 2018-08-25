import java.util.*;

public class Heap
{
    private final boolean DEBUGGING = true;

    private int size = 0;
    private Element root = null;
    private Element last = null;


    //adds a number to the heap
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
    }	    		

    /**
     *  Display the heap.
     */
    public void display()
    {
        System.out.println("Heap (" + size + " elements):");
        displayHelper(root, 0);
        System.out.println();
    }

    /**
     * Display the subheap rooted at element k, indenting each element
     * according to the indent level argument.
     */
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


    //check if tree obeys max-heap property
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

	//check if tree rooted at this element obeys the max-heap property
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

	//checks if node is a left node
	public boolean isLeft() {
	    return !(this == root) && this == parent.left;
	}

	//checks if node is a right node
	public boolean isRight() {
	    return !(this == root) && this == parent.right;
	}

	//adds and returns new node to left
	public Element addLeft(int n) {
	    assert left == null: "checking if " + value + " is open to the left";
	    left = new Element(n, this);
	    return left;
	}

	//adds and returns new node to right
	public Element addRight(int n) {
	    assert right == null: "checking if " + value + " is open to the right";
	    right = new Element(n, this);
	    return right;
	}
    }


    public static void main(String[] args) {
	Heap h = new Heap();
	for (int i = 0; i < 10; i++) {
	    h.add(i);
	}
	h.display();
    }
}
