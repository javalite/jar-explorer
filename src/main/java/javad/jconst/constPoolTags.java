
/*

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/

package javad.jconst;


/*
 * interface constPoolTags
 *

   The purpose of this interface is to define the tag
   values for the constant table tags.
 
 */
interface constPoolTags {
  int CONSTANT_Class = 7;
  int CONSTANT_Fieldref = 9;
  int CONSTANT_Methodref = 10;
  int CONSTANT_InterfaceMethodref = 11;
  int CONSTANT_String = 8;
  int CONSTANT_Integer = 3;
  int CONSTANT_Float = 4;
  int CONSTANT_Long = 5;
  int CONSTANT_Double = 6;
  int CONSTANT_NameAndType = 12;
  int CONSTANT_Utf8 = 1;
} // interface constPoolTags

