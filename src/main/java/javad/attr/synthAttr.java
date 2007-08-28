
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.attr;

/**

   Represent a JVM class file synthetic attribute.
<p>
   A class member that does not appear in the source code must be
   marked using a Synthetic attribute.
<p>
   The length of the synthetic attribute is zero, so this class does
   nothing, since the attrFactory allocAttr method reads the
   attribute_name_index and the attribute_length (which, again,
   is zero).

   @author Ian Kaplan
 */
public class synthAttr extends attrInfo {

  public synthAttr(String name, int length) {
    super( name, length );
    // that's all folks
  }  // synthAttr constructor

} // synthAttr
