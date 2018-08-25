import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * AmoebaFamily: TODO: Write your class description here.
 *
 *  @author Joshua Turcotti
 *    @date Aug 24, 2018
 * @project Iterator Exercises
 */
public class AmoebaFamily
{
    private Amoeba head;  // Head of the AmoebaFamily
    
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
    public AmoebaIterator iterator()
    {
        return new AmoebaIterator();
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
        // TODO: You supply this code for Exercise 1.
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
    public void print()
    {
        // TODO: You supply this code for Exercise 2.
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

        System.out.println("Here's the family:");
        family.print();

//        System.out.println("");
//        System.out.println("Here it is again!");
//        AmoebaIterator iter = family.iterator();
//        while (iter.hasNext())
//        {
//            System.out.println(((Amoeba)iter.next()));
//        }
    }

}

