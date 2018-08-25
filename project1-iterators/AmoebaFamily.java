import java.util.*;

/**
 * AmoebaFamily: TODO: Write your class description here.
 *
 *  @author Joshua Turcotti
 *    @date Aug 24, 2018
 * @project Iterator Exercises
 */
public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba> {
    public Amoeba head;  // Head of the AmoebaFamily
    
    /**
     * Constructs a new AmoebaFamily containing a single Amoeba (the head of
     * the family).
     * @param name Name of the head Amoeba.
     */
    public AmoebaFamily(String name)
    {
        head = new Amoeba(name, null);
    }

    /**
     * Returns a new iterator for this AmoebaFamily.
     * @return New AmoebaIterator instance corresponding to this AmoebaFamily.
     */
    public Iterator<AmoebaFamily.Amoeba> iterator()
    {
	return new AmoebaIterator();
        //return new BreadthFirstAmoebaIterator();
    }
    
    /**
     * Add a new Amoeba child with a given name to the specified Amoeba parent.
     * Precondition: The AmoebaFamily contains an Amoeba named parentName.
     * @param parentName Name of the parent Amoeba that we want to add a child
     *                   to.
     * @param childName Name of the new Amoeba child.
     */
    public void addChild(String parentName, String childName)
    {
	Amoeba parent = null;
	for (Amoeba amoeba: this) {
	    if (parentName.equals(amoeba.name)) {
		parent = amoeba;
		break;
	    }
	}
	
	if (parent != null) {
	    parent.addChild(childName);
	}
	
    }
    
    /**
     * Print the names of all amoebas in the family. Names should appear in
     * preorder, with children's names printed oldest first. Members of the
     * family constructed with the main driver program below should be printed
     * in the following sequence:
     *
     *     Amos McCoy, mom/dad, me, Mike, Bart, Lisa, Homer, Marge, George,
     *     Martha, Ben, Leslie
     */
    public void print() {
        for (Amoeba amoeba: this) {
	    System.out.println(amoeba);
	}
    }

    /**
     * Construct a family of Amoebas, and then print the family tree using the
     * print() method as well as the AmoebaIterator.
     * @param args Command-line arguments.
     */
    public static void main(String[] args)
    {
        AmoebaFamily family = new AmoebaFamily("Amos McCoy");
        family.addChild("Amos McCoy", "mom/dad");
        family.addChild("mom/dad", "me");
        family.addChild("mom/dad", "Ben");
        family.addChild("mom/dad", "Leslie");
        family.addChild("me", "Mike");
        family.addChild("me", "Homer");
        family.addChild("me", "Marge");
        family.addChild("Mike", "Bart");
        family.addChild("Mike", "Lisa");
        family.addChild("Marge", "George");
        family.addChild("Marge", "Martha");

        System.out.println("Here's the family depth-first:");
        family.print();
	

        System.out.println("");
        System.out.println("Here it is again (breadth-first!)");
        Iterator<AmoebaFamily.Amoeba> iter = family.new BreadthFirstAmoebaIterator();
        while (iter.hasNext()) {
            System.out.println(((Amoeba)iter.next()));
        }
    }
    /**
     * AmoebaIterator: Amoebas in the family are iterated over in preorder,
     * with oldest children first. Members of the family constructed with the
     * main program above should be iterated over in the following sequence:
     *
     *     Amos McCoy, mom/dad, me, Mike, Bart, Lisa, Homer, Marge, George,
     *     Martha, Ben, Leslie
     *
     * Complete iteration of a family of N amoebas should take O(N) operations.
     */
    private class AmoebaIterator implements Iterator<AmoebaFamily.Amoeba> {
	private Stack<AmoebaFamily.Amoeba> stack;
	
	public AmoebaIterator() {
	    stack = new Stack<AmoebaFamily.Amoeba>();
	    stack.push(head);
	}
	
	public boolean hasNext() {
	    return !stack.empty();
	}
	
	public Amoeba next() {
	    Amoeba next = stack.pop();
	    for (int i = next.children.size() - 1; i >= 0; i-- ) {
		stack.push(next.children.get(i));
	    }
	    return next;
	}
    }
    /**
     *provides an Iterator object similar to the default AmoebaFamily iterator, but proceeding breadth-first not depth-first
     */
    private class BreadthFirstAmoebaIterator implements Iterator<AmoebaFamily.Amoeba> {
	private Queue<AmoebaFamily.Amoeba> queue;

	public BreadthFirstAmoebaIterator() {
	    queue = new LinkedList<AmoebaFamily.Amoeba>();
	    queue.add(head);
	}

	public boolean hasNext() {
	    return queue.peek() != null;
	}

	public Amoeba next() {
	    Amoeba next = queue.remove();
	    for (Amoeba amoeba: next.children) {
		queue.add(amoeba);
	    }
	    return next;
	}
    }

    /**
     * Amoeba: Defines an Amoeba with a name, a parent, and children.
     */
    public class Amoeba {
	public String name;                   // Amoeba's Name
	public Amoeba parent;                 // Amoeba's Parent
	public ArrayList<AmoebaFamily.Amoeba> children;    // Amoeba's Children

	/**
	 * Constructs a new Amoeba with a given name and parent.
	 * @param name Name of this Amoeba.
	 * @param parent Parent of this Amoeba.
	 */
	public Amoeba(String name, Amoeba parent) {
	    this.name = name;
	    this.parent = parent;
	    children = new ArrayList<AmoebaFamily.Amoeba>();
	}
    
	/**
	 * Constructs a new Amoeba with a given name, and adds it as the
	 * youngest child of this current Amoeba.
	 * @param childName Amoeba child name.
	 * @return the newly created child
	 */
	public Amoeba addChild(String childName) {
	    Amoeba child = new Amoeba(childName, this);
	    children.add(child);
	    return child;
	}

	/**
	 * Returns the String representation of this Amoeba.
	 * @return Name of this Amoeba.
	 */
	public String toString() {
	    return name;
	}
    }

}

