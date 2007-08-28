
/**

  The author of this software is Ian Kaplan
  Bear Products International
  www.bearcave.com
  iank@bearcave.com

  Copyright (c) Ian Kaplan, 1999, 2000

  See copyright file for usage and licensing

*/


package javad.classfile;

import java.util.Vector;
import java.util.ArrayList;
import java.io.*;
import javad.util.*;
import javad.jconst.*;
import javad.attr.*;



/**

   The classFileHeader contains the class file header information.
   This information is:

<pre>
      u4 magic          -- magic number, the famous 0xcafebabe
      u2 minor_version
      u2 major_version
</pre>

  @author Ian Kaplan

 */
class classFileHeader extends dataRead {
  private int magic;
  private short minor_version;
  private short major_version;

  classFileHeader( DataInputStream dStream ) {
    magic = readU4( dStream );
    minor_version = (short)readU2( dStream );
    major_version = (short)readU2( dStream );
  } // classFileHeader constructor

  void pr() {
    System.out.println("magic number: 0x" + Integer.toHexString( magic ));
    System.out.println("Java version " + major_version + "." + minor_version );
  }

} // classFileHeader




/** 
   Read the JVM class file information that defines the class header.
   This information is the access modifier information (access_flags),
   the class name (this_class), the super class (super_class) and the
   interface list (interface_count and interfaces[]).  Note that if
   the interface_flags field is ACC_INTERFACE its not a class but an
   interface.

  @author Ian Kaplan

 */
class classDeclSec extends dataRead implements access_and_modifier_flags {
  private int accessFlags = 0;
  private constBase thisClass = null;
  private constBase superClass = null;
  private constBase interfaces[] = null;

  classDeclSec( DataInputStream dStream, constPool constPoolSec ) {
    int thisClassIx;
    int superClassIx;
    int interfaceCnt;
    
    accessFlags = readU2( dStream );
    thisClassIx = readU2( dStream );
    superClassIx = readU2( dStream );

    thisClass = constPoolSec.constPoolElem( thisClassIx );
    superClass = constPoolSec.constPoolElem( superClassIx );

    interfaceCnt = readU2( dStream );

    if (interfaceCnt > 0) {
      int ix;

      interfaces = new constBase[ interfaceCnt ];
      for (int i = 0; i < interfaceCnt; i++) {
	ix = readU2( dStream );
	interfaces[ i ] = constPoolSec.constPoolElem( ix );
      }
    }
  } // classDeclSec constructor


  String getClassName() {
    String name = null;

    if (thisClass != null) {
      if (thisClass instanceof constClass_or_String) {
	name = objNameFormat.toDotSeparator( thisClass.getString() );
      }
    }

    return name;
  } // getClassPath

  /**
     Print the class modifiers
   */
  private void pr_modifiers( int mod ) {
    String modStr;

    modStr = accString.toString( mod, false /* not a method */  );
    if (modStr != null) {
      System.out.print( modStr );
    }
  } // pr_modifiers

    public String getModifiers()
    {
        String modStr;
        modStr = accString.toString( accessFlags, false);
        return modStr != null? modStr : "";
    }



    public String getSuperClassName()
    {
      String superClassName;
	  superClassName = superClass.getString();
	  return objNameFormat.toDotSeparator(superClassName);
    }

    public String[] getInterfacesNames()
    {
        if (interfaces != null)
        {

            String [] intNames = new String[interfaces.length];
            for (int i = 0; i < interfaces.length; i++)
            {
                if (interfaces[i] instanceof constClass_or_String) {
                    intNames[i] = interfaces[i].getName();
                  }
                
            }

            return intNames;
        }
        else
        {
            return new String[]{};
        }
    }
 
