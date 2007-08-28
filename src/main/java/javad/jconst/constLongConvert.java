
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

// base class for sub classes that need to
// convert a high and low int to a long
abstract class constLongConvert extends constBase {

  private long toLong( int high, int low ) {
    long l;

    l = high;
    l = l << 32;
    l = l | low;
    return l;
  } // toLong

  protected long readLong( DataInputStream dStream ) {
    int high, low;
    long l;

    high = readU4(dStream);
    low = readU4(dStream);
    l = toLong( high, low );
    return l;
  }

} // constLongConvert
