
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
 * constPool
 *

   Read the Java JVM constant pool from the class file

   The JVM Specification, Second Edition states:

     The constant pool count (constPoolCnt) is the number of constant
     pool table entries plus one.  A constant pool index is considered
     valid if it is greater than zero and less than the constant pool
     count, with the exception for constants of type long and double.

  There should be constPoolCnt-1 constant pool entries.  These
  will be inserted into the constant pool (constPool) at
  locations 1 ... constPoolCnt-1.
  
     The first entry of the constant_pool table, constant_pool[0],
     is reserved for internal use by the Java Virtual Machine
     implementation.  That entry is not present in the class
     file.  The first entry in the class file is constant_pool[1];

 */
public class constPool extends dataRead implements constPoolTags {
  private int constPoolCnt;
  private constBase constPool[] = null;

  public constPool( DataInputStream dStream ) {

    constPoolCnt = readU2( dStream );

    // The constant pool array is an array of references to
    // the base class object.  References 1..constPoolCnt-1
    // are valid and are filled in when the constant pool
    // is read.
    constPool = new constBase[ constPoolCnt ];

    readConstPool( dStream );
    resolveConstPool();
  } // constPool constructor


  //
  // constPoolElem
  //
  // Return the constant pool object at index ix.
  // Note that only constant pool indices greater than
  // zero are valid.
  //
  public constBase constPoolElem( int ix )
  {
    constBase elem = null;

    if (ix > 0 && ix < constPool.length) {
      elem = constPool[ix];
    }
    return elem;
  } // constPoolElem


  //
  // allocconstEntry
  //
  // Allocate the constant object associated with the tag.
  //
  private constBase allocConstEntry( int tag ) {
    constBase obj = null;

    switch ( tag ) {
    case CONSTANT_Utf8:
      obj = new constUtf8();
      break;
    case CONSTANT_Integer:
      obj = new constInt();
      break;
    case CONSTANT_Float:
      obj = new constFloat();
      break;
    case CONSTANT_Long:
      obj = new constLong();
      break;
    case CONSTANT_Double:
      obj = new constDouble();
      break;
    case CONSTANT_Class:
    case CONSTANT_String:
      obj = new constClass_or_String();
      break;
    case CONSTANT_Fieldref:
    case CONSTANT_Methodref:
    case CONSTANT_InterfaceMethodref:
      obj = new constRef();
      break; 
    case CONSTANT_NameAndType:
      obj = new constName_and_Type_info();
      break;
    default:
      System.out.println("allocConstEntry: bad tag value = " + tag);
      break;
    } // switch

    if (obj != null) {
      obj.tag = tag;
    }
    return obj;
  } // allocConstEntry


  //
  // resolveConstPool
  //
  //
  // A number of the constant pool classes contain indices that
  // refer to other elements in the constant pool.  Convert these
  // to references, using the set_ref() method.  This method is
  // implemented for all classes, but only does something when it
  // is appropriate.
  //
  //
  private void resolveConstPool() {
    for (int i = 1; i < constPoolCnt; i++) {
      if (constPool[i] != null)
	constPool[i].set_ref(constPool);
    }    
  } // resolveConstPool


  /**
     Read the JVM class file constant pool and put
     it in the internal constant pool.
<p>
     There is a special case in constant pool construction.
     CONSTANT_Long_info and CONSTANT_Double_info structures
     take up tow elements in the constant pool table 
     (constPool).  See JVM Spec. 4.4.5.  As noted in a footnote
     "In retrospect, making 8-byte constants take two constant
     pool entries was a poor choice."  The extra constant pool
     entry is unused (and is set to null here).
  */
  private void readConstPool(DataInputStream dStream) {
    int tag;
    constBase constObj;

    for (int i = 1; i < constPoolCnt; i++) {
      tag = readU1( dStream );
      if (tag > 0) {
	constObj = allocConstEntry( tag );
	constObj.read( dStream );
	constPool[i] = constObj;
	if (constObj instanceof constLong || 
	    constObj instanceof constDouble) {
	  i++;
	  constPool[i] = null;
	}
      }
      else {
	System.out.println("readConstPool: tag == 0 at constPool index " + i );
      }

    }
  } // readConstPool


  //
  // pr
  // 
  // Display constant pool count and the contents of the constant pool
  //
  public void pr() {
    System.out.println("constant pool count = " + constPoolCnt + 
		       " (size = " + (constPoolCnt-1) + ")");
    for (int i = 1; i < constPoolCnt; i++) {
      System.out.print( i + " ");
      if (constPool[i] != null) {
	constPool[i].pr();
	System.out.println();
      }
    }
  }

} // constPool