  void pr() {
    pr_modifiers( accessFlags );
    if ((accessFlags & ACC_INTERFACE) == 0) {
      if (accessFlags > 0) {
	System.out.print(" ");
      }
      System.out.print("class ");
    }


    System.out.print( getClassName() );
    
    // only print the super class if it is NOT java.lang.Object
    if (superClass != null) {
      if (superClass instanceof constClass_or_String) {
	if (superClass.getString().compareTo( "java/lang/Object" ) != 0) {
	  String superClassName;

	  superClassName = superClass.getString();
	  superClassName = objNameFormat.toDotSeparator(superClassName);
	  System.out.print(" extends " + superClassName );
	}
      }
    }

    if (interfaces != null) {
      boolean firstName = true;

      System.out.print(" implements ");

      for (int i = 0; i < interfaces.length; i++) {
	if (! firstName) {
	  System.out.print(", ");
	}
	else {
	  firstName = false;
	}
	if (interfaces[i] != null) {
	  if (interfaces[i] instanceof constClass_or_String) {
	    interfaces[i].prString();
	  }
	  else {
	    System.out.println(" classDeclSec: interfaces[" + i + "] is a " +
			       interfaces[i].getClass().getName() );
	  }
	}
	else {
	  System.out.print("<null>");
	}
      } // for
    }
    System.out.println(" {");
  } // pr

} // classDeclSec



/**

   As described in the JVM Specification, the field_info
   is:

<pre>
   field_info {
     u2 access_flags;
     u2 name_index;        (Utf8 in the constant pool)
     u2 descriptor_index;  (Utf8 in the constant pool)
     u2 attributes_cnt;
     attribute_info attributes[ attributes_cnt ];
   }
</pre>

<p>
   The only attribute defined for the attributes table of a
   field_info structure by the JVM Specification is the 
   ConstantValue attribute

  @author Ian Kaplan

 */
class fieldInfo extends dataRead {
  int access_flags = 0;
  constUtf8 name = null;
  constUtf8 descriptor = null;
  attrInfo attributes[] = null;

  fieldInfo( DataInputStream dStream, constPool constPoolSec ) {
    int name_index;
    int desc_index;
    int attr_cnt;
    constBase obj;

    access_flags = readU2( dStream );
    name_index   = readU2( dStream );
    desc_index   = readU2( dStream );
    attr_cnt     = readU2( dStream );
    
    obj = constPoolSec.constPoolElem( name_index );
    if (obj != null && obj instanceof constUtf8) {
      name = (constUtf8)obj;
    }

    obj = constPoolSec.constPoolElem( desc_index );
    if (obj != null && obj instanceof constUtf8) {
      descriptor = (constUtf8)obj;
    }

    if (attr_cnt > 0) {
      attributes = new attrInfo[ attr_cnt ];
      for (int i = 0; i < attr_cnt; i++) {
	attributes[i] = attrFactory.allocAttr( dStream, constPoolSec );
          if(attributes[i] != null)
          {
    if (attributes[i].getName().compareTo( "ConstantValue" ) != 0 &&
	    attributes[i].getName().compareTo( "Synthetic" ) != 0     &&
	    attributes[i].getName().compareTo( "Deprecated" ) != 0 ) {
	  errorMessage.errorPrint("fieldInfo: field info attribute expected");
	  errorMessage.errorPrint("fieldInfo: javad.attr name = " +
				  attributes[i].getName() );
	}
          }
      }
    }
  } // fieldInfo constructor



  /**
     Create a string for the field in something like Java
     source format.  Field strings have the format:
<pre>
         <access-modifiers> <type> <name>
</pre>

<p>
     The access modifiers are described by the access_flags.
     The type is described by the descriptor and the name
     is referenced by the "name" object.

    @Return return a the field in something like Java source format
   */
  public String fieldString() {
    String modStr;  // access/modifiers (e.g., private static)
    String typeStr; // type
    StringBuffer fieldStr = new StringBuffer();

    modStr = accString.toString( access_flags, false /* not a method */ );
    typeStr = typeDesc.decodeFieldDesc( descriptor.getString() );
    if (modStr != null) {
      fieldStr.append( modStr );
      fieldStr.append(" ");
    }
    fieldStr.append( typeStr );
    fieldStr.append(" ");
    fieldStr.append( name.getString() );

    /*
       Process the attribute table information
     */
    boolean isSynthetic = false;
    boolean isDeprecated = false;
    constValueAttr constAttr;
    constBase constVal;
    boolean firstConst = true;
    String initVal = null;
    String tmp = null;

    if (attributes != null) {
      for (int i = 0; i < attributes.length; i++) {
	if (attributes[i] instanceof constValueAttr) {
	  constAttr = (constValueAttr)attributes[i];
	  constVal = constAttr.getConstVal();
	  if (constVal instanceof constClass_or_String ) {
	    tmp = ((constClass_or_String)constVal).getPrintableString();
	  }
	  else {
	    tmp = constVal.getString();
	  }
	  if (firstConst) {
	    firstConst = false;
	    initVal = tmp;
	  }
	  else {
	    initVal = initVal + ", " + tmp;
	  }
	}
	else if (attributes[i] instanceof synthAttr) {
	  isSynthetic = true;
	}
	else if (attributes[i] instanceof deprecAttr) {
	  isDeprecated = true;
	}
      } // for
      
      if (accData.isStatic( access_flags) && initVal != null) {
	fieldStr.append(" = ");
	fieldStr.append( initVal );
      } // if static
      fieldStr.append(";");
      if (isSynthetic || isDeprecated )
	fieldStr.append(" //");
      if (isSynthetic) {
	fieldStr.append(" Synthetic");
      }
      if (isDeprecated) {
	fieldStr.append(", Deprecated");
      }
    } // if attributes != null
    else
      fieldStr.append(";");
    return fieldStr.toString();
  } // fieldString

