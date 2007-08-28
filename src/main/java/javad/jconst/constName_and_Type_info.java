
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

/*
 * constName_and_Type_info
 *

    CONSTANT_NameAndType {
       u1 tag;
       u2 index;
       u2 descriptor_index
    }

    Both index and descriptor index refer to a CONSTANT_Utf8 entry in
    the constant table

 */
public class constName_and_Type_info extends constClass_or_String {
  int descriptor_index;
  constUtf8 descriptor_Utf8;

  public void read( DataInputStream dStream ) {
    super.read(dStream);
    descriptor_index = readU2(dStream);
  }

  public void set_ref(constBase objAry[] ) {
    super.set_ref( objAry );
    constBase tmp = objAry[ descriptor_index ];

    if (tmp instanceof constUtf8) {
      descriptor_Utf8 = (constUtf8)tmp;
    }
    else {
      System.out.println("Descriptor object at index is not a constUtf8");
    }
  } // set_ref


  public String getString() {
    StringBuffer str = new StringBuffer();
    
    str.append( super.getString() );
    str.append(", ");
    if (descriptor_Utf8 != null) {
      str.append( descriptor_Utf8.getString() );
    }
    else {
      str.append("null ref");
    }
    return str.toString();
  } // getString


  public void pr() {
    System.out.print( getString() );
  } // pr

} // constName_and_type_info

