package org.jarexplorer;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;


/**
 *	Utility class. Provides some useful methods, mostly for gui operations.
 * @author Igor Polevoy
 * <br/>
 * Date: Jan 3, 2003
 */
public class GUIUtil
{
    private static Frame frame;

    private static boolean canExit;
    private static HashMap cachMap = new HashMap();


    /**
     *  Sets canExit boolean. This property indicates wether application
     * should hide or do System.exit when user requests to exit. Applets should never call System.exit because it will cause a
     * hosting browser to exit. See setCanExit()
     * @param   canExit should be  true if Application, false if Applet
     */
    public static void setCanExit(boolean canExit)
    {
        GUIUtil.canExit = canExit;
    }

    /**
     *  Returns true if not running within applet environment. See setCanExit()
     * @return  true if not running within applet environment
     */
    public static boolean canExit()
    {
        return GUIUtil.canExit;
    }

    /**
     *  Sets main frame of the application
     * @param   f  main frame of the application
     */
    public static void setMainFrame(Frame f)
    {
        GUIUtil.frame = f;
    }

    /**
     *  Returns a main frame of the application. If setMainFrame() was not called, it creates a hidden frame and returns it.
     * @return    main frame of the application
     */
    public static Frame getMainFrame()
    {
        if (GUIUtil.frame == null)
        {
            GUIUtil.frame = new Frame();
        }
        return GUIUtil.frame;
    }