    public String getField()
    {
        return fieldString();
    }
 
  /**
    Print the field
   */
  void pr() {
    System.out.println( fieldString() );
  } // pr

} // fieldInfo



/** 
   Read the class field section and build representative
   classes.  The class field section is composed of a
   field count and an array of field_info data structures.

<pre>
      u2 field_cnt;
      field_info fields[ field_cnt ];
</pre>

  @author Ian Kaplan

 */
class classFieldSec extends dataRead {
  private fieldInfo classFields[] = null;

  classFieldSec( DataInputStream dStream, constPool constPoolSec ) {
    int field_cnt;

    field_cnt = readU2( dStream );
    if (field_cnt > 0) {
      classFields = new fieldInfo[ field_cnt ];
    }

    // initialize the fieldInfo array
    for (int i = 0; i < field_cnt; i++) {
      classFields[i] = new fieldInfo( dStream, constPoolSec );
    } // for

  } // classFieldSec constructor


    public String[] getFields()
    {
             if (classFields != null)
             {
                 String[]  fields = new String[classFields.length];
                fieldInfo info;

                  for (int i = 0; i < classFields.length; i++)
                  {
                        info = classFields[i];

                        if (info != null)
                        {
                          fields[i] = info.getField();
                        }
                  }
                 return fields;
            }
            else
             {
                 return new String[]{};
             }

    }


  /** Print the class fields
    */
  void pr() {
    if (classFields != null) {
      fieldInfo info;

      for (int i = 0; i < classFields.length; i++) {
	info = classFields[i];
	System.out.print("   ");
	if (info != null) {
	  info.pr();
	}
	else {
	  System.out.println("\nclassFieldSec: pr - null at classFields[" + i + "]");
	}
      }
    }
  } // pr

} // classFieldSec



/**

   The fields of the methodInfo structure are the same as those of the
   fieldInfo structure.  However, the semantics of the two structures
   differ enough that they are implemented by two different objects.
   For example, in the case of the fieldInfo structure only the
   constValueAttr is allowed.  The attributes that are allowed for a
   methodInfo attribute are:

      <ul>
      <li>
      Code
      </li>
      <li>
      Exceptions
      </li>
      <li>
      Synthetic
      </li>
      <li>
      Deprecated
      </li>
      </ul>

<p>
   Allowed access modifiers for a method are:

      <ul>
      <li>
      private
      </li>
      <li>
      protected
      </li>
      <li>
      public
      </li>
      </ul>

<p>
Other modifiers are
 
<ul>
      <li>
      abstract
      </li>
      <li>
      final
      </li>
      <li>
      native
      </li>
      <li>
      static
      </li>
      <li>
      strict
      </li>
      <li>
      synchronized
      </li>
<ul>

<pre>
   method_info {
     u2 access_flags;
     u2 name_index;        (Utf8 in the constant pool)
     u2 descriptor_index;  (Utf8 in the constant pool)
     u2 attributes_cnt;
     attribute_info attributes[ attributes_cnt ];
   }
</pre>
<p>
   There should not be more than one code attribute for a method.
   Code attributes are special in that they provide extra information
   about the method in the code attribute attribute table.  For example,
   the code attribute contains information about the local variables
   in the method.

  @author Ian Kaplan
 */
