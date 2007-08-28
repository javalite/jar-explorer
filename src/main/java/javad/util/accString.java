
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

/*
 * accString
 *

   The class constructor is passed an access value
   and the class initializes to the associated
   string.

 */
public final class accString implements access_and_modifier_flags {

  private static String getName( int val ) {
    String str = null;

    switch ( val ) {
    case ACC_PUBLIC:
      str = "public";
      break;
    case ACC_PRIVATE:
      str = "private";
      break;
    case ACC_PROTECTED:
      str = "protected";
      break;
    case ACC_STATIC:
      str = "static";
      break;
    case ACC_FINAL:
      str = "final";
      break;
    case ACC_SYNC:
      str = "synchronized";
      break;
    case ACC_VOLATILE:
      str = "volatile";
      break;
    case ACC_TRANSIENT:
      str = "transient";
      break;
    case ACC_NATIVE:
      str = "native";
      break;
    case ACC_INTERFACE:
      str = "interface ";
      break;
    case ACC_ABSTRACT:
      str = "abstract";
      break;
    case ACC_STRICT:
      str = "strict";
      break;
    } // switch
    return str;
  } // getName

  
  //
  // toString
  // 
  // This function is passed an access or modifier bitmap.  It
  // returns a string with the equivalent names.
  //
  public static String toString( int mod, boolean isMethod ) {
    String modStr = null;

    if (mod > 0) {
      int mask, cur_mod;
      boolean firstName = true;
      
      for (mask = 1; mask < 0x0010000; mask = mask << 1) {
	cur_mod = mod & mask;
	if (cur_mod != 0) {

	  // ACC_SYNCHRONIZED and ACC_SUPER have the same value.
	  // If a method is being processed then the value
	  // indicates a "synchronized" modifier.
	  if (cur_mod == ACC_SYNC && (!isMethod))
	    continue;

	  if (! firstName) {
	    modStr = modStr + " " + getName( cur_mod );
	  }
	  else {
	    modStr = getName( cur_mod );
	    firstName = false;
	  }
	}
      } // for
    } // if

    return modStr;
  } // toString

} // accString