    /**
     *  Centers window on the screen
     * @param   window  window to be centered
     */
    public static void centerWindow(Window window)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = window.getSize();
        window.setLocation(screenSize.width / 2 - dialogSize.width / 2, screenSize.height / 2 - dialogSize.height / 2);
    }

    /**
     *  Sets a wait cursor (hour glass on Windows) on the component
     * @param   c  target component for cursor
     */
    public static void waitCursor(Component c)
    {
        c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    /**
     *  Sets default cursor on the component
     * @param   c  component for which the cursor needs to be set.
     */
    public static void defaultCursor(Component c)
    {
        c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Ask user whether it is OK to proceed. Displays a dialog box with message and two buttons: "Yes", "No".
     * @param   c  component ralative to which the gialog should be modal
     * @param   msg  message
     * @return     returns true if user presses "Yes", false if user presses "No".
     */
    public static boolean checkWithUser(Component c, String msg)
    {
        Object[] options = {"YES", "NO"};
        return (JOptionPane.showOptionDialog(c, msg, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                options, options[1]) == 0);
    }

    /**
     *  Positions a window so that it's left upper corner is at the same screen coodrinates as the left upper
     * corner of the base component
     * @param   base  base component
     * @param   window to be repositioned
     */
    public static void locateRelativeTo(Component base, Window window)
    {
        Point p = new Point(0, 0);
        SwingUtilities.convertPointToScreen(p, base);
        window.setLocation(p);
    }

    /**
     *	Returns an Image object given a file name. This method
     * assumes that the file is located in the same classpath with this class.
     * @param   fileName  file name relative to the location of this class on the claspath
     * @return     an Image object given a file name.
     * @throws java.lang.NullPointerException if cannot load Image
     */
    public static ImageIcon getImageIcon(String fileName)
    {
        Object o;
        if ((o = cachMap.get(fileName)) != null)
        {
            return (ImageIcon) o;
        }
        ImageIcon imageIcon = null;
        try
        {
            imageIcon = new ImageIcon(getResource(fileName));
        }
        catch (IOException e)
        {
            //these exception can only be thrown during development/debugging
            throw new NullPointerException(e.toString());
        }
        cachMap.put(fileName, imageIcon);
        return imageIcon;
    }

    /**
     *  Returns resource as a byte array.
     * @param   fileName  name of resource relative to the lication of this class
     * @return     resource relative to the location of this class
     * @exception   java.io.IOException  in case there was error reading from resource
     */
    public static byte[] getResource(String fileName) throws IOException
    {
        InputStream in = GUIUtil.class.getResourceAsStream(fileName);
        if (in == null)
        {
            throw new NullPointerException("Could not get open: " + fileName);
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        int x;
        while ((x = in.read()) != -1)
        {
            bout.write(x);
        }
        return bout.toByteArray();
    }

    /**
     * Pops error box. If system property <code>gui.debug=true</code>, it also
     * pops a log Window with stack trace of the exception.
     * @param parent a component to serve as a parent for a dialog box
     * @param title - string to be displayed on the title bar of dialog
     * @param text - text of the message
     */
    public static void errorBox(Component parent, String title, String text)
    {
        popErrorBox(parent, title, text);
    }

    /**
     * Pops error box. If system property <code>gui.debug=true</code>, it also
     * pops a log Window with stack trace of the exception t.
     * @param title - string to be displayed on the title bar of dialog
     * @param text - text of the message
     */
    public static void errorBox(String title, String text)
    {

        popErrorBox(getMainFrame(), title, text);
    }

    /**
     * Pops error box.
     * @param parent -
     * @param title - title of message box
     * @param text - text of message box
     */
    public static void popErrorBox(Component parent, String title, String text)
    {
        String[] options = {"OK"};
        JOptionPane.showOptionDialog(parent,
                text, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
    }

    public static String getStackTrace(Throwable e)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(stream));
        return stream.toString();
    }

    /**
     * Displays a message box with "OK" button and "Details" button.
     * The latter opens up a bottom pane with details text in it
     * Main frame set as a static value in this class is a parent of this
     * message. Exception's message is text and stack trace used as details
     *
     * @param title - title of message box
     * @param e exception
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */

    public static void messageBoxWithDetails(String title, Throwable e, int type)
    {
        messageBoxWithDetails(getMainFrame(), title, e.getMessage(), getStackTrace(e), type);
    }


    /**
     * Shows message box with details
     *
     * @param comp - instance of <code>java.awt.Component</code>
     * @param title title of the message box
     * @param e exception whic caused error condition
     */
    public static void messageBoxWithDetails(Component comp, String title, Throwable e)
    {
        messageBoxWithDetails(getParentWindow(comp), title, e.toString(), GUIUtil.getStackTrace(e), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns a parent <code>java.awt.Window</code> instance, new <code>java.awt.Frame</code>
     * if argument <code>java.awt.Component</code> has no ansestor window associated
     *
     * @param comp
     * @return
     */
    private static Window getParentWindow(Component comp)
    {
        Window parent = SwingUtilities.getWindowAncestor(comp);
        if(parent == null)
        {
            parent = new Frame();
        }
        return null;
    }


    /**
     * Shows message box with details
     *
     * @param parent - parent window
     * @param title title of the message box
     * @param e exception whic caused error condition
     */
    public static void messageBoxWithDetails(Window parent, String title, Throwable e)
    {
        messageBoxWithDetails(parent, title, e.getMessage(), GUIUtil.getStackTrace(e), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a message box with "OK" button and "Details" button.
     * The latter opens up a bottom pane with details text in it
     *
     * @param parent - parent of this message - must be either <code>java.awt.Frame</code> or <code>java.awt.Dialog</code>
     * @param title - title of message box
     * @param message - main message
     * @param details -  details text to be displayed in the bottom pane
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */
    public static void messageBoxWithDetails(Window parent, String title, String message, String details, int type)
    {
        Dialog parentD;
        Frame parentF;

        if (parent instanceof Dialog)
        {
            parentD = (Dialog) parent;
            DetailsMessageBox mb = new DetailsMessageBox(parentD, title, message, details, type);
            mb.setVisible(true);
        }
        else if (parent instanceof Frame)
        {
            parentF = (Frame) parent;
            DetailsMessageBox mb = new DetailsMessageBox(parentF, title, message, details, type);
            mb.setVisible(true);
        }
        else
        {
            throw new IllegalArgumentException("Parent component must be either 'java.awt.Frame' or 'java.awt.Dialog'");
        }
    }

    /**
     *  Pops an "info" dialog box wiht "OK" button
     * @param parent - parent
     * @param   title  title text
     * @param   text  text for to be displayed
     */
    public static void messageBox(Component parent, String title, String text)
    {
        String[] options = {"OK"};
        JOptionPane.showOptionDialog(parent,
                text, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    /**
     *  Pops an "info" dialog box wiht "OK" button
     * @param   title  title text
     * @param   text  text for to be displayed
     */
    public static void messageBox(String title, String text)
    {
        messageBox(getMainFrame(), title, text);
    }
}