class methodInfo extends dataRead implements access_and_modifier_flags {
  int access_flags = 0;
  constUtf8 name = null;
  String constructorName = null;
  constUtf8 descriptor = null;
  attrInfo attributes[] = null;
  codeAttr codeAttribute = null;


  methodInfo( DataInputStream dStream, constPool constPoolSec ) {
    int name_index;
    int desc_index;
    int attr_cnt;
    constBase obj;

    access_flags = readU2( dStream );
    name_index   = readU2( dStream );
    desc_index   = readU2( dStream );
    attr_cnt     = readU2( dStream );
    
    obj = constPoolSec.constPoolElem( name_index );
    if (obj != null && obj instanceof constUtf8) {
      name = (constUtf8)obj;
    }

    obj = constPoolSec.constPoolElem( desc_index );
    if (obj != null && obj instanceof constUtf8) {
      descriptor = (constUtf8)obj;
    }

    // methodInfo objects differ from fieldInfo objects in
    // the allowed attributes.
    if (attr_cnt > 0) {
      attributes = new attrInfo[ attr_cnt ];
      for (int i = 0; i < attr_cnt; i++) {
	attributes[i] = attrFactory.allocAttr( dStream, constPoolSec );
	if (attributes[i] instanceof codeAttr)
	  codeAttribute = (codeAttr)attributes[i];
      } // for
    }
  } // methodInfo constructor


  /**
    Class constructors have the internal name "<init>".  Return
    true if this is the name of the current constructor.
   */
  public boolean isConstructor() {
    return (name.getString().compareTo("<init>") == 0);
  }


  /**
    Set the name of the constructor name
    */
  public void setConstructorName( String newName ) {
    constructorName = newName;
  }


  /**
    @return return the constructor name
    */
  public String getConstructorName() {
    return constructorName;
  }


  /**
    If there are synthetic or deprecated attributes in
    the method attribute table, return a comment string
    showing the appropriate attribute.  Otherwise return
    null.
    */
  private String commentString() {
    String str = null;

    if (attributes != null) {
      for (int i = 0; i < attributes.length; i++) {
	if (attributes[i] instanceof synthAttr) {
	  if (str == null)
	    str = "// ";
	  else
	    str = str + ", ";
	  str = str + "synthetic";
	}
	if (attributes[i] instanceof deprecAttr) {
	  if (str == null)
	    str = "// ";
	  else
	    str = str + ", ";
	  str = str + "deprecated";
	}
      }
    }
    return str;
  } // commentString


  /**
    Return a string containing "\n     throws <exception-class-list>"
    if the method has associated exceptions.  Otherwise return
    null.
<p>
    The exception attribute has a table of exceptions that are
    associated with the class method.  So there should only
    be one exceptions attribute associated with a given
    method.
    */
  private String exceptionString() {
    String str = null;
    
    if (attributes != null) {
      String exceptList = null;  // exception list

      for (int i = 0; i < attributes.length; i++) {
	if (attributes[i] instanceof exceptAttr) {
	  exceptAttr eAttr;
	  constClass_or_String exceptTab[];

	  eAttr = (exceptAttr)attributes[i];
	  exceptTab = eAttr.getExceptTab();
	  if (exceptTab != null && exceptTab.length > 0) {
	    String className;

	    // build exception list
	    for (int j = 0; j < exceptTab.length; j++) {
	      className = exceptTab[j].getString();
	      className = objNameFormat.toDotSeparator( className );
	      if (exceptList == null) {
		exceptList = className;
	      }
	      else {
		exceptList = exceptList + ", " + className;
	      }
	    } // for
	  }
	  break;
	}
      } // for
      if (exceptList != null) {
	str = "\n     throws " + exceptList;
      }
    }
    return str;
  } // exceptionString


  /**
    The methodTypes class is used by the methodTypes method
    to return the argument list string and the return type
    that are built from the method descriptor.
<p>
    A StringBuffer object is used for argList with the thought
    that appending to a string buffer may cause less memory
    garbage and be faster.  But this could be a mistaken idea.

    */
  class methodTypes {
    public StringBuffer argList;
    public String returnType;

    methodTypes() {
      argList = new StringBuffer();
    }
  } // methodTypes


