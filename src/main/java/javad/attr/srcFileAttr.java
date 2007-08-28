
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.attr;

import java.io.*;

import javad.jconst.*;


/**

   This object represents the source file attribute.  The structure of
   this attribute is:

<pre>
      SourceFile_attribute {
        u2 attribute_name_index;
        u4 attribute_length;
	u2 sourcefile_index;
      }
</pre>

<p>
   The attribute_name_index and attribute length are read by the
   attrFactory.allocAttr method.  These values are passed into the class
   constructor.

<p>
   The sourcefile_index points to a constUtf8 object in the constant
   pool.  This object contains the file name for the object.  There
   can be no more than one SourceFile attribute in the attributes
   table of a given ClassFile structure.

   @author Ian Kaplan
 */
public class srcFileAttr extends attrInfo {
  private constUtf8 srcFile = null;
  
  public srcFileAttr( String name, int length, 
		      DataInputStream dStream, constPool constPoolSec ) {
    super( name, length );
    constBase obj;
    int srcFileIx;

    srcFileIx = readU2( dStream );
    if (srcFileIx > 0) {
      obj = constPoolSec.constPoolElem( srcFileIx );
      if (obj instanceof constUtf8) {
	srcFile = (constUtf8)obj;
      }
      else {
	System.out.println("srcFileAttr: object not Utf8");
      }
    }
  } // srcFileAttr constructor

  /**
     @return a String object for the source file name
   */
  public String getFileName() {
    String name = null;

    if (srcFile != null) {
      name = srcFile.getString();
    }
    
    return name;
  } // getFileName


  public void pr() {
    super.pr();
    
    if (srcFile != null)
      srcFile.pr();
  } // pr

} // srcFileAttr

