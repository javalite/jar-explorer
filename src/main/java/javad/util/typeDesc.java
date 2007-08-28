
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

/**
  Support methods for decoding and printing Java type
  descriptors.

 */
public final class typeDesc {

  public static String charToType( char ch ) {
    String typeName;

    typeName = "badType";

    switch (ch) {
    case 'B':
      typeName = "byte";
      break;
    case 'C':
      typeName = "char";
      break;
    case 'D':
      typeName = "double";
      break;
    case 'F':
      typeName = "float";
      break;
    case 'I':
      typeName = "int";
      break;
    case 'J':
      typeName = "long";
      break;
    case 'S':
      typeName = "short";
      break;
    case 'Z':
      typeName = "boolean";
      break;
    } // switch

    return typeName;
  } // charToType


  public static boolean isTypeChar( char tChar ) {
    return (tChar == 'B' ||
	    tChar == 'C' ||
	    tChar == 'D' ||
	    tChar == 'F' ||
	    tChar == 'I' ||
	    tChar == 'J' ||
	    tChar == 'S' ||
 	    tChar == 'Z');
  } // isTypeChar


  /**
     Parse a field descriptor and return a String describing the type.
     For example, the descriptor [[I will result in the String
     "int[][]".  The field descriptor defines the type of the field.
     This method is also used to parse types for the declarations
     of method local variables.

  */
  public static String decodeFieldDesc( String descStr ) {
    String typeStr = null;

    if (descStr != null) {
      int dimCnt, charIx;
      char typeChar;
      
      // count array dimensions
      for (dimCnt = 0; descStr.charAt( dimCnt ) == '['; dimCnt++)
	/* nada */;

      charIx = dimCnt;
      typeChar = descStr.charAt(charIx);
      if (typeChar == 'L') {// the object is a class
	String objName;

	charIx++;  // charIx is the start of the object name
	objName = descStr.substring( charIx );
	// convert from foo/bar/zorch form to foo.bar.zorch form
	typeStr = objNameFormat.toDotSeparator( objName );
      }
      else if (typeDesc.isTypeChar( typeChar )) {
	typeStr = typeDesc.charToType( typeChar );
      }
      // if it's an array, print the dimensions
      for (int i = 0; i < dimCnt; i++) {
	typeStr = typeStr + "[]";
      }
    }

    return typeStr;
  } // decodeFieldDesc


} // typeDesc
