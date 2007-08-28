
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
import javad.util.*;

/**
<pre>
    CONSTANT_Class_info {
       u1 tag;
       u2 name_index;
    }

    or

    CONSTANT_String_info {
       u1 tag
       u2 string_index
    }

 */
public class constClass_or_String extends constBase {
  int index = 0;
  constUtf8 Utf8 = null;

  void read( DataInputStream dStream ) {
    index = readU2( dStream );
  }

  // For both class and strings, the index referrs to a
  // constUtf8 object.
  public void set_ref(constBase objAry[] ) {
    constBase tmp = objAry[ index ];

    if (tmp instanceof constUtf8) {
      Utf8 = (constUtf8)tmp;
    }
    else {
      System.out.println("Object at index " + index + " is not a constUtf8");
    }
  } // set_ref

  public void pr() {
    super.pr();
    System.out.print(": ");
    if (Utf8 != null) {
      Utf8.pr();
    }
    else {
      System.out.println("null ref");
    }
  } // pr

  public void prString() {
    if (Utf8 != null) {
      String name;

      name = objNameFormat.toDotSeparator( Utf8.getString() );
      if (name != null) {
	System.out.print( name );
      }
      else {
	System.out.println("\nconstClass_or_String: prName - name is null");
      }
    }    
  }

    public String getName()
    {

            if (Utf8 != null) {
              String name;

              name = objNameFormat.toDotSeparator( Utf8.getString() );
              if (name != null) {
                return  name ;
              }
              else {
                    return "";
              }
            }
        else
            return "";
    }


  public String getPrintableString() {
    return Utf8.getPrintableString();
  }

  public String getString() {
    return Utf8.getString();
  } // getName

} // constClass_or_String

