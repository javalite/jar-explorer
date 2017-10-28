/**
 * 
 */
package javad;

/**
 * Simple inner class without any actual use other than providing test data
 * @author docs.oracle at https://docs.oracle.com/javase/tutorial/java/javaOO/innerclasses.html
 *
 */
public class DataStructureForTest {
    
    // Create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
    
    public DataStructureForTest() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    
    public void printEven() {
        
        // Print out values of even indices of the array
        DataStructureIterator iterator = this.new EvenIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
    
    interface DataStructureIterator extends java.util.Iterator<Integer> { } 

    // Inner class implements the DataStructureIterator interface,
    // which extends the Iterator<Integer> interface
    
    private class EvenIterator implements DataStructureIterator {
        
        // Start stepping through the array from the beginning
        private int nextIndex = 0;
        
        public boolean hasNext() {
            
            // Check if the current element is the last in the array
            return (nextIndex <= SIZE - 1);
        }        
        
        public Integer next() {
            
            // Record a value of an even index of the array
            Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
            
            // Get the next even element
            nextIndex += 2;
            return retValue;
        }
    }
    
    public static void main(String s[]) {
        
        // Fill the array with integer values and print out only
        // values of even indices
        DataStructureForTest ds = new DataStructureForTest();
        ds.printEven();
    }
    
    /**
     * inner class with some parts otherwise missing in test coverage
     * @author Simon Sobisch
     *
     */
    class additionalInnerClass4test {
    	
    	/* some consts */
    	final static boolean C_BOOLEAN = true;
    	final static int C_INT = 42;
    	final static long C_LONG = 15L;
    	final static char C_CHAR = 'I';
    	final static float C_FLOAT = 0.1f;
    	final static double C_DOUBLE = 0.2d;
    	
    	final static String C_STRING = "AB" + "CD";
    	
    	final Byte C_BYTE = 0x00;
    	
    	final String[] CA_STRING = {"AB", "CD"};
    	final Byte [] CA_BYTE = {(byte) 0xAB, (byte) 0xCD};
    	
    	@Deprecated
    	public int getFinalSolution () throws Exception {
    		if (this.getClass().getName().equals("Earth")) {
    			throw new Exception(C_STRING);
    		};
    		return 42;
    	}
    }
}

