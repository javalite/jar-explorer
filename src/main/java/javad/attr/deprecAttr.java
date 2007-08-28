
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  "The Java Virtual Machine Specification" is copyrighted by Sun
  Microsystems.

  See copyright file for usage and licensing

*/

package javad.attr;

/**

   <p>
   This class represents the depreciated attribute.  This attribute
   consists of only the attribute_name_index and length.  Like the
   Synthetic attribute this class does nothing, since the allocAttr
   function in the attrFactory class reads the attribute_name_index
   and length.

   <p>
   The depreciated attribute may be added to the attribute table
   of a ClassFile, field_info or method_info class.

   <p>
   Sun's JVM Spec. states:

<blockquote>
     A class, interface, method, or field may be marked using a
     Deprecated attribute to indicate that the class, interface,
     method, or field has been superseded.  A runtime interpreter or
     tool that reads the class file format, such as a compiler, can
     use this marking to advise the user that a superseded class,
     interface, method, or field is being referred to.  The presence
     of a Deprecated attribute does not alter the semantics of a class
     or interface.
</blockquote>

   @author Ian Kaplan

 */
public class deprecAttr extends attrInfo {

  public deprecAttr(String name, int length) {
    super( name, length );
    // that's all folks
  }  // deprecAttr constructor

} // deprecAttr
