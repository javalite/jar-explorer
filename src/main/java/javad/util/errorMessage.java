
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

/*
 * errorMessage
 *

   Support printing error messages in the form:

       <program-name>: <message>

   The class is initialized with the program name at application
   startup.  Since the program name is static, all instance of the
   class will share the program name.  Note that there are two class
   constructors, one which is passed the program name String and one
   with no arguments.  The argumentless constructor should be used
   after the class is initialized.

 */
public final class errorMessage {
  private static String programName = null;

  //
  // setProgName
  //
  public static void setProgName( String name ) {
    programName = name;
  } // setProgName 
  
  public static void errorPrint( String msg ) {
    String name;

    if (programName == null)
      name = "";
    else
      name = programName;

    System.out.println( name + ": " + msg );
  }

} // errorMessage
