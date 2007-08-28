package org.jarexplorer;

import javad.classfile.classFile;

import java.util.ArrayList;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * Constains the information about a class in a jar. Allows for two ways of creation of objects of this type: lightweith and heavyweight.
 * The lighweight is created with a constructopr, and does not contain any more information that passed to a
 * constructor. The heavyweight is created with a {@link #createFully(String, String)} method, and will contain all information
 * regarding a class.
 * 
 * @author Greg.Tatham
 * @author  Igor Polevoy
 */
public class ClassInformation
{
    private ArrayList constructors = new ArrayList();
    private String[] fields = new String[]{};
    private String modifiers = "";
    private String superclass = "";
    private String[] interfaces = new String[]{};
    private ArrayList methods = new ArrayList();


    private String jarFileName;
    private String classPath;

    /**
     * Creates a lightweightstyle instance - no actual class information, just class name and a jar file.
     * Use {@link ClassInformation#createFully(String, String)} to construct a fully filled object. The latter
     * operation is expoensive (as expected)
     *  
     * @param jarName - name of a jar file (fully qualified)
     * @param classPath - name of class (really a resource, includiong a ".class")
     */
    public ClassInformation(String jarName, String classPath)
    {
        this.jarFileName = jarName;
        this.classPath = classPath;
    }

    /**
     * Creates a fully filled instance of {@link ClassInformation} by parsing bytecode.
     *
     * @param jarName - name of jar file
     * @param className - name of class (in format: pack1/pack2/pack3/ClassName.class)
     * @return instance of {@link ClassInformation} filled with all values parsed from
     * bytecode
     * @throws IOException - thrown in case there is a problem reading bytecode
     */
    public static ClassInformation createFully(String jarName, String className) throws IOException
    {
        URL u = new URL("jar:file:" + jarName + "!/" + className);
        InputStream in = u.openConnection().getInputStream();
        BufferedInputStream bin = new BufferedInputStream(in);
        DataInputStream din = new DataInputStream(bin);

        ClassInformation classInfo = new ClassInformation(jarName, className);

        if (className.endsWith(".class"))
        {
            classFile classFile = new classFile(din);
            classInfo.setModifiers(classFile.getClassModifiers());
            classInfo.setConstructors(classFile.getConstructors());
            classInfo.setFields(classFile.getFields());
            classInfo.setInterfaces(classFile.getInterfaces());
            classInfo.setMethods(classFile.getMethods());
            classInfo.setSuperclass(classFile.getSuperClassName());
        }


        return classInfo;

    }


    public String getJarFileName()
    {
        return jarFileName;
    }



    public String getClassPath()
    {
        return classPath;
   }


    public String getClassName()
    {
        //need to replace slashes with dots and remove .class
        String tmp = classPath.replace('/', '.');
        tmp = tmp.substring(0, tmp.lastIndexOf('.'));
        return tmp;
    }


    public String getKey()
    {

        return getJarFileName() + "!" + getClassPath();
    }


    public ArrayList getMethods()
    {


        return methods;
    }

    public ArrayList getConstructors()
    {


        return constructors;
    }

    public String[] getFields()
    {

        return fields;
    }

    public String getModifiers()
    {
        return modifiers;
    }

    public void setModifiers(String modifiers)
    {
        this.modifiers = modifiers;
    }

    public String getSuperclass()
    {
        return superclass;
    }

    public void setSuperclass(String superclass)
    {
        this.superclass = superclass;
    }

    public String[] getInterfaces()
    {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces)
    {
        this.interfaces = interfaces;
    }

    public void setConstructors(ArrayList constructors)
    {
        this.constructors = constructors;
    }

    public void setFields(String[] fields)
    {
        this.fields = fields;
    }

    public void setMethods(ArrayList methods)
    {
        this.methods = methods;
    }

    public String toString()
    {
        return getJarFileName() + " : " + getClassPath();
    }
}
