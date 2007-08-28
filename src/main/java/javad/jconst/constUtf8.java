
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
 * constUtf8
 *

    CONSTANT_Utf8_info {
       u1 tag;
       u2 len;
       u1 bytes[len];
    }

   See section 4.4.7 of the Java Virtual Machine Specification for a
   description of multi-byte character representation.  Bit twidling
   in Java Dudes and Dudettes.

 */
public class constUtf8 extends constBase {
  private String str = null;
  
  public void read( DataInputStream dStream ) {
    StringBuffer strBuf;
    int len, charCnt;
    byte one_byte;
    char one_char;

    one_char = '\u0000';
    len = readU2( dStream );
    strBuf = new StringBuffer();
    charCnt = 0;
    while (charCnt < len) {
      one_byte = (byte)readU1( dStream );
      charCnt++;
      if ((one_byte >> 7) == 1) {
	short tmp;

	// its a multi-byte character
	tmp = (short)(one_byte & 0x3f);  // Bits 5..0 (six bits)
	// read the next byte
	one_byte = (byte)readU1( dStream );
	charCnt++;
	tmp = (short)(tmp | ((one_byte & 0x3f) << 6));
	if ((one_byte >> 6) == 0x2) {
	  // We have 12 bits so far, get bits 15..12
	  one_byte = (byte)readU1( dStream );
	  charCnt++;
	  one_byte = (byte)(one_byte & 0xf);
	  tmp = (short)(tmp | (one_byte << 12));
	}
	one_char = (char)tmp;
      }
      else {
	one_char = (char)one_byte;
      }
      strBuf.append(one_char);
    } // while
    str = strBuf.toString();
  } // read


  /**
     Return the Utf8 string in ASCII format.  Any characters
     which are outside the ASCII printable range are represented
     as either back-slash escapes or as \\uxxxx strings (e.g.,
     hex form of the unicode character.
   */
  public String toAsciiString() {
    String retStr = null;

    if (str != null) {
      StringBuffer strbuf = new StringBuffer();
      int len = str.length();
      char ch;

      for (int i = 0; i < len; i++) {
	// standard non-graphic printable 
	// ASCII range is ' ' (0x20) to '~' (7E)
	ch = str.charAt(i);
	if (ch >= ' ' && ch <= '~')
	  strbuf.append( ch );
	else {
	  String tmp;

	  tmp = null;
	  if (ch == '\b')
	    tmp = "\\b";
	  else if (ch == '\t')
	    tmp = "\\t";
	  else if (ch == '\n')
	    tmp = "\\n";
	  else if (ch == '\f')
	    tmp = "\\f";
	  else if (ch == '\r')
	    tmp = "\\r";
	  else 
	    tmp = "\\u" + Integer.toHexString( (int)ch );
	  strbuf.append( tmp );
	}
      } // for
      retStr = strbuf.toString();
    }
    return retStr;
  } // toAsciiString


  /**
    Print a Utf8 String in ASCII format.  Characters
    which are outside the ASCII range are printed
    as hex values in \\uxxxx format.

   */
  public void pr() {
    System.out.print( toAsciiString() );
  } // pr


  public void pr_data() {
    int len = str.length();

    System.out.println("end offset = " + getBytesRead() );
    System.out.println( str );
    for (int i = 0; i < str.length(); i++) {
      System.out.print( Integer.toHexString( (int)str.charAt(i) ) + " " );
    }
    System.out.println();
  } // pr_data


  /**
    Return the raw Utf8 string, without any translation.
    */
  public String getString() {
    return str;
  }


  /**
    Return a printable version of the Utf8 string.
    */
  public String getPrintableString() {
    return toAsciiString();
  }

} // constUtf8