  /**
    Decode the method descriptor and return the argument list
    and return type.
<p>
    The method descriptor describes the arguments to the method.
    The format for the method descriptor is:

      '(' (type-descriptor)* ')' return-type

    The method argument descriptor starts with '(' and consists
    of zero or more type descriptions.  Types are described by
    either a single letter (see javad.util.typeDesc class) or
    an object path terminated by a semicolon.  In the same way,
    the return type will either be an object path, an atomic
    type or "void", which is indicated by the letter 'V'.
    See section 4.3.3 of the JVM Spec.

    Note that the character that denotes an object path start is
    "L".  So lets say we have the following method declaration:

      myObj foo(int i, double d, long l, barObj b, char c) {...}

    This method would have the method descriptor

       (IDJLmypkg/barObj;C)Lmypkg/myObj;

    The descriptor arguments are enclosed by parenthesis.  The
    IDJ represents the int, double and long arguments.  The barObj
    argument is represented by the "Lmypkg/barObj;", where 'L'
    denotes the start of the object path and ';' terminates the
    path.

   */
  private methodTypes decodeMethodDesc( String methodDesc ) {
    methodTypes types = null;

    if (methodDesc != null) {
      int len = methodDesc.length();
      int ix = 0;

      types = new methodTypes();
      
      if (methodDesc.charAt(ix) == '(') {
	String typeName;
	char ch;
	int dimCnt;  // array dimension count
	boolean first_type = true;

	for (ix++; ix < len && methodDesc.charAt(ix) != ')'; ix++) {
	  ch = methodDesc.charAt(ix);
	  dimCnt = 0;

	  // pick up the array dimension description
	  while (ch == '[') {
	    dimCnt++;
	    ix++;
	    ch = methodDesc.charAt(ix);
	  }

	  if (ch == 'L') { // The type is a class (represented by a class path)
	    String objName;
	    int jx;

	    ix++; // ix is now the String index following the 'L'
	    // find the string index of the next ';' which terminates
	    // the object name
	    for (jx = ix; jx < len && methodDesc.charAt(jx) != ';'; jx++)
	      /* nada */;

	    objName = methodDesc.substring(ix, jx);
	    typeName = objNameFormat.toDotSeparator( objName );
	    ix = jx;  // ix now points to the ';'
	  } 
	  else if (typeDesc.isTypeChar( ch )) {
	    // the character is a base type character, so get the
	    // corresponding type name.
	    typeName = typeDesc.charToType( ch );
	  }
	  else {
	    errorMessage.errorPrint("decodeMethodDesc: unexpected char");
	    typeName = null;
	  }
	  // add the array dimensions to the end of the type
	  for (int i = 0; i < dimCnt; i++) {
	    typeName = typeName + "[]";
	  }
	  if (typeName != null) {
	    if (first_type) {
	      first_type = false;
	      types.argList.append( typeName );
	    }
	    else {
	      types.argList.append(", ");
	      types.argList.append( typeName );
	    }
	  }
	} // for
	if (ix < len && methodDesc.charAt(ix) == ')') {
	  // get the return type
	  ix++;

	  ch = methodDesc.charAt(ix);
	  if (ch == 'V') // return type is void
	    types.returnType = "void";
	  else if (ch == 'L') {
	    ix++;
	    typeName = methodDesc.substring( ix );
	    types.returnType = objNameFormat.toDotSeparator( typeName );
	  } 
	  else if (typeDesc.isTypeChar( ch )) {
	    types.returnType = typeDesc.charToType( ch );
	  }
	}
	else
	  errorMessage.errorPrint("decodeMethodDesc: ')' expected");
      }
      else
	errorMessage.errorPrint("decodeMethodDesc: '(' expected");
    }

    return types;
  } // decodeMethodDesc


