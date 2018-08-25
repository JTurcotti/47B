import java.util.*;

public class JugSolver
{
    private static boolean DEBUGGING = false;
    private int desired;
    private int capacity[];
    
    public JugSolver(int amt0, int amt1, int amt2, int d)
    {
        capacity = new int[3];
        capacity[0] = amt0;
        capacity[1] = amt1;
        capacity[2] = amt2;
        desired = d;
    }
    
    /**
     * Try to solve the puzzle, starting at configuration b.
     */
    public boolean tryPouring(JugContents b)
    {
        debugPrint(b.toString());
        if (b.jugs[0] == desired)
        {
            debugPrint("Jug 0 contains " + b.jugs[0]);
            return true;
        }
        else if (b.jugs[1] == desired)
        {
            debugPrint("Jug 1 contains " + b.jugs[1]);
            return true;
        }
        else if (b.jugs[2] == desired)
        {
            debugPrint("Jug 2 contains " + b.jugs[2]);
            return true;
        }

        // TODO: You add some code here.

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (i != j && tryPouring(b.pour(i, j)))
                {
                    System.out.println("Pouring from jug " + i + " to jug "
                                       + j);
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * @param args Three jug capacities, plus the contents of jugs 0 and 1,
     *             plus the desired amount.
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length != 6)
        {
            System.err.println("Wrong number of arguments.");
            System.exit(1);
        }

        JugSolver puzzle = new JugSolver(Integer.parseInt(args[0]),
            Integer.parseInt(args[1]), Integer.parseInt(args[2]),
            Integer.parseInt(args[5]));
        JugContents init = puzzle.new JugContents(Integer.parseInt(args[3]),
            Integer.parseInt(args[4]), 0);
        System.out.println(puzzle.tryPouring(init));
    }
    
    private static void debugPrint(String s)
    {
        if (DEBUGGING)
        {
            System.out.println(s);
        }
    }
        
    static int min(int x, int y)
    {
        return x < y ? x: y;
    }
    
    class JugContents
    {
        public int jugs[];  // Contents of the three jugs.
        
        public JugContents(int amt0, int amt1, int amt2)
        {
            jugs = new int[3];
            jugs[0] = amt0;
            jugs[1] = amt1;
            jugs[2] = amt2;
        }
        
        public JugContents(JugContents b)
        {
            jugs = new int[3];
            jugs[0] = b.jugs[0];
            jugs[1] = b.jugs[1];
            jugs[2] = b.jugs[2];
        }
        
        /**
         * Return the result of pouring as much as possible from jug from to jug to.
         */
        public JugContents pour(int from, int to)
        {
            JugContents afterPour = new JugContents(this);
            int amtToCanGet = capacity[to] - jugs[to];
            int amtFromCanSupply = jugs[from];
            int amtPoured = min(amtToCanGet, amtFromCanSupply);
            debugPrint("Pouring " + amtPoured + " from jug " + from
                       + " to jug " + to);
            afterPour.jugs[from] -= amtPoured;
            afterPour.jugs[to] += amtPoured;
            return afterPour;
        }
        
        public String toString()
        {
            return "Configuration = (" + jugs[0] + "," + jugs[1] + "," + jugs[2]
                   + ")";
        }
        
        // TODO: You add more code to this class.
    }
}
