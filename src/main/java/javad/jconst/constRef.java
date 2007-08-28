
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.jconst;

import java.io.*;

/** 

   The constRef object represents CONSTANT_FieldRef,
   CONSTANT_MethodRef and CONSTANT_InterfaceMethodRef.
   The format is
<pre>
     CONSTANT_<>ref_info {
       u1 tag;
       u2 index;
       u2 name_and_type_index
     }
</pre>
<p>
     index must be a an index in the constant table for a
     constClass_or_String object.
<p>
     The name_and_type_index must be an index in the constant
     table for a constName_and_type_info object.
 <p>
     Note that this class is not derived from the constClass_or_String
     since the reference pointers do not point to constUtf8 classes.


 */
public class constRef extends constBase {
  int index;
  int name_and_type_index;
  constClass_or_String class_ref;
  constName_and_Type_info name_ref;

  public void read( DataInputStream dStream ) {
    index = readU2( dStream );
    name_and_type_index = readU2( dStream );
  }

  public void set_ref(constBase objAry[] ) {
    constBase tmp = objAry[ index ];

    if (tmp instanceof constClass_or_String) {
      class_ref = (constClass_or_String)tmp;
    }
    else {
      System.out.println("object at const. pool index " + index + 
			 " is not a constClass_or_String");
    }

    tmp = objAry[ name_and_type_index ];
    if (tmp instanceof constName_and_Type_info) {
      name_ref = (constName_and_Type_info)tmp;
    }
    else {
      System.out.println("object at const. pool index " + index + 
			 " is not a constName_and_Type_info");
    }
  } // set_ref    


  public String getString() {
    StringBuffer str = new StringBuffer();

    str.append( super.getString() );
    str.append(": ");
    if (class_ref != null) {
      str.append( class_ref.getString() );
    }
    else
      str.append("null ref");

    str.append(", ");

    if (name_ref != null) {
      str.append( name_ref.getString() );
    }
    else
      str.append("null ref");
    return str.toString();
  } // getString


  public void pr() {
    System.out.print( getString() );
  } // pr

} // constRef
