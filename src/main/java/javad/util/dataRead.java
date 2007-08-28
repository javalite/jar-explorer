
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

import java.io.*;

/*
 * dataRead
 *

   Support read of u1, u2 and u4 data items.

 */
public class dataRead {
  private static int bytesRead = 0;

  public final int getBytesRead() { return bytesRead; }

  //
  // readU1: read an 8-bit byte
  // 
  // Is there a problem with sign extend here?  Java's lack of
  // unsigned types is a pain.  What would be nice is an unsigned
  // byte.
  //
  public final int readU1( DataInputStream dStream ) {
    int byteVal = -1;

    try {
      byteVal = dStream.readUnsignedByte();
      bytesRead++;
    } catch (Exception e) {
      if (e instanceof IOException) {
	if (! (e instanceof EOFException)) {
	  errorMessage.errorPrint( "(readU1): " + e.getMessage() );
	}
	else {
	  errorMessage.errorPrint( "(readU1): unexpected end of file" + 
				   e.getMessage() );
	}
      }
      else {
	errorMessage.errorPrint("(readU1): " + e.toString() + 
				"unexpected exception" );
      }
    } // catch

    return byteVal;
  } // readU1


  //
  // readU2: read a 16-bit unsigned short
  //
  public final int readU2( DataInputStream dStream ) {
    int shortVal = -1;

    try {
      shortVal = dStream.readUnsignedShort();
      bytesRead += 2;
    } catch (Exception e) {
      if (e instanceof IOException) {
	if (! (e instanceof EOFException)) {
	  errorMessage.errorPrint( "(readU2): " + e.getMessage() );
	}
	else {
	  errorMessage.errorPrint( "(readU2): unexpected end of file" + 
				   e.getMessage() );
	}
      }
      else {
	errorMessage.errorPrint("(readU2): " + e.toString() + 
				"unexpected exception" );
      }
    } // catch    
    return shortVal;
  } // readU2


  //
  // readU4: read a 32-bit int
  //
  public final int readU4( DataInputStream dStream ) {
    int intVal = -1;

    try {
      intVal = dStream.readInt();
      bytesRead += 4;
    } catch (Exception e) {
      if (e instanceof IOException) {
	if (! (e instanceof EOFException)) {
	  errorMessage.errorPrint( "(readU4): " + e.getMessage() );
	}
	else {
	  errorMessage.errorPrint( "(readU4): unexpected end of file" + 
				   e.getMessage() );
	}
      }
      else {
	errorMessage.errorPrint("(readU4): " + e.toString() + 
				"unexpected exception" );
      }
    } // catch    
    return intVal;
  } // readU4

} // dataRead

