package org.jarexplorer;

import org.jarexplorer.ClassInformation;
import org.jarexplorer.GUIUtil;
import org.jarexplorer.ResourceExplorerDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * This is a panel with a list box, which shows result of a search. It shows fully qualified paths to resources,
 * including inside the jar files.
 * @author Igor Polevoy
 */
public class SearchResultsPanel extends JPanel
{

    private JList resultList = new JList();
    private DefaultListModel model;
    private TitledBorder border;

    private JButton exploreB, cleanB;

    /**
     * Constructor
     */
    public SearchResultsPanel()
    {
        this.resultList.setModel(model = new DefaultListModel());
        setLayout(new BorderLayout());
        setBorder(border = new TitledBorder("Search Results"));
        add(new JScrollPane(resultList), BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.add(exploreB = new JButton("Explore"));
        exploreB.setEnabled(false);
        southPanel.add(cleanB = new JButton("Clean"));
        cleanB.setEnabled(false);
        add(southPanel, BorderLayout.SOUTH);
        addActionListeners();
    }

    /**
     * Wires listeners
     */
    private void addActionListeners()
    {
        //Construct popup menu
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem copyMI = new JMenuItem("Copy");
        copyMI.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                StringSelection stringSelection = new StringSelection(resultList.getSelectedValue().toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
            }
        });
        menu.add(copyMI);

        //this will popup a menu if there is something selected
        resultList.addMouseListener(new MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    exploreSelected();
                }
            }

            public void mousePressed(MouseEvent e)
            {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {

                    if (resultList.getSelectedValue() != null)
                    {
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        cleanB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                model.removeAllElements();
                SearchResultsPanel.this.repaint();
                border.setTitle("");
                cleanB.setEnabled(false);
            }
        });

        resultList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (resultList.getSelectedValue() == null)
                {
                    exploreB.setEnabled(false);
                }
                else if(!resultList.getSelectedValue().toString().endsWith("/"))
                {
                    exploreB.setEnabled(true);
                }
                else
                {
                    exploreB.setEnabled(false);
                }
            }
        });

        resultList.addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
               if(e.getKeyChar() == '\n')
                {
                    exploreSelected();
                }
            }
        });

        exploreB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                exploreSelected();
            }
        });
    }

    /**
     * will pop a corresponding dialog for a resource.
     */
    private void exploreSelected()
    {
        try
        {
            String selectedValue = resultList.getSelectedValue().toString();
            int index = selectedValue.lastIndexOf(":");


            String jarName = selectedValue.substring(0, index).trim();
            String resourceName = selectedValue.substring(index + 1).trim();


            if (selectedValue.endsWith(".class"))//show class
            {
                ClassInformation classInfo = ClassInformation.createFully(jarName, resourceName);
                ClassExplorerDialog explorerDialog = new ClassExplorerDialog(GUIUtil.getMainFrame(), classInfo);
                explorerDialog.pack();
                explorerDialog.setSize(new Dimension(800, 600));
                explorerDialog.setVisible(true);
            }
            else //show any other resource property file, manifest, etc.
            {
                ResourceExplorerDialog resourceExplorerDialog = new ResourceExplorerDialog(GUIUtil.getMainFrame(), jarName, resourceName);
                resourceExplorerDialog.pack();
                resourceExplorerDialog.setSize(new Dimension(800, 600));
                resourceExplorerDialog.setVisible(true);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            GUIUtil.messageBoxWithDetails("Error", ex, javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }


    /**
     * Sets results for this search panel.
     *
     * @param title   - title of hte results .
     * @param results - strings are expected.
     */
    public void setResults(String title, ArrayList results)
    {
        model.removeAllElements();
        border.setTitle(title);

        for (int i = 0; i < results.size(); i++)
        {
            Object o = results.get(i);
            model.addElement(o);
        }
        cleanB.setEnabled(true);
        repaint();
    }

    public void clean()
    {
        model = new DefaultListModel();
        resultList.setModel(model);
    }
}
