/**
 * Amoeba: Defines an Amoeba with a name, a parent, and children.
 */
public class Amoeba {
    public String name;                   // Amoeba's Name
    public Amoeba parent;                 // Amoeba's Parent
    public ArrayList<Amoeba> children;    // Amoeba's Children

    /**
     * Constructs a new Amoeba with a given name and parent.
     * @param name Name of this Amoeba.
     * @param parent Parent of this Amoeba.
     */
    public Amoeba(String name, Amoeba parent) {
	this.name = name;
	this.parent = parent;
	children = new ArrayList<Amoeba>();
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
