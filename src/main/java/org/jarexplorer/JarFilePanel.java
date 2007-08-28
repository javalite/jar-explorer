package org.jarexplorer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;


/**
 * This panel will display the jar files found under a path
 *
 * @author igor jarexplorer
 */
public class JarFilePanel extends JPanel {

    /**
     * this is a list that will contain String objects with full paths to the jar files
     */
    private JList jarGUIList = new JList();


    /**
     * No-arg constructor
     */
    public JarFilePanel()
    {
        setLayout(new BorderLayout());
        add(new JScrollPane(jarGUIList), BorderLayout.CENTER);
        setBorder(new TitledBorder("Jar File List"));
       // jarGUIList.setModel(model);
        jarGUIList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    /**
     * Sets a list of jar file paths to this panel
     *
     * @param jarList - list of <code>java.lang.String</code> objects. Each string is a fully quialified path to a
     * jar file
     */
    public void setJarList(ArrayList jarList)
    {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < jarList.size(); i++) {
            Object jarName =  jarList.get(i);
            model.addElement(jarName);
        }

        jarGUIList.setModel(model);
        repaint();
    }

    /**
     * Returns selected jar file.
     *
     * @return string representation of a full path to a jar file. WIll return null if nothing is selected \.
     */
    public String getSelectedJar()
    {
        Object selected = jarGUIList.getSelectedValue();
        return selected == null? null:selected.toString();
    }

    /**
     * Allows registration of a listener. This is necessary in order to display contents of selected jar on another
     * panel.
     *
     * @param listener
     */
    public void addSelectionListener(ListSelectionListener listener)
    {
        jarGUIList.getSelectionModel().addListSelectionListener(listener);
    }

    public void clean()
    {
        jarGUIList.setModel(new DefaultListModel());
    }
}
