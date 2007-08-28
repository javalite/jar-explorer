
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

<p>
   This object represents the Java class file inner class
   information.

<p>
   Inner classes were added to Java 1.1 and are described by
   Sun's Inner Class Specification, 2/10/97.

<p>
   An inner class is a class that is nested inside another class.  The
   inner class exists in the local scope of its enclosing class.  So
   an inner class may access any class variables or member functions
   of the parent class.  It may also reference any visible methods
   from the parent classes super class.

<p>
   Although the inner class shares scoping with its parent class, it
   is compiled into a separate class file.  This class has a name that
   is created from the inner class and its parent class.  To quote the
   Inner class specification (Class Name Transformations):

<blockquote>
     Names of nested classes are transformed as necessary by the
     compiler to avoid conflicts with identical names in other
     scopes.  Names are encoded to the virtual machine by taking their
     source form, qualified with dots, and changing each dot after a
     class name into a dollar sign.
</blockquote>

<p>
   The JVM has no special support for inner classes.  They are
   supported in the compiler by allowing inner class methods to
   reference class variables and methods that might not normally be in
   scope for an external class.  To quote Sun's inner class
   specification "This complicates the compiler's job."

<p>
   The format for the inner classes attribute is:

<pre>
     innerClasses_attribute {
       u2 attribute_name_index;
       u4 attribute_name_length;
       u2 number_of_classes
       innerClassInfo classes[ number_of_classes];
     }
</pre>

<p>
   The attribute_name_index and attribute length are read by the
   attrFactory.allocAttr method.  These values are passed into the class
   constructor.

<p>
   Here innerClassInfo is

<pre>
   innerClassInfo {
     u2 inner_class_info_index;
     u2 outer_class_info_index;
     u2 inner_name_index;
     u2 inner_class_access_flags;
   }
</pre>

<p>
   Sun's JVM Specification and inner class specification is difficult
   to understand when it comes to inner classes.

<p>
   The inner_class_info_index is either zero or an index into the 
   constant table for a constClass_or_String representing the 
   inner class.

<p>
   The outer_class_info_index is zero if the inner_class is not a
   member.  Otherwise the value of outer_class_info_index is an index
   into the constant table for a constClass_or_String representing the
   class or interface of which the inner_class is a member.

<p>
   If the inner_class is anonymous, the inner_name_index will be
   zero.  Otherwise this is an index into the constant table for
   a constUtf8 object which contains the class name.

<p>
   The inner_class_access_flags are the access mask for the inner
   class or interface.

 */
public class innerClassAttr extends attrInfo {


  class innerClassInfo {
    constClass_or_String innerClass = null;
    constClass_or_String outerClass = null;
    constUtf8 className = null;
    int accessFlags;


    innerClassInfo( DataInputStream dStream, constPool constPoolSec ) {

      int inner_class_info_index = readU2( dStream );
      int outer_class_info_index = readU2( dStream );
      int inner_name_index = readU2( dStream );
      accessFlags = readU2( dStream );
      constBase obj;

      if (inner_class_info_index > 0) {
	obj = constPoolSec.constPoolElem( inner_class_info_index );
	if (obj != null && obj instanceof constClass_or_String) {
	  innerClass = (constClass_or_String)obj;
	}
      }

      if (outer_class_info_index > 0) {
	obj = constPoolSec.constPoolElem( outer_class_info_index );
	if (obj != null && obj instanceof constClass_or_String) {
	  outerClass = (constClass_or_String)obj;
	}
      }

      if (inner_name_index > 0) {
	obj = constPoolSec.constPoolElem( inner_name_index );
	if (obj != null && obj instanceof constUtf8) {
	  className = (constUtf8)obj;
	}
      }
      
    } // innerClassInfo constructor


    void pr() {
      System.out.print("innerClass: ");
      innerClass.pr();
      System.out.print("outerClass: ");
      outerClass.pr();
    } // pr

  } // class innderClassInfo


  innerClassInfo classInfoTab[] = null;


  public innerClassAttr( String name, int length, DataInputStream dStream, constPool constPoolSec )
  {
      super(name, length);

    int numInnerClasses = readU2( dStream );
    if (numInnerClasses > 0) {
      
      classInfoTab = new innerClassInfo[ numInnerClasses ];

      for (int i = 0; i < numInnerClasses; i++) {
	classInfoTab[i] = new innerClassInfo( dStream, constPoolSec );
      }
    }
  } // innerClassAttr constructor

} // innerClassAttr
