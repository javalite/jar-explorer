
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


/*
 * constBase
 *

   Base type for constant pool element classes.

 */
public abstract class constBase extends dataRead implements constPoolTags {
  int tag;

  abstract void read( DataInputStream dStream );


  final static String Tag_to_String( int tag ) {
    String str;

    switch ( tag ) {
    case CONSTANT_Utf8:
      str = "Utf8";
      break;
    case CONSTANT_Integer:
      str = "int";
      break;
    case CONSTANT_Float:
      str = "float";
      break;
    case CONSTANT_Long:
      str = "long";
      break;
    case CONSTANT_Double:
      str = "double";
      break;
    case CONSTANT_Class:
      str = "class";
      break;
    case CONSTANT_String:
      str = "String";
      break;
    case CONSTANT_Fieldref:
      str = "field ref.";
      break;
    case CONSTANT_Methodref:
      str = "method ref.";
      break;
    case CONSTANT_InterfaceMethodref:
      str = "interface method ref.";
      break;
    case CONSTANT_NameAndType:
      str = "name and type";
      break;
    default:
      str = "unknow tag";
      break;
    } // switch

    return str;
  } // Tag_to_String


    public String getName()
    {        
        return Tag_to_String( tag );
    }

  public void pr() {
    String tag_name = Tag_to_String( tag );
    System.out.print(tag_name);
  }

  //
  // set_ref
  //
  // Do nothing.  This method is included in the base class so that
  // the same operation (set_ref) can be done on all classes without
  // checking whether it is appropriate.
  public void set_ref(constBase objAry[] ) {
    // Nada
  }

  public void prString() {
    System.out.print("constBase");
  } // prString


  public String getString() {
    return "constBase";
  }

} // constBase
