
/**

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

/*

  To generate Java class documentation use

    javadoc -private -author -d doc javad.java javad.attr/*.java javad.classfile/*.java javad.jconst/*.java javad.util/*.java

  if you have a UNIX style shell.  If you don't I recomment getting Bash from
  cygnus.com.  Bash is a UNIX style shell that runs on Windows NT.  Live free
  or die!

 */

package javad;

import java.io.*;
//
// local utility directory, not to be confused with
// java.lang.javad.util.*
//
import javad.util.*;
import javad.classfile.*;


/** 

The <b>javad</b> class contains the <i>main</i> for the <i>javad</i>
program.  The <i>javad</i> program reads a Java class file and
writes out a Java like source representation of the class that
generated the file. 

*/
public class main {

  static void usage() {
    errorMessage.errorPrint(" this program takes a set of one or more" +
			    " .class file names as its argument");
  }

  public static void main( String[] args ) {

    errorMessage.setProgName( "javad" );

    if (args.length == 0) {
      usage();
    }
    else {
      for (int i = 0; i < args.length; i++) {
	jvmDump dumpObj = new jvmDump( args[i] );
      }
    }
  }
} // main
