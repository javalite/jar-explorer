
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

/*
 * constLong
 *
 */
public class constLong extends constLongConvert {
  long longVal;

  public void read( DataInputStream dStream ) {
    longVal = readLong( dStream );
  }

  public String getString() {
    return Long.toString( longVal );
  }

  public void pr() {
    System.out.print( longVal );
  } // pr

} // constLong
