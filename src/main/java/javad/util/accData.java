
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.util;

/**
  Boolean tests for the access and modifier bit field
 */
public final class accData implements access_and_modifier_flags {
  public static boolean isPublic( int val ) {
    return (val & ACC_PUBLIC) != 0; 
  }
  public static boolean isPrivate( int val ) {
    return (val & ACC_PRIVATE) != 0; 
  }
  public static boolean isProtected( int val ) {
    return (val & ACC_PROTECTED) != 0; 
  }
  public static boolean isStatic( int val ) {
    return (val & ACC_STATIC) != 0; 
  }
  public static boolean isFinal( int val ) {
    return (val & ACC_FINAL) != 0; 
  }
  public static boolean isSync( int val ) {
    return (val & ACC_SYNC) != 0; 
  }
  public static boolean isSuper( int val ) {
    return (val & ACC_SYNC) != 0; 
  }
  public static boolean isVolatile( int val ) {
    return (val & ACC_VOLATILE) != 0; 
  }
  public static boolean isTransient( int val ) {
    return (val & ACC_TRANSIENT) != 0; 
  }
  public static boolean isNative( int val ) {
    return (val & ACC_NATIVE) != 0; 
  }
  public static boolean isInterface( int val ) {
    return (val & ACC_INTERFACE) != 0; 
  }
  public static boolean isAbstract( int val ) {
    return (val & ACC_ABSTRACT) != 0; 
  }
  public static boolean isStrict( int val ) {
    return (val & ACC_STRICT) != 0; 
  }
} // accData