  /**
    This method is passed the string that should be used
    to terminate the method declaration.  If there are local
    variable declarations in the codeAttribute table (e.g., the
    class was compiled with debug) the terminal string will
    be " {".  If there are no local variables or the class was
    compiled without debug the terminal character will be ";"

    @Return a string representing the method declaration
    */
  String methodString(String terminalStr ) {
    StringBuffer methodBuf = new StringBuffer();

    // modifiers (e.g., private, native, etc...).  May be null
    String modifiers = accString.toString( access_flags, true );
    methodTypes types = decodeMethodDesc( descriptor.getString() );

    // add any method modifiers (e.g., private, protected, ...)
    if (modifiers != null) {
      methodBuf.append( modifiers);
      methodBuf.append(" ");
    }

    // Add the return type and method name.  All methods have a type 
    // (if no return value, then void) do this should never be null.
    methodBuf.append( types.returnType);
    methodBuf.append( " " );
    if (constructorName == null)
      methodBuf.append( name.getString() );
    else
      methodBuf.append( constructorName );
    methodBuf.append( "(" );

    if (types.argList != null) {
      methodBuf.append( types.argList.toString() );
    }

    methodBuf.append(")");

    // get synthetic/deprecated comment
    String cmntStr = commentString();
    // get any exceptions
    String excptStr = exceptionString();

    if (excptStr == null) { // there are no exceptions
      methodBuf.append( terminalStr );
    }

    // Append the a comment re. synthetic or deprecated attributes
    if (cmntStr != null) {
      methodBuf.append(" ");
      methodBuf.append( cmntStr );
    }

    // Append the exception part of the method declaration.
    // If the exception exists there will be a carriage return
    // and an indent placed between the method declaration and
    // the exception declaration.
    if (excptStr != null) {
      methodBuf.append( excptStr );
      methodBuf.append( terminalStr );
    }

    return methodBuf.toString();
  } // methodString


  /**
    Print method declaration (e.g., private int foobar( int x )
    and any local variables.

   */
  void pr() {
    final String indent1 = "   ";
    final String indent2 = "     ";
    boolean printed_header = false;

    if (codeAttribute != null) {      // print local variables
      Vector localVarVec = codeAttribute.getLocalVarVec();

      if (localVarVec != null) {
	int len = localVarVec.size();
	String localVar;

	if (len > 0) {
	  System.out.println(indent1 + methodString( " {" ) );
	  printed_header = true;
	}
       
	for (int i = 0; i < len; i++) {
	  localVar = (String)localVarVec.elementAt( i );
	  System.out.println( indent2 + localVar);
	}
      }
    }
    if (! printed_header)
      System.out.println(indent1 + methodString( ";" ) );
    else
      System.out.println( indent1 + "}" );
  } // pr

} // methodInfo


/**

   Read the class method section and build representative
   classes.  The class method section is composed of a 
   method count and array of method_info data structures.

<pre>
      u2 method_cnt;
      method_info methods[ method_cnt ];
</pre>

  @author Ian Kaplan
 */
class classMethodSec extends dataRead {
  methodInfo classMethods[];

  classMethodSec( DataInputStream dStream, 
		  constPool constPoolSec,
		  String className ) {
    int methodCnt;

    methodCnt = readU2( dStream );
    if (methodCnt > 0) {
      classMethods = new methodInfo[ methodCnt ];
    }

    for (int i = 0; i < methodCnt; i++) {
      classMethods[i] = new methodInfo( dStream, constPoolSec );
      if (classMethods[i].isConstructor())
	classMethods[i].setConstructorName( className );
    }
  } // classMethodSec constructor

    public ArrayList getMethods()
    {
        if (classMethods != null)
        {
            ArrayList methods = new ArrayList();
              for (int i = 0; i < classMethods.length; i++)
              {
                  if(!classMethods[i].isConstructor())
                  {
                    methods.add(classMethods[i].methodString(""));
                  }
              }
            return methods;
        }
        else
        {
            return new ArrayList();
        }
    }

    public ArrayList getConstructors()
    {
        if (classMethods != null)
        {
            ArrayList methods = new ArrayList ();
              for (int i = 0; i < classMethods.length; i++)
              {
                  if(classMethods[i].isConstructor())
                  {
                    methods.add(classMethods[i].methodString(""));
                  }
              }
            return methods;
        }
        else
        {
            return new ArrayList();
        }
    }

  void pr() {
    if (classMethods != null) {
      // print a blank line between the class members and the methods
      System.out.println();
      for (int i = 0; i < classMethods.length; i++) {
	classMethods[i].pr();
      }
    }
  } // pr

} // classMethodSec




/** 

 The classAttrSec object contains the attribute table that ends the
 class file.  The only attributes that appear in this section are the
 SourceFile attribute (srcFileAttr object), the InnerClasses attribute
 or the Depreciated attribute.

 */
