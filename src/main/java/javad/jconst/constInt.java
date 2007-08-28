
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
 * constInt
 *
 */
public class constInt extends constBase {
  int val;

  public void read( DataInputStream dStream ) {
    val = readU4(dStream);
  }

  public String getString() {
    return Integer.toString( val );
  }
  
  public void pr() {
    System.out.print( val );
  } // pr

} // constInt;

