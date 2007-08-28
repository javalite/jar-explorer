package org.jarexplorer;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.IOException;


/**
 * Dialog displays licenses
 *
 * @author Igor Polevoy
 */
public class LicenseDialog extends CenteredDialog
{
    public LicenseDialog()
    {
        super(GUIUtil.getMainFrame(), "Licenses", true);
        JTabbedPane tabbedPane = new JTabbedPane();

        try
        {
            JEditorPane pane;
            tabbedPane.add("JarExplorer", new JScrollPane(pane = new JEditorPane(getClass().getResource("/license.html"))));
            pane.setEditable(false);
        }
        catch (Exception ignore){ignore.printStackTrace();}
        try
        {
            JEditorPane pane;
            tabbedPane.add("javad", new JScrollPane(pane = new JEditorPane("text/html", readResource("/javad/copyright.html"))));
            pane.setEditable(false);
        }
        catch (Exception ignore){ignore.printStackTrace();}


        getContentPane().add(tabbedPane);
        setSize(new Dimension(800, 600));

    }

    private String readResource(String resource) throws IOException
    {
        InputStream in = this.getClass().getResourceAsStream(resource);
        StringBuffer tmp = new StringBuffer(1024);
        for (int i = in.read(); i != -1; i = in.read())
        {
            tmp.append((char) i);
        }
        return tmp.toString();
    }
}
