/**
 * 
 */
package javad;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import javad.jconst.constPool;


/**
 * @author Simon Sobisch
 *
 */
public class javadCommandLineTest {
	
	private static String[] args = {
			getPathOfUrl(Igor.class.getResource("Igor.class")),
			getPathOfUrl(constPool.class.getResource("constPool.class")),
			getPathOfUrl(DataStructureForTest.class.getResource("DataStructureForTest.class")),
			getPathOfUrl(DataStructureForTest.class.getResource("DataStructureForTest$EvenIterator.class")),
			getPathOfUrl(DataStructureForTest.class.getResource("DataStructureForTest$additionalInnerClass4test.class"))
			};
	
	/**
	 * integration test for old "javad" (ensuring we don't get any Exceptions)
	 */
	@Test
	public void oldJavadTest() {		
		for (String givenClassName : args) {
			new jvmDump (givenClassName);
		}
	}

	/**
	 * integration test for new "javad" (ensuring we don't get any Exceptions)
	 * TODO: verify the output (one time manually and then by comparing a file / StringBuffer
	 * here)
	 */
	@Test
	public void newJavadTest() {
		for (String givenClassName : args) {
			Igor.printNewDump (givenClassName);
		}
	}
    

	/* get a path of a given local URL */
	private static String getPathOfUrl(URL resource) {
		String protocol = resource.getProtocol();
		if (protocol.equals("jar")){
			try {
				resource = new URL(resource.getPath());
			} catch (MalformedURLException e) {
				return null;
			}
			protocol = resource.getProtocol();
		}
		if (protocol.equals("file")){
			// if this is called from jar we may have: jar:file:d:\dev\some.jar!/package/Some.class
			String[] pathArray = resource.getPath().split("!");
			return pathArray[0];
		}
		return "";
	}
}

