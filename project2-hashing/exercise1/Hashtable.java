/**
 * Hashtable: Implements a chained hash table using linked lists. Four
 * different hash functions are defined.
 */
public class Hashtable
{
    
    // Constants

    private static final int MAGIC1 = 257;      // ETH Magic Number

    private static final int MAGIC2 = 4;        // GNU-cpp Magic Number

    private static final int MAGIC3 = 613;      // GNU-cc1 Magic Number

    // Instance Variables

    private int functionNum; //which hash function to use

    private static boolean DEBUGGING = false;   // Debug Flag

    private LinkedList[] myTable;
      // Chained hash table implemented as an array of linked lists. Note that
      // this LinkedList class is custom and is defined at the bottom of this
      // file! This is not the same class defined in java.util.LinkedList!


    /**
     * Constructs a new Hashtable with a specified size.
     * @param size Number of chains (linked lists).
     */
    public Hashtable(int size, int functionNum)
    {
        myTable = new LinkedList[size];
        for (int k = 0; k < size; k++)
        {
            myTable[k] = new LinkedList();
        }

	this.functionNum = functionNum;
    }

    /**
     * Computes and returns the hash value of the given key.
     * @param key Key to compute hash of.
     * @return Hash value of key.
     */
    private long hash(String key)
    {
        // TODO: Uncomment one of the following lines to use the corresponding
        //       hash function.
	switch (functionNum) {
	case 1: return hash1(key);
        case 2: return hash2(key);
        case 3: return hash3(key);
	case 4: return Math.abs(key.hashCode());
	    
	}
	return 0;
    }
    
    /**
     * Slight variation on the ETH hashing algorithm
     * @param key Key to compute hash of.
     * @return Hash value of key.
     */
    private static long hash1(String key)
    {
        long h = 1;

        for (int k = 0; k < key.length(); k++)
        {
            h = ((h % MAGIC1) + 1) * (int)key.charAt(k);
        }

        return h;
    }

    /**
     * Slight variation on the GNU-cpp hashing algorithm
     * @param key Key to compute hash of.
     * @return Hash value of key.
     */
    private static long hash2(String key)
    {
        long h = 0;

        for (int k = 0; k < key.length(); k++)
        {
            h = MAGIC2 * h + (int)key.charAt(k);
        }

        return h << 1 >>> 1;
    }

    /**
     * Slight variation on the GNU-cc1 hashing algorithm
     * @param key Key to compute hash of.
     * @return Hash value of key.
     */
    private static long hash3(String key)
    {
        long h = key.length();

        for (int k = 0; k < key.length(); k++)
        {
            h = MAGIC3 * h + (int)key.charAt(k);
        }

        return h << 2 >>> 2;
    }

    /**
     * Add the key to the table. The value is included just for compatibility
     * with the Hashtable class in java.util.
     * @param key Key.
     * @param value Value.
     */
    public void put(String key, Integer value)
    {
        int h = (int)(hash(key) % myTable.length);
        if (!myTable[h].find(key))
        {
            myTable[h].insert(new Info(key, value));
            if (DEBUGGING)
            {
                System.out.println("Inserting " + key);
            }
        }
        else
        {
            System.err.println(key + " already in table.");
        }
    }
    
    /**
     * Returns whether or not the given key is contained in this Hashtable.
     * @param key Key to search for.
     * @return True if key is in the table, and false otherwise.
     */
    public boolean containsKey(String key)
    {
        int h = (int)(hash(key) % myTable.length);
        return myTable[h].find(key);
    }

