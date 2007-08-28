
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
 * access_and_modifier_flags
 *

   Note that the various access and modifiers are unique
   except for ACC_SUPER, which seems to be "deprecated"
   and is used as the ACC_SYNCHRONIZED (synchronized) 
   modifier for methods.

 */
public interface access_and_modifier_flags {
  short ACC_PUBLIC    = 0x0001;
  short ACC_PRIVATE   = 0x0002;
  short ACC_PROTECTED = 0x0004;
  short ACC_STATIC    = 0x0008;
  short ACC_FINAL     = 0x0010;
  short ACC_SYNC      = 0x0020;  // also ACC_SUPER, which is "deprecated"
  short ACC_VOLATILE  = 0x0040;
  short ACC_TRANSIENT = 0x0080;
  short ACC_NATIVE    = 0x0100;
  short ACC_INTERFACE = 0x0200;
  short ACC_ABSTRACT  = 0x0400;
  short ACC_STRICT    = 0x0800;
} // interface access_and_modifier_flags

