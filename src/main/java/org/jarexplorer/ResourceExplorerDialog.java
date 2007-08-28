package org.jarexplorer;

import org.jarexplorer.CenteredDialog;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.io.*;


/**
 * This dialog will show resources, such as HTML, text files and images (JPEG, GIF, PNG).
 * It will show contents of a resource read from a jar file. Text resoutrce view alows to
 * switch font from monospaced to default font. Property files are easier to read with monospaced font. HTML view
 * renders HTML as in browser, but also provides a second tab to see HTML source.
 *
 * @author Igor Polevoy
 */
public class ResourceExplorerDialog extends CenteredDialog
{
    private JCheckBox monospacedCB;
    private JTextArea textArea;
    private Font font;

    /**
     * Creates a dialod, and reads a resource in the process.
     *
     * @param owner        - top level window
     * @param jarFileName  - name of jar file
     * @param resourceName - internal path to resource
     * @throws IOException - thrown in case there is a problem reading the resource.
     */
    public ResourceExplorerDialog(Frame owner, String jarFileName, String resourceName) throws IOException
    {
        super(owner, jarFileName + ":" + resourceName, false);

        getContentPane().setLayout(new BorderLayout());

        if (resourceName.toLowerCase().endsWith(".html"))
        {
            buildForHTML(jarFileName, resourceName);
        }
        else if(isImage(resourceName))
        {
            buildForImage(Util.readResourceAsBytes(jarFileName, resourceName));
        }
        else
        {
            buildForSimpleResource(Util.readResourceAsString(jarFileName, resourceName));
        }

        //build south panel
        JPanel southPanel = new JPanel();
        JButton closeB = new JButton("Close");
        southPanel.add(closeB);
        closeB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        //build main panel

        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Builds a center panelel for text resource
     *
     * @param resource content of resourse (property file, manifest, etc.)
     */
    private void buildForSimpleResource(String resource)
    {
        //north panel - toolbar
        monospacedCB = new JCheckBox("Monospaced Font");
        monospacedCB.setSelected(true);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(monospacedCB, BorderLayout.WEST);
        monospacedCB.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                if (monospacedCB.isSelected())
                {
                    textArea.setFont(new Font("Monospaced", font.getStyle(), font.getSize()));
                }
                else
                {
                    textArea.setFont(new Font("Default", font.getStyle(), font.getSize()));
                }
            }
        });

        //center panel
        textArea = new JTextArea(resource);
        font = textArea.getFont();
        textArea.setFont(new Font("Monospaced", font.getStyle(), font.getSize()));
        textArea.setEditable(false);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    /**
     *
     * @param jarFileName name of jar file (fully qualified)
     * @param resourceName path to resource within the jar file
     * @throws IOException
     */
    private void buildForHTML(String jarFileName, String resourceName) throws IOException
    {
        JEditorPane htmlPane = new JEditorPane(new URL("jar:file:" + jarFileName + "!/" + resourceName));
        htmlPane.setEditable(false);
        JTextArea sourceArea = new JTextArea(Util.readResourceAsString(jarFileName, resourceName));
        sourceArea.setEditable(false);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("HTML", new JScrollPane(htmlPane));
        tabbedPane.add("Source", new JScrollPane(sourceArea));
        sourceArea.setFont(new Font("Monospaced", Font.PLAIN, sourceArea.getFont().getSize() ));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Builds UI for display of image.
     *
     * @param image image content.
     */
    private void buildForImage(byte[] image)
    {
        JLabel l = new JLabel();
        Icon icon = new ImageIcon(image);
        l.setIcon(icon);
        JPanel center = new JPanel();
        center.add(l);
        getContentPane().add(new JScrollPane(center), BorderLayout.CENTER);
    }

    /**
     * Checks if resource is image. Test is done based on extension only.
     *
     * @param selectedValue - path to resource
     * @return true is extension is: gif, jpg, jpeg, png (case insensitive)
     */
    private boolean isImage(String selectedValue)
    {
        String tmp = selectedValue.toLowerCase();
        return tmp.endsWith(".gif") || tmp.endsWith(".jpg") || tmp.endsWith(".jpeg")  || tmp.endsWith(".png");
    }
}