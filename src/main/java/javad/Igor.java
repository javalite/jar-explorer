package javad;

import javad.classfile.classFile;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;


/**
 * This is a simple class that shows how to use javad with added API.
 * 
 * @author Igor Polevoy
 */
public class Igor
{
    public static void main(String[] args)
    {

        try
        {
            FileInputStream fin = new FileInputStream("C:\\gwt\\build\\web\\WEB-INF\\classes\\sample\\client\\Sample$TableAsyncCallback.class");
            BufferedInputStream bin = new BufferedInputStream(fin);
            DataInputStream din = new DataInputStream(bin);


            classFile classFile = new classFile(din);
            System.out.println("Class Modifiers: " + classFile.getClassModifiers());
            System.out.println("Class Name: " + classFile.getClassName());
            System.out.println("Class Superclass: " + classFile.getSuperClassName());
            String [] interfaces = classFile.getInterfaces();
            
            for (int i = 0; i < interfaces.length; i++)
            {
                String anInterface = interfaces[i];
                System.out.println("Interface: " + anInterface);
            }

            String[] fields = classFile.getFields();
            for (int i = 0; i < fields.length; i++)
            {
                String field = fields[i];
                System.out.println("Field: " + field);
            }

            ArrayList methods = classFile.getMethods();

            for (int i = 0; i < methods.size(); i++)
            {
                String method = (String) methods.get(i);
                System.out.println("Method: " + method);
            }


            ArrayList constructors = classFile.getConstructors();
            for (int i = 0; i < constructors.size(); i++)
            {
                String constructor = (String) constructors.get(i);
                System.out.println("Constructor: " + constructor);
            }

            System.out.println("====================================================");

            classFile.pr();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
