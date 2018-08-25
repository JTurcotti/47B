import java.io.*;
import java.util.*;

public class TTThashTest
{
    public static void main(String[] args)
    {
        Hashtable table = new Hashtable();

        long startTime = System.currentTimeMillis();
	for (int functionNum = 0; functionNum <= 1; functionNum++) {
	    for (int k = 0; k < 19683; k++)
		{
		    TTTboard b = new TTTboard(k, functionNum);
		    table.put(b, new Integer(k));
		}
	    long finishTime = System.currentTimeMillis();
	    
	    System.out.println("Function Num: " + functionNum + "; Time to insert all Tic-tac-toe boards = "
			       + (finishTime - startTime));
	}
    }
}

