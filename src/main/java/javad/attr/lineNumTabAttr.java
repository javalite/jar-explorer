
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


/**

   This class represents the line number table attribute.

<p>
   The line number table attribute may be assocated with a code
   attribute (see codeAttr object).

<p>
   The JVM Spec (4.7.6) states:

<blockquote>
     It [the line number table attribute] may be used by debuggers to
     determine which part of the Java Virtual Machine code array
     corresponds to a given line number in the original Java source
     file. If LineNumberTable attributes are present in the attributes
     table of a given Code attribute, then they may appear in any
     order. Furthermore, multiple LineNumberTable attributes may
     together represent a given line of a Java source file; that is,
     LineNumberTable attributes need not be one-to-one with source
     line
</blockquote>

<p>
   The LineNumberTable attribute has the format

<pre>
    LineNumberTable_attribute {
        u2 attribute_name_index;
        u4 attribute_length;
        u2 line_number_table_length;
        lineEntry line_number_table[line_number_table_length];
    }
</pre>

<p>
   The attribute_name_index and attribute length are read by the
   attrFactory.allocAttr method.  These values are passed into the class
   constructor.

<p>
   The line number entries have the format:

<pre>
    lineEntry {
      u2 start_pc;
      u2 line_number;
    }
</pre>

   @author Ian Kaplan
 */
public class lineNumTabAttr extends attrInfo {
  
  //
  // lineEntry
  // 
  // This object represents a line table entry.
  //
  class lineEntry {
    private int start_pc;
    private int line_number;

    lineEntry( DataInputStream dStream ) {
      start_pc    = readU2( dStream );
      line_number = readU2( dStream );
    }

    int getStartPC() { return start_pc; };
    int getLineNum() { return line_number; }
  } // lineEntry

  lineEntry lineNumTab[] = null;

  public lineNumTabAttr( String name, int length, 
	      DataInputStream dStream ) {
    super( name, length );

    int numEntries = readU2( dStream );
    if (numEntries > 0) {
      lineNumTab = new lineEntry[ numEntries ];
      for (int i = 0; i < numEntries; i++) {
	lineNumTab[i] = new lineEntry( dStream );
      }
    }
  } // lineNumTabAttr

} // lineNumTabAttr