class classAttrSec extends dataRead {
  attrInfo classAttrTab[] = null;

  classAttrSec( DataInputStream dStream, constPool constPoolSec ) {

    int numAttr = readU2( dStream );
    
    if (numAttr > 0) {
      classAttrTab = new attrInfo[ numAttr ];
      for (int i = 0; i < numAttr; i++) {
	classAttrTab[i] = attrFactory.allocAttr( dStream, constPoolSec );
      }
    }
  } // classAttrSec

  /** 

    Return the name of the source file that generated the class file.
    There should only be one source file in the ClassFile attribute
    table.

    @return a String for the source file name.

   */
  String getSrcFileName() {
    String name = null;

    if (classAttrTab != null) {
      for (int i = 0; i < classAttrTab.length; i++) {
	if (classAttrTab[i] instanceof srcFileAttr) {
	  srcFileAttr src;

	  src = (srcFileAttr)classAttrTab[i];
	  name = src.getFileName();
	  break;
	}
      } // for
    }
    
    return name;
  } // getSrcFileName

} // classAttrSec





/**

  The classFile object contains the information from a
  single Java class file.  The class file format is
  described in The Java Virtual Machine Specification,
  Second Edition, Lindholm and elin, Addison and Westley

<p>
  A class file contains a single ClassFile structure: 

<pre>
    ClassFile {
        u4 magic;
        u2 minor_version;
        u2 major_version;
        u2 constant_pool_count;
        cp_info constant_pool[constant_pool_count-1];
        u2 access_flags;
        u2 this_class;
        u2 super_class;
        u2 interfaces_count;
        u2 interfaces[interfaces_count];
        u2 fields_count;
        field_info fields[fields_count];
        u2 methods_count;
        method_info methods[methods_count];
        u2 attributes_count;
        attribute_info attributes[attributes_count];
    }
</pre>

*/
public class classFile {
  classFileHeader header = null;
  constPool classConstPool = null;
  classDeclSec classDecl = null;
  classFieldSec classFields = null;
  classMethodSec classMethods = null;
  classAttrSec classAttrs = null;
  String className = null;


  /**

     classFile constructor.

<p>
     The classFile constructor allocates a set of objects
     which corresponds to the various sections of the Java
     class file.  Each of these objects reads its own section
     and builds any data structures needed (e.g., tables) to
     represent the information.

   */
  public classFile(DataInputStream dStream) {

    // u4 magic;
    // u2 minor_version;
    // u2 major_version;
    header = new classFileHeader( dStream );

    // u2 constant_pool_count;
    // cp_info constant_pool[constant_pool_count-1];
    classConstPool = new constPool( dStream );

    // u2 access_flags;
    // u2 this_class;
    // u2 super_class;
    // u2 interfaces_count;
    // u2 interfaces[interfaces_count];
    classDecl = new classDeclSec( dStream, classConstPool );

    // u2 fields_count;
    // field_info fields[fields_count];
    classFields = new classFieldSec(dStream, classConstPool);

    className = classDecl.getClassName();

    // u2 methods_count;
    // method_info methods[methods_count];
    classMethods = new classMethodSec(dStream, classConstPool, className );

    // u2 attributes_count;
    // attribute_info attributes[attributes_count];
    classAttrs = new classAttrSec(dStream, classConstPool);
  } // classFile

  /**
    Print the class file in a source format resembling
    Java.

    */
  public void pr() {
    String srcFile;

    srcFile = classAttrs.getSrcFileName();
    if (srcFile != null) {
      System.out.println("Compiled from " + srcFile + "\n");
      classDecl.pr();
      classFields.pr();
      classMethods.pr();
      System.out.println(" } // " + className + "\n");
    }
  } // pr


    public String  getClassModifiers()
    {
        return classDecl.getModifiers();
    }

    public String getSuperClassName()
    {
        return classDecl.getSuperClassName();
    }

    public String[] getInterfaces()
    {
        return classDecl.getInterfacesNames();
    }

    public String[] getFields()
    {
        return classFields.getFields();
    }

    public ArrayList getMethods()
    {
        return classMethods.getMethods();
    }

    public ArrayList getConstructors()
    {
        return classMethods.getConstructors();
    }

    public String getClassName()
    {
        return classDecl.getClassName();
    }

} // classFile
