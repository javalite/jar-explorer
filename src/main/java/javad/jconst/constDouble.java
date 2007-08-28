
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


public class constDouble extends constLongConvert  {
  double d;
  
  private void toDouble( long l ) {
    d = Double.longBitsToDouble( l );
  }

  public void read( DataInputStream dStream ) {
    long longVal;

    longVal = readLong( dStream );
    toDouble( longVal );
  }

  public String getString() {
    return Double.toString( d );
  }

  public void pr() {
    System.out.print( d );
  } // pr
} // constDouble
