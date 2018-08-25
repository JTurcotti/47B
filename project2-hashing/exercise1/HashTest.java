import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * HashTest: Tests the performance of our custom Hashtable class against a list
 * of words.
 */
public class HashTest
{
    /**
     * Opens and returns a BufferedReader on the specified file.
     * @param fileName Name of the file to open.
     * @return BufferedReader on the file specified by fileName.
     */
    static BufferedReader reader(String fileName) throws Exception
    {
        File wordFile;
        FileInputStream wordFileStream;
        InputStreamReader wordsIn; 
        
        wordFile = new File(fileName);
        wordFileStream = new FileInputStream(wordFile);
        wordsIn = new InputStreamReader(wordFileStream);
        return new BufferedReader(wordsIn);
    }

    /**
     * Tests the performance of our custom Hashtable class by hashing a list of
     * words, and then printing the table statistics.
     * @param args Command-line argument containing the name of the file to
     *             read and the table size.
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            System.err.println("usage: java HashTest <file>");
            System.exit(1);
        }

	for (int functionNum = 1; functionNum <=4; functionNum++) {
	    double[] times = double[11];
	    for (int tableSize = 1040; tableSize <= 1050; tableSize++) {
		
		BufferedReader wordReader;
		Hashtable table = new Hashtable(tableSize, functionNum);
		String word;
		
		// Read all the words into the hash table.
		int wordCount = 0;
		wordReader = reader(args[0]);
		do
		    {
			try
			    {
				word = wordReader.readLine();
			    }
			catch (Exception e)
			    {
				System.err.println(e.getMessage());
				break;
			    }
		    
			if (word == null)
			    {
				break;
			    }
			else
			    {
				wordCount++;
				table.put(word, new Integer(wordCount));
			    }
		    } while (true);
	    
		// Now look up all the words.
		wordReader = reader(args[0]);
		long startTime = System.currentTimeMillis();
		do
		    {
			try
			    {
				word = wordReader.readLine ();
			    }
			catch (Exception e)
			    {
				System.err.println (e.getMessage());
				break;
			    }
		    
			if (word == null)
			    {
				break;
			    }
			else
			    {
				boolean result = table.containsKey(word);
			    }
		    } while (true);
	    
		long finishTime = System.currentTimeMillis();
		System.out.println("Table Size: " + tableSize + "; Function Num: " + functionNum + "; Time to hash " + wordCount + " words is " 
				   + (finishTime - startTime) + " milliseconds.");
		table.printStatistics();
	    }
	    System.out.println("\n\n\n");
	}
    }
}
