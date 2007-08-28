package javad;

import javad.classfile.classFile;
import javad.util.errorMessage;

import java.io.*;

/**
   Dump a compiled Java JVM class file in human readable form.

<p>
   This object is invoked via the constructor, which is passed
   the file name path.

 */
class jvmDump {
  private String fileName;
  private FileInputStream fStream;
  private boolean fileIsOpen = false;


  //
  // jvmDump: class constructor
  //
  jvmDump( String name ) {
    DataInputStream dStream;

    fileName = name;

    dStream = openFile( name );
    if (dStream != null) {
      javad.classfile.classFile curClassFile;

      curClassFile = new classFile( dStream );
      System.out.println();
      curClassFile.pr();

      closeFile();
      fileIsOpen = false;
    }
  }  // jvmDump constructor


  private DataInputStream openFile( String name ) {
    DataInputStream dStream;

    // try the file open
    try {
      BufferedInputStream bufStream;

      fStream = new FileInputStream( name );
      bufStream = new BufferedInputStream( fStream );
      dStream = new DataInputStream( bufStream );
      fileIsOpen = true;
    } catch( Exception e ) {
      dStream = null;  // file open did not succeed

      // the constructor can throw either the FileNotFoundException
      // or the SecurityException
      if (e instanceof FileNotFoundException) {
	errorMessage.errorPrint("could not open file " + name );
      }
      else if (e instanceof SecurityException) {
	errorMessage.errorPrint("not allowed to open file " + name );
      }
      else {
	errorMessage.errorPrint(e.toString() + "unexpected exception" );
      }
      fileIsOpen = false;
    } // catch

    return dStream;
  } // openFile


  //
  // closeFile
  //
  private void closeFile() {
    // we're done with the file, so be a good citizen
    // and release the file descriptor.
    try {
      fStream.close();
    } catch (Exception e) {
      if (e instanceof IOException) {
	errorMessage.errorPrint( e.getMessage() );
      }
      else {
	errorMessage.errorPrint(e.toString() + "unexpected exception" );
      }
    } // catch
  } // closeFile


  /**

     Use a finalize method to free up the file descriptor.
     Finalize is always called, even if the object terminates
     with an exception.

   */
  protected void finalize() {
    if (fileIsOpen) {
      closeFile();
    }
  } // finalize

} // jvdDump