    /**
     * Print statistics about the table:
     * - Maximum length of a collision list
     * - Optimal length of a collision list
     * - Average number of comparisons needed for a successful search
     * - Standard deviation for the number of comparisons needed for a
     *   successful search
     *
     * The average number of comparisons is computed by summing the total
     * number of comparisons necessary to find each element in the table, and
     * dividing that total by the number of elements.
     *
     * The standard deviation is computed by finding the total over all
     * elements of the square of the number of comparisons necessary to find
     * that element, dividing by the number of elements, subtracting the square
     * of the average number of comparisons, and taking the square root of that
     * difference.
     */
    public void printStatistics()
    {
        int elementCount = 0;
        int maxLen = 0;
        int sum = 0;
        int sqSum = 0;
        double avg;

        for (int k = 0; k < myTable.length; k++)
        {
            int len = myTable[k].size();
            maxLen = (len > maxLen) ? len : maxLen;
            elementCount += len;

            // 1 + 2 + 3 + ... + len = len(len+1)/2
            sum += (len*(len+1))/2;

            // 1^2 + 2^2 + 3^2 + ... + len^2 = len(len+1)(2 len+1)/6
            sqSum += (len * (len + 1) * (2 * len + 1)) / 6;
        }
        avg = ((float)sum) / elementCount;

        System.out.println("Number of elements  = " + elementCount);
        System.out.println("Maximum list length = " + maxLen);
        System.out.println("Optimal list length = "
            + (elementCount + myTable.length - 1) / myTable.length);
        System.out.println("Average # compares  = " + avg);
        System.out.println("Standard deviation  = "
            + Math.sqrt(((double)sqSum) / elementCount - avg * avg));
    }

    /**
     * Simple driver for testing basic functionality of the Hashtable methods.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args)
    {
        Hashtable table = new Hashtable(1000, 1);
        table.put("Mike", new Integer(3));
        System.out.println("Does table contain Mike? " 
            + table.containsKey("Mike"));
        System.out.println("Does table contain Clancy? " 
            + table.containsKey("Clancy"));
        table.printStatistics();
    }
}

/**
 * Info: Key-value pair.
 */
class Info
{
    public String key;      // Key
    public Integer value;   // Value

    /**
     * Constructs a new Info object that stores a key-value pair.
     * @param s Key.
     * @param v Value.
     */
    public Info(String s, Integer v)
    {
        key = s;
        value = v;
    }

    /**
     * Returns whether or not this Info object is equal to a given String by
     * checking if the *key* of this Info object is equal to the String.
     * @param s String to check equality against.
     * @return True if key equals s, otherwise false.
     */
    public boolean equals(String s)
    {
        return key.equals(s);
    }

    /**
     * Returns whether or not this Info object is equal to another generic
     * Object by checking if the *key* of this Info object is equal to the
     * other Object casted as a String.
     * @param s Object to check equality against.
     * @return True if key equals (String)s, otherwise false.
     */
    public boolean equals(Object s)
    {
        return key.equals((String)s);
    }
}

/**
 * LinkedList: Custom singly linked list.
 */
class LinkedList
{
    private int myCount;    // Number of items in the list
    private Node myHead;    // Reference to the list's first node

    /**
     * Node: List node containing an Info object and a reference to the next
     * list node.
     */
    private class Node
    {
        public Info myInfo; // Info
        public Node myNext; // Next Node
    }

    /**
     * Constructs and empty singly linked list.
     */
    public LinkedList()
    {
        myCount = 0;
        myHead = null;
    }
    
    /**
     * Insert at the start of the list.
     * @param x Info to insert.
     */
    public void insert(Info x)
    {
        Node ptr = new Node();
        ptr.myInfo = x;
        ptr.myNext = myHead;
        myHead = ptr;
        myCount++;
    }
    
    /**
     * Return the length of the linked list.
     * @return Number of elements in the list.
     */
    public int size()
    {
        return myCount;
    }
    
    /**
     * Return a reference to a node of the linked list that contains s,
     * or null if no such node exists.
     * @param s String to search for.
     * @return True if this linked list has a node that equals s, otherwise
     *         false.
     */
    public boolean find(String s)
    {
        Node ptr;
        for (ptr = myHead; ptr != null && !ptr.myInfo.equals(s);
             ptr = ptr.myNext);
        return ptr != null;
    }
}
