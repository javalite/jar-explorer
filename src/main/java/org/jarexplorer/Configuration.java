package org.jarexplorer;


import javax.swing.*;
import java.beans.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *   This class presents interface to configurational parameters to application.
 *   The parameters are persistent in  file. Parameter changes are propagated to
 *   registered property change listeners. This class is a Singleton.
 *   Before any usage of this class a runtime must run the <code>setFileName()</code> method
 *
 *   @author Igor Polevoy
 */
public class Configuration implements PropertyChangeListener
{

    //property file name where properties are stored
    private static String fileName;
    //this class is a singleton
    private static Configuration instance;
    //reference to Properties object that holds all properties.
    private static Properties props;
    //listeners interested in change of properties.
    private VetoableChangeSupport listeners;

    /**
     *   Private constructor. Singleton.
     *
     *
     */
    private Configuration()
    {
        listeners = new VetoableChangeSupport(this);
    }

    public List<String> getDefaultExtensions(){
        return Arrays.asList("jar", "zip", "war", "ear", "rar");
    }

    /**
     *  Adds a PropertyChangeListener to this object.
     *  All PropertyChangeListener(s) are notified when a property is changed
     *
     * @param   listener  PropertyChangeListener interested in property change events.
     */
    public void addVetoableChangeListener(VetoableChangeListener listener)
    {
        listeners.addVetoableChangeListener(listener);
    }

    /**
     *  Sets file name for persistence
     *
     * @param   fileName
     */
    public static void setFileName(String fileName, String applicationName)
    {
        try
        {
            Configuration.fileName = fileName;
            props = new Properties();
            FileInputStream fin = new FileInputStream(fileName);
            props.load(fin);
            fin.close();
        }
        catch (Exception e)
        {
            FileOutputStream out;
            try
            {
                out = new FileOutputStream(Configuration.fileName);
                out.write(new StringBuffer().append("#").append(applicationName).append(" configuration file").toString().getBytes());
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }


    /**
     *  Returns one and only one instance of this class
     *
     * @return     one and only one instance of this class
     */
    public static Configuration getInstance()
    {
        if(fileName == null)
        {
            throw new RuntimeException("need to set configuration file name first");
        }
        synchronized (Configuration.class)
        {
            if (instance == null)
            {
                instance = new Configuration();
            }
        }
        return instance;
    }


    /**
     *  Returns a named property
     *
     * @param   name  name of property
     * @return     property value
     */
    public static String getProperty(String name)
    {
        return getProperty(name, null);
    }


    /**
     *  Returns a named property
     *
     * @param   name  property name
     * @param   defaultValue  default value - returned in case property not found
     * @return     property value or default value if property not found
     */
    public static String getProperty(String name, String defaultValue)
    {
        return props.getProperty(name, defaultValue);
    }

    public static boolean getBooleanProperty(String name)
    {
        String val = Configuration.props.getProperty(name);
        if (val == null)
        {
            try
            {
                setProperty(name, "false");
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        val = props.getProperty(name);
        if (val.equalsIgnoreCase("true"))
        {
            return true;
        }
        else if (val.equalsIgnoreCase("false"))
        {
            return false;
        }
        else
        {
            throw new RuntimeException("Illegal value of boolean: " + val);
        }
    }

    /**
     * Returns clone of properties
     * @return
     */
    public Properties getProperties()
    {
        return (Properties) props.clone();
    }


    public void removeProperty(String name)
    {
        props.remove(name);
        try
        {
            Configuration.saveFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     *  Sets properties. This method replaces properties with the same name. Saves properties to file.
     *
     * @param   properties  properties to be set
     * @exception   java.io.IOException
     */
    public static void setProperties(Properties properties)
    {
        //todo should I fire events here? who knows...
        //to do it properly, it has to set one property at the time
        //this would fire property change events
        props = properties;
        try
        {
            saveFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  Sets property. Saves properties to file. Fires PropertyChangeEvent.
     *
     * @param   name property name
     * @param   value  property value
     * @param   source  - object that initiates property change. Nedded to propagate it with
     *  PropertyChangeEvent
     * @exception   java.io.IOException
     */
    public void setProperty(String name, String value, Object source)
    {
        Object old = props.getProperty(name);
        
        
        try
        {
            saveFile();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
        
        try
        {
            listeners.fireVetoableChange(new PropertyChangeEvent(source, name, old, value));
        }
        catch (PropertyVetoException e)
        {
            return;// do not want to set the property
        }
        props.setProperty(name, value);
        

    }

    /**
     *  Sets a property. Saves properties to file. Fires PropertyChangeEvent.
     *
     * @param   name  property name
     * @param   value  property value
     * @exception   java.io.IOException
     */
    public static void setProperty(String name, String value)
    {
        
        Object old = Configuration.props.getProperty(name);
        try
        {
            Configuration.getInstance().listeners.fireVetoableChange(name, old, value);
            Configuration.props.setProperty(name, value);
            saveFile();
        }
        catch (PropertyVetoException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     *  Saves properties to file
     *
     * @exception   java.io.IOException
     */
    private static void saveFile() throws IOException
    {
        FileOutputStream fout = new FileOutputStream(fileName);
        props.store(fout, "Do not put comments into this file - it is re-generated  " + new java.util.Date());
        fout.flush();
        fout.close();
    }


    /**
     *  Removes properties with a prefix
     *
     * @param   prefix  prefix of properties to be removed
     * @exception   java.io.IOException
     */
    private void removeProperties(String prefix) throws IOException
    {
        Enumeration names = props.propertyNames();
        Properties tempProps = (Properties) props.clone();
        String name;
        while (names.hasMoreElements())
        {
            name = (String) names.nextElement();
            if (name.startsWith(prefix))
            {
                tempProps.remove(name);
            }
        }


        props = tempProps;
        saveFile();


    }

    /**
     *  Implementation of PropertyChangeListener interface
     *
     * @param   e
     */
    public void propertyChange(java.beans.PropertyChangeEvent e)
    {
        try
        {
            if (e.getNewValue() instanceof Properties)
            {
                setProperties((Properties) e.getNewValue());
            }
            else if (e.getPropertyName().equals("remove"))
            {
                removeProperties(e.getNewValue().toString());
            }
            else if (e.getNewValue() instanceof String)
            {
                setProperty(e.getPropertyName(), e.getNewValue().toString());
            }
        }
        catch (Exception ex)
        {
            GUIUtil.messageBoxWithDetails("Configuration Error:", ex, JOptionPane.ERROR_MESSAGE);
        }

    }

    public int getIntProperty(String name, int defaultValue)
    {
        String val = props.getProperty(name);
        if (val == null)
        {
            try
            {
                setProperty(name, Integer.toString(defaultValue));
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        val = props.getProperty(name);

        int property;
        try
        {
            property = Integer.parseInt(val);
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("Illegal value of integer: " + val);
        }
        return property;
    }
}
