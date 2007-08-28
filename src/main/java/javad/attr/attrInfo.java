
package javad.attr;

import javad.util.*;

/**
   <p>
   Base class for attributes (Section 4.7, JVM Specification).  All
   other attributes are derived from this class.  The attributes
   in (Java 1.2) are:

   <ul>
   <li>
   <p>
   constant attribute
   </li>
   <li>
   <p>
   code attribute
   </li>
   <li>
   <p>
   exceptions attribute
   </li>
   <li>
   <p>
   inner classes attribute   
   </li>
   <li>
   <p>
   synthetic attribute
   </li>
   <li>
   <p>
   source file attribute
   </li>
   <li>
   <p>
   line number table attribute
   </li>
   <li>
   <p>
   Local variable table attribute
   </li>
   <li>
   <p>
   deprecated attribute
   </li>
   </ul>

   <p>
    All attributes have a  CONSTANT_Utf8_info entry and a length.
    There may be attributes which are unknown.  The data in these
    attributes (length bytes following the length field) is 
    skipped.

    <p>
    The structure of the base class is:

<pre>
    attrInfo {
      u2 attr_name_index;
      u4 length;
    }

    @author Ian Kaplan
</pre>

 */
public class attrInfo extends dataRead {
  String attrName = null;
  int len = 0;

  attrInfo( String name, int length ) {
    attrName = name;
    len = length;
  } // attrInfo


  public String getName() { return attrName; }


  public void pr() {
    if (attrName != null) {
      System.out.print(attrName + ": len = " + len );
    }
  } // pr

} // attrInfo
