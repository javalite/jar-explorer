
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

public final class objNameFormat {

  //
  // toDotSeparator
  //
  // This function is passed a String for an object name in internal
  // form, with slash separators and returns a String for the object
  // name with dot separators.
  //
  // Some objects names may be terminated by a semicolon.  I have not
  // seen this explicitly discussed in the JVM Specification, but an
  // example is shown in section 4.3.2.  In any case, the semicolon
  // is skipped in the code below.
  //
  public static String toDotSeparator( String slashName ) {
    String newName = null;

    if (slashName != null) {
      int len;
      char ch;

      StringBuffer strBuf = new StringBuffer();

      len = slashName.length();
      for (int i = 0; i < len; i++) {
	ch = slashName.charAt( i );
	if (ch == '/') {
	  strBuf.append( '.' );
	}
	else if (ch != ';') {
	  strBuf.append( ch );
	}
      } // for
      newName = strBuf.toString();
    } // if
    return newName;
  } // toDotSeparator


  /**
    Return the last name (the leaf) in a "dot" name
    sequence.  For example, in the name 
    java.lang.Character, Character is the leaf name.
   */
  String leafName( String name ) {
    String leaf = null;

    if (name != null) {
      int ix;
    
      ix = name.lastIndexOf( '.' );
      if (ix >= 0) {
	leaf = name.substring( ix );
      }
    }
    return leaf;
  }  // leafName

} // objNameFormat
