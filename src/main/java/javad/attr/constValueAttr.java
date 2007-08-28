
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

   Represent a ConstantValue attribute.

  <p>
   The JVM Specification (4.7.3) states:

<blockquote>
     The constValueAttr is a fixed length attribute used in the
     "attributes" table of the field info structure.  A constValueAttr
     represents the value of a constant field that must be (explicitly
     or implicitly) static; that is the ACC_STATIC bit in the flags
     item o fthe field_info structure must be set.  The field is not
     required to be final.  There can be no more than one
     constValueAttr attribute in the attributes table of a given field
     info structure.  The constant field represented by the field_info
     structure is assigned the value referenced by its constValueAttr
     attribute as part of its initialization.
</blockquote>

<p>
   Every JVM is required to recognize the constValueAttr.  There
   may be other locally defined attributes which are ignored in
   general but may be used by a specific combination of Java
   compiler and JVM (so much for portability).

<p>
   The constValueAttr is

<pre>
     ConstantValue_attribute {
       u2 attribute_name_index;
       u4 attribute_length;    
       u2 constantvalue_index;
     }
</pre>

<p>
   The attribute_name_index and attribute length are read by the
   attrFactory.allocAttr method.  These values are passed into the class
   constructor.

<p>
   The constantvalue_index is the index into the constant pool
   for a long, float, double, int (for int, short, char, byte
   and boolean) or String.

   Question:

      What is the ConstantValue attribute for mondoRef, since
      its type is not a base type, but it is static.

        class mondo {
           int p, d, q;
        }
        
        class foobar {
          static mondo mondoRef = null;
          ...
        }

   @author Ian Kaplan

 */
public class constValueAttr extends attrInfo {
  constBase constValue = null;

  public constValueAttr( String name, int length, 
			 DataInputStream dStream, constPool constPoolSec ) {
    // read the attribute type string and the length
    super( name, length );

    int constValueIx = readU2( dStream );
    if (constValueIx > 0) {
      constValue = constPoolSec.constPoolElem( constValueIx );
    }

  } // constValueAttr constructor


  public constBase getConstVal() { return constValue; }

  public void pr() {
    super.pr();
    if (constValue != null) {
      constValue.pr();
    }
  } // pr

} // constValueAttr
