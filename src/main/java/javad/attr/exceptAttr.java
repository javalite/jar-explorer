
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
import javad.util.*;
import javad.jconst.*;


/**

   This class represents the exceptions attribute.  The structure of
   this attribute is:

<pre>
      Exceptions_attribute {
        u2 attribute_name_index;
        u4 attribute_length;
        u2 number_of_exceptions;
        u2 exception_index_table[ number_of_exceptions ];
      }
</pre>

<p>
   The attribute_name_index and attribute length are read by the
   attrFactory.allocAttr method.  These values are passed into the class
   constructor.

<p>
   Each non-zero value in the exception_index_table is an index into
   the constant pool of a constClass_or_String object represent
   a class type that this method throws.

<p>
   JVM Spec. 4.7.5

<blockquote>
     The Exceptions attribute indicates which checked exceptions a
     method may throw.  There must be exactly one Exceptions attribute
     in each method_info structure.
</blockquote>

   @author Ian Kaplan
 */
public class exceptAttr extends attrInfo {
  constClass_or_String exceptTable[] = null;

  public exceptAttr( String name, int length, 
	      DataInputStream dStream, constPool constPoolSec ) {
    super( name, length );

    int numExcept = readU2( dStream );
    if (numExcept > 0) {
      constBase obj;
      int ix;

      exceptTable = new constClass_or_String[ numExcept ];
      for (int i = 0; i < numExcept; i++) {
	// read the index in the constant table for the exception class
	ix = readU2( dStream );
	if (ix > 0) {
	  obj = constPoolSec.constPoolElem( ix );
	  if (obj != null && obj instanceof constClass_or_String) {
	    exceptTable[i] = (constClass_or_String)obj;
	  }
	  else {
	    errorMessage.errorPrint("exceptAttr: constClass_or_String expected " +
				    "at exception table index " + i );
	    errorMessage.errorPrint("exceptAttr: obj = " + 
				    obj.getClass().getName());
	    break;  // we're in trouble, so get out of the loop
	  }
	}
      } // for
    }
  } // exceptAttr

  public constClass_or_String[] getExceptTab() { return exceptTable; }

} // exceptAttr
